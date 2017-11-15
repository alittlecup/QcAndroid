package cn.qingchengfit.pos.exchange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.RongPrinter;
import cn.qingchengfit.pos.exchange.beans.ExchangeWrapper;
import cn.qingchengfit.pos.login.LoginActivity;
import cn.qingchengfit.pos.net.PosApi;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.TipTextDialogFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.bumptech.glide.Glide;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/10/17.
 */
@Leaf(module = "exchange",path = "/home/")
public class ExchangeFragment extends BaseFragment {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.img_exchange_head) ImageView imgExchangeHead;
  @BindView(R.id.tv_exchange_name) TextView tvExchangeName;
  @BindView(R.id.tv_exchange_now_position) TextView tvExchangeNowPosition;
  @BindView(R.id.tv_exchange_start) TextView tvExchangeStart;
  @BindView(R.id.tv_exchange_end) TextView tvExchangeEnd;
  @BindView(R.id.tv_exchange_business) TextView tvExchangeBusiness;
  @BindView(R.id.tv_exchange_money) TextView tvExchangeMoney;
  private Subscription sb;
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository restRepository;
  @Inject LoginStatus loginStatus;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_exchange, container, false);
    unbinder = ButterKnife.bind(this, view);
    setToolbar(toolbar);
    setData();
    return view;
  }

  private void setToolbar(Toolbar toolbar){
    initToolbar(toolbar);
    toolbarTitle.setText("交班");
  }

  private void setData(){
    Glide.with(getContext())
        .load(loginStatus.getLoginUser().avatar)
        .asBitmap()
        .placeholder(loginStatus.getLoginUser().gender == 1 ? R.drawable.ic_default_staff_women_head
            : R.drawable.ic_default_staff_man_head)
        .into(new CircleImgWrapper(imgExchangeHead, getContext()));
    tvExchangeName.setText(getResources().getString(R.string.exchange_staff_info,
        loginStatus.getLoginUser().getUsername(), loginStatus.getLoginUser().phone));

    sb = restRepository.createPostApi(PosApi.class)
        .qcGetExchange(gymWrapper.id())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<QcDataResponse<ExchangeWrapper>>() {
          @Override
          public void call(final QcDataResponse<ExchangeWrapper> exchangeWrapperQcDataResponse) {
            if (ResponseConstant.checkSuccess(exchangeWrapperQcDataResponse)) {
              getActivity().runOnUiThread(new Runnable() {
                @Override public void run() {

                  tvExchangeStart.setText(DateUtils.replaceTFromServer(
                      exchangeWrapperQcDataResponse.data.exchange.start));
                  tvExchangeEnd.setText(DateUtils.replaceTFromServer(
                      exchangeWrapperQcDataResponse.data.exchange.end));
                  tvExchangeBusiness.setText(
                      String.valueOf(exchangeWrapperQcDataResponse.data.exchange.bills_numbers) + "笔");
                  tvExchangeMoney.setText(
                      exchangeWrapperQcDataResponse.data.exchange.bills_price / 100 + "元");
                }
              });
            } else {
              onShowError(exchangeWrapperQcDataResponse.getMsg());
            }
          }
        }, new NetWorkThrowable());
  }

  @OnClick(R.id.btn_exchange)
  public void onExchange(){
    TipTextDialogFragment dialogFragment = TipTextDialogFragment.newInstance(getResources().getString(R.string.tips_exchange),
        getResources().getString(R.string.confirm),
        getResources().getString(R.string.exchange_confirm));
    dialogFragment.setOnConfirmListener(new TipTextDialogFragment.OnConfirmListener() {
      @Override public void onConfirm() {

        RongPrinter.Builder rongPrinter = new RongPrinter.Builder();
        rongPrinter.title("交班单（"+loginStatus.getLoginUser().getUsername()+")");
        rongPrinter.first("开始时间: ",tvExchangeStart.getText().toString());
        rongPrinter.first("结束时间: ",tvExchangeEnd.getText().toString());
        rongPrinter.first("共交易  : ",tvExchangeBusiness.getText().toString());
        rongPrinter.first("实收    : ",tvExchangeMoney.getText().toString());
        startActivityForResult(rongPrinter.build().print(getContext()),101);
      }
    });
    dialogFragment.show(getFragmentManager(), null );
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 101) {
        logOut();
      }
    }
  }

  private void logOut(){
    PreferenceUtils.setPrefString(getContext(), Configs.PREFER_SESSION, "");
    PreferenceUtils.setPrefString(getContext(), Configs.PREFER_SESSION_ID, "");
    Intent intent = new Intent(getActivity(), LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    getContext().startActivity(intent);
    getActivity().finish();
  }
}
