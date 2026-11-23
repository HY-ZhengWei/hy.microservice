package org.hy.microservice.common.probe.client;

import org.hy.common.Date;
import org.hy.common.StringHelp;





/**
 * 数据资产编号
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-11
 * @version     v1.0
 */
public class AssetID
{
    
    /** 序号的长度 */
    public static int $NoLen = 9;
    
    
    
    /** 数据资产编号：日期 */
    private String ymd;
    
    /** 数据资产编号：序号 */
    private long   no;
    
    /** 最后一次生成的数据资产编号 */
    private String lastAssetID;
    
    /** 昨天最后一次生成的数据资产编号 */
    private String lastAssetIDYesterday;
    
    
    
    public AssetID()
    {
        this(Date.getNowTime().getYMD_ID() ,0);
    }
    
    
    
    public AssetID(String i_YMDNo)
    {
        this(i_YMDNo.substring(0 ,8) ,Long.valueOf(i_YMDNo.substring(8)));
    }
    
    
    
    public AssetID(String i_YMD ,long i_No)
    {
        this.ymd = i_YMD;
        this.no  = i_No;
        
        String v_YMD = Date.getNowTime().getYMD_ID();
        
        if ( !this.ymd.equals(v_YMD) )
        {
            this.lastAssetID          = this.ymd + StringHelp.lpad(this.no ,$NoLen ,"0");
            this.lastAssetIDYesterday = this.lastAssetID;
            this.ymd                  = v_YMD;
            this.no                   = 0;
        }
        else if ( this.no >= 1 )
        {
            this.lastAssetID= this.ymd + StringHelp.lpad(this.no ,$NoLen ,"0");
        }
    }
    
    
    
    /**
     * 获取一个新的数据资产编号
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-11
     * @version     v1.0
     *
     * @return
     */
    public synchronized String newAssetID()
    {
        String v_YMD = Date.getNowTime().getYMD_ID();
        
        if ( !this.ymd.equals(v_YMD) )
        {
            this.lastAssetIDYesterday = this.lastAssetID;
            this.ymd                  = v_YMD;
            this.no                   = 0;
        }
        
        this.lastAssetID = this.ymd + StringHelp.lpad((++this.no) ,$NoLen ,"0");
        return this.lastAssetID;
    }
    
    
    /**
     * 获取：数据资产编号：日期
     */
    public synchronized String getYmd()
    {
        return ymd;
    }

    
    /**
     * 获取：数据资产编号：序号
     */
    public synchronized long getNo()
    {
        return no;
    }

    
    /**
     * 获取：最后一次生成的数据资产编号。有可以返回NULL
     */
    public synchronized String getLastAssetID()
    {
        return lastAssetID;
    }

    
    /**
     * 获取：昨天最后一次生成的数据资产编号。有可以返回NULL
     */
    public synchronized String getLastAssetIDYesterday()
    {
        return lastAssetIDYesterday;
    }
    
}
