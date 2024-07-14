package org.hy.microservice.common.state;

import org.hy.common.Help;





/**
 * 状态转换路径
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-06-16
 * @version     v1.0
 */
public class Transition
{

    /** 起始状态 */
    private final String fromState;

    /** 操作动作 */
    private final String action;

    /** 目标状态 */
    private final String toState;



    /**
     * 状态转换路径的构造器
     *
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_FromState  起始状态
     * @param i_Action     操作动作
     * @param i_ToState    目标状态
     */
    public Transition(String i_FromState ,String i_Action ,String i_ToState)
    {
        if ( Help.isNull(i_FromState) )
        {
            throw new NullPointerException("FromState is null");
        }
        
        if ( Help.isNull(i_Action) )
        {
            throw new NullPointerException("Action is null");
        }
        
        if ( Help.isNull(i_ToState) )
        {
            throw new NullPointerException("ToState is null");
        }
        
        if ( i_FromState.equals(i_ToState) )
        {
            throw new RuntimeException("FromState equals ToState ,it is error.");
        }
        
        this.fromState = i_FromState;
        this.action    = i_Action;
        this.toState   = i_ToState;
    }

    
    /**
     * 获取：起始状态
     */
    public String getFromState()
    {
        return fromState;
    }

    
    /**
     * 获取：操作动作
     */
    public String getAction()
    {
        return action;
    }

    
    /**
     * 获取：目标状态
     */
    public String getToState()
    {
        return toState;
    }

}
