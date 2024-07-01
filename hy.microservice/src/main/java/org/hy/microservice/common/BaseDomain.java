package org.hy.microservice.common;

import org.hy.common.Date;
import org.hy.common.xml.SerializableDef;





/**
 * 通用的领域模型
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-06-12
 * @version     v1.0
 * @param <Data>  数据对象的类型
 */
public class BaseDomain<Data extends BaseData> extends SerializableDef
{
 
    private static final long serialVersionUID = 5055692459964652206L;
    
    
    
    /** 数据对象 */
    protected Data data;
    
    
    
    public BaseDomain(Data i_Data)
    {
        this.data = i_Data;
    }
    
    
    
    /**
     * 获取：Data 类型的数据
     */
    public Data gatData()
    {
        return data;
    }

    
    /**
     * 设置：Data 类型的数据
     * 
     * @param i_Data 类型的数据
     */
    public void satData(Data i_Data)
    {
        this.data = i_Data;
    }
    
    
    /**
     * 获取：应用appKey
     */
    public String getAppKey()
    {
        return this.data.getAppKey();
    }

    
    /**
     * 设置：应用appKey
     * 
     * @param appKey
     */
    public void setAppKey(String appKey)
    {
        this.data.setAppKey(appKey);
    }

    
    /**
     * 获取：票据号
     */
    public String getToken()
    {
        return this.data.getToken();
    }

    
    /**
     * 设置：票据号
     * 
     * @param token
     */
    public void setToken(String token)
    {
        this.data.setToken(token);
    }


    /**
     * 获取：设备号
     */
    public String getDeviceNo()
    {
        return this.data.getDeviceNo();
    }

    
    /**
     * 获取：设备类型
     */
    public String getDeviceType()
    {
        return this.data.getDeviceType();
    }

    
    /**
     * 获取：业务类型（值内容由业务决定）
     */
    public String getServiceType()
    {
        return this.data.getServiceType();
    }

    
    /**
     * 获取：用户编号
     */
    public String getUserID()
    {
        return this.data.getUserID();
    }

    
    /**
     * 获取：用户名称
     */
    public String getUserName()
    {
        return this.data.getUserName();
    }

    
    /**
     * 获取：用户头像
     */
    public String getUserIcon()
    {
        return this.data.getUserIcon();
    }

    
    /**
     * 获取：创建时间
     */
    public Date getCreateTime()
    {
        return this.data.getCreateTime();
    }

    
    /**
     * 获取：是否显示。1显示；0不显示
     */
    public Integer getIsShow()
    {
        return this.data.getIsShow();
    }

    
    /**
     * 获取：审核状态：0：待审核、1：已审核
     */
    public String getAuditState()
    {
        return this.data.getAuditState();
    }

    
    /**
     * 获取：审核结果，0：不通过、1：通过
     */
    public String getAuditResult()
    {
        return this.data.getAuditResult();
    }

    
    /**
     * 获取：审核时间
     */
    public Date getAuditTime()
    {
        return this.data.getAuditTime();
    }

    
    /**
     * 设置：设备号
     * 
     * @param deviceNo
     */
    public void setDeviceNo(String deviceNo)
    {
        this.data.setDeviceNo(deviceNo);
    }

    
    /**
     * 设置：设备类型
     * 
     * @param deviceType
     */
    public void setDeviceType(String deviceType)
    {
        this.data.setDeviceType(deviceType);
    }

    
    /**
     * 设置：业务类型（值内容由业务决定）
     * 
     * @param serviceType
     */
    public void setServiceType(String serviceType)
    {
        this.data.setServiceType(serviceType);
    }

    
    /**
     * 设置：用户编号
     * 
     * @param userID
     */
    public void setUserID(String userID)
    {
        this.data.setUserID(userID);
    }

    
    /**
     * 设置：用户名称
     * 
     * @param userName
     */
    public void setUserName(String userName)
    {
        this.data.setUserName(userName);
    }

    
    /**
     * 设置：用户头像
     * 
     * @param userIcon
     */
    public void setUserIcon(String userIcon)
    {
        this.data.setUserIcon(userIcon);
    }

    
    /**
     * 设置：创建时间
     * 
     * @param createTime
     */
    public void setCreateTime(Date createTime)
    {
        this.data.setCreateTime(createTime);
    }

    
    /**
     * 设置：是否显示。1显示；0不显示
     * 
     * @param isShow
     */
    public void setIsShow(Integer isShow)
    {
        this.data.setIsShow(isShow);
    }

    
    /**
     * 设置：审核状态：0：待审核、1：已审核
     * 
     * @param auditState
     */
    public void setAuditState(String auditState)
    {
        this.data.setAuditState(auditState);
    }

    
    /**
     * 设置：审核结果，0：不通过、1：通过
     * 
     * @param auditResult
     */
    public void setAuditResult(String auditResult)
    {
        this.data.setAuditResult(auditResult);
    }

    
    /**
     * 设置：审核时间
     * 
     * @param auditTime
     */
    public void setAuditTime(Date auditTime)
    {
        this.data.setAuditTime(auditTime);
    }

    
    /**
     * 获取：用户类型
     */
    public String getUserType()
    {
        return this.data.getUserType();
    }

    
    /**
     * 设置：用户类型
     * 
     * @param userType
     */
    public void setUserType(String userType)
    {
        this.data.setUserType(userType);
    }


    /**
     * 获取：开始索引
     */
    public Long getStartIndex()
    {
        if ( this.data.getPageIndex() == null || this.data.getPagePerCount() == null )
        {
            return null;
        }
        else
        {
            return this.data.getPagePerCount() * (this.data.getPageIndex() - 1);
        }
    }


