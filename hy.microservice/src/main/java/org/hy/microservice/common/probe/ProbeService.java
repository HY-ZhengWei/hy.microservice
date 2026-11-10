package org.hy.microservice.common.probe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;

import org.hy.microservice.common.probe.data.IProbeDataCache;
import org.hy.microservice.common.probe.data.ProbeData;
import org.hy.microservice.common.probe.enums.MetaFormat;
import org.hy.microservice.common.probe.enums.ProbeError;
import org.hy.microservice.common.probe.enums.ProbeKeyword;
import org.hy.microservice.common.probe.schema.IProbeSchemaService;
import org.hy.microservice.common.probe.schema.ProbeSchemaDomain;
import org.hy.microservice.common.probe.schema.ProbeSchemaMetaDomain;





/**
 * 业务层：数据探针
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
@Xjava
public class ProbeService implements IProbeService ,Serializable
{

    private static final long serialVersionUID = 3601265677534101116L;
    
    private static final Logger $Logger = new Logger(ProbeService.class);
    
    
    
    @Xjava
    private IProbeSchemaService probeSchemaService;
    
    @Xjava
    private IProbeDataCache     probeDataCache;
    
    
    
    /**
     * 按归类查询数据探针的结构（带缓存的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-06
     * @version     v1.0
     *
     * @param i_Type  本体结构的归类
     * @return
     */
    public ProbeSchemaDomain queryByType(String i_Type)
    {
        return this.probeSchemaService.queryByXID(i_Type);
    }

    
    
    /**
     * 保存数据探针的本体结构
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     *
     * @param io_Schema  本体结构
     * @return           Return.value     表示是否成功
     *                   Return.paramStr  表示异常消息
     *                   Return.paramObj  表示成功时的数据探针的本体结构
     */
    public synchronized Return<ProbeSchemaDomain> saveSchema(ProbeSchemaDomain io_Schema)
    {
        Return<ProbeSchemaDomain> v_Ret = new Return<ProbeSchemaDomain>(true);
        
        if ( Help.isNull(io_Schema.getType()) )
        {
            return v_Ret.set(false).setParamStr("数据探针的本体结构的归类为空");
        }
        
        if ( this.probeSchemaService.save(io_Schema) == null )
        {
            return v_Ret.set(false).setParamStr("系统内部异常，请联系管理");
        }
        
        return v_Ret.setParamObj(io_Schema);
    }
    
    
    
    /**
     * 数据探针的数据保存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     *
     * @param i_Schema        数据探针的结构
     * @param i_ProbeRequest  数据探针的请求
     * @return                数据探针的响应
     */
    @Override
    public ProbeResponseDomain dataSaves(ProbeSchemaDomain i_Schema ,ProbeRequestDomain i_ProbeRequest)
    {
        ProbeResponseDomain v_Response   = new ProbeResponseDomain();
        Map<String ,String> v_ErrorDatas = new HashMap<String ,String>(); // Map.key是数据资产编号，Map.value是错误编号和原因
        int                 v_OKCount    = 0;
        
        for (Map.Entry<String ,Map<String ,Object>> v_Item : i_ProbeRequest.getSaves().entrySet())
        {
            String              v_YMDNo      = v_Item.getKey();
            Map<String ,Object> v_Record     = v_Item.getValue();
            String              v_AssetIDDB  = v_YMDNo + i_ProbeRequest.getSource();
            String              v_AssetID    = i_Schema.getType() + v_AssetIDDB;
            String              v_AssetIDLog = "ProbeData[" + i_ProbeRequest.getType() + " -> "  + v_YMDNo + " -> " + i_ProbeRequest.getSource() + "] "; 
            
            if ( Help.isNull(v_Record) )
            {
                v_ErrorDatas.put(v_AssetID ,ProbeError.E001_DataNull.toString(""));
                $Logger.warn(v_AssetIDLog + ProbeError.E001_DataNull.toString(""));
                continue;
            }
            if ( v_YMDNo.length() < 17 )
            {
                v_ErrorDatas.put(v_AssetID ,ProbeError.E002_YMDNo.toString(""));
                $Logger.warn(v_AssetIDLog + ProbeError.E002_YMDNo.toString(""));
                continue;
            }
            
            Date v_YMD = null;
            try
            {
                v_YMD = new Date(v_YMDNo.substring(0 ,8));
            }
            catch (Exception exce)
            {
                // Nothing.
            }
            if ( v_YMD == null )
            {
                v_ErrorDatas.put(v_AssetID ,ProbeError.E003_YMD.toString(""));
                $Logger.warn(v_AssetIDLog + ProbeError.E003_YMD.toString(""));
                continue;
            }
            
            if ( !Help.isNumber(v_YMDNo.substring(8)) )
            {
                v_ErrorDatas.put(v_AssetID ,ProbeError.E004_No.toString(""));
                $Logger.warn(v_AssetIDLog + ProbeError.E004_No.toString(""));
                continue;
            }
            
            // 名称是否存在、类型、长度、小数位的验证
            boolean   v_Continue  = false;
            ProbeData v_ProbeData = new ProbeData();
            for (Map.Entry<String ,Object> v_FieldItem : v_Record.entrySet())
            {
                String                v_Key   = v_FieldItem.getKey();
                ProbeSchemaMetaDomain v_Filed = i_Schema.getName().getMetasNames().get(v_Key);
                if ( v_Filed == null )
                {
                    v_ErrorDatas.put(v_AssetID ,ProbeError.E005_NameExists.toString(v_Key));
                    $Logger.warn(v_AssetIDLog + ProbeError.E005_NameExists.toString(v_Key));
                    v_Continue = true;
                    break;
                }
                
                String v_Value = v_FieldItem.getValue().toString();
                if ( MetaFormat.Boolean.equals(v_Filed.getMformat()) )
                {
                    if ( Help.isNumber(v_Value) )
                    {
                        v_FieldItem.setValue(Double.valueOf(v_Value) >= 1 ? 1 : 0);
                    }
                    else
                    {
                        v_FieldItem.setValue(Boolean.valueOf(v_Value) ? 1 : 0);
                    }
                }
                else
                {
                    if ( v_Value.length() > v_Filed.getMlength() )
                    {
                        v_ErrorDatas.put(v_AssetID ,ProbeError.E007_Lenght.toString(v_Key));
                        $Logger.warn(v_AssetIDLog + ProbeError.E007_Lenght.toString(v_Key));
                        v_Continue = true;
                        break;
                    }
                    
                    if ( MetaFormat.Number.equals(v_Filed.getMformat()) )
                    {
                        if ( !Help.isNumber(v_Value) )
                        {
                            v_ErrorDatas.put(v_AssetID ,ProbeError.E006_Type.toString(v_Key));
                            $Logger.warn(v_AssetIDLog + ProbeError.E006_Type.toString(v_Key));
                            v_Continue = true;
                            break;
                        }
                        
                        int v_Decimal = v_Value.indexOf("\\.");
                        if ( v_Filed.getMdecimal() <= 0 && v_Decimal > 0 )
                        {
                            v_ErrorDatas.put(v_AssetID ,ProbeError.E007_Lenght.toString(v_Key));
                            $Logger.warn(v_AssetIDLog + ProbeError.E007_Lenght.toString(v_Key));
                            v_Continue = true;
                            break;
                        }
                        else if ( v_Decimal > 0 && v_Value.length() - v_Decimal - 1 > v_Filed.getMdecimal() )
                        {
                            v_ErrorDatas.put(v_AssetID ,ProbeError.E007_Lenght.toString(v_Key));
                            $Logger.warn(v_AssetIDLog + ProbeError.E007_Lenght.toString(v_Key));
                            v_Continue = true;
                            break;
                        }
                    }
                }
                
                v_ProbeData.getData().put(v_Filed.getMname() ,v_FieldItem.getValue());
            }
            
            if ( v_Continue )
            {
                v_ProbeData.getData().clear();
                v_ProbeData = null;
                continue;
            }
            
            // 主键与非空字段的验证
            for (ProbeSchemaMetaDomain v_Field : i_Schema.getMetas())
            {
                if ( v_Field.getPk() )
                {
                    Object v_Value = v_Record.get(v_Field.getMnameABC());
                    if ( v_Value == null )
                    {
                        v_Value = v_Record.get(v_Field.getMname());
                        if ( v_Value == null )
                        {
                            v_ErrorDatas.put(v_AssetID ,ProbeError.E008_PK.toString(v_Field.getMname()));
                            $Logger.warn(v_AssetIDLog + ProbeError.E008_PK.toString(v_Field.getMname()));
                            v_Continue = true;
                            break;
                        }
                    }
                }
                else if ( !v_Field.getNullable() )
                {
                    Object v_Value = v_Record.get(v_Field.getMnameABC());
                    if ( v_Value == null )
                    {
                        v_Value = v_Record.get(v_Field.getMname());
                        if ( v_Value == null )
                        {
                            v_ErrorDatas.put(v_AssetID ,ProbeError.E009_Nullable.toString(v_Field.getMname()));
                            $Logger.warn(v_AssetIDLog + ProbeError.E009_Nullable.toString(v_Field.getMname()));
                            v_Continue = true;
                            break;
                        }
                    }
                }
            }
            
            if ( v_Continue )
            {
                v_ProbeData.getData().clear();
                v_ProbeData = null;
                continue;
            }
            
            // 验证通过的数据
            v_ProbeData.setAssetID(v_AssetID);
            v_ProbeData.setType(i_Schema.getType());
            v_ProbeData.setMetaCode(i_Schema.getName().getMetaCode());
            v_ProbeData.getData().put(ProbeKeyword.TableName   .getValue() ,i_Schema.getName().getTableName());
            v_ProbeData.getData().put(ProbeKeyword.AssetID     .getValue() ,v_AssetIDDB);
            v_ProbeData.getData().put(ProbeKeyword.IsDel       .getValue() ,0);
            v_ProbeData.getData().put(ProbeKeyword.CreateUserID.getValue() ,i_ProbeRequest.getUserID());
            v_ProbeData.getData().put(ProbeKeyword.UpdateUserID.getValue() ,i_ProbeRequest.getUserID());
            v_ProbeData.getData().put(ProbeKeyword.CreateTime  .getValue() ,Date.getNowTime().getFull());
            v_ProbeData.getData().put(ProbeKeyword.UpdateTime  .getValue() ,Date.getNowTime().getFull());
            
            this.probeDataCache.push(v_ProbeData);
            v_OKCount++;
        }
        
        $Logger.info("数据探针保存：成功接收 " + v_OKCount + " 个，反馈非法数据 " + v_ErrorDatas.size() + " 个");
        v_Response.setErrorDatas(v_ErrorDatas);
        
        return v_Response;
    }
    
    
    
    /**
     * 数据探针的数据删除
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-09
     * @version     v1.0
     *
     * @param i_Schema        数据探针的结构
     * @param i_ProbeRequest  数据探针的请求
     * @return                数据探针的响应
     */
    public ProbeResponseDomain dataDels(ProbeSchemaDomain i_Schema ,ProbeRequestDomain i_ProbeRequest)
    {
        ProbeResponseDomain v_Response   = new ProbeResponseDomain();
        Map<String ,String> v_ErrorDatas = new HashMap<String ,String>(); // Map.key是数据资产编号，Map.value是错误编号和原因
        int                 v_OKCount    = 0;
        
        for (String v_YMDNo : i_ProbeRequest.getDels())
        {
            String              v_AssetIDDB  = v_YMDNo + i_ProbeRequest.getSource();
            String              v_AssetID    = i_Schema.getType() + v_AssetIDDB;
            String              v_AssetIDLog = "ProbeData[" + i_ProbeRequest.getType() + " -> "  + v_YMDNo + " -> " + i_ProbeRequest.getSource() + "] "; 
            
            if ( v_YMDNo.length() < 17 )
            {
                v_ErrorDatas.put(v_AssetID ,ProbeError.E002_YMDNo.toString(""));
                $Logger.warn(v_AssetIDLog + ProbeError.E002_YMDNo.toString(""));
                continue;
            }
            
            Date v_YMD = null;
            try
            {
                v_YMD = new Date(v_YMDNo.substring(0 ,8));
            }
            catch (Exception exce)
            {
                // Nothing.
            }
            if ( v_YMD == null )
            {
                v_ErrorDatas.put(v_AssetID ,ProbeError.E003_YMD.toString(""));
                $Logger.warn(v_AssetIDLog + ProbeError.E003_YMD.toString(""));
                continue;
            }
            
            if ( !Help.isNumber(v_YMDNo.substring(8)) )
            {
                v_ErrorDatas.put(v_AssetID ,ProbeError.E004_No.toString(""));
                $Logger.warn(v_AssetIDLog + ProbeError.E004_No.toString(""));
                continue;
            }
            
            // 验证通过的数据
            ProbeData v_ProbeData = new ProbeData();
            v_ProbeData.setAssetID(v_AssetID);
            v_ProbeData.setType(i_Schema.getType());
            v_ProbeData.setMetaCode(i_Schema.getName().getMetaCode());
            v_ProbeData.getData().put(ProbeKeyword.TableName   .getValue() ,i_Schema.getName().getTableName());
            v_ProbeData.getData().put(ProbeKeyword.AssetID     .getValue() ,v_AssetIDDB);
            v_ProbeData.getData().put(ProbeKeyword.IsDel       .getValue() ,1);
            v_ProbeData.getData().put(ProbeKeyword.UpdateUserID.getValue() ,i_ProbeRequest.getUserID());
            v_ProbeData.getData().put(ProbeKeyword.UpdateTime  .getValue() ,Date.getNowTime().getFull());
            
            this.probeDataCache.push(v_ProbeData);
            v_OKCount++;
        }
        
        $Logger.info("数据探针删除：成功接收 " + v_OKCount + " 个，反馈非法数据 " + v_ErrorDatas.size() + " 个");
        v_Response.setErrorDatas(v_ErrorDatas);
        
        return v_Response;
    }
    
}
