package cn.qingchengfit.staffkit.views.setting;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.body.SignInNoticeConfigBody;
import cn.qingchengfit.model.responese.SigninNoticeConfig;
import cn.qingchengfit.model.responese.SigninNoticeConfigs;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.rxbus.event.SignInNoticeConfigEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yangming on 16/9/29.
 */
public class FixCheckinPresenter extends BasePresenter {

    public PresenterView view;
    private StaffRespository restRepository;

    @Inject public FixCheckinPresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (PresenterView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void getConfigs() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("keys", "app_checkin_notification");
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetCheckinNotiConfigs(App.staffId, params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<SigninNoticeConfigs>>() {
                @Override public void call(QcDataResponse<SigninNoticeConfigs> responseData) {
                    if (responseData.status == 200) {
                        if (view != null) {
                            view.onGetConfigSuccess(responseData.data.configs);
                        }
                    } else {
                        if (view != null) view.conGetFail(responseData.error_code, responseData.msg);
                    }
                }
            }));
    }

    public void setConfigs(SignInNoticeConfigEvent event) {

        SignInNoticeConfigBody body = new SignInNoticeConfigBody();
        List<SignInNoticeConfigBody.ConfigsBean> configs = new ArrayList<>();
        SignInNoticeConfigBody.ConfigsBean config = new SignInNoticeConfigBody.ConfigsBean();
        config.setValue(event.getValue());
        config.setId(event.getId());
        config.setBrand_id(event.getBrandId());
        config.setShop_id(event.getShopId());
        configs.add(config);
        body.setConfigs(configs);

        RxRegiste(restRepository.getStaffAllApi()
            .qcPutCheckinNoticeCOnfig(App.staffId, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200) {
                        if (view != null) {
                            view.conSetSuccess();
                        }
                    } else {
                        if (view != null) view.conSetFail(qcResponse.error_code, qcResponse.msg);
                    }
                }
            }));
    }

    public interface PresenterView extends PView {

        void onGetConfigSuccess(List<SigninNoticeConfig> list);

        void conGetFail(String erroCode, String msg);

        void conSetFail(String erroCode, String msg);

        void conSetSuccess();
    }
}