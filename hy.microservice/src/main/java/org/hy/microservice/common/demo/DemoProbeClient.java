package org.hy.microservice.common.demo;

import org.hy.common.Date;
import org.hy.common.xml.annotation.Xjava;

import org.hy.microservice.common.probe.client.ProbeClient;





@Xjava
public class DemoProbeClient
{
    
    @Xjava
    private ProbeClient client;
    
    
    
    public void test()
    {
        DemoOntology v_Data = new DemoOntology();
        
        v_Data.setId(Date.getNowTime().getFullMilli_ID());
        v_Data.setName("HY");
        v_Data.setSucceed(true);
        v_Data.setAge(100);
        v_Data.setWeight(150.1234);
        
        this.client.dataSave(v_Data ,"ZhengWei");
    }
    
}
