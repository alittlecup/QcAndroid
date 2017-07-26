package cn.qingchengfit.staffkit.views.course.limit;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.ShopConfigBody;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.constant.ShopConfigs;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class OrderLimitPresenter extends BasePresenter {
    @Inject GymWrapper gymWrapper;
    @Inject LoginStatus loginStatus;
    private MVPView view;
    private RestRepository restRepository;

    @Inject public OrderLimitPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    void saveConfigs(ShopConfigBody body) {
        RxRegiste(restRepository.getPost_api()
            .qcShopConfigs(loginStatus.staff_id(), gymWrapper.getParams(), body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onSuccess();
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    void queryOrderLimit(boolean isPrivate) {
        RxRegiste(restRepository.getGet_api()
            .qcGetShopConfig(loginStatus.staff_id(), isPrivate ? ShopConfigs.PRIVATE_MINUTES_CAN_ORDER : ShopConfigs.TEAM_MINUTES_CAN_ORDER,
                gymWrapper.getParams()).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<SignInConfig.Data>>() {
                @Override public void call(QcResponseData<SignInConfig.Data> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.getData().configs != null && qcResponse.getData().configs.size() > 0) {
                            view.onCourseOrderLimit(qcResponse.getData().configs.get(0));
                        }
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    void queryCancelLimit(boolean isPrivate) {
        RxRegiste(restRepository.getGet_api()
            .qcGetShopConfig(loginStatus.staff_id(),
                isPrivate ? ShopConfigs.PRIVATE_MINUTES_CAN_CANCEL : ShopConfigs.TEAM_MINUTES_CAN_CANCEL, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<SignInConfig.Data>>() {
                @Override public void call(QcResponseData<SignInConfig.Data> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.getData().configs != null && qcResponse.getData().configs.size() > 0) {
                            view.onCourseCancelLimit(qcResponse.getData().configs.get(0));
                        }
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    void querySubstituteLimit(boolean isPrivate) {
        RxRegiste(restRepository.getGet_api()
            .qcGetShopConfig(loginStatus.staff_id(), isPrivate ? ShopConfigs.PRIVATE_MINUTES_CAN_ORDER : ShopConfigs.TEAM_MINUTES_CAN_ORDER,
                gymWrapper.getParams()).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<SignInConfig.Data>>() {
                @Override public void call(QcResponseData<SignInConfig.Data> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.getData().configs != null && qcResponse.getData().configs.size() > 0) {
                            view.onCourseSubstituteLimit(qcResponse.getData().configs.get(0));
                        }
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onSuccess();

        /**
         * 团课预约限制 开始前xx分钟
         */
        void onCourseOrderLimit(SignInConfig.Config config);

        /**
         * 团课取消预约限制
         */
        void onCourseCancelLimit(SignInConfig.Config config);

        /**
         * 排队候补
         */
        void onCourseSubstituteLimit(SignInConfig.Config config);

        void onSaveOk();
    }
}
