package org.hy.microservice.common.probe.schema;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.license.Hash;
import org.hy.common.license.IHash;
import org.hy.common.xml.XJava;
import org.hy.common.xml.XSQL;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;

import org.hy.microservice.common.domain.DataUtil;
import org.hy.microservice.common.domain.DomainUtil;
import org.hy.microservice.common.probe.enums.MetaFormat;
import org.hy.microservice.common.probe.enums.ProbeKeyword;





/**
 * 业务层：数据探针的结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
@Xjava
public class ProbeSchemaService implements IProbeSchemaService ,Serializable
{
    
    private static final long serialVersionUID = -6326649858946286980L;
    
    private static final Logger $Logger = new Logger(ProbeSchemaService.class);

    
    
    @Xjava
    private IProbeSchemaDAO     probeSchemaDAO;
    
    @Xjava
    private IProbeSchemaNameDAO probeSchemaNameDAO;
    
    @Xjava
    private IProbeSchemaCache   probeSchemaCache;
    
    @Xjava(ref="XSQL_Common_Create_ProbeData")
    private XSQL                probeData;
    
    
    
    /**
     * 查询任务数据探针的结构列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     * 
     * @return
     */
    @Override
    public Map<String ,ProbeSchemaDomain> queryList()
    {
        return DomainUtil.toDomain(this.probeSchemaDAO.queryList() ,ProbeSchemaDomain.class);
    }
    
    
    
    /**
     * 查询任务数据探针的结构列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     * 
     * @param i_ProbeSchema  数据探针的结构
     * @return
     */
    @Override
    public Map<String ,ProbeSchemaDomain> queryList(ProbeSchemaDomain i_ProbeSchema)
    {
        return DomainUtil.toDomain(this.probeSchemaDAO.queryList(i_ProbeSchema.gatData()) ,ProbeSchemaDomain.class);
    }
    
    
    
    /**
     * 按ID查询数据探针的结构
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     * 
     * @param i_ID  数据探针的结构ID
     * @return
     */
    @Override
    public ProbeSchemaDomain queryByID(String i_ID)
    {
        if ( Help.isNull(i_ID) )
        {
            return null;
        }
        
        ProbeSchemaDomain v_ProbeSchema = DomainUtil.toDomain(this.probeSchemaDAO.queryByID(i_ID) ,ProbeSchemaDomain.class);
        
        if ( v_ProbeSchema != null )
        {
            Map<String ,ProbeSchemaName> v_AllName = this.probeSchemaNameDAO.queryBySchemaID(i_ID);
            v_ProbeSchema.setAllName(DomainUtil.toDomain(v_AllName ,ProbeSchemaNameDomain.class));
        }
        
        return v_ProbeSchema;
    }
    
    
    
    /**
     * 按归类查询数据探针的结构
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     * 
     * @param i_Type  数据探针的结构归类
     * @return
     */
    @Override
    public ProbeSchemaDomain queryByType(String i_Type)
    {
        if ( Help.isNull(i_Type) )
        {
            return null;
        }
        
        return DomainUtil.toDomain(this.probeSchemaDAO.queryByType(i_Type) ,ProbeSchemaDomain.class);
    }
    
    
    
    /**
     * 按XID（归类）查询数据探针的结构（带缓存的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     * 
     * @param i_XID  数据探针的结构的XID。即，结构归类type
     * @return
     */
    @Override
    public synchronized ProbeSchemaDomain queryByXID(String i_XID)
    {
        if ( Help.isNull(i_XID) )
        {
            return null;
        }
        
        ProbeSchema v_ProbeSchema = this.probeSchemaCache.get(i_XID);
        if ( v_ProbeSchema != null )
        {
            return new ProbeSchemaDomain(v_ProbeSchema);
        }
        
        v_ProbeSchema = this.probeSchemaDAO.queryByType(i_XID);
        if ( v_ProbeSchema == null )
        {
            return null;
        }
        else
        {
            v_ProbeSchema.setAllName(this.probeSchemaNameDAO.queryBySchemaID(v_ProbeSchema.getId()));
            
            ProbeSchemaDomain v_Ret = new ProbeSchemaDomain(v_ProbeSchema);
            this.probeSchemaCache.refreshXObject(v_Ret);
            return v_Ret;
        }
    }
    
    
    
    /**
     * 新增、修改、逻辑删除数据探针的结构
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     *
     * @param io_ProbeSchema  数据探针的结构
     * @return
     */
    @Override
    public synchronized ProbeSchemaDomain save(ProbeSchemaDomain io_ProbeSchema)
    {
        if ( io_ProbeSchema == null )
        {
            return null;
        }
        
        io_ProbeSchema.setCreateUserID(Help.NVL(io_ProbeSchema.getCreateUserID() ,io_ProbeSchema.getUserID()));
        io_ProbeSchema.setUpdateUserID(Help.NVL(io_ProbeSchema.getUpdateUserID() ,io_ProbeSchema.getUserID()));
        io_ProbeSchema.setIsDel(       Help.NVL(io_ProbeSchema.getIsDel()        ,0));
        
        if ( Help.isNull(io_ProbeSchema.getType()) )
        {
            return null;
        }
        
        ProbeSchemaDomain v_Old = this.queryByXID(io_ProbeSchema.getType());
        if ( v_Old != null )
        {
            io_ProbeSchema.setId(v_Old.getId());
        }
        
        if ( Help.isNull(io_ProbeSchema.getId()) )
        {
            io_ProbeSchema.setId("Probe" + StringHelp.getUUID9n());
            io_ProbeSchema.getName().setId("ProbeName" + StringHelp.getUUID9n());
            io_ProbeSchema.getName().setSchemaID(io_ProbeSchema.getId());
            io_ProbeSchema.getName().setTableName(   Help.NVL(io_ProbeSchema.getName().getTableName() ,"TProbeData" + Date.getNowTime().getFull_ID()));
            io_ProbeSchema.getName().setComment(     Help.NVL(io_ProbeSchema.getComment()));
            io_ProbeSchema.getName().setCreateUserID(Help.NVL(io_ProbeSchema.getCreateUserID() ,io_ProbeSchema.getUserID()));
            io_ProbeSchema.getName().setUpdateUserID(Help.NVL(io_ProbeSchema.getUpdateUserID() ,io_ProbeSchema.getUserID()));
            io_ProbeSchema.getName().setIsDel(       Help.NVL(io_ProbeSchema.getName().getIsDel() ,0));
        }
        else
        {
            if ( v_Old == null )
            {
                v_Old = this.queryByID(io_ProbeSchema.getId());
            }
            
            io_ProbeSchema.setMetas(Help.NVL(io_ProbeSchema.getMetas() ,v_Old.getMetas()));
            io_ProbeSchema.getName().setSchemaID(io_ProbeSchema.getId());
            io_ProbeSchema.getName().setId(          Help.NVL(io_ProbeSchema.getName().getId()        ,v_Old.getName().getId()));
            io_ProbeSchema.getName().setTableName(   Help.NVL(io_ProbeSchema.getName().getTableName() ,v_Old.getName().getTableName()));
            io_ProbeSchema.getName().setComment(     Help.NVL(io_ProbeSchema.getComment()             ,v_Old.getName().getComment()));
            io_ProbeSchema.getName().setCreateUserID(Help.NVL(io_ProbeSchema.getCreateUserID()        ,io_ProbeSchema.getUserID()));
            io_ProbeSchema.getName().setUpdateUserID(Help.NVL(io_ProbeSchema.getUpdateUserID()        ,io_ProbeSchema.getUserID()));
            io_ProbeSchema.getName().setIsDel(       Help.NVL(io_ProbeSchema.getName().getIsDel()     ,v_Old.getName().getIsDel()));
        }
        
        if ( !Help.isNull(io_ProbeSchema.getMetas()) )
        {
            // 所有目标的数据探针的结构ID指向数据探针的结构本体
            int v_OrderBy  = 0;
            for (ProbeSchemaMetaDomain v_Item : io_ProbeSchema.getMetas())
            {
                if ( Help.isNull(v_Item.getMname()) )
                {
                    return null;
                }
                
                v_Item.setMname(v_Item.getMname().trim());
                if ( v_Item.getMname().length() <= 1 )
                {
                    // TODO 将异常信息返回给外界
                    return null;
                }
                if ( ProbeKeyword.get(v_Item.getMname()) != null )
                {
                    // TODO 将异常信息返回给外界
                    return null;
                }
                
                v_Item.setId("ProbeMeta" + StringHelp.getUUID9n());
                v_Item.setMformat( v_Item.getMformat()  == null ? MetaFormat.String : v_Item.getMformat());
                v_Item.setMlength( v_Item.getMlength()  == null ? 0     : v_Item.getMlength());
                v_Item.setMdecimal(v_Item.getMdecimal() == null ? 0     : v_Item.getMdecimal());
                v_Item.setPk(      v_Item.getPk()       == null ? false : v_Item.getPk());
                v_Item.setNullable(v_Item.getNullable() == null ? true  : v_Item.getNullable());
                v_Item.setSchemaID(    io_ProbeSchema.getId());
                v_Item.setNameID(      io_ProbeSchema.getNameID());
                v_Item.setCreateUserID(io_ProbeSchema.getCreateUserID());
                v_Item.setUpdateUserID(io_ProbeSchema.getUpdateUserID());
                v_Item.setIsDel(       io_ProbeSchema.getIsDel());
                v_Item.setOrderBy(++v_OrderBy);
                
                if ( v_Item.getPk() )
                {
                    v_Item.setNullable(false);
                }
            }
            
            if ( io_ProbeSchema.getName().getMetasNames().size() != v_OrderBy * 2 )
            {
                // 字段名称与名称代号有雷同，请更换字段命名。 TODO 将异常信息返回给外界
                return null;
            }
        }
        else 
        {
            return null;
        }
        
        io_ProbeSchema.getName().setMetaCode(this.metaCode(io_ProbeSchema.getType() ,io_ProbeSchema.getMetas()));
        
        boolean                            v_Change      = false;  // 新旧在整体层面上是否有变化 
        boolean                            v_CreateMetas = false;  // 是否创建本体结构，即创建本体数据对应的表
        Map<String ,ProbeSchemaNameDomain> v_AllName     = v_Old == null ? null : v_Old.getAllName();
        if ( Help.isNull(v_AllName) )
        {
            v_Change      = true;
            v_CreateMetas = true;
        }
        else 
        {
            ProbeSchemaNameDomain v_OldName = v_AllName.get(io_ProbeSchema.getName().getMetaCode());
            if ( v_OldName == null )
            {
                v_Change      = true;
                v_CreateMetas = true;
                
                io_ProbeSchema.getName().setId("ProbeName" + StringHelp.getUUID9n());
                for (ProbeSchemaMetaDomain v_Item : io_ProbeSchema.getMetas())
                {
                    if ( Help.isNull(v_Item.getMname()) )
                    {
                        return null;
                    }
                    v_Item.setNameID(io_ProbeSchema.getNameID());
                }
            }
            else
            {
                io_ProbeSchema.setName(v_OldName);
                
                // 判定是否有变化
                if ( !v_Old.getNameID().equals(io_ProbeSchema.getNameID()) )
                {
                    v_Change = true;
                }
                if ( !Help.isNull(io_ProbeSchema.getComment()) 
                  && !io_ProbeSchema.getComment().equals(v_Old.getComment()) )
                {
                    io_ProbeSchema.getName().setComment(io_ProbeSchema.getComment());
                    v_Change = true;
                }
            }
        }
        
        if ( v_CreateMetas )
        {
            io_ProbeSchema.getName().setTableName("TProbeData" + Date.getNowTime().getFull_ID());
            if ( this.createProbeDataTable(io_ProbeSchema) )
            {
                $Logger.info("保存数据探针的本体表结构成功：" + io_ProbeSchema.getComment());
            }
            else
            {
                // TODO 将异常信息返回给外界
                return null;
            }
        }
        
        if ( io_ProbeSchema.getIsDel() >= 1 )
        {
            io_ProbeSchema.setCreateMetas(true);
        }
        
        io_ProbeSchema.setCreateMetas(v_CreateMetas);
        boolean v_Ret = true;
        if ( v_Change )
        {
            v_Ret = this.probeSchemaDAO.save(io_ProbeSchema.gatData() ,DataUtil.toData(io_ProbeSchema.getMetas()));
            if ( v_Ret )
            {
                if ( v_Old != null )
                {
                    io_ProbeSchema.setXidOld(v_Old.getXid());
                    io_ProbeSchema.setAllName(v_AllName);
                }
                else
                {
                    io_ProbeSchema.setAllName(new LinkedHashMap<String ,ProbeSchemaNameDomain>());
                }
                
                io_ProbeSchema.gatData().getAllName().put(io_ProbeSchema.getName().getMetaCode() ,io_ProbeSchema.getName().gatData());
                this.probeSchemaCache.refreshXObject(io_ProbeSchema);
                
                $Logger.info("保存数据探针的本体元数据成功：" + io_ProbeSchema.getComment());
            }
        }
        
        return v_Ret ? io_ProbeSchema : null;
    }
    
    
    
    
    /**
     * 生成本体结构串码
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-26
     * @version     v1.0
     *
     * @param i_Type   归类
     * @param i_Metas  本体结构
     * @return
     */
    private String metaCode(String i_Type ,List<ProbeSchemaMetaDomain> i_Metas)
    {
        StringBuilder v_Buffer = new StringBuilder();
        
        v_Buffer.append(i_Type).append(":");
        
        for (ProbeSchemaMetaDomain v_Item : i_Metas)
        {
            v_Buffer.append(v_Item.toString());
        }
        
        v_Buffer.toString();
        
        IHash v_MM = new Hash();
        return v_MM.encrypt(v_Buffer.toString());
    }
    
    
    
    /**
     * 创建数据探针的原数据表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-26
     * @version     v1.0
     *
     * @param i_ProbeSchema  数据探针的结构
     * @return               返回是否创建成功
     */
    private boolean createProbeDataTable(ProbeSchemaDomain i_ProbeSchema)
    {
        String        v_SQLText = this.probeData.getContent().getSqlText();
        int           v_BIndex  = v_SQLText.indexOf(":STRING");
        int           v_EIndex  = v_SQLText.indexOf(");");
        String []     v_Fields  = v_SQLText.substring(v_BIndex ,v_EIndex).trim().split("\\)");
        StringBuilder v_SQL     = new StringBuilder();
        
        v_SQL.append(v_SQLText.substring(0 ,v_BIndex));
        
        for (ProbeSchemaMetaDomain v_Item : i_ProbeSchema.getMetas())
        {
            if ( v_Item.getMformat().equals(MetaFormat.Boolean) )
            {
                v_SQL.append(",").append(StringHelp.replaceAll(v_Fields[2].trim() ,":BOOLEAN" ,v_Item.getMname())).append(")");
            }
            else if ( v_Item.getMformat().equals(MetaFormat.Number) )
            {
                v_SQL.append(",").append(StringHelp.replaceAll(v_Fields[1].trim() ,new String[] {":NUMBER" ,"1L" ,"0D"} ,new String[] {v_Item.getMname() ,v_Item.getMlength() + "" ,v_Item.getMdecimal() + ""})).append(")");
            }
            else if ( v_Item.getMformat().equals(MetaFormat.String) )
            {
                v_SQL.append(",").append(StringHelp.replaceAll(v_Fields[0].trim() ,new String[] {":STRING" ,"1L"} ,new String[] {v_Item.getMname() ,v_Item.getMlength() + ""})).append(")");
            }
        }
        
        v_SQL.append(v_SQLText.substring(v_EIndex));
        
        try
        {
            XSQL v_Clone = XJava.getXSQL(this.probeData.getXJavaID() ,true);
            v_Clone.setContent(StringHelp.replaceAll(v_SQL.toString() ,"TProbeData" ,i_ProbeSchema.getName().getTableName()));
            v_Clone.setCreate(i_ProbeSchema.getName().getTableName());
            return true;
        }
        catch (Exception exce)
        {
            return false;
        }
    }
    
}
