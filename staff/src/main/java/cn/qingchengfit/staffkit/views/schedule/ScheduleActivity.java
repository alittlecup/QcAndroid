package cn.qingchengfit.staffkit.views.schedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.ScheduleAction;
import cn.qingchengfit.model.responese.ScheduleActions;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import cn.qingchengfit.staffkit.views.custom.DatePicker;
import cn.qingchengfit.staffkit.views.custom.PagerSlidingTabStrip;
import cn.qingchengfit.staffkit.views.custom.RobotoCalendarView;
import cn.qingchengfit.staffkit.views.gym.ChooseGymFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
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
 * Created by Paper on 16/2/23 2016.
 */
public class ScheduleActivity extends BaseActivity implements FragCallBack {

    private static final int REQUEST_CHOOSE_GYM = 1;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.schedule_notification) ImageView scheduleNotification;
    @BindView(R.id.schedule_notification_count) TextView scheduleNotificationCount;
    @BindView(R.id.schedule_notification_layout) RelativeLayout scheduleNotificationLayout;
    @BindView(R.id.schedule_calendar_ic) ImageView scheduleCalendarIc;
    @BindView(R.id.schedule_calendar) RelativeLayout scheduleCalendar;
    @BindView(R.id.schedule_tab) PagerSlidingTabStrip scheduleTab;
    @BindView(R.id.schedule_calendar_header) LinearLayout scheduleCalendarHeader;
    @BindView(R.id.schedule_expend_view) LinearLayout scheduleExpendView;
    @BindView(R.id.schedule_vp) ViewPager scheduleVp;
    @BindView(R.id.schedule_floatbg) LinearLayout scheduleFloatbg;
    @BindView(R.id.web_floatbtn) FloatingActionsMenu webFloatbtn;
    @BindView(R.id.half_bg) View halfBg;
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    private FragmentAdapter mFragmentAdapter;
    private DatePicker mDatePicker;
    private MaterialDialog mAlert;
    private String mChooseShopId;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setNavigationIcon(R.drawable.ic_triangle_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        setUpViewPager();

        FloatingActionButton btn1 = new FloatingActionButton(this);
        btn1.setIcon(R.drawable.ic_action_rest);
        btn1.setColorNormal(getResources().getColor(R.color.green));
        btn1.setTitle(getString(R.string.set_rest));
        //
        FloatingActionButton btn2 = new FloatingActionButton(this);
        btn2.setIcon(R.drawable.ic_action_group);
        btn2.setColorNormal(getResources().getColor(R.color.blue));
        btn2.setTitle(getString(R.string.help_order_group));

        FloatingActionButton btn3 = new FloatingActionButton(this);
        btn3.setIcon(R.drawable.ic_action_private);
        btn3.setColorNormal(getResources().getColor(R.color.purple));
        btn3.setTitle(getString(R.string.help_order_private));
        webFloatbtn.addButton(btn1);
        webFloatbtn.addButton(btn2);
        webFloatbtn.addButton(btn3);
        webFloatbtn.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override public void onMenuExpanded() {
                halfBg.setAlpha(0f);
                halfBg.setVisibility(View.VISIBLE);
                ViewCompat.animate(halfBg).alpha(1f).setDuration((long) getResources().getInteger(R.integer.anim_time)).start();
            }

            @Override public void onMenuCollapsed() {
                ViewCompat.animate(halfBg).alpha(0f).setDuration((long) getResources().getInteger(R.integer.anim_time)).start();
                halfBg.setVisibility(View.GONE);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onAction(1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onAction(3);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onAction(2);
            }
        });

        if (gymWrapper.inBrand()) {
            toolbarTitle.setText(R.string.all_gyms);
            toolbarTitle.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onChangeGym();
                }
            });
        } else {
            toolbarTitle.setCompoundDrawables(null, null, null, null);
            toolbarTitle.setText("课程预约");
        }
    }

    @OnClick(R.id.half_bg) public void onBgClick() {
        webFloatbtn.collapse();
    }

    public void onAction(int v) {
        String action = "";
        webFloatbtn.collapse();
        boolean isPrivate = false;
        switch (v) {
            case 1:
                action = "rest";
                break;
            case 2:
                action = "privatelesson";
                if (!serPermisAction.checkAtLeastOne(PermissionServerUtils.ORDERS_DAY_CAN_WRITE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return;
                }
                isPrivate = true;
                break;
            case 3:
                if (!serPermisAction.checkAtLeastOne(PermissionServerUtils.ORDERS_DAY_CAN_WRITE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return;
                }
                action = "grouplesson";
                isPrivate = false;
                break;
        }

        final boolean finalP = isPrivate;
        final String finalAction = action;
        restRepository.getGet_api()
            .qcGetScheduleAciton(App.staffId, action, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<ScheduleActions>>() {
                @Override public void call(QcDataResponse<ScheduleActions> qcRepsonseScheduleAction) {
                    if (qcRepsonseScheduleAction.getStatus() == ResponseConstant.SUCCESS) {
                        final List<ScheduleAction> gyms = qcRepsonseScheduleAction.data.services;
                        if (gyms.size() == 1) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(mFragmentAdapter.getCurDay());
                            calendar.add(Calendar.DAY_OF_MONTH, scheduleVp.getCurrentItem() - 30);
                            WebActivity.startWebForResult(gyms.get(0).getUrl() + "date=" + DateUtils.Date2YYYYMMDD(calendar.getTime()),
                                ScheduleActivity.this, 404);
                        } else if (gyms.size() > 1) {
                            ArrayList<ImageTwoTextBean> d = new ArrayList<ImageTwoTextBean>();
                            for (ScheduleAction scheduleService : gyms) {
                                ImageTwoTextBean bean = new ImageTwoTextBean(scheduleService.getPhoto(), scheduleService.getName(),
                                    scheduleService.getBrand_name());
                                bean.hiden =
                                    serPermisAction.check(scheduleService.getShop_id(), PermissionServerUtils.ORDERS_DAY_CAN_WRITE);
                                if (finalAction.equalsIgnoreCase("rest")) bean.hiden = false;
                                d.add(bean);
                            }

                            ChooseGymFragment.start(null, ScheduleActivity.this, d, 999, new AdapterView.OnItemClickListener() {
                                @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(mFragmentAdapter.getCurDay());
                                    calendar.add(Calendar.DAY_OF_MONTH, scheduleVp.getCurrentItem() - 30);
                                    //                                        sb.append("&").append("date=").append(DateUtils.Date2YYYYMMDD(calendar.getTime()));
                                    WebActivity.startWebForResult(
                                        gyms.get(position).getUrl() + "date=" + DateUtils.Date2YYYYMMDD(calendar.getTime()),
                                        ScheduleActivity.this, 404);
                                }
                            });
                        } else {
                            //                                // ToastUtils.logHttp(qcRepsonseScheduleAction);
                        }
                    } else {

                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    @OnClick(R.id.schedule_calendar) public void onCalendar() {
        scheduleCalendar.setClickable(false);
        if (mDatePicker == null) {
            mDatePicker = new DatePicker(this);
            mDatePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override public void onDismiss(DialogInterface dialog) {
                    scheduleCalendar.setClickable(true);
                }
            });
            mDatePicker.setDayClickListener(new RobotoCalendarView.RobotoCalendarListener() {

                @Override public void onDateSelected(Date date) {
                    goDateSchedule(date);
                    mDatePicker.dismiss();
                }

                @Override public void onRightButtonClick() {
                    mDatePicker.addMonth();
                }

                @Override public void onLeftButtonClick() {
                    mDatePicker.minlusMonth();
                }
            });
        }
        if (mDatePicker.isShowing()) {
            mDatePicker.hide();
        } else {
            mDatePicker.show();
        }
    }

    private void goDateSchedule(Date date) {
        mFragmentAdapter.setCurCenterDay(date);
        mFragmentAdapter.notifyDataSetChanged();
        scheduleTab.notifyDataSetChanged();
        scheduleVp.setCurrentItem(30, false);
    }

    public void onChangeGym() {
        ChooseGymActivity.start(this, REQUEST_CHOOSE_GYM, getString(R.string.choose_gym), mChooseShopId);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_GYM) {
                //toolbarTitle.setText(IntentUtils.getIntentString(data, 0));
                //mChooseShopId = IntentUtils.getIntentString(data, 1);
                //String CurGymId = IntentUtils.getIntentString(data, 2);
                //String CurGymModel = IntentUtils.getIntentString(data, 3);
                //if (!TextUtils.isEmpty(mChooseShopId)) {
                //gymBaseInfoAction.getGymByModel(CurGymId, CurGymModel)
                //        .subscribe(new Action1<List<CoachService>>() {
                //            @Override
                //            public void call(List<CoachService> coachServices) {
                //                if (coachServices != null && coachServices.size() > 0) {
                //                    mCurService = coachServices.get(0);
                //                    RxBus.getBus().post(coachServices.get(0));
                //                }
                //            }
                //        });
                //} else {
                //    RxBus.getBus().post(mCoachService);
                //}
            } else if (requestCode == 404) {
                mFragmentAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setUpViewPager() {
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        scheduleVp.setAdapter(mFragmentAdapter);
        scheduleVp.setOffscreenPageLimit(1);
        scheduleVp.setCurrentItem(30, false);
        scheduleTab.setViewPager(scheduleVp);
    }

    public <T> T getComponent() {
        return null;
    }

    @Override public int getFragId() {
        return 0;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {

    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {

    }

    public void showAlert(String content) {
        if (mAlert == null) ;
        mAlert =
            new MaterialDialog.Builder(this).positiveText(R.string.common_i_konw).autoDismiss(true).canceledOnTouchOutside(true).build();
        if (mAlert.isShowing()) mAlert.dismiss();
        mAlert.setContent(content);
        mAlert.show();
    }

    public void showAlert(@StringRes int content) {
        showAlert(getString(content));
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter {

        private String[] weekDays = new String[] {
            "周日", "周一", "周二", "周三", "周四", "周五", "周六"
        };
        private Date curDate = new Date();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setCurCenterDay(Date day) {
            curDate = day;
        }

        public Date getCurDay() {
            return curDate;
        }

        @Override public Fragment getItem(int position) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);
            calendar.add(Calendar.DAY_OF_MONTH, position - 30);
            return ScheduleListFragment.newInstance(DateUtils.Date2YYYYMMDD(calendar.getTime()));
        }

        @Override public int getCount() {
            return 60;
        }

        @Override public CharSequence getPageTitle(int position) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);
            calendar.add(Calendar.DAY_OF_MONTH, position - 30);
            StringBuffer sb = new StringBuffer();
            sb.append(weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
            sb.append("\n");
            sb.append(calendar.get(Calendar.MONTH) + 1);
            sb.append(".");
            sb.append(calendar.get(Calendar.DAY_OF_MONTH));
            return sb.toString();
        }

        @Override public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
