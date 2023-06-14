package org.hy.microservice.common.ipSafe;

import java.io.Serializable;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.TablePartitionRID;
import org.hy.common.xml.annotation.Xjava;





/**
 * 系统安全访问IP黑白名单的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-05-26
 * @version     v1.0
 */
@Xjava
public class IPSafeConfigService implements IIPSafeConfigService ,Serializable
{
    
    private static final long serialVersionUID = -8491749740722409103L;
    
    private static TablePartitionRID<String ,IPSafeConfig> $CacheIPSafes = new TablePartitionRID<String ,IPSafeConfig>();
    
    
    
    @Xjava
    private IIPSafeConfigDAO ipSafeConfigDAO;
    
    

    /**
     * 新增系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param io_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    @Override
    public IPSafeConfig insert(IPSafeConfig io_IPSafeConfig)
    {
        io_IPSafeConfig.setId(StringHelp.getUUID());
        io_IPSafeConfig.setCreateUserID(Help.NVL(io_IPSafeConfig.getCreateUserID() ,io_IPSafeConfig.getUserID()));
        io_IPSafeConfig.setIsDel(Help.NVL(io_IPSafeConfig.getIsDel() ,0));
        int v_Ret = this.ipSafeConfigDAO.insert(io_IPSafeConfig);
        
        if ( v_Ret == 1 )
        {
            this.cacheIPSafesRefurbish();
            return io_IPSafeConfig;
        }
        else
        {
            return null;
        }
    }
    
    
    
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
    @Override
    public IPSafeConfig update(IPSafeConfig i_IPSafeConfig)
    {
        int v_Ret = this.ipSafeConfigDAO.update(i_IPSafeConfig);
        
        if ( v_Ret == 1 )
        {
            this.cacheIPSafesRefurbish();
            return i_IPSafeConfig;
        }
        else
        {
            return null;
        }
    }
    
    
    
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
    @Override
    public IPSafeConfig queryByID(IPSafeConfig i_IPSafeConfig)
    {
        return this.ipSafeConfigDAO.queryByID(i_IPSafeConfig);
    }
    
    
    
    /**
     * 按主键，查询系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_ID  主键
     * @return
     */
    @Override
    public IPSafeConfig queryByID(String i_ID)
    {
        IPSafeConfig v_IPSafeConfig = new IPSafeConfig();
        v_IPSafeConfig.setId(i_ID);
        return this.ipSafeConfigDAO.queryByID(v_IPSafeConfig);
    }
    
    
    
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
    @Override
    public List<IPSafeConfig> queryByIPType(IPSafeConfig i_IPSafeConfig)
    {
        return this.ipSafeConfigDAO.queryByIPType(i_IPSafeConfig);
    }
    
    
    
    /**
     * 按类型，查询系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @return
     */
    @Override
    public TablePartitionRID<String ,IPSafeConfig> queryAll()
    {
        if ( Help.isNull($CacheIPSafes) )
        {
            return this.cacheIPSafesRefurbish();
        }
        else
        {
            return $CacheIPSafes;
        }
    }
    
    
    
    /**
     * 刷新黑白名单缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @return
     */
    @Override
    public synchronized TablePartitionRID<String ,IPSafeConfig> cacheIPSafesRefurbish()
    {
        $CacheIPSafes = this.ipSafeConfigDAO.queryAll();
        return $CacheIPSafes;
    }
    
}
