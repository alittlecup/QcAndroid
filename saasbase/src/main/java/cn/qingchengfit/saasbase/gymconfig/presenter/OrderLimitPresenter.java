//package cn.qingchengfit.saasbase.gymconfig.presenter;
//
//import cn.qingchengfit.di.BasePresenter;
//import cn.qingchengfit.di.CView;
//import cn.qingchengfit.network.ResponseConstant;
//import cn.qingchengfit.network.response.QcDataResponse;
//import cn.qingchengfit.network.response.QcResponse;
//import cn.qingchengfit.saasbase.constant.ShopConfigs;
//import cn.qingchengfit.saasbase.gymconfig.network.response.ShopConfigBody;
//import cn.qingchengfit.saasbase.repository.ICourseModel;
//import java.util.List;
//import javax.inject.Inject;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Action1;
//import rx.schedulers.Schedulers;
//
//public class OrderLimitPresenter extends BasePresenter<OrderLimitPresenter.MVPView> {
//
//    @Inject ICourseModel courseModel;
//
//    @Inject public OrderLimitPresenter() {
//
//    }
//
//    void saveConfigs(ShopConfigBody body) {
//        RxRegiste(restRepository.getPost_api()
//            .qcShopConfigs(loginStatus.staff_id(), gymWrapper.getParams(), body)
//            .onBackpressureBuffer()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Action1<QcResponse>() {
//                @Override public void call(QcResponse qcResponse) {
//                    if (ResponseConstant.checkSuccess(qcResponse)) {
//                        mvpView.onSuccess();
//                    } else {
//                        mvpView.onShowError(qcResponse.getMsg());
//                    }
//                }
//            }, new Action1<Throwable>() {
//                @Override public void call(Throwable throwable) {
//                    mvpView.onShowError(throwable.getMessage());
//                }
//            }));
//    }
//
//    void queryOrderLimit(boolean isPrivate) {
//        RxRegiste(restRepository.getGet_api()
//            .qcGetShopConfig(loginStatus.staff_id(), isPrivate ? ShopConfigs.PRIVATE_MINUTES_CAN_ORDER : ShopConfigs.TEAM_MINUTES_CAN_ORDER,
//                gymWrapper.getParams()).onBackpressureBuffer().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Action1<QcDataResponse<SignInConfig.Data>>() {
//                @Override public void call(QcDataResponse<SignInConfig.Data> qcResponse) {
//                    if (ResponseConstant.checkSuccess(qcResponse)) {
//                        if (qcResponse.getData().configs != null && qcResponse.getData().configs.size() > 0) {
//                            mvpView.onCourseOrderLimit(qcResponse.getData().configs.get(0));
//                        }
//                    } else {
//                        mvpView.onShowError(qcResponse.getMsg());
//                    }
//                }
//            }, new Action1<Throwable>() {
//                @Override public void call(Throwable throwable) {
//                    mvpView.onShowError(throwable.getMessage());
//                }
//            }));
//    }
//
//    void querySignLimit(boolean isPrivate) {
//        String keys = isPrivate ? ShopConfigs.PRIVATE_SIGN_CLASS_OPEN
//            + ","
//            + ShopConfigs.PRIVATE_SIGN_CLASS_WAY
//            + ","
//            + ShopConfigs.PRIVATE_SIGN_CLASS_START
//            + ","
//            + ShopConfigs.PRIVATE_SIGN_CLASS_END : ShopConfigs.GROUP_SIGN_CLASS_OPEN
//            + ","
//            + ShopConfigs.GROUP_SIGN_CLASS_WAY
//            + ","
//            + ShopConfigs.GROUP_SIGN_CLASS_START
//            + ","
//            + ShopConfigs.GROUP_SIGN_CLASS_END;
//        RxRegiste(restRepository.getGet_api()
//            .qcGetShopConfig(loginStatus.staff_id(), keys,
//                gymWrapper.getParams()).onBackpressureBuffer().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Action1<QcDataResponse<SignInConfig.Data>>() {
//                @Override public void call(QcDataResponse<SignInConfig.Data> qcResponse) {
//                    if (ResponseConstant.checkSuccess(qcResponse)) {
//                        if (qcResponse.getData().configs != null && qcResponse.getData().configs.size() > 0) {
//                            mvpView.onSignClassLimit(qcResponse.getData().configs);
//                        }
//                    } else {
//                        mvpView.onShowError(qcResponse.getMsg());
//                    }
//                }
//            }, new Action1<Throwable>() {
//                @Override public void call(Throwable throwable) {
//                    mvpView.onShowError(throwable.getMessage());
//                }
//            }));
//    }
//
//    void queryCancelLimit(boolean isPrivate) {
//        RxRegiste(restRepository.getGet_api()
//            .qcGetShopConfig(loginStatus.staff_id(),
//                isPrivate ? ShopConfigs.PRIVATE_MINUTES_CAN_CANCEL : ShopConfigs.TEAM_MINUTES_CAN_CANCEL, gymWrapper.getParams())
//            .onBackpressureBuffer()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Action1<QcDataResponse<SignInConfig.Data>>() {
//                @Override public void call(QcDataResponse<SignInConfig.Data> qcResponse) {
//                    if (ResponseConstant.checkSuccess(qcResponse)) {
//                        if (qcResponse.getData().configs != null && qcResponse.getData().configs.size() > 0) {
//                            mvpView.onCourseCancelLimit(qcResponse.getData().configs.get(0));
//                        }
//                    } else {
//                        mvpView.onShowError(qcResponse.getMsg());
//                    }
//                }
//            }, new Action1<Throwable>() {
//                @Override public void call(Throwable throwable) {
//                    mvpView.onShowError(throwable.getMessage());
//                }
//            }));
//    }
//
//    void querySubstituteLimit(boolean isPrivate) {
//        RxRegiste(restRepository.getGet_api()
//            .qcGetShopConfig(loginStatus.staff_id(), isPrivate ? ShopConfigs.PRIVATE_MINUTES_CAN_ORDER : ShopConfigs.TEAM_MINUTES_CAN_ORDER,
//                gymWrapper.getParams()).onBackpressureBuffer().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Action1<QcDataResponse<SignInConfig.Data>>() {
//                @Override public void call(QcDataResponse<SignInConfig.Data> qcResponse) {
//                    if (ResponseConstant.checkSuccess(qcResponse)) {
//                        if (qcResponse.getData().configs != null && qcResponse.getData().configs.size() > 0) {
//                            mvpView.onCourseSubstituteLimit(qcResponse.getData().configs.get(0));
//                        }
//                    } else {
//                        mvpView.onShowError(qcResponse.getMsg());
//                    }
//                }
//            }, new Action1<Throwable>() {
//                @Override public void call(Throwable throwable) {
//                    mvpView.onShowError(throwable.getMessage());
//                }
//            }));
//    }
//
//
//
//    public interface MVPView extends CView {
//        void onSuccess();
//
//        /**
//         * 团课预约限制 开始前xx分钟
//         */
//        void onCourseOrderLimit(SignInConfig.Config config);
//
//        /**
//         * 团课取消预约限制
//         */
//        void onCourseCancelLimit(SignInConfig.Config config);
//
//        /**
//         * 排队候补
//         */
//        void onCourseSubstituteLimit(SignInConfig.Config config);
//
//        void onSaveOk();
//        void onSignClassLimit(List<SignInConfig.Config> configs);
//    }
//}