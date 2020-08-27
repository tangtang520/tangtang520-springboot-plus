<p align="center">
  <a href="https://github.com/geekidea/spring-boot-plus">
   <img alt="spring-boot-plus logo" src="https://springboot.plus/img/logo.png">
  </a>
</p>
<p align="center">
  Everyone can develop projects independently, quickly and efficiently！
</p>

<p align="center">  
  <a href="https://github.com/geekidea/spring-boot-plus/">
    <img alt="spring-boot-plus version" src="https://img.shields.io/badge/spring--boot--plus-2.0-blue">
  </a>
  <a href="https://github.com/spring-projects/spring-boot">
    <img alt="spring boot version" src="https://img.shields.io/badge/spring%20boot-2.2.5.RELEASE-brightgreen">
  </a>
  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
</p>



**springboot-plus是一套集成spring boot常用开发组件的后台快速开发框架**
<!-- more -->

> springboot-plus 是易于使用，快速，高效，功能丰富，开源的spring boot 脚手架

### 主要特性
- 全局上下文唯一流水号，以及日志中自动存储当前流水号，快速定位问题
- 集成spring boot 常用开发组件集、公共配置、AOP日志等
- 集成mybatis plus快速dao操作
- 快速生成后台代码: entity/controller/service/mapper/xml
- 集成Swagger/Knife4j，可自动生成api文档
- 集成jwt、签名机制，通过注解灵活鉴权
- 集成Redis缓存
- 集成druid连接池，JDBC性能和慢查询检测
- 全局错误处理

## 快速开始
### 克隆 springboot-plus
```bash
git clone https://github.com/tangtang520/tangtang520-springboot-plus.git
cd springboot-plus
```

## 5分钟完成增删改查

### 1. 创建数据库表
```sql
-- ----------------------------
-- Table structure for foo_bar
-- ----------------------------
DROP TABLE IF EXISTS `foo_bar`;
CREATE TABLE `foo_bar`
(
    `id`            bigint(20)  NOT NULL COMMENT '主键',
    `name`          varchar(20) NOT NULL COMMENT '名称',
    `foo`           varchar(20)          DEFAULT NULL COMMENT 'Foo',
    `bar`           varchar(20) NOT NULL COMMENT 'Bar',
    `remark`        varchar(200)         DEFAULT NULL COMMENT '备注',
    `state`         int(11)     NOT NULL DEFAULT '1' COMMENT '状态，0：禁用，1：启用',
    `version`       int(11)     NOT NULL DEFAULT '0' COMMENT '版本',
    `create_time`   timestamp   NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp   NULL     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='FooBar';

-- ----------------------------
-- Records of foo_bar
-- ----------------------------
INSERT INTO foo_bar (id, name, foo, bar, remark, state, version, create_time, update_time) 
    VALUES (1, 'FooBar', 'foo', 'bar', 'remark...', 1, 0, '2019-11-01 14:05:14', null);
INSERT INTO foo_bar (id, name, foo, bar, remark, state, version, create_time, update_time) 
    VALUES (2, 'HelloWorld', 'hello', 'world', null, 1, 0, '2019-11-01 14:05:14', null);

```

### 2.使用代码生成器生成增删改查代码
> 代码生成入口类

```text
FrameworkCodeGenerator类执行main函数
```
> **支持表的修改不影响已有的mapper文件**
> 
> **支持多表同时生成entity/controller/service/mapper/xml**
> 
> **支持常用的增删改查分页，支持极其丰富的条件构造器**
> 
> **真正的0配置，免去了 application.yml 或者数据源连接池配置的重复性工作**
> 
> **多数据源也做到了0配置**



```java
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
}
```

## 全局上下文流水号


### 日志文件自动记录全局流水号
```java
log.info("params . name:{}", name);

2020-08-26 20:28:58.696 |  INFO | 3523 |   XNIO-1 task-1 | c.i.u.controller.outer.TestController   :57   | SERIAL_NUMBER=1298598310491193344 | params . name:汤丰源
```

### 返回内容自动返回全局流水号
```json
{
  "code": 0,
  "data": true,
  "serialNumber": "1298598310491193344"
}
```

### 错误日志全局处理自动记录接口地址，全局流水号，堆栈
```java
2020-08-26 20:33:38.985 | ERROR | 3559 |   XNIO-1 task-1 | c.i.u.c.e.GlobalExceptionHandler        :49   | SERIAL_NUMBER=1298599486083301376 | exceptionHandle.Exception url:/test/test, 

java.lang.ArithmeticException: / by zero
	at com.iciba.user.controller.outer.TestController.test1(TestController.java:57)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:106)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:888)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:793)
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:503)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:590)
```

## redis key统一配置和使用

### 配置 
> RedisKeyConst类分模块配置

```java
    @AllArgsConstructor
    @Getter
    public enum ExampleEnum implements BaseEnum {
        /* 格式: key -> STR:USER:{userId},  value -> JSONString */
        STR_USER_INFO("STR:USER:%s", TimeUnit.SECONDS, 24 * 60 * 60);
        private String redisKeyFormat;
        private TimeUnit timeUnit;
        private Integer timeValue;
    }
```
### 使用
```java
/* 获取指定的redis key相关信息，并且复制占位符*/
KeyVo userInfoKeyVo = ExampleEnum.STR_USER_INFO.keyVo(userId);

String userInfoRedisKey = userInfoKeyVo.getRedisKey();

TimeUnit userInfoRedisKeyTimeUnit = userInfoKeyVo.getTimeUnit();

Integer userInfoRedisKeyTimeValue = userInfoKeyVo.getTimeValue();
```




## 后续计划添加功能
*  后续会使用[HikariCP](https://github.com/brettwooldridge/HikariCP)来替代druid
* 增加分库分表策略以及主从策略的代码生成器，推荐大家使用[shardingsphere](https://github.com/apache/shardingsphere) 
* jwt验证，签名验证实现自定义注解配置
* 增加限流策略配置
