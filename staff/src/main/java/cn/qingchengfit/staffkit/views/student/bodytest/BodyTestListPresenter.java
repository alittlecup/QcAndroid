package cn.qingchengfit.staffkit.views.student.bodytest;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.responese.BodyTestBean;
import cn.qingchengfit.model.responese.BodyTestPreview;
import cn.qingchengfit.model.responese.BodyTestPreviews;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
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
public class BodyTestListPresenter extends BasePresenter {
    BodyTestListView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentWrapper studentBase;
    @Inject RestRepository restRepository;
    private Subscription sp;

    @Inject public BodyTestListPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (BodyTestListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
        if (sp != null) sp.unsubscribe();
    }

    public void queryBodyTestList() {
        RxRegiste(restRepository.getGet_api()
            .qcGetStuedntBodyTest(loginStatus.staff_id(), studentBase.id(), gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<BodyTestPreviews>>() {
                @Override public void call(QcDataResponse<BodyTestPreviews> bodyTestReponse) {
                    List<BodyTestBean> strings = new ArrayList<BodyTestBean>();
                    for (BodyTestPreview measure : bodyTestReponse.data.measures) {
                        strings.add(measure.toBodyTestBean());
                    }
                    view.onData(strings);
                }
            }, new NetWorkThrowable()));
    }
}
