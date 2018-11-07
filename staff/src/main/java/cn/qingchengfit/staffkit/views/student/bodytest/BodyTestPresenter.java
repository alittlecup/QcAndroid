package cn.qingchengfit.staffkit.views.student.bodytest;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;

import cn.qingchengfit.model.responese.BodyTestMeasureData;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.student.bean.StudentWrap;
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
public class BodyTestPresenter extends BasePresenter {

    @Inject StudentWrap studentBean;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StaffRespository restRepository;

    BodyTestView bodyTestView;

    @Inject public BodyTestPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        bodyTestView = (BodyTestView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        bodyTestView = null;
    }

    public void queryBodyTest(String measure_id) {
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetBodyTest(App.staffId, measure_id, gymWrapper.getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<BodyTestMeasureData>>() {
                @Override public void call(QcDataResponse<BodyTestMeasureData> responseData) {
                    bodyTestView.onMeasure(responseData.data.measure);
                    if (responseData.data.measure.extra != null) bodyTestView.onExtras(responseData.data.measure.extra);

                    if (responseData.data.measure.photos != null && responseData.data.measure.photos.size() > 0) {
                        bodyTestView.onShowPics(true, responseData.data.measure.photos);
                    } else {
                        bodyTestView.onShowPics(false, null);
                    }
                }
            }, new NetWorkThrowable()));
    }
}
