package cn.qingchengfit.staffkit.allocate.coach.presenter;

import android.content.Intent;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.allocate.coach.model.Coach;
import cn.qingchengfit.staffkit.allocate.coach.model.CoachResponseList;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by fb on 2017/5/4.
 */

public class AllocateCoachListPresenter extends BasePresenter {

    public CoachListView view;

    @Inject RestRepository restRepository;
    @Inject GymWrapper gymWrapper;

    @Inject public AllocateCoachListPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        this.view = (CoachListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void getCoachPreviewList() {
        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(restRepository.getGet_api()
            .qcGetCoachList(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<CoachResponseList>>() {
                @Override public void call(QcResponseData<CoachResponseList> allotSalePreViewsQcResponseData) {
                    if (allotSalePreViewsQcResponseData.status == 200) {
                        view.onCoachessPreview(allotSalePreViewsQcResponseData.data.coaches);
                    } else {
                        view.onShowError(allotSalePreViewsQcResponseData.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public interface CoachListView extends PView {
        void onCoachessPreview(List<Coach> list);

        void onShowError(String e);
    }
}
