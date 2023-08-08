package org.hy.microservice.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                this.loadXML("config/ms.sys.Config.xml" ,this.xmlRoot);
                
                // 父项目参数加载之后，公共资源加载之前，先行加载子项目的初始化参数
                this.loadDirectory("config/initialization/" ,this.xmlRoot);
                
                // 加载子项目的启动配置
                this.loadDirectory("config/startup/" ,this.xmlRoot);
                
                this.loadXML("config/ms.startup.Config.xml" ,this.xmlRoot);
                this.loadXML((List<Param>)XJava.getObject("StartupConfig_MS_Common") ,this.xmlRoot);
                
                // 加载多个顶级包的路径
                String    v_RootPackageNames = ((Param)XJava.getObject("MS_Common_RootPackageName")).getValue();
                String [] v_RPackageNames    = v_RootPackageNames.split(",");
                if ( !Help.isNull(v_RPackageNames) )
                {
                    for (String v_RPackageName : v_RPackageNames)
                    {
                        this.loadClasses(v_RPackageName);
                    }
                }
                
                // 在注解解释完成后，在Job任务解释之前执行
                this.loadDirectory("config/startupFinish/" ,this.xmlRoot);
                
                if ( i_IsStartJobs )
                {
                    this.loadXML("config/ms.job.xml" ,this.xmlRoot);
                }
                
                Map<String ,String> v_AppMsgKeySysID = new HashMap<String ,String>();
                v_AppMsgKeySysID.put(XJava.getParam("MS_Common_AppMsg_SYSID").getValue() ,XJava.getParam("MS_Common_AppMsg_MsgPWD").getValue());
                XJava.putObject("AppMsgKeySysID" ,v_AppMsgKeySysID);
            }
            catch (Exception exce)
            {
                $Logger.error(exce);
            }
        }
    }

}
