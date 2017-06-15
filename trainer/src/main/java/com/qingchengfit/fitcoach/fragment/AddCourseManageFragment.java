package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.component.FullyLinearLayoutManager;
import com.qingchengfit.fitcoach.component.TimePeriodChooser;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.AddBatchCourse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCourseManageFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.starttime) CommonInputView starttime;
    @BindView(R.id.endtime) CommonInputView endtime;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.add) TextView add;
    List<CmBean> datas = new ArrayList<>();
    @BindView(R.id.rootview) LinearLayout rootview;
    private CmAdapter adapter;
    private TimePeriodChooser timeDialogWindow;
    private TimeDialogWindow timeWindow;
    private DialogList dialogList;
    private String[] weeks = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
    private int mType = Configs.TYPE_GROUP;//类型 1是私教 2是团课
    private TimeDialogWindow pwTime;

    private String mModel;
    private String mId;
    private String mCourseid;
    private long mCourseLength;
    private Unbinder unbinder;

    public AddCourseManageFragment() {
    }

    public static AddCourseManageFragment newInstance(String mode, String gymid, String courseid, int coursetype, String courseLength) {

        Bundle args = new Bundle();
        args.putString("model", mode);
        args.putString("gymid", gymid);
        args.putString("courseid", courseid);
        args.putString("courseLength", courseLength);
        args.putInt("coursetype", coursetype);
        AddCourseManageFragment fragment = new AddCourseManageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCourseid = getArguments().getString("courseid");
            mId = getArguments().getString("gymid");
            mModel = getArguments().getString("model");
            String lengh = getArguments().getString("courseLength");
            mType = getArguments().getInt("coursetype");
            if (!TextUtils.isEmpty(lengh)) mCourseLength = Long.parseLong(lengh) * 1000;
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course_manage, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (mType == Configs.TYPE_GROUP) {
            toolbar.setTitle(R.string.title_add_group_course_batch);
            datas.add(new CmBean(0, DateUtils.formatDateHHMM("08:00"), null));
        } else {
            toolbar.setTitle(R.string.title_add_private_batch);
            datas.add(new CmBean(0, DateUtils.formatDateHHMM("08:00"), DateUtils.formatDateHHMM("20:00")));
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        //        datas.add(new CmBean(-1, null, null));
        adapter = new CmAdapter();
        recyclerview.setLayoutManager(new FullyLinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);

        starttime.setContent(DateUtils.Date2YYYYMMDD(new Date()));
        endtime.setContent(DateUtils.getEndDayOfMonthNew(new Date()));
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    /**
     * 添加
     */
    @OnClick(R.id.add) public void onAdd() {
        //        if (datas.size() > 0 && (datas.get(datas.size() - 1).week < 0 || datas.get(datas.size() - 1).dateStart == null))
        //            return;

        if (mType == Configs.TYPE_PRIVATE) {
            CmBean bean = new CmBean(-1, DateUtils.formatDateHHMM("8:00"), DateUtils.formatDateHHMM("20:00"));
            datas.add(bean);
        } else {
            CmBean bean = new CmBean(-1, DateUtils.formatDateHHMM("8:00"));
            datas.add(bean);
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * 选择开始时间
     */
    @OnClick(R.id.starttime) public void onStartTime() {
        if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR),
            Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
        pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
                starttime.setContent(DateUtils.Date2YYYYMMDD(date));
                endtime.setContent(DateUtils.getEndDayOfMonthNew(date));
                pwTime.dismiss();
            }
        });
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
    }

    /**
     * 选择结束时间
     */
    @OnClick(R.id.endtime) public void onEndTime() {
        if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR),
            Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
        pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
                if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(starttime.getContent()).getTime()) {
                    Toast.makeText(App.AppContex, R.string.alert_endtime_greater_starttime, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (date.getTime() - DateUtils.formatDateFromYYYYMMDD(starttime.getContent()).getTime() > 92 * DateUtils.DAY_TIME) {
                    Toast.makeText(App.AppContex, R.string.alert_batch_greater_three_month, Toast.LENGTH_SHORT).show();
                    return;
                }
                endtime.setContent(DateUtils.Date2YYYYMMDD(date));
                pwTime.dismiss();
            }
        });
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
    }

    /**
     * 点击确认按钮
     */
    @OnClick(R.id.comfirm) public void onComfirm() {
        AddBatchCourse addBatchCourse = new AddBatchCourse();
        if (datas.size() == 0) {
            ToastUtils.showDefaultStyle(getContext().getString(R.string.please_add_one_batch));
            return;
        } else {
            if (datas.size() == 1 && (datas.get(0).week < 0 || datas.get(0).dateStart == null)) return;
            List<AddBatchCourse.WeekTime> weekTimes = new ArrayList<>();

            for (CmBean cm : datas) {
                AddBatchCourse.WeekTime wt = new AddBatchCourse.WeekTime();
                if (cm.dateStart == null || cm.week < 0) continue;
                wt.weekday = cm.week + 1;
                wt.start = DateUtils.getTimeHHMM(cm.dateStart);
                if (cm.dateEnd != null) {
                    wt.end = DateUtils.getTimeHHMM(cm.dateEnd);
                } else {
                    wt.end = DateUtils.getTimeHHMM(new Date(cm.dateStart.getTime() + mCourseLength));
                    //                    LogUtil.e("endtime:" + wt.end + "   " + mCourseLength + "   :" + cm.dateStart.getTime());
                }
                weekTimes.add(wt);
            }
            if (weekTimes.size() < 1) {
                ToastUtils.showDefaultStyle(getContext().getString(R.string.please_add_one_batch));
                return;
            }

            addBatchCourse.from_date = starttime.getContent();
            addBatchCourse.to_date = endtime.getContent();
            addBatchCourse.course_id = mCourseid;
            addBatchCourse.id = mId;
            addBatchCourse.model = mModel;
            addBatchCourse.time_repeats = weekTimes;
            QcCloudClient.getApi().postApi.qcAddCourseManage(App.coachid, addBatchCourse)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onNext(QcResponse qcResponse) {
                        ToastUtils.show("添加成功");
                        RxBus.getBus().post(RxBus.BUS_REFRESH);
                        getActivity().onBackPressed();
                    }
                });
        }
    }

    /**
     * 选择周几
     */
    public void chooseWeek(int pos) {
        //        if (dialogList == null) {
        dialogList = new DialogList(getContext());
        dialogList.title("请选择健身房");
        //        }
        dialogList.list(weeks, new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.e("pos:" + position);
                datas.get(pos).week = position;
                adapter.notifyItemChanged(pos);
                dialogList.dismiss();
            }
        });
        dialogList.show();
    }

    /**
     * 时间选择器
     */
    public void chooseTime(int pos) {
        if (mType == Configs.TYPE_GROUP) {
            if (timeWindow == null) {
                timeWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
            }
            if (datas.get(pos).dateStart != null) {
                timeWindow.setTime(datas.get(pos).dateStart);
            } else {
                timeWindow.setTime(new Date(DateUtils.getDayMidnight(new Date())));
            }
            timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override public void onTimeSelect(Date date) {
                    datas.get(pos).dateStart = date;
                    adapter.notifyItemChanged(pos);
                }
            });
            timeWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
        } else {
            if (timeDialogWindow == null) {
                timeDialogWindow = new TimePeriodChooser(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
            }
            timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
                @Override public void onTimeSelect(Date start, Date end) {
                    if (start.getTime() >= end.getTime()) {
                        ToastUtils.showDefaultStyle("开始时间不能小于结束时间");
                        return;
                    }

                    datas.get(pos).dateStart = start;
                    datas.get(pos).dateEnd = end;
                    adapter.notifyItemChanged(pos);
                }
            });
            Date dstart = datas.get(pos).dateStart;
            Date dend = datas.get(pos).dateEnd;

            if (dstart != null && dend != null) {
                timeDialogWindow.setTime(dstart, dend);
            } else {
                timeDialogWindow.setTime(new Date(DateUtils.getDayMidnight(new Date())), new Date(DateUtils.getDayMidnight(new Date())));
            }
            timeDialogWindow.showAtLocation();
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class CmVh extends RecyclerView.ViewHolder {
        @BindView(R.id.text1) TextView text1;
        @BindView(R.id.layout1) RelativeLayout layout1;
        @BindView(R.id.text2) TextView text2;
        @BindView(R.id.layout2) RelativeLayout layout2;
        @BindView(R.id.delete) ImageView delete;
        @BindView(R.id.down1) ImageView down1;
        @BindView(R.id.down2) ImageView down2;

        public CmVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CmAdapter extends RecyclerView.Adapter<CmVh> {

        @Override public CmVh onCreateViewHolder(ViewGroup parent, int viewType) {
            CmVh vh = new CmVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_manage, parent, false));
            return vh;
        }

        @Override public void onBindViewHolder(CmVh holder, int position) {
            CmBean bean = datas.get(position);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    datas.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            holder.layout1.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //选择周几
                    chooseWeek(position);
                }
            });
            holder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //选择时间
                    chooseTime(position);
                }
            });
            //            holder.delete.setVisibility(View.VISIBLE);
            if (bean.week < 0) {
                holder.text1.setText(R.string.choose_week);
                holder.down1.setVisibility(View.INVISIBLE);
            } else {
                holder.text1.setText("每" + weeks[bean.week]);
                holder.down1.setVisibility(View.VISIBLE);
            }
            if (bean.dateStart == null) {
                holder.text2.setText(R.string.choose_time);
                holder.down2.setVisibility(View.INVISIBLE);
            } else {
                holder.down2.setVisibility(View.VISIBLE);
                if (mType == Configs.TYPE_PRIVATE) {
                    holder.text2.setText(DateUtils.getTimeHHMM(bean.dateStart) + "-" + DateUtils.getTimeHHMM(bean.dateEnd));
                } else {
                    holder.text2.setText(DateUtils.getTimeHHMM(bean.dateStart));
                }
            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }
    }

    public class CmBean {
        public int week;
        public Date dateStart;
        public Date dateEnd;

        public CmBean(int week, Date dateStart) {
            this.week = week;
            this.dateStart = dateStart;
            this.dateEnd = null;
        }

        public CmBean(int week, Date dateStart, Date dateEnd) {
            this.week = week;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
        }
    }
}
