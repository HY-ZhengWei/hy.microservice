package org.hy.microservice.common.message;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.StringHelp;
import org.hy.common.callflow.CallFlow;
import org.hy.common.callflow.execute.ExecuteResult;
import org.hy.common.xml.annotation.Xjava;





/**
 * 发微信
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-07-19
 * @version     v1.0
 */
@Xjava
public class DemoWeiXin
{
    
    public void send(String i_OpenID)
    {
        Map<String ,Object> v_Context = new HashMap<String ,Object>();
        v_Context.put("OpenID"  ,i_OpenID);
        v_Context.put("GotoUrl" ,"https://www.openapis.cn");
        v_Context.put("Message" ,"""
                                {
                                    "character_string1": {
                                        "value": "1234567890"
                                    },
                                    "thing8": {
                                        "value": "基座"
                                    },
                                    "thing19": {
                                        "value": "微信服务号"
                                    },
                                    "thing7": {
                                        "value": "柔性编排.第35号元素"
                                    },
                                    "time22": {
                                        "value": ":Time"
                                    }
                                }
                                 """);
        v_Context.put("Message" ,StringHelp.replaceAll(v_Context.get("Message").toString() ,":Time" ,new Date().getFull()));
        v_Context.put("UserID"  ,"ZhengWei(HY)");
        
        ExecuteResult v_Result = CallFlow.execute("XWeiXin" ,v_Context);
        
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
