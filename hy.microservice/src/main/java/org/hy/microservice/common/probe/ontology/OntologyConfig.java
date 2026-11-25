package org.hy.microservice.common.probe.ontology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.callflow.CallFlow;
import org.hy.common.callflow.execute.ExecuteElement;
import org.hy.common.callflow.execute.ExecuteResult;
import org.hy.common.callflow.file.IToXml;
import org.hy.common.callflow.node.NodeConfig;
import org.hy.common.callflow.node.NodeConfigBase;
import org.hy.common.callflow.node.NodeParam;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;

import org.hy.microservice.common.probe.client.ProbeClient;
import org.hy.microservice.common.probe.schema.ProbeSchemaDomain;





/**
 * 本体元素：本体的定义及数据探针功能的客户端实现
 * 
 * @author      ZhengWei(HY)
 * @createDate  2026-11-23
 * @version     v1.0
 */
public class OntologyConfig extends NodeConfig implements NodeConfigBase
{
    
    private static final Logger $Logger = new Logger(OntologyConfig.class);
    
    private static final String $ElementType = "xontology";
    
    /** 查询类型 */
    public  static final String $Type_Query  = "QUERY";
    
    /** 保存类型 */
    public  static final String $Type_Save   = "SAVE";
    
    /** 删除类型 */
    public  static final String $Type_Del    = "DEL";
    
    
    
    static
    {
        CallFlow.getHelpExport().addImportHead($ElementType ,OntologyConfig.class);
    }
    
    
    
    /** 本体结构。可以是数值、上下文变量、XID标识 */
    private NodeParam schemaParam;
    
    /** 数据。可以是数值、上下文变量、XID标识 */
    private NodeParam dataParam;
    
    /** 用户ID。可以是数值、上下文变量、XID标识 */
    private NodeParam userIDParam;
    
    /** 操作类型（QUERY查询、SAVE保存）。默认为：SAVE */
    private String    type;
    
    
    
    /**
     * 构造器
     *
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     */
    public OntologyConfig()
    {
        this(0L ,0L);
    }
    
    
    
    /**
     * 构造器
     *
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @param i_RequestTotal  累计的执行次数
     * @param i_SuccessTotal  累计的执行成功次数
     */
    public OntologyConfig(long i_RequestTotal ,long i_SuccessTotal)
    {
        super(i_RequestTotal ,i_SuccessTotal);
        
        this.setCallMethod("dataSave");
        
        this.schemaParam = new NodeParam();
        this.schemaParam.setValueClass(ProbeSchemaDomain.class.getName());
        this.setCallParam(this.schemaParam);
        
        this.dataParam = new NodeParam();
        this.dataParam.setValueClass(Map.class.getName());
        this.setCallParam(this.dataParam);
        
        this.userIDParam = new NodeParam();
        this.userIDParam.setValueClass(String.class.getName());
        this.setCallParam(this.userIDParam);
        
        this.type = $Type_Save;
    }
    
    
    
    /**
     * 静态检查
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @param io_Result     表示检测结果
     * @return
     */
    public boolean check(Return<Object> io_Result)
    {
        if ( !super.check(io_Result) )
        {
            return false;
        }
        
        if ( Help.isNull(this.getSchema()) )
        {
            io_Result.set(false).setParamStr("CFlowCheck：" + this.getClass().getSimpleName() + "[" + Help.NVL(this.getXid()) + "].schema is null.");
            return false;
        }
        if ( Help.isNull(this.getData()) )
        {
            io_Result.set(false).setParamStr("CFlowCheck：" + this.getClass().getSimpleName() + "[" + Help.NVL(this.getXid()) + "].data is null.");
            return false;
        }
        if ( Help.isNull(this.getUserID()) )
        {
            io_Result.set(false).setParamStr("CFlowCheck：" + this.getClass().getSimpleName() + "[" + Help.NVL(this.getXid()) + "].userID is null.");
            return false;
        }
        
        return true;
    }
    
    
    
    /**
     * 运行时中获取模拟数据。
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @param io_Context   上下文类型的变量信息
     * @param i_BeginTime  编排元素的开始时间
     * @param io_Result    编排元素的执行结果
     * @return             表示是否有模拟数据
     */
    public boolean mock(Map<String ,Object> io_Context ,long i_BeginTime ,ExecuteResult io_Result) 
    {
        return super.mock(io_Context ,i_BeginTime ,io_Result ,null ,HashMap.class.getName());
    }
    
    
    
    /**
     * 当用户没有设置XID时，可使用此方法生成
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @return
     */
    public String makeXID()
    {
        return "XOntology_" + StringHelp.getUUID9n();
    }

    
    
