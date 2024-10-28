package org.hy.microservice.common;

import org.hy.common.Date;
import org.hy.common.xml.SerializableDef;

import com.fasterxml.jackson.annotation.JsonFormat;





/**
 * 与页面交互的基础类（2024新版）
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-03
 * @version     v1.0
 */
public class BaseView<Domain extends BaseDomain<? extends BaseData>> extends SerializableDef
{

    private static final long serialVersionUID = -1012316681966628518L;
    
    /** 服务启动时间 */
    public final static Date $StartupTime = BaseData.$StartupTime;
    
    
    
    /** 领域数据 */
    protected Domain domain;
    
    
    
    public BaseView(Domain i_Domain)
    {
        this.domain = i_Domain;
    }
    
    
    
    /**
     * 获取：领域数据
     */
    public Domain gatDomain()
    {
        return domain;
    }

    
    /**
     * 设置：领域数据
     * 
     * @param i_Domain 类型的数据
     */
    public void satDomain(Domain i_Domain)
    {
        this.domain = i_Domain;
    }
    
    
    /**
     * 获取：应用appKey
     */
    public String getAppKey()
    {
        return this.domain.getAppKey();
    }

    
    /**
     * 设置：应用appKey
     * 
     * @param appKey
     */
    public void setAppKey(String appKey)
    {
        this.domain.setAppKey(appKey);
    }

    
    /**
     * 获取：票据号
     */
    public String getToken()
    {
        return this.domain.getToken();
    }

    
    /**
     * 设置：票据号
     * 
     * @param token
     */
    public void setToken(String token)
    {
        this.domain.setToken(token);
    }


    /**
     * 获取：设备号
     */
    public String getDeviceNo()
    {
        return this.domain.getDeviceNo();
    }

    
    /**
     * 获取：设备类型
     */
    public String getDeviceType()
    {
        return this.domain.getDeviceType();
    }

    
    /**
     * 获取：业务类型（值内容由业务决定）
     */
    public String getServiceType()
    {
        return this.domain.getServiceType();
    }

    
    /**
     * 获取：用户编号
     */
    public String getUserID()
    {
        return this.domain.getUserID();
    }

    
    /**
     * 获取：用户名称
     */
    public String getUserName()
    {
        return this.domain.getUserName();
    }

    
    /**
     * 获取：用户头像
     */
    public String getUserIcon()
    {
        return this.domain.getUserIcon();
    }

    
    /**
     * 获取：是否显示。1显示；0不显示
     */
    public Integer getIsShow()
    {
        return this.domain.getIsShow();
    }

    
    /**
     * 获取：审核状态：0：待审核、1：已审核
     */
    public String getAuditState()
    {
        return this.domain.getAuditState();
    }

    
    /**
     * 获取：审核结果，0：不通过、1：通过
     */
    public String getAuditResult()
    {
        return this.domain.getAuditResult();
    }
    
    
    /**
     * 设置：设备号
     * 
     * @param deviceNo
     */
    public void setDeviceNo(String deviceNo)
    {
        this.domain.setDeviceNo(deviceNo);
    }

    
    /**
     * 设置：设备类型
     * 
     * @param deviceType
     */
    public void setDeviceType(String deviceType)
    {
        this.domain.setDeviceType(deviceType);
    }

    
    /**
     * 设置：业务类型（值内容由业务决定）
     * 
     * @param serviceType
     */
    public void setServiceType(String serviceType)
    {
        this.domain.setServiceType(serviceType);
    }

    
    /**
     * 设置：用户编号
     * 
     * @param userID
     */
    public void setUserID(String userID)
    {
        this.domain.setUserID(userID);
    }

    
    /**
     * 设置：用户名称
     * 
     * @param userName
     */
    public void setUserName(String userName)
    {
        this.domain.setUserName(userName);
    }

    
    /**
     * 设置：用户头像
     * 
     * @param userIcon
     */
    public void setUserIcon(String userIcon)
    {
        this.domain.setUserIcon(userIcon);
    }

    
    /**
     * 设置：是否显示。1显示；0不显示
     * 
     * @param isShow
     */
    public void setIsShow(Integer isShow)
    {
        this.domain.setIsShow(isShow);
    }

    
    /**
     * 设置：审核状态：0：待审核、1：已审核
     * 
     * @param auditState
     */
    public void setAuditState(String auditState)
    {
        this.domain.setAuditState(auditState);
    }

    
    /**
     * 设置：审核结果，0：不通过、1：通过
     * 
     * @param auditResult
     */
    public void setAuditResult(String auditResult)
    {
        this.domain.setAuditResult(auditResult);
    }

    
    /**
     * 获取：用户类型
     */
    public String getUserType()
    {
        return this.domain.getUserType();
    }

    
    /**
     * 设置：用户类型
     * 
     * @param userType
     */
    public void setUserType(String userType)
    {
        this.domain.setUserType(userType);
    }


