package cn.qingchengfit.staffkit.views.signin.out;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;

import cn.qingchengfit.model.body.SignOutBody;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.student.bean.StudentWrap;
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
public class SignOutManualPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentWrap studentWrapper;
    private SignOutManualView view;
    private StaffRespository restRepository;

    @Inject public SignOutManualPresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (SignOutManualView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void querySignOutList() {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("status", "0");
        params.put("user_id", studentWrapper.id());
        params.put("show_all", "1");
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetCheckInList(App.staffId, params)
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
                    view.onData(new ArrayList<SignInTasks.SignInTask>());
                }
            }));
    }

    public void confirm(final int position, int checkInId) {
        HashMap<String, Object> params = gymWrapper.getParams();
        SignOutBody body = new SignOutBody();
        body.setCheckin_id(checkInId);
        RxRegiste(restRepository.getStaffAllApi()
            .qcPutCheckOutMaual(App.staffId, params, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200 && view != null) {
                        view.confirmSignOut(position);
                    } else {
                        view.onConfirmFail();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onConfirmFail();
                }
            }));
    }

    public void changeImage(String img) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id", studentWrapper.id());
        body.put("photo", img);
        RxRegiste(restRepository.getStaffAllApi()
            .qcUploadStuImg(loginStatus.staff_id(), gymWrapper.getParams(), body)
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
    public interface SignOutManualView extends CView {

        void onData(List<SignInTasks.SignInTask> list);

        void confirmSignOut(int position);

        void onConfirmFail();

        void onImageChangeSuccess();
    }
}