package org.hy.microservice.common.probe.schema;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.common.cache.CacheFactory;
import org.hy.microservice.common.cache.ICache;





/**
 * 缓存层：数据探针的结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-13
 * @version     v1.0
 */
@Xjava
public class ProbeSchemaCache implements IProbeSchemaCache
{
    
    /** 缓存的表名称 */
    private static final String $RTable = "TProbeSchema";
    
    
    
    /** 缓存对象 */
    private ICache<ProbeSchema> cache;
    
    /** 缓存的数据库名称。默认使用微服务的服务名称 */
    @Xjava(ref="MS_Common_ServiceName")
    private Param               cacheDatabase;
    
    
    
    public ProbeSchemaCache()
    {
        boolean v_UseRemote = "启用".equals(XJava.getParam("MS_Common_IsRedis").getValue());
        this.cache = CacheFactory.newInstanceOf(v_UseRemote ,ProbeSchema.class);
    }
    
    
    
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
    @Override
    public ProbeSchema get(String i_XID)
    {
        return this.cache.get(this.cacheDatabase.getValue() ,$RTable ,i_XID);
    }
    
    
    
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
    @Override
    public boolean refreshXObject(ProbeSchemaDomain i_ProbeSchema)
    {
        // 删除
        if ( !i_ProbeSchema.getIsDel().equals(0) )
        {
            if ( !Help.isNull(i_ProbeSchema.getXidOld()) )
            {
                ProbeSchema v_Old = this.cache.get(this.cacheDatabase.getValue() ,$RTable ,i_ProbeSchema.getXidOld());
                if ( v_Old != null )
                {
                    refreshRelations(new ProbeSchemaDomain(v_Old) ,null);
                    this.cache.remove(this.cacheDatabase.getValue() ,$RTable ,i_ProbeSchema.getXidOld());
                }
            }
            
            if ( !Help.isNull(i_ProbeSchema.getXid()) )
            {
                ProbeSchema v_Old = this.cache.get(this.cacheDatabase.getValue() ,$RTable ,i_ProbeSchema.getXid());
                if ( v_Old != null )
                {
                    refreshRelations(new ProbeSchemaDomain(v_Old) ,null);
                    this.cache.remove(this.cacheDatabase.getValue() ,$RTable ,i_ProbeSchema.getXid());
                }
            }
        }
        // 修改 & 创建
        else
        {
            ProbeSchema v_Old = null;
            // 防止修改Xid，先删除原Xid
            if ( !Help.isNull(i_ProbeSchema.getXidOld()) )
            {
                if ( !i_ProbeSchema.getXidOld().equals(i_ProbeSchema.getXid()) )
                {
                    v_Old = this.cache.get(this.cacheDatabase.getValue() ,$RTable ,i_ProbeSchema.getXidOld());
                    if ( v_Old != null )
                    {
                        this.cache.remove(this.cacheDatabase.getValue() ,$RTable ,i_ProbeSchema.getXidOld());
                    }
                }
            }
            
            // 查询到旧数据
            if ( v_Old == null )
            {
                v_Old = this.cache.get(this.cacheDatabase.getValue() ,$RTable ,i_ProbeSchema.getXid());
            }
            
            // 创建
            this.cache.save(this.cacheDatabase.getValue() ,$RTable ,i_ProbeSchema.getXid() ,i_ProbeSchema.gatData());
            if ( v_Old != null )
            {
                // 替换原来的
                this.refreshRelations(new ProbeSchemaDomain(v_Old) ,i_ProbeSchema);
            }
        }
        
        return true;
    }
    
    
    
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
    @Override
    public boolean refreshRelations(ProbeSchemaDomain i_Old ,ProbeSchemaDomain i_New)
    {
        return true;
    }
    
}
