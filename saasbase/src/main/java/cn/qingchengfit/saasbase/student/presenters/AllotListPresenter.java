//package cn.qingchengfit.saasbase.student.presenters;
//
//import java.util.HashMap;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import cn.qingchengfit.di.BasePresenter;
//import cn.qingchengfit.di.PView;
//import cn.qingchengfit.di.model.GymWrapper;
//import cn.qingchengfit.network.response.QcDataResponse;
//import cn.qingchengfit.saasbase.repository.IStudentModel;
//import cn.qingchengfit.saasbase.student.network.body.AllotDataResponse;
//import cn.qingchengfit.saasbase.student.network.body.AllotDataResponseWrap;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Action1;
//import rx.schedulers.Schedulers;
//
///**
// *
// * Created by huangbaole on 2017/10/27.
// */
//
//public class AllotListPresenter extends BasePresenter{
//    @Inject
//    IStudentModel studentModel;
//    @Inject
//    GymWrapper gymWrapper;
//    @Inject
//    public AllotListPresenter(){
//
//    }
//    public void loadSellerData(){
//
//    }
//    public void loadCoachData(){
//        HashMap<String, Object> params = gymWrapper.getParams();
//        RxRegiste(studentModel
//                .qcGetCoachList("", params).onBackpressureBuffer().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<QcDataResponse<AllotDataResponseWrap>>() {
//                    @Override public void call(QcDataResponse<AllotDataResponseWrap> allotSalePreViewsQcResponseData) {
//                        if (allotSalePreViewsQcResponseData.status == 200) {
//                            view.onCoachessPreview(allotSalePreViewsQcResponseData.data.coaches);
//                        } else {
//                            view.onShowError(allotSalePreViewsQcResponseData.getMsg());
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override public void call(Throwable throwable) {
//                        Timber.e(throwable.getMessage());
//                        view.onShowError(throwable.getMessage());
//                    }
//                }));
//    }
//    public interface MVPView extends PView{
//        void onDataList(List<AllotDataResponse> datas);
//        void onLoadFinish();
//
//    }
//}
