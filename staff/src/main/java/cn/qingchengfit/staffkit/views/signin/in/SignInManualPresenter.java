package cn.qingchengfit.staffkit.views.signin.in;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.body.SignInManualBody;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.model.responese.SignInSchdule;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.model.responese.SigninValidCard;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.StudentUsecase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yangming on 16/8/29.
 */
public class SignInManualPresenter extends BasePresenter {

    @Inject StudentWrapper studentBase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    StudentUsecase usecase;

    private RestRepository restRepository;
    private SignInManualView view;

    @Inject public SignInManualPresenter(StudentUsecase usecase, RestRepository restRepository) {
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
        this.view = (SignInManualView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryCard() {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("user_id", studentBase.id());
        RxRegiste(restRepository.getGet_api()
            .qcGetStudentCards(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<SigninValidCard>() {
                @Override public void call(SigninValidCard signinValidCard) {
                    if (view != null) view.queryCardList(signinValidCard.getData().getCards());
                }
            }));
        //        usecase.queryCards(studentBase.getId(), coachService.getId(), coachService.getModel(), brand.getId(), new Action1<QcResponseStudentCards>() {
        //            @Override
        //            public void call(QcResponseStudentCards qcResponseStudentCards) {
        //                if (qcResponseStudentCards.data != null && qcResponseStudentCards.data.cards != null) {
        //                    view.queryCardList(qcResponseStudentCards.data.cards);
        //                } else {
        //                    view.queryCardList(new ArrayList<Card>());
        //                }
        //            }
        //        });
    }

    public void getCardCostList() {
        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(restRepository.getGet_api()
            .qcGetSignInCostConfig(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<SignInCardCostBean.Data>>() {
                @Override public void call(QcResponseData<SignInCardCostBean.Data> signInCardCostBean) {
                    if (view != null) view.getCardCost(signInCardCostBean.data.card_costs);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            }));
    }

    public void queryCourse() {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("user_id", studentBase.id());
        RxRegiste(restRepository.getGet_api()
            .qcGetStudentCourse(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<SignInSchdule>() {
                @Override public void call(SignInSchdule signInSchdule) {
                    if (view != null) view.onCourseListData(signInSchdule.data.getSchedules());
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    if (view != null) view.onCourseListData(new ArrayList<SignInTasks.Schedule>());
                }
            }));
    }

    public void choseLocker() {
        view.selectLocker();
    }

    public void confirm(String lockerId, String cardId) {
        SignInManualBody body = new SignInManualBody.Builder().build();
        body.setUser_id(Integer.valueOf(studentBase.id()));
        body.setLocker_id(lockerId);
        body.setCard_id(cardId);
        RxRegiste(restRepository.getPost_api()
            .qcPostCheckInMaual(App.staffId, gymWrapper.getParams(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (view != null) view.confirmSignIn();
                    } else {

                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            }));
    }

    public void changeImage(String staffid, String img, String studentid) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id", studentid);
        body.put("photo", img);
        RxRegiste(restRepository.getPost_api()
            .qcUploadStuImg(staffid, gymWrapper.getParams(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        //                            if (view != null)
                        //                                view.onImageChangeSuccess();
                    } else {
                        //                            if (view != null)
                        //                                view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    //                        if (view != null)
                    //                            view.onShowError(throwable.getMessage());
                }
            }));
    }

    /**
     * Created by yangming on 16/8/29.
     */
    public interface SignInManualView extends PView {

        void confirmSignIn();

        void queryCardList(List<SigninValidCard.DataBean.CardsBean> cards);

        void selectLocker();

        void onCourseListData(List<SignInTasks.Schedule> list);

        void getCardCost(List<SignInCardCostBean.CardCost> signInConfigs);
    }
}