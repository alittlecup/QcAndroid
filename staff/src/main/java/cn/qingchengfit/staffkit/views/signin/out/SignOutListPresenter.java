package cn.qingchengfit.staffkit.views.signin.out;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.SignInIgnorBody;
import cn.qingchengfit.model.body.SignOutBody;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
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
public class SignOutListPresenter extends BasePresenter {

    public SignOutListView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private RestRepository restRepository;

    @Inject public SignOutListPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (SignOutListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    /**
     * 会员签到/签出状态：
     * status:0 已签到
     * status:1 已撤销
     * status:2 待签到
     * status:3 待签出
     * status:4 已签出
     */
    public void queryData() {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("status", "3");
        params.put("show_all", "1");
        RxRegiste(restRepository.getGet_api()
            .qcGetSignInTasks(App.staffId, params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<SignInTasks>() {
                @Override public void call(SignInTasks signInTasks) {
                    if (view != null) view.onData(signInTasks.data.check_in);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    if (view != null) view.onData(new ArrayList<SignInTasks.SignInTask>());
                }
            }));
    }

    public void interval(String modifyAt) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("status", "3");
        params.put("show_all", "1");
        if (!TextUtils.isEmpty(modifyAt)) {
            params.put("modify_at__gt", modifyAt);
        }
        RxRegiste(restRepository.getGet_api()
            .qcGetSignInTasks(App.staffId, params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<SignInTasks>() {
                @Override public void call(SignInTasks signInTasks) {
                    if (view != null) view.onInterval(signInTasks.data.check_in);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    if (view != null) view.onData(new ArrayList<SignInTasks.SignInTask>());
                }
            }));
    }

    public void ignor(final int position, int checkInId) {
        HashMap<String, Object> params = gymWrapper.getParams();
        SignInIgnorBody body = new SignInIgnorBody(checkInId, true);
        RxRegiste(restRepository.getPost_api()
            .qcPostIgnore(App.staffId, params, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200 && view != null) {
                        view.ignorComplete(position);
                    } else {
                        view.onConfirmFail(position, "", "");
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            }));
    }

    public void confirm(final int position, int checkInId) {
        HashMap<String, Object> params = gymWrapper.getParams();
        SignOutBody body = new SignOutBody();
        body.setCheckin_id(checkInId);
        RxRegiste(restRepository.getPost_api()
            .qcPutDoubleCheckout(App.staffId, params, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200 && view != null) {
                        view.confirComplete(position);
                    } else if (qcResponse.status == 500 && view != null) {
                        view.onConfirmFail(position, qcResponse.error_code, qcResponse.msg);
                    } else {
                        view.onConfirmFail(position, "", "");
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    if (view != null) Timber.e(throwable.getMessage());
                    view.onConfirmFail(position, "", "");
                }
            }));
    }

    public void changeImage(String staffid, String img, String studentid) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id", studentid);
        body.put("photo", img);
        RxRegiste(restRepository.getPost_api()
            .qcUploadStuImg(staffid, gymWrapper.getParams(), body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (view != null) view.onImageChangeSuccess();
                    } else {
                        if (view != null) view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    if (view != null) view.onShowError(throwable.getMessage());
                }
            }));
    }

    /**
     * Created by yangming on 16/8/29.
     */
    public interface SignOutListView extends CView {

        void onData(List<SignInTasks.SignInTask> list);

        void onInterval(List<SignInTasks.SignInTask> list);

        void ignorComplete(int position);

        void confirComplete(int position);

        void onConfirmFail(int position, String erroCode, String msg);

        void onImageChangeSuccess();
    }
}
