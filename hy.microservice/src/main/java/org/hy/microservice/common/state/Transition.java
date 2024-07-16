package org.hy.microservice.common.state;

import org.hy.common.Help;
import org.hy.microservice.common.BaseEnum;





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
     * 状态转换路径的构造器
     *
     * @author      ZhengWei(HY)
     * @createDate  2024-07-16
     * @version     v1.0
     *
     * @param i_FromState  起始状态
     * @param i_Action     操作动作
     * @param i_ToState    目标状态
     */
    public Transition(BaseEnum<String> i_FromState ,BaseEnum<String> i_Action ,BaseEnum<String> i_ToState)
    {
        if ( i_FromState == null )
        {
            throw new NullPointerException("FromState is null");
        }
        
        if ( i_Action == null )
        {
            throw new NullPointerException("Action is null");
        }
        
        if ( i_ToState == null )
        {
            throw new NullPointerException("ToState is null");
        }
        
        if ( i_FromState == i_ToState || i_FromState.getValue().equals(i_ToState.getValue()) )
        {
            throw new RuntimeException("FromState equals ToState ,it is error.");
        }
        
        this.fromState = i_FromState.getValue();
        this.action    = i_Action   .getValue();
        this.toState   = i_ToState  .getValue();
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
