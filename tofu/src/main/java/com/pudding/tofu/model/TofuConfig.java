package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/25 0025.
 * 邮箱：632716169@qq.com
 */

public class TofuConfig {



    private TofuConfig(){}

    private static int post_out_time = 10000;

    /**
     * debug
     */
    private static boolean debug = false;

    /**
     * 是否debug
     * @param isDebug
     */
    protected static void debug(boolean isDebug){
        debug = isDebug;
    }

    protected static boolean isDebug(){
        return debug;
    }

    public static void setPostConnectTimeout(int time){
        post_out_time = time;
    }

    public static int getConnectTimeout() {
        return post_out_time;
    }
}
