package cn.qingchengfit.checkout.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.recruit.item.ResumeTitleItem;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CheckoutComponent implements IComponent {

  public void setCheckoutModel(ICheckoutModel checkoutModel) {
    this.checkoutModel = checkoutModel;
  }

  ICheckoutModel checkoutModel;

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
        return true;
      case "reOrder":
        Map<String, Object> params = qc.getParams();
        String prices = (String) params.get("prices");
        String channel = (String) params.get("channel");
        Log.d("TAG", "onCall: " + params);

        Flowable.just(1).delay(200,TimeUnit.SECONDS).subscribe(new Consumer<Integer>() {
          @Override public void accept(Integer integer) throws Exception {
            Map<String,Object> params=new HashMap<>();
            params.put("orderNumber","123123123");
            params.put("pollingNumber","123123123");
            QC.sendQCResult(qc.getCallId(),QCResult.success(params));
          }
        });
        return true;
    }
    return false;
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
