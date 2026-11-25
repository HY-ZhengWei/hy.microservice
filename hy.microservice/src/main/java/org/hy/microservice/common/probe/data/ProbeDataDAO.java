package org.hy.microservice.common.probe.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.TablePartitionRID;
import org.hy.common.app.Param;
import org.hy.common.xml.XJava;
import org.hy.common.xml.XSQL;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.common.xml.plugins.XSQLGroup;
import org.hy.common.xml.plugins.XSQLGroupResult;
import org.hy.common.xml.plugins.XSQLNode;

import org.hy.microservice.common.probe.client.AssetID;
import org.hy.microservice.common.probe.enums.MetaFormat;
import org.hy.microservice.common.probe.schema.ProbeSchemaMeta;
import org.hy.microservice.common.probe.schema.ProbeSchemaName;





/**
 * 操作DAO层：数据探针的数据
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-09
 * @version     v1.0
 */
@Xjava
public class ProbeDataDAO implements IProbeDataDAO
{
    
    private static final Logger $Logger = new Logger(ProbeDataDAO.class);
    
    
    
    @Xjava(ref="XSQL_Common_ProbeData_IsExists")
    private XSQL      exists;
    
    @Xjava(ref="XSQL_Common_ProbeData_Insert")
    private XSQL      insert;
    
    @Xjava(ref="XSQL_Common_ProbeData_Update")
    private XSQL      update;
    
    @Xjava(ref="GXSQL_Common_ProbeData_Save")
    private XSQLGroup save;
    
    @Xjava(ref="XSQL_Common_ProbeData_FindAssetID")
    private XSQL      findAssetID;
    
    @Xjava(ref="XSQL_Common_ProbeData_MaxAssetID")
    private XSQL      maxAssetID;
    
    @Xjava(ref="XSQL_Common_ProbeData_Query_RequireSync")
    private XSQL      requireSync;
    
    @Xjava(ref="XSQL_Common_ProbeData_Update_AssetStates")
    private XSQL      updateAssetStates;
    
    @Xjava(ref="XSQL_Common_ProbeData_Query")
    private XSQL      query;
    
    @Xjava(ref="MS_Common_ProbeData_SyncMaxCount")
    private Param     requireSyncMaxCount;
    
    
    
    /**
     * 更新数据资产状态
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-15
     * @version     v1.0
     *
     * @param i_TableName   表名
     * @param i_AssetIDs    多个数据资产编号
     * @param i_AssetState  未同步false； 已同步true
     * @param i_UserID      更新用户ID
     * @return              更新成功数量
     */
    public int updateAssetStates(String i_TableName ,List<String> i_AssetIDs ,boolean i_AssetState ,String i_UserID)
    {
        Map<String ,Object> v_Data = new HashMap<String ,Object>();
        
        v_Data.put("tableName"    ,i_TableName);
        v_Data.put("assetIDs"     ,StringHelp.toString(i_AssetIDs));
        v_Data.put("assetState"   ,i_AssetState ? 1 : 0);
        v_Data.put("updateUserID" ,i_UserID);
        
        return this.updateAssetStates.executeUpdate(v_Data);
    }
    
    
    
    /**
     * 数据探针的数据持久化入库
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-09
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @param i_Data        数据探针的数据
     * @return              是否保存成功
     */
    public boolean save(ProbeSchemaName i_SchemaName ,ProbeData i_Data)
    {
        XSQLGroup       v_XSQLGroup = this.getSave(i_SchemaName);
        XSQLGroupResult v_XSQLGRet  = v_XSQLGroup.executes(i_Data.getData());
        
        if ( v_XSQLGRet.isSuccess() ) 
        {
            // TODO 是否还要进一步检查数据被正确保存，尚有待观察
            return true;
        }
        else
        {
            $Logger.error(v_XSQLGRet.getException());
            return false;
        }
    }
    
    
    
