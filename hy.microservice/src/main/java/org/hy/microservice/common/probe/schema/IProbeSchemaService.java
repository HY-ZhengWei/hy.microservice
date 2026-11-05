package org.hy.microservice.common.probe.schema;

import java.util.Map;





/**
 * 业务层：数据探针的结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
public interface IProbeSchemaService
{
    
    /**
     * 查询任务数据探针的结构列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     * 
     * @return
     */
    public Map<String ,ProbeSchemaDomain> queryList();
    
    
    
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
    public Map<String ,ProbeSchemaDomain> queryList(ProbeSchemaDomain i_ProbeSchema);
    
    
    
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
    public ProbeSchemaDomain queryByID(String i_ID);
    
    
    
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
    public ProbeSchemaDomain queryByType(String i_Type);
    
    
    
    /**
     * 按XID查询数据探针的结构（带缓存的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     * 
     * @param i_XID  数据探针的结构的XID
     * @return
     */
    public ProbeSchemaDomain queryByXID(String i_XID);
    
    
    
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
    public ProbeSchemaDomain save(ProbeSchemaDomain io_ProbeSchema);
    
}
