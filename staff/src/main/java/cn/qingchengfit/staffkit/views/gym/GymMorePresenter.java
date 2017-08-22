package cn.qingchengfit.staffkit.views.gym;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.UpdateModule;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.constant.Post_Api;
import cn.qingchengfit.staffkit.model.db.QCDbManager;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GymMorePresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private RestRepositoryV2 restRepository;

    @Inject public GymMorePresenter(RestRepositoryV2 restRepository) {
        this.restRepository = restRepository;
    }

    void updateFunction(List<String> modules) {
        if (modules != null) {
            QCDbManager.insertFunction(modules);
        }
        RxRegiste(restRepository.getApi(Post_Api.class)
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
        RxRegiste(QCDbManager.queryAllFunctions()
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<List<String>>() {
                @Override public void call(List<String> strings) {
                    view.onModule(strings);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public interface MVPView extends CView {
        void onModule(List<String> modules);
    }
}
