package cn.qingchengfit.saasbase.student.presenters.attendance;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.bean.Absentce;
import cn.qingchengfit.saascommon.utils.StringUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AttendanceStudentListPresenter extends BasePresenter<AttendanceStudentListPresenter.MVPView> {

    @Inject
    IStudentModel studentModel;
    @Inject
    GymWrapper gymWrapper;

    @Inject
    public AttendanceStudentListPresenter() {
    }

    public void queryAbsenceList(String staff_id, String start, String end) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params = handleData(params, start, end);
        params.put("show_all", 1);
        RxRegiste(studentModel
                .qcGetUsersAbsences(staff_id, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(absentcesQcResponseData -> {
                    if (ResponseConstant.checkSuccess(absentcesQcResponseData)) {
                        mvpView.onAbsence(absentcesQcResponseData.data.attendances, absentcesQcResponseData.data.total_count);
                    } else {
                        mvpView.onShowError(absentcesQcResponseData.getMsg());
                    }
                }, throwable -> mvpView.onShowError(throwable.getMessage())));
    }

    //根据条件处理参数
    private HashMap<String, Object> handleData(HashMap<String, Object> hashMap, String startStr, String endStr) {

        if (StringUtils.isEmpty(startStr) || StringUtils.isEmpty(endStr)) {
            return hashMap;
        }

        int start = Integer.valueOf(startStr);
        int end = Integer.valueOf(endStr);

        if (start == 7 && end == 30) {
            hashMap.put("absence__gte", "7");
            hashMap.put("absence__lte", "30");
            return hashMap;
        }

        if (start == 60 && end == -1) {
            hashMap.put("absence__gt", "60");
            return hashMap;
        }

        hashMap.put("absence__gte", start);
        hashMap.put("absence__lte", end);
        return hashMap;
    }

    public interface MVPView extends CView {

        void onAbsence(List<Absentce> attendances, int total_count);

    }
}
