package com.qingchengfit.fitcoach.fragment.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.NotificationActivity;
import com.qingchengfit.fitcoach.bean.NewPushMsg;
import com.qingchengfit.fitcoach.event.EventGoPreview;
import com.qingchengfit.fitcoach.event.EventInit;
import com.qingchengfit.fitcoach.event.EventScheduleService;
import com.qingchengfit.fitcoach.event.EventScheduleView;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcNotificationResponse;
import java.util.Date;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/11/23.
 */
public class MainScheduleFragment extends BaseFragment {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.layout_title) LinearLayout layoutTitle;
    @BindView(R.id.student_order) TextView studentOrder;
    @BindView(R.id.schedule_notification) ImageView scheduleNotification;
    @BindView(R.id.schedule_notification_count) TextView scheduleNotificationCount;
    @BindView(R.id.schedule_notification_layout) RelativeLayout scheduleNotificationLayout;
    @BindView(R.id.schedule_frag) FrameLayout scheduleFrag;

    private CoachService mCoachService;

    public CoachService getCoachService() {
        return mCoachService;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_schedule, container, false);

        boolean isWeekView = PreferenceUtils.getPrefBoolean(getContext(),"is_week_view",false);
        getChildFragmentManager().beginTransaction().replace(R.id.schedule_frag,isWeekView?new ScheduleWeekFragment() : new ScheduesFragmentBuilder(new Date().getTime()).build()).commitAllowingStateLoss();
        ButterKnife.bind(this, view);
        RxBusAdd(NewPushMsg.class).subscribe(new Action1<NewPushMsg>() {
            @Override public void call(NewPushMsg newPushMsg) {
                queryNotify();
            }
        });
        RxBusAdd(EventGoPreview.class).subscribe(new Action1<EventGoPreview>() {
            @Override public void call(EventGoPreview eventGoPreview) {
                goStudentPreview(eventGoPreview.mCoachService);
            }
        });
        RxBusAdd(EventScheduleService.class).subscribe(new Action1<EventScheduleService>() {
            @Override public void call(EventScheduleService eventScheduleService) {
                if (eventScheduleService.mCoachService.getId() == 0){
                    mCoachService = null;
                    title.setText(getString(R.string.all_schedules));

                }else {
                mCoachService = eventScheduleService.mCoachService;
                title.setText(mCoachService.getName());
                }
            }
        });
        //跳转到日视图或者周视图的某天
        RxBusAdd(EventScheduleView.class)
            .subscribe(new Action1<EventScheduleView>() {
                @Override public void call(EventScheduleView eventScheduleView) {
                    if (eventScheduleView.isWeekView){

                    }else
                        getChildFragmentManager().beginTransaction().replace(R.id.schedule_frag,new ScheduesFragmentBuilder(eventScheduleView.mDate.getTime()).build()).commitAllowingStateLoss();

                }
            });
        return view;
    }

    private void goStudentPreview(CoachService coachService) {
        Intent toStudnet = new Intent(getActivity(), SpecialWebActivity.class);
        String s = "";
        if (coachService != null) {
            s = s.concat("?id=").concat(coachService.getId() + "").concat("&model=").concat(coachService.getModel());
        }
        toStudnet.putExtra("url", Configs.HOST_STUDENT_PREVIEW + s);
        startActivity(toStudnet);
    }

    @Override public void onStart() {
        super.onStart();
        queryNotify();
    }

    @OnClick(R.id.student_order) public void onStuOrder() {
        try {

            RxBus.getBus().post(new EventInit(false, 1));
            //if (mCoachService == null) {
            //    new ChooseGymDialogFragment().show(getFragmentManager(), "");
            //} else {
            goStudentPreview(mCoachService);
            //}
        }catch (Exception e){

        }
    }

    public void queryNotify() {
        RxRegiste(QcCloudClient.getApi().getApi.qcGetMessages(App.coachid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<QcNotificationResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcNotificationResponse qcNotificationResponse) {
                    if (getActivity() != null) {
                        if (qcNotificationResponse.getData().getUnread_count() > 0) {
                            if (qcNotificationResponse.getData().getUnread_count() < 100) {
                                scheduleNotificationCount.setText(Integer.toString(qcNotificationResponse.getData().getUnread_count()));
                            } else {
                                scheduleNotificationCount.setText("99");
                            }
                            scheduleNotificationCount.setVisibility(View.VISIBLE);
                            //if (getActivity() instanceof Main2Activity){
                            //    ((Main2Activity) getActivity()).showIcon(0,true);
                            //}
                        } else {
                            scheduleNotificationCount.setVisibility(View.GONE);
                            //if (getActivity() instanceof Main2Activity){
                            //    ((Main2Activity) getActivity()).showIcon(0,false);
                            //}
                        }
                    }
                }
            }));
    }

    @Override public String getFragmentName() {
        return MainScheduleFragment.class.getName();
    }

    @OnClick({ R.id.layout_title, R.id.student_order, R.id.schedule_notification_layout }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_title://选择场馆
                Intent toChooseGym = new Intent(getContext(), ChooseActivity.class);
                toChooseGym.putExtra("to", ChooseActivity.TO_CHOSSE_GYM_SCHEDULE);
                toChooseGym.putExtra("service",mCoachService);
                startActivityForResult(toChooseGym, 1);
                break;
            case R.id.student_order://会员预约
                break;
            case R.id.schedule_notification_layout:
                Intent toNotification = new Intent(getActivity(), NotificationActivity.class);
                startActivity(toNotification);
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {

            }
        }
    }
}
