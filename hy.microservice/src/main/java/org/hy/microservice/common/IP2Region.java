package org.hy.microservice.common;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.lionsoul.ip2region.xdb.Searcher;





/**
 * IP地址与地区
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-09-09
 * @version     v1.0
 */
@Xjava
public class IP2Region
{
    
    private static final Logger $Logger = new Logger(IP2Region.class);
    
    private static boolean      $IsInit = false;
    
    
    
    @Xjava(ref="MS_Common_ApiUseIP2Region")
    private Param          apiUseIP2Region;
    
    private Searcher       searcher;
    
    private List<String[]> regions;
    
    
    
    public IP2Region()
    {
        this.initIP2Region();
    }
    
    
    
    /**
     * 初始化IP地址的地区信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-09
     * @version     v1.0
     *
     */
    private synchronized void initIP2Region()
    {
        if ( $IsInit )
        {
            return;
        }
        
        String dbPath = Help.getClassHomePath() + "lib/ip2region_v4.xdb";
        // 1、从 dbPath 加载整个 xdb 到内存。
        byte [] cBuff = new byte[0];
        try
        {
            cBuff = Searcher.loadContentFromFile(dbPath);
        }
        catch (Exception e)
        {
            $Logger.error("failed to load content from `%s`: %s\n" ,dbPath ,e);
        }
        
        if ( cBuff != null && cBuff.length > 0 )
        {
            // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
            try
            {
                this.searcher = Searcher.newWithBuffer(cBuff);
            }
            catch (Exception e)
            {
                $Logger.error("failed to create content cached searcher: %s\n" ,e);
            }
        }
    }
    
    
    
    /**
     * 初始化允许的国家和地区
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-09
     * @version     v1.0
     *
     */
    private synchronized void initAllowRegion()
    {
        if ( Help.isNull(this.regions) )
        {
            this.regions = new ArrayList<String[]>();
            this.regions.add(new String[]{"内网"});
            
            String [] v_Countrys = this.apiUseIP2Region.getValue().split(";");
            for (String v_Country : v_Countrys)
            {
                String [] v_Regions = v_Country.split(",");
                this.regions.add(v_Regions);
            }
        }
    }
    
    
    
    /**
     * 判定IP地址是否允许访问
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-09
     * @version     v1.0
     *
     * @param i_IP
     * @return
     */
    public boolean isAllow(String i_IP)
    {
        if ( Help.isNull(i_IP) )
        {
            return true;
        }
        
        String v_Region = this.search(i_IP);
        if ( Help.isNull(v_Region) )
        {
            return true;
        }
        
        this.initAllowRegion();
        for (String [] v_AllowRegion : this.regions)
        {
            if ( StringHelp.isContains(v_Region ,true ,v_AllowRegion) )
            {
                return true;
            }
        }
        
        return false;
    }
    
    
    
    /**
     * 按IP地址查找地区
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-09
     * @version     v1.0
     *
     * @param i_IP
     * @return
     */
    public String search(String i_IP)
    {
        if ( this.searcher != null )
        {
            try
            {
                return this.searcher.search(i_IP);
            }
            catch (Exception exce)
            {
                $Logger.error("failed to search(%s): %s\n" ,i_IP ,exce);
            }
        }
        
        return null;
    }
    
}
