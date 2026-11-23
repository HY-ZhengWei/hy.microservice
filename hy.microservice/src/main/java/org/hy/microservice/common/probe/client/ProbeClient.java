package org.hy.microservice.common.probe.client;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.SerializableDef;
import org.hy.common.xml.annotation.Xjava;

import org.hy.microservice.common.probe.IProbeService;
import org.hy.microservice.common.probe.annotation.XOntology;
import org.hy.microservice.common.probe.data.IProbeDataDAO;
import org.hy.microservice.common.probe.data.ProbeData;
import org.hy.microservice.common.probe.enums.MetaFormat;
import org.hy.microservice.common.probe.enums.ProbeKeyword;
import org.hy.microservice.common.probe.schema.IProbeSchemaService;
import org.hy.microservice.common.probe.schema.ProbeSchemaDomain;
import org.hy.microservice.common.probe.schema.ProbeSchemaMetaDomain;





/**
 * 数据探针的客户端操作类
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-11
 * @version     v1.0
 */
@Xjava
public class ProbeClient
{
    
    /** 数据探针的本体结构缓存（程序固化的）。Map.key为体定义的对象元素类型 */
    private static Map<Class<?> ,ProbeSchemaDomain> $SchemaCaches   = new HashMap<>();
    
    /** 数据探针的本体结构缓存（用户动态的）。Map.key为本体结构串码MetaCode */
    private static Map<String ,ProbeSchemaDomain>   $MetaCodeCaches = new HashMap<>();
    
    /** 归类对应的数据资产编号。Map.key为归类 */
    private static Map<String ,AssetID>             $AssetIDs       = new HashMap<>();
    
    
    
    @Xjava 
    private IProbeService       probeService;
    
    @Xjava
    private IProbeDataDAO       probeDataDAO;
    
    @Xjava
    private IProbeSchemaService probeSchemaService;
    
    /** 数据资产编号中的来源 */
    private String              source;
    
    
    
    /**
     * 通过自动注入解释数据资产编号中的来源
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-12
     * @version     v1.0
     *
     * @param i_Source
     */
    @Xjava(ref="MS_Common_AssetID_Source")
    public void setSource(Param i_Source)
    {
        this.source = i_Source.getValue().trim();
        
        String [] v_Temp = this.source.split("://");
        if ( v_Temp.length >= 2 )
        {
            this.source = v_Temp[1];
        }
        
        v_Temp = this.source.split("/");
        if ( v_Temp.length >= 2 )
        {
            this.source = StringHelp.replaceAll(v_Temp[0] ,":" ,"-") + v_Temp[1];
        }
        
        if ( this.source.length() > 55 )
        {
            throw new RuntimeException("数据探针来源[" + this.source + "].length > 55");
        }
    }
    
    
    
    /**
     * 获取：数据资产编号中的来源
     */
    public String getSource()
    {
        return source;
    }
    
    
    
    /**
     * 检查数据探针的结构（用户动态的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-22
     * @version     v1.0
     *
     * @param io_Schema  数据探针的结构
     * @return           Return.paramStr为异常信息
     */
    public Return<String> checkSchema(ProbeSchemaDomain io_Schema)
    {
        Return<String> v_Ret = new Return<>(false);
                
        if ( io_Schema == null )
        {
            return v_Ret.paramStr("Ontology User Schema is null");
        }
        else if ( Help.isNull(io_Schema.getType()) )
        {
            return v_Ret.paramStr("Ontology User type is null");
        }
        else if ( io_Schema.getType().length() > 128 )
        {
            return v_Ret.paramStr("Ontology User type[" + io_Schema.getType() + "].lenght > 128");
        }
        
        if ( !Help.isNull(io_Schema.getMetas()) )
        {
            // 所有目标的数据探针的结构ID指向数据探针的结构本体
            int v_OrderBy  = 0;
            for (ProbeSchemaMetaDomain v_Item : io_Schema.getMetas())
            {
                if ( Help.isNull(v_Item.getMname()) )
                {
                    return v_Ret.paramStr("Ontology User type[" + io_Schema.getType() + "] mname is null");
                }
                
                v_Item.setMname(v_Item.getMname().trim());
                if ( v_Item.getMname().length() <= 1 )
                {
                    return v_Ret.paramStr("Ontology User type[" + io_Schema.getType() + "] mname[" + v_Item.getMname() + "].lenght <= 1");
                }
                if ( ProbeKeyword.get(v_Item.getMname()) != null )
                {
                    return v_Ret.paramStr("Ontology User type[" + io_Schema.getType() + "] mname[" + v_Item.getMname() + "] is Keyword");
                }
                
                v_Item.setMformat( v_Item.getMformat()  == null ? MetaFormat.String : v_Item.getMformat());
                v_Item.setMlength( v_Item.getMlength()  == null ? 0     : v_Item.getMlength());
                v_Item.setMdecimal(v_Item.getMdecimal() == null ? 0     : v_Item.getMdecimal());
                v_Item.setPk(      v_Item.getPk()       == null ? false : v_Item.getPk());
                v_Item.setNullable(v_Item.getNullable() == null ? true  : v_Item.getNullable());
                v_Item.setOrderBy(++v_OrderBy);
                
                if ( v_Item.getPk() )
                {
                    v_Item.setNullable(false);
                }
            }
            
            if ( io_Schema.getName().getMetasNames().size() != v_OrderBy * 2 )
            {
                // 字段名称与名称代号有雷同，请更换字段命名
                return v_Ret.paramStr("Ontology User type[" + io_Schema.getType() + "] mname is similar to mnameABC");
            }
        }
        else 
        {
            return v_Ret.paramStr("Ontology User type[" + io_Schema.getType() + "] metas is null");
        }
        
        return v_Ret.set(true);
    }
    
    
    
