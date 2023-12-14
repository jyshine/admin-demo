package com.demo.admindemo.config.web;

public class Path {
    /** API Prefix */
    public static final String API							          = "/api";
    public static final String VERSION							      = "/v1";
    public static final String AUTH							          = "/auth";


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | API
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/


    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Auth
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    public static final String SIGN_IN                                 = AUTH + "/signIn";
    public static final String SIGN_OUT                                = AUTH + "/signOut";
    public static final String REFRESHTOKEN                            = AUTH + "/refreshToken";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | GUEST
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    public static final String GUEST                                   = API + VERSION + "/guest";

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | USER
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    public static final String USER_DETAIL                             = API + VERSION + "/user/detail";

}
