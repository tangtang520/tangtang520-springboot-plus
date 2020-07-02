package com.tfy.framework.configuration.tomcat;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(TomcatConfigProperties.PREFIX)
public class TomcatConfigProperties {
    static final String PREFIX = "optimize";

    private Tomcat tomcat;

    /**
     * Tomcat配置文件
     */
    @Setter
    @Getter
    public static class Tomcat {
        /**
         * 连接超时，单位ms
         */
        private Integer connectionTimeout = 20000;

        /**
         * 接收连接线程数量，参考cpu核数
         */
        private Integer acceptorThreadCount = 2;

        /**
         * 最小监听线程
         */
        private Integer minSpareThreads = 5;

        /**
         * 最大监听线程
         * 同时相应客户请求最大值
         */
        private Integer maxSpareThreads = 200;

        /**
         * 最大排队数
         */
        private Integer acceptCount = 200;

        /**
         * 最大连接数
         */
        private Integer maxConnections = 800;

        /**
         * 最大线程数
         */
        private Integer maxThreads = 500;

        /**
         * 运行模式
         */
        private String protocol = "org.apache.coyote.http11.Http11Nio2Protocol";
    }
}
