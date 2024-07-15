package org.hy.microservice.common.state;

import java.util.HashMap;
import java.util.Map;

import org.hy.microservice.common.BaseEnum;





/**
 * 状态机管理器。
 *     即：本项目中所有的状态机
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-06-16
 * @version     v1.0
 */
public class StateMachineManager
{

    private static final Map<String ,StateMachineCell> $StateMachineMap = new HashMap<String ,StateMachineCell>();
    
    
    
    /**
     * 注册状态机
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_ServiceType   业务类型
     * @param i_StateMachine  状态机模型
     * @param i_StateClass    状态枚举的元类型
     * @param i_ActionClass   操作动作枚的举元类型
     * @return                注册是否成功
     */
    public static synchronized boolean register(String i_ServiceType ,StateMachine i_StateMachine ,Class<? extends BaseEnum<?>> i_StateClass ,Class<? extends BaseEnum<?>> i_ActionClass)
    {
        if ( $StateMachineMap.containsKey(i_ServiceType) )
        {
            return false;
        }
        else
        {
            $StateMachineMap.put(i_ServiceType ,new StateMachineCell(i_StateMachine ,i_StateClass ,i_ActionClass));
            return true;
        }
    }
    
    
    
    /**
     * 获取状态机模型
     * 
     * @param i_ServiceType   业务类型
     * @return
     */
    public static StateMachine getStateMachine(String i_ServiceType)
    {
        return $StateMachineMap.get(i_ServiceType).getStateMachine();
    }
    
    
    
    /**
     * 获取状态枚举值的举元类型
     * 
     * @param i_ServiceType   业务类型
     * @return
     */
    public static Class<? extends BaseEnum<?>> getStateClass(String i_ServiceType)
    {
        return $StateMachineMap.get(i_ServiceType).getStateClass();
    }
    
    
    
    /**
     * 获取状态操作动作枚举值的举元类型
     * 
     * @param i_ServiceType   业务类型
     * @return
     */
    public static Class<? extends BaseEnum<?>> getActionClass(String i_ServiceType)
    {
        return $StateMachineMap.get(i_ServiceType).getActionClass();
    }
    
    
    
    /**
     * 获取状态机模型的组成单元
     * 
     * @param i_ServiceType   业务类型
     * @return
     */
    public static StateMachineCell getStateMachineCell(String i_ServiceType)
    {
        return $StateMachineMap.get(i_ServiceType);
    }
    
    
    
    private StateMachineManager()
    {
        // 不允许被实例化
        // Nothing.
    }
    
}
