package com.tfy.framework.common.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class SystemConfigConst {

    /*基础包名*/
    public static final String BASE_PACKAGE;

    static {
        String packageName = SystemConfigConst.class.getPackage().getName();
        int lastCutIndex = StringUtils.lastOrdinalIndexOf(packageName, ".", 2);
        BASE_PACKAGE = packageName.substring(0, lastCutIndex);
    }

    /**
     * 数据库配置 FrameworkCodeGenerator 代码生成器使用
     */
    @AllArgsConstructor
    @Getter
    public enum DatasourceEnum {
        /*reading 库*/
        READING("reading", "jdbc:mysql://ip:port", "", "", "tb_"),
        ACTIVITY("activity", "jdbc:mysql://ip:port", "", "", "tb_");

        /*数据库名称*/
        private String databaseName;
        /*数据库连接地址*/
        private String jdbcUrl;
        /*账号*/
        private String userName;
        /*密码*/
        private String password;
        /*表前缀*/
        private String tablePrefix;
    }
}
