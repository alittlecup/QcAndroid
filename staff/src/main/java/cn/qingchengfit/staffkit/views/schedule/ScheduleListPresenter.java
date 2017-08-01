package cn.qingchengfit.staffkit.views.schedule;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.QcSchedulesResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.bean.QcScheduleBean;
import cn.qingchengfit.staffkit.usecase.bean.ScheduleBean;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ScheduleCompare;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/2 2016.
 */
public class ScheduleListPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private ScheduleListView view;
    private Subscription sp;
    private RestRepository restRepository;

    @Inject public ScheduleListPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (ScheduleListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (sp != null) sp.unsubscribe();
    }

    public void queryOneSchedule(final String id, String date) {
        RxRegiste(restRepository.getGet_api()
            .qcGetSchedules(id, date, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcSchedulesResponse>() {
                @Override public void call(QcSchedulesResponse qcSchedulesResponse) {
                    if (ResponseConstant.checkSuccess(qcSchedulesResponse)) {
                        List<ScheduleBean> scheduleBeans = new ArrayList<ScheduleBean>();
                        if (qcSchedulesResponse.data.rests != null) {
                            for (int j = 0; j < qcSchedulesResponse.data.rests.size(); j++) {
                                QcSchedulesResponse.Rest rest = qcSchedulesResponse.data.rests.get(j);
                                ScheduleBean bean = new ScheduleBean();
                                bean.type = 0;
                                bean.time = DateUtils.formatDateFromServer(rest.start).getTime();
                                bean.timeEnd = DateUtils.formatDateFromServer(rest.end).getTime();
                                bean.teacher = rest.teacher.username;
                                try {
                                    bean.gymname = rest.shop.name;
                                } catch (Exception e) {

                                }
                                bean.intent_url = rest.url;

                                scheduleBeans.add(bean);
                            }
                        }
                        if (qcSchedulesResponse.data.schedules != null) {
                            for (int k = 0; k < qcSchedulesResponse.data.schedules.size(); k++) {
                                QcScheduleBean schedule = qcSchedulesResponse.data.schedules.get(k);
                                ScheduleBean bean = new ScheduleBean();
                                bean.type = 1;

                                if (schedule.orders != null && schedule.orders.size() == 1) {
                                    bean.isSingle = true;
                                    bean.users = schedule.orders.get(0).username;
                                } else {
                                    bean.isSingle = false;
                                }
                                bean.gymname = schedule.shop.name;
                                //                        bean.color = syscolor;
                                bean.time = DateUtils.formatDateFromServer(schedule.start).getTime();
                                bean.timeEnd = DateUtils.formatDateFromServer(schedule.end).getTime();
                                bean.count = schedule.count;
                                bean.pic_url = schedule.course.photo;
                                bean.title = schedule.course.name;
                                bean.intent_url = schedule.url;
                                bean.teacher = schedule.teacher.username;
                                if (!TextUtils.isEmpty(gymWrapper.shop_id()) && !gymWrapper.shop_id().equalsIgnoreCase(schedule.shop.id)) {
                                    continue;
                                }
                                scheduleBeans.add(bean);
                            }
                        }
                        //                }
                        Collections.sort(scheduleBeans, new ScheduleCompare());
                        view.onGetData(scheduleBeans);
                    }else {
                        // TODO: 2017/9/13 错误处理
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }
}
