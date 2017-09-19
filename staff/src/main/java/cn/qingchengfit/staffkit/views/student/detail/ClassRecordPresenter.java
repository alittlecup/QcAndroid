package cn.qingchengfit.staffkit.views.student.detail;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.responese.ClassRecords;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.StudentUsecase;
import cn.qingchengfit.utils.StringUtils;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/19 2016.
 */

public class ClassRecordPresenter extends BasePresenter {
    @Inject StudentWrapper studentBase;

    @Inject StudentUsecase usecase;
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private ClassRecordView view;

    @Inject public ClassRecordPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (ClassRecordView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryData(HashMap<String, Object> params) {

        params.put("brand_id", gymWrapper.brand_id());

        if (params.containsKey("type")){
            if (TextUtils.isEmpty(String.valueOf(params.get("type")))){
                params.remove("type");
            }
        }

        if (params.containsKey("status")){
            if (String.valueOf(params.get("status")).equals("0")){
                params.remove("status");
            }
        }

        if (params.containsKey("start")){
            if (TextUtils.isEmpty(String.valueOf(params.get("start")))){
                params.remove("start");
            }
        }

        if (params.containsKey("end")){
            if (TextUtils.isEmpty(String.valueOf(params.get("end")))){
                params.remove("end");
            }
        }

        if (StringUtils.isEmpty(String.valueOf(params.get("shop_ids")))) {
            params.put("shop_ids", 0);
        }

        RxRegiste(restRepository.getGet_api()
            .qcGetStudentClassRecords(App.staffId, studentBase.id(), params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<ClassRecords>>() {
                @Override public void call(QcDataResponse<ClassRecords> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.data.attendances != null && qcResponse.data.stat != null && qcResponse.data.shops != null) {
                            view.onData(qcResponse.getData().attendances, qcResponse.getData().stat, qcResponse.getData().shops);
                        }
                    }
                    ;
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            }));
    }

}
