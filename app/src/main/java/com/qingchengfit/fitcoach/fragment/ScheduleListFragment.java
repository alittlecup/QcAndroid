package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ScheduleCompare;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.bean.RxRefreshList;
import com.qingchengfit.fitcoach.component.LoopView;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcScheduleBean;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import com.qingchengfit.fitcoach.http.bean.ScheduleBean;
import com.qingchengfit.fitcoach.server.CalendarIntentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleListFragment extends Fragment {
    public static final String TAG = ScheduleListFragment.class.getName();
    @Bind(R.id.schedule_rv)
    RecyclerView scheduleRv;
    @Bind(R.id.schedule_no_img)
    ImageView scheduleNoImg;
    @Bind(R.id.schedule_no_tv)
    TextView scheduleNoTv;
    @Bind(R.id.schedule_no_schedule)
    SwipeRefreshLayout scheduleNoSchedule;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.schedule_timeline)
    View scheduleTimeline;

    private Date mCurDate;                              //当前日期
    private ArrayList<ScheduleBean> scheduleBeans = new ArrayList<>();      //列表数据
    private ScheduesAdapter scheduesAdapter;
    private int curentGym = 0;
    private String currentModel;
    private long mCurCalId;
    private QcSchedulesResponse mQcSchedulesResponse;
    Observer<QcSchedulesResponse> mHttpCallBack = new Observer<QcSchedulesResponse>() {

        @Override
        public void onCompleted() {
            if (refresh != null) {
                refresh.setRefreshing(false);
                scheduleNoSchedule.setRefreshing(false);
            }
        }

        @Override
        public void onError(Throwable e) {
            if (e != null)
                e.printStackTrace();
            ToastUtils.show(R.drawable.ic_share_fail, "网络错误");
            if (refresh != null) {
                refresh.setRefreshing(false);
                scheduleNoSchedule.setRefreshing(false);
            }
        }

        @Override
        public void onNext(QcSchedulesResponse qcSchedulesResponse) {
            mQcSchedulesResponse = qcSchedulesResponse;
            handleResponse(qcSchedulesResponse);
        }
    };

    public ScheduleListFragment() {
    }

    public static ScheduleListFragment newInstance(Long date, int curentGym,String model) {

        Bundle args = new Bundle();
        args.putLong("date", date);
        args.putInt("gym", curentGym);
        args.putString("model",model);
        ScheduleListFragment fragment = new ScheduleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurDate = new Date(getArguments().getLong("date"));
            curentGym = getArguments().getInt("gym");
            currentModel = getArguments().getString("model");
        } else mCurDate = new Date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedulelist, container, false);
        ButterKnife.bind(this, view);
        scheduesAdapter = new ScheduesAdapter(scheduleBeans);

        scheduleRv.setLayoutManager(new LinearLayoutManager(getContext()));
        scheduleRv.setAdapter(scheduesAdapter);
        scheduesAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String url = scheduesAdapter.datas.get(pos).intent_url;
                LogUtil.e("pos:" + pos + url);
                if (!TextUtils.isEmpty(url)) {
                    Intent it = new Intent(getActivity(), WebActivity.class);
                    it.putExtra("url", url);
                    getParentFragment().startActivityForResult(it, 113);
                }
            }
        });
        goDateSchedule(mCurDate);
        mCurCalId = PreferenceUtils.getPrefLong(getContext(), "calendar_id", -1);
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RxBus.getBus().post(new RxRefreshList());
                refresh();
            }
        });
        scheduleNoSchedule.setColorSchemeResources(R.color.primary);
        scheduleNoSchedule.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RxBus.getBus().post(new RxRefreshList());
                refresh();
            }
        });

        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (refresh != null) {
                    refresh.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    refresh.setRefreshing(true);
                }
            }
        });

        App.gCanReload = true;
        return view;
    }


    private void goDateSchedule(Date date) {
        HashMap<String, String> params = new HashMap<>();
        params.put("date", DateUtils.getServerDateDay(date));
        QcCloudClient.getApi().getApi.qcGetCoachScheduleV1(App.coachid, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mHttpCallBack);

    }

    private void handleResponse(QcSchedulesResponse qcSchedulesResponse) {
        if (qcSchedulesResponse == null)
            return;

        List<QcSchedulesResponse.Service> systems = qcSchedulesResponse.data.services;
        CalendarIntentService.startActionDay(getContext(), mCurDate.getTime(), new Gson().toJson(qcSchedulesResponse));

        scheduleBeans.clear();


        for (int i = 0; i < systems.size(); i++) {
            QcSchedulesResponse.Service system = systems.get(i);

            List<QcSchedulesResponse.Rest> rests = system.rests;
            List<QcScheduleBean> schedules = system.schedules;
            if (system.system == null)
                continue;
            String syscolor = system.system.name;
            if (curentGym != 0 && (curentGym != system.system.id || !system.system.model.equals(currentModel)))
                continue;

            for (int j = 0; j < rests.size(); j++) {
                QcSchedulesResponse.Rest rest = rests.get(j);
                ScheduleBean bean = new ScheduleBean();
                bean.type = 0;
                bean.color = syscolor;
                bean.time = DateUtils.formatDateFromServer(rest.start).getTime();
                bean.timeEnd = DateUtils.formatDateFromServer(rest.end).getTime();
                bean.gymname = system.system.name;

                bean.intent_url = rest.url;
                scheduleBeans.add(bean);

            }
            for (int k = 0; k < schedules.size(); k++) {
                QcScheduleBean schedule = schedules.get(k);
                ScheduleBean bean = new ScheduleBean();
                bean.type = 1;

                if (schedule.orders != null && schedule.orders.size() == 1) {
                    bean.isSingle = true;
                    bean.users = schedule.orders.get(0).username;
                } else {
                    bean.isSingle = false;
                }
                bean.gymname = schedule.shop.name;
                bean.color = syscolor;
                bean.time = DateUtils.formatDateFromServer(schedule.start).getTime();
                bean.timeEnd = DateUtils.formatDateFromServer(schedule.end).getTime();
                bean.count = schedule.count;
                bean.pic_url = schedule.course.photo;
                bean.title = schedule.course.name;
                bean.intent_url = schedule.url;
                if (getContext() != null) {
                    String thing = PhoneFuncUtils.queryEvent(getContext(), bean.time, bean.timeEnd, mCurCalId);
                    bean.conflict = thing;
                }
                scheduleBeans.add(bean);
            }

        }
        Collections.sort(scheduleBeans, new ScheduleCompare());

        if (getActivity() == null)
            return;
        getActivity().runOnUiThread(() -> {
            if (scheduleNoSchedule != null) {
                scheduesAdapter.notifyDataSetChanged();

                if (scheduleBeans.size() > 0) {
                    scheduleNoSchedule.setVisibility(View.GONE);
                    scheduleRv.setVisibility(View.VISIBLE);
                    scheduleTimeline.setVisibility(View.VISIBLE);

                } else {
                    scheduleRv.setVisibility(View.GONE);
                    scheduleNoSchedule.setVisibility(View.VISIBLE);
                    scheduleTimeline.setVisibility(View.VISIBLE);
                }


            }


        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0) {
            refresh();
        }
    }

    public void refresh() {
        goDateSchedule(mCurDate);

    }

    public static class SchedulesVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_schedule_time)
        TextView itemScheduleTime;
        @Bind(R.id.item_schedule_classname)
        TextView itemScheduleClassname;
        @Bind(R.id.item_schedule_gymname)
        TextView itemScheduleGymname;
        @Bind(R.id.item_schedule_num)
        TextView itemScheduleNum;
        @Bind(R.id.item_schedule_classpic)
        ImageView itemScheduleClasspic;
        @Bind(R.id.item_schedule_status)
        ImageView itemScheduleStatus;
        @Bind(R.id.item_schedule_done)
        TextView getItemScheduleDone;
        @Bind(R.id.item_schedule_conflict)
        TextView itemScheduleConflict;

        public SchedulesVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ScheduesAdapter extends RecyclerView.Adapter<SchedulesVH> implements View.OnClickListener {


        private List<ScheduleBean> datas;
        private OnRecycleItemClickListener listener;


        public ScheduesAdapter(List datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public SchedulesVH onCreateViewHolder(ViewGroup parent, int viewType) {
            SchedulesVH holder = new SchedulesVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedules, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(SchedulesVH holder, int position) {
            holder.itemView.setTag(position);
            ScheduleBean bean = datas.get(position);
            if (bean.type == 0) { //休息
                holder.itemScheduleTime.setText(DateUtils.getTimeHHMM(new Date(bean.time)));
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(new Date(bean.time)));
                sb.append("-");
                sb.append(DateUtils.getTimeHHMM(new Date(bean.timeEnd)));
                sb.append(" 休息");
                holder.itemScheduleClassname.setText(sb.toString());
                holder.itemScheduleGymname.setText(bean.gymname);
                holder.itemScheduleNum.setVisibility(View.GONE);
                holder.itemScheduleClasspic.setScaleType(ImageView.ScaleType.CENTER);
                Glide.with(App.AppContex).load(R.drawable.ic_schedule_rest).into(holder.itemScheduleClasspic);


            } else if (bean.type == 1) { //预约
                holder.itemScheduleTime.setText(DateUtils.getTimeHHMM(new Date(bean.time)));
                holder.itemScheduleClassname.setText(bean.title);
                holder.itemScheduleGymname.setText(bean.gymname);
                holder.itemScheduleClasspic.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(App.AppContex).load(bean.pic_url).into(holder.itemScheduleClasspic);

                holder.itemScheduleClasspic.setVisibility(View.VISIBLE);


            }
            if (bean.conflict == null) {
                //TODO conflict 事件不冲突
                holder.itemScheduleConflict.setVisibility(View.GONE);
            } else {
                holder.itemScheduleConflict.setVisibility(View.VISIBLE);
                holder.itemScheduleConflict.setText("时间冲突: " + bean.conflict);
            }
            if (bean.time < new Date().getTime()) {
                holder.itemScheduleClassname.setTextColor(getContext().getResources().getColor(R.color.text_grey));
                holder.itemScheduleTime.setTextColor(getContext().getResources().getColor(R.color.text_grey));
                holder.itemScheduleStatus.setImageResource(R.drawable.ic_calendar_done);
                holder.itemScheduleGymname.setTextColor(getResources().getColor(R.color.text_grey));
                holder.itemScheduleNum.setTextColor(getResources().getColor(R.color.text_grey));
                if (bean.isSingle) {
                    holder.itemScheduleNum.setText(bean.count + "人: " + bean.users);
                } else {
                    holder.itemScheduleNum.setText("共" + bean.count + "人上课");
                }

                holder.getItemScheduleDone.setVisibility(View.VISIBLE);
            } else {
                holder.itemScheduleClassname.setTextColor(getContext().getResources().getColor(R.color.most_black));
                holder.itemScheduleTime.setTextColor(getContext().getResources().getColor(R.color.most_black));
                holder.itemScheduleGymname.setTextColor(getResources().getColor(R.color.text_black));
                holder.itemScheduleNum.setTextColor(getResources().getColor(R.color.text_black));
                holder.itemScheduleStatus.setImageDrawable(new LoopView(bean.color));
                if (bean.isSingle) {
                    holder.itemScheduleNum.setText(bean.count + "人: " + bean.users);
                } else {
                    holder.itemScheduleNum.setText(bean.count + "人已预约");
                }

                holder.getItemScheduleDone.setVisibility(View.GONE);
            }

        }


        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(v, (int) v.getTag());
        }
    }
}