    /**
     * 获取：每页显示数量
     */
    public Long getPagePerCount()
    {
        if ( this.data.getPagePerCount() == null )
        {
            return null;
        }
        else if ( this.data.getPagePerCount() > 1000L )
        {
            return 1000L;
        }
        else if ( this.data.getPagePerCount() <= 0L )
        {
            return 10L;
        }
        else
        {
            return this.data.getPagePerCount();
        }
    }

    
    /**
     * 设置：每页显示数量
     * 
     * @param pagePerCount
     */
    public void setPagePerCount(Long pagePerCount)
    {
        this.data.setPagePerCount(pagePerCount);
    }

    
    /**
     * 获取：总行数
     */
    public Long getTotalCount()
    {
        return this.data.getTotalCount();
    }


    /**
     * 设置：总行数
     * 
     * @param totalCount
     */
    public void setTotalCount(Long totalCount)
    {
        this.data.setTotalCount(totalCount);
    }


    /**
     * 获取：修改时间
     */
    public Date getUpdateTime()
    {
        return this.data.getUpdateTime();
    }

    
    /**
     * 设置：修改时间
     * 
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime)
    {
        this.data.setUpdateTime(updateTime);
    }

    
    /**
     * 获取：删除标记。1删除；0未删除
     */
    public Integer getIsDel()
    {
        return this.data.getIsDel();
    }

    
    /**
     * 设置：删除标记。1删除；0未删除
     * 
     * @param isDel
     */
    public void setIsDel(Integer isDel)
    {
        this.data.setIsDel(isDel);
    }

    
    /**
     * 获取：页码。有效下标从1开始
     */
    public Long getPageIndex()
    {
        if ( this.data.getPageIndex() == null )
        {
            return null;
        }
        else if ( this.data.getPageIndex() <= 0 )
        {
            return 1L;
        }
        else
        {
            return this.data.getPageIndex();
        }
    }

    
    /**
     * 设置：页码。有效下标从1开始
     * 
     * @param pageIndex
     */
    public void setPageIndex(Long pageIndex)
    {
        this.data.setPageIndex(pageIndex);
    }

    
    /**
     * 获取：排列显示顺序。数据越大越在前显示
     */
    public Integer getOrderBy()
    {
        return this.data.getOrderBy();
    }


    /**
     * 设置：排列显示顺序。数据越大越在前显示
     * 
     * @param orderBy
     */
    public void setOrderBy(Integer orderBy)
    {
        this.data.setOrderBy(orderBy);
    }

    
    /**
     * 获取：过期时间
     */
    public Date getExpireTime()
    {
        return this.data.getExpireTime();
    }


    /**
     * 设置：过期时间
     * 
     * @param expireTime
     */
    public void setExpireTime(Date expireTime)
    {
        this.data.setExpireTime(expireTime);
    }
    
    
    /**
     * 获取：备注说明
     */
    public String getRemarks()
    {
        return this.data.getRemarks();
    }

    
    /**
     * 设置：备注说明
     * 
     * @param remarks
     */
    public void setRemarks(String remarks)
    {
        this.data.setRemarks(remarks);
    }


    /**
     * 获取：创建人编号
     */
    public String getCreateUserID()
    {
        return this.data.getCreateUserID();
    }


    /**
     * 设置：创建人编号
     * 
     * @param createUserID
     */
    public void setCreateUserID(String createUserID)
    {
        this.data.setCreateUserID(createUserID);
    }


    /**
     * 获取：修改者编号
     */
    public String getUpdateUserID()
    {
        return this.data.getUpdateUserID();
    }


    /**
     * 设置：修改者编号
     * 
     * @param updateUserID
     */
    public void setUpdateUserID(String updateUserID)
    {
        this.data.setUpdateUserID(updateUserID);
    }


    /**
     * 获取：有效标记。1有效；-1无效
     */
    public Integer getIsValid()
    {
        return this.data.getIsValid();
    }


    /**
     * 设置：有效标记。1有效；-1无效
     * 
     * @param isValid
     */
    public void setIsValid(Integer isValid)
    {
        this.data.setIsValid(isValid);
    }

    
    /**
     * 获取：注解说明
     */
    public String getComment()
    {
        return this.data.getComment();
    }


    /**
     * 设置：注解说明
     * 
     * @param i_Comment 注解说明
     */
    public void setComment(String i_Comment)
    {
        this.data.setComment(i_Comment);
    }

    
    /**
     * 获取：开始时间
     */
    public Date getStartTime()
    {
        return this.data.getStartTime();
    }

    
    /**
     * 设置：开始时间
     * 
     * @param i_StartTime 开始时间
     */
    public void setStartTime(Date i_StartTime)
    {
        this.data.setStartTime(i_StartTime);
    }

    
    /**
     * 获取：结束时间
     */
    public Date getEndTime()
    {
        return this.data.getEndTime();
    }

    
    /**
     * 设置：结束时间
     * 
     * @param i_EndTime 结束时间
     */
    public void setEndTime(Date i_EndTime)
    {
        this.data.setEndTime(i_EndTime);
    }

    
    /**
     * 获取：项目ID
     */
    public String getProjectID()
    {
        return this.data.getProjectID();
    }

    
    /**
     * 设置：项目ID
     * 
     * @param i_ProjectID 项目ID
     */
    public void setProjectID(String i_ProjectID)
    {
        this.data.setProjectID(i_ProjectID);
    }
    
}
