package com.yyxnb.common_base.config;


public class BaseAPI {

    // 切换请求url  @Headers()
    //apiopen
    public static final String HEADER_APIOPEN = UrlInterceptor.URL_PREFIX + BaseAPI.URL_APIOPEN;
    // 玩安卓
    public static final String HEADER_WAN = UrlInterceptor.URL_PREFIX + BaseAPI.URL_WAN_ANDROID;

    //====\key

    //免费开放接口API
    public final static String URL_APIOPEN = "https://www.apiopen.top/";
    public final static String URL_MOCKY = "http://www.mocky.io/";
    public final static String URL_WAN_ANDROID = "https://www.wanandroid.com/";

}
