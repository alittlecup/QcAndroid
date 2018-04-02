package com.qingchengfit.fitcoach.Utils;

import android.content.Context;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventCloseApp;
import com.baidu.android.pushservice.PushManager;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
import com.umeng.analytics.MobclickAgent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/8/17 2015.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override public void uncaughtException(Thread thread, Throwable ex) {
        if (!BuildConfig.DEBUG) MobclickAgent.reportError(App.AppContex, ex);
        RxBus.getBus().post(new EventCloseApp());
        PushManager.stopWork(mContext);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();
        //使用Toast来显示异常信息
        //        new Thread() {
        //            @Override
        //            public void run() {
        //                Looper.prepare();
        //                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
        //                Looper.loop();
        //            }
        //        }.start();
        //收集设备参数信息
        //        collectDeviceInfo(mContext);
        //保存日志文件
        //        RevenUtils.sendException("uncatch", ex.toString(), ex);
        MobclickAgent.reportError(App.AppContex, ex);
        return true;
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo(Context ctx) {

    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    //    private String saveCrashInfo2File(Throwable ex) {
    //
    //        StringBuffer sb = new StringBuffer();
    //        for (Map.Entry<String, String> entry : infos.entrySet()) {
    //            String key = entry.getKey();
    //            String value = entry.getValue();
    //            sb.append(key + "=" + value + "\n");
    //        }
    //
    //        Writer writer = new StringWriter();
    //        PrintWriter printWriter = new PrintWriter(writer);
    //        ex.printStackTrace(printWriter);
    //        Throwable cause = ex.getCause();
    //        while (cause != null) {
    //            cause.printStackTrace(printWriter);
    //            cause = cause.getCause();
    //        }
    //        printWriter.close();
    //        String result = writer.toString();
    //        sb.append(result);
    //        try {
    //            long timestamp = System.currentTimeMillis();
    //            String time = formatter.format(new Date());
    //            String fileName = "crash-" + time + "-" + timestamp + ".log";
    //            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    //                String path = "/sdcard/crash/";
    //                File dir = new File(path);
    //                if (!dir.exists()) {
    //                    dir.mkdirs();
    //                }
    //                FileOutputStream fos = new FileOutputStream(path + fileName);
    //                fos.write(sb.toString().getBytes());
    //                fos.close();
    //            }
    //            return fileName;
    //        } catch (Exception e) {
    //            Log.e(TAG, "an error occured while writing file...", e);
    //        }
    //        return null;
    //    }
}
