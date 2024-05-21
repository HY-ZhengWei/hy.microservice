package org.hy.microservice.common.heartbeat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.thread.Job;
import org.hy.common.thread.Jobs;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseViewMode;
import org.hy.microservice.common.heartbeat.task.ITask;
import org.hy.microservice.common.heartbeat.task.ITaskService;
import org.hy.microservice.common.heartbeat.task.ITaskX;





/**
 * 心跳的业务层
 * 
 * @author      ZhengWei(HY)
 * @createDate  2023-12-16
 * @version     v1.0
 */
@Xjava
public class HeartbeatService implements IHeartbeatService ,Serializable
{
    
    private static final long serialVersionUID = -6579154134709942316L;
    
    private static Logger $Logger = new Logger(HeartbeatService.class);
    
    
    
    @Xjava
    private IHeartbeatDAO                                  heartbeatDAO;
    
    @Xjava
    private IHeartbeatCache                                heartbeatCache;
    
    @Xjava(ref="MS_Common_Version")
    private Param                                          edgeVersion;
    
    @Xjava(ref="MS_Common_Heartbeat_CheckCount")
    private Param                                          heartbeatCheckCount;
                                                           
    /** 是否在运行中。同步锁 */
    private boolean                                        isRunning;
    
    /** 之前在运行的老任务XID集合。Map.key为XPull.xid, Map.value为刷新时间戳 */
    private Map<String ,Long>                              jobXIDs    = new HashMap<String ,Long>();
    
    /**
     * 认领的任务。
     * Map.key为ITask.webPushName 或 ITask.xsqlXID
     * Map.value为最早认领时间
     */
    private Map<String ,String>                            claimTasks  = new HashMap<String ,String>();
    
    /** 认领任务的XPull对象集合 */
    private List<ITaskX>                                   claimTaskList;
    
    /** 服务所在的主机IP地址 */
    private String                                         myIP;
    
    /** 服务所在的宿主机IP地址 */
    private String                                         myKubernetesIP;
    
    /** 服务类型，是Windows还是Linux */
    private String                                         osType;
    
    /** 服务无效次数。当大于心跳检测次数时，服务真正无效，并写入数据库 */
    private int                                            invalidCount = 0;
    
    /** 服务之前是否无效过 */
    private boolean                                        invalid = false;
    
    /** 心跳保存次数 */
    private long                                           heartbeatSaveCount = 0;
    
    
    
