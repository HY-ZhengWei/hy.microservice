package org.hy.microservice.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.thread.ThreadPool;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.common.xml.plugins.AppInitConfig;
import org.hy.common.xml.plugins.AppInterface;

import org.hy.microservice.common.ProjectStartBase;
import org.hy.microservice.common.operationLog.OperationLogApi;
import org.hy.microservice.common.operationLog.OperationLogModule;





/**
 * Web初始化信息
 * 
 * @author      ZhengWei(HY)、马龙
 * @createDate  2020-11-19
 * @version     v1.0
 */
public class XJavaInit extends AppInitConfig
{
    private static Logger  $Logger = Logger.getLogger(XJavaInit.class);
    
    private static boolean $Init = false;
    
    private String xmlRoot;
    
    
    
    public XJavaInit()
    {
        this(true ,null);
    }
    
    
    
    public XJavaInit(boolean i_IsStartJobs ,String i_ClassHomePath)
    {
        super(i_IsStartJobs);
        this.xmlRoot = Help.NVL(i_ClassHomePath ,Help.getClassHomePath());
        init(i_IsStartJobs);

    }
    
    
    @SuppressWarnings("unchecked")
    private synchronized void init(boolean i_IsStartJobs)
    {
        if ( !$Init )
        {
            $Init = true;
            
            try
            {
                this.loadXML("config/ms.sys.Config.xml" ,this.xmlRoot);
                
                // 父项目参数加载之后，公共资源加载之前，先行加载子项目的初始化参数
                this.loadDirectory("config/initialization/" ,this.xmlRoot);
                
                // 加载子项目的启动配置
                this.loadDirectory("config/startup/" ,this.xmlRoot);
                
                this.loadXML("config/ms.startup.Config.xml" ,this.xmlRoot);
                this.loadXML((List<Param>)XJava.getObject("StartupConfig_MS_Common") ,this.xmlRoot);
                
                // 加载多个顶级包的路径
                String    v_RootPackageNames = ((Param)XJava.getObject("MS_Common_RootPackageName")).getValue();
                String [] v_RPackageNames    = v_RootPackageNames.split(",");
                if ( !Help.isNull(v_RPackageNames) )
                {
                    for (String v_RPackageName : v_RPackageNames)
                    {
                        this.loadClasses(v_RPackageName);
                    }
                }
                
                init_TPool();
                
                // 在注解解释完成后，在Job任务解释之前执行
                this.loadDirectory("config/startupFinish/" ,this.xmlRoot);
                
                if ( i_IsStartJobs )
                {
                    this.loadXML("config/ms.job.xml" ,this.xmlRoot);
                }
                
                init_XRequest();
            }
            catch (Exception exce)
            {
                $Logger.error(exce);
            }
        }
    }
    
    
    
    /**
     * 初始化 @XRequest 接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-17
     * @version     v1.0
     */
    private void init_XRequest()
    {
        Map<String ,String> v_AppMsgKeySysID = new HashMap<String ,String>();
        v_AppMsgKeySysID.put(XJava.getParam("MS_Common_AppMsg_SYSID").getValue() ,XJava.getParam("MS_Common_AppMsg_MsgPWD").getValue());
        XJava.putObject("AppMsgKeySysID" ,v_AppMsgKeySysID);
        
        @SuppressWarnings("unchecked")
        Map<String ,AppInterface> v_Apps = (Map<String ,AppInterface>)XJava.getObject("AppInterfaces");
        if ( Help.isNull(v_Apps) )
        {
            return;
        }
        
        OperationLogModule v_OModule = new OperationLogModule();
        v_OModule.setModuleCode("app");
        v_OModule.setModuleName("XRequest接口");
        ProjectStartBase.$RequestMappingModules.put(v_OModule.getModuleCode() ,v_OModule);
        
        for (AppInterface v_App : v_Apps.values())
        {
            OperationLogApi v_OApi = new OperationLogApi();
            v_OApi.setModuleCode(v_OModule.getModuleCode());
            v_OApi.setModuleName(v_OModule.getModuleName());
            v_OApi.setUrl("/" + v_OModule.getModuleCode() + "/" + v_App.getName());
            v_OApi.setUrlName(v_App.getComment());
            
            ProjectStartBase.$RequestMappingMethods.putRow(v_OApi.getModuleCode() ,v_OApi);
        }
    }
    
    
    
    private void init_TPool()
    {
        ThreadPool.setMaxThread(    this.getIntConfig("MS_Common_TPool_MaxThread"));
        ThreadPool.setMinThread(    this.getIntConfig("MS_Common_TPool_MinThread"));
        ThreadPool.setMinIdleThread(this.getIntConfig("MS_Common_TPool_MinIdleThread"));
        ThreadPool.setIntervalTime( this.getIntConfig("MS_Common_TPool_IntervalTime"));
        ThreadPool.setIdleTimeKill( this.getIntConfig("MS_Common_TPool_IdleTimeKill"));
        ThreadPool.setWaitResource( this.getIntConfig("MS_Common_TPool_WaitResource"));
    }
    
    
    
    private int getIntConfig(String i_XJavaID)
    {
        return Integer.parseInt(XJava.getParam(i_XJavaID).getValue());
    }

}
