package cn.qingchengfit.checkout.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;

public class CheckoutComponent implements IComponent {
  @Override public String getName() {
    return "checkout";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName) {
      case "/checkout/pay":
        Bundle bundle = new Bundle();
        String type = qc.getParamItem("type");
        String count = qc.getParamItem("count");
        bundle.putString("type", type);
        bundle.putString("money", count);
        bundle.putString("callId", qc.getCallId());
        Context context = qc.getContext();
        checkoutMoney(context, bundle);
        break;
    }
    return true;
  }

  private void checkoutMoney(Context context, Bundle bundle) {
    routeTo(context, "checkout", "/checkout/pay", bundle);
  }

  private void routeTo(Context context, String model, String path, Bundle bd) {
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
