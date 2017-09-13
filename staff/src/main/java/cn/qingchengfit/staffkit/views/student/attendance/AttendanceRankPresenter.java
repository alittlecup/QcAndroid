package cn.qingchengfit.staffkit.views.student.attendance;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.common.Attendance;
import cn.qingchengfit.model.common.Attendances;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AttendanceRankPresenter extends BasePresenter {

    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private int rankType = 0;
    private boolean revert = false;//是否逆序
    private String start, end;
    private int curpage = 1, pages = 1;
    private List<Attendance> attendanceList = new ArrayList<>();

    @Inject public AttendanceRankPresenter() {
    }

    public void refresh() {
        curpage = 1;
        pages = 1;
        if (view != null) view.clearDatas();
        getRank();
    }

    public void setParams(String start, String end, int rankType, boolean revert) {
        this.start = start;
        this.end = end;
        this.rankType = rankType;
        this.revert = revert;
        curpage = 1;
        pages = 1;
        if (view != null) view.clearDatas();
        getRank();
    }

    /**
     * 获取排行  必须先设置 setParams
     */
    public void getRank() {
        HashMap<String, Object> params = gymWrapper.getParams();
        String orderStr = revert ? "" : "-";
        switch (rankType) {
            case 1:
                orderStr = orderStr.concat("checkin");
                break;
            case 2:
                orderStr = orderStr.concat("group");
                break;
            case 3:
                orderStr = orderStr.concat("private");
                break;
            default:
                orderStr = orderStr.concat("days");
                break;
        }
        params.put("order_by", orderStr);
        params.put("start", start);
        params.put("end", end);
        params.put("show_all", 1);
        if (curpage <= pages) {
            RxRegiste(restRepository.getGet_api()
                .qcGetUsersAttendances(App.staffId, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcDataResponse<Attendances>>() {
                    @Override public void call(QcDataResponse<Attendances> qcResponse) {
                        if (ResponseConstant.checkSuccess(qcResponse)) {

                            attendanceList.addAll(qcResponse.getData().attendances);
                            pages = qcResponse.data.pages;
                            view.onAttendances(qcResponse.getData().attendances, 1, qcResponse.data.pages, qcResponse.data.total_count);
                            curpage++;
                        } else {
                            view.onShowError(qcResponse.getMsg());
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

    public StudentBean handleStudentBean(int position) {
        StudentBean bean = new StudentBean();
        Student student = attendanceList.get(position).student;
        bean.id = student.getId();
        bean.username = student.username;
        bean.avatar = student.avatar;
        bean.phone = student.phone;
        return bean;
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
        void onAttendances(List<Attendance> attendances, int curpage, int maxPages, int pages);

        void onNoMore();

        void clearDatas();
    }
}
