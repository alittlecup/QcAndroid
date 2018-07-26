package cn.qingchengfit.staffkit.card.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saasbase.cards.views.CardDetailParams;
import cn.qingchengfit.saasbase.cards.views.NewCardChargeFragment;
import cn.qingchengfit.saascommon.bean.CashierBean;
import cn.qingchengfit.saascommon.bean.CashierBeanWrapper;
import cn.qingchengfit.saascommon.bean.ScanRepayInfo;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.card.presenter.StaffCardBuyPresenter;
import cn.qingchengfit.staffkit.views.card.buy.CompletedBuyView;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Created by fb on 2017/12/21.
 */

public class StaffCardChargeFragment extends NewCardChargeFragment implements CompletedBuyView {

  @Inject StaffCardBuyPresenter buyPresenter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    delegatePresenter(buyPresenter, this);
    return view;
  }

  @Override public void onBusinessOrder(JsonObject payBusinessResponse) {
    Gson gson = new Gson();
    if (payMethod() < 6) {
      buyPresenter.cacluScore(realMoney(), StringUtils.List2Str(card.getUserIds()));
    } else {
      CashierBean
          cashierBean = gson.fromJson(payBusinessResponse.toString(), CashierBean.class);
      CashierBeanWrapper wrapper=new CashierBeanWrapper(cashierBean);
      wrapper.setPrices(realMoney());
      ScanRepayInfo info=new ScanRepayInfo();
      info.setModuleName("card");
      info.setActionName("/repay/balance");
      info.setParams(presenter.getBalanceInfo());
      wrapper.setInfo(info);
      if (payMethod() == 7) {
        QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/pay")
            .setContext(getContext())
            .addParam("type","微信")
            .addParam("orderData",wrapper)).call();
      } else if (payMethod() == 12) {
        QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/pay")
            .setContext(getContext())
            .addParam("type","支付宝")
            .addParam("orderData",wrapper)).call();
      }
    }
  }

  @Override public void onSalers(List<Staff> salers) {

  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case 404:
          if (data != null) {
            buyPresenter.cacluScore(realMoney(), StringUtils.List2Str(presenter.getChoseStuIds()));
          }
          break;
      }
    } else {
      ToastUtils.show("充值失败");
    }
  }

  @Override public void onSuccess() {
    ToastUtils.showS("续卡成功");
    getActivity().setResult(Activity.RESULT_OK);
    //getActivity().getSupportFragmentManager().popBackStack("", 1 );
    getActivity().finish();
    routeTo(AppUtils.getRouterUri(getContext(), "card/detail/"),
        new CardDetailParams().cardid(card.getId()).build());
  }

  @Override public void onFailed(String s) {
    getActivity().finish();
    routeTo(AppUtils.getRouterUri(getContext(), "card/detail/"),
        new CardDetailParams().cardid(card.getId()).build());
    ToastUtils.show(s);
  }

  @Override public void onWxPay(String url) {
    Intent toWeb = new Intent(getContext(), WebActivity.class);
    toWeb.putExtra("url", url);
    startActivityForResult(toWeb, 404);
    hideLoading();
  }

  @Override public void onScoreHint(String s) {
    new MaterialDialog.Builder(getContext()).autoDismiss(true)
        .canceledOnTouchOutside(false)
        .title("会员卡充值成功!")
        .positiveText(R.string.common_comfirm)
        .content(getString(R.string.caclu_score_hint, s))
        .cancelable(false)
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            onSuccess();
          }
        })
        .show();
  }
}
