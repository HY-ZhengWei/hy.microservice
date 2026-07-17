package org.hy.microservice.common.message;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.callflow.CallFlow;
import org.hy.common.callflow.common.ValueHelp;
import org.hy.common.callflow.execute.ExecuteElement;
import org.hy.common.callflow.file.IToXml;
import org.hy.common.callflow.node.APIConfig;





/**
 * 短信元素。衍生于接口元素。
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-07-11
 * @version     v1.0
 */
public class SMSConfig extends APIConfig
{
    
    private static final String $ElementType = "xsms";
    
    public static        String $MessageURL  = "http://127.0.0.1";
    
    
    
    static
    {
        CallFlow.getHelpExport().addImportHead($ElementType ,SMSConfig.class);
    }
    
    
    
    /** 短信签名 */
    private String messageType;
    
    /** 短消息微服务地址。默认为：$MessageURL */
    private String messageURL;
    
    /** 发布的消息。可以是数值、上下文变量、XID标识 */
    private String message;
    
    /** 手机号 */
    private String cellPhoneNo;
    
    /** 用户ID。可以是数值、上下文变量、XID标识 */
    private String userID;
    
    
    
    /**
     * 构造器
     *
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
     * @version     v1.0
     *
     */
    public SMSConfig()
    {
        this(0L ,0L);
    }
    
    
    
    /**
     * 构造器
     *
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
     * @version     v1.0
     *
     * @param i_RequestTotal  累计的执行次数
     * @param i_SuccessTotal  累计的执行成功次数
     */
    public SMSConfig(long i_RequestTotal ,long i_SuccessTotal)
    {
        super(i_RequestTotal ,i_SuccessTotal);
        
        this.messageType = "短信签名";
        this.setMessageURL($MessageURL);
        this.setRequestType("POST");
        this.setSucceedFlag("200");
        this.setConnectTimeout(10 * 1000);
        this.setReadTimeout(   15 * 1000);
    }
    
    
    
    /**
     * 静态检查
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
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
        
        if ( Help.isNull(this.getMessageURL()) )
        {
            io_Result.set(false).setParamStr("CFlowCheck：" + this.getClass().getSimpleName() + "[" + Help.NVL(this.getXid()) + "].messageURL is null.");
            return false;
        }
        if ( Help.isNull(this.getMessage()) )
        {
            io_Result.set(false).setParamStr("CFlowCheck：" + this.getClass().getSimpleName() + "[" + Help.NVL(this.getXid()) + "].message is null.");
            return false;
        }
        if ( Help.isNull(this.getCellPhoneNo()) )
        {
            io_Result.set(false).setParamStr("CFlowCheck：" + this.getClass().getSimpleName() + "[" + Help.NVL(this.getXid()) + "].cellPhoneNo is null.");
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
     * 当用户没有设置XID时，可使用此方法生成
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
     * @version     v1.0
     *
     * @return
     */
    public String makeXID()
    {
        return "XSMS_" + StringHelp.getUUID9n();
    }

    
    
    /**
     * 获取：短信签名
     */
    public String getMessageType()
    {
        return messageType;
    }


    
    /**
     * 设置：短信签名
     * 
     * @param i_MessageType 短信签名
     */
    public void setMessageType(String i_MessageType)
    {
        this.messageType = i_MessageType;
        this.reset(this.getRequestTotal() ,this.getSuccessTotal());
        this.keyChange();
    }


    
    /**
     * 获取：短消息微服务地址。默认为：$MessageURL
     */
    public String getMessageURL()
    {
        return messageURL;
    }


    
    /**
     * 设置：短消息微服务地址。默认为：$MessageURL
     * 
     * @param i_MessageURL 短消息微服务地址。默认为：$MessageURL
     */
    public void setMessageURL(String i_MessageURL)
    {
        if ( Help.isNull(i_MessageURL) )
        {
            this.messageURL = $MessageURL;
        }
        else
        {
            this.messageURL = i_MessageURL;
        }
        this.setUrl(this.messageURL + "/msMessage/message/sendSMS");
        this.reset(this.getRequestTotal() ,this.getSuccessTotal());
        this.keyChange();
    }


    
    /**
     * 获取：发布的消息。可以是数值、上下文变量、XID标识
     */
    public String getMessage()
    {
        return message;
    }


    
    /**
     * 设置：发布的消息。可以是数值、上下文变量、XID标识
     * 
     * @param i_Message 发布的消息。可以是数值、上下文变量、XID标识
     */
    public void setMessage(String i_Message)
    {
        this.message = i_Message;
        this.reset(this.getRequestTotal() ,this.getSuccessTotal());
        this.keyChange();
    }

    
    
