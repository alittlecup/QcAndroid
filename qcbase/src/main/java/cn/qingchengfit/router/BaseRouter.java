package cn.qingchengfit.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.activity.WebActivity;
import java.util.HashMap;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/5/27.
 */

public class BaseRouter {

  public static final int RESULT_LOGIN = 201;
  HashMap<String, Class<?>> routers = new HashMap<>();

  public static void routerToWeb(String url, Context context) {
    try {
      WebActivity.startWeb(url, context);
    } catch (Exception e) {
      LogUtil.e(e.getMessage());
    }
  }

  public static void toChooseStaff(Fragment fragment, String json) {
    try {
      Intent toLogin = new Intent();
      toLogin.setPackage(fragment.getActivity().getPackageName());
      toLogin.setAction("cn.qingcheng.choose");
      toLogin.putExtra("to", 62);
      toLogin.putExtra("chatgym", json);
      fragment.startActivityForResult(toLogin, 11);
    } catch (Exception e) {
      LogUtil.e(e.getMessage());
    }
  }

  public static void toChooseGym(Fragment fragment) {
    try {
      Intent toLogin = new Intent();
      toLogin.setPackage(fragment.getActivity().getPackageName());
      toLogin.setAction("cn.qingcheng.choose");
      toLogin.putExtra("to", 80);
      fragment.startActivityForResult(toLogin, 11);
    } catch (Exception e) {
      LogUtil.e(e.getMessage());
    }
  }

  /**
   * 只用来选地址
   */
  public static void toChoose(Context context, int type, Gym gym) {
    try {
      Intent toLogin = new Intent();
      toLogin.setPackage(context.getPackageName());
      toLogin.setAction("cn.qingcheng.choose");
      toLogin.putExtra("to", 71);
      toLogin.putExtra("gym", gym);
      context.startActivity(toLogin);
    } catch (Exception e) {
      LogUtil.e(e.getMessage());
    }
  }

  public static void toLogin(Fragment fragment) {
    try {
      Intent toLogin = new Intent();
      toLogin.setPackage(fragment.getContext().getPackageName());
      toLogin.setAction("cn.qingcheng.login");
      toLogin.putExtra("web", true);
      fragment.startActivityForResult(toLogin, RESULT_LOGIN);
    } catch (Exception e) {
      LogUtil.e(e.getMessage());
    }
  }
  public static void toLogin(Activity fragment) {
    try {
      Intent toLogin = new Intent();
      toLogin.setPackage(fragment.getPackageName());
      toLogin.setAction("cn.qingcheng.login");
      toLogin.putExtra("web", true);
      fragment.startActivityForResult(toLogin, RESULT_LOGIN);
    } catch (Exception e) {
      LogUtil.e(e.getMessage());
    }
  }



  public void routerTo(String module, String action, Context context, int request) {
    if (routers.containsKey(module)) {
      Intent it = new Intent(context, routers.get(module));
      it.putExtra("action", action);
      if (context instanceof Activity) {
        ((Activity) context).startActivityForResult(it, request);
      }
    } else {
      //没有指定模块 todo
    }
  }

  public void routerTo(String module, Context context) {
    if (routers.containsKey(module)) {
      Intent it = new Intent(context, routers.get(module));
      it.putExtra("action", "recruit");
      context.startActivity(it);
    } else {
      //没有指定模块 todo
    }
  }

  public void routeTo(Uri uri,Context context){
    try {
      Intent to = new Intent(Intent.ACTION_VIEW,uri);
      to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(to);
    }catch (Exception e){
      LogUtil.e(e.getMessage());
    }
  }

  public void registeRouter(String module, Class<?> activitycalss) {
    routers.put(module, activitycalss);
  }

}
