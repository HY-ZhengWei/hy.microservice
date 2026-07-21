package org.hy.microservice.common.minio;

import java.util.concurrent.TimeUnit;

import org.hy.common.Help;
import org.hy.common.minio.MinioHelp;
import org.hy.common.xml.XJava;





/**
 * 演示单元：Minio
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-07-14
 * @version     v1.0
 */
public class DemoMinio
{
    
    /** Minio客户端对象 */
    private MinioHelp minio;
    
    
    
    public void demo()
    {
        String v_UserNo        = "60091";
        String v_MinioFileName = "申公豹：人心中的成见是一座大山，任你怎么努力都休想搬运.mp4";
        String v_FileFullName  = "C:\\Users\\ZLX\\Downloads\\申公豹：人心中的成见是一座大山，任你怎么努力都休想搬运.mp4";
        
        this.minio = (MinioHelp) XJava.getObject("MS_Common_MinioHelp");
        this.minio.getPathType(v_UserNo ,"Tools/");
        
        Help.print(this.minio.queryBucketNames());
        this.minio.createBucket(v_UserNo);
        this.minio.upload(v_UserNo ,v_FileFullName ,v_MinioFileName);
        this.minio.download(v_UserNo ,v_MinioFileName ,"D:\\迅雷下载");
        this.minio.share(v_UserNo ,v_MinioFileName ,7 ,TimeUnit.DAYS);
        this.minio.getFiles(v_UserNo ,"" ,true);
        this.minio.getFiles(v_UserNo ,"" ,false);
        this.minio.getFiles(v_UserNo ,"Tools/" ,false);
        this.minio.getFiles(v_UserNo ,"Tools/" ,true);
        this.minio.getFiles(v_UserNo ,"二期/" ,false);
        this.minio.getFiles(v_UserNo ,"二期/" ,true);
        this.minio.getPathType(v_UserNo ,v_MinioFileName);
        this.minio.getPathType(v_UserNo ,"二期/");
        this.minio.getPathType(v_UserNo ,"二期");
        this.minio.getPathType(v_UserNo ,"Tools/");
        this.minio.delete(v_UserNo ,v_MinioFileName);
    }
    
}