    /**
     * 数据探针的数据保存（用户动态的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-11
     * @version     v1.0
     *
     * @param <D>
     * @param i_Schema  数据探针的结构
     * @param i_Data    数据
     * @param i_UserID  用户ID
     */
    public void dataSave(ProbeSchemaDomain i_Schema ,Map<String ,Object> i_Data ,String i_UserID)
    {
        ProbeSchemaDomain v_Schema = null;
        
        synchronized (this)
        {
            Return<String> v_ChectRet = this.checkSchema(i_Schema);
            if ( !v_ChectRet.booleanValue() )
            {
                throw new RuntimeException(v_ChectRet.paramStr);
            }
            
            String v_MetaCode = this.probeSchemaService.metaCode(i_Schema.getType() ,i_Schema.getMetas());
            v_Schema = $MetaCodeCaches.get(v_MetaCode);
            if ( v_Schema == null )
            {
                v_Schema = i_Schema;
                v_Schema.setCreateUserID(i_UserID);
                v_Schema.setUpdateUserID(i_UserID);
                
                Return<ProbeSchemaDomain> v_RetSchema = this.probeService.saveSchema(v_Schema);
                if ( !v_RetSchema.booleanValue() )
                {
                    throw new RuntimeException("Ontology User type[" + v_Schema.getName() + "] create schema error: " + v_RetSchema.getParamStr());
                }
                
                $MetaCodeCaches.put(v_MetaCode ,v_Schema);
                $AssetIDs      .put(v_Schema.getType() ,this.probeDataDAO.queryMaxAssetID(v_RetSchema.getParamObj().getName().gatData() ,this.source));
            }
        }
        
        this.dataSaveHandle(v_Schema ,new ProbeClientDataMap(i_Data) ,i_UserID);
    }
    
    
    
    /**
     * 数据探针的数据保存（程序固化的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-11
     * @version     v1.0
     *
     * @param <D>
     * @param i_Data    数据
     * @param i_UserID  用户ID
     */
    public <D extends SerializableDef> void dataSave(D i_Data ,String i_UserID)
    {
        Class<? extends SerializableDef> v_DataClass = i_Data.getClass();
        ProbeSchemaDomain                v_Schema    = null;
        
        synchronized (this)
        {
            v_Schema = $SchemaCaches.get(v_DataClass);
            if ( v_Schema == null )
            {
                v_Schema = XOntology.parser(i_Data);
                if ( v_Schema == null )
                {
                    throw new RuntimeException("Ontology[" + v_DataClass.getName() + "] is error");
                }
                
                v_Schema.setCreateUserID(i_UserID);
                v_Schema.setUpdateUserID(i_UserID);
                
                Return<ProbeSchemaDomain> v_RetSchema = this.probeService.saveSchema(v_Schema);
                if ( !v_RetSchema.booleanValue() )
                {
                    throw new RuntimeException("Ontology[" + v_DataClass.getName() + "] create schema error: " + v_RetSchema.getParamStr());
                }
                
                $SchemaCaches.put(v_DataClass ,v_Schema);
                $AssetIDs    .put(v_Schema.getType() ,this.probeDataDAO.queryMaxAssetID(v_RetSchema.getParamObj().getName().gatData() ,this.source));
            }
        }
        
        this.dataSaveHandle(v_Schema ,new ProbeClientDataJava<D>(i_Data) ,i_UserID);
    }
    
    
    
