package cn.qingchengfit.staffkit.card.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.card.presenter.StaffCardBuyPresenter;
import cn.qingchengfit.staffkit.views.card.buy.CompletedBuyView;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
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
    Gson gson = new Gson();
    if (payMethod() < 6) {
      buyPresenter.cacluScore(realMoney(), StringUtils.List2Str(presenter.getChoseStuIds()));
    } else {
      //QcResponsePayWx qcResponsePayWx = gson.fromJson(payBusinessResponse.toString(), QcResponsePayWx.class);
      onWxPay(String.valueOf(payBusinessResponse.get("url")));
    }
  }

  @Override public void onSalers(List<Staff> salers) {

  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK){
      switch (requestCode){
        case 404:
          if (data != null) {

            int ret = data.getIntExtra(IntentUtils.RESULT, -1);
            if (ret == 0) {
              //                        onSuccess();
              buyPresenter.cacluScore(realMoney(), StringUtils.List2Str(presenter.getChoseStuIds()));
            } else {
              ToastUtils.show("充值失败");
            }
          }
          break;
      }
    }
  }

  @Override public void onSuccess() {
    ToastUtils.showS("购卡成功");
    getActivity().setResult(Activity.RESULT_OK);
    getActivity().getSupportFragmentManager().popBackStack("", 1 );
    routeTo(AppUtils.getRouterUri(getContext(), "card/cardtpl/list/"), null);
  }

  @Override public void onFailed(String s) {
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
          @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            onSuccess();
          }
        })
        .show();
  }
}
