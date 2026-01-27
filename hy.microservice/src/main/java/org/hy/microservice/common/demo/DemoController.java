package org.hy.microservice.common.demo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.rocketmq.client.producer.SendResult;
import org.hy.common.xml.XSQL;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.operationLog.IOperationLogDAO;
import org.hy.microservice.common.operationLog.IOperationLogService;
import org.hy.microservice.common.rocketMQ.RocketMQProducer;
import org.hy.microservice.common.user.UserSSO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;





/**
 * 样例Demo的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-12-09
 * @version     v1.0
 */
@Controller
@RequestMapping(value="demo" ,name="演示")   // 总体的访问父路径为：http://ip:port/WebName/demo
public class DemoController extends BaseController
{
    
    /** 用Srping 注解XJava对象池中的对象 */
    @Autowired
    @Qualifier("OperationLogService")
    private IOperationLogService operationLogService;
    
    /** 用Srping 注解XJava对象池中的对象 */
    @Autowired
    @Qualifier("OperationLogDAO")
    private IOperationLogDAO     operationLogDAO;
    
    /** 用Srping 注解XJava对象池中的对象 */
    @Autowired
    @Qualifier("XSQL_Common_OperationLog_Query")
    private XSQL                 xsql;

    @Autowired
    @Qualifier("RocketMQProducer")
    private RocketMQProducer messageProducer;
    
    
    
    /**
     * 无参数，直接跳转的。
     * 
     * 访问页面的完整路径为：http://ip:port/WebName/demo/test01
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-09
     * @version     v1.0
     *
     * @return
     */
    @RequestMapping(name="演示用例01" ,value="test01")
    public String test01()
    {
        if ( operationLogService != null && operationLogDAO != null && xsql != null )
        {
            System.out.println("Spring MVC - XJava is Succeed.");
        }
        
        // 跳转页面为：classpath:/pages/demo/demo_01.ftl
        return "/common/demo/demo_01";
    }



    /**
     * 无参数，直接跳转的。
     *
     * 访问页面的完整路径为：http://ip:port/WebName/demo/test01
     *
     * @author      ZhengWei(HY)
     * @createDate  2018-12-09
     * @version     v1.0
     *
     * @return
     */
    @RequestMapping(name="演示发消息" ,value="sendMQ01")
    public String sendMQ01()
    {
        // 发送测试消息
        String topic = "LPS_Test_Topice";
        String tag = "LPS_tag";
        String content = "集成测试消息：" + System.currentTimeMillis();

        // 执行发送
        SendResult result = messageProducer.sendMessage(topic, tag, content);

        // 验证结果
        System.out.println("消息发送成功，消息ID：" + result.toString());
        return "消息发送成功，消息ID：" + result.getSendStatus();
    }
    
    
    /**
     * 有参数传递的页面跳转。
     * 
     * 访问页面的完整路径为：http://ip:port/WebName/demo/test02?userName=HY中文
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-09
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    @RequestMapping(name="演示用例02" ,value="test02")
    public String test02(UserSSO i_User)
    {
        System.out.println(i_User.getUserName());
        
        // 跳转页面为：classpath:/pages/demo/demo_02.ftl
        return "/common/demo/demo_02";
    }
    
    
    
    /**
     * 有参数传递、并且有业务处理的页面跳转。
     * 
     * 访问页面的完整路径为：http://ip:port/WebName/demo/test03?userName=HY中文
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-09
     * @version     v1.0
     *
     * @param i_Request    请求对象
     * @param i_Response   响应对象
     * @param i_User       请求参数
     * @param io_Model     响应结果
     * @return
     */
    @RequestMapping(name="演示用例03" ,value="test03")
    public String test02(HttpServletRequest i_Request ,HttpServletResponse i_Response ,UserSSO i_User ,ModelMap io_Model)
    {
        UserSSO v_New = new UserSSO();           // 响应数据
        
        v_New.setUserName(i_User.getUserName()); // 好比这里是业务处理
        
        io_Model.put("data" ,v_New);             // 响应页面返回传参
        
        // 跳转页面为：classpath:/pages/demo/demo_03.ftl
        return "/common/demo/demo_03";
    }
    
    
    
    /**
     * 打开Ajax动作所在的页面
     * 
     * 访问页面的完整路径为：http://ip:port/WebName/demo/test04?userName=HY中文
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-09
     * @version     v1.0
     *
     * @param i_Request    请求对象
     * @param i_Response   响应对象
     * @param i_User       请求参数
     * @param io_Model     响应结果
     * @return
     */
    @RequestMapping(name="演示用例04" ,value="test04")
    public String test04(HttpServletRequest i_Request ,HttpServletResponse i_Response ,UserSSO i_User ,ModelMap io_Model)
    {
        UserSSO v_New = new UserSSO();            // 响应数据
        
        v_New.setUserName(i_User.getUserName());  // 好比这里是业务处理
        
        io_Model.put("data" ,v_New);              // 响应页面返回传参
        
        // 跳转页面为：classpath:/pages/demo/demo_ajax.ftl
        return "/common/demo/demo_ajax";
    }
    
    
    
    /**
     * 有参数传递的Ajax访问
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-09
     * @version     v1.0
     *
     * @param i_User      请求参数
     * @param i_Response  响应对象
     */
    @RequestMapping(name="演示Ajax" ,value="test04_Ajax")
    public void ajaxDemo(UserSSO i_User ,HttpServletResponse i_Response)
    {
        Map<String ,Object> v_JsonMap = new HashMap<String ,Object>();  // 响应数据
        
        v_JsonMap.put("datas" ,i_User.getUserName() + "123456");        // 设置响应数据
        
        writerJson(i_Response ,v_JsonMap);                              // 响应页面返回传参
    }
    
}
