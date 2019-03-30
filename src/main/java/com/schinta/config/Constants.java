package com.schinta.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";

    // 测试用常数，最后需要注释掉
//    public static final String WX_TEST_OPENID = "oPhnp5scZ4Mf0b9hObV6vj7FqfeA";
//    public static final String WX_APPID = "wx87d2791c2c3a3ded"; // 测试号
    public static final String WX_APPID = "wxba1e1e0cfc55f4a9"; // 小伊配对中心

    // 每个用户默认的推送限制，需要改为从配置文件中获取
    public static final int PUSH_LIMIT = 1;

    private Constants() {
    }
}