    /**
     * 获取：本体结构。可以是数值、上下文变量、XID标识
     */
    public String getSchema()
    {
        return this.schemaParam.getValue();
    }


    
    /**
     * 设置：本体结构。可以是数值、上下文变量、XID标识
     * 
     * @param i_Schema 本体结构。可以是数值、上下文变量、XID标识
     */
    public void setSchema(String i_Schema)
    {
        this.schemaParam.setValue(i_Schema);
        this.reset(this.getRequestTotal() ,this.getSuccessTotal());
        this.keyChange();
    }


    
    /**
     * 获取：数据资产变量的XID。可以是数值、上下文变量、XID标识
     */
    public String getData()
    {
        return this.dataParam.getValue();
    }


    
    /**
     * 设置：数据资产变量的XID。可以是数值、上下文变量、XID标识
     * 
     * @param i_Data 数据资产变量的XID。可以是数值、上下文变量、XID标识
     */
    public void setData(String i_Data)
    {
        this.dataParam.setValue(i_Data);
        this.reset(this.getRequestTotal() ,this.getSuccessTotal());
        this.keyChange();
    }
    
    
    
    /**
     * 获取：用户ID。可以是数值、上下文变量、XID标识
     */
    public String getUserID()
    {
        return this.userIDParam.getValue();
    }


    
    /**
     * 设置：用户ID。可以是数值、上下文变量、XID标识
     * 
     * @param i_UserID 用户ID。可以是数值、上下文变量、XID标识
     */
    public void setUserID(String i_UserID)
    {
        this.userIDParam.setValue(i_UserID);
        this.reset(this.getRequestTotal() ,this.getSuccessTotal());
        this.keyChange();
    }


    
    /**
     * 获取：操作类型（QUERY查询、SAVE保存）。默认为：SAVE
     */
    public String getType()
    {
        return type;
    }


    
    /**
     * 设置：操作类型（QUERY查询、SAVE保存）。默认为：SAVE
     * 
     * @param i_Type 操作类型（QUERY查询、SAVE保存）。默认为：SAVE
     */
    public void setType(String i_Type)
    {
        this.type = i_Type.trim().toUpperCase();
        this.reset(this.getRequestTotal() ,this.getSuccessTotal());
        this.keyChange();
    }



    /**
     * 设置XJava池中对象的ID标识。此方法不用用户调用设置值，是自动的。
     * 
     * 自己反射调用自己的实例中的方法
     * 
     * @param i_XJavaID
     */
    public void setXJavaID(String i_Xid)
    {
        super.setXJavaID(i_Xid);
        this.setCallXID(this.getXid());
    }
    
    
    
    /**
     * 设置：全局惟一标识ID
     * 
     * 自己反射调用自己的实例中的方法
     * 
     * @param i_Xid 全局惟一标识ID
     */
    public void setXid(String i_Xid)
    {
        super.setXid(i_Xid);
        this.setCallXID(this.getXid());
    }
    
    
    
    /**
     * 元素的类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @return
     */
    public String getElementType()
    {
        return "ONT";
    }
    
    
    
    /**
     * 获取XML内容中的名称，如<名称>内容</名称>
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @return
     */
    public String toXmlName()
    {
        return $ElementType;
    }
    
    
    
    /**
     * 转XML时是否显示retFalseIsError属性
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @return
     */
    public boolean xmlShowRetFalseIsError()
    {
        return false;
    }
    
    
    
    /**
     * 执行方法前，对执行对象的处理
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @param io_Context        上下文类型的变量信息
     * @param io_ExecuteObject  执行对象。已用NodeConfig自己的力量生成了执行对象。
     * @return
     */
    public Object generateObject(Map<String ,Object> io_Context ,Object io_ExecuteObject)
    {
        // 其实就是返回自己。io_ExecuteObject 获取正确时，也是this自己
        return io_ExecuteObject == null ? this : io_ExecuteObject;
    }
    
    
    
    /**
     * 执行方法前，对方法入参的处理、加工、合成
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @param io_Context  上下文类型的变量信息
     * @param io_Params   方法执行参数。已用NodeConfig自己的力量生成了执行参数。
     * @return
     * @throws Exception 
     */
    public Object [] generateParams(Map<String ,Object> io_Context ,Object [] io_Params)
    {
        return io_Params;
    }
    
    
    
