package cn.qingchengfit.staffkit.views.signin.in;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.SignInManualBody;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.model.responese.SignInSchdule;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.model.responese.SigninValidCard;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.usecase.StudentUsecase;
import cn.qingchengfit.staffkit.views.signin.bean.UserCheckInOrder;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.Date;
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

  @Inject StudentWrap studentBase;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  StudentUsecase usecase;

  private StaffRespository restRepository;
  private SignInManualView view;

  @Inject public SignInManualPresenter(StudentUsecase usecase, StaffRespository restRepository) {
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
    RxRegiste(restRepository.getStaffAllApi()
        .qcGetStudentCards(App.staffId, params)
        .onBackpressureBuffer()
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
    RxRegiste(restRepository.getStaffAllApi()
        .qcGetSignInCostConfig(App.staffId, params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<SignInCardCostBean.Data>>() {
          @Override public void call(QcDataResponse<SignInCardCostBean.Data> signInCardCostBean) {
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
    RxRegiste(restRepository.getStaffAllApi()
        .qcGetStudentCourse(App.staffId, params)
        .onBackpressureBuffer()
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
    loadStudentCheckinOrders(body);
  }

  private void checkinStudent(SignInManualBody body) {
    RxRegiste(restRepository.getStaffAllApi()
        .qcPostCheckInMaual(App.staffId, gymWrapper.getParams(), body)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (view != null) view.confirmSignIn();
            } else {
              if (view != null) view.onShowError(qcResponse.msg);
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
    RxRegiste(restRepository.getStaffAllApi()
        .qcUploadStuImg(staffid, gymWrapper.getParams(), body)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
            } else {
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            //                        if (view != null)
            //                            view.onShowError(throwable.getMessage());
          }
        }));
  }

  public void loadStudentCheckinOrders(SignInManualBody body) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("user_id", body.getUser_id());
    RxRegiste(restRepository.getStaffAllApi()
        .qcGetUserCheckinOrders(loginStatus.staff_id(), params)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            UserCheckInOrder order = response.data.order;
            if (order != null && order.getId() > 0) {
              view.errorMultiCheckinOrders(DateUtils.formatDateFromServer(order.getExpireTime()));
              return;
            }
            checkinStudent(body);
          } else {
            ToastUtils.show(response.msg);
          }
        }, throwable -> {
          ToastUtils.show(throwable.getMessage());
        }));
  }

  /**
   * Created by yangming on 16/8/29.
   */
  public interface SignInManualView extends CView {

    void confirmSignIn();

    void errorMultiCheckinOrders(Date date);

    void queryCardList(List<SigninValidCard.DataBean.CardsBean> cards);

    void selectLocker();

    void onCourseListData(List<SignInTasks.Schedule> list);

    void getCardCost(List<SignInCardCostBean.CardCost> signInConfigs);
  }
}