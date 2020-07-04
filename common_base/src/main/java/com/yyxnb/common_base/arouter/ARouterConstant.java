package com.yyxnb.common_base.arouter;


public class ARouterConstant {

    // == 服务
    public static final String LOGIN_SERVICE = "/login/login_service";

    
    //======login
    //跳转到登陆页面
    public static final String LOGIN_ACTIVITY = "/login/LoginActivity";
    public static final String LOGIN_FRAGMENT = "/login/LoginFragment";
    //跳转到注册页面
    public static final String REGISTER_ACTIVITY = "/login/RegisterActivity";

    //======user
    //跳转到用户页面
    public static final String USER_FRAGMENT = "/user/UserFragment";


    //==========video

    //跳转到视频页面
    public static final String VIDEO_VIDEO = "/video/VideoActivity";
    public static final String VIDEO_MAIN_FRAGMENT = "/video/VideoMainFragment";


    //=======message
    //消息列表
    public static final String MESSAGE_LIST_FRAGMENT = "/message/MessageListFragment";



    //========joke
    public static final String JOKE_MAIN = "/joke/JokeActivity";
    public static final String JOKE_MAIN_FRAGMENT = "/joke/JokeMainFragment";
    public static final String JOKE_HOME_FRAGMENT = "/joke/JokeHomeFragment";


    //==========其他

    //跳转到关于项目更多页面
    public static final String OTHER_ABOUT_ME = "/other/AboutMeActivity";
    //跳转到webView详情页面
    public static final String LIBRARY_WEB_VIEW = "/library/WebViewActivity";
    //跳转到意见反馈页面
    public static final String OTHER_FEEDBACK = "/other/MeFeedBackActivity";

}
