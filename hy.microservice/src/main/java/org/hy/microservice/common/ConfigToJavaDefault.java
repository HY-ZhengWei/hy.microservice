package org.hy.microservice.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.XJavaID;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;





/**
 * 配置信息转化为XJava对象实例
 * 
 * 确保相同配置信息的“单例化”
 *
 * @author      ZhengWei(HY)
 * @createDate  2022-09-01
 * @version     v1.0
 * @param <Config>    配置信息的类
 * @param <Java>      实例化的对象类
 */
public abstract class ConfigToJavaDefault<Config extends XJavaID ,Java> implements ConfigToJava<Config ,Java>
{
    
    private static final Logger                   $Logger     = new Logger(ConfigToJavaDefault.class);
    
    /** 所有配置信息转化类实例的集合 */
    private static final List<ConfigToJava<? ,?>> $AllConfigs = new ArrayList<ConfigToJava<? ,?>>();
    
    
    
    /** 本地对象XID，即仅保存通过本实例创建出的对象实例XID。Map.key为 XID */
    private Map<String ,String> localObjectXIDs = new HashMap<String ,String>();
    
    
    
    public ConfigToJavaDefault()
    {
        $AllConfigs.add(this);
    }
    
    
    
    /**
     * 全局移除所有对象。好实现 “软重启” 功能
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-12-16
     * @version     v1.0
     *
     */
    public static synchronized void removeAllConfigToJava()
    {
        if ( !Help.isNull($AllConfigs) )
        {
            for (ConfigToJava<? ,?> v_ConfigToJava : $AllConfigs)
            {
                if ( v_ConfigToJava != null )
                {
                    v_ConfigToJava.removeObjects();
                }
            }
        }
    }
    
    
    
    /**
     * 获取配置信息的实例。当已存在时，不重复生成
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-09-01
     * @version     v1.0
     *
     * @param i_Config
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized Java getObject(Config i_Config)
    {
        if ( i_Config == null )
        {
            $Logger.error("配置信息为空");
            return null;
        }
        
        String v_XID = this.getXJavaID(i_Config);
        if ( Help.isNull(v_XID) )
        {
            $Logger.error("配置信息的XJavaID为空");
            return null;
        }
        
        Java v_Java = (Java) XJava.getObject(v_XID);
        
        if ( v_Java == null )
        {
            v_Java = newObject(i_Config);
            if ( v_Java != null )
            {
                this.localObjectXIDs.put(v_XID ,v_XID);
                XJava.putObject(v_XID ,v_Java);
            }
        }
        
        return v_Java;
    }
    
    
    
    /**
     * 移除对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-09-16
     * @version     v1.0
     *
     * @param i_Config
     */
    @Override
    public synchronized void removeObject(Config i_Config)
    {
        XJava.remove(this.getXJavaID(i_Config));
    }
    
    
    
    /**
     * 移除所有对象。好配合 “软重启” 功能
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-12-16
     * @version     v1.0
     *
     */
    @Override
    public synchronized void removeObjects()
    {
        if ( !Help.isNull(this.localObjectXIDs) )
        {
            for (String v_XID : this.localObjectXIDs.keySet())
            {
                XJava.remove(v_XID);
            }
            
            this.localObjectXIDs.clear();
        }
    }
    
    
    
    /**
     * 方便实现者个性化定制（通过重写的方式）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-09-05
     * @version     v1.0
     *
     * @param i_Config
     * @return
     */
    protected String getXJavaID(Config i_Config)
    {
        return i_Config.getXJavaID();
    }
    
    
    
    /**
     * 获取一个新实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-09-05
     * @version     v1.0
     *
     * @param i_Config
     * @return
     */
    protected abstract Java newObject(Config i_Config);
    
}
