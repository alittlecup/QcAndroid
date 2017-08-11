package cn.qingchengfit.staffkit.views.login;

import android.content.Context;
import android.content.Intent;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.GymList;
import cn.qingchengfit.model.responese.Login;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.model.db.QCDbManager;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.mvpbase.Presenter;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshCoachService;
import cn.qingchengfit.staffkit.usecase.LoginUsecase;
import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
import cn.qingchengfit.staffkit.usecase.bean.LoginBody;
import cn.qingchengfit.staffkit.usecase.bean.RegisteBody;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
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
 * Created by Paper on 15/11/19 2015.
 */
public class LoginPresenter implements Presenter {

    public static final String TAG = LoginPresenter.class.getSimpleName();
    @Inject GymWrapper gymWrapper;
    @Inject LoginStatus loginStatus;
    @Inject RestRepository mRestRepository;
    private LoginView mLoginView;
    private Context mContext;
    private LoginUsecase loginUsecase;
    private Subscription spGetSer;

    @Inject public LoginPresenter(LoginUsecase usecase) {
        this.loginUsecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        mLoginView = (LoginView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        mLoginView = null;
        if (spGetSer != null) spGetSer.unsubscribe();
    }

    public void doLogin(final LoginBody loginBody) {
        mLoginView.onShowLogining();

        loginBody.setDevice_type("android");
        loginBody.setPush_channel_id("");
        loginUsecase.login(loginBody).subscribe(new Action1<QcResponseData<Login>>() {
            @Override public void call(QcResponseData<Login> qcResponLogin) {
                mLoginView.cancelLogin();
                if (qcResponLogin.getStatus() == ResponseConstant.SUCCESS) {
                    PreferenceUtils.setPrefString(App.context, Configs.PREFER_SESSION, qcResponLogin.data.session_id);
                    PreferenceUtils.setPrefString(App.context, Configs.PREFER_PHONE, loginBody.phone);
                    PreferenceUtils.setPrefString(App.context, Configs.PREFER_WORK_ID, qcResponLogin.data.staff.getId());

                    App.staffId = qcResponLogin.data.staff.getId();
                    PreferenceUtils.setPrefString(App.context, Configs.PREFER_WORK_NAME, qcResponLogin.data.staff.getUsername());
                    PreferenceUtils.setPrefString(App.context, Configs.PREFER_USER_ID, qcResponLogin.getData().user.getId());
                    StudentAction.newInstance().delAllStudent();
                    getService(qcResponLogin);
                } else {
                    mLoginView.onError(qcResponLogin.msg);
                }
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                mLoginView.cancelLogin();
                ToastUtils.show("系统维护中...请稍后再试");
            }
        });
    }

    public void getService(final QcResponseData<Login> qcResponLogin) {
        spGetSer = mRestRepository.getGet_api()
            .qcGetCoachService(App.staffId, null)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<GymList>>() {
                @Override public void call(QcResponseData<GymList> gymListQcResponseData) {
                    mLoginView.cancelLogin();
                    if (ResponseConstant.checkSuccess(gymListQcResponseData)) {
                        Staff staff = new Staff(qcResponLogin.data.user);
                        staff.id = qcResponLogin.data.staff.getId();
                        loginStatus.setLoginUser(staff);
                        loginStatus.setSession(qcResponLogin.data.session_id);
                        loginStatus.setUserId(qcResponLogin.data.user.getId());

                        List<CoachService> services = gymListQcResponseData.getData().services;
                        QCDbManager.writeGyms(services);
                        if (services == null || services.size() == 0) {
                            gymWrapper.setNoService(true);
                        } else if (services.size() == 1) {
                            gymWrapper.setBrand(new Brand.Builder().id(services.get(0).brand_id()).name(services.get(0).name()).build());
                            gymWrapper.setCoachService(services.get(0));
                        } else {
                            gymWrapper.setBrand(new Brand.Builder().id(services.get(0).brand_id()).name(services.get(0).name()).build());
                        }
                        mLoginView.onSuccess(1);
                        RxBus.getBus().post(new EventFreshCoachService());
                        RxBus.getBus().post(new EventLoginChange());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    mLoginView.onError(throwable.getMessage());
                    mLoginView.cancelLogin();
                }
            });
    }

    public void queryCode(GetCodeBody phone) {
        loginUsecase.queryCode(phone).subscribe();
    }

    public void registe(final RegisteBody body) {
        mLoginView.onShowLogining();
        if (StringUtils.isEmpty(body.username)) {
            mLoginView.onError("请填写用户名");
            return;
        }
        if (StringUtils.isEmpty(body.phone)) {
            mLoginView.onError("请填写电话号码");
        }
        if (StringUtils.isEmpty(body.code)) {
            mLoginView.onError("请填写验证码");
        }
        if (StringUtils.isEmpty(body.getPassword())) {
            mLoginView.onError("请填写密码");
        }
        loginUsecase.registe(body).subscribe(new Action1<QcResponseData<Login>>() {
            @Override public void call(QcResponseData<Login> qcResponLogin) {

                if (qcResponLogin.getStatus() == ResponseConstant.SUCCESS) {
                    PreferenceUtils.setPrefString(App.context, Configs.PREFER_SESSION, qcResponLogin.data.session_id);
                    PreferenceUtils.setPrefString(App.context, Configs.PREFER_PHONE, body.phone);
                    PreferenceUtils.setPrefString(App.context, Configs.PREFER_WORK_ID, qcResponLogin.data.staff.getId());

                    App.staffId = qcResponLogin.data.staff.getId();
                    PreferenceUtils.setPrefString(App.context, Configs.PREFER_WORK_NAME, qcResponLogin.data.staff.getUsername());
                    PreferenceUtils.setPrefString(App.context, Configs.PREFER_USER_ID, qcResponLogin.getData().user.getId());

                    getService(qcResponLogin);
                } else {
                    mLoginView.onError(qcResponLogin.getMsg());
                }
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                ToastUtils.show("系统维护中...请稍后再试");
                mLoginView.cancelLogin();
            }
        });
    }
}
