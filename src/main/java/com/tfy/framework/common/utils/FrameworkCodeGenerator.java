package com.tfy.framework.common.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.tfy.framework.common.consts.SystemConfigConst;
import com.tfy.framework.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器
 */
@Slf4j
public class FrameworkCodeGenerator {
    public static void main(String[] args) throws IOException {

        /*是否仅仅生成entity*/
        boolean onlyEntity = false;

        /*是否是默认库*/
        boolean isPrimary = true;

        /*是否生成controller代码*/
        boolean isGenerateController = true;

        /*链接的数据库*/
        SystemConfigConst.DatasourceEnum datasourceEnum = SystemConfigConst.DatasourceEnum.READING;

        /*生成的表结构*/
        String[] tables = new String[]{"tb_test"};

        /*代码生成*/
        generateByTables(onlyEntity, isPrimary, datasourceEnum, isGenerateController, tables);
    }


    private static void generateByTables(boolean onlyEntity, boolean isPrimary, SystemConfigConst.DatasourceEnum databaseConst, boolean isGenerateController, String... tableNames) throws IOException {
        checkContinue(onlyEntity);
        //user -> UserService, 设置成true: user -> IUserService
        boolean isServiceI = true;

        String packageName = SystemConfigConst.BASE_PACKAGE;

        GlobalConfig config = new GlobalConfig();
        String dbUrl = databaseConst.getJdbcUrl();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(databaseConst.getUserName())
                .setPassword(databaseConst.getPassword())
                .setDriverName("com.mysql.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setTablePrefix(databaseConst.getTablePrefix())
                .setEntityColumnConstant(true)
                .setCapitalMode(true)
                .setEntityLombokModel(false)
                .setNaming(NamingStrategy.underline_to_camel)
                .setRestControllerStyle(true)
                .setControllerMappingHyphenStyle(true)
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组
        strategyConfig.setSuperControllerClass(BaseController.class);
        config.setActiveRecord(false)
                .setEnableCache(false)
//                .setSwagger2(true)  //设置swagger
                .setEntityName("%sEntity")
                .setAuthor("tfy")
                .setOutputDir("src/main/java")
                .setActiveRecord(true)
                .setFileOverride(true);
        if (!isServiceI) {
            config.setServiceName("%sService");
        }
        PackageConfig packageConfig = new PackageConfig()
                .setParent(packageName)
                .setEntity("entity." + databaseConst.getDatabaseName())
                .setService("service." + databaseConst.getDatabaseName())
                .setServiceImpl("service." + databaseConst.getDatabaseName() + ".impl")
                .setMapper("dao." + databaseConst.getDatabaseName());
//              .setXml("dao.impl." + databaseConst.getDatabaseName());

        AutoGenerator mpg = new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig);

        TemplateConfig tc = new TemplateConfig();
        tc.setEntity("/template/entity.java.vm");
        tc.setController("/template/controller.java.vm");
//      tc.setController(null);
        if(!isGenerateController){
            tc.setController(null);
        }
        tc.setXml(null);
        if (onlyEntity) {
            tc.setController(null);
            tc.setMapper(null);
            tc.setXml(null);
            tc.setService(null);
            tc.setServiceImpl(null);
        }

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        if (!onlyEntity) {
            List<FileOutConfig> focList = new ArrayList<>();
            focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    return "src/main/resources/mapper/" + databaseConst.getDatabaseName()
                            + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });
            cfg.setFileOutConfigList(focList);
        }
        mpg.setCfg(cfg);
        mpg.setTemplate(tc);
        mpg.execute();
        if (!onlyEntity) {
            saveDatabase(databaseConst, isPrimary);
            saveApplicationProperties(databaseConst);
        }
    }

    private static void saveDatabase(SystemConfigConst.DatasourceEnum datasourceEnum, boolean isPrimary) throws IOException {
        String packageName = SystemConfigConst.BASE_PACKAGE;
        String databaseName = datasourceEnum.getDatabaseName();
        String databaseNameCapitalize = StringUtils.capitalize(databaseName);
        String primaryImport = isPrimary ? "import org.springframework.context.annotation.Primary;\n" : "";
        String primaryAnnotation = isPrimary ? "    @Primary\n" : "";
        String className = databaseNameCapitalize + "DataSourceConfig";
        String content = "package ${PACKAGE_NAME}.configuration.datasource;\n" +
                "\n" +
                "import com.alibaba.druid.pool.DruidDataSource;\n" +
                "import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;\n" +
                "import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;\n" +
                "import org.apache.ibatis.plugin.Interceptor;\n" +
                "import org.apache.ibatis.session.SqlSessionFactory;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.mybatis.spring.annotation.MapperScan;\n" +
                "import org.springframework.beans.factory.annotation.Qualifier;\n" +
                "import org.springframework.beans.factory.annotation.Value;\n" + primaryImport +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.core.io.support.PathMatchingResourcePatternResolver;\n" +
                "import org.springframework.jdbc.datasource.DataSourceTransactionManager;\n" +
                "\n" +
                "import javax.sql.DataSource;\n" +
                "import java.sql.SQLException;\n" +
                "\n" +
                "\n" +
                "@Configuration\n" +
                "@MapperScan(basePackages = \"${MAPPER_PATH}\", sqlSessionFactoryRef = \"${SQL_SESSION_FACTORY}\")\n" +
                "public class ${CLASS_NAME} {\n" +
                "\n" +
                "    @Value(\"${${DATABASE_NAME}.datasource.url}\")\n" +
                "    private String url;\n" +
                "\n" +
                "    @Value(\"${${DATABASE_NAME}.datasource.username}\")\n" +
                "    private String user;\n" +
                "\n" +
                "    @Value(\"${${DATABASE_NAME}.datasource.password}\")\n" +
                "    private String password;\n" +
                "\n" +
                "    @Value(\"${${DATABASE_NAME}.datasource.driverClassName}\")\n" +
                "    private String driverClass;\n" +
                "\n" +
                "    @Autowired\n" +
                "    private PaginationInterceptor paginationInterceptor;\n" +
                "\n" +
                "    @Bean(name = \"${DATA_SOURCE_NAME}\")\n" + primaryAnnotation +
                "    public DataSource dataSource() throws SQLException {\n" +
                "        return mybatisDataSource();\n" +
                "    }\n" +
                "\n" +
                "    private DataSource mybatisDataSource() throws SQLException {\n" +
                "\n" +
                "        DruidDataSource dataSource = new DruidDataSource();\n" +
                "        dataSource.setDriverClassName(driverClass);\n" +
                "        dataSource.setUrl(url);\n" +
                "        dataSource.setUsername(user);\n" +
                "        dataSource.setPassword(password);\n" +
                "\n" +
                "        /* 配置初始化大小、最小、最大 */\n" +
                "        dataSource.setInitialSize(1);\n" +
                "        dataSource.setMinIdle(1);\n" +
                "        dataSource.setMaxActive(20);\n" +
                "\n" +
                "        /* 配置获取连接等待超时的时间 */\n" +
                "        dataSource.setMaxWait(60000);\n" +
                "\n" +
                "        /* 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */\n" +
                "        dataSource.setTimeBetweenEvictionRunsMillis(60000);\n" +
                "\n" +
                "        /* 配置一个连接在池中最小生存的时间，单位是毫秒 */\n" +
                "        dataSource.setMinEvictableIdleTimeMillis(300000);\n" +
                "\n" +
                "//        dataSource.setValidationQuery(\"SELECT 'x'\");\n" +
                "        dataSource.setTestWhileIdle(true);\n" +
                "        dataSource.setTestOnBorrow(false);\n" +
                "        dataSource.setTestOnReturn(false);\n" +
                "\n" +
                "        /* 打开PSCache，并且指定每个连接上PSCache的大小。\n" +
                "           如果用Oracle，则把poolPreparedStatements配置为true，\n" +
                "           mysql可以配置为false。分库分表较多的数据库，建议配置为false */\n" +
                "        dataSource.setPoolPreparedStatements(false);\n" +
                "        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);\n" +
                "\n" +
                "        /* 配置监控统计拦截的filters */\n" +
                "        dataSource.setFilters(\"stat,wall\");\n" +
                "\n" +
                "        return dataSource;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    @Bean(\"${TRANSACTION_MANAGE}\")\n" + primaryAnnotation +
                "    public DataSourceTransactionManager transactionManager(@Qualifier(\"${DATA_SOURCE_NAME}\") DataSource dataSource) {\n" +
                "        return new DataSourceTransactionManager(dataSource);\n" +
                "    }\n" +
                "\n" +
                "    @Bean(name = \"${SQL_SESSION_FACTORY}\")\n" + primaryAnnotation +
                "    public SqlSessionFactory sqlSessionFactory(@Qualifier(\"${DATA_SOURCE_NAME}\") DataSource dataSource)\n" +
                "            throws Exception {\n" +
                "        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();\n" +
                "        sessionFactory.setDataSource(dataSource);\n" +
                "        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()\n" +
                "                .getResources(\"${MAPPER_XML_PATH}\"));\n" +
                "        sessionFactory.setPlugins(new Interceptor[]{paginationInterceptor});\n" +
                "        return sessionFactory.getObject();\n" +
                "    }\n" +
                "}\n";
        String mapperPath = packageName + ".dao." + databaseName;
        String mapperXmlPath = "classpath*:mapper/" + databaseName + "/*.xml";
        String dataSourceName = databaseName + "DataSource";
        String transactionManage = databaseName + "TransactionManager";
        String sqlSessionFactory = databaseName + "SqlSessionFactory";
        content = StringUtils.replace(content, "${CLASS_NAME}", className);
        content = StringUtils.replace(content, "${PACKAGE_NAME}", packageName);
        content = StringUtils.replace(content, "${DATABASE_NAME}", databaseName);
        content = StringUtils.replace(content, "${MAPPER_PATH}", mapperPath);
        content = StringUtils.replace(content, "${SQL_SESSION_FACTORY}", sqlSessionFactory);
        content = StringUtils.replace(content, "${DATA_SOURCE_NAME}", dataSourceName);
        content = StringUtils.replace(content, "${SQL_SESSION_FACTORY}", sqlSessionFactory);
        content = StringUtils.replace(content, "${TRANSACTION_MANAGE}", transactionManage);
        content = StringUtils.replace(content, "${MAPPER_XML_PATH}", mapperXmlPath);
        String packageReplace = StringUtils.replace(packageName, ".", "/");
        String path = "src/main/java/" + packageReplace + "/configuration/datasource/" + className + ".java";
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        FileUtils.writeStringToFile(file, content, "utf-8");
    }

    private static void saveApplicationProperties(SystemConfigConst.DatasourceEnum datasourceEnum) throws IOException {
        String content = "\n\n" +
                "# ${DATABASE_NAME} 数据库\n" +
                "${DATABASE_NAME}.datasource.url=${URL}\n" +
                "${DATABASE_NAME}.datasource.username=${USER_NAME}\n" +
                "${DATABASE_NAME}.datasource.password=${PASSWORD}\n" +
                "${DATABASE_NAME}.datasource.driverClassName=com.mysql.jdbc.Driver";
        content = StringUtils.replace(content, "${URL}", datasourceEnum.getJdbcUrl());
        content = StringUtils.replace(content, "${DATABASE_NAME}", datasourceEnum.getDatabaseName());
        content = StringUtils.replace(content, "${USER_NAME}", datasourceEnum.getUserName());
        content = StringUtils.replace(content, "${PASSWORD}", datasourceEnum.getPassword());
        String path = "src/main/resources/application.properties";
        File file = new File(path);
        boolean contains = FileUtils.readFileToString(file, "utf-8").contains(datasourceEnum.getDatabaseName() + ".datasource.url");
        log.info("contains:{}", contains);
        if (contains) {
            return;
        }
        FileUtils.writeStringToFile(file, content, "utf-8", true);
    }

    private static void checkContinue(boolean onlyEntity) {
        if (onlyEntity) {
            return;
        }
        Object[] options = {"我想好后果了", "我有点后悔了"};
        int dialog = JOptionPane.showOptionDialog(null, "当前模式为全模块覆盖，会导致代码被覆盖", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (dialog == 0) {
            return;
        }
        System.exit(-1);
    }
}
