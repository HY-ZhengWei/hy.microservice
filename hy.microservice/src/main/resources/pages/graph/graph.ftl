<html>
<head>
    <title>图谱数显</title>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=yes">
    
    <link rel="stylesheet" href="../bootstrap-4.3.1-dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="../jquery/jquery-ui.min.css" />

    <script type="text/javascript" charset="utf-8" src="../jquery/jquery.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../jquery/jquery-ui.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../bootstrap-4.3.1-dist/js/popper.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../bootstrap-4.3.1-dist/js/tooltip.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../bootstrap-4.3.1-dist/js/bootstrap.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../bootstrap-4.3.1-dist/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../d3/d3.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="../d3/hy.common.d3.js"></script>
    
    <style type="text/css">
    body { 
        background-color: #101832;
        overflow-x: hidden;
        overflow-y: hidden;
        width: 100vw;
        height: 100vh;
    }
    </style>
</head>
<body oncontextmenu="return false">

    <svg width="100%" height="100%" version="1.1" xmlns="http://www.w3.org/2000/svg"></svg>
    

<script>
const v_WebSP        = "${graph.webSProtocol}" == "s" ? "s" : "";
const v_WebSHostName = "${graph.webSHost}/${graph.webSName}";
const v_WebSID       = "${graph.webSID}";
const v_UserID       = "${graph.userID}";
const v_StyleType    = "${graph.styleType}" == "" || "${graph.styleType}" == "icon" ? "icon" : "base";
</script>

<script type="text/javascript" charset="utf-8" src="../graph/graph.webSocket.js"></script>

