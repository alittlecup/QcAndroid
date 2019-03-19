package cn.qingchengfit.staffkit.views.gym;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.UpdateModule;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saascommon.model.QcDbManager;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GymMorePresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject QcDbManager qcDbManager;
    private MVPView view;
    private StaffRespository restRepository;

    @Inject public GymMorePresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }
    void updateFunction(List<String> modules) {
        if (modules != null) {
            qcDbManager.insertFunction(modules);
        }
        RxRegiste(restRepository.getStaffAllApi()
            .qcUpdateModule(loginStatus.staff_id(), new UpdateModule.Builder().module_custom(modules).build(), gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {

                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }


    @Override public void attachView(PView v) {
        view = (MVPView) v;
        RxRegiste(qcDbManager.queryAllFunctions()
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(strings -> view.onModule(strings),
              throwable -> LogUtil.e(throwable.getMessage())));
    }
    public void queiteGym(){
        RxRegiste(restRepository.getStaffAllApi()
            .qcQuitGym(loginStatus.staff_id(), gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onQuiteGym();
                    } else {
                        ToastUtils.show(qcResponse.getMsg());
                    }
                }
            }, throwable -> ToastUtils.show(throwable.getMessage()))

        );
    }

    public interface MVPView extends CView {
        void onModule(List<String> modules);
        void onQuiteGym();
    }
}
