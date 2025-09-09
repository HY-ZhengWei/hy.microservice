package org.hy.microservice.common.ipSafe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.TablePartitionRID;
import org.hy.common.app.Param;
import org.hy.common.xml.annotation.Xjava;





/**
 * 系统安全访问IP黑白名单的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-05-26
 * @version     v1.0
 *              v2.0  2025-09-02  添加：分页查询
 */
@Xjava
public class IPSafeConfigService implements IIPSafeConfigService ,Serializable
{
    
    private static final long serialVersionUID = -8491749740722409103L;
    
    private static TablePartitionRID<String ,IPSafeConfig> $CacheIPSafes = new TablePartitionRID<String ,IPSafeConfig>();
    
    /**
     * IP黑白名单的内存命中缓存。
     * 
     * 提高判定命中性能，不用每次判定均要执行12次Map.get(key)
     * 
     * Map.key为getIpSafeKey()，
     * Map.value为IPSafeConfig.$Type_BackList或$Type_WhiteList
     */
    private static final Map<String ,String>               $IPSafeHits   = new HashMap<String ,String>();
    
    
    
    @Xjava
    private IIPSafeConfigDAO ipSafeConfigDAO;
    
    @Xjava(ref="MS_Common_PagePerCount")
    private Param            pagePerCount;
    
    

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
     * 按类型，查询系统安全访问IP黑白名单（分页查询）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-02
     * @version     v1.0
     * 
     * @param io_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    public List<IPSafeConfig> queryByIPTypeByPage(IPSafeConfig io_IPSafeConfig)
    {
        long v_DefPagePerCount = this.pagePerCount.getValueLong();
        
        io_IPSafeConfig.setPageIndex(Help.NVL(io_IPSafeConfig.getPageIndex() ,1L));
        io_IPSafeConfig.setPagePerCount(Help.min(Help.NVL(io_IPSafeConfig.getPagePerCount() ,v_DefPagePerCount) ,v_DefPagePerCount));
        return this.ipSafeConfigDAO.queryByIPTypeByPage(io_IPSafeConfig);
    }
    
    
    
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
    public Long queryByIPTypeCount(IPSafeConfig i_IPSafeConfig)
    {
        return this.ipSafeConfigDAO.queryByIPTypeCount(i_IPSafeConfig);
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
        TablePartitionRID<String ,IPSafeConfig> v_Datas = this.ipSafeConfigDAO.queryAll();
        TablePartitionRID<String ,IPSafeConfig> v_IPs   = new TablePartitionRID<String ,IPSafeConfig>();
        
        Map<String ,IPSafeConfig> v_BackList = v_Datas.get(IPSafeConfig.$Type_BackList);
        for (IPSafeConfig v_IP : v_BackList.values())
        {
            if ( Help.isNull(v_IP.getIpMax()) )
            {
                v_IPs.putRow(v_IP.getIpType() ,v_IP.getIpSafeKey() ,v_IP);
                continue;
            }
            
            if ( v_IP.getIp().endsWith(".0") && v_IP.getIp().endsWith(".255") )
            {
                long v_IPStart = ipToLong(v_IP.getIp());
                long v_IPEnd   = ipToLong(v_IP.getIpMax());
                
                do
                {
                    IPSafeConfig v_New = new IPSafeConfig();
                    v_New.initNotNull(v_IP);
                    v_New.setIp(ipToString3(v_IPStart));
                    v_New.setIpMax(null);
                    v_IPs.putRow(v_New.getIpType() ,v_New.getIpSafeKey() ,v_New);
                    
                    v_IPStart += 255;
                } while ( v_IPStart < v_IPEnd );
            }
            else
            {
                long v_IPStart = ipToLong(v_IP.getIp());
                long v_IPEnd   = ipToLong(v_IP.getIpMax());
                
                for (long v_IPIndex=v_IPStart; v_IPIndex<=v_IPEnd; v_IPIndex++)
                {
                    IPSafeConfig v_New = new IPSafeConfig();
                    v_New.initNotNull(v_IP);
                    v_New.setIp(ipToString(v_IPIndex));
                    v_New.setIpMax(null);
                    v_IPs.putRow(v_New.getIpType() ,v_New.getIpSafeKey() ,v_New);
                }
            }
        }
        
        Map<String ,IPSafeConfig> v_WhiteList = v_Datas.get(IPSafeConfig.$Type_WhiteList);
        for (IPSafeConfig v_IP : v_WhiteList.values())
        {
            if ( Help.isNull(v_IP.getIpMax()) )
            {
                v_IPs.putRow(IPSafeConfig.$Type_WhiteList ,v_IP.getIpSafeKey() ,v_IP);
                continue;
            }
            
            if ( v_IP.getIp().endsWith(".0") && v_IP.getIp().endsWith(".255") )
            {
                long v_IPStart = ipToLong(v_IP.getIp());
                long v_IPEnd   = ipToLong(v_IP.getIpMax());
                
                do
                {
                    IPSafeConfig v_New = new IPSafeConfig();
                    v_New.initNotNull(v_IP);
                    v_New.setIp(ipToString3(v_IPStart));
                    v_New.setIpMax(null);
                    v_IPs.putRow(v_New.getIpType() ,v_New.getIpSafeKey() ,v_New);
                    
                    v_IPStart += 255;
                } while ( v_IPStart < v_IPEnd );
            }
            else
            {
                long v_IPStart = ipToLong(v_IP.getIp());
                long v_IPEnd   = ipToLong(v_IP.getIpMax());
                
                for (long v_IPIndex=v_IPStart; v_IPIndex<=v_IPEnd; v_IPIndex++)
                {
                    IPSafeConfig v_New = new IPSafeConfig();
                    v_New.initNotNull(v_IP);
                    v_New.setIp(ipToString(v_IPIndex));
                    v_New.setIpMax(null);
                    v_IPs.putRow(v_New.getIpType() ,v_New.getIpSafeKey() ,v_New);
                }
            }
        }
        
        $CacheIPSafes = v_IPs;
        $IPSafeHits.clear();  // 清空命中的历史信息
        return $CacheIPSafes;
    }
    
    
    
