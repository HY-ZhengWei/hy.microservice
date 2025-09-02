package org.hy.microservice.common.ipSafe;

import java.util.List;

import org.hy.common.TablePartitionRID;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xsql;





/**
 * 系统安全访问IP黑白名单的DAO层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-05-26
 * @version     v1.0
 *              v2.0  2025-09-02  添加：分页查询
 */
@Xjava(id="IPSafeConfigDAO" ,value=XType.XSQL)
public interface IIPSafeConfigDAO
{
    
    /**
     * 新增系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    @Xsql(id="XSQL_Common_IPSafeConfig_Insert" ,batch=true)
    public int insert(IPSafeConfig i_IPSafeConfig);
    
    
    
    /**
     * 更新系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    @Xsql(id="XSQL_Common_IPSafeConfig_Update")
    public int update(IPSafeConfig i_IPSafeConfig);
    
    
    
    /**
     * 按主键，查询系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    @Xsql(id="XSQL_Common_IPSafeConfig_Query_ByID" ,returnOne=true)
    public IPSafeConfig queryByID(IPSafeConfig i_IPSafeConfig);
    
    
    
    /**
     * 按类型，查询系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    @Xsql("XSQL_Common_IPSafeConfig_Query_ByIPType")
    public List<IPSafeConfig> queryByIPType(IPSafeConfig i_IPSafeConfig);
    
    
    
    /**
     * 按类型，查询系统安全访问IP黑白名单（分页查询）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-02
     * @version     v1.0
     * 
     * @param i_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    @Xsql(id="XSQL_Common_IPSafeConfig_Query_ByIPType_ByPage" ,paging=true)
    public List<IPSafeConfig> queryByIPTypeByPage(IPSafeConfig i_IPSafeConfig);
    
    
    
    /**
     * 按类型，查询系统安全访问IP黑白名单的总记录数
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-02
     * @version     v1.0
     *
     * @param i_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    @Xsql(id="XSQL_Common_IPSafeConfig_Query_ByIPType_Count")
    public Long queryByIPTypeCount(IPSafeConfig i_IPSafeConfig);
    
    
    
    /**
     * 按类型，查询系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @return
     */
    @Xsql("XSQL_Common_IPSafeConfig_Query_All")
    public TablePartitionRID<String ,IPSafeConfig> queryAll();
    
}
