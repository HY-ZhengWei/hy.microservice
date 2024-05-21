package org.hy.microservice.common.heartbeat;

import org.hy.common.Date;
import org.hy.microservice.common.BaseViewMode;

import com.fasterxml.jackson.annotation.JsonFormat;





/**
 * 心跳信息
 * 
 * @author      ZhengWei(HY)
 * @createDate  2024-01-29
 * @version     v1.0
 */
public class Heartbeat extends BaseViewMode implements Comparable<Heartbeat>
{

    private static final long serialVersionUID = -9053189970318592962L;
    
    
    
    /** 操作系统的类型 */
    private String     osType;
    
    /** 操作系统的当前时间 */
    private String     osTime;
    
    /** 边缘计算服务的启动时间 */
    private String     edgeStartTime;
    
    /** 本服务实例的IP地址 */
    private String     edgeIP;
    
    /** 宿主机的IP地址。如将服务部署在K8s上，为服务所在Docker的宿主机IP */
    private String     hostIP;
    
    /** 本服务实例的版本号 */
    private String     edgeVersion;
    
    /** 获取最近多少秒内的数据 */
    private Integer    second;
    
    /** 认领任务数量 */
    private Integer    claimCount;
    
    /** 任务执行正常的数量 */
    private Integer    taskOKCount;
    
    /** 无效时间（仅记录首次） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date       invalidTime;
    
    
    
    /**
     * 获取：操作系统的类型
     */
    public String getOsType()
    {
        return osType;
    }

    
    /**
     * 设置：操作系统的类型
     * 
     * @param i_OsType 操作系统的类型
     */
    public void setOsType(String i_OsType)
    {
        this.osType = i_OsType;
    }

    
    /**
     * 获取：操作系统的当前时间
     */
    public String getOsTime()
    {
        return osTime;
    }

    
    /**
     * 设置：操作系统的当前时间
     * 
     * @param i_OsTime 操作系统的当前时间
     */
    public void setOsTime(String i_OsTime)
    {
        this.osTime = i_OsTime;
    }

    
    /**
     * 获取：边缘计算服务的启动时间
     */
    public String getEdgeStartTime()
    {
        return edgeStartTime;
    }

    
    /**
     * 设置：边缘计算服务的启动时间
     * 
     * @param i_EdgeStartTime 边缘计算服务的启动时间
     */
    public void setEdgeStartTime(String i_EdgeStartTime)
    {
        this.edgeStartTime = i_EdgeStartTime;
    }

    
    /**
     * 获取：本服务实例的IP地址
     */
    public String getEdgeIP()
    {
        return edgeIP;
    }

    
    /**
     * 设置：本服务实例的IP地址
     * 
     * @param i_EdgeIP 本服务实例的IP地址
     */
    public void setEdgeIP(String i_EdgeIP)
    {
        this.edgeIP = i_EdgeIP;
    }

    
    /**
     * 获取：宿主机的IP地址。如将服务部署在K8s上，为服务所在Docker的宿主机IP
     */
    public String getHostIP()
    {
        return hostIP;
    }

    
    /**
     * 设置：宿主机的IP地址。如将服务部署在K8s上，为服务所在Docker的宿主机IP
     * 
     * @param i_HostIP 宿主机的IP地址。如将服务部署在K8s上，为服务所在Docker的宿主机IP
     */
    public void setHostIP(String i_HostIP)
    {
        this.hostIP = i_HostIP;
    }


    /**
     * 获取：本服务实例的版本号
     */
    public String getEdgeVersion()
    {
        return edgeVersion;
    }

    
    /**
     * 设置：本服务实例的版本号
     * 
     * @param i_EdgeVersion 本服务实例的版本号
     */
    public void setEdgeVersion(String i_EdgeVersion)
    {
        this.edgeVersion = i_EdgeVersion;
    }

    
    /**
     * 获取：获取最近多少秒内的数据
     */
    public Integer getSecond()
    {
        return second;
    }

    
    /**
     * 设置：获取最近多少秒内的数据
     * 
     * @param i_Second 获取最近多少秒内的数据
     */
    public void setSecond(Integer i_Second)
    {
        this.second = i_Second;
    }

    
    /**
     * 获取：认领任务数量
     */
    public Integer getClaimCount()
    {
        return claimCount;
    }

    
    /**
     * 设置：认领任务数量
     * 
     * @param i_ClaimCount 认领任务数量
     */
    public void setClaimCount(Integer i_ClaimCount)
    {
        this.claimCount = i_ClaimCount;
    }

    
    /**
     * 获取：任务执行正常的数量
     */
    public Integer getTaskOKCount()
    {
        return taskOKCount;
    }

    
    /**
     * 设置：任务执行正常的数量
     * 
     * @param i_TaskOKCount 任务执行正常的数量
     */
    public void setTaskOKCount(Integer i_TaskOKCount)
    {
        this.taskOKCount = i_TaskOKCount;
    }

    
    /**
     * 获取：无效时间（仅记录首次）
     */
    public Date getInvalidTime()
    {
        return invalidTime;
    }

    
    /**
     * 设置：无效时间（仅记录首次）
     * 
     * @param i_InvalidTime 无效时间（仅记录首次）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public void setInvalidTime(Date i_InvalidTime)
    {
        this.invalidTime = i_InvalidTime;
    }


    @Override
    public int compareTo(Heartbeat i_Other)
    {
        if ( i_Other == null )
        {
            return 1;
        }
        else if ( this == i_Other )
        {
            return 0;
        }
        else
        {
            return this.hashCode() - i_Other.hashCode();
        }
    }

    
    @Override
    public int hashCode()
    {
        if ( this.edgeIP == null )
        {
            return -1;
        }
        else
        {
            return this.edgeIP.hashCode();
        }
    }


    @Override
    public boolean equals(Object i_Other)
    {
        if ( i_Other == null )
        {
            return false;
        }
        else if ( this == i_Other )
        {
            return true;
        }
        else if ( i_Other instanceof Heartbeat )
        {
            return this.hashCode() == ((Heartbeat)i_Other).hashCode();
        }
        else
        {
            return false;
        }
    }

}
