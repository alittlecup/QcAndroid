package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Staffs;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.CoachUseCase;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
 * Created by Paper on 16/1/29 2016.
 */
public class ChooseCoachPresenter extends BasePresenter {

    @Inject GymWrapper gymWrapper;
    private CoachUseCase coachUseCase;
    private RestRepository restRepository;

    @Inject public ChooseCoachPresenter(CoachUseCase coachUseCase, RestRepository restRepository) {
        this.coachUseCase = coachUseCase;
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {

    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
    }

    public void getCoachesPermission(final Action1<List<ImageTwoTextBean>> action) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("key", PermissionServerUtils.COACHSETTING);
        params.put("method", "get");

        RxRegiste(restRepository.getGet_api()
            .qcGetGymCoachesPermission(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<Staffs>>() {
                @Override public void call(QcResponseData<Staffs> qcResponseGymCoach) {
                    if (qcResponseGymCoach.getStatus() == ResponseConstant.SUCCESS) {
                        List<ImageTwoTextBean> d = new ArrayList<ImageTwoTextBean>();
                        for (Staff coach : qcResponseGymCoach.data.teachers) {
                            ImageTwoTextBean imageTwoTextBean = new ImageTwoTextBean(coach.avatar, coach.username, coach.phone);
                            imageTwoTextBean.tags.put("id", coach.id);
                            d.add(imageTwoTextBean);
                        }
                        action.call(d);
                    } else {

                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    @SuppressWarnings("unused") public void getCoaches(final Action1<List<ImageTwoTextBean>> action, int type) {
        if (type == 0) {
            SystemInitBody body = (SystemInitBody) App.caches.get("init");
            if (body != null && body.teachers.size() > 0) {
                Observable.just(body.teachers).flatMap(new Func1<List<Staff>, Observable<List<ImageTwoTextBean>>>() {
                    @Override public Observable<List<ImageTwoTextBean>> call(List<Staff> coaches) {
                        List<ImageTwoTextBean> d = new ArrayList<ImageTwoTextBean>();
                        for (Staff coach : coaches) {
                            d.add(new ImageTwoTextBean(coach.getAvatar(), coach.getUsername(), coach.getPhone()));
                        }
                        return Observable.just(d);
                    }
                }).subscribe(action);
            }
        } else if (type == 1 || type == 2) {
            getCoachesPermission(action);
        }
    }
}
