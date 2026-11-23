package org.hy.microservice.common.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.hy.common.Date;
import org.hy.common.Return;
import org.hy.common.callflow.CallFlow;
import org.hy.common.callflow.execute.ExecuteResult;
import org.hy.common.callflow.returns.ReturnConfig;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;





/**
 * 测试单元：编排引擎024：占位符
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-06-18
 * @version     v1.0
 */
@Xjava
public class DemoLongJson
{
    
    private static boolean $isInit = false;
    
    
    
    public DemoLongJson() throws Exception
    {
        if ( !$isInit )
        {
            $isInit = true;
        }
    }
    
    
    
    public void test() throws InterruptedException
    {
        test_Inner();
    }
    
    
    
    private void test_Inner() throws InterruptedException
    {
        // 获取编排中的首个元素
        ReturnConfig        v_ReturnConfig = (ReturnConfig) XJava.getObject("CFlow_LongJson_001");
        Map<String ,Object> v_Context      = new HashMap<String ,Object>();
        
        // 执行前的静态检查（关键属性未变时，check方法内部为快速检查）
        Return<Object> v_CheckRet = CallFlow.getHelpCheck().check(v_ReturnConfig);
        if ( !v_CheckRet.get() )
        {
            System.out.println(v_CheckRet.getParamStr());  // 打印不合格的原因
            return;
        }
        
        ExecuteResult v_Result = CallFlow.execute(v_ReturnConfig ,v_Context);
        if ( v_Result.isSuccess() )
        {
            System.out.println("Success");
        }
        else
        {
            System.out.println("Error XID = " + v_Result.getExecuteXID());
            System.out.println("Error Msg = " + v_Result.getException().getMessage());
            if ( v_Result.getException() instanceof TimeoutException )
            {
                System.out.println("is TimeoutException");
            }
            v_Result.getException().printStackTrace();
        }
        
        // 打印执行路径
        ExecuteResult v_FirstResult = CallFlow.getFirstResult(v_Context);
        System.out.println(CallFlow.getHelpLog().logs(v_FirstResult));
        System.out.println("整体用时：" + Date.toTimeLenNano(v_Result.getEndTime() - v_Result.getBeginTime()) + "\n");
        
        // 导出
        System.out.println(CallFlow.getHelpExport().export(v_ReturnConfig));
    }
    
}
