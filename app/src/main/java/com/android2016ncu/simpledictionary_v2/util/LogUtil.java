package com.android2016ncu.simpledictionary_v2.util;

import android.util.Log;


/**
 * 日志打印工具
 * 可自由控制打印哪种类型的日志
 */
public class LogUtil {
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int NOTHING = 6;

    public  static int level = DEBUG;

    public static void v(String tag,String msg){
        if (level <= VERBOSE){
            Log.v(tag,msg);
        }
    }
    public static void d(String tag,String msg){
        if (level <= DEBUG){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag,String msg){
        if (level <= INFO){
            Log.i(tag,msg);
        }
    }
    public static void w(String tag,String msg){
        if (level <= WARN){
            Log.w(tag,msg);
        }
    }
    public static void e(String tag,String msg){
        if (level <= ERROR){
            Log.e(tag,msg);
        }
    }
}
