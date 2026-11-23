package org.hy.microservice.common.probe.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.hy.common.Counter;
import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.common.TablePartitionLink;
import org.hy.common.TablePartitionRID;
import org.hy.common.callflow.CallFlow;
import org.hy.common.callflow.execute.ExecuteResult;
import org.hy.common.callflow.node.APIConfig;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;

import org.hy.microservice.common.probe.ProbeRequest;
import org.hy.microservice.common.probe.ProbeRequestDomain;
import org.hy.microservice.common.probe.ProbeRequestVO;
import org.hy.microservice.common.probe.data.IProbeDataDAO;
import org.hy.microservice.common.probe.enums.ProbeKeyword;
import org.hy.microservice.common.probe.schema.IProbeSchemaCache;
import org.hy.microservice.common.probe.schema.IProbeSchemaService;
import org.hy.microservice.common.probe.schema.ProbeSchema;
import org.hy.microservice.common.probe.schema.ProbeSchemaDomain;
import org.hy.microservice.common.probe.schema.ProbeSchemaMetaDomain;
import org.hy.microservice.common.probe.schema.ProbeSchemaName;
import org.hy.microservice.common.probe.schema.ProbeSchemaNameDomain;





/**
 * 数据探针的向云端同步数据的定时任务（客户端）
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-11
 * @version     v1.0
 */
@Xjava
public class ProbeClientJob
{
    
    private static final Logger          $Logger       = new Logger(ProbeClientJob.class);
    
    /** 统计请求。Map.key为：归类type + 本体结构串码metaCode */
    private static final Counter<String> $RequestCount = new Counter<>();
    
    
    
    @Xjava
    private IProbeSchemaService probeSchemaService;
    
    @Xjava
    private IProbeSchemaCache   probeSchemaCache;
    
    @Xjava
    private IProbeDataDAO       probeDataDAO;
    
    @Xjava
    private ProbeClient         client;
    
    @Xjava(ref="XCFlow_Common_ProbeData_Sync")
    private APIConfig           request;
    
    /** 是否在运行中 */
    private int                 isRunning;
    
    /** 是否刷新过缓存 */
    private boolean             isRefresh = false;
    
    
    
