package org.hy.microservice.common.operationLog;

import org.hy.common.xml.SerializableDef;





/**
 * 操作日志中的系统模块
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-12
 * @version     v1.0
 */
public class OperationLogModule extends SerializableDef
{

    private static final long serialVersionUID = 7714086021925628293L;
    
    /** 模块编号 */
    private String moduleCode;
    
    /** 模块名称 */
    private String moduleName;

    
    
    /**
     * 获取：模块编号
     */
    public String getModuleCode()
    {
        return moduleCode;
    }

    
    /**
     * 设置：模块编号
     * 
     * @param i_ModuleCode 模块编号
     */
    public void setModuleCode(String i_ModuleCode)
    {
        this.moduleCode = i_ModuleCode;
    }

    
    /**
     * 获取：模块名称
     */
    public String getModuleName()
    {
        return moduleName;
    }

    
    /**
     * 设置：模块名称
     * 
     * @param i_ModuleName 模块名称
     */
    public void setModuleName(String i_ModuleName)
    {
        this.moduleName = i_ModuleName;
    }
    
}
