package cn.qingchengfit.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.BuildConfig;
import cn.qingchengfit.staffkit.views.statement.ContainerActivity;
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
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override public void run() {
                        mContext.startActivity(new Intent(mContext, ContainerActivity.class));
                    }
                });
                LogUtils.e("错误已处理");
            } catch (Exception e) {
                Log.e(TAG, "麻蛋 新的error : ", e);
            }

            ////退出程序
            //if (App.gCanReload) {
            //    Intent intent = new Intent(mContext.getApplicationContext(), SplashActivity.class);
            //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            //    intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //    PendingIntent restartIntent = PendingIntent.getActivity(
            //            mContext.getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            //    AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            //    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
            //}
            //((App) mContext).finishActivity();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        if (!BuildConfig.DEBUG) MobclickAgent.reportError(App.context, ex);
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
