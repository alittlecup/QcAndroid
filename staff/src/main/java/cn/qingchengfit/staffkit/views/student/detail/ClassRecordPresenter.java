package cn.qingchengfit.staffkit.views.student.detail;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.responese.ClassRecords;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.StudentUsecase;
import cn.qingchengfit.utils.StringUtils;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/19 2016.
 */

public class ClassRecordPresenter extends BasePresenter {
    @Inject StudentWrapper studentBase;

    @Inject StudentUsecase usecase;
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private ClassRecordView view;

    @Inject public ClassRecordPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (ClassRecordView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
    }

    public void queryData(String shopid) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("brand_id", gymWrapper.brand_id());
        if (StringUtils.isEmpty(shopid)) {
            params.put("shop_ids", 0);
        } else {
            params.put("shop_ids", shopid);
        }

        RxRegiste(restRepository.getGet_api()

            .qcGetStudentClassRecords(App.staffId, studentBase.id(), params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<ClassRecords>>() {
                @Override public void call(QcDataResponse<ClassRecords> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.data.attendances != null && qcResponse.data.stat != null && qcResponse.data.shops != null) {
                            view.onData(qcResponse.getData().attendances, qcResponse.getData().stat, qcResponse.getData().shops);
                        }
                    }
                    ;
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            }));

        //usecase.queryClassRecords(studentBase.getId(), coachService.getId(), coachService.getModel(), brand.getId(), new Action1<QcResponseData<ClassRecords>>() {
        //    @Override
        //    public void call(QcResponseData<ClassRecords> qcResponseClassRecords) {
        //        int lastyear = 0,lastMonth = 0;
        //        List<StatementBean> d = new ArrayList<StatementBean>();
        //        int private_count=0,group_count=0,lastHeaderpos=0,i=0;
        //
        //        for (Schedule schedule : qcResponseClassRecords.data.schedules) {
        //            int year = DateUtils.getYear(DateUtils.formatDateFromServer(schedule.start));
        //            int month = DateUtils.getMonth(DateUtils.formatDateFromServer(schedule.start));
        //            boolean showHeader = false, showBottom = false;
        //            if (lastyear != year  || lastMonth != month)  {
        //                lastyear = year;
        //                lastMonth = month;
        //                if (d.size() >lastHeaderpos)
        //                    d.get(lastHeaderpos).month_data =String.format(Locale.CHINA,"%d节团课,%d节私教",group_count,private_count);
        //                lastHeaderpos = i;
        //                showHeader = true;
        //                private_count = group_count =0;
        //            }
        //            if (i == qcResponseClassRecords.data.schedules.size() -1){
        //                if (d.size() >lastHeaderpos)
        //                    d.get(lastHeaderpos).month_data =String.format(Locale.CHINA,"%d节团课,%d节私教",group_count,private_count);
        //
        //            }
        //
        //
        //            if (schedule.course.is_private)
        //                private_count++;
        //            else group_count++;
        //            String start = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.start));
        //            String end = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.end));
        //            d.add(new StatementBean(DateUtils.formatDateFromServer(schedule.start), schedule.course.photo, schedule.course.name
        //                    , start + "-" + end + "   教练:" + schedule.teacher.username, false, false, showHeader, schedule.url,""));
        //            i++;
        //        }
        //        view.onData(d);
        //    }
        //});
    }
}
