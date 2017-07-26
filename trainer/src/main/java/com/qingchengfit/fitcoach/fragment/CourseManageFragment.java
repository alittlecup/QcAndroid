package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.CourseManageBean;
import com.qingchengfit.fitcoach.adapter.CourseManagerAdapter;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.component.TimePeriodChooser;
import com.qingchengfit.fitcoach.fragment.batch.single.SingleBatchFragmentBuilder;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.DelCourseManage;
import com.qingchengfit.fitcoach.http.bean.FixBatchBean;
import com.qingchengfit.fitcoach.http.bean.QcBatchResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseManageFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.del_layout) FrameLayout delLayout;
    @BindView(R.id.rootview) RelativeLayout rootview;
    @BindView(R.id.no_data) LinearLayout noData;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;

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
    private Unbinder unbinder;

    public CourseManageFragment() {
    }

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

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mModel = getArguments().getString("model");
            mId = getArguments().getString("id");
            mBatchId = getArguments().getString("batchid");
            mCourseType = getArguments().getInt("courseid");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_manage, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText("编辑课程安排");
        toolbar.inflateMenu(R.menu.menu_edit);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                courseManagerAdapter.toggleEditable();
                courseManagerAdapter.notifyDataSetChanged();
                return true;
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        courseManagerAdapter = new CourseManagerAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(courseManagerAdapter);
        courseManagerAdapter.setClickTimeListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                CourseManageBean bean = datas.get(pos);
                if (!courseManagerAdapter.isEditable()) {
                    getFragmentManager().beginTransaction()
                        .replace(R.id.frag, new SingleBatchFragmentBuilder(mCourseType == Configs.TYPE_PRIVATE, bean.id).build())
                        .addToBackStack(null)
                        .commit();
                } else {
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

    private void freshData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", mId);
        QcCloudClient.getApi().getApi.qcGetGroupManageDetail(App.coachid, mBatchId,
            mCourseType == Configs.TYPE_PRIVATE ? "timetables" : "schedules", params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<QcBatchResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcBatchResponse qcBatchResponse) {
                    datas.clear();
                    int pos = 0;
                    for (QcBatchResponse.Schedule schedule : (mCourseType == Configs.TYPE_PRIVATE ? qcBatchResponse.data.timetables
                        : qcBatchResponse.data.schedules)) {
                        CourseManageBean b = new CourseManageBean();
                        b.month = DateUtils.Date2YYYYMM(DateUtils.formatDateFromServer(schedule.start));
                        b.day = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(schedule.start));
                        b.WeekDay = DateUtils.getDayOfWeek(DateUtils.formatDateFromServer(schedule.start));
                        if (mCourseType == Configs.TYPE_GROUP) {
                            b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.start));
                        } else {
                            b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.start)) + "-" + DateUtils.getTimeHHMM(
                                DateUtils.formatDateFromServer(schedule.end));
                        }
                        b.start = DateUtils.formatDateFromServer(schedule.start);
                        b.end = DateUtils.formatDateFromServer(schedule.end);
                        b.outdue = DateUtils.formatDateFromServer(schedule.start).getTime() < new Date().getTime();
                        b.id = schedule.id + "";
                        b.length = DateUtils.formatDateFromServer(schedule.end).getTime() - DateUtils.formatDateFromServer(schedule.start)
                            .getTime();
                        if (b.outdue) pos++;
                        datas.add(b);
                    }
                    if (datas.size() == 0) {
                        noData.setVisibility(View.VISIBLE);
                    } else {
                        noData.setVisibility(View.GONE);
                    }
                    //                        courseManagerAdapter = new CourseManagerAdapter(datas);
                    //                        recyclerview.setAdapter(courseManagerAdapter);
                    courseManagerAdapter.notifyDataSetChanged();
                    if (pos > 0) recyclerview.scrollToPosition(pos);
                }
            });
    }

    public void chooseTime(int pos) {
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
                    FixBatchBean batchBean = new FixBatchBean();
                    batchBean.model = mModel;
                    batchBean.id = mId;
                    batchBean.start = datas.get(pos).day + "T" + DateUtils.getTimeHHMM(date) + ":00";
                    batchBean.end =
                        datas.get(pos).day + "T" + DateUtils.getTimeHHMM(new Date(date.getTime() + datas.get(pos).length)) + ":00";
                    //                    batchBean.end = DateUtils.formatToServer();
                    QcCloudClient.getApi().postApi.qcFixBatch(App.coachid, datas.get(pos).id, "schedules", batchBean)
                        .observeOn(AndroidSchedulers.mainThread())
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<QcResponse>() {
                            @Override public void onCompleted() {

                            }

                            @Override public void onError(Throwable e) {

                            }

                            @Override public void onNext(QcResponse qcResponse) {
                                if (qcResponse.status == ResponseResult.SUCCESS) {
                                    datas.get(pos).time = DateUtils.getTimeHHMM(date);
                                    courseManagerAdapter.notifyItemChanged(pos);
                                }
                            }
                        });
                }
            });
            //            timeWindow.setTime(datas.get(pos).start);
            timeWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, datas.get(pos).start);
        } else {
            if (timeDialogWindow == null) {
                timeDialogWindow = new TimePeriodChooser(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
            }
            //            timeDialogWindow.setTime(datas.get(pos).start, datas.get(pos).end);
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

                    FixBatchBean batchBean = new FixBatchBean();
                    batchBean.model = mModel;
                    batchBean.id = mId;
                    batchBean.start = datas.get(pos).day + "T" + DateUtils.getTimeHHMM(start) + ":00";
                    batchBean.end = datas.get(pos).day + "T" + DateUtils.getTimeHHMM(end) + ":00";
                    QcCloudClient.getApi().postApi.qcFixBatch(App.coachid, datas.get(pos).id, "timetables", batchBean)
                        .observeOn(AndroidSchedulers.mainThread())
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<QcResponse>() {
                            @Override public void onCompleted() {

                            }

                            @Override public void onError(Throwable e) {

                            }

                            @Override public void onNext(QcResponse qcResponse) {
                                if (qcResponse.status == ResponseResult.SUCCESS) {
                                    datas.get(pos).time = DateUtils.getTimeHHMM(start) + "-" + DateUtils.getTimeHHMM(end);

                                    courseManagerAdapter.notifyItemChanged(pos);
                                }
                            }
                        });
                }
            });
            timeDialogWindow.setTime(datas.get(pos).start, datas.get(pos).end);
            timeDialogWindow.showAtLocation();
        }
    }

    @OnClick(R.id.btn_del) public void OnDel() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
                .content("是否删除排期?")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                        DelCourseManage delCourseManage = new DelCourseManage();
                        delCourseManage.id = mId;
                        delCourseManage.model = mModel;
                        delCourseManage.ids = "";
                        for (CourseManageBean bean : datas) {
                            if (bean.checked) delCourseManage.ids = TextUtils.concat(delCourseManage.ids, bean.id, ",").toString();
                        }
                        if (delCourseManage.ids.endsWith(",")) delCourseManage.ids.substring(0, delCourseManage.ids.length() - 1);
                        QcCloudClient.getApi().postApi.qcDelCourseManage(App.coachid,
                            mCourseType == Configs.TYPE_PRIVATE ? "timetables" : "schedules", delCourseManage)
                            .observeOn(AndroidSchedulers.mainThread())
                            .onBackpressureBuffer()
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<QcResponse>() {
                                @Override public void onCompleted() {

                                }

                                @Override public void onError(Throwable e) {

                                }

                                @Override public void onNext(QcResponse qcResponse) {
                                    if (qcResponse.status == ResponseResult.SUCCESS) {
                                        ToastUtils.showDefaultStyle("删除成功");
                                        freshData();
                                    }
                                }
                            });
                    }

                    @Override public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .build();
        }
        delDialog.show();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
