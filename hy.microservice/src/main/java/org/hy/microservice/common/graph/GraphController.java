package org.hy.microservice.common.graph;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Help;
import org.hy.microservice.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;





/**
 * 图谱数显的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-08
 * @version     v1.0
 */
@Controller
@RequestMapping("/graph")
public class GraphController extends BaseController
{
    
    /**
     * 显示图谱数量页面
     * 
     * 使用案例
     * http://127.0.0.1:81/hy.microservice/graph/show?userID=ZhengWei&webSHost=127.0.0.1:81&webSName=msCDC&webSID=cdc&dataUrl=http://127.0.0.1:81/msCDC/neo4j/neo4j.cdc.json
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-08
     * @version     v1.0
     *
     * @param i_Request
     * @param i_Response
     * @param i_Graph
     * @param io_Model
     * @return
     */
    @RequestMapping(value="/show")
    public String show(HttpServletRequest i_Request ,HttpServletResponse i_Response ,GraphInfo i_Graph ,ModelMap io_Model)
    {
        i_Graph.setWebSProtocol(Help.NVL(i_Graph.getWebSProtocol()));
        i_Graph.setStyleType(   Help.NVL(i_Graph.getStyleType()));
        
        io_Model.put("graph" ,i_Graph);
        
        return "/graph/graph";
    }
    
}