    /**
     * 获取：开始索引
     */
    public Long getStartIndex()
    {
        if ( this.domain.getPageIndex() == null || this.domain.getPagePerCount() == null )
        {
            return null;
        }
        else
        {
            return this.domain.getPagePerCount() * (this.domain.getPageIndex() - 1);
        }
    }


    /**
     * 获取：每页显示数量
     */
    public Long getPagePerCount()
    {
        if ( this.domain.getPagePerCount() == null )
        {
            return null;
        }
        else if ( this.domain.getPagePerCount() > 1000L )
        {
            return 1000L;
        }
        else if ( this.domain.getPagePerCount() <= 0L )
        {
            return 10L;
        }
        else
        {
            return this.domain.getPagePerCount();
        }
    }

    
    /**
     * 设置：每页显示数量
     * 
     * @param pagePerCount
     */
    public void setPagePerCount(Long pagePerCount)
    {
        this.domain.setPagePerCount(pagePerCount);
    }

    
    /**
     * 获取：总行数
     */
    public Long getTotalCount()
    {
        return this.domain.getTotalCount();
    }


    /**
     * 设置：总行数
     * 
     * @param totalCount
     */
    public void setTotalCount(Long totalCount)
    {
        this.domain.setTotalCount(totalCount);
    }


    /**
     * 获取：删除标记。1删除；0未删除
     */
    public Integer getIsDel()
    {
        return this.domain.getIsDel();
    }

    
    /**
     * 设置：删除标记。1删除；0未删除
     * 
     * @param isDel
     */
    public void setIsDel(Integer isDel)
    {
        this.domain.setIsDel(isDel);
    }

    
    /**
     * 获取：页码。有效下标从1开始
     */
    public Long getPageIndex()
    {
        if ( this.domain.getPageIndex() == null )
        {
            return null;
        }
        else if ( this.domain.getPageIndex() <= 0 )
        {
            return 1L;
        }
        else
        {
            return this.domain.getPageIndex();
        }
    }

    
    /**
     * 设置：页码。有效下标从1开始
     * 
     * @param pageIndex
     */
    public void setPageIndex(Long pageIndex)
    {
        this.domain.setPageIndex(pageIndex);
    }

    
    /**
     * 获取：排列显示顺序。数据越大越在前显示
     */
    public Integer getOrderBy()
    {
        return this.domain.getOrderBy();
    }


    /**
     * 设置：排列显示顺序。数据越大越在前显示
     * 
     * @param orderBy
     */
    public void setOrderBy(Integer orderBy)
    {
        this.domain.setOrderBy(orderBy);
    }
    
    
    /**
     * 获取：当前用户是否收藏。1收藏；0不收藏
     */
    public Integer getIsFavorite()
    {
        return this.domain.getIsFavorite();
    }

    
    /**
     * 设置：当前用户是否收藏。1收藏；0不收藏
     * 
     * @param i_IsFavorite 当前用户是否收藏。1收藏；0不收藏
     */
    public void setIsFavorite(Integer i_IsFavorite)
    {
        this.domain.setIsFavorite(i_IsFavorite);
    }

    
    /**
     * 获取：备注说明
     */
    public String getRemarks()
    {
        return this.domain.getRemarks();
    }

    
    /**
     * 设置：备注说明
     * 
     * @param remarks
     */
    public void setRemarks(String remarks)
    {
        this.domain.setRemarks(remarks);
    }


