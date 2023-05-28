package org.hy.microservice.common;

import org.hy.common.XJavaID;





/**
 * 配置信息转化为XJava对象实例的接口
 * 
 * @author      ZhengWei(HY)
 * @createDate  2022-09-01
 * @version     v1.0
 * @param <Config>    配置信息的类
 * @param <Java>      实例化的对象类
 */
public interface ConfigToJava<Config extends XJavaID ,Java>
{
    
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
    public Java getObject(Config i_Config);
    
    
    
    /**
     * 移除对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-09-16
     * @version     v1.0
     *
     * @param i_Config
     */
    public void removeObject(Config i_Config);
    
    
    
    /**
     * 移除所有对象。好配合 “软重启” 功能
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-12-16
     * @version     v1.0
     *
     */
    public void removeObjects();
    
}
