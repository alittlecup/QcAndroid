package cn.qingchengfit.student.view.followup;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Staff;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/11/8.
 */

public class FollowUpSalersPresenter extends BasePresenter<FollowUpSalersPresenter.MVPView> {
    //@Inject
    //IStudentModel studentModel;
    @Inject
    GymWrapper gymWrapper;

    @Inject
    public FollowUpSalersPresenter() {

    }

    public void getFilterSelers(String staff_id) {
        HashMap<String, Object> params = gymWrapper.getParams();
        //RxRegiste(studentModel
        //        .qcGetTrackStudentsFilterSalers(staff_id, params)
        //        .onBackpressureBuffer()
        //        .subscribeOn(Schedulers.io())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(new Action1<QcDataResponse<SalerListWrap>>() {
        //            @Override public void call(QcDataResponse<SalerListWrap> trackSellersQcResponseData) {
        //                mvpView.onSalers(trackSellersQcResponseData.data.sellers);
        //            }
        //        }, new Action1<Throwable>() {
        //            @Override public void call(Throwable throwable) {
        //                mvpView.onShowError(throwable.getMessage());
        //            }
        //        }));
    }


    public interface MVPView extends CView {

        void onSalers(List<Staff> sellers);
    }
}
