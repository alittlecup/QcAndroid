package cn.qingchengfit.staffkit.views.setting;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.model.body.CardBalanceNotifyBody;
import cn.qingchengfit.model.responese.BalanceNotify;
import cn.qingchengfit.model.responese.BalanceNotifyConfigs;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/2/26.
 */

public class FixNotifyPresenter extends BasePresenter {

    private OnGetBalanceNotifyListener onGetBalanceNotifyListener;
    private RestRepository restRepository;

    @Inject public FixNotifyPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public void setOnGetBalanceNotifyListener(OnGetBalanceNotifyListener onGetBalanceNotifyListener) {
        this.onGetBalanceNotifyListener = onGetBalanceNotifyListener;
    }

    public void queryBalanceNotify(String staffId) {

        HashMap<String, Object> params = new HashMap<>();

        params.put("keys", "notify_balance_cards");
        RxRegiste(restRepository.getGet_api()
            .qcGetBalanceNotify(App.staffId, params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<BalanceNotifyConfigs>>() {
                @Override public void call(QcDataResponse<BalanceNotifyConfigs> responseData) {
                    if (responseData.status == 200) {
                        if (onGetBalanceNotifyListener != null) {
                            onGetBalanceNotifyListener.onGetSuccess(responseData.data.balanceNotifies);
                        }
                    } else {
                        if (onGetBalanceNotifyListener != null) {
                            onGetBalanceNotifyListener.onGetFailed(responseData.msg);
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public void putBalanceNotify(String staffid, CardBalanceNotifyBody.ConfigsBean configsBean) {
        CardBalanceNotifyBody body = new CardBalanceNotifyBody();
        List<CardBalanceNotifyBody.ConfigsBean> configs = new ArrayList<>();
        //        SignInNoticeConfigBody.ConfigsBean config = new SignInNoticeConfigBody.ConfigsBean();
        //        config.setValue(event.getValue());
        //        config.setId(event.getId());
        //        config.setBrand_id(event.getBrandId());
        //        config.setShop_id(event.getShopId());
        configs.add(configsBean);
        body.setConfigs(configs);

        RxRegiste(restRepository.getPost_api()
            .qcPostBalanceNotify(staffid, body).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        onGetBalanceNotifyListener.onDone();
                    } else {
                        onGetBalanceNotifyListener.onGetFailed(qcResponse.msg);
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public interface OnGetBalanceNotifyListener extends CView {
        void onGetSuccess(List<BalanceNotify> balanceNotify);

        void onGetFailed(String msg);

        void onDone();
    }
}
