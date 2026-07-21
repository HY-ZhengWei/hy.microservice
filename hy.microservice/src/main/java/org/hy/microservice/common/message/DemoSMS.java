package org.hy.microservice.common.message;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.callflow.CallFlow;
import org.hy.common.callflow.execute.ExecuteResult;
import org.hy.common.xml.annotation.Xjava;





/**
 * 发短信
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-07-11
 * @version     v1.0
 */
@Xjava
public class DemoSMS
{
    
    public void send()
    {
        Map<String ,Object> v_Context = new HashMap<String ,Object>();
        v_Context.put("CellPhoneNo" ,"19909218550");
        v_Context.put("Message"     ,"基座1.22.6版本：柔性编排.第33号元素发送的短信，能收到吗");
        v_Context.put("UserID"      ,"ZhengWei(HY)");
        
        ExecuteResult v_Result = CallFlow.execute("XSMS" ,v_Context);
        
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
