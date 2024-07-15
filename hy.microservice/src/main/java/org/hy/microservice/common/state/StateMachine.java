package org.hy.microservice.common.state;

import java.util.Map;
import java.util.Set;

import org.hy.common.Help;
import org.hy.common.TablePartitionRID;





/**
 * 状态机模型
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-06-16
 * @version     v1.0
 */
public class StateMachine
{
    
    /** 分割符 */
    private static final String $Split = "->";
    
    
    
    /**
     * 状态推进的操作，及状态转换模型。
     *    分区为：起始状态
     *    行主键：操作动作
     *    行值为：目标状态（包装在Transition类中）
     */
    private final TablePartitionRID<String ,Transition> transitions = new TablePartitionRID<String ,Transition>();
    
    /** 与上面一样，仅结构不同，主要用于界面展示 */
    private final Transition []                         transitionList;
    
    
    
    /**
     * 状态机模型的构造器
     *
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_Transitions  状态转换路径集合
     */
    public StateMachine(Transition [] i_Transitions)
    {
        if ( Help.isNull(i_Transitions) )
        {
            throw new NullPointerException("Transitions is null");
        }
        
        for (Transition v_Item : i_Transitions)
        {
            if ( this.getTargetTransition(v_Item.getFromState() ,v_Item.getAction()) != null )
            {
                // 起始状态的操作动作已存在，不能重复配置
                throw new RuntimeException(v_Item.getFromState() + $Split + v_Item.getAction() + " is exists.");
            }
            
            if ( this.checkStateFromTo(v_Item.getFromState() ,v_Item.getToState()) )
            {
                // 起始状态到目标状态的操作动作已存在，不能重复配置
                throw new RuntimeException(v_Item.getFromState() + $Split + getAction(v_Item.getFromState() ,v_Item.getToState()) + $Split + v_Item.getToState() + " is exists.");
            }
            
            this.transitions.putRow(v_Item.getFromState() ,v_Item.getAction() ,v_Item);
        }
        
        this.transitionList = i_Transitions;
    }
    
    
    
    /**
     * 起始状态允许的操作动作
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_FromState  起始状态
     * @return             返回允许的操作动作
     */
    public Set<String> allowActions(String i_FromState)
    {
        Map<String ,Transition> v_Actions = this.transitions.get(i_FromState);
        if ( Help.isNull(v_Actions) )
        {
            return null;
        }
        else
        {
            return v_Actions.keySet();
        }
    }
    
    
    
    /**
     * 获取目标状态
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_FromState  起始状态
     * @param i_Action     操作动作
     * @return             返回状态转换路径
     */
    public Transition getTargetTransition(String i_FromState ,String i_Action)
    {
        return this.transitions.getRow(i_FromState ,i_Action);
    }
    
    
    
    /**
     * 获取目标状态
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_FromState  起始状态
     * @param i_Action     操作动作
     * @return             返回目标状态
     */
    public String getTargetState(String i_FromState ,String i_Action)
    {
        Transition v_Transition = this.getTargetTransition(i_FromState ,i_Action);
        if ( v_Transition == null )
        {
            return null;
        }
        else
        {
            return v_Transition.getToState();
        }
    }
    
    
    
    /**
     * 验证签转路径是否有效
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_FromState  起始状态
     * @param i_Action     操作动作
     * @param i_ToState    目标状态
     * @return
     */
    public boolean checkTransition(String i_FromState ,String i_Action ,String i_ToState)
    {
        Transition v_Transition = this.getTargetTransition(i_FromState ,i_Action);
        if ( v_Transition == null )
        {
            return false;
        }
        else if ( v_Transition.getToState().equals(i_ToState) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    
    /**
     * 验证操作在状态下是否合法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_FromState  起始状态
     * @param i_Action     操作动作
     * @return
     */
    public boolean checkAction(String i_FromState ,String i_Action)
    {
        Transition v_Transition = this.getTargetTransition(i_FromState ,i_Action);
        return v_Transition != null;
    }
    
    
    
    /**
     * 获取起始状态到目标状态间的有且仅有的唯一的操作动作
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_FromState
     * @param i_ToState
     * @return
     */
    public String getAction(String i_FromState ,String i_ToState)
    {
        Map<String ,Transition> v_Actions = this.transitions.get(i_FromState);
        if ( Help.isNull(v_Actions) )
        {
            return null;
        }
        
        String v_Action = null;
        for (Transition v_Item : v_Actions.values())
        {
            if ( !v_Item.getToState().equals(i_ToState) )
            {
                continue;
            }
            
            if ( v_Action != null )
            {
                return null;
            }
            else
            {
                v_Action = v_Item.getAction();
            }
        }
        return v_Action;
    }
    
    
    
    /**
     * 验证起始状态到目标状态间是否有且仅有一个操作动作
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-16
     * @version     v1.0
     *
     * @param i_FromState  起始状态
     * @param i_ToState    目标状态
     * @return
     */
    public boolean checkStateFromTo(String i_FromState ,String i_ToState)
    {
        return this.getAction(i_FromState ,i_ToState) != null;
    }
    
    
    
    /**
     * 获取状态转换路径
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-15
     * @version     v1.0
     *
     * @return
     */
    public Transition[] getTransitions()
    {
        return this.transitionList;
    }
    
}