    /**
     * 向云端同步数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-13
     * @version     v1.0
     *
     */
    public void syncToCloud()
    {
        synchronized (this)
        {
            if ( this.isRunning >= 1 )
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
            this.refreshSchemaCaches();
            List<ProbeSchema> v_Schemas = this.probeSchemaCache.getAll();
            if ( Help.isNull(v_Schemas) )
            {
                return;
            }
            
            if ( Help.isNull(this.client.getSource()) )
            {
                throw new RuntimeException("数据探针来源未配置[MS_Common_AssetID_Source]");
            }
            else if ( this.client.getSource().length() > 55 )
            {
                throw new RuntimeException("数据探针来源[" + this.client.getSource() + "].length > 55");
            }
            
            for (ProbeSchema v_Schema : v_Schemas)
            {
                // 按归类及逻辑表分类同步
                for (ProbeSchemaName v_Name : v_Schema.getAllName().values())
                {
                    // 分批多次同步
                    do
                    {
                        TablePartitionRID<String ,Map<String ,Object>> v_Datas = this.probeDataDAO.queryRequireSync(v_Name);
                        if ( Help.isNull(v_Datas) )
                        {
                            break;
                        }
                        
                        PartitionMap<String ,String> v_UserDels = this.handleDatas(v_Name ,v_Datas);
                        this.syncDatas(v_Schema.getType() ,v_Name ,v_Datas ,v_UserDels);
                        
                        v_Datas   .clear();
                        v_UserDels.clear();
                        v_Datas    = null;
                        v_UserDels = null;
                    }
                    while (true);
                }
            }
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
     * 刷新缓存：数据探针的结构
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-14
     * @version     v1.0
     *
     */
    private synchronized void refreshSchemaCaches()
    {
        if ( this.isRefresh )
        {
            return;
        }
        
        Map<String ,ProbeSchemaDomain> v_Schemas = this.probeSchemaService.queryList();
        if ( Help.isNull(v_Schemas) )
        {
            return;
        }
        
        for (ProbeSchemaDomain v_Schema : v_Schemas.values())
        {
            this.probeSchemaService.queryByXID(v_Schema.getType());
        }
        
        this.isRefresh = true;
    }
    
    
    
    /**
     * 处理数据1：分离需删除数据
     * 处理数据2：分离需上传数据
     * 处理数据3：简化上传数据的字段名称
     * 处理数据4：数据资产编号剔除来源
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-13
     * @version     v1.0
     *
     * @param i_Name    数据探针的结构中本体逻辑名称
     * @param io_Datas  原始多行数据
     * @return          需删除数据
     */
    private PartitionMap<String ,String> handleDatas(ProbeSchemaName i_Name ,TablePartitionRID<String ,Map<String ,Object>> io_Datas)
    {
        PartitionMap<String ,String> v_UserDels = new TablePartitionLink<>();
        ProbeSchemaNameDomain        v_Name     = new ProbeSchemaNameDomain(i_Name);
        
        for (String v_UserID : io_Datas.keySet())
        {
            Map<String, Map<String ,Object>> v_UserDatas = io_Datas.get(v_UserID);
            for (Map.Entry<String, Map<String ,Object>> v_Item : v_UserDatas.entrySet())
            {
                String              v_AssetIDDB = v_Item.getKey();
                Map<String ,Object> v_Row       = v_Item.getValue();
                int                 v_IsDel     = (int) v_Row.get(ProbeKeyword.IsDel.getValue().toUpperCase());
                
                if ( v_IsDel == 0 )
                {
                    // 简化上传数据的字段名称
                    for (ProbeSchemaMetaDomain v_Meta : v_Name.getMetas())
                    {
                        String v_ColName  = v_Meta.getMnameABC();
                        Object v_ColValue = v_Row.remove(v_Meta.getMname().toUpperCase());
                        v_Row.put(v_ColName ,v_ColValue);
                    }
                    
                    // 删除无须上传的信息
                    v_Row.remove(ProbeKeyword.IsDel       .getValue().toUpperCase());
                    v_Row.remove(ProbeKeyword.UpdateUserID.getValue().toUpperCase());
                    v_Row.remove(ProbeKeyword.AssetID     .getValue().toUpperCase());
                }
                else
                {
                    v_UserDels.putRow(v_UserID ,v_AssetIDDB);
                }
            }
        }
        
        // 从 "需上传数据" 中剔除 "需删除数据"
        if ( !Help.isNull(v_UserDels) )
        {
            for (String v_UserID : v_UserDels.keySet())
            {
                List<String> v_UDels = v_UserDels.get(v_UserID);
                for (String v_AssetIDDB : v_UDels)
                {
                    io_Datas.removeRow(v_UserID ,v_AssetIDDB);
                }
            }
        }
        
        return v_UserDels;
    }
    
    
    
    /**
     * 向云端同步同一归类的数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-14
     * @version     v1.0
     *
     * @param i_Type       归类
     * @param i_Name       数据探针的结构中本体逻辑名称
     * @param i_UserSaves  需上传数据
     * @param i_UserDels   需删除数据
     */
    private void syncDatas(String i_Type ,ProbeSchemaName i_Name ,TablePartitionRID<String ,Map<String ,Object>> i_UserSaves ,PartitionMap<String ,String> i_UserDels)
    {
        XJSON v_XJson = new XJSON();
        v_XJson.setReturnNVL(false);
        
        if ( !Help.isNull(i_UserSaves) )
        {
            for (String v_UserID : i_UserSaves.keySet())
            {
                Map<String, Map<String ,Object>> v_UserDatas = i_UserSaves.get(v_UserID);
                List<String>                     v_UserDels  = i_UserDels .get(v_UserID);
                ProbeRequest                     v_Request   = newRequest(i_Type ,i_Name);
                
                v_Request.setSaves(v_UserDatas);
                v_Request.setDels(v_UserDels);
                v_Request.setUserID(v_UserID);
                this.syncRequest(i_Name.getTableName() ,v_Request ,v_XJson);
            }
        }
        else
        {
            for (String v_UserID : i_UserDels.keySet())
            {
                List<String> v_UserDels = i_UserDels.get(v_UserID);
                ProbeRequest v_Request  = newRequest(i_Type ,i_Name);
                
                v_Request.setDels(v_UserDels);
                v_Request.setUserID(v_UserID);
                this.syncRequest(i_Name.getTableName() ,v_Request ,v_XJson);
            }
        }
    }
    
    
    
    /**
     * 创建数据探针的请求报文对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-14
     * @version     v1.0
     *
     * @param i_Type  归类
     * @param i_Name  数据探针的结构中本体逻辑名称
     * @return
     */
    private synchronized ProbeRequest newRequest(String i_Type ,ProbeSchemaName i_Name)
    {
        String       v_TotalID    = i_Type + i_Name.getMetaCode();
        Long         v_TotalCount = $RequestCount.get(v_TotalID);
        ProbeRequest v_Request    = new ProbeRequest();
        
        if ( v_TotalCount == null )
        {
            v_Request.setSchema(new ProbeSchema());
            v_Request.getSchema().setName(i_Name);
            $RequestCount.put(v_TotalID ,0);
        }
        
        v_Request.setType(i_Type);
        v_Request.setSource(this.client.getSource());
        return v_Request;
    }
    
    
    
    /**
     * 向云端同步同一归类同一用户的数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-14
     * @version     v1.0
     *
     * @param i_TableName  数据表名称
     * @param i_Request    数据探针的请求报文
     * @Param i_XJson      Json解析器
     */
    private void syncRequest(String i_TableName ,ProbeRequest i_Request ,XJSON i_XJson)
    {
        ProbeRequestVO      v_RequestVO = new ProbeRequestVO(new ProbeRequestDomain(i_Request));
        Map<String ,Object> v_Context   = new HashMap<String ,Object>();
        
        v_Context.put("RequestBody" ,i_XJson.toJsonText(v_RequestVO));
        
        ExecuteResult v_Result = CallFlow.execute(this.request ,v_Context);
        if ( v_Result.isSuccess() )
        {
            $Logger.info("数据探针云同步：上传" 
                       + (i_Request.getSaves() == null ? 0 : i_Request.getSaves().size()) 
                       + "条，删除" 
                       + (i_Request.getDels()  == null ? 0 : i_Request.getDels().size()) 
                       + "条，整体用时" + Date.toTimeLenNano(v_Result.getEndTime() - v_Result.getBeginTime()));
            
            /*
            List<String> v_AssetIDDBs = null;
            if ( !Help.isNull(i_Request.getDels()) )
            {
                v_AssetIDDBs = i_Request.getDels();
            }
            else
            {
                v_AssetIDDBs = new ArrayList<String>();
            }
            if ( !Help.isNull(i_Request.getSaves()) )
            {
                v_AssetIDDBs.addAll(i_Request.getSaves().keySet());
            }
            this.probeDataDAO.updateAssetStates(i_TableName ,v_AssetIDDBs ,true ,i_Request.getUserID());
            */
        }
        else
        {
            String v_Error = "";
            v_Error += "Error XID = " + v_Result.getExecuteXID() + "\n";
            v_Error += "Error Msg = " + v_Result.getException().getMessage() + "\n";
            if ( v_Result.getException() instanceof TimeoutException )
            {
                v_Error += " is TimeoutException";
            }
            $Logger.error(v_Error ,v_Result.getException());
        }
        
        System.out.println("返回值：ReturnData=" + v_Context.get("ProbeResponse"));
    }
    
}
