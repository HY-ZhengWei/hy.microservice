package org.hy.microservice.common.probe.schema;

import java.util.List;
import java.util.Map;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 操作DAO层：数据探针的结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
@Xjava(id="ProbeSchemaDAO" ,value=XType.XSQL)
public interface IProbeSchemaDAO
{
    
    /**
     * 查询数据探针的结构列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     * 
     * @return
     */
    @Xsql("XSQL_Common_ProbeSchema_Query")
    public Map<String ,ProbeSchema> queryList();
    
    
    
    /**
     * 查询数据探针的结构列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     * 
     * @param i_ProbeSchema  数据探针的结构
     * @return
     */
    @Xsql(id="XSQL_Common_ProbeSchema_Query")
    public Map<String ,ProbeSchema> queryList(ProbeSchema i_ProbeSchema);
    
    
    
    /**
     * 查询数据探针的结构列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     * 
     * @param i_ProbeSchema  数据探针的结构
     * @return
     */
    @Xsql(id="XSQL_Common_ProbeSchema_Query_ByPage" ,paging=true)
    public Map<String ,ProbeSchema> queryByPage(ProbeSchema i_ProbeSchema);
    
    
    
    /**
     * 查询数据探针的结构列表的总记录数
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     *
     * @return
     */
    @Xsql(id="XSQL_Common_ProbeSchema_Count")
    public Long queryCount(ProbeSchema i_ProbeSchema);
    
    
    
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
    @Xsql(id="XSQL_Common_ProbeSchema_Query" ,returnOne=true)
    public ProbeSchema queryByID(@Xparam("id") String i_ID);
    
    
    
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
    @Xsql(id="XSQL_Common_ProbeSchema_Query" ,returnOne=true)
    public ProbeSchema queryByType(@Xparam("type") String i_Type);
    
    
    
    /**
     * 新增、修改、逻辑删除数据探针的结构
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-15
     * @version     v1.0
     *
     * @param i_ProbeSchema       数据探针的结构
     * @param i_ProbeSchemaMetas  数据探针的本体结构的集合
     * @return
     */
    @Xsql("GXSQL_Common_ProbeSchema_Save")
    public boolean save(@Xparam("Schema")      ProbeSchema           i_ProbeSchema
                       ,@Xparam("SchemaMetas") List<ProbeSchemaMeta> i_ProbeSchemaMetas);
    
}
