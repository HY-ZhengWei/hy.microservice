package org.hy.microservice.common.state;

import java.util.HashSet;
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
    
    /** 开始状态 */
    private final String []                             startStates;
    
    /** 最终状态 */
    private final String []                             endStates;
    
    
    
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
        
        this.transitionList = i_Transitions;
        
        Set<String> v_ToStates = new HashSet<String>();
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
            v_ToStates.add(v_Item.getToState());
        }
        
        Set<String> v_StartStates = this.findStartStates(v_ToStates);
        Set<String> v_EndStates   = this.findEndStates  (v_ToStates);
        
        this.checkStartEnd(v_StartStates ,v_EndStates);
        
        this.startStates = v_StartStates.toArray(new String []{});
        this.endStates   = v_EndStates  .toArray(new String []{});
        
        v_ToStates   .clear();
        v_StartStates.clear();
        v_EndStates  .clear();
        v_ToStates    = null;
        v_StartStates = null;
        v_EndStates   = null;
    }
    
    
    
    /**
     * 检查开始状态与最终状态是否正确
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-16
     * @version     v1.0
     *
     * @param i_StartStates  开始状态的集合
     * @param i_EndStates    结束状态的信息
     */
    private void checkStartEnd(Set<String> i_StartStates ,Set<String> i_EndStates)
    {
        if ( !Help.isNull(i_StartStates) )
        {
            for (String i_StartState : i_StartStates)
            {
                if ( i_EndStates.contains(i_StartState) )
                {
                    // 开始状态不能又是最终状态。或是程序判定开始状态与最终状态有误
                    throw new RuntimeException("StartState[" + i_StartState + "] is EndState again.");
                }
            }
        }
        
        if ( !Help.isNull(i_EndStates) )
        {
            for (String i_EndState : i_EndStates)
            {
                if ( i_StartStates.contains(i_EndState) )
                {
                    // 最终状态不能又是开始状态。或是程序判定开始状态与最终状态有误
                    throw new RuntimeException("EndState[" + i_EndState + "] is StartState again.");
                }
            }
        }
    }
    
    
    
    /**
     * 查找状态机中的开始状态
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-16
     * @version     v1.0
     *
     * @param i_ToStates  流转状态（包含最终状态）
     * @return
     */
    private Set<String> findStartStates(Set<String> i_ToStates)
    {
        Set<String> v_StartStates = new HashSet<String>();
        
        if ( !Help.isNull(i_ToStates) )
        {
            for (Transition v_Item : this.transitionList)
            {
                if ( i_ToStates.contains(v_Item.getFromState()) )
                {
                    continue;
                }
                
                v_StartStates.add(v_Item.getFromState());
            }
        }
        
        return v_StartStates;
    }
    
    
    
    /**
     * 查找状态机中的最终状态
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-16
     * @version     v1.0
     *
     * @param i_ToStates  流转状态（包含最终状态）
     * @return
     */
    private Set<String> findEndStates(Set<String> i_ToStates)
    {
        Set<String> v_EndStates = new HashSet<String>();
        
        if ( !Help.isNull(i_ToStates) )
        {
            for (String v_ToState : i_ToStates)
            {
                if ( this.transitions.containsKey(v_ToState) )
                {
                    continue;
                }
                
                v_EndStates.add(v_ToState);
            }
        }
        
        return v_EndStates;
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


    
    /**
     * 获取：开始状态
     */
    public String [] getStartStates()
    {
        return startStates;
    }


    
    /**
     * 获取：最终状态
     */
    public String [] getEndStates()
    {
        return endStates;
    }
    
}
