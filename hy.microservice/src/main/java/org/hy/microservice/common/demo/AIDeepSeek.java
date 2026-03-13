package org.hy.microservice.common.demo;

import org.hy.common.app.Param;
import org.hy.common.xml.annotation.Xjava;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.ai.model.ApiKey;
import org.springframework.ai.model.SimpleApiKey;

import reactor.core.publisher.Flux;





@Xjava
public class AIDeepSeek
{
    
    @Xjava(ref="MS_Common_AI_DeepSeek_BaseUrl")
    private Param baseUrl;
    
    @Xjava(ref="MS_Common_AI_DeepSeek_ApiKey")
    private Param apiKey;
    
    @Xjava(ref="MS_Common_AI_DeepSeek_MaxTokens")
    private Param maxTokens;
    
    @Xjava(ref="MS_Common_AI_DeepSeek_Temperature")
    private Param temperature;
    
    
    
    public Flux<String> ai()
    {
        ApiKey      v_ApiKey      = new SimpleApiKey(this.apiKey.getValue());
        DeepSeekApi v_DeepSeekApi = DeepSeekApi.builder()
                                               .baseUrl(this.baseUrl.getValue())
                                               .apiKey(v_ApiKey)
                                               .build();
        
        DeepSeekChatOptions v_Options = DeepSeekChatOptions.builder()
                                                           .model(DeepSeekApi.ChatModel.DEEPSEEK_CHAT.getValue())
                                                           .temperature(this.temperature.getValueDouble())
                                                           .maxTokens(this.maxTokens.getValueInt())
                                                           .build();
        
        DeepSeekChatModel v_ChatModel = DeepSeekChatModel.builder()
                                                         .deepSeekApi(v_DeepSeekApi)
                                                         .defaultOptions(v_Options)
                                                         .build();
        
        Prompt v_Prompt = new Prompt("你是什么");
        
        // ChatResponse v_Response = v_ChatModel.call(v_Prompt);
        // System.out.println(v_Response.getResult().getOutput().getText());
        // System.out.println(v_Response.getMetadata());
        
        Flux<String> v_Flux = ChatClient.builder(v_ChatModel).build().prompt(v_Prompt).stream().content();
        if ( v_Flux == null )
        {
            System.out.println("1111111111");
        }
        else
        {
            System.out.println("000000000000");
        }
        
        return v_Flux;
    }
    
}
