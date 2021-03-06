package cn.qingchengfit.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import cn.qingchengfit.widgets.R;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * Created by Paper on 15/8/4 2015.
 */
public class AppUtils {

  public static String getAppVer(Context c) {
    try {
      PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
      return packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      return "";
    }
  }

  public static int getAppVerCode(Context c) {
    try {
      PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
      return packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      return 0;
    }
  }

  public static void install(Context context, String filePath) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    File file = new File(filePath);
    Uri uri = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      uri = FileProvider.getUriForFile(context,
          context.getApplicationContext().getPackageName() + ".provider", file);
      context.grantUriPermission(context.getApplicationContext().getPackageName(), uri,
          Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
      i.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
      i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
      i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
        if (!hasInstallPermission) {
          Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          context.startActivity(intent);
        }
      }
    } else {
      uri = Uri.fromFile(file);
    }
    i.setDataAndType(uri, "application/vnd.android.package-archive");
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);
  }

  public static void showKeyboard(Context context, View v) {
    InputMethodManager imm =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    //        ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

  }

  public static void hideKeyboard(Activity activity) {
    InputMethodManager inputManager =
        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

    // check if no view has focus:
    View v = activity.getCurrentFocus();
    if (v == null) return;

    inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
  }

  public static void doSendSMSTo(Context context, String phoneNumber) {
    if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
      Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }
  }

  public static void doAppSettingPageTo(Context context) {
    Uri packageURI = Uri.parse("package:" + context.getPackageName());
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
    context.startActivity(intent);
  }

  public static void doCallPhoneTo(Context context, String phoneNumber) {
    if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
      Uri uri = Uri.parse(new StringBuilder().append("tel:").append(phoneNumber).toString());
      Intent dialntent = new Intent(Intent.ACTION_DIAL, uri);
      dialntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(dialntent);
    }
  }

  /**
   * 判断应用是否已经启动
   *
   * @param context 一个context
   * @param packageName 要判断应用的包名
   * @return boolean
   */
  public static boolean isAppAlive(Context context, String packageName) {
    ActivityManager activityManager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> processInfos =
        activityManager.getRunningAppProcesses();
    for (int i = 0; i < processInfos.size(); i++) {
      if (processInfos.get(i).processName.equals(packageName)) {
        Log.i("NotificationLaunch",
            String.format("the %s is running, isAppAlive return true", packageName));
        return true;
      }
    }
    Log.i("NotificationLaunch",
        String.format("the %s is not running, isAppAlive return false", packageName));
    return false;
  }

  public static void hideKeyboardFore(Context context) {
    InputMethodManager inputMethodManager =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
  }

  public static int getPrimaryColor(Context context) {
    TypedValue typedValue = new TypedValue();
    context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
    return typedValue.data;
  }

  public static String getManifestData(Context context, String dataname) {
    ApplicationInfo info = null;
    try {
      info = context.getPackageManager()
          .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    if (info != null) {
      return info.metaData.getString(dataname);
    }
    return "";
  }

  public static int getCurApp(Context context) {
    String packagename = context.getPackageName();
    if (packagename.contains("coach")) {
      return 0;
    } else if (packagename.contains("staff")) {
      return 1;
    } else {
      return 2;
    }
  }

  public static String getCurAppName(Context context) {
    switch (getCurApp(context)) {
      case 0:
        return "Trainer";
      case 1:
        return "Staff";
      case 2:
        return "Pos";
      default:
        return "Trainer";
    }
  }

  public static String getCurAppSchema(Context context) {
    switch (getCurApp(context)) {
      case 1:
        return "qcstaff";
      case 2:
        return "qcpos";
      default:
        return "qccoach";
    }
  }

  public static Uri getRouterUri(Context context, String path) {
    if (!path.startsWith("/")) path = "/" + path;
    return Uri.parse(getCurAppSchema(context) + ":/" + path);
  }

  /**
   * 根据性别返回默认头像
   */
  public static int getHeaderDrawable(int gender) {
    return gender == 1 ? R.drawable.ic_default_staff_women_head
        : R.drawable.ic_default_staff_man_head;
  }

  /**
   * 判断手机号码是否合法
   */
  public static boolean isMobiPhoneNum(String telNum) {
    if (TextUtils.isEmpty(telNum) || telNum.length() < 11) {
      return false;
    }
    String regex = "^1\\d{10}$";
    Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(telNum);
    return m.matches();
  }

  public boolean isAppOnForeground(Context context) {
    ActivityManager activityManager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
    if (tasksInfo.size() > 0) {

      // 应用程序位于堆栈的顶层
      if (TextUtils.equals(tasksInfo.get(0).topActivity.getPackageName(),
          context.getPackageName())) {
        return true;
      }
    }
    return false;
  }

  public static int StatusBarLightMode(Activity activity) {
    int result = 0;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.getWindow()
            .getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        result = 3;
      }
    }
    return result;
  }

  public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
    boolean result = false;
    if (window != null) {
      Class clazz = window.getClass();
      try {
        int darkModeFlag = 0;
        Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
        Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
        darkModeFlag = field.getInt(layoutParams);
        Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
        if (dark) {
          extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
        } else {
          extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
        }
        result = true;
      } catch (Exception e) {

      }
    }
    return result;
  }

  public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
    boolean result = false;
    if (window != null) {
      try {
        WindowManager.LayoutParams lp = window.getAttributes();
        Field darkFlag =
            WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
        Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
        darkFlag.setAccessible(true);
        meizuFlags.setAccessible(true);
        int bit = darkFlag.getInt(null);
        int value = meizuFlags.getInt(lp);
        if (dark) {
          value |= bit;
        } else {
          value &= ~bit;
        }
        meizuFlags.setInt(lp, value);
        window.setAttributes(lp);
        result = true;
      } catch (Exception e) {

      }
    }
    return result;
  }
}
