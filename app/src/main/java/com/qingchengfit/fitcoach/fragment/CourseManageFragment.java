package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.CourseManageBean;
import com.qingchengfit.fitcoach.adapter.CourseManagerAdapter;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.component.TimePeriodChooser;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.DelCourseManage;
import com.qingchengfit.fitcoach.http.bean.FixBatchBean;
import com.qingchengfit.fitcoach.http.bean.QcBatchResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseManageFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.del_layout)
    FrameLayout delLayout;
    @Bind(R.id.rootview)
    RelativeLayout rootview;

    private String mBatchId; //排期id
    private String mModel;
    private String mId;
    private List<CourseManageBean> datas = new ArrayList<>();
    private CourseManagerAdapter courseManagerAdapter;
    private List<Integer> delChooose = new ArrayList<>();
    private int mCourseType = Configs.TYPE_PRIVATE;
    private int chooseCount = 0; //选中的个数
    private TimeDialogWindow timeWindow;
    private TimePeriodChooser timeDialogWindow;
    private MaterialDialog delDialog;

    public static CourseManageFragment newInstance(String Model, String id, String Batchid, int coursetype) {

        Bundle args = new Bundle();
        args.putString("model", Model);
        args.putString("id", id);
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
            mModel = getArguments().getString("model");
            mId = getArguments().getString("id");
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
        toolbar.setTitle("编辑课程安排");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        courseManagerAdapter = new CourseManagerAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(courseManagerAdapter);
        courseManagerAdapter.setClickTimeListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CourseManageBean bean = datas.get(pos);
                if (v.getId() == R.id.time) {
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
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", mId);
        QcCloudClient.getApi().getApi.qcGetGroupManageDetail(App.coachid, mBatchId, mCourseType == Configs.TYPE_PRIVATE ? "timetables" : "schedules", params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcBatchResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcBatchResponse qcBatchResponse) {
                        datas.clear();
                        for (QcBatchResponse.Schedule schedule : (mCourseType == Configs.TYPE_PRIVATE?qcBatchResponse.data.timetables:qcBatchResponse.data.schedules)) {
                            CourseManageBean b = new CourseManageBean();
                            b.month = DateUtils.getDateMonth(DateUtils.formatDateFromServer(schedule.start));
                            b.day = DateUtils.getDateDay(DateUtils.formatDateFromServer(schedule.start));
                            b.WeekDay = DateUtils.getDayOfWeek(DateUtils.formatDateFromServer(schedule.start));
                            if (mCourseType == Configs.TYPE_GROUP)
                                b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.start));
                            else
                                b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.start)) + "-"
                                        + DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.end));

                            b.outdue = DateUtils.formatDateFromServer(schedule.start).getTime() < new Date().getTime();
                            b.id = schedule.id + "";
                            b.length = DateUtils.formatDateFromServer(schedule.end).getTime() - DateUtils.formatDateFromServer(schedule.start).getTime();
                            datas.add(b);
                        }
//                        courseManagerAdapter = new CourseManagerAdapter(datas);
//                        recyclerview.setAdapter(courseManagerAdapter);
                        courseManagerAdapter.notifyDataSetChanged();
                    }
                })
        ;
    }

    public void chooseTime(int pos) {
        if (mCourseType == Configs.TYPE_GROUP) {
            if (timeWindow == null) {
                timeWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
            }
            timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    if (datas.get(pos).day.equalsIgnoreCase(DateUtils.getDateDay(new Date())) && date.getTime() <= new Date().getTime()) {
                        ToastUtils.showDefaultStyle("开始时间不能小于当前时间");
                        return;
                    }
                    FixBatchBean batchBean = new FixBatchBean();
                    batchBean.model = mModel;
                    batchBean.id = mId;
                    batchBean.end = DateUtils.formatToServer(date);
                    batchBean.start = DateUtils.formatToServer(new Date(date.getTime() + datas.get(pos).length));
                    QcCloudClient.getApi().postApi.qcFixBatch(App.coachid, datas.get(pos).id, "schedules",
                            batchBean).observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<QcResponse>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(QcResponse qcResponse) {
                                    if (qcResponse.status == ResponseResult.SUCCESS) {
                                        datas.get(pos).time = DateUtils.getTimeHHMM(date);
                                        courseManagerAdapter.notifyItemChanged(pos);
                                    }
                                }
                            });


                }
            });
            timeWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());

        } else {
            if (timeDialogWindow == null) {
                timeDialogWindow = new TimePeriodChooser(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);

            }
            timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date start, Date end) {
                    if (start.getTime() >= end.getTime()) {
                        ToastUtils.showDefaultStyle("开始时间不能小于结束时间");
                        return;
                    }
                    if (datas.get(pos).day.equalsIgnoreCase(DateUtils.getDateDay(new Date())) && start.getTime() <= new Date().getTime()) {
                        ToastUtils.showDefaultStyle("开始时间不能小于当前时间");
                        return;
                    }


                    FixBatchBean batchBean = new FixBatchBean();
                    batchBean.model = mModel;
                    batchBean.id = mId;
                    batchBean.end = DateUtils.formatToServer(start);
                    batchBean.start = DateUtils.formatToServer(end);
                    QcCloudClient.getApi().postApi.qcFixBatch(App.coachid, datas.get(pos).id, "timetables",
                            batchBean).observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<QcResponse>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(QcResponse qcResponse) {
                                    if (qcResponse.status == ResponseResult.SUCCESS) {
                                        datas.get(pos).time = DateUtils.getTimeHHMM(start) + "-" + DateUtils.getTimeHHMM(end);

                                        courseManagerAdapter.notifyItemChanged(pos);
                                    }
                                }
                            });


                }
            });
            timeDialogWindow.setTime(new Date(), new Date(System.currentTimeMillis() + DateUtils.HOUR_TIME));
            timeDialogWindow.showAtLocation();
        }
    }

    @OnClick(R.id.btn_del)
    public void OnDel() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext())
                    .autoDismiss(true)
                    .content("是否删除课程?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            dialog.dismiss();
                            DelCourseManage delCourseManage = new DelCourseManage();
                            delCourseManage.id = mId;
                            delCourseManage.model = mModel;
                            for (CourseManageBean bean : datas) {
                                if (bean.checked)
                                    delCourseManage.ids.add(bean.id);
                            }
                            QcCloudClient.getApi().postApi.qcDelCourseManage(App.coachid, delCourseManage)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<QcResponse>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(QcResponse qcResponse) {
                                            if (qcResponse.status == ResponseResult.SUCCESS) {
                                                ToastUtils.showDefaultStyle("删除成功");
                                                freshData();
                                            }
                                        }
                                    });

                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    })
                    .cancelable(false)
                    .build();
        }
        delDialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


//    public class

}