    /**
     * 创建一个新的数据探针数据的XSQL组对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-09
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @return
     */
    private synchronized XSQLGroup getSave(ProbeSchemaName i_SchemaName)
    {
        String    v_MySaveXID = StringHelp.replaceAll(this.save.getXJavaID() ,"ProbeData" ,i_SchemaName.getTableName());
        XSQLGroup v_MySave    = XJava.getXSQLGroup(v_MySaveXID);
        
        if ( v_MySave != null )
        {
            return v_MySave;
        }
        
        XSQL v_MyInsert = this.newInsert(i_SchemaName);
        XSQL v_MyUpdate = this.newUpdate(i_SchemaName);
        
        XSQLNode v_MyExistsNode = new XSQLNode();
        v_MyExistsNode.setSql(this.exists);
        v_MyExistsNode.setType(XSQLNode.$Type_Query);
        v_MyExistsNode.setOneConnection(true);
        
        XSQLNode v_MyInsertNode = new XSQLNode();
        v_MyInsertNode.setSql(v_MyInsert);
        v_MyInsertNode.setType(XSQLNode.$Type_ExecuteUpdate);
        v_MyInsertNode.setCondition(this.save.getXsqlNodes().get(1).getCondition());
        
        XSQLNode v_MyUpdateNode = new XSQLNode();
        v_MyUpdateNode.setSql(v_MyUpdate);
        v_MyUpdateNode.setType(XSQLNode.$Type_ExecuteUpdate);
        v_MyUpdateNode.setCondition(this.save.getXsqlNodes().get(2).getCondition());
        
        v_MySave = new XSQLGroup();
        v_MySave.setXJavaID(v_MySaveXID);
        v_MySave.setComment(this.save.getComment());
        v_MySave.setSqlNode(v_MyExistsNode);
        v_MySave.setSqlNode(v_MyInsertNode);
        v_MySave.setSqlNode(v_MyUpdateNode);
        
        XJava.putObject(v_MySaveXID ,v_MySave);
        return v_MySave;
    }
    
    
    
    /**
     * 创建一个新的数据探针数据的Insert对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-09
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @return
     */
    private XSQL newInsert(ProbeSchemaName i_SchemaName)
    {
        String        v_MyInsertXID = StringHelp.replaceAll(this.insert.getXJavaID() ,"ProbeData" ,i_SchemaName.getTableName());
        XSQL          v_MyInsert    = XJava.getXSQL(this.insert.getXJavaID() ,true);
        String        v_SQL         = v_MyInsert.getContentDB().getSqlText();
        StringBuilder v_Names       = new StringBuilder();
        StringBuilder v_Values      = new StringBuilder();
        
        for (ProbeSchemaMeta v_Meta : i_SchemaName.getMetas())
        {
            v_Names .append(",")  .append(v_Meta.getMname());
            v_Values.append(",':").append(v_Meta.getMname()).append("'");
        }
        
        v_SQL = StringHelp.replaceAll(v_SQL ,new String[] {":tableName" 
                                                          ,":1DataName" 
                                                          ,":1DataValue"} 
                                            ,new String[] {i_SchemaName.getTableName()
                                                          ,v_Names .toString()
                                                          ,v_Values.toString()});
        v_MyInsert.getContentDB().setSqlText(v_SQL);
        v_MyInsert.setXJavaID(v_MyInsertXID);
        XJava.putObject(v_MyInsertXID ,v_MyInsert);
        return v_MyInsert;
    }
    
    
    
    /**
     * 创建一个新的数据探针数据的Update对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-09
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @return
     */
    private XSQL newUpdate(ProbeSchemaName i_SchemaName)
    {
        String        v_MyUpdateXID = StringHelp.replaceAll(this.update.getXJavaID() ,"ProbeData" ,i_SchemaName.getTableName());
        XSQL          v_MyUpdate    = XJava.getXSQL(this.update.getXJavaID() ,true);
        String        v_SQL         = v_MyUpdate.getContentDB().getSqlText();
        StringBuilder v_Sets        = new StringBuilder();
        
        for (ProbeSchemaMeta v_Meta : i_SchemaName.getMetas())
        {
            v_Sets.append("<[ ,").append(v_Meta.getMname()).append(" = ':").append(v_Meta.getMname()).append("' ]> ");
        }
        
        v_SQL = StringHelp.replaceAll(v_SQL ,new String[] {":tableName" 
                                                          ,":1SetS"} 
                                            ,new String[] {i_SchemaName.getTableName()
                                                          ,v_Sets.toString()});
        v_MyUpdate.getContentDB().setSqlText(v_SQL);
        v_MyUpdate.setXJavaID(v_MyUpdateXID);
        XJava.putObject(v_MyUpdateXID ,v_MyUpdate);
        return v_MyUpdate;
    }
    
    
    
