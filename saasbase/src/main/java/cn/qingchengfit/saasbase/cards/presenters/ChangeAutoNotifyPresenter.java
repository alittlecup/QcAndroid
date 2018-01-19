package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.cards.bean.NotifyIsOpen;
import cn.qingchengfit.saasbase.cards.network.body.CardBalanceNotifyBody;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangeAutoNotifyPresenter extends BasePresenter {

    public static final String STORE_CARD_REMINE = "value_card_remind_enable";
    public static final String STORE_CARD_REMINE_VALUE = "value_card_remind";

    public static final String SECOND_CARD_REMIND = "times_card_remind_enable";
    public static final String SECOND_CARD_REMIND_VALUE = "times_card_remind";

    public static final String TIME_CARD_REMINE_VALUE = "time_card_remind";
    public static final String TIME_CARD_REMINE = "time_card_remind_enable";
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject ICardModel iCardModel;
    private OnGetNotifySettingListener onGetNotifySettingListener;

    @Inject public ChangeAutoNotifyPresenter() {
    }

    public void setOnGetNotifySettingListener(OnGetNotifySettingListener onGetNotifySettingListener) {
        this.onGetNotifySettingListener = onGetNotifySettingListener;
    }

    public void getNotifySettingRequest() {

        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("keys", "value_card_remind_enable,value_card_remind,times_card_remind_enable," + "times_card_remind,time_card_remind_enable,time_card_remind");

        RxRegiste(iCardModel
          .qcGetNotifySetting(params)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(balanceConfigsQcResponseData -> {
              if (ResponseConstant.checkSuccess(balanceConfigsQcResponseData)) {
                  onGetNotifySettingListener.onGetSuccess(balanceConfigsQcResponseData.data.notifyIsOpens);
              } else {
                  onGetNotifySettingListener.onGetFailed();
              }
          }));
    }

    public void putBalanceRemindCondition(List<CardBalanceNotifyBody.ConfigsBean> configs) {
        CardBalanceNotifyBody cardBalanceNotifyBody = new CardBalanceNotifyBody();
        cardBalanceNotifyBody.setConfigs(configs);
        RxRegiste(iCardModel
          .qcChangeAutoNotify(cardBalanceNotifyBody)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new NetSubscribe<QcResponse>() {
              @Override public void onNext(QcResponse qcResponse) {
                  if (ResponseConstant.checkSuccess(qcResponse)) {
                      onGetNotifySettingListener.onSettingSuccess();
                  } else {
                      onGetNotifySettingListener.onSettingFailed();
                  }
              }
          }));
    }

    public interface OnGetNotifySettingListener {
        void onGetSuccess(List<NotifyIsOpen> balanceDetailList);

        void onGetFailed();

        void onSettingSuccess();

        void onSettingFailed();
    }
}