    public HeartbeatService()
    {
        this.myIP          = Help.getIP();
        this.claimTaskList = new ArrayList<ITaskX>();
        
        try
        {
            this.osType         = System.getProperty("os.name");
            this.myKubernetesIP = System.getenv("MY_HOST_IP");
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        this.myKubernetesIP = Help.NVL(this.myKubernetesIP ,this.myIP);
    }
    
    
    
    /**
     * 我认领的所有任务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-30
     * @version     v1.0
     *
     * @return
     */
    @Override
    public Map<String ,String> myClaimTasks()
    {
        return this.claimTasks;
    }
    
    
    
    /**
     * 保存边缘心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-29
     * @version     v1.0
     * 
     * @return
     */
    @Override
    public void heartbeat()
    {
        synchronized ( this )
        {
            if ( this.isRunning )
            {
                return;
            }
            
            this.isRunning = true;
        }
        
        Jobs                           v_Jobs      = (Jobs) XJava.getObject("JOBS_MS_Common");
        List<Heartbeat>                v_Workers   = null;
        List<ITask>                    v_Tasks     = null;
        PartitionMap<Heartbeat ,ITask> v_ClaimTask = null;
        List<ITask>                    v_MyTasks   = null;
        Long                           v_NowTime   = Date.getNowTime().getTime();
        try
        {
            Heartbeat v_Heartbeat = this.saveHeartbeat();
            if ( v_Heartbeat == null )
            {
                $Logger.error("保存心跳异常");
                return;
            }
            
            // 等待一下集群中所有服务者的更新心跳
            Thread.sleep(5 * 1000);
            
            v_Workers = this.queryByValids(((Job) XJava.getObject("JOB_MS_Common_Heartbeat")).getIntervalLen() * this.heartbeatCheckCount.getValueInt() * -1);
            if ( Help.isNull(v_Workers) )
            {
                $Logger.info("服务者列表为空");
                return;
            }
            
            ITaskService v_TaskService = (ITaskService) XJava.getObject("MS_Common_Heartbeat_TaskService");
            if ( v_TaskService == null )
            {
                $Logger.debug("尚未开发心跳任务");
                return;
            }
            
            v_Tasks = v_TaskService.getTasks();
            if ( Help.isNull(v_Tasks) )
            {
                $Logger.info("任务列表为空");
                return;
            }
            
            v_ClaimTask = Help.claimTask(v_Workers ,v_Tasks);
            v_MyTasks   = v_ClaimTask.get(v_Heartbeat);
            if ( Help.isNull(v_MyTasks) )
            {
                $Logger.info("本服务者(" + v_Heartbeat.getEdgeIP() +")未认领到任务");
                if ( !Help.isNull(this.claimTasks) )
                {
                    this.printWorkersTasks(v_Workers ,v_Tasks);
                }
                return;
            }
            
            // 提前初始化&更新数据拉取对象信息，方便它在定时任务中高效的执行
            // 并在最后更新定时任务的规划情况
            boolean v_IsChangeByAll = false;     // 本服务认领的任务在全局上是否有变化
            int     v_Index         = 0;
            this.claimTaskList.clear();
            for (ITask v_Task : v_MyTasks)
            {
                Return<ITaskX> v_IsChange = v_TaskService.refresh(v_Task);
                
                this.claimTaskList.add(v_IsChange.getParamObj());
                this.jobXIDs.put(v_Task.getXid() ,v_NowTime);
                $Logger.info("认领任务(" + (++v_Index) + "/" + v_MyTasks.size() + ")：" + (v_IsChange.booleanValue() ? "有更新的：" : "保持不变：") + v_Task.getXid());
                
                if ( v_IsChange.booleanValue() )
                {
                    v_IsChangeByAll = true;
                }
            }
            
            // 本服务认领的任务在全局上有变化时，再次更新一下心跳信息。避免信息的延时反馈
            if ( v_IsChangeByAll )
            {
                v_Heartbeat = this.saveHeartbeat();
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        finally
        {
            this.isRunning = false;
            this.releaseAddClaimTasks(v_NowTime ,v_Jobs);
            
            if ( v_MyTasks != null )
            {
                v_MyTasks.clear();
            }
            
            if ( v_ClaimTask != null )
            {
                v_ClaimTask.clear();
            }
            
            if ( v_Tasks != null )
            {
                v_Tasks.clear();
            }
            
            if ( v_Workers != null )
            {
                v_Workers.clear();
            }
        }
    }
    
    
    
    /**
     * 打印服务者列表和任务列表。辅助分析
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-01
     * @version     v1.0
     *
     * @param i_Workers
     * @param i_Tasks
     */
    private void printWorkersTasks(List<Heartbeat> i_Workers ,List<ITask> i_Tasks)
    {
        StringBuilder v_Buffer = new StringBuilder();
        
        // 打印服务者列表
        v_Buffer.append("\n");
        v_Buffer.append(StringHelp.rpad("Edge IP"         ,16 ," "));
        v_Buffer.append(StringHelp.rpad("Edge Start Time" ,22 ," "));
        v_Buffer.append(StringHelp.rpad("OS Time"         ,22 ," "));
        v_Buffer.append(StringHelp.rpad("Update Time"     ,22 ," ")).append("\n");
        v_Buffer.append(StringHelp.rpad(""                ,82 ,"-")).append("\n");
        for (Heartbeat v_Work : i_Workers)
        {
            v_Buffer.append(StringHelp.rpad(v_Work.getEdgeIP()        ,16 ," "));
            v_Buffer.append(StringHelp.rpad(v_Work.getEdgeStartTime() ,22 ," "));
            v_Buffer.append(StringHelp.rpad(v_Work.getOsTime()        ,22 ," "));
            v_Buffer.append(StringHelp.rpad(v_Work.getUpdateTime()    ,22 ," ")).append("\n");
        }
        
        // 打印任务列表
        v_Buffer.append("\n");
        v_Buffer.append("\n");
        v_Buffer.append(StringHelp.rpad("Create Time" ,22 ," "));
        v_Buffer.append("Task XID").append("\n");
        v_Buffer.append(StringHelp.rpad("" ,58 ,"-")).append("\n");
        for (ITask v_Task : i_Tasks)
        {
            v_Buffer.append(StringHelp.rpad(v_Task.getCreateTime().getFull() ,22 ," "));
            v_Buffer.append(v_Task.getXid()).append("\n");
        }
        
        $Logger.info(v_Buffer.toString());
    }
    
    
    
    /**
     * 心跳信息入库
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-01
     * @version     v1.0
     *
     * @return
     */
    private Heartbeat saveHeartbeat()
    {
        int v_ClaimCount  = 0;
        int v_TaskOKCount = 0;
        if ( !Help.isNull(this.claimTaskList) )
        {
            v_ClaimCount = this.claimTaskList.size();
            
            for (ITaskX v_TaskX : this.claimTaskList)
            {
                v_TaskOKCount += v_TaskX.isRunOK() ? 1 : 0;
            }
        }
        
        Heartbeat v_Heartbeat = new Heartbeat();
        
        v_Heartbeat.setEdgeIP(     this.myIP);
        v_Heartbeat.setHostIP(     this.myKubernetesIP);
        v_Heartbeat.setEdgeVersion(this.edgeVersion.getValue());
        v_Heartbeat.setOsType(     this.osType);
        v_Heartbeat.setEdgeStartTime(BaseViewMode.$StartupTime.getFull());
        v_Heartbeat.setOsTime(Date.getNowTime().getFullMilli());
        v_Heartbeat.setClaimCount( v_ClaimCount);
        v_Heartbeat.setTaskOKCount(v_TaskOKCount);
        v_Heartbeat.setIsValid(v_ClaimCount >= 1 && v_TaskOKCount <= 0 ? -1 : 1);
        
        // 在K8s上部署，K8s节点宕机时，发现如下现象
        // 现象01：心跳可写库成功。
        // 现象02：操作系统时间能获取，但时间不在变化，被固定在宕机时刻
        // 现象03：内存无法写操作，如 invalidCount++; 将无效
        if ( v_Heartbeat.getIsValid() < 1 )
        {
            if ( this.invalidCount >= this.heartbeatCheckCount.getValueInt() )
            {
                // 当大于心跳检测次数时，服务真正无效，并写入数据库
                v_Heartbeat.setInvalidTime(new Date());
                this.invalid = true;
            }
            else
            {
                this.invalidCount++;
                v_Heartbeat.setIsValid(1);
            }
        }
        
        // 有过一次无效，就一直无效下去，等人工来处理
        if ( this.invalid )
        {
            v_Heartbeat.setIsValid(-1);
        }
        
        return this.save(v_Heartbeat);
    }
    
    
    
    /**
     * 释放之前认领过的但现在不属于自己的任务
     * 添加认领的任务，并创建定时任务Job
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-30
     * @version     v1.0
     *
     * @param i_NowTime  本次周期的时间
     * @param i_Jobs     任务池
     */
    private void releaseAddClaimTasks(Long i_NowTime ,Jobs i_Jobs)
    {
        ITaskService        v_TaskService = (ITaskService) XJava.getObject("MS_Common_Heartbeat_TaskService");
        Map<String ,String> v_ClaimTasks  = new HashMap<String ,String>();
        boolean             v_IsChange    = false;
        
        if ( !Help.isNull(this.jobXIDs) )
        {
            Iterator<String> v_JobXIDKeys = this.jobXIDs.keySet().iterator();
            while ( v_JobXIDKeys.hasNext() )
            {
                String v_TaskXID   = v_JobXIDKeys.next();
                String v_JobXID_WS = "JOB_WS_" + v_TaskXID;        // WebSocket数据的定时任务XID
                String v_JobXID_DB = "JOB_DB_" + v_TaskXID;        // 写入数据库的定时任务XID
                Long   v_Time      = this.jobXIDs.get(v_TaskXID);
                
                // 释放定时任务
                if ( !i_NowTime.equals(v_Time) )
                {
                    if ( XJava.getObject(v_JobXID_WS ,false) != null )
                    {
                        XJava.remove(v_JobXID_WS);
                        delJobByJobs(i_Jobs ,v_JobXID_WS);
                        $Logger.info("释放任务：" + v_JobXID_WS);
                    }
                    
                    if ( XJava.getObject(v_JobXID_DB ,false) != null )
                    {
                        XJava.remove(v_JobXID_DB);
                        delJobByJobs(i_Jobs ,v_JobXID_DB);
                        $Logger.info("释放任务：" + v_JobXID_DB);
                    }
                    
                    v_JobXIDKeys.remove();
                    v_IsChange = true;
                }
                // 添加&更新定时任务
                else
                {
                    ITaskX  v_TaskX    = (ITaskX) XJava.getObject(v_TaskXID);
                    boolean v_IsSaveIP = false;        // 避免重复写库
                    
                    if ( v_TaskX.isWebPush() )
                    {
                        Job v_JobWS = (Job)XJava.getObject(v_JobXID_WS);
                        if ( v_JobWS == null )
                        {
                            this.addJobWebSocketByJobs(i_Jobs ,v_JobXID_WS ,v_TaskX);
                            v_IsChange = true;
                            v_IsSaveIP = true;
                            
                            if ( v_TaskService != null )
                            {
                                // 这里只能是K8s的IP，因为前端页面还要通过此IP访问WebSocket服务
                                v_TaskService.updateClaimIP(v_TaskX ,this.myKubernetesIP);
                            }
                        }
                        else
                        {
                            if ( v_JobWS.getIntervalLen() != v_TaskX.getWebPushInterval().intValue() )
                            {
                                v_JobWS.setIntervalLen(v_TaskX.getWebPushInterval());
                                v_IsChange = true;
                            }
                        }
                        
                        String v_FirstTime = this.claimTasks.get(v_TaskX.getWebPushName());
                        if ( Help.isNull(v_FirstTime) )
                        {
                            v_FirstTime = Date.getNowTime().getFull();
                        }
                        v_ClaimTasks.put(v_TaskX.getWebPushName() ,v_FirstTime);
                    }
                    
                    if ( v_TaskX.isXsql() )
                    {
                        Job v_JobDB = (Job)XJava.getObject(v_JobXID_DB);
                        if ( v_JobDB == null )
                        {
                            this.addJobDatabaseByJobs(i_Jobs ,v_JobXID_DB ,v_TaskX);
                            v_IsChange = true;
                            
                            if ( !v_IsSaveIP )
                            {
                                if ( v_TaskService != null )
                                {
                                    // 这里只能是K8s的IP，因为前端页面还要通过此IP访问WebSocket服务
                                    v_TaskService.updateClaimIP(v_TaskX ,this.myKubernetesIP);
                                }
                            }
                        }
                        else
                        {
                            if ( v_JobDB.getIntervalLen() != v_TaskX.getXsqlInterval().intValue() )
                            {
                                v_JobDB.setIntervalLen(v_TaskX.getXsqlInterval());
                                v_IsChange = true;
                            }
                        }
                        
                        String v_FirstTime = this.claimTasks.get(v_TaskX.getXsqlXID());
                        if ( Help.isNull(v_FirstTime) )
                        {
                            v_FirstTime = Date.getNowTime().getFull();
                        }
                        v_ClaimTasks.put(v_TaskX.getXsqlXID() ,v_FirstTime);
                    }
                }
            }
        }
        
        if ( v_IsChange )
        {
            this.claimTasks = v_ClaimTasks;
        }
        else
        {
            v_ClaimTasks.clear();
            v_ClaimTasks = null;
        }
    }
    
    
    
    /**
     * 添加WebSocket的定时任务到任务池中
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-30
     * @version     v1.0
     *
     * @param i_Jobs
     * @param i_Code
     * @param i_TaskX
     */
    private void addJobWebSocketByJobs(Jobs i_Jobs ,String i_Code ,ITaskX i_TaskX)
    {
        Job v_Job = new Job();
        
        v_Job.setXJavaID(     i_Code);
        v_Job.setCode   (     i_Code);
        v_Job.setName   (     i_TaskX.getXid());
        v_Job.setIntervalType(Job.$IntervalType_Second);
        v_Job.setIntervalLen( i_TaskX.getWebPushInterval());
        v_Job.setXid(         i_TaskX.getXid());
        v_Job.setMethodName(  i_TaskX.getWebPushExecuteName());
        v_Job.setComment(     i_TaskX.getComment());
        v_Job.setStartTime(   Date.getNowTime().getFirstTimeOfDay().getFull());
        
        XJava.putObject(i_Code ,v_Job);
        i_Jobs.addJob(v_Job);
    }
    
    
    
    /**
     * 添加写入数据库的定时任务到任务池中
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-01
     * @version     v1.0
     *
     * @param i_Jobs
     * @param i_Code
     * @param i_TaskX
     */
    private void addJobDatabaseByJobs(Jobs i_Jobs ,String i_Code ,ITaskX i_TaskX)
    {
        Job v_Job = new Job();
        
        v_Job.setXJavaID(     i_Code);
        v_Job.setCode   (     i_Code);
        v_Job.setName   (     i_TaskX.getXid());
        v_Job.setIntervalType(Job.$IntervalType_Minute);
        v_Job.setIntervalLen( i_TaskX.getXsqlInterval());
        v_Job.setXid(         i_TaskX.getXid());
        v_Job.setMethodName(  i_TaskX.getXsqlExecuteName());
        v_Job.setComment(     i_TaskX.getComment());
        v_Job.setStartTime(   Date.getNowTime().getFirstTimeOfDay().getFull());
        
        XJava.putObject(i_Code ,v_Job);
        i_Jobs.addJob(v_Job);
    }
    
    
    
    /**
     * 删除定时任务池中的任务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-13
     * @version     v1.0
     *
     * @param i_Jobs   定时任务池
     * @param i_Code   要删除任务编号
     */
    private void delJobByJobs(Jobs i_Jobs ,String i_Code)
    {
        Iterator<Job> v_JobList = i_Jobs.getJobs();
        while ( v_JobList.hasNext() )
        {
            Job v_Item = v_JobList.next();
            if ( v_Item.getCode().equals(i_Code) )
            {
                i_Jobs.delJob(v_Item);
                break;
            }
        }
    }
    
    
    
    /**
     * 保存边缘心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-29
     * @version     v1.0
     * 
     * @param i_Heartbeat  边缘服务的心跳
     * @return
     */
    @Override
    public Heartbeat save(Heartbeat i_Heartbeat)
    {
        if ( i_Heartbeat == null )
        {
            return null;
        }
        
        this.heartbeatSaveCount++;
        if ( this.heartbeatSaveCount <= 1 )
        {
            // 预防重启服务后生成IP是同一样egdeIP
            this.heartbeatCache.delEdgeIP(i_Heartbeat.getEdgeIP());
            this.heartbeatDAO.delEdgeIP(i_Heartbeat.getEdgeIP());
        }
        else if ( this.heartbeatSaveCount + 1 >= Long.MAX_VALUE )
        {
            // 虽然我一生身与不会执行一次，但还是写上吧，只少逻辑上是严谨的
            this.heartbeatSaveCount = 1;
        }
        
        this.heartbeatCache.save(i_Heartbeat);
        boolean v_Ret = this.heartbeatDAO.save(i_Heartbeat);
        return v_Ret ? i_Heartbeat : null;
    }
    
    
    
    /**
     * 按IP查找边缘计算服务的心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-29
     * @version     v1.0
     * 
     * @param i_EdgeIP    边缘服务的IP
     * @return
     */
    @Override
    public Heartbeat queryByEdge(String i_EdgeIP)
    {
        return this.heartbeatDAO.queryByEdge(i_EdgeIP);
    }
    
    
    
    /**
     * 查询在线的边缘服务。即，仅查询最后多少秒几的心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-29
     * @version     v1.0
     * 
     * @param i_Second  秒数
     * @return
     */
    @Override
    public List<Heartbeat> queryByValids(Integer i_Second)
    {
        return this.heartbeatCache.queryByValids(i_Second);
        // return this.heartbeatDAO.queryByValids(i_Second);
    }
    
    
    
    /**
     * 查找边缘计算服务的心跳列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-29
     * @version     v1.0
     * 
     * @param i_Heartbeat  边缘服务的心跳
     * @return
     */
    @Override
    public List<Heartbeat> query(Heartbeat i_Heartbeat)
    {
        return this.heartbeatDAO.query(i_Heartbeat);
    }
    
}
