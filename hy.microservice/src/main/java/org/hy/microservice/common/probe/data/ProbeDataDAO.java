package org.hy.microservice.common.probe.data;

import org.hy.common.StringHelp;
import org.hy.common.xml.XJava;
import org.hy.common.xml.XSQL;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.common.xml.plugins.XSQLGroup;
import org.hy.common.xml.plugins.XSQLGroupResult;
import org.hy.common.xml.plugins.XSQLNode;

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
            v_Sets .append("<[ ,").append(v_Meta.getMname()).append(" = ':").append(v_Meta.getMname()).append("' ]> ");
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
    
}