    /**
     * IP地址转为数字
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-08
     * @version     v1.0
     *
     * @param i_IP
     * @return
     */
    public final static long ipToLong(String i_IP)
    {
        String [] v_Parts = i_IP.split("\\.");
        long v_Value = 0;
        for (int i = 0; i < 4; i++)
        {
            v_Value = v_Value << 8 | Integer.parseInt(v_Parts[i]);
        }
        return v_Value;
    }
    
    
    
    /**
     * IP地址（数字形式）转为字符
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-08
     * @version     v1.0
     *
     * @param i_IP
     * @return
     */
    public final static String ipToString(long i_IP) 
    {
        return ((i_IP >> 24) & 0xFF) + "." +
               ((i_IP >> 16) & 0xFF) + "." +
               ((i_IP >> 8)  & 0xFF) + "." +
                (i_IP        & 0xFF);
    }
    
    
    
    /**
     * IP地址（数字形式）转为字符，但只有三段IP地址位
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-08
     * @version     v1.0
     *
     * @param i_IP
     * @return
     */
    public final static String ipToString3(long i_IP) 
    {
        return ((i_IP >> 24) & 0xFF) + "." +
               ((i_IP >> 16) & 0xFF) + "." +
               ((i_IP >> 8)  & 0xFF) + ".";
    }
    
    
    
    /**
     * 添加IP黑白名单的命中信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-08
     * @version     v1.0
     *
     * @param i_IpSafeKey
     * @param i_IPSafeType
     */
    public void putIPSafeHit(String i_IpSafeKey ,String i_IPSafeType)
    {
        $IPSafeHits.put(i_IpSafeKey ,i_IPSafeType);
    }
    
    
    
    /**
     * 获取IP黑白名单的命中信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-08
     * @version     v1.0
     *
     * @param i_IpSafeKey
     * @return
     */
    public String getIPSafeHit(String i_IpSafeKey)
    {
        return $IPSafeHits.get(i_IpSafeKey);
    }
    
}
