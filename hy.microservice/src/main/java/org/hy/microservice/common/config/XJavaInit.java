package org.hy.microservice.common.config;

import java.util.List;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.common.xml.plugins.AppInitConfig;





/**
 * Web初始化信息
 * 
 * @author      ZhengWei(HY)、马龙
 * @createDate  2020-11-19
 * @version     v1.0
 */
public class XJavaInit extends AppInitConfig
{
    private static Logger  $Logger = Logger.getLogger(XJavaInit.class);
    
    private static boolean $Init = false;
    
    private String xmlRoot;
    
    
    
    public XJavaInit()
    {
        this(true);
    }
    
    
    
    public XJavaInit(boolean i_IsStartJobs)
    {
        super(i_IsStartJobs);
        this.xmlRoot = Help.getClassHomePath();
        init(i_IsStartJobs);

    }

    public XJavaInit(boolean i_IsLog ,String i_EnCode)
    {
        super(i_IsLog,i_EnCode);
    }
    
    
    
    @SuppressWarnings("unchecked")
    private synchronized void init(boolean i_IsStartJobs)
    {
        if ( !$Init )
        {
            $Init = true;
            
            try
            {
                this.init("config/ms.sys.Config.xml"     ,this.xmlRoot);
                this.init("config/ms.startup.Config.xml" ,this.xmlRoot);
                this.init((List<Param>)XJava.getObject("StartupConfig_MS_Common") ,this.xmlRoot);
                this.init(((Param)XJava.getObject("MS_Common_RootPackageName")).getValue());
                
                if ( i_IsStartJobs )
                {
                    this.init("config/ms.job.xml" ,this.xmlRoot);
                }
            }
            catch (Exception exce)
            {
                $Logger.error(exce);
            }
        }
    }

}
