package com.demo.daniel.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {

    public static final Long REFRESH_EXPIRY_REMEMBER = 7L;

    public static final Long REFRESH_EXPIRY_NOT_REMEMBER = 1L;

    public static final String IP2REGION_DB_PATH = "classpath:ip2region.xdb";

    public static final String FILE_PREFIX = "my-upload/";

    public static final String USER_EXCEL = "user_excel";

    public static final String LOG_LOGIN_EXCEL = "log_Login_excel";

    public static final String DEFAULT_SHEET_NAME = "sheet1";

    public static final String BAR = "bar";
}
