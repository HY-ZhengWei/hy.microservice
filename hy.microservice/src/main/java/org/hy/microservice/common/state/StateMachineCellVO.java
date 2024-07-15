package org.hy.microservice.common.state;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.MethodReflect;
import org.hy.common.StaticReflect;
import org.hy.microservice.common.BaseEnum;
import org.hy.microservice.common.BaseViewMode;





/**
 * 状态机模型的组成单元的交互层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-15
 * @version     v1.0
 */
public class StateMachineCellVO extends BaseViewMode
{

    private static final long serialVersionUID = -1048633586821061457L;
    
    
    
    /** 状态 */
    private Map<String ,String> states;
    
    /** 操作动作 */
    private Map<String ,String> actions;
    
    /** 状态转换路径 */
    private Transition []       transitions;
    
    
    
    public StateMachineCellVO()
    {
        
    }
    
    
    
    public StateMachineCellVO(StateMachineCell i_StateMachineCell)
    {
        this.states      = new HashMap<String ,String>();
        this.actions     = new HashMap<String ,String>();
        this.transitions = i_StateMachineCell.getStateMachine().getTransitions();
        
        this.putToMap(i_StateMachineCell.getStateClass()  ,this.states);
        this.putToMap(i_StateMachineCell.getActionClass() ,this.actions);
    }
    
    
    
    /**
     * 将枚举类的每个元素值填充到Map集合中
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-15
     * @version     v1.0
     *
     * @param i_EnumClass  枚举类的元类型
     * @param io_Map       Map.key为：  枚举类的getValue()  方法的返回值
     *                     Map.value为：枚举类的getComment()方法的返回值
     */
    @SuppressWarnings("unchecked")
    private void putToMap(Class<? extends BaseEnum<?>> i_EnumClass ,Map<String ,String> io_Map)
    {
        Enum<?> [] v_StateEnums = StaticReflect.getEnums((Class<? extends Enum<?>>) i_EnumClass);
        if ( !Help.isNull(v_StateEnums) )
        {
            for (Enum<?> v_Item : v_StateEnums)
            {
                try
                {
                    Method v_ValueMethod   = MethodReflect.getMethods(i_EnumClass ,"getValue"   ,0).get(0);
                    Method v_CommentMethod = MethodReflect.getMethods(i_EnumClass ,"getComment" ,0).get(0);
                    Object v_Value         = v_ValueMethod  .invoke(v_Item);
                    Object v_Comment       = v_CommentMethod.invoke(v_Item);
                    
                    io_Map.put(v_Value.toString() ,v_Comment.toString());
                }
                catch (Exception exce)
                {
                    exce.printStackTrace();
                }
            }
        }
    }

    
    
    /**
     * 获取：状态
     */
    public Map<String ,String> getStates()
    {
        return states;
    }

    
    /**
     * 设置：状态
     * 
     * @param i_States 状态
     */
    public void setStates(Map<String ,String> i_States)
    {
        this.states = i_States;
    }

    
    /**
     * 获取：操作动作
     */
    public Map<String ,String> getActions()
    {
        return actions;
    }

    
    /**
     * 设置：操作动作
     * 
     * @param i_Actions 操作动作
     */
    public void setActions(Map<String ,String> i_Actions)
    {
        this.actions = i_Actions;
    }

    
    /**
     * 获取：状态转换路径
     */
    public Transition [] getTransitions()
    {
        return transitions;
    }

    
    /**
     * 设置：状态转换路径
     * 
     * @param i_Transitions 状态转换路径
     */
    public void setTransitions(Transition [] i_Transitions)
    {
        this.transitions = i_Transitions;
    }
    
}