    /**
     * 数据探针的数据保存（用户动态的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @param i_Schema  数据探针的结构
     * @param i_Data    数据
     * @param i_UserID  用户ID
     * @return          在查询类型时，返回值表示查询结果
     *                  在保存类型时，返回值什么都不表示
     */
    public List<Map<String ,Object>> dataSave(ProbeSchemaDomain i_Schema ,Map<String ,Object> i_Data ,String i_UserID)
    {
        try
        {
            if ( i_Schema == null )
            {
                throw new RuntimeException("Schema is null");
            }
            if ( Help.isNull(i_Data) )
            {
                throw new RuntimeException("Data is null");
            }
            if ( Help.isNull(i_UserID) )
            {
                throw new RuntimeException("UserID is null");
            }
            
            ProbeClient               v_ProbeClient = (ProbeClient) XJava.getObject("ProbeClient");
            List<Map<String ,Object>> v_QueryRet    = null;
            if ( $Type_Save.equals(this.getType()) )
            {
                v_ProbeClient.dataSave(i_Schema ,i_Data ,i_UserID);
            }
            else if ( $Type_Del.equals(this.getType()) )
            {
                v_ProbeClient.delData(i_Schema ,Help.toListKeys(i_Data) ,i_UserID);
            }
            else
            {
                v_QueryRet = v_ProbeClient.queryData(i_Schema ,i_Data ,i_UserID);
            }
            return v_QueryRet;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
    /**
     * 生成或写入个性化的XML内容
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @param io_Xml         XML内容的缓存区
     * @param i_Level        层级。最小下标从0开始。
     *                           0表示每行前面有0个空格；
     *                           1表示每行前面有4个空格；
     *                           2表示每行前面有8个空格；
     * @param i_Level1       单级层级的空格间隔
     * @param i_LevelN       N级层级的空格间隔
     * @param i_SuperTreeID  父级树ID
     * @param i_TreeID       当前树ID
     */
    public void toXmlContent(StringBuilder io_Xml ,int i_Level ,String i_Level1 ,String i_LevelN ,String i_SuperTreeID ,String i_TreeID)
    {
        String v_NewSpace = "\n" + i_LevelN + i_Level1;
        
        if ( !Help.isNull(this.schemaParam.getValue()) )
        {
            io_Xml.append(v_NewSpace).append(IToXml.toValue("schema" ,this.schemaParam.getValue()));
        }
        if ( !Help.isNull(this.dataParam.getValue()) )
        {
            io_Xml.append(v_NewSpace).append(IToXml.toValue("data"   ,this.dataParam.getValue()));
        }
        if ( !Help.isNull(this.userIDParam.getValue()) )
        {
            io_Xml.append(v_NewSpace).append(IToXml.toValue("userID" ,this.userIDParam.getValue()));
        }
        if ( !$Type_Save.equals(this.getType()) )
        {
            io_Xml.append(v_NewSpace).append(IToXml.toValue("type"   ,this.getType()));
        }
    }
    
    
    
    /**
     * 解析为实时运行时的执行表达式
     * 
     * 注：禁止在此真的执行方法
     * 
     * 建议：子类重写此方法
     *
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @param i_Context  上下文类型的变量信息
     * @return
     */
    @SuppressWarnings("unchecked")
    public String toString(Map<String ,Object> i_Context)
    {
        StringBuilder v_Builder = new StringBuilder();
        
        v_Builder.append(this.getType()).append(" ");
        if ( !Help.isNull(this.schemaParam.getValue()) )
        {
            v_Builder.append(this.schemaParam.getValue());
            try
            {
                ProbeSchemaDomain v_Data = (ProbeSchemaDomain) this.schemaParam.gatValue(i_Context);
                if ( v_Data != null )
                {
                    v_Builder.append(v_Data.getType());
                }
                else
                {
                    v_Builder.append("?");
                }
            }
            catch (Exception exce)
            {
                $Logger.error(exce);
                v_Builder.append("?");
            }
        }
        else
        {
            v_Builder.append("?");
        }
        
        v_Builder.append(" = ");
        if ( !Help.isNull(this.dataParam.getValue()) )
        {
            try
            {
                Map<String ,Object> v_Data = (Map<String ,Object>) this.dataParam.gatValue(i_Context);
                if ( !Help.isNull(v_Data) )
                {
                    v_Builder.append(v_Data);
                }
                else
                {
                    v_Builder.append("?");
                }
            }
            catch (Exception exce)
            {
                $Logger.error(exce);
                v_Builder.append("?");
            }
        }
        else
        {
            v_Builder.append("?");
        }
        
        return v_Builder.toString();
    }
    
    
    
    /**
     * 解析为执行表达式
     * 
     * 建议：子类重写此方法
     *
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @return
     */
    @Override
    public String toString()
    {
        StringBuilder v_Builder = new StringBuilder();
        
        if ( !Help.isNull(this.schemaParam.getValue()) )
        {
            v_Builder.append(this.schemaParam.getValue());
        }
        else
        {
            v_Builder.append("?");
        }
        
        v_Builder.append(" = ");
        if ( !Help.isNull(this.dataParam.getValue()) )
        {
            v_Builder.append(this.dataParam.getValue());
        }
        else
        {
            v_Builder.append("?");
        }
        
        return v_Builder.toString();
    }
    
    
    
    /**
     * 仅仅创建一个新的实例，没有任何赋值
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @return
     */
    public Object newMy()
    {
        return new OntologyConfig();
    }
    
    
    
    /**
     * 浅克隆，只克隆自己，不克隆路由。
     * 
     * 注：不克隆XID。
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     */
    public Object cloneMyOnly()
    {
        OntologyConfig v_Clone = new OntologyConfig();
        
        this.cloneMyOnly(v_Clone);
        // v_Clone.callXID  = this.callXID;     不能克隆callXID，因为它就是类自己的xid
        v_Clone.callMethod  = this.callMethod; 
        v_Clone.timeout     = this.timeout;
        v_Clone.type        = this.type;
        
        if ( !Help.isNull(this.callParams) )
        {
            v_Clone.callParams = new ArrayList<NodeParam>();
            for (NodeParam v_NodeParam : this.callParams)
            {
                v_Clone.callParams.add((NodeParam) v_NodeParam.cloneMyOnly());
            }
            
            v_Clone.schemaParam = v_Clone.callParams.get(0);
            v_Clone.dataParam   = v_Clone.callParams.get(1);
            v_Clone.userIDParam = v_Clone.callParams.get(2);
        }
        
        return v_Clone;
    }
    
    
    
    /**
     * 深度克隆编排元素
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @param io_Clone        克隆的复制品对象
     * @param i_ReplaceXID    要被替换掉的XID中的关键字（可为空）
     * @param i_ReplaceByXID  新的XID内容，替换为的内容（可为空）
     * @param i_AppendXID     替换后，在XID尾追加的内容（可为空）
     * @param io_XIDObjects   已实例化的XID对象。Map.key为XID值
     * @return
     */
    public void clone(Object io_Clone ,String i_ReplaceXID ,String i_ReplaceByXID ,String i_AppendXID ,Map<String ,ExecuteElement> io_XIDObjects)
    {
        if ( Help.isNull(this.xid) )
        {
            throw new NullPointerException("Clone OntologyConfig xid is null.");
        }
        
        OntologyConfig v_Clone = (OntologyConfig) io_Clone;
        ((ExecuteElement) this).clone(v_Clone ,i_ReplaceXID ,i_ReplaceByXID ,i_AppendXID ,io_XIDObjects);
        
        // v_Clone.callXID  = this.callXID;     不能克隆callXID，因为它就是类自己的xid
        v_Clone.callMethod  = this.callMethod; 
        v_Clone.timeout     = this.timeout;
        v_Clone.type        = this.type;
        
        if ( !Help.isNull(this.callParams) )
        {
            v_Clone.callParams = new ArrayList<NodeParam>();
            for (NodeParam v_NodeParam : this.callParams)
            {
                v_Clone.callParams.add((NodeParam) v_NodeParam.cloneMyOnly());
            }
            
            v_Clone.schemaParam  = v_Clone.callParams.get(0);
            v_Clone.dataParam = v_Clone.callParams.get(1);
            v_Clone.userIDParam  = v_Clone.callParams.get(2);
        }
    }
    
    
    
    /**
     * 深度克隆编排元素
     * 
     * 建议：子类重写此方法
     *
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @return
     * @throws CloneNotSupportedException
     *
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        if ( Help.isNull(this.xid) )
        {
            throw new NullPointerException("Clone OntologyConfig xid is null.");
        }
        
        Map<String ,ExecuteElement> v_XIDObjects = new HashMap<String ,ExecuteElement>();
        Return<String>              v_Version    = parserXIDVersion(this.xid);
        OntologyConfig              v_Clone      = new OntologyConfig();
        
        if ( v_Version.booleanValue() )
        {
            this.clone(v_Clone ,v_Version.getParamStr() ,XIDVersion + (v_Version.getParamInt() + 1) ,""         ,v_XIDObjects);
        }
        else
        {
            this.clone(v_Clone ,""                      ,""                                         ,XIDVersion ,v_XIDObjects);
        }
        
        v_XIDObjects.clear();
        v_XIDObjects = null;
        return v_Clone;
    }
    
}
