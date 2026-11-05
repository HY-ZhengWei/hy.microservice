package org.hy.microservice.common.probe.schema;





/**
 * 缓存层：数据探针的结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-13
 * @version     v1.0
 */
public interface IProbeSchemaCache
{
    
    /**
     * 用XID获取数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-13
     * @version     v1.0
     *
     * @param i_XID
     * @return
     */
    public ProbeSchema get(String i_XID);
    
    
    
    /**
     * 刷新XObject对象池
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-13
     * @version     v1.0
     *
     * @param i_ProbeSchema
     * @return
     */
    public boolean refreshXObject(ProbeSchemaDomain i_ProbeSchema);
    
    
    
    /**
     * 刷新其它数据对象的引用
     * 
     * 原则：不销毁引用我的对象，仅替换新的我就好。
     * 解绑：无新X对象时，表示移除引用关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-13
     * @version     v1.0
     *
     * @param i_Old  旧的API信息X对象
     * @param i_New  新的API信息X对象
     * @return
     */
    public boolean refreshRelations(ProbeSchemaDomain i_Old ,ProbeSchemaDomain i_New);
    
}
