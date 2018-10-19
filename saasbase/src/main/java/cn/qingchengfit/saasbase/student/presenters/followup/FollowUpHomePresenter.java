package cn.qingchengfit.saasbase.student.presenters.followup;

import cn.qingchengfit.saascommon.model.FollowUpDataStatistic;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.DateUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/11/3.
 */

public class FollowUpHomePresenter extends BasePresenter<FollowUpHomePresenter.MVPView> {

    @Inject
    GymWrapper gymWrapper;
    @Inject
    IStudentModel studentModel;

    @Inject
    public FollowUpHomePresenter() {
    }

    public void getStudentsStatistics(String staffId, StudentFilter filter) {
        HashMap<String, Object> params = gymWrapper.getParams();

        if(null!=filter){
            if (!StringUtils.isEmpty(filter.registerTimeStart) && !StringUtils.isEmpty(filter.registerTimeEnd)) {
                params.put("start", filter.registerTimeStart);
                params.put("end", filter.registerTimeEnd);
            }
            if (filter.sale != null && !"-1".equals(filter.sale.id)) {// -1是全部
                params.put("seller_id", filter.sale.id);//无销售seller_id=0
            }
        }else{
            params.put("start", DateUtils.minusDay(new Date(), 29));
            params.put("end", DateUtils.getStringToday());
        }
        RxRegiste(studentModel
                .qcGetTrackStudentsStatistics(staffId, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trackStudentsQcResponseData -> mvpView.onFollowUpStatistics(trackStudentsQcResponseData.data),
                        throwable -> mvpView.onShowError(throwable.getMessage())));
    }

    public interface MVPView extends CView {
        void onFollowUpStatistics(FollowUpDataStatistic statistics);

    }
}
