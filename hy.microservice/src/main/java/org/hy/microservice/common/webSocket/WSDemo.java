package org.hy.microservice.common.webSocket;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Return;
import org.hy.common.callflow.CallFlow;
import org.hy.common.callflow.event.WSPullConfig;
import org.hy.common.callflow.execute.ExecuteResult;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;





@Xjava
public class WSDemo
{
    
    public void showObject(WSData i_MessageData)
    {
        System.out.println(i_MessageData);
    }
    
    
    
    public void showMap(Map<String ,Object> i_MessageData)
    {
        System.out.println(i_MessageData);
    }
    
    
    
    public void start()
    {
        WSPullConfig        v_WSPull  = (WSPullConfig) XJava.getObject("XWSPull");
        Map<String ,Object> v_Context = new HashMap<String ,Object>();
        
        // 执行前的静态检查（关键属性未变时，check方法内部为快速检查）
        Return<Object> v_CheckRet = CallFlow.getHelpCheck().check(v_WSPull);
        if ( !v_CheckRet.get() )
        {
            System.out.println(v_CheckRet.getParamStr());  // 打印不合格的原因
            return;
        }
        
        ExecuteResult v_Result = CallFlow.execute(v_WSPull ,v_Context);
        if ( v_Result.isSuccess() )
        {
            System.out.println("Success");
        }
        else
        {
            System.out.println("Error XID = " + v_Result.getExecuteXID());
            v_Result.getException().printStackTrace();
        }
        
        // 打印执行路径
        ExecuteResult v_FirstResult = CallFlow.getFirstResult(v_Context);
        System.out.println(CallFlow.getHelpLog().logs(v_FirstResult));
        
        System.out.println();
        
        // 第二种方法获取首个执行结果
        v_FirstResult = CallFlow.getHelpExecute().getFirstResult(v_Result);
        System.out.println(CallFlow.getHelpLog().logs(v_FirstResult));
        System.out.println("整体用时：" + Date.toTimeLenNano(v_Result.getEndTime() - v_Result.getBeginTime()) + "\n");
    }
    
}