    /**
     * 获取：手机号
     */
    public String getCellPhoneNo()
    {
        return cellPhoneNo;
    }


    
    /**
     * 设置：手机号
     * 
     * @param i_CellPhoneNo 手机号
     */
    public void setCellPhoneNo(String i_CellPhoneNo)
    {
        this.cellPhoneNo = i_CellPhoneNo;
        this.reset(this.getRequestTotal() ,this.getSuccessTotal());
        this.keyChange();
    }
    
    
    
    /**
     * 获取：用户ID。可以是数值、上下文变量、XID标识
     */
    public String getUserID()
    {
        return userID;
    }


    
    /**
     * 设置：用户ID。可以是数值、上下文变量、XID标识
     * 
     * @param i_UserID 用户ID。可以是数值、上下文变量、XID标识
     */
    public void setUserID(String i_UserID)
    {
        this.userID = i_UserID;
        this.reset(this.getRequestTotal() ,this.getSuccessTotal());
        this.keyChange();
    }



    /**
     * 元素的类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
     * @version     v1.0
     *
     * @return
     */
    public String getElementType()
    {
        return "SMS";
    }
   
    
    
    /**
     * 获取XML内容中的名称，如<名称>内容</名称>
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
     * @version     v1.0
     *
     * @return
     */
    public String toXmlName()
    {
        return $ElementType;
    }
    
    
    
    /**
     * 执行方法前，对方法入参的处理、加工、合成
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
     * @version     v1.0
     *
     * @param io_Context  上下文类型的变量信息
     * @param io_Params   方法执行参数。已用NodeConfig自己的力量生成了执行参数。
     * @return
     * @throws Exception 
     */
    public Object [] generateParams(Map<String ,Object> io_Context ,Object [] io_Params)
    {
        if ( !Help.isNull(this.param) )
        {
            io_Params[0] = ValueHelp.replaceByContext(this.param ,io_Context);
        }
        
        String v_CellPhoneNo = null;
        if ( !Help.isNull(this.cellPhoneNo) )
        {
            v_CellPhoneNo = ValueHelp.replaceByContext(this.cellPhoneNo ,io_Context);
        }
        else
        {
            v_CellPhoneNo = "";
        }
        
        String v_Message = null;
        if ( !Help.isNull(this.message) )
        {
            v_Message = ValueHelp.replaceByContext(this.message ,io_Context);
        }
        else
        {
            v_Message = "";
        }
        
        String v_UserID = null;
        if ( !Help.isNull(this.userID) )
        {
            v_UserID = ValueHelp.replaceByContext(this.userID ,io_Context);
        }
        else
        {
            v_UserID = "";
        }
        
        StringBuilder v_Body = new StringBuilder();
        v_Body.append("{");
        v_Body.append("  \"phone\": \"").append(v_CellPhoneNo).append("\",");
        v_Body.append("  \"message\": \"");
        if ( !Help.isNull(this.messageType) )
        {
            v_Body.append("【").append(this.messageType).append("】");
        }
        v_Body.append(v_Message).append("\",");
        v_Body.append("  \"userID\": \"").append(v_UserID).append("\"");
        v_Body.append("}");
        io_Params[1] = v_Body.toString();
        
        if ( !Help.isNull(this.head) )
        {
            String v_Head = ValueHelp.replaceByContext(this.head ,io_Context);
            try
            {
                io_Params[2] = ValueHelp.getValue(v_Head ,Map.class ,null ,io_Context);
            }
            catch (Exception exce)
            {
                throw new RuntimeException(exce);
            }
        }
        return io_Params;
    }
    
    
    
    /**
     * 生成或写入个性化的XML内容
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
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
        
        if ( !Help.isNull(this.messageType) && !"六盘山实验室".equals(this.messageType) )
        {
            io_Xml.append(v_NewSpace).append(IToXml.toValue("messageType" ,this.messageType));
        }
        if ( !Help.isNull(this.messageURL) && !$MessageURL.equals(this.messageURL) )
        {
            io_Xml.append(v_NewSpace).append(IToXml.toValue("messageURL" ,this.messageURL));
        }
        if ( !Help.isNull(this.message) )
        {
            io_Xml.append(v_NewSpace).append(IToXml.toValue("message" ,this.message));
        }
        if ( !Help.isNull(this.cellPhoneNo) )
        {
            io_Xml.append(v_NewSpace).append(IToXml.toValue("cellPhoneNo" ,this.cellPhoneNo));
        }
        if ( !Help.isNull(this.userID) )
        {
            io_Xml.append(v_NewSpace).append(IToXml.toValue("userID" ,this.userID));
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
     * @createDate  2026-07-11
     * @version     v1.0
     *
     * @param i_Context  上下文类型的变量信息
     * @return
     */
    public String toString(Map<String ,Object> i_Context)
    {
        StringBuilder v_Builder = new StringBuilder();
        
        v_Builder.append(this.messageURL);
        
        v_Builder.append(":");
        if ( Help.isNull(this.getUserID()) )
        {
            v_Builder.append("?");
        }
        else
        {
            try
            {
                v_Builder.append(ValueHelp.getValue(this.getUserID() ,String.class ,this.getUserID() ,i_Context));
            }
            catch (Exception exce)
            {
                // Nothing.
            }
        }
        
        v_Builder.append(":");
        if ( Help.isNull(this.cellPhoneNo) )
        {
            v_Builder.append("?");
        }
        else
        {
            try
            {
                v_Builder.append(ValueHelp.getValue(this.cellPhoneNo ,String.class ,this.cellPhoneNo ,i_Context));
            }
            catch (Exception exce)
            {
                // Nothing.
            }
        }
        
        v_Builder.append(":");
        if ( !Help.isNull(this.messageType) )
        {
            v_Builder.append("【").append(messageType).append("】");
        }
        
        if ( Help.isNull(this.message) )
        {
            v_Builder.append("?");
        }
        else
        {
            try
            {
                v_Builder.append(ValueHelp.getValue(this.message ,String.class ,this.message ,i_Context));
            }
            catch (Exception exce)
            {
                // Nothing.
            }
        }
        
        return v_Builder.toString();
    }
    
    
    
