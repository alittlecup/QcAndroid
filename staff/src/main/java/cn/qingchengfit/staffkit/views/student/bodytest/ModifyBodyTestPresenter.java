package cn.qingchengfit.staffkit.views.student.bodytest;

import android.content.Intent;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.body.BodyTestBody;
import cn.qingchengfit.model.responese.BodyTestMeasureData;
import cn.qingchengfit.model.responese.BodyTestTemplateData;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.NetWorkThrowable;
import cn.qingchengfit.staffkit.rest.RestRepository;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/21 2016.
 */
public class ModifyBodyTestPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    ModifyBodyTestView view;

    @Inject public ModifyBodyTestPresenter() {

    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (ModifyBodyTestView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    void queryInfo(String measureId) {
        RxRegiste(restRepository.getGet_api()
            .qcGetBodyTest(App.staffId, measureId, gymWrapper.getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponseData<BodyTestMeasureData>>() {
                @Override public void call(QcResponseData<BodyTestMeasureData> responseData) {
                    if (responseData.data != null) {
                        view.onShowMeasure(responseData.data.measure);
                        view.onShowExtra(true, responseData.data.measure.photos, responseData.data.measure.extra);
                    }
                }
            }, new NetWorkThrowable()));
    }

    void modifyBodyTest(String measureId, String gymid, String gymmodel, BodyTestBody bodyTestBean) {
        if (gymmodel != null && gymmodel.equalsIgnoreCase("service")) {
            bodyTestBean.extra = "";
            bodyTestBean.photos = null;
        }
        RxRegiste(restRepository.getPost_api()
            .qcUpdateBodyTest(loginStatus.staff_id(), measureId, gymWrapper.getParams(), bodyTestBean)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        view.onSuccess();
                    } else {
                        view.onFailed(qcResponse.msg);
                    }
                }
            }, new NetWorkThrowable()));
    }

    void addBodyTest(BodyTestBody bodyTestBean, String id, String model) {
        if (model != null && model.equalsIgnoreCase("service")) {
            bodyTestBean.extra = "";
            bodyTestBean.photos = null;
        }
        RxRegiste(restRepository.getPost_api()
            .qcAddBodyTest(loginStatus.staff_id(), gymWrapper.getParams(), bodyTestBean)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        view.onSuccess();
                    } else {
                        view.onFailed(qcResponse.msg);
                    }
                }
            }, new NetWorkThrowable()));
    }

    void delBodyTest(String measureid) {
        RxRegiste(restRepository.getPost_api()
            .qcDelBodyTest(loginStatus.staff_id(), measureid, gymWrapper.getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        view.onSuccess();
                    } else {
                        view.onFailed(qcResponse.msg);
                    }
                }
            }, new NetWorkThrowable()));
    }

    void queryBodyModel() {
        RxRegiste(restRepository.getGet_api()
            .qcGetBodyTestModel(gymWrapper.getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponseData<BodyTestTemplateData>>() {
                @Override public void call(QcResponseData<BodyTestTemplateData> responseData) {
                    if (responseData.getStatus() == ResponseConstant.SUCCESS) {
                        view.onShowBase(responseData.data.template.base);
                        view.onShowExtra(true, null, responseData.data.template.extra);
                    } else {
                        view.onFailed(responseData.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }
}
