package org.hy.microservice.common;

import org.hy.common.Help;
import org.hy.microservice.common.config.XJavaInit;





/**
 * 基础测试类
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-09-23
 * @version     v1.0
 */
public class BaseJunit
{
    private static boolean $IsInit = false;
    
    
    
    public BaseJunit()
    {
        synchronized ( this )
        {
            if ( !$IsInit )
            {
                $IsInit = true;
                
                new XJavaInit(false ,Help.getClassHomePath() + ".." + Help.getSysPathSeparator() + "classes" + Help.getSysPathSeparator());
            }
        }
    }
    
}
