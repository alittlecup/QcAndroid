package com.qingchengfit.fitcoach.fragment.batch.looplist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.lib.TimeDialogWindow;
import com.bigkoo.pickerview.lib.Type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.inject.commpont.GymComponent;
import cn.qingchengfit.staffkit.model.dataaction.SerPermisAction;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.usecase.bean.CoachService;
import cn.qingchengfit.staffkit.usecase.body.FixBatchBean;
import cn.qingchengfit.staffkit.usecase.response.QcResponseBatchSchedules;
import cn.qingchengfit.staffkit.utils.DateUtils;
import cn.qingchengfit.staffkit.utils.ToastUtils;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.TimePeriodChooser;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseManageFragment extends BaseFragment implements CourseManageView {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.del_layout)
    FrameLayout delLayout;
    @Bind(R.id.rootview)
    RelativeLayout rootview;
    @Bind(R.id.no_data)
    LinearLayout noData;

    private String mBatchId; //排期id
    private List<CourseManageBean> datas = new ArrayList<>();
    private CourseManagerAdapter courseManagerAdapter;
    private List<Integer> delChooose = new ArrayList<>();
    private int mCourseType = Configs.TYPE_PRIVATE;
    private int chooseCount = 0; //选中的个数
    private TimeDialogWindow timeWindow;
    private TimePeriodChooser timeDialogWindow;
    private MaterialDialog delDialog;

    @Inject
    CourseManagePresenter presenter;
    @Inject
    CoachService coachService;

    public static CourseManageFragment newInstance(String Batchid, int coursetype) {

        Bundle args = new Bundle();
        args.putString("batchid", Batchid);
        args.putInt("courseid", coursetype);
        CourseManageFragment fragment = new CourseManageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBatchId = getArguments().getString("batchid");
            mCourseType = getArguments().getInt("courseid");
        }
    }

    public CourseManageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_manage, container, false);
        ButterKnife.bind(this, view);
        ((GymComponent) mCallbackActivity.getComponent()).inject(this);

        mCallbackActivity.setToolbar("课程安排", false, null, 0, null);
        presenter.attachView(this);
        courseManagerAdapter = new CourseManagerAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(courseManagerAdapter);
        courseManagerAdapter.setClickTimeListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {


                CourseManageBean bean = datas.get(pos);
                if (v.getId() == R.id.time) {
                    if ((mCourseType == Configs.TYPE_GROUP && !SerPermisAction.check(coachService.getShop_id(), PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE))
                            || (mCourseType == Configs.TYPE_PRIVATE && !SerPermisAction.check(coachService.getShop_id(), PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE))) {
                        showAlert(R.string.alert_permission_forbid);
                        return;
                    }
                    chooseTime(pos);
                } else {
                    if (!bean.outdue) {
                        bean.checked = !bean.checked;
                        if (bean.checked)
                            chooseCount++;
                        else chooseCount--;
                        courseManagerAdapter.notifyItemChanged(pos);
                        if (chooseCount > 0) {
                            delLayout.setVisibility(View.VISIBLE);
                        } else delLayout.setVisibility(View.GONE);
                    }
                }

            }
        });
        freshData();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }


    private void freshData() {
        presenter.queryList(mBatchId, mCourseType);
//        HashMap<String, String> params = new HashMap<>();
//        params.put("model", mModel);
//        params.put("id", mId);

//        QcCloudClient.getApi().getApi.qcGetGroupManageDetail(App.coachid, mBatchId, mCourseType == Configs.TYPE_PRIVATE ? "timetables" : "schedules", params)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<QcBatchResponse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(QcBatchResponse qcBatchResponse) {
//                        datas.clear();
//                        int pos = 0;
//                        for (QcBatchResponse.Schedule schedule : (mCourseType == Configs.TYPE_PRIVATE ? qcBatchResponse.data.timetables : qcBatchResponse.data.schedules)) {
//                            CourseManageBean b = new CourseManageBean();
//                            b.month = DateUtils.Date2YYYYMM(DateUtils.formatDateFromServer(schedule.start));
//                            b.day = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(schedule.start));
//                            b.WeekDay = DateUtils.getDayOfWeek(DateUtils.formatDateFromServer(schedule.start));
//                            if (mCourseType == Configs.TYPE_GROUP)
//                                b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.start));
//                            else
//                                b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.start)) + "-"
//                                        + DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.end));
//                            b.start = DateUtils.formatDateFromServer(schedule.start);
//                            b.end = DateUtils.formatDateFromServer(schedule.end);
//                            b.outdue = DateUtils.formatDateFromServer(schedule.start).getTime() < new Date().getTime();
//                            b.id = schedule.id + "";
//                            b.length = DateUtils.formatDateFromServer(schedule.end).getTime() - DateUtils.formatDateFromServer(schedule.start).getTime();
//                            if (b.outdue)
//                                pos++;
//                            datas.add(b);
//                        }
//                        if (datas.size() == 0){
//                            noData.setVisibility(View.VISIBLE);
//                        }else
//                            noData.setVisibility(View.GONE);
//                        courseManagerAdapter.notifyDataSetChanged();
//                        if (pos > 0)
//                            recyclerview.scrollToPosition(pos);
//                    }
//                })
//        ;
    }

    public void chooseTime(final int pos) {
        if (mCourseType == Configs.TYPE_GROUP) {
            if (timeWindow == null) {
                timeWindow = new TimeDialogWindow(getContext(), Type.HOURS_MINS, 5);
            }

            timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    if (datas.get(pos).day.equalsIgnoreCase(DateUtils.Date2YYYYMMDD(new Date())) && date.getTime() <= new Date().getTime()) {
                        ToastUtils.showDefaultStyle("开始时间不能小于当前时间");
                        return;
                    }
                    FixBatchBean batchBean = new FixBatchBean();
                    batchBean.model = coachService.getModel();
                    batchBean.id = coachService.getId();
                    batchBean.start = datas.get(pos).day + "T" + DateUtils.getTimeHHMM(date) + ":00";
                    batchBean.end = datas.get(pos).day + "T" + DateUtils.getTimeHHMM(new Date(date.getTime() + datas.get(pos).length)) + ":00";

                    presenter.updateSchedules(datas.get(pos).id, batchBean, mCourseType);
//                    QcCloudClient.getApi().postApi.qcFixBatch(App.coachid, datas.get(pos).id, "schedules",
//                            batchBean).observeOn(AndroidSchedulers.mainThread())
//                            .subscribeOn(Schedulers.io())
//                            .subscribe(new Subscriber<QcResponse>() {
//                                @Override
//                                public void onCompleted() {
//
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//
//                                }
//
//                                @Override
//                                public void onNext(QcResponse qcResponse) {
//                                    if (qcResponse.status == ResponseConstant.SUCCESS) {
//                                        datas.get(pos).time = DateUtils.getTimeHHMM(date);
//                                        courseManagerAdapter.notifyItemChanged(pos);
//                                    }
//                                }
//                            });


                }
            });
            timeWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, datas.get(pos).start);

        } else {
            if (timeDialogWindow == null) {
                timeDialogWindow = new TimePeriodChooser(getContext(), Type.HOURS_MINS, 5);

            }
            timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date start, Date end) {
                    if (start.getTime() >= end.getTime()) {
                        ToastUtils.showDefaultStyle("开始时间不能小于结束时间");
                        return;
                    }
                    if (datas.get(pos).day.equalsIgnoreCase(DateUtils.Date2YYYYMMDD(new Date())) && start.getTime() <= new Date().getTime()) {
                        ToastUtils.showDefaultStyle("开始时间不能小于当前时间");
                        return;
                    }


                    FixBatchBean batchBean = new FixBatchBean();
                    batchBean.model = coachService.getModel();
                    batchBean.id = coachService.getId();
                    batchBean.start = datas.get(pos).day + "T" + DateUtils.getTimeHHMM(start) + ":00";
                    batchBean.end = datas.get(pos).day + "T" + DateUtils.getTimeHHMM(end) + ":00";
                    presenter.updateSchedules(datas.get(pos).id, batchBean, mCourseType);
//                    QcCloudClient.getApi().postApi.qcFixBatch(App.coachid, datas.get(pos).id, "timetables",
//                            batchBean).observeOn(AndroidSchedulers.mainThread())
//                            .subscribeOn(Schedulers.io())
//                            .subscribe(new Subscriber<QcResponse>() {
//                                @Override
//                                public void onCompleted() {
//
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//
//                                }
//
//                                @Override
//                                public void onNext(QcResponse qcResponse) {
//                                    if (qcResponse.status == ResponseConstant.SUCCESS) {
//                                        datas.get(pos).time = DateUtils.getTimeHHMM(start) + "-" + DateUtils.getTimeHHMM(end);
//
//                                        courseManagerAdapter.notifyItemChanged(pos);
//                                    }
//                                }
//                            });


                }
            });
            timeDialogWindow.setTime(datas.get(pos).start, datas.get(pos).end);
            timeDialogWindow.showAtLocation();
        }
    }

    @OnClick(R.id.btn_del)
    public void OnDel() {
        if ((mCourseType == Configs.TYPE_GROUP && !SerPermisAction.check(coachService.getShop_id(), PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_DELETE))
                || (mCourseType == Configs.TYPE_PRIVATE && !SerPermisAction.check(coachService.getShop_id(), PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_DELETE))) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext())
                    .autoDismiss(true)
                    .content("是否删除排期?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .cancelable(true)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            presenter.delSchedules(courseManagerAdapter.getChooseIds(), mCourseType);
                        }
                    })
                    .build();
        }
        delDialog.show();
    }


    @Override
    public void onDestroyView() {
        RxBus.getBus().post(new EventFresh());
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Override
    public void onList(List<QcResponseBatchSchedules.Schedule> schedules) {
        datas.clear();
        int pos = 0;
        for (QcResponseBatchSchedules.Schedule schedule : schedules) {
            CourseManageBean b = new CourseManageBean();
            b.month = DateUtils.Date2YYYYMM(DateUtils.formatDateFromServer(schedule.start));
            b.day = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(schedule.start));
            b.WeekDay = DateUtils.getDayOfWeek(DateUtils.formatDateFromServer(schedule.start));
            if (mCourseType == Configs.TYPE_GROUP)
                b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.start));
            else
                b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.start)) + "-"
                        + DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.end));
            b.start = DateUtils.formatDateFromServer(schedule.start);
            b.end = DateUtils.formatDateFromServer(schedule.end);
            b.outdue = DateUtils.formatDateFromServer(schedule.start).getTime() < new Date().getTime();
            b.id = schedule.id + "";
            b.length = DateUtils.formatDateFromServer(schedule.end).getTime() - DateUtils.formatDateFromServer(schedule.start).getTime();
            if (b.outdue)
                pos++;
            datas.add(b);
        }
        if (datas.size() == 0) {
            noData.setVisibility(View.VISIBLE);
        } else
            noData.setVisibility(View.GONE);
        courseManagerAdapter.notifyDataSetChanged();
        if (pos > 0)
            recyclerview.scrollToPosition(pos);
    }

    @Override
    public void onDelSuccess() {
        freshData();
    }

    @Override
    public void onFixSuccess() {
        freshData();
    }
}