<script type="module">
d3.json("${graph.dataUrl}")
.then(function (i_Datas) 
{
  const v_LinkDatas    = i_Datas.links.map(d => ({...d}));
  const v_NodeDatas    = i_Datas.nodes.map(d => ({...d}));
  const v_WindowWidth  = document.body.clientWidth;
  const v_WindowHeight = document.body.clientHeight;
  const v_NodeColors   = d3.scaleOrdinal(d3.schemeCategory10);
  
  console.log("加载数据成功，准备初始化。w=" + v_WindowWidth + " ,h=" + v_WindowHeight);
  
  const v_SVG = d3.select("body").select("svg")
        .attr("width", v_WindowWidth)
        .attr("height", v_WindowHeight)
        .attr("viewBox", [0, 0, v_WindowWidth, v_WindowHeight])
        .attr("style", "max-width: 100%; height: auto;");
        
  /* 创建并添加所有节点连接线 */
  const link = v_SVG.append("g")
        .attr("stroke-opacity", 0.6)
        .selectAll()
        .data(v_LinkDatas)
        .join("line")
        .attr("id" ,function(d) 
        {
            return "line_" + d.source + "_t_" + d.target;
        })
        .attr("sourceID" ,d => d.source)
        .attr("targetID" ,d => d.target)
        .attr("stroke-width", d => Math.sqrt(d.width))
        .attr("stroke", d => d.lineColor ? d.lineColor : "#999");
        
  /* 创建节点连接线的动画层 */
  const linkAnimation = v_SVG.append("g")
        .attr("id" ,"linkAnimation");

  /* 创建并添加所有节点 */
  const node = v_SVG.append("g")
        .attr("stroke-width", 1.5)
        .selectAll()
        .data(v_NodeDatas)
        .join(v_StyleType == "icon" ? "g" : "circle")
        .attr("id" ,d => "node_" + d.id)
        .attr("r", d => (v_StyleType == "icon" ? d.size : d.size / 2))
        .attr("stroke", d => d.lineColor ? d.lineColor : "white")
        .attr("fill", d => d.bgColor ? d.bgColor : v_NodeColors(d.group));
        
  /* 创建节点的名称层 */
  const label = v_SVG.append("g")
        .attr("text-anchor" ,"middle") 
        .attr("dominant-baseline" ,"middle")
        .selectAll()
        .data(v_NodeDatas)
        .join("text")
        .attr("id" ,d => "label_" + d.id)
        .attr("shortName" ,d => d.shortName)
        .text("0")
        .attr("fill", d => d.fontColor ? d.fontColor : "white")
        .attr("font-size" ,d => d.fontSize ? d.fontSize : 14);

  if ( v_StyleType == "icon" )
  { 
      node.append("image")
      .attr("xlink:href" ,function(d)
      {
        if ( d.icon )
        {
            return d.icon;
        }
        else if ( d.stType == "S" && d.objectType == "D" )
        {
            return "./images/DataBase01.png";
        }
        else if ( d.stType == "T" && d.objectType == "D" )
        {
            return "./images/DataBase03.png";
        }
        else if ( d.objectType == "T" || d.objectType == "O" )
        {
            return "./images/Table01.png";
        }
        else if ( d.objectType == "F" )
        {
            return "./images/Recovery01.png";
        }
        else if ( d.objectType == "R" )
        {
            return "./images/Datas01.png";
        }
      })
      .attr("height" ,d => d.size + "px")
      .attr("width"  ,d => d.size + "px");
  }

  node.append("title")
  .text(d => d.name);

  // Add a drag behavior.
  node.call(d3.drag()
        .on("start", dragstarted)
        .on("drag", dragged)
        .on("end", dragended));

  // Set the position attributes of links and nodes each time the simulation ticks.
  /** 节点为图片的样式 */
  function ticked_icon() {
    link
        .attr("x1", d => d.source.x)
        .attr("y1", d => d.source.y)
        .attr("x2", d => d.target.x)
        .attr("y2", d => d.target.y);
    
    node
        .attr("x" , d => d.x)
        .attr("y" , d => d.y)
        .attr("cx", d => d.x)
        .attr("cy", d => d.y)
        .attr("transform", function(d) 
        {
            return "translate(" + (d.x - d.size / 2) + "," + (d.y - d.size / 2) + ")";
        });
        
    label
        .attr("x", d => d.x)
        .attr("y", d => d.y + d.size / 2 + 12);
  }
  
  /** 节点为圆圈的样式 */
  function ticked_base() {
    link
        .attr("x1", d => d.source.x)
        .attr("y1", d => d.source.y)
        .attr("x2", d => d.target.x)
        .attr("y2", d => d.target.y);
    
    node
        .attr("cx", d => d.x)
        .attr("cy", d => d.y);
        
    label
        .attr("x", d => d.x)
        .attr("y", d => d.y);
  }

  // Reheat the simulation when drag starts, and fix the subject position.
  function dragstarted(event) {
    if (!event.active) simulation.alphaTarget(0.3).restart();
    event.subject.fx = event.subject.x;
    event.subject.fy = event.subject.y;
  }

  // Update the subject (dragged node) position during drag.
  function dragged(event) {
    event.subject.fx = event.x;
    event.subject.fy = event.y;
  }

  // Restore the target alpha so the simulation cools after dragging ends.
  // Unfix the subject position now that it’s no longer being dragged.
  function dragended(event) {
    if (!event.active) simulation.alphaTarget(0);
    event.subject.fx = null;
    event.subject.fy = null;
  }
  
    // Create a simulation with several forces.
  const simulation = d3.forceSimulation(v_NodeDatas)
      .force("link", d3.forceLink(v_LinkDatas).id(d => d.id).distance(function(d) {return d.length}).strength(0.1))
      .force("charge", d3.forceManyBody())
      .force("center", d3.forceCenter(v_WindowWidth / 2, v_WindowHeight / 2))
      .on("tick", v_StyleType == "icon" ? ticked_icon : ticked_base);
      
  console.log("初始化完成");
  webSocketInit();
})
.catch(function (i_Error) 
{
    console.log(i_Error);
});



d3.select("#Test").on("click" ,function ()
{
    
});
</script>

</body>
</html>