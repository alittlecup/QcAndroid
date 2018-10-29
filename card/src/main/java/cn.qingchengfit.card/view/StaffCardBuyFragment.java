package cn.qingchengfit.card.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.card.R;
import cn.qingchengfit.card.buy.CompletedBuyView;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.router.qc.IQcRouteCallback;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
import cn.qingchengfit.card.presenter.StaffCardBuyPresenter;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.AppUtils;
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
 * Created by fb on 2017/12/20.
 */

public class StaffCardBuyFragment extends CardBuyFragment implements CompletedBuyView {

  @Inject StaffCardBuyPresenter buyPresenter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = super.onCreateView(inflater, container, savedInstanceState);
    delegatePresenter(buyPresenter, this);
    return view;
  }

  @Override public void onBusinessOrder(JsonObject payBusinessResponse) {
    if (payMethod() < 6) {
      buyPresenter.cacluScore(realMoney(), StringUtils.List2Str(presenter.getChoseStuIds()));
    } else {
      JsonObject wrapper = new JsonObject();
      wrapper.addProperty("price", realMoney());
      wrapper.add("bean", payBusinessResponse);
      JsonObject info = new JsonObject();
      info.addProperty("moduleName", "card");
      info.addProperty("actionName", "/repay/newcard");
      info.addProperty("params", presenter.getRePayJson());
      wrapper.add("info", info);

      if (payMethod() == 7) {
        wrapper.addProperty("type", "WEIXIN_QRCODE");
      } else if (payMethod() == 12) {
        wrapper.addProperty("type", "ALIPAY_QRCODE");
      }
      QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/pay")
          .setContext(getContext())
          .addParam("data", new Gson().toJson(wrapper))).callAsync(callback);
    }
  }

  private IQcRouteCallback callback = qcResult -> {
    if (qcResult.isSuccess()) {
      paySuccess();
    } else {
      onFailed("充值失败");
    }
  };

  @Override public void onCivBindMenbersClicked() {
    String qcCallId = getActivity().getIntent().getStringExtra("qcCallId");
    if (TextUtils.isEmpty(qcCallId)) {
      super.onCivBindMenbersClicked();
    } else {
      QcRouteUtil.setRouteOptions(new RouteOptions("student").setActionName("/search/student/")
          .addParam("addAble",true)
          .addParam("studentIdList", presenter.getChoseStuIds())).call();
    }
  }

  @Override public void onSalers(List<Staff> salers) {

  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case 404:
          paySuccess();
          break;
      }
    } else {
      ToastUtils.show("充值失败");
    }
  }

  private void paySuccess() {
    buyPresenter.cacluScore(realMoney(), StringUtils.List2Str(presenter.getChoseStuIds()));
  }

  @Override public void onSuccess() {
    ToastUtils.showS("购卡成功");
    getActivity().setResult(Activity.RESULT_OK);
    getActivity().finish();
    String qcCallId = getActivity().getIntent().getStringExtra("qcCallId");
    if (TextUtils.isEmpty(qcCallId)) {
      routeTo(AppUtils.getRouterUri(getContext(), "card/list/home/"), null);
    } else {
      QC.sendQCResult(qcCallId, QCResult.success());
    }
  }

  @Override public void onFailed(String s) {
    getActivity().finish();
    String qcCallId = getActivity().getIntent().getExtras().getString("qcCallId");
    if (TextUtils.isEmpty(qcCallId)) {
      routeTo(AppUtils.getRouterUri(getContext(), "card/list/home/"), null);
    } else {
      QC.sendQCResult(qcCallId, QCResult.error(s));
    }
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
        .title("会员卡添加成功!")
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
