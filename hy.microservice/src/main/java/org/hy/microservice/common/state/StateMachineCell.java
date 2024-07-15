package org.hy.microservice.common.state;

import org.hy.microservice.common.BaseEnum;





/**
 * 状态机模型的组成单元
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-15
 * @version     v1.0
 */
public class StateMachineCell
{
    
    /** 状态机模型 */
    private StateMachine                 stateMachine;
    
    /** 状态枚举值的元类型 */
    private Class<? extends BaseEnum<?>> stateClass;
    
    /** 操作动作枚举值的元类型 */
    private Class<? extends BaseEnum<?>> actionClass;
    
    
    
    /**
     * 状态机模型的组成单元
     */
    public StateMachineCell()
    {
        
    }
    
    
    
    /**
     * 状态机模型的组成单元
     *
     * @author      ZhengWei(HY)
     * @createDate  2024-07-15
     * @version     v1.0
     *
     * @param i_StateMachine  状态机模型
     * @param i_StateClass    状态枚举值的元类型
     * @param i_ActionClass   操作动作枚举值的元类型
     */
    public StateMachineCell(StateMachine i_StateMachine ,Class<? extends BaseEnum<?>> i_StateClass ,Class<? extends BaseEnum<?>> i_ActionClass)
    {
        this.stateMachine = i_StateMachine;
        this.stateClass   = i_StateClass;
        this.actionClass  = i_ActionClass;
    }
    
    
    
    /**
     * 获取：状态机模型
     */
    public StateMachine getStateMachine()
    {
        return stateMachine;
    }

    
    
    /**
     * 设置：状态机模型
     * 
     * @param i_StateMachine 状态机模型
     */
    public void setStateMachine(StateMachine i_StateMachine)
    {
        this.stateMachine = i_StateMachine;
    }


    
    /**
     * 获取：状态枚举值的元类型
     */
    public Class<? extends BaseEnum<?>> getStateClass()
    {
        return stateClass;
    }


    
    /**
     * 设置：状态枚举值的元类型
     * 
     * @param i_StateClass 状态枚举值的元类型
     */
    public void setStateClass(Class<? extends BaseEnum<?>> i_StateClass)
    {
        this.stateClass = i_StateClass;
    }


    
    /**
     * 获取：操作动作枚举值的元类型
     */
    public Class<? extends BaseEnum<?>> getActionClass()
    {
        return actionClass;
    }


    
    /**
     * 设置：操作动作枚举值的元类型
     * 
     * @param i_ActionClass 操作动作枚举值的元类型
     */
    public void setActionClass(Class<? extends BaseEnum<?>> i_ActionClass)
    {
        this.actionClass = i_ActionClass;
    }
    
}
