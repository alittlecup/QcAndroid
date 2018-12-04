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
import cn.qingchengfit.card.bean.Coupon;
import cn.qingchengfit.card.buy.CompletedBuyView;
import cn.qingchengfit.card.event.ChooseCouponsEvent;
import cn.qingchengfit.card.presenter.StaffCardBuyPresenter;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.router.qc.IQcRouteCallback;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by fb on 2017/12/20.
 */

public class StaffCardBuyFragment extends CardBuyFragment implements CompletedBuyView {

  @Inject StaffCardBuyPresenter buyPresenter;
  CommonInputView mBindCoupons;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = super.onCreateView(inflater, container, savedInstanceState);
    mBindCoupons = view.findViewById(R.id.civ_bind_coupons);
    mBindCoupons.setVisibility(View.VISIBLE);
    mBindCoupons.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        routeToChooseCoupons();
      }
    });
    delegatePresenter(buyPresenter, this);
    return view;
  }

  private void routeToChooseCoupons() {
    if (canRouteToChooseCoupons()) {
      float price = presenter.getmChosenOption().getPrice();
      ArrayList<String> choseStuIds = presenter.getChoseStuIds();
      Bundle bundle = new Bundle();
      bundle.putFloat("price", price);
      bundle.putStringArrayList("user_ids", choseStuIds);
      if (coupon != null) {
        bundle.putParcelable("chooseCoupon", coupon);
      }
      routeTo("/choose/coupons", bundle);
    }
  }

  @Override public void initBus() {
    super.initBus();
    RxBusAdd(ChooseCouponsEvent.class).subscribe(new Action1<ChooseCouponsEvent>() {
      @Override public void call(ChooseCouponsEvent chooseCouponsEvent) {
        updateCoupons(chooseCouponsEvent.getCoupon());
      }
    });
  }

  private Coupon coupon;

  private void updateCoupons(Coupon coupon) {
    this.coupon = coupon;
    if (coupon == null) {
      mBindCoupons.setContent("");
      setPayMoney(presenter.getmChosenOption().getPrice());
    } else {
      mBindCoupons.setContent(coupon.getDescription());
      //setPayMoney();
    }
  }

  private boolean canRouteToChooseCoupons() {
    ArrayList<String> choseStuIds = presenter.getChoseStuIds();
    CardTplOption cardTplOption = presenter.getmChosenOption();
    boolean canRoute = false;
    if (cardTplOption == null) {
      ToastUtils.show("请至少选择一种会员卡规格");
    } else if (payMethod() != 12 && payMethod() != 7) {
      ToastUtils.show("优惠券功能只支持线上支付");
    } else if (choseStuIds == null || choseStuIds.isEmpty()) {
      ToastUtils.show("请选择会员");
    } else {
      canRoute = true;
    }
    return canRoute;
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
          .addParam("addAble", true)
          .addParam("selectedStudent", presenter.getQcStudentBeans())
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
