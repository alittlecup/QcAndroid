package cn.qingchengfit.saasbase;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.bill.view.BillDetailParams;
import cn.qingchengfit.saasbase.routers.SaasbaseRouterCenter;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.R2;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

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
 * Created by Paper on 2017/8/15.
 */

public class SaasContainerActivity extends BaseActivity implements HasSupportFragmentInjector {
  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
  @Inject SaasbaseRouterCenter routerCenter;
  @BindView(R2.id.web_frag_layout) FrameLayout webFragLayout;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base_frag);
    ButterKnife.bind(this);
    webFragLayout.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    onNewIntent(getIntent());
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getExtras());
  }

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 100) {

        if (data.getBundleExtra("data") == null) {
        } else {
          Bundle args = data.getBundleExtra("data");
          Long amount = args.getLong("amount", -1);
          String merorderId = args.getString("merOrderId");
          String payStatus = args.getString("payStatus");
          String title = args.getString("title");
          String operator = args.getString("operator");
          String packageName = args.getString("packageName");
          int payType = args.getInt("payType", 0);
          String tradeFlowId = args.getString("tradeFlowId");                // 交易流水号
          String dealTime = args.getString("dealTime");               // 交易时间
          LogUtil.d("PosPay", "amount:"
              + amount
              + "|merorderId:"
              + merorderId
              + "|title:"
              + title
              + "|operator:"
              + operator
              + "|packageName:"
              + packageName
              + "|payType:"
              + payType
              + "|tradeFlowId:"
              + tradeFlowId
              + "|dealTime:"
              + dealTime
              + "|payStatus:"
              + payStatus);
          onPayDone(merorderId);
        }
      }
    } else {

    }
  }

  protected void onPayDone(String orderNo) {
    routeTo("bill", "/pay/done/", new BillDetailParams().orderNo(orderNo).build());
  }

  protected void routeTo(String model, String path, Bundle bd) {
    String uri = model + path;
    try {
      uri = AppUtils.getCurAppSchema(this) + "://" + model + path;
      Intent to = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
      to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      if (bd != null) {
        to.putExtras(bd);
      }
      startActivity(to);
      finish();
    } catch (Exception e) {
      LogUtil.e("找不到模块去处理" + uri);
      CrashUtils.sendCrash(e);
    }
  }

  @Override public int getFragId() {
    return R.id.web_frag_layout;
  }
}