    /**
     * 数据探针的数据保存的处理方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-22
     * @version     v1.0
     *
     * @param i_Schema  数据探针的结构
     * @param i_Data    数据探针的客户端数据
     * @param i_UserID  用户ID
     */
    private void dataSaveHandle(ProbeSchemaDomain i_Schema ,ProbeClientData i_Data ,String i_UserID)
    {
     // 判定按用户主键PK添加或更新数据，还是保存新数据
        String v_AssetIDDB = this.probeDataDAO.queryAssetIDByPK(i_Schema.getName().gatData() ,i_Data.getData());
        if ( Help.isNull(v_AssetIDDB) )
        {
            AssetID v_AssetID = $AssetIDs.get(i_Schema.getType());
            v_AssetIDDB = v_AssetID.newAssetID();      // 注：客户端的本地数据库不追加来源 this.source
        }
        
        ProbeData v_ProbeData = new ProbeData();
        v_ProbeData.setType(i_Schema.getType());
        v_ProbeData.getData().put(ProbeKeyword.TableName   .getValue() ,i_Schema.getName().getTableName());
        v_ProbeData.getData().put(ProbeKeyword.AssetID     .getValue() ,v_AssetIDDB);
        v_ProbeData.getData().put(ProbeKeyword.AssetState  .getValue() ,0);
        v_ProbeData.getData().put(ProbeKeyword.IsDel       .getValue() ,0);
        v_ProbeData.getData().put(ProbeKeyword.CreateUserID.getValue() ,i_UserID);
        v_ProbeData.getData().put(ProbeKeyword.UpdateUserID.getValue() ,i_UserID);
        v_ProbeData.getData().put(ProbeKeyword.CreateTime  .getValue() ,Date.getNowTime().getFull());
        v_ProbeData.getData().put(ProbeKeyword.UpdateTime  .getValue() ,Date.getNowTime().getFull());
        
        for (ProbeSchemaMetaDomain v_Meta : i_Schema.getName().getMetas())
        {
            String v_Key   = v_Meta.getMname();
            Object v_Value = i_Data.getFiledValue(v_Meta.getPropIndex());
            
            if ( v_Meta.getPk() && v_Value == null )
            {
                throw new RuntimeException("Ontology[" + i_Schema.getType() + "] name[" + v_Meta.getMname() + "] pk is null");
            }
            else if ( !v_Meta.getNullable() && v_Value == null )
            {
                throw new RuntimeException("Ontology[" + i_Schema.getType() + "] name[" + v_Meta.getMname() + "] not allow null");
            }
            
            if ( v_Meta.getMformat().equals(MetaFormat.Number) )
            {
                if ( v_Value == null )
                {
                    v_ProbeData.getData().put(v_Key ,0);
                }
                else
                {
                    Number v_Num  = (Number) v_Value;
                    String v_Text = v_Value.toString();
                    if ( v_Meta.getMdecimal() <= 0 )
                    {
                        if ( v_Text.length() > v_Meta.getMlength() )
                        {
                            throw new RuntimeException("Ontology[" + i_Schema.getType() + "] name[" + v_Meta.getMname() + "] [" + v_Text + "].lenght > " + v_Meta.getMlength());
                        }
                        v_ProbeData.getData().put(v_Key ,v_Num.longValue());
                    }
                    else
                    {
                        v_Num  = Help.round(v_Num.doubleValue() ,v_Meta.getMdecimal());
                        v_Text = v_Num.toString();
                                
                        int v_Decimal = v_Text.indexOf(".");
                        if ( v_Decimal >= 0 && v_Text.length() > v_Meta.getMlength() + 1 )
                        {
                            throw new RuntimeException("Ontology[" + i_Schema.getType() + "] name[" + v_Meta.getMname() + "] [" + v_Text + "].lenght > " + v_Meta.getMlength());
                        }
                        else if ( v_Decimal < 0 && v_Text.length() > v_Meta.getMlength() )
                        {
                            throw new RuntimeException("Ontology[" + i_Schema.getType() + "] name[" + v_Meta.getMname() + "] [" + v_Text + "].lenght > " + v_Meta.getMlength());
                        }
                        v_ProbeData.getData().put(v_Key ,v_Num);
                    }
                }
            }
            else if ( v_Meta.getMformat().equals(MetaFormat.Boolean) )
            {
                v_ProbeData.getData().put(v_Key ,Help.NVL((Boolean) v_Value) ? 1 : 0);
            }
            else
            {
                if ( v_Value == null )
                {
                    v_ProbeData.getData().put(v_Key ,null);
                }
                else
                {
                    String v_Text = v_Value.toString();
                    if ( v_Text.length() > v_Meta.getMlength() )
                    {
                        throw new RuntimeException("Ontology[" + i_Schema.getType() + "] name[" + v_Meta.getMname() + "] [" + v_Text + "].lenght > " + v_Meta.getMlength());
                    }
                    v_ProbeData.getData().put(v_Key ,v_Text);
                }
            }
        }
        
        this.probeDataDAO.save(i_Schema.getName().gatData() ,v_ProbeData);
    }
    
}
