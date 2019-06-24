package com.util;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Created by dell on 2018/4/24.
 */

public class LogUtils {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int level = VERBOSE;

    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Logger.t(tag).v(tag,msg);
        }
    }

    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Logger.t(tag).d(tag,msg);
        }
    }

    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Logger.t(tag).i(tag,msg);
        }
    }

    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Logger.t(tag).w(tag,msg);
        }
    }

    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            Logger.t(tag).e(msg);
        }
    }









}
