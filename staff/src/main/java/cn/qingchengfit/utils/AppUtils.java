//package cn.qingchengfit.utils;
//
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.Charset;
//import java.util.List;
//
//import cn.qingchengfit.staffkit.App;
//import cn.qingchengfit.inject.commpont.AppComponent;
//
///**
// * power by
// * <p>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p>
// * <p>
// * Created by Paper on 15/8/4 2015.
// */
//public class AppUtils {
//
//    public static AppComponent getComponent(Activity activity) {
//        return ((App) activity.getApplication()).getAppCompoent();
//    }
//
//
//    public static void openUrlByChrome(Context context, String url) {
//        Uri uri = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        context.startActivity(intent);
//    }
//
//    public static String getAppVer(Context c) {
//        try {
//            PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
//            return packageInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            return "";
//        }
//    }
//
//    public static int getAppVerCode(Context c) {
//        try {
//            PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
//            return packageInfo.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            return 0;
//        }
//    }
//
//
//    public static void install(Context context, String filePath) {
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);
//    }
//
//    public static void install(Context context, Uri uri) {
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setDataAndType(uri, "application/vnd.android.package-archive");
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);
//    }
//
//    public static void showKeyboard(Context context, View v) {
//        v.requestFocus();
//        if (context instanceof Activity){
//            InputMethodManager inputMethodManager = (InputMethodManager) ((Activity)context).getSystemService(Context.INPUT_METHOD_SERVICE);
////        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//            inputMethodManager.showSoftInput(v,InputMethodManager.SHOW_FORCED);
//        }else {
//            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
////        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//            inputMethodManager.showSoftInput(v,InputMethodManager.SHOW_FORCED);
//        }
//
//
//    }
//}
//
//    public static void hideKeyboard(Activity activity) {
//        InputMethodManager inputManager = (InputMethodManager) activity
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        // check if no view has focus:
//        View v = activity.getCurrentFocus();
//        if (v == null)
//            return;
//        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//    }
//    public static void hideKeyboardFore(Context context){
//        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
//
//    }
//
//
//    public boolean isAppOnForeground(Context context) {
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
//        if (tasksInfo.size() > 0) {
//
//            // 应用程序位于堆栈的顶层
//            if (TextUtils.equals(tasksInfo.get(0).topActivity
//                    .getPackageName(), context.getPackageName())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    /**
//     * 判断应用是否已经启动
//     *
//     * @param context     一个context
//     * @param packageName 要判断应用的包名
//     * @return boolean
//     */
//    public static boolean isAppAlive(Context context, String packageName) {
//        ActivityManager activityManager =
//                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> processInfos
//                = activityManager.getRunningAppProcesses();
//        for (int i = 0; i < processInfos.size(); i++) {
//            if (processInfos.get(i).processName.equals(packageName)) {
//                Log.i("NotificationLaunch",
//                        String.format("the %s is running, isAppAlive return true", packageName));
//                return true;
//            }
//        }
//        Log.i("NotificationLaunch",
//                String.format("the %s is not running, isAppAlive return false", packageName));
//        return false;
//    }
//
//
//
//
//        /**
//         * 静默安装
//         * @param apkPath
//         * @return
//         */
//    public static boolean installSilence(String apkPath){
//        boolean result = false;
//        DataOutputStream dataOutputStream = null;
//        BufferedReader errorStream = null;
//        try {
//            // 申请su权限
//            Process process = Runtime.getRuntime().exec("su");
//            dataOutputStream = new DataOutputStream(process.getOutputStream());
//            // 执行pm install命令
//            String command = "pm install -r " + apkPath + "\n";
//            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
//            dataOutputStream.flush();
//            dataOutputStream.writeBytes("exit\n");
//            dataOutputStream.flush();
//            process.waitFor();
//            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            String msg = "";
//            String line;
//            // 读取命令的执行结果
//            while ((line = errorStream.readLine()) != null) {
//                msg += line;
//            }
//            Log.d("TAG", "install msg is " + msg);
//            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
//            if (!msg.contains("Failure")) {
//                result = true;
//            }
//        } catch (Exception e) {
//            Log.e("TAG", e.getMessage(), e);
//        } finally {
//            try {
//                if (dataOutputStream != null) {
//                    dataOutputStream.close();
//                }
//                if (errorStream != null) {
//                    errorStream.close();
//                }
//            } catch (IOException e) {
//                Log.e("TAG", e.getMessage(), e);
//            }
//        }
//        return result;
//    }
//
//}
