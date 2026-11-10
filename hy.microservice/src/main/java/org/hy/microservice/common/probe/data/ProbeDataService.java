package org.hy.microservice.common.probe.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;

import org.hy.microservice.common.probe.schema.IProbeSchemaService;
import org.hy.microservice.common.probe.schema.ProbeSchemaDomain;
import org.hy.microservice.common.probe.schema.ProbeSchemaNameDomain;





/**
 * 业务层：数据探针的数据
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-09
 * @version     v1.0
 */
@Xjava
public class ProbeDataService implements IProbeDataService ,Serializable
{
    
    private static final long serialVersionUID = 1365484528384183805L;

    private static final Logger $Logger = new Logger(ProbeDataService.class);
    
    
    
    @Xjava
    private IProbeDataCache     probeDataCache;
    
    @Xjava
    private IProbeDataDAO       probeDataDAO;
    
    @Xjava
    private IProbeSchemaService probeSchemaService;
    
    /** 日志写入任务的最大线程数量 */
    @Xjava(ref="MS_Common_OperationLog_MaxThreadCount")
    Param                       maxThreadCount;
    
    /** 每次写入操作日志后释放CPU资源的间隔时长（单位：毫秒），小于等于0时无效 */
    @Xjava(ref="MS_Common_OperationLog_WriteSleep")
    Param                       writeSleep;
    
    /** 是否在运行中 */
    private int                 isRunning;
    
    
    
    /**
     * 数据探针的数据持久化入库
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-09
     * @version     v1.0
     *
     * @return
     */
    public void saves()
    {
        int v_MaxThreadCount = Help.max(Help.NVL(this.maxThreadCount.getValueInt()) ,1);
        synchronized (this)
        {
            if ( this.isRunning >= v_MaxThreadCount )
            {
                return;
            }
            else
            {
                if ( this.isRunning < 0 )
                {
                    this.isRunning  = 1;
                }
                else
                {
                    this.isRunning += 1;
                }
            }
        }
        
        try
        {
            List<ProbeData>       v_Errors     = new ArrayList<ProbeData>();
            ProbeData             v_ProbeData  = null;
            ProbeSchemaDomain     v_Schema     = null;
            ProbeSchemaNameDomain v_SchemaName = null;
            while ( (v_ProbeData = this.probeDataCache.get()) != null )
            {
                v_Schema = this.probeSchemaService.queryByXID(v_ProbeData.getType());
                if ( v_Schema == null )
                {
                    $Logger.error("数据资产编号[" + v_ProbeData.getAssetID() + "] Type[" + v_ProbeData.getType() + "] is not exists.");
                    continue;
                }
                
                v_SchemaName = v_Schema.getAllName().get(v_ProbeData.getMetaCode());
                if ( v_SchemaName == null )
                {
                    $Logger.error("数据资产编号[" + v_ProbeData.getAssetID() + "] MetaCode[" + v_ProbeData.getMetaCode() + "] is not exists.");
                    continue;
                }
                
                boolean v_Succeed = this.probeDataDAO.save(v_SchemaName.gatData() ,v_ProbeData);
                if ( !v_Succeed )
                {
                    $Logger.error("数据资产编号[" + v_ProbeData.getAssetID() + "] save error.");
                    v_Errors.add(v_ProbeData);
                }
            }
            
            // 如有异常情况，等待下一轮再尝试处理
            if ( !Help.isNull(v_Errors) )
            {
                for (ProbeData v_Error : v_Errors)
                {
                    this.saveDataError(v_Error);
                }
            }
            
            v_Errors.clear();
            v_Errors = null;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        finally
        {
            synchronized (this)
            {
                this.isRunning -= 1;
            }
        }
    }
    
    
    
    /**
     * 保存数据异常时的处理方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @param io_ProbeData  数据探针的数据
     */
    private void saveDataError(ProbeData io_ProbeData)
    {
        // 尝试过3次异常时，将放弃保存数据
        if ( io_ProbeData.getCacheToDBErrCount() < 3 )
        {
            io_ProbeData.setCacheToDBErrCount(io_ProbeData.getCacheToDBErrCount() + 1);
            this.probeDataCache.push(io_ProbeData);
        }
    }
    
}
