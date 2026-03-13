package org.hy.microservice.common.demo;

import org.hy.common.app.Param;
import org.hy.common.xml.annotation.Xjava;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;

@Xjava
public class AIAlibabaDashScope
{
    
    @Xjava(ref="MS_Common_AI_AlibabaDashScope_BaseUrl")
    private Param baseUrl;
    
    @Xjava(ref="MS_Common_AI_AlibabaDashScope_ApiKey")
    private Param apiKey;
    
    @Xjava(ref="MS_Common_AI_AlibabaDashScope_MaxTokens")
    private Param maxTokens;
    
    @Xjava(ref="MS_Common_AI_AlibabaDashScope_Temperature")
    private Param temperature;
    
    
    
    public void ai()
    {
        DashScopeApi v_DashScopeApi = DashScopeApi.builder()
                                                  .baseUrl(this.baseUrl.getValue())
                                                  .apiKey(this.apiKey.getValue())
                                                  .build();
        
        DashScopeChatOptions v_Options = DashScopeChatOptions.builder()
                                                             .model(DashScopeModel.ChatModel.QWEN_PLUS.getValue())
                                                             .temperature(this.temperature.getValueDouble())
                                                             .build();
        
        DashScopeChatModel v_ChatModel = DashScopeChatModel.builder()
                                                           .dashScopeApi(v_DashScopeApi)
                                                           .defaultOptions(v_Options)
                                                           .build();
        
        Prompt v_Prompt = new Prompt("你是什么");
        
        ChatResponse v_Response = v_ChatModel.call(v_Prompt);
        
        System.out.println(v_Response.getResult().getOutput().getText());
        System.out.println(v_Response.getMetadata());
    }
    
}
