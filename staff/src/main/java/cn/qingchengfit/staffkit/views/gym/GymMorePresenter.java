package cn.qingchengfit.staffkit.views.gym;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.gym.gymconfig.IGymConfigModel;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.body.UpdateModule;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saascommon.model.GymBaseInfoAction;
import cn.qingchengfit.saascommon.model.QcDbManager;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.EventFreshCoachService;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GymMorePresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject QcDbManager qcDbManager;
    @Inject IGymConfigModel gymConfigModel;
    @Inject GymBaseInfoAction gymBaseInfoAction;
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
                        createGymSuccess();
                    } else {
                        ToastUtils.show(qcResponse.getMsg());
                    }
                }
            }, throwable -> ToastUtils.show(throwable.getMessage()))

        );
    }
    public void createGymSuccess() {
        RxRegiste(gymConfigModel.qcGetCoachService(null)
            .compose(RxHelper.schedulersTransformer())
            .subscribe(gymListQcResponseData -> {
                if (ResponseConstant.checkSuccess(gymListQcResponseData)) {
                    List<CoachService> services = gymListQcResponseData.getData().services;
                    gymBaseInfoAction.writeGyms(services);
                    if (services == null || services.size() == 0) {
                        gymWrapper.setNoService(true);
                    } else if (services.size() == 1) {
                        gymWrapper.setBrand(new Brand.Builder().id(services.get(0).brand_id())
                            .name(services.get(0).name())
                            .build());
                        gymWrapper.setCoachService(services.get(0));
                    } else {
                        gymWrapper.setBrand(new Brand.Builder().id(services.get(0).brand_id())
                            .name(services.get(0).name())
                            .build());
                    }

                    RxBus.getBus().post(new EventFreshCoachService());
                    RxBus.getBus().post(new EventLoginChange());
                    view.onQuiteGym();
                }
            }, throwable -> {
            }));
    }


    public interface MVPView extends CView {
        void onModule(List<String> modules);
        void onQuiteGym();
    }
}
