package cn.qingchengfit.staffkit.views.batch.looplist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.SingleBatchBody;
import cn.qingchengfit.model.responese.CourseManageBean;
import cn.qingchengfit.model.responese.CourseSchedule;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.views.batch.single.SingleBatchFragmentBuilder;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.TimePeriodChooser;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseManageFragment extends BaseFragment implements CourseManageView {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.del_layout) FrameLayout delLayout;
    @BindView(R.id.rootview) RelativeLayout rootview;
    @BindView(R.id.no_data) LinearLayout noData;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.btn_del) Button btnDel;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject CourseManagePresenter presenter;

    private String mBatchId; //排期id
    private List<CourseManageBean> datas = new ArrayList<>();
    private CourseManagerAdapter courseManagerAdapter;
    private List<Integer> delChooose = new ArrayList<>();
    private int mCourseType = Configs.TYPE_PRIVATE;
    private int chooseCount = 0; //选中的个数
    private TimeDialogWindow timeWindow;
    private TimePeriodChooser timeDialogWindow;
    private MaterialDialog delDialog;
  private Toolbar.OnMenuItemClickListener mEditLis = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            toolbar.getMenu().clear();
          toolbar.inflateMenu(R.menu.menu_cancel);
          toolbar.setOnMenuItemClickListener(mCancel);
          courseManagerAdapter.setEditing(true);
            courseManagerAdapter.notifyDataSetChanged();
            return true;
        }
    };
  private Toolbar.OnMenuItemClickListener mCancel = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            toolbar.getMenu().clear();
          toolbar.inflateMenu(R.menu.menu_edit);
          toolbar.setOnMenuItemClickListener(mEditLis);
          courseManagerAdapter.setEditing(false);
            courseManagerAdapter.notifyDataSetChanged();
            return true;
        }
    };

    public CourseManageFragment() {
    }

    public static CourseManageFragment newInstance(String Batchid, int coursetype) {

        Bundle args = new Bundle();
        args.putString("batchid", Batchid);
        args.putInt("courseid", coursetype);
        CourseManageFragment fragment = new CourseManageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBatchId = getArguments().getString("batchid");
            mCourseType = getArguments().getInt("courseid");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_manage, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        btnDel.setText(mCourseType == Configs.TYPE_PRIVATE ? "删除所选排期" : "删除所选课程");
        courseManagerAdapter = new CourseManagerAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(courseManagerAdapter);
        courseManagerAdapter.setClickTimeListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                CourseManageBean bean = datas.get(pos);
                if (v.getId() == R.id.time) {
                    if ((mCourseType == Configs.TYPE_GROUP && !SerPermisAction.check(gymWrapper.shop_id(),
                        PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE)) || (mCourseType == Configs.TYPE_PRIVATE
                        && !SerPermisAction.check(gymWrapper.shop_id(), PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE))) {
                        showAlert(R.string.alert_permission_forbid);
                        return;
                    }
                    chooseTime(pos);
                } else {
                    if (courseManagerAdapter.isEditing()) {
                        if (!bean.outdue) {
                            bean.checked = !bean.checked;
                            if (bean.checked) {
                                chooseCount++;
                            } else {
                                chooseCount--;
                            }
                            courseManagerAdapter.notifyItemChanged(pos);
                            if (chooseCount > 0) {
                                delLayout.setVisibility(View.VISIBLE);
                            } else {
                                delLayout.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        getFragmentManager().beginTransaction()
                            .replace(mCallbackActivity.getFragId(),
                                new SingleBatchFragmentBuilder(mCourseType == Configs.TYPE_PRIVATE, bean.id).build())
                            .addToBackStack(null)
                            .commit();
                    }
                }
            }
        });
        freshData();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        //mCallbackActivity.setToolbar("课程安排", false, null, R.menu.menu_edit, mEdits);
        toolbarTitile.setText(mCourseType == Configs.TYPE_PRIVATE ? "所有排期" : "所有课程");
        toolbar.inflateMenu(R.menu.menu_edit);
        toolbar.setOnMenuItemClickListener(mEditLis);
    }

    private void freshData() {
        presenter.queryList(mBatchId, mCourseType);
    }

    public void chooseTime(final int pos) {
        if (mCourseType == Configs.TYPE_GROUP) {
            if (timeWindow == null) {
              timeWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
            }

            timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override public void onTimeSelect(Date date) {
                    if (datas.get(pos).day.equalsIgnoreCase(DateUtils.Date2YYYYMMDD(new Date()))
                        && date.getTime() <= new Date().getTime()) {
                        ToastUtils.showDefaultStyle("开始时间不能小于当前时间");
                        return;
                    }
                    SingleBatchBody batchBean = new SingleBatchBody();
                    batchBean.model = gymWrapper.model();
                    batchBean.id = gymWrapper.id();
                    batchBean.start = datas.get(pos).day + "T" + DateUtils.getTimeHHMM(date) + ":00";
                    batchBean.end =
                        datas.get(pos).day + "T" + DateUtils.getTimeHHMM(new Date(date.getTime() + datas.get(pos).length)) + ":00";

                    presenter.updateSchedules(datas.get(pos).id, batchBean, mCourseType);
                }
            });
            timeWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, datas.get(pos).start);
        } else {
            if (timeDialogWindow == null) {
              timeDialogWindow =
                  new TimePeriodChooser(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
            }
            timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
                @Override public void onTimeSelect(Date start, Date end) {
                    if (start.getTime() >= end.getTime()) {
                        ToastUtils.showDefaultStyle("开始时间不能小于结束时间");
                        return;
                    }
                    if (datas.get(pos).day.equalsIgnoreCase(DateUtils.Date2YYYYMMDD(new Date()))
                        && start.getTime() <= new Date().getTime()) {
                        ToastUtils.showDefaultStyle("开始时间不能小于当前时间");
                        return;
                    }

                    SingleBatchBody batchBean = new SingleBatchBody();
                    batchBean.model = gymWrapper.model();
                    batchBean.id = gymWrapper.id();
                    batchBean.start = datas.get(pos).day + "T" + DateUtils.getTimeHHMM(start) + ":00";
                    batchBean.end = datas.get(pos).day + "T" + DateUtils.getTimeHHMM(end) + ":00";
                    presenter.updateSchedules(datas.get(pos).id, batchBean, mCourseType);
                }
            });
            timeDialogWindow.setTime(datas.get(pos).start, datas.get(pos).end);
            timeDialogWindow.showAtLocation();
        }
    }

    @OnClick(R.id.btn_del) public void OnDel() {
        if ((mCourseType == Configs.TYPE_GROUP && !SerPermisAction.check(gymWrapper.shop_id(),
            PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_DELETE)) || (mCourseType == Configs.TYPE_PRIVATE && !SerPermisAction.check(
            gymWrapper.shop_id(), PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_DELETE))) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
                .content("是否删除排期?")
                .positiveText("确定")
                .negativeText("取消")
                .cancelable(true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.delSchedules(courseManagerAdapter.getChooseIds(), mCourseType);
                    }
                })
                .build();
        }
        delDialog.show();
    }

    @Override public void onDestroyView() {
        RxBus.getBus().post(new EventFresh());
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onList(List<CourseSchedule> schedules) {
        datas.clear();
        int pos = 0;
        for (CourseSchedule schedule : schedules) {
            CourseManageBean b = schedule.toCourseScheduleBean(mCourseType);
            if (DateUtils.formatDateFromServer(schedule.start).getTime() < new Date().getTime()) pos++;
            datas.add(b);
        }
        if (datas.size() == 0) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }
        courseManagerAdapter.notifyDataSetChanged();
        if (pos > 0) recyclerview.scrollToPosition(pos);
    }

    @Override public void onDelSuccess() {
        freshData();
    }

    @Override public void onFixSuccess() {
        freshData();
    }
}
