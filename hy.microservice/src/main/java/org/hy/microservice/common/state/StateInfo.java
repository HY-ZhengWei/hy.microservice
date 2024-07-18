package org.hy.microservice.common.state;





/**
 * 状态信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-18
 * @version     v1.0
 */
public class StateInfo
{
    
    public static final Integer $StartLevel = 1;
    
    public static final Integer $EndLevel   = 999;
    
    
    
    /** 状态层级（开始状态为1，最终状态为999） */
    private final Integer level;
    
    /** 状态其下的分支数量 */
    private final Integer branch;
    
    
    
    public StateInfo(Integer i_Level ,Integer i_Branch)
    {
        this.level  = i_Level;
        this.branch = i_Branch;
    }

    
    /**
     * 获取：状态层级（开始状态为1，最终状态为999）
     */
    public Integer getLevel()
    {
        return level;
    }

    
    /**
     * 获取：状态其下的分支数量
     */
    public Integer getBranch()
    {
        return branch;
    }

}
