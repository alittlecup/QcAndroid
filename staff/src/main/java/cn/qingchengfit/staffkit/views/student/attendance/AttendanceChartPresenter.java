package cn.qingchengfit.staffkit.views.student.attendance;

import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.AttendanceCharDataBean;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.DateUtils;
import java.util.Date;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/3/8.
 */

public class AttendanceChartPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    private AttendanceView view;
    private int pages;
    private int curpage;

    @Inject public AttendanceChartPresenter() {

    }

    public void refreshData(String start, String end) {
        curpage = 1;
        pages = 1;
        if (view != null) view.clearDatas();
        queryChartData(start, end);
    }

    @Override public void attachView(PView v) {
        view = (AttendanceView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryChartData(String start, String end) {
        HashMap<String, Object> params = gymWrapper.getParams();
        if (TextUtils.isEmpty(start)) {
            start = DateUtils.minusDay(new Date(), 6);
        }
        if (TextUtils.isEmpty(end)) {
            end = DateUtils.getStringToday();
        }

        params.put("start", start);
        params.put("end", end);
        if (curpage <= pages) {
            RxRegiste(restRepository.getGet_api()
                .qcGetAttendanceChart(App.staffId, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseData<AttendanceCharDataBean>>() {
                    @Override public void call(QcResponseData<AttendanceCharDataBean> absentcesQcResponseData) {
                        if (ResponseConstant.checkSuccess(absentcesQcResponseData)) {
                            view.onAbsence(absentcesQcResponseData.data);
                            pages = absentcesQcResponseData.pages;
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

    public interface AttendanceView extends CView {
        void onAbsence(AttendanceCharDataBean statistic);

        void onNoMore();

        void clearDatas();
    }
}
