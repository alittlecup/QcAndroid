package cn.qingchengfit.saasbase.student.presenters.attendance;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.bean.Attendance;
import cn.qingchengfit.saasbase.student.network.body.AttendanceListWrap;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AttendanceRankPresenter extends BasePresenter<AttendanceRankPresenter.MVPView> {
    @Inject
    IStudentModel studentModel;

    @Inject
    GymWrapper gymWrapper;

    @Inject
    public AttendanceRankPresenter() {
    }

    /**
     * 获取排行  必须先设置 setParams
     */
    public void getRank(String staffId, String start, String end, String sortType, boolean revert) {
        HashMap<String, Object> params = gymWrapper.getParams();
        String orderStr = (revert ? "" : "-").concat(sortType);
        params.put("order_by", orderStr);
        params.put("start", start);
        params.put("end", end);
        params.put("show_all", 1);
        RxRegiste(studentModel
                .qcGetUsersAttendances(staffId, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcResponse -> {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        mvpView.onAttendances(qcResponse.getData().attendances, qcResponse.data.total_count);
                    } else {
                        mvpView.onShowError(qcResponse.getMsg());
                    }
                }, throwable -> mvpView.onShowError(throwable.getMessage())));

    }

    public interface MVPView extends CView {
        void onAttendances(List<Attendance> attendances, int pages);
    }
}
