//package cn.qingchengfit.staffkit.views.gym.staff;
//
//import cn.qingchengfit.di.BasePresenter;
//import cn.qingchengfit.di.CView;
//import cn.qingchengfit.di.PView;
//import cn.qingchengfit.di.model.GymWrapper;
//import cn.qingchengfit.di.model.LoginStatus;
//import ChangeSuBody;
//import cn.qingchengfit.network.ResponseConstant;
//import cn.qingchengfit.network.response.QcResponse;
//import cn.qingchengfit.staffkit.rest.RestRepository;
//import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
//import javax.inject.Inject;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Action1;
//import rx.schedulers.Schedulers;
//
//public class SuNewPresenter extends BasePresenter {
//    @Inject LoginStatus loginStatus;
//    @Inject GymWrapper gymWrapper;
//    private MVPView view;
//    private RestRepository restRepository;
//
//    @Inject public SuNewPresenter(RestRepository restRepository) {
//        this.restRepository = restRepository;
//    }
//
//    public void sendMsg(GetCodeBody body) {
//        RxRegiste(restRepository.getPost_api()
//            .qcGetCode(body).onBackpressureBuffer().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Action1<QcResponse>() {
//                @Override public void call(QcResponse qcResponse) {
//                    if (ResponseConstant.checkSuccess(qcResponse)) {
//                        view.onSendMSGSuccess();
//                    } else {
//                        view.onShowError(qcResponse.getMsg());
//                    }
//                }
//            }, new Action1<Throwable>() {
//                @Override public void call(Throwable throwable) {
//                    view.onShowError(throwable.getMessage());
//                }
//            }));
//    }
//
//    public void changeSu(String staffid, ChangeSuBody body) {
//        RxRegiste(restRepository.getPost_api()
//            .qcChangeSu(staffid, gymWrapper.getParams(), body)
//            .onBackpressureBuffer()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Action1<QcResponse>() {
//                @Override public void call(QcResponse qcResponse) {
//                    if (ResponseConstant.checkSuccess(qcResponse)) {
//                        view.onChangeSuccess();
//                    } else {
//                        view.onShowError(qcResponse.getMsg());
//                    }
//                }
//            }, new Action1<Throwable>() {
//                @Override public void call(Throwable throwable) {
//                    view.onShowError(throwable.getMessage());
//                }
//            }));
//    }
//
//    @Override public void attachView(PView v) {
//        view = (MVPView) v;
//    }
//
//    public interface MVPView extends CView {
//        void onSendMSGSuccess();
//
//        void onChangeSuccess();
//    }
//}