    /**
     * 获取一个用于查询列表数据的XSQL
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-24
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @return
     */
    private synchronized XSQL getQuery(ProbeSchemaName i_SchemaName)
    {
        String v_MyQueryXID = StringHelp.replaceAll(this.query.getXJavaID() ,"ProbeData" ,i_SchemaName.getTableName());
        XSQL   v_MyQuery    = XJava.getXSQL(v_MyQueryXID);
        
        if ( v_MyQuery != null )
        {
            return v_MyQuery;
        }
        
        v_MyQuery = XJava.getXSQL(this.query.getXJavaID() ,true);
        String        v_SQL   = v_MyQuery.getContentDB().getSqlText();
        StringBuilder v_Names = new StringBuilder();
        StringBuilder v_Where = new StringBuilder();
        
        for (ProbeSchemaMeta v_Meta : i_SchemaName.getMetas())
        {
            v_Names.append(",").append(v_Meta.getMname()).append(" AS \"").append(v_Meta.getMname()).append("\" ");
            
            if ( MetaFormat.String.getValue().equals(v_Meta.getMformat()) )
            {
                v_Where.append(" <[ AND ").append(v_Meta.getMname()).append(" = ':").append(v_Meta.getMname()).append("' ]> ");
            }
            else
            {
                v_Where.append(" <[ AND ").append(v_Meta.getMname()).append(" = :") .append(v_Meta.getMname()).append(" ]> ");
            }
        }
        
        v_SQL = StringHelp.replaceAll(v_SQL ,new String[] {":tableName" 
                                                          ,":1DataName"
                                                          ,":1WhereNV"} 
                                            ,new String[] {i_SchemaName.getTableName()
                                                          ,v_Names.toString()
                                                          ,v_Where.toString()});
        v_MyQuery.getContentDB().setSqlText(v_SQL);
        v_MyQuery.setXJavaID(v_MyQueryXID);
        XJava.putObject(v_MyQueryXID ,v_MyQuery);
        return v_MyQuery;
    }
    
    
    
    /**
     * 获取一个用于查询需要同步的数据的XSQL
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-13
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @param i_Source      来源
     * @return
     */
    private synchronized XSQL getRequireSync(ProbeSchemaName i_SchemaName ,String i_Source)
    {
        String v_MyQueryXID = StringHelp.replaceAll(this.requireSync.getXJavaID() ,"ProbeData" ,i_SchemaName.getTableName());
        XSQL   v_MyQuery    = XJava.getXSQL(v_MyQueryXID);
        
        if ( v_MyQuery != null )
        {
            return v_MyQuery;
        }
        
        v_MyQuery = XJava.getXSQL(this.requireSync.getXJavaID() ,true);
        String        v_SQL   = v_MyQuery.getContentDB().getSqlText();
        StringBuilder v_Names = new StringBuilder();
        
        for (ProbeSchemaMeta v_Meta : i_SchemaName.getMetas())
        {
            v_Names.append(",").append(v_Meta.getMname());
        }
        
        v_SQL = StringHelp.replaceAll(v_SQL ,new String[] {":tableName" 
                                                          ,":1DataName"
                                                          ,":Source"
                                                          ,":MaxCount"} 
                                            ,new String[] {i_SchemaName.getTableName()
                                                          ,v_Names.toString()
                                                          ,i_Source
                                                          ,"" + (this.requireSyncMaxCount != null ? Help.NVL(this.requireSyncMaxCount.getValueInt() ,100) : 100)});
        v_MyQuery.getContentDB().setSqlText(v_SQL);
        v_MyQuery.setXJavaID(v_MyQueryXID);
        XJava.putObject(v_MyQueryXID ,v_MyQuery);
        return v_MyQuery;
    }
    
    
    
    /**
     * 获取一个用于查询主键的XSQL
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-22
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @return
     */
    private synchronized XSQL getFindAssetID(ProbeSchemaName i_SchemaName)
    {
        String v_MyQueryXID = StringHelp.replaceAll(this.findAssetID.getXJavaID() ,"ProbeData" ,i_SchemaName.getTableName());
        XSQL   v_MyQuery    = XJava.getXSQL(v_MyQueryXID);
        
        if ( v_MyQuery != null )
        {
            return v_MyQuery;
        }
        
        v_MyQuery = XJava.getXSQL(this.findAssetID.getXJavaID() ,true);
        String        v_SQL   = v_MyQuery.getContentDB().getSqlText();
        StringBuilder v_Names = new StringBuilder();
        int           v_Count = 0;
        
        for (ProbeSchemaMeta v_Meta : i_SchemaName.getMetas())
        {
            if ( v_Meta.getPk() == null || v_Meta.getPk() < 1 )
            {
                continue;
            }
            
            v_Names.append(" AND ").append(v_Meta.getMname()).append(" = ");
            if ( MetaFormat.String.getValue().equals(v_Meta.getMformat()) )
            {
                v_Names.append("':").append(v_Meta.getMname()).append("'");
            }
            else
            {
                v_Names.append(":").append(v_Meta.getMname());
            }
            v_Count++;
        }
        
        if ( v_Count <= 0 )
        {
            v_SQL = "";  // 空即无效
        }
        else
        {
            v_SQL = StringHelp.replaceAll(v_SQL ,new String[] {":tableName" 
                                                              ,":1WhereNV"} 
                                                ,new String[] {i_SchemaName.getTableName()
                                                              ,v_Names.toString()});
        }
        v_MyQuery.getContentDB().setSqlText(v_SQL);
        v_MyQuery.setXJavaID(v_MyQueryXID);
        XJava.putObject(v_MyQueryXID ,v_MyQuery);
        return v_MyQuery;
    }
    
    
    
