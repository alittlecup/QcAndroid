package cn.qingchengfit.staffkit.views.signin;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.SignInDetail;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yangming on 16/8/29.
 */
public class SignInDetailPresenter extends BasePresenter {

    SignInDetailView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StaffRespository restRepository;

    @Inject public SignInDetailPresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (SignInDetailView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        this.view = null;
    }

    public void queryData(String checkin_id) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("checkin_id", checkin_id);
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetCheckInDetail(loginStatus.staff_id(), params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<SignInDetail>() {
                @Override public void call(SignInDetail signInDetail) {
                    view.onData(signInDetail.data.check_in);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            }));
    }

    public void changeImage(String img, String studentid) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id", studentid);
        body.put("photo", img);
        RxRegiste(restRepository.getStaffAllApi()
            .qcUploadStuImg(loginStatus.staff_id(), gymWrapper.getParams(), body)
            .onBackpressureBuffer()
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

    public void cancelSignIn(String checkin_id) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("checkin_id", checkin_id);
        RxRegiste(restRepository.getStaffAllApi()
            .qcDeleteCheckin(loginStatus.staff_id(), params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    view.cancelComplete();
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            }));
    }

    /**
     * Created by yangming on 16/8/29.
     */
    public interface SignInDetailView extends PView {

        void cancelComplete();

        void onData(SignInTasks.SignInTask signInTask);
    }
}