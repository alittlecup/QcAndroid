package cn.qingchengfit.checkout.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;

public final class RouteUtil {
  public static void routeTo(Context context, String model, String path, Bundle bd) {
    String uri = model + path;
    try {
      uri = AppUtils.getCurAppSchema(context) + "://" + model + path;
      Intent to = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
      to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      if (bd != null) {
        to.putExtras(bd);
      }
      context.startActivity(to);
    } catch (Exception e) {
      LogUtil.e("找不到模块去处理" + uri);
      CrashUtils.sendCrash(e);
    }
  }
}
