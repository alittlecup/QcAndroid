package cn.qingchengfit.utils;

import timber.log.Timber;

/**
 * Log统一管理类
 */
public class LogUtil {
    private static final String TAG = "hstag";
    //	public static boolean isDebug = BuildConfig.DEBUG;
    public static boolean isDebug = true;

    public static void init(){
      Timber.tag(TAG);
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        //if (isDebug)
        Timber.i(msg);
    }

    public static void d(String msg) {
        //if (isDebug) {
            Timber.d(msg);
        //}
    }

    public static void e(String msg) {
        //if (isDebug) {

        Timber.e(msg);
        //}
    }

    public static void v(String msg) {
        //if (isDebug)
            Timber.v(msg);
    }

    //下面是传入类名打印log
    public static void i(Class<?> _class, String msg) {
        //if (isDebug)
            Timber.i(_class.getName(), msg);
    }

    public static void d(Class<?> _class, String msg) {
        //if (isDebug)
            Timber.i(_class.getName(), msg);
    }

    public static void e(Class<?> _class, String msg) {
        //if (isDebug)
            Timber.i(_class.getName(), msg);
    }

    public static void v(Class<?> _class, String msg) {
        //if (isDebug)
            Timber.i(_class.getName(), msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        //if (isDebug)
            Timber.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        //if (isDebug)
            Timber.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        //if (isDebug)
            Timber.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        //if (isDebug)
            Timber.i(tag, msg);
    }
}