    /**
     * 获取：创建人编号
     */
    public String getCreateUserID()
    {
        return this.domain.getCreateUserID();
    }


    /**
     * 设置：创建人编号
     * 
     * @param createUserID
     */
    public void setCreateUserID(String createUserID)
    {
        this.domain.setCreateUserID(createUserID);
    }


    /**
     * 获取：修改者编号
     */
    public String getUpdateUserID()
    {
        return this.domain.getUpdateUserID();
    }


    /**
     * 设置：修改者编号
     * 
     * @param updateUserID
     */
    public void setUpdateUserID(String updateUserID)
    {
        this.domain.setUpdateUserID(updateUserID);
    }


    /**
     * 获取：有效标记。1有效；-1无效
     */
    public Integer getIsValid()
    {
        return this.domain.getIsValid();
    }


    /**
     * 设置：有效标记。1有效；-1无效
     * 
     * @param isValid
     */
    public void setIsValid(Integer isValid)
    {
        this.domain.setIsValid(isValid);
    }

    
    /**
     * 获取：注解说明
     */
    public String getComment()
    {
        return this.domain.getComment();
    }


    /**
     * 设置：注解说明
     * 
     * @param i_Comment 注解说明
     */
    public void setComment(String i_Comment)
    {
        this.domain.setComment(i_Comment);
    }
    
    
    /**
     * 获取：项目ID
     */
    public String getProjectID()
    {
        return this.domain.getProjectID();
    }

    
    /**
     * 设置：项目ID
     * 
     * @param i_ProjectID 项目ID
     */
    public void setProjectID(String i_ProjectID)
    {
        this.domain.setProjectID(i_ProjectID);
    }
    
    
    /**
     * 获取：过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public Date getExpireTime()
    {
        return this.domain.getExpireTime();
    }


    /**
     * 设置：过期时间
     * 
     * @param expireTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public void setExpireTime(Date expireTime)
    {
        this.domain.setExpireTime(expireTime);
    }
    
    
    /**
     * 获取：创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public Date getCreateTime()
    {
        return this.domain.getCreateTime();
    }

    
    /**
     * 设置：创建时间
     * 
     * @param createTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public void setCreateTime(Date createTime)
    {
        this.domain.setCreateTime(createTime);
    }
    
    
    /**
     * 获取：修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public Date getUpdateTime()
    {
        return this.domain.getUpdateTime();
    }

    
    /**
     * 设置：修改时间
     * 
     * @param updateTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public void setUpdateTime(Date updateTime)
    {
        this.domain.setUpdateTime(updateTime);
    }
    
    
    /**
     * 获取：审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public Date getAuditTime()
    {
        return this.domain.getAuditTime();
    }
    
    
    /**
     * 设置：审核时间
     * 
     * @param auditTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public void setAuditTime(Date auditTime)
    {
        this.domain.setAuditTime(auditTime);
    }

    
    /**
     * 获取：开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public Date getStartTime()
    {
        return this.domain.getStartTime();
    }

    
    /**
     * 设置：开始时间
     * 
     * @param i_StartTime 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public void setStartTime(Date i_StartTime)
    {
        this.domain.setStartTime(i_StartTime);
    }

    
    /**
     * 获取：结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public Date getEndTime()
    {
        return this.domain.getEndTime();
    }

    
    /**
     * 设置：结束时间
     * 
     * @param i_EndTime 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public void setEndTime(Date i_EndTime)
    {
        this.domain.setEndTime(i_EndTime);
    }
    
}
