package cn.qingchengfit.staffkit.views.signin.in;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.SignInBody;
import cn.qingchengfit.model.body.SignInIgnorBody;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
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
public class SignInListPresenter extends BasePresenter {

    public SignInListView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StaffRespository restRepository;

    @Inject public SignInListPresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (SignInListView) v;
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
        params.put("status", "2");
        params.put("show_all", "1");
        params.put("order_by", "-created_at");
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetSignInTasks(App.staffId, params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<SignInTasks>() {
                @Override public void call(SignInTasks signInTasks) {
                    view.onData(signInTasks.data.check_in);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    if (view != null) view.onData(new ArrayList<SignInTasks.SignInTask>());
                }
            }));
    }

    public void interval(int latestId) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("status", "2");
        params.put("show_all", "1");
        params.put("order_by", "-created_at");
        params.put("id__gt", "" + latestId);

        RxRegiste(restRepository.getStaffAllApi()
            .qcGetSignInTasks(App.staffId, params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<SignInTasks>() {
                @Override public void call(SignInTasks signInTasks) {
                    view.onInterval(signInTasks.data.check_in);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            }));
    }

    public void ignor(final int position, int checkInId) {
        HashMap<String, Object> params = gymWrapper.getParams();
        SignInIgnorBody body = new SignInIgnorBody(checkInId, true);
        RxRegiste(restRepository.getStaffAllApi()
            .qcPostIgnore(App.staffId, params, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200 && view != null) {
                        view.ignorComplete(position);
                    } else {
                        view.confirmFail(position, "", "");
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            }));
    }

    public void confirm(final int position, int checkInId, long lockerId) {
        HashMap<String, Object> params = gymWrapper.getParams();
        SignInBody body = new SignInBody();
        body.setCheckin_id(checkInId);
        body.setLocker_id(lockerId == 0 ? "" : String.valueOf(lockerId));
        RxRegiste(restRepository.getStaffAllApi()
            .qcPutDoubleCheckin(App.staffId, params, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200 && view != null) {
                        view.confirComplete(position);
                    } else if (qcResponse.status == 500 && view != null) {
                        view.confirmFail(position, qcResponse.error_code, qcResponse.msg);
                    } else {
                        view.confirmFail(position, "", "");
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    if (view != null) view.confirmFail(position, "", "");
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
    public interface SignInListView extends CView {

        void onData(List<SignInTasks.SignInTask> list);

        void onInterval(List<SignInTasks.SignInTask> list);

        void ignorComplete(int position);

        void confirComplete(int position);

        void confirmFail(int position, String erroCode, String msg);

        void onImageChangeSuccess();
    }
}
