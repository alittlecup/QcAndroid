package cn.qingchengfit.staffkit.views.statement.filter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.Staffs;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
import java.util.HashMap;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
 * Created by Paper on 16/6/29 2016.
 */
public class CoachChoosePresenter extends BasePresenter {

    StatementUsecase usecase;
    CoachChooseView view;
    StaffRespository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    @Inject public CoachChoosePresenter(StatementUsecase usecase, StaffRespository restRepository) {
        this.usecase = usecase;
        this.restRepository = restRepository;
    }

    @Override public void attachView(PView v) {
        view = (CoachChooseView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryCoach() {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("key", PermissionServerUtils.COACHSETTING);
        params.put("method", "get");
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetGymCoachesPermission(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<QcDataResponse<Staffs>>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcDataResponse<Staffs> qcResponseGymCoach) {
                    if (ResponseConstant.checkSuccess(qcResponseGymCoach)) {
                        view.onCoaches(qcResponseGymCoach.data.teachers);
                    }
                }
            }));
    }
}
