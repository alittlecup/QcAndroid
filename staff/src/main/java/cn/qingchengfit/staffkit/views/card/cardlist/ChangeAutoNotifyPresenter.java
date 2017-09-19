package cn.qingchengfit.staffkit.views.card.cardlist;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.CardBalanceNotifyBody;
import cn.qingchengfit.model.responese.NotifyIsOpen;
import cn.qingchengfit.model.responese.NotityIsOpenConfigs;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/3/1.
 */

public class ChangeAutoNotifyPresenter extends BasePresenter {

    public static final String STORE_CARD_REMINE = "value_card_remind_enable";
    public static final String STORE_CARD_REMINE_VALUE = "value_card_remind";

    public static final String SECOND_CARD_REMIND = "times_card_remind_enable";
    public static final String SECOND_CARD_REMIND_VALUE = "times_card_remind";

    public static final String TIME_CARD_REMINE_VALUE = "time_card_remind";
    public static final String TIME_CARD_REMINE = "time_card_remind_enable";
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private RestRepository restRepository;
    private OnGetNotifySettingListener onGetNotifySettingListener;

    @Inject public ChangeAutoNotifyPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public void setOnGetNotifySettingListener(OnGetNotifySettingListener onGetNotifySettingListener) {
        this.onGetNotifySettingListener = onGetNotifySettingListener;
    }

    public void getNotifySettingRequest() {

        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("keys", "value_card_remind_enable,value_card_remind,times_card_remind_enable,"
            + "times_card_remind,time_card_remind_enable,time_card_remind");

        RxRegiste(restRepository.getGet_api()
            .qcGetNotifySetting(loginStatus.staff_id(), params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<NotityIsOpenConfigs>>() {
                @Override public void call(QcDataResponse<NotityIsOpenConfigs> balanceConfigsQcResponseData) {
                    if (ResponseConstant.checkSuccess(balanceConfigsQcResponseData)) {
                        onGetNotifySettingListener.onGetSuccess(balanceConfigsQcResponseData.data.notifyIsOpens);
                    } else {
                        onGetNotifySettingListener.onGetFailed();
                    }
                }
            }));
    }

    public void putBalanceRemindCondition(List<CardBalanceNotifyBody.ConfigsBean> configs) {
        CardBalanceNotifyBody cardBalanceNotifyBody = new CardBalanceNotifyBody();
        cardBalanceNotifyBody.setConfigs(configs);
        RxRegiste(restRepository.getPost_api()
            .qcChangeAutoNotify(loginStatus.staff_id(), gymWrapper.getParams(), cardBalanceNotifyBody)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
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
