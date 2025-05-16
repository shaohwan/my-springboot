package com.demo.daniel.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {

    public static final Long REFRESH_EXPIRY_REMEMBER = 7L;

    public static final Long REFRESH_EXPIRY_NOT_REMEMBER = 1L;

    public static final String IP2REGION_DB_PATH = "classpath:ip2region.xdb";

    public static final String S3_BUCKET_PREFIX = "test-upload/";
}
