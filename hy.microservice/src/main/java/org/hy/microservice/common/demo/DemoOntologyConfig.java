package org.hy.microservice.common.demo;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.callflow.CallFlow;
import org.hy.common.callflow.execute.ExecuteResult;
import org.hy.common.xml.annotation.Xjava;





/**
 * 测试单元：编排引擎
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-24
 * @version     v1.0
 */
@Xjava
public class DemoOntologyConfig
{
    
    public void uploading()
    {
        Map<String ,Object> v_Formula = new HashMap<String ,Object>();
        v_Formula.put("ClosingPressure" ,100);
        v_Formula.put("ClosingSpeed"    ,200);
        
        Map<String ,Object> v_Context = new HashMap<String ,Object>();
        v_Context.put("Formula" ,v_Formula);
        v_Context.put("UserID"  ,"ZhengWei(HY)");
        
        ExecuteResult v_Result = CallFlow.execute("XContology" ,v_Context);
        
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
    }
    
}
