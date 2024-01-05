package org.hy.microservice.common.openapi;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.hy.common.Help;
import org.hy.common.xml.XJava;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;





/**
 * 数据库设计文档的生成
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-05
 * @version     v1.0
 */
public class DBDocument
{
    
    
    /**
     * 数据库设计文档的生成
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-05
     * @version     v1.0
     *
     * @param i_DataSourceXID  数据源XID
     * @param i_Version        文档版本号：如 1.0.0
     * @param i_SaveDir        保存路径
     * @param i_SaveShortName  保存文件名称，不包含扩展名
     */
    public static void makeDatabaseDoc(String i_DataSourceXID ,String i_Version)
    {
        String v_SaveDir       = Help.getClassHomePath() + ".." + Help.getSysPathSeparator() + ".." + Help.getSysPathSeparator() + "doc";
        String v_SaveShortName = "OpenApi.数据库设计";
        
        makeDatabaseDoc(i_DataSourceXID ,i_Version ,v_SaveDir ,v_SaveShortName);
    }

    
    
    /**
     * 数据库设计文档的生成
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-05
     * @version     v1.0
     *
     * @param i_DataSourceXID  数据源XID
     * @param i_Version        文档版本号：如 1.0.0
     * @param i_SaveDir        保存路径
     * @param i_SaveShortName  保存文件名称，不包含扩展名
     */
    public static void makeDatabaseDoc(String i_DataSourceXID ,String i_Version ,String i_SaveDir ,String i_SaveShortName)
    {
        makeDatabaseDoc(i_DataSourceXID ,i_Version ,EngineFileType.HTML ,i_SaveDir ,i_SaveShortName);
        makeDatabaseDoc(i_DataSourceXID ,i_Version ,EngineFileType.WORD ,i_SaveDir ,i_SaveShortName);
        makeDatabaseDoc(i_DataSourceXID ,i_Version ,EngineFileType.MD   ,i_SaveDir ,i_SaveShortName);
    }
    
    
    
    /**
     * 数据库设计文档的生成
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-05
     * @version     v1.0
     *
     * @param i_DataSourceXID  数据源XID
     * @param i_Version        文档版本号：如 1.0.0
     * @param i_SaveType       文档格式
     * @param i_SaveDir        保存路径
     * @param i_SaveShortName  保存文件名称，不包含扩展名
     */
    public static void makeDatabaseDoc(String i_DataSourceXID ,String i_Version ,EngineFileType i_SaveType ,String i_SaveDir ,String i_SaveShortName)
    {
        com.alibaba.druid.pool.DruidDataSource v_DB = (com.alibaba.druid.pool.DruidDataSource) XJava.getObject(i_DataSourceXID);
        
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(v_DB.getDriverClassName());
        hikariConfig.setJdbcUrl(v_DB.getUrl());
        hikariConfig.setUsername(v_DB.getUsername());
        hikariConfig.setPassword(v_DB.getPassword());
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        
        //生成配置
        EngineConfig engineConfig = EngineConfig.builder()
              //生成文件路径
              .fileOutputDir(i_SaveDir)
              //打开目录
              .openOutputDir(true)
              //文件类型
              .fileType(i_SaveType)
              //生成模板实现
              .produceType(EngineTemplateType.freemarker)
              //自定义文件名称
              .fileName(i_SaveShortName).build();

        //忽略表
        ArrayList<String> ignoreTableName = new ArrayList<>();
        ignoreTableName.add("test_user");
        ignoreTableName.add("test_group");
        //忽略表前缀
        ArrayList<String> ignorePrefix = new ArrayList<>();
        ignorePrefix.add("TUpgrade");
        //忽略表后缀
        ArrayList<String> ignoreSuffix = new ArrayList<>();
        ignoreSuffix.add("_test");
        
        ProcessConfig processConfig = ProcessConfig.builder()
              //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
              //根据名称指定表生成
              .designatedTableName(new ArrayList<>())
              //根据表前缀生成
              .designatedTablePrefix(new ArrayList<>())
              //根据表后缀生成
              .designatedTableSuffix(new ArrayList<>())
              //忽略表名
              .ignoreTableName(ignoreTableName)
              //忽略表前缀
              .ignoreTablePrefix(ignorePrefix)
              //忽略表后缀
              .ignoreTableSuffix(ignoreSuffix).build();
        
        //配置
        Configuration config = Configuration.builder()
              //版本
              .version(i_Version)
              //描述
              .description("数据库设计")
              //数据源
              .dataSource(dataSource)
              //生成配置
              .engineConfig(engineConfig)
              //生成配置
              .produceConfig(processConfig)
              .build();
        
        //执行生成
        new DocumentationExecute(config).execute();
    }
    
}
