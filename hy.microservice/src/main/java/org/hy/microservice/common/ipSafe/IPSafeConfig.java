package org.hy.microservice.common.ipSafe;

import org.hy.common.Help;
import org.hy.microservice.common.BaseViewMode;





/**
 * 系统安全访问IP黑白名单
 * 
 * IP地址支持：IP段
 * 黑白名单判定优先级：黑名单 > 白名单 > 接口URL > 接口模块 > IP地址
 * 
 *   举例1：当配置IP 127.0.0.1 的 "接口URL /A/B/C" 为配置黑名单时，无论是否有白名单，均拒绝IP 127.0.0.1的访问 "接口/A/B/C
 *   举例2：当配置IP 127.0.0.1 的 "接口URL /A/B/C" 为白名单，且无其它黑名单时，"接口/A/B/C" 仅允许此IP 127.0.0.1访问，其它IP无权访问
 *   举例3：当 "接口URL /A/B/C" 未配置白名单，且无黑名单时，允许任何系统访问
 * 
 * @author      ZhengWei(HY)
 * @createDate  2023-05-26
 * @version     v1.0
 *              v2.0  2023-08-08  添加：接口模块编号和URL地址，允许更细粒度的控制黑白名单
 */
public class IPSafeConfig extends BaseViewMode
{
    
    private static final long serialVersionUID = 8155182081994238170L;
    
    
    /** 黑名单 */
    public  static final String $Type_BackList  = "blacklist";
    
    /** 白名单 */
    public  static final String $Type_WhiteList = "whitelist";
    
    
    
    /** 主键 */
    private String id;
    
    /** IP类型 */
    private String ipType;
    
    /** IP地址 */
    private String ip;
    
    /** IP地址范围的最大值 */
    private String ipMax;
    
    /** 接口模块编号 */
    private String moduleCode;
                                    
    /** 接口URL地址 */
    private String url;
    
    
    
    /**
     * IP+模块+URL组合成的Map集合的Key。
     * 
     * 主要用于从Map中快速匹配到黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-08
     * @version     v1.0
     *
     * @return
     */
    public String getIpSafeKey()
    {
        StringBuilder v_Buffer = new StringBuilder();
        
        v_Buffer.append(this.ip);
        
        if ( !Help.isNull(this.moduleCode) )
        {
            v_Buffer.append("@");
            v_Buffer.append(this.moduleCode);
        }
        
        if ( !Help.isNull(this.url) )
        {
            v_Buffer.append("@");
            v_Buffer.append(this.url);
        }
        
        return v_Buffer.toString();
    }
    
    
    /**
     * 获取：主键
     */
    public String getId()
    {
        return id;
    }

    
    /**
     * 设置：主键
     * 
     * @param i_Id 主键
     */
    public void setId(String i_Id)
    {
        this.id = i_Id;
    }

    
    /**
     * 获取：IP类型
     */
    public String getIpType()
    {
        return ipType;
    }

    
    /**
     * 设置：IP类型
     * 
     * @param i_IpType IP类型
     */
    public void setIpType(String i_IpType)
    {
        this.ipType = i_IpType;
    }

    
    /**
     * 获取：IP地址
     */
    public String getIp()
    {
        return ip;
    }

    
    /**
     * 设置：IP地址
     * 
     * @param i_Ip IP地址
     */
    public void setIp(String i_Ip)
    {
        this.ip = i_Ip;
    }

    
    /**
     * 获取：IP地址范围的最大值
     */
    public String getIpMax()
    {
        return ipMax;
    }

    
    /**
     * 设置：IP地址范围的最大值
     * 
     * @param i_IpMax IP地址范围的最大值
     */
    public void setIpMax(String i_IpMax)
    {
        this.ipMax = i_IpMax;
    }


    /**
     * 获取：接口模块编号
     */
    public String getModuleCode()
    {
        return moduleCode;
    }

    
    /**
     * 设置：接口模块编号
     * 
     * @param i_ModuleCode 接口模块编号
     */
    public void setModuleCode(String i_ModuleCode)
    {
        this.moduleCode = i_ModuleCode;
    }


    /**
     * 获取：接口URL地址
     */
    public String getUrl()
    {
        return url;
    }

    
    /**
     * 设置：接口URL地址
     * 
     * @param i_Url 接口URL地址
     */
    public void setUrl(String i_Url)
    {
        this.url = i_Url;
    }
    
}
