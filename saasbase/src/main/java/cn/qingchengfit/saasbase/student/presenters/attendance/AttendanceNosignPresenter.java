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
import cn.qingchengfit.saasbase.student.bean.StudentWIthCount;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AttendanceNosignPresenter extends BasePresenter<AttendanceNosignPresenter.MVPView> {
    @Inject
    IStudentModel studentModel;

    @Inject
    GymWrapper gymWrapper;

    @Inject
    public AttendanceNosignPresenter() {
    }

    public void getNotSignStudent(String staff_id, String start, String end, int limit) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("start", start);
        params.put("end", end);
        params.put("limit", limit);
        RxRegiste(studentModel
                .qcGetNotSignStudent(staff_id, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listQcDataResponse -> {
                    if (ResponseConstant.checkSuccess(listQcDataResponse)) {
                        mvpView.onGetNotSign(listQcDataResponse.data);
                    } else {
                        mvpView.onShowError(listQcDataResponse.getMsg());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mvpView.onShowError(throwable.getMessage());
                    }
                }));
    }


    public interface MVPView extends CView {
        void onGetNotSign(List<StudentWIthCount> attendances);
    }
}