    /**
     * 解析为执行表达式
     * 
     * 建议：子类重写此方法
     *
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
     * @version     v1.0
     *
     * @return
     */
    @Override
    public String toString()
    {
        StringBuilder v_Builder = new StringBuilder();
        
        v_Builder.append(this.messageURL);
        v_Builder.append(":");
        v_Builder.append(Help.NVL(this.getUserID() ,"?"));
        
        v_Builder.append(":");
        v_Builder.append(Help.NVL(this.cellPhoneNo ,"?"));
        
        v_Builder.append(":");
        if ( !Help.isNull(this.messageType) )
        {
            v_Builder.append("【").append(messageType).append("】");
        }
        
        v_Builder.append(Help.NVL(this.message ,"?"));
        
        return v_Builder.toString();
    }
    
    
    
    /**
     * 仅仅创建一个新的实例，没有任何赋值
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
     * @version     v1.0
     *
     * @return
     */
    public Object newMy()
    {
        return new SMSConfig();
    }
    
    
    
    /**
     * 浅克隆，只克隆自己，不克隆路由。
     * 
     * 注：不克隆XID。
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
     * @version     v1.0
     *
     */
    public Object cloneMyOnly()
    {
        SMSConfig v_Clone = new SMSConfig();
        
        this.cloneMyOnly(v_Clone);
        v_Clone.setMessageType(   this.getMessageType());
        v_Clone.setMessageURL(    this.getMessageURL());
        v_Clone.setMessage(       this.getMessage());
        v_Clone.setCellPhoneNo(   this.getCellPhoneNo());
        v_Clone.setUserID(        this.getUserID());
        v_Clone.setParam(         this.getParam());
        v_Clone.setHead(          this.getHead());
        v_Clone.setContext(       this.getContext());
        v_Clone.setConnectTimeout(this.getConnectTimeout());
        v_Clone.setReadTimeout(   this.getReadTimeout());
        v_Clone.setTimeout(       this.getTimeout());
        
        return v_Clone;
    }
    
    
    
    /**
     * 深度克隆编排元素
     * 
     * 建议：子类重写此方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
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
            throw new NullPointerException("Clone SMSConfig xid is null.");
        }
        
        SMSConfig v_Clone = (SMSConfig) io_Clone;
        ((ExecuteElement) this).clone(v_Clone ,i_ReplaceXID ,i_ReplaceByXID ,i_AppendXID ,io_XIDObjects);
        
        v_Clone.setMessageType(   this.getMessageType());
        v_Clone.setMessageURL(    this.getMessageURL());
        v_Clone.setMessage(       this.getMessage());
        v_Clone.setCellPhoneNo(   this.getCellPhoneNo());
        v_Clone.setUserID(        this.getUserID());
        v_Clone.setParam(         this.getParam());
        v_Clone.setHead(          this.getHead());
        v_Clone.setContext(       this.getContext());
        v_Clone.setConnectTimeout(this.getConnectTimeout());
        v_Clone.setReadTimeout(   this.getReadTimeout());
        v_Clone.setTimeout(       this.getTimeout());
    }
    
    
    
    /**
     * 深度克隆编排元素
     * 
     * 建议：子类重写此方法
     *
     * @author      ZhengWei(HY)
     * @createDate  2026-07-11
     * @version     v1.0
     *
     * @return
     * @throws CloneNotSupportedException
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException
    {
        if ( Help.isNull(this.xid) )
        {
            throw new NullPointerException("Clone SMSConfig xid is null.");
        }
        
        Map<String ,ExecuteElement> v_XIDObjects = new HashMap<String ,ExecuteElement>();
        Return<String>              v_Version    = parserXIDVersion(this.xid);
        SMSConfig                   v_Clone      = new SMSConfig();
        
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
