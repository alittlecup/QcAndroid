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
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.GuideWindow;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.NotificationActivity;
import com.qingchengfit.fitcoach.event.EventGoPreview;
import com.qingchengfit.fitcoach.event.EventInit;
import com.qingchengfit.fitcoach.event.EventScheduleService;
import com.qingchengfit.fitcoach.event.EventScheduleView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
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
    @BindView(R.id.view_p) View viewP;

    private CoachService mCoachService;
    private boolean isWeekView;
    private ScheduleWeekFragment scheduleWeekFragment;
    private ScheduesFragment scheduesFragment;
    private GuideWindow gd1;
    private GuideWindow gd2;

    public CoachService getCoachService() {
        return mCoachService;
    }

    @Override protected void onFinishAnimation() {
        super.onFinishAnimation();
        scheduleWeekFragment = new ScheduleWeekFragment();
        scheduesFragment = new ScheduesFragmentBuilder(new Date().getTime()).build();

        stuff(isWeekView ? scheduleWeekFragment : scheduesFragment);
        setVisible();
    }

    @Override public int getLayoutRes() {
        return R.id.schedule_frag;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_schedule, container, false);
        unbinder = ButterKnife.bind(this, view);
        isWeekView = PreferenceUtils.getPrefBoolean(getContext(), "is_week_view", false);
        RxBusAdd(EventGoPreview.class).subscribe(new Action1<EventGoPreview>() {
            @Override public void call(EventGoPreview eventGoPreview) {
                goStudentPreview(eventGoPreview.mCoachService);
            }
        });
        RxBusAdd(EventScheduleService.class).subscribe(new Action1<EventScheduleService>() {
            @Override public void call(EventScheduleService eventScheduleService) {
                if (eventScheduleService.mCoachService.getId() == 0) {
                    mCoachService = null;
                    title.setText(getString(R.string.all_schedules));
                } else {
                    mCoachService = eventScheduleService.mCoachService;
                    title.setText(mCoachService.getName());
                }
            }
        });
        //跳转到日视图或者周视图的某天
        RxBusAdd(EventScheduleView.class).subscribe(new Action1<EventScheduleView>() {
            @Override public void call(EventScheduleView eventScheduleView) {
                if (eventScheduleView.isWeekView) {

                } else {
                    getChildFragmentManager().beginTransaction()
                        .replace(R.id.schedule_frag, new ScheduesFragmentBuilder(eventScheduleView.mDate.getTime()).build())
                        .commitAllowingStateLoss();
                }
            }
        });
        RxBusAdd(EventInit.class).subscribe(new Action1<EventInit>() {
            @Override public void call(EventInit eventInit) {
                if (isVisible() && !eventInit.show && eventInit.pos == 2) {
                    if (gd2 != null && gd2.isShowing()) gd2.dismiss();
                    gd2 = null;
                    PreferenceUtils.setPrefBoolean(getContext(), "guide_2", true);
                }
            }
        });

        return view;
    }

    public void setVisible() {
        if (!PreferenceUtils.getPrefBoolean(getContext(), "guide_1", false)) {
            if (gd1 == null) gd1 = new GuideWindow(getContext(), getString(R.string.hint_order_self), GuideWindow.DOWN);
            gd1.show(studentOrder);
        } else if (!PreferenceUtils.getPrefBoolean(getContext(), "guide_2", false)) {
            if (gd2 == null) gd2 = new GuideWindow(getContext(), getString(R.string.hint_help_order), GuideWindow.UP);
            gd2.show(viewP);
        }
    }

    public void setInvisible() {
        if (gd1 != null && gd1.isShowing()) gd1.dismiss();
        if (gd2 != null && gd2.isShowing()) gd2.dismiss();
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
            hideHint();
            goStudentPreview(mCoachService);
        } catch (Exception e) {

        }
    }

    private void hideHint() {
        if (gd1 != null && gd1.isShowing()) gd1.dismiss();
        gd1 = null;
        PreferenceUtils.setPrefBoolean(getContext(), "guide_1", true);
        if (PreferenceUtils.getPrefBoolean(getContext(), "guide_1", false) && !PreferenceUtils.getPrefBoolean(getContext(), "guide_2",
            false)) {
            if (gd2 == null) {
                gd2 = new GuideWindow(getContext(), getString(R.string.hint_help_order), GuideWindow.UP);
            } else if (viewP != null && !gd2.isShowing()) gd2.show(viewP);
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
                toChooseGym.putExtra("service", mCoachService);
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

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
