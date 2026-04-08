package org.hy.microservice.common.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.BaseViewMode;

import reactor.core.publisher.Flux;





@Controller
@RequestMapping(name="aidemo" ,value="aidemo")
public class AIDemoController extends BaseController
{
    
    @Autowired
    @Qualifier("AIDeepSeek")
    private AIDeepSeek      aiDeepSeek;
    
    
    /**
     * 重置所有服务配置的统计数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-01-21
     * @version     v1.0
     *
     * @param i_Token
     * @param i_ServiceConfig
     * @return
     */
    @RequestMapping(value="hello" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Flux<String> hello(@RequestParam(value="token" ,required=false) String i_Token
                             ,@RequestBody BaseViewMode i_Data)
    {
        BaseResponse<Flux<String>> v_RetResp = new BaseResponse<Flux<String>>();
        v_RetResp.setData(aiDeepSeek.ai());
        
        return v_RetResp.getData().getData();
    }
    
}
