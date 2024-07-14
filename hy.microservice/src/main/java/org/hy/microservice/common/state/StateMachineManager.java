package org.hy.microservice.common.state;

import java.util.HashMap;
import java.util.Map;





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

    private static final Map<String ,StateMachine> $StateMachineMap = new HashMap<String ,StateMachine>();
    
    
    
    /**
     * 注册状态机
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_ServiceType   业务类型
     * @param i_StateMachine  状态机
     * @return                注册是否成功
     */
    public static synchronized boolean register(String i_ServiceType ,StateMachine i_StateMachine)
    {
        if ( $StateMachineMap.containsKey(i_ServiceType) )
        {
            return false;
        }
        else
        {
            $StateMachineMap.put(i_ServiceType ,i_StateMachine);
            return true;
        }
    }
    
    
    
    /**
     * 获取状态机
     * 
     * @param i_ServiceType   业务类型
     * @return
     */
    public static StateMachine getStateMachine(String i_ServiceType)
    {
        return $StateMachineMap.get(i_ServiceType);
    }
    
    
    
    private StateMachineManager()
    {
        // 不允许被实例化
        // Nothing.
    }
    
}
