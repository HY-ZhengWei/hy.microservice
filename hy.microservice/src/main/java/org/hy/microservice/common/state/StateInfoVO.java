package org.hy.microservice.common.state;





/**
 * 交互模型：状态信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-18
 * @version     v1.0
 */
public class StateInfoVO extends StateInfo
{
    
    /** 状态备注说明 */
    private final String comment;
    
    
    
    public StateInfoVO(StateInfo i_StateInfo ,String i_Comment)
    {
        super(i_StateInfo.getLevel() ,i_StateInfo.getBranch());
        this.comment = i_Comment;
    }
    
    
    /**
     * 获取：状态备注说明
     */
    public String getComment()
    {
        return comment;
    }
    
}