    /**
     * 获取一个用于查询最大数据资产编号的XSQL
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-25
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @param i_Source      来源
     * @return
     */
    private synchronized XSQL getMaxAssetID(ProbeSchemaName i_SchemaName ,String i_Source)
    {
        String v_MyQueryXID = StringHelp.replaceAll(this.maxAssetID.getXJavaID() ,"ProbeData" ,i_SchemaName.getTableName());
        XSQL   v_MyQuery    = XJava.getXSQL(v_MyQueryXID);
        
        if ( v_MyQuery != null )
        {
            return v_MyQuery;
        }
        
        v_MyQuery = XJava.getXSQL(this.maxAssetID.getXJavaID() ,true);
        String v_SQL = v_MyQuery.getContentDB().getSqlText();
        
        v_SQL = StringHelp.replaceAll(v_SQL ,new String[] {":tableName" 
                                                          ,":Source"} 
                                            ,new String[] {i_SchemaName.getTableName()
                                                          ,i_Source});
        
        v_MyQuery.getContentDB().setSqlText(v_SQL);
        v_MyQuery.setXJavaID(v_MyQueryXID);
        XJava.putObject(v_MyQueryXID ,v_MyQuery);
        return v_MyQuery;
    }
    
    
    
    /**
     * 按用户主键查数据资产编号
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-22
     * @version     v1.0
     *
     * @param <D>
     * @param i_SchemaName  数据探针的结构
     * @param i_Data        数据
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public String queryAssetIDByPK(ProbeSchemaName i_SchemaName ,Object i_Data)
    {
        XSQL v_Query = this.getFindAssetID(i_SchemaName);
        if ( v_Query == null || Help.isNull(v_Query.getContent().getSqlText()) )
        {
            return null;
        }
        
        List<List<String>> v_QueryRet = (List<List<String>>) v_Query.query(i_Data);
        if ( Help.isNull(v_QueryRet) )
        {
            return null;
        }
        else
        {
            if ( Help.isNull(v_QueryRet.get(0)) )
            {
                return null;
            }
            else if ( Help.isNull(v_QueryRet.get(0).get(0)) )
            {
                return null;
            }
        }
        
        return v_QueryRet.get(0).get(0);
    }
    
    
    
    /**
     * 查当前表中最大的数据资产编号
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-12
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @param i_Source      数据资产编号中的来源
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public AssetID queryMaxAssetID(ProbeSchemaName i_SchemaName ,String i_Source)
    {
        List<List<String>> v_QueryRet = (List<List<String>>) this.getMaxAssetID(i_SchemaName ,i_Source).query(i_SchemaName);
        
        if ( Help.isNull(v_QueryRet) )
        {
            return new AssetID();
        }
        else
        {
            if ( Help.isNull(v_QueryRet.get(0)) )
            {
                return new AssetID();
            }
            else if ( Help.isNull(v_QueryRet.get(0).get(0)) )
            {
                return new AssetID();
            }
        }
        
        return new AssetID(StringHelp.replaceLast(v_QueryRet.get(0).get(0) ,i_Source ,""));
    }
    
    
    
    /**
     * 查询需要同步的数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-13
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @param i_Source      来源
     * @return              按用户ID分区，按数据资产编号为主键
     */
    @SuppressWarnings("unchecked")
    @Override
    public TablePartitionRID<String ,Map<String ,Object>> queryRequireSync(ProbeSchemaName i_SchemaName ,String i_Source)
    {
        XSQL v_Query = this.getRequireSync(i_SchemaName ,i_Source);
        return (TablePartitionRID<String ,Map<String ,Object>>) v_Query.query();
    }
    
    
    
    /**
     * 查询列表数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-24
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @param i_QueryData   查询条件
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String ,Object>> query(ProbeSchemaName i_SchemaName ,Object i_QueryData)
    {
        XSQL v_Query = this.getQuery(i_SchemaName);
        return (List<Map<String ,Object>>) v_Query.query(i_QueryData);
    }
    
}
