package org.hy.microservice.common.prometheus;

import java.util.Map;

import org.hy.common.xml.XJava;
import org.hy.microservice.common.BaseData;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;






/**
 * 项目主体基本信息的指示器
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-09-15
 * @version     v1.0
 */
@Component
public class MSInfoContributor implements InfoContributor
{
    
    private Map<String ,Object> info;
    
    
    
    public MSInfoContributor()
    {
        this.info = Map.of("name"        ,XJava.getParam("MS_Common_ServiceName").getValue() 
                          ,"version"     ,XJava.getParam("MS_Common_Version").getValue() 
                          ,"startuptime" ,BaseData.$StartupTime.getFullMilli());
        
        XJava.putObject("MS_Common_Info" ,info);
    }
    
    
    
    @Override
    public void contribute(Info.Builder io_Builder)
    {
        io_Builder.withDetail("app" ,this.info);
    }
    
}
