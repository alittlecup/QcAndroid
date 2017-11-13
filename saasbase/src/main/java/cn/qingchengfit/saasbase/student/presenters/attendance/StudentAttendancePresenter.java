package cn.qingchengfit.saasbase.student.presenters.attendance;

import android.text.TextUtils;

import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.network.body.AttendanceCharDataBean;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.utils.DateUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/11/13.
 */

public class StudentAttendancePresenter extends BasePresenter<StudentAttendancePresenter.MVPView> {
    @Inject
    IStudentModel studentModel;

    @Inject
    GymWrapper gymWrapper;

    @Inject
    public StudentAttendancePresenter() {
    }

    public void loadAttendanceData(String staff_id, String start, String end) {
        HashMap<String, Object> params = gymWrapper.getParams();
        if (StringUtils.isEmpty(start)) {
            start = DateUtils.minusDay(new Date(), 6);
        }
        if (StringUtils.isEmpty(end)) {
            end = DateUtils.getStringToday();
        }

        params.put("start", start);
        params.put("end", end);
        RxRegiste(studentModel
                .qcGetAttendanceChart(staff_id, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(absentcesQcResponseData -> {
                    if (ResponseConstant.checkSuccess(absentcesQcResponseData)) {
                        mvpView.onAbsence(absentcesQcResponseData.data);
                    } else {
                        mvpView.onShowError(absentcesQcResponseData.getMsg());
                    }
                }, throwable -> mvpView.onShowError(throwable.getMessage())));

    }

    public interface MVPView extends CView {
        void onAbsence(AttendanceCharDataBean statistic);

    }
}
