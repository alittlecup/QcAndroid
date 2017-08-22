package cn.qingchengfit.staffkit.views.student.detail;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponseStudentInfo;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Score;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshStudent;
import cn.qingchengfit.staffkit.rxbus.event.StudentBaseInfoEvent;
import cn.qingchengfit.staffkit.usecase.StudentUsecase;
import cn.qingchengfit.staffkit.usecase.bean.User_Student;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.StringUtils;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

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
 * Created by Paper on 16/3/20 2016.
 */
public class StudentMoreInfoPresenter extends BasePresenter {

    @Inject StudentWrapper studentWrapper;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StudentUsecase usecase;
    private Subscription querySp;
    private Subscription delSp;
    private Subscription edSp;
    private StudentMoreInfoView view;
    private RestRepositoryV2 restRepository;

    @Inject public StudentMoreInfoPresenter(StudentUsecase usecase, RestRepositoryV2 restRepository) {
        this.usecase = usecase;
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (StudentMoreInfoView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (querySp != null) querySp.unsubscribe();
        if (delSp != null) delSp.unsubscribe();
        if (edSp != null) edSp.unsubscribe();
    }

    public void queryStudents() {
        querySp = usecase.queryBaseInfo(studentWrapper.id(), gymWrapper.id(), gymWrapper.model(),
            gymWrapper.inBrand() ? gymWrapper.brand_id() : null, new Action1<QcResponseStudentInfo>() {
                @Override public void call(QcResponseStudentInfo qcResponseStudentInfo) {
                    if (qcResponseStudentInfo.getStatus() == ResponseConstant.SUCCESS) {
                        view.onData(qcResponseStudentInfo.data.user);
                        RxBus.getBus()
                            .post(new StudentBaseInfoEvent(qcResponseStudentInfo.data.user, qcResponseStudentInfo.data.private_url,
                                qcResponseStudentInfo.data.group_url, qcResponseStudentInfo.data.user.getStatus()));
                    } else {

                    }
                }
            });
    }

    public void delStudent(final String shop_ids) {

        String id = null, model = null;
        if (gymWrapper.inBrand()) {
            if (StringUtils.isEmpty(shop_ids)) {
                view.onFailed("至少选择一家场馆");
                return;
            }
        } else {
            id = gymWrapper.id();
            model = gymWrapper.model();
        }

        delSp = usecase.delStudent(studentWrapper.id(), id, model, null, shop_ids, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    if (gymWrapper.inBrand()) {
                        if (studentWrapper.getStudentBean().getSupport_shop_ids().equalsIgnoreCase(shop_ids)) {
                            StudentAction.newInstance().delStudentByBrand(gymWrapper.brand_id(), studentWrapper.id());
                        }
                    } else {
                        StudentAction.newInstance().delStudentByGym(gymWrapper.id(), gymWrapper.model(), studentWrapper.id());
                    }
                    view.onSuccess();
                    RxBus.getBus().post(new EventFreshStudent());
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        });
    }

    public void editStudent(User_Student user_student) {

        final User_Student body = new User_Student();
        body.setPhone(user_student.getPhone());
        body.setUsername(user_student.getUsername());
        body.setAddress(user_student.getAddress());
        body.setAvatar(user_student.getAvatar());
        body.setDate_of_birth(user_student.getDate_of_birth());
        body.setGender(user_student.getGender());
        body.setJoined_at(user_student.getJoined_at());
        body.setRecommend_by_id(user_student.getRecommend_by_id());
        body.setOrigin(user_student.getOrigin());
        body.setRemarks(user_student.getRemarks());

        edSp = usecase.updateStudent(user_student.getId(), gymWrapper.id(), gymWrapper.model(), gymWrapper.brand_id(), body,
            new Action1<QcResponseData>() {
                @Override public void call(QcResponseData qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        RxBus.getBus().post(new EventFreshStudent());
                        view.onEditSuccess();
                    } else {
                        // ToastUtils.logHttp(qcResponse);
                    }
                }
            });
    }

    /**
     * 获取积分功能是否开启
     */
    public void getScoreStatus() {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);
        Observable observable = restRepository.getApi(Get_Api.class).qcGetStudentScoreStatus(App.staffId, params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<ScoreStatus>() {
            @Override protected void _onNext(ScoreStatus scoreStatus) {
                view.onScoreStatus(scoreStatus.getModule().isScore());
            }

            @Override protected void _onError(String message) {
                view.onScoreStatusFail(message);
            }
        }));
    }

    public void getScore(String studentId) {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);

        ArrayMap<String, Object> body = new ArrayMap<>();
        body.put("score", true);
        Observable observable = restRepository.getApi(Get_Api.class).qcGetStudentScore(App.staffId, studentId, params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<Score>() {
            @Override protected void _onNext(Score score) {
                view.onStudentScore(score.shopbrand.score);
            }

            @Override protected void _onError(String message) {
                view.onStudentScoreFail(message);
            }
        }));
    }
}
