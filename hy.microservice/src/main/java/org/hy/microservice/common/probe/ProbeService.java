package org.hy.microservice.common.probe;

import java.io.Serializable;

import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.xml.annotation.Xjava;

import org.hy.microservice.common.probe.schema.IProbeSchemaService;
import org.hy.microservice.common.probe.schema.ProbeSchemaDomain;





/**
 * 业务层：数据探针
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
@Xjava
public class ProbeService implements IProbeService ,Serializable
{

    private static final long serialVersionUID = 3601265677534101116L;
    
    
    
    @Xjava
    private IProbeSchemaService probeSchemaService;

    
    
    /**
     * 数据探针
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     *
     * @param i_ProbeRequest  数据探针的请求
     * @return                数据探针的响应
     */
    @Override
    public ProbeResponseDomain data(ProbeRequestDomain i_ProbeRequest)
    {
        ProbeSchemaDomain v_Schema = i_ProbeRequest.getSchema();
        
        if ( v_Schema != null )
        {
        }
        
        return null;
    }
    
    
    
    /**
     * 保存数据探针的本体结构
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     *
     * @param io_Schema  本体结构
     * @return           Return.value     表示是否成功
     *                   Return.paramStr  表示异常消息
     *                   Return.paramObj  表示成功时的数据探针的本体结构
     */
    public synchronized Return<ProbeSchemaDomain> saveSchema(ProbeSchemaDomain io_Schema)
    {
        Return<ProbeSchemaDomain> v_Ret = new Return<ProbeSchemaDomain>();
        
        if ( Help.isNull(io_Schema.getType()) )
        {
            return v_Ret.set(false).setParamStr("数据探针的本体结构的归类为空");
        }
        
        if ( this.probeSchemaService.save(io_Schema) == null )
        {
            return v_Ret.set(false).setParamStr("系统内部异常，请联系管理");
        }
        
        return v_Ret.setParamObj(io_Schema);
    }
    
}
