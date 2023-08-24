var v_UrlName   = '10.1.20.84:85';
var v_Url       = 'http://' + v_UrlName + '/msCDC/windows/images/xsqlLog.png';
var v_SID       = 'cdc';
var v_WSUrl     = 'ws://' + v_UrlName + '/msCDC/report/' + v_SID + "/ZehngWei";
var v_Websocket = null;



function webSocketInit()
{
    // 判断当前浏览器是否支持WebSocket
    if ( 'WebSocket' in window && v_SID != null ) 
    {
        v_Websocket = new WebSocket(v_WSUrl);
    }
    else 
    {
        alert('当前浏览器 Not support websocket')
    }

    //连接发生错误的回调方法
    v_Websocket.onerror = function () 
    {
        console.log("WebSocket连接发生错误");
        reconnect();
    };

    //连接成功建立的回调方法
    v_Websocket.onopen = function () 
    {
        console.log("WebSocket连接成功");
    };

    //接收到消息的回调方法
    v_Websocket.onmessage = function (event) 
    {
        let v_MData = JSON.parse(event.data);
        if ( v_MData.hasOwnProperty("sourceID") && v_MData.sourceID )
        {
            lineAnimation(v_MData.sourceID ,v_MData.targetID ,v_MData.sourceTotal ,v_MData.targetTotal ,v_MData.type);
        }
        
        if ( v_MData.hasOwnProperty("objectTotal") && v_MData.objectTotal )
        {
            initTotal(v_MData.objectTotal);
        }
    };

    //连接关闭的回调方法
    v_Websocket.onclose = function () 
    {
        console.log("WebSocket连接关闭");
        reconnect();
    };


    //当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    //监听页面刷新
    window.onbeforeunload = function () 
    {
        closeWebSocket();
    };
    
    //监听窗口关闭
    window.onunload=function () 
    {
        closeWebSocket();
    };

    /*
    setInterval(function () {
        window.location.reload();
    },3600000)
    */
}


function reconnect()
{
    setInterval(function () 
    {
        //给服务器发送请求，如果发送成功，则说明服务器启动了，那么就关闭websocket连接，刷新页面
        $.ajax({
            url: v_Url,
            type:'POST',
            async:false,
            success:function(data) {    //成功回调函数
                closeWebSocket();
                window.location.reload();
            }
            });

    }, 60000);
}



// 关闭WebSocket连接
function closeWebSocket() 
{
    v_Websocket.close();
};



// 发送消息
function send(message) 
{
    v_Websocket.send(message);
};




/**
  * 线条的动画
  *
  * @author      ZhengWei(HY)
  * @createDate  2023-08-21
  * @version     v1.0
  *
  * @param i_SourceID     源端ID
  * @param i_TargetID     目标ID
  * @param i_SourceTotal  源ID对象的统计数量
  * @param i_TargetTotal  目标ID对象的统计数量
  * @param i_Type         操作类型
  */
function lineAnimation(i_SourceID ,i_TargetID ,i_SourceTotal ,i_TargetTotal ,i_Type)
{
    let v_SVG         = d3.select("body").select("svg");
    let v_LineID      = "line_" + i_SourceID + "_t_" + i_TargetID;
    let v_Line        = v_SVG.select("#"       + v_LineID);
    let v_SourceNode  = v_SVG.select("#node_"  + i_SourceID);
    let v_TargetNode  = v_SVG.select("#node_"  + i_TargetID);
    let v_SourceLabel = v_SVG.select("#label_" + i_SourceID);
    let v_TargetLabel = v_SVG.select("#label_" + i_TargetID);
    
    console.log(i_Type);
    if ( !v_Line )
    {
        return;
    }
    
    let v_TimeLen       = 5 * 1000;
    let v_LineAnimation = d3.select("#linkAnimation");
    v_LineAnimation.append("circle")
        .attr("r", v_Line.attr("stroke-width"))
        .attr("stroke", v_SourceNode.attr("stroke"))
        .attr("fill",   i_Type == "create" ? "green" : (i_Type == "update" ? "orange" : "red"))
        .attr("cx",     v_Line.attr("x1"))
        .attr("cy",     v_Line.attr("y1"))
        .transition()
        .duration(v_TimeLen)
        .attr("stroke", v_TargetNode.attr("stroke"))
        .attr("fill",   i_Type == "create" ? "green" : (i_Type == "update" ? "orange" : "red"))
        .attr("cx",     v_Line.attr("x2"))
        .attr("cy",     v_Line.attr("y2"))
        .remove();
        
    v_SourceLabel.text(i_SourceTotal);
    v_TargetLabel.text(i_TargetTotal);
}



/**
  * 初始化对象统计数据
  *
  * @author      ZhengWei(HY)
  * @createDate  2023-08-24
  * @version     v1.0
  *
  * @param i_Datas  对象统计数据
  */
function initTotal(i_Datas)
{
    let v_SVG = d3.select("body").select("svg");
    
    for (let x=0; x<i_Datas.length; x++)
    {
        v_SVG.select("#label_" + i_Datas[x].totalID).text(i_Datas[x].total);
    }
}