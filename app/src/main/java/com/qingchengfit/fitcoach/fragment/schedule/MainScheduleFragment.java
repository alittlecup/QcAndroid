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
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.NotificationActivity;
import com.qingchengfit.fitcoach.bean.NewPushMsg;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcNotificationResponse;
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

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_schedule, container, false);
        getFragmentManager().beginTransaction().replace(R.id.schedule_frag, new ScheduesFragment()).commitAllowingStateLoss();
        ButterKnife.bind(this, view);
        RxBusAdd(NewPushMsg.class)
            .subscribe(new Action1<NewPushMsg>() {
                @Override public void call(NewPushMsg newPushMsg) {
                    queryNotify();
                }
            });
        return view;
    }

    @Override public void onStart() {
        super.onStart();
        queryNotify();

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
                            if (qcNotificationResponse.getData().getUnread_count() < 100)
                                scheduleNotificationCount.setText(Integer.toString(qcNotificationResponse.getData().getUnread_count()));
                            else scheduleNotificationCount.setText("99");
                            scheduleNotificationCount.setVisibility(View.VISIBLE);
                        } else {
                            scheduleNotificationCount.setVisibility(View.GONE);
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
                toChooseGym.putExtra("to", ChooseActivity.TO_CHOSSE_GYM);
                startActivityForResult(toChooseGym, 1);
                break;
            case R.id.student_order://会员预约
                break;
            case R.id.schedule_notification_layout:
                Intent toNotification  = new Intent(getActivity(), NotificationActivity.class);
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
