package cn.qingchengfit.staffkit.views.student.attendance;

import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.common.Absentce;
import cn.qingchengfit.model.common.Absentces;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/3/7.
 */

public class AbsenceListPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    private AbsenceView view;
    private int pages;
    private int curpage;
    private List<Absentce> attendanceList;

    @Inject public AbsenceListPresenter() {
    }

    public void refreshData(String start, String end) {
        curpage = 1;
        pages = 1;
        if (view != null) view.clearDatas();
        queryAbsenceList(start, end);
    }

    @Override public void attachView(PView v) {
        view = (AbsenceView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryAbsenceList(String start, String end) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params = handleData(params, start, end);
        params.put("show_all", 1);
        if (curpage <= pages) {
            RxRegiste(restRepository.getGet_api()
                .qcGetUsersAbsences(App.staffId, params).onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseData<Absentces>>() {
                    @Override public void call(QcResponseData<Absentces> absentcesQcResponseData) {
                        if (ResponseConstant.checkSuccess(absentcesQcResponseData)) {

                            attendanceList = absentcesQcResponseData.data.attendances;
                            pages = absentcesQcResponseData.data.pages;
                            view.onAbsence(absentcesQcResponseData.data.attendances, curpage, absentcesQcResponseData.data.total_count);
                            curpage++;
                        } else {
                            view.onShowError(absentcesQcResponseData.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        view.onShowError(throwable.getMessage());
                    }
                }));
        } else {
            view.onNoMore();
        }
    }

    //根据条件处理参数
    private HashMap<String, Object> handleData(HashMap<String, Object> hashMap, String startStr, String endStr) {

        if (TextUtils.isEmpty(startStr) || TextUtils.isEmpty(endStr)) {
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

    public StudentBean handleStudentBean(int position) {
        StudentBean bean = new StudentBean();
        Student student = attendanceList.get(position).user;
        bean.id = student.getId();
        bean.username = student.username;
        bean.avatar = student.avatar;
        bean.phone = student.phone;
        return bean;
    }

    public interface AbsenceView extends CView {
        void onAbsence(List<Absentce> attendances, int curPage, int totalPage);

        void onNoMore();

        void clearDatas();
    }
}
