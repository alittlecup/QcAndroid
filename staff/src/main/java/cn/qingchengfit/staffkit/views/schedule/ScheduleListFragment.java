package cn.qingchengfit.staffkit.views.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.usecase.bean.ScheduleBean;
import cn.qingchengfit.staffkit.views.custom.LoopView;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.RecycleViewWithNoImg;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 *
 */
public class ScheduleListFragment extends BaseFragment implements ScheduleListView {
  public static final String TAG = ScheduleListFragment.class.getName();
  View scheduleTimeline;
  RecycleViewWithNoImg scheduleRv;

  @Inject ScheduleListPresenter presenter;
  private ScheduesAdapter mAdapter;
  private List<ScheduleBean> datas = new ArrayList<>();
  private String currenDate;

  public static ScheduleListFragment newInstance(String currenDate) {
    Bundle args = new Bundle();
    args.putString("date", currenDate);
    ScheduleListFragment fragment = new ScheduleListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      currenDate = getArguments().getString("date", "2018-01-01");
    }
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_schedulelist, container, false);
    scheduleTimeline = (View) view.findViewById(R.id.schedule_timeline);
    scheduleRv = (RecycleViewWithNoImg) view.findViewById(R.id.schedule_rv);
    delegatePresenter(presenter, this);
    initView();

    return view;
  }

  private void initView() {
    mAdapter = new ScheduesAdapter(datas);
    scheduleRv.setLayoutManager(new LinearLayoutManager(getContext()));
    scheduleRv.setAdapter(mAdapter);
    scheduleRv.setOnRefreshListener(this::freshData);
    mAdapter.setListener((v, pos) -> {
      ScheduleBean scheduleBean = datas.get(pos);
      if (scheduleBean.type == 0) {
        WebActivity.startWebForResult(datas.get(pos).intent_url, getActivity(), 404);
      } else {

        RouteUtil.routeTo(getContext(), "course", "/schedule/detail",
            new BundleBuilder().withString("scheduleID", datas.get(pos).id).build());
      }
    });
    freshData();
  }

  public void freshData() {
    presenter.queryOneSchedule(App.staffId, currenDate);
  }

  @Override public void onGetData(List<ScheduleBean> scheduleBeans) {
    datas.clear();
    datas.addAll(scheduleBeans);
    mAdapter.notifyDataSetChanged();
    if (datas.size() > 0) {
      scheduleRv.setNoData(false);
    } else {
      scheduleRv.setNoData(true);
    }
    scheduleRv.setFresh(false);
    scheduleTimeline.setVisibility(datas.size() > 0 ? View.VISIBLE : View.GONE);
  }

  @Override public String getFragmentName() {
    return null;
  }

  @Override public void onDestroyView() {
    presenter.unattachView();
    super.onDestroyView();
  }

  public static class SchedulesVH extends RecyclerView.ViewHolder {
    TextView itemScheduleTime;
    TextView itemScheduleClassname;
    TextView itemScheduleGymname;
    TextView itemScheduleNum;
    ImageView itemScheduleClasspic;
    ImageView itemScheduleStatus;
    TextView getItemScheduleDone;
    TextView itemScheduleConflict;
    TextView itemTeacher;

    public SchedulesVH(View view) {
      super(view);
      itemScheduleTime = (TextView) view.findViewById(R.id.item_schedule_time);
      itemScheduleClassname = (TextView) view.findViewById(R.id.item_schedule_classname);
      itemScheduleGymname = (TextView) view.findViewById(R.id.item_schedule_gymname);
      itemScheduleNum = (TextView) view.findViewById(R.id.item_schedule_num);
      itemScheduleClasspic = (ImageView) view.findViewById(R.id.item_schedule_classpic);
      itemScheduleStatus = (ImageView) view.findViewById(R.id.item_schedule_status);
      getItemScheduleDone = (TextView) view.findViewById(R.id.item_schedule_done);
      itemScheduleConflict = (TextView) view.findViewById(R.id.item_schedule_conflict);
      itemTeacher = (TextView) view.findViewById(R.id.item_schedule_teacher);
    }
  }

  class ScheduesAdapter extends RecyclerView.Adapter<SchedulesVH> implements View.OnClickListener {
    List<ScheduleBean> datas;
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

    @Override public SchedulesVH onCreateViewHolder(ViewGroup parent, int viewType) {
      SchedulesVH holder = new SchedulesVH(
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedules, parent, false));
      holder.itemView.setOnClickListener(this);
      return holder;
    }

    @Override public void onBindViewHolder(SchedulesVH holder, int position) {
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
        holder.itemTeacher.setText(bean.teacher);
        holder.itemScheduleClasspic.setScaleType(ImageView.ScaleType.CENTER);
        Glide.with(getContext())
            .load(R.drawable.ic_schedule_rest)
            .into(holder.itemScheduleClasspic);
      } else if (bean.type == 1) { //预约
        holder.itemScheduleTime.setText(DateUtils.getTimeHHMM(new Date(bean.time)));
        holder.itemScheduleClassname.setText(bean.title);
        holder.itemScheduleGymname.setText(bean.gymname);
        holder.itemScheduleClasspic.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(getContext())
            .load(PhotoUtils.getMiddle(bean.pic_url))
            .into(holder.itemScheduleClasspic);
        holder.itemTeacher.setText(bean.teacher);
        holder.itemScheduleClasspic.setVisibility(View.VISIBLE);
      }
      if (bean.time < new Date().getTime()) {
        holder.itemScheduleNum.setVisibility(View.VISIBLE);
        holder.itemScheduleClassname.setTextColor(
            getContext().getResources().getColor(R.color.text_grey));
        holder.itemScheduleTime.setTextColor(
            getContext().getResources().getColor(R.color.text_grey));
        holder.itemScheduleStatus.setImageResource(R.drawable.vd_checkbox_checked);
        holder.itemScheduleGymname.setTextColor(getResources().getColor(R.color.text_grey));
        holder.itemScheduleNum.setTextColor(getResources().getColor(R.color.text_grey));
        holder.itemTeacher.setTextColor(getResources().getColor(R.color.text_grey));
        if (bean.isSingle) {
          holder.itemScheduleNum.setText(bean.count + "人: " + bean.users);
        } else {
          holder.itemScheduleNum.setText("共" + bean.count + "人上课");
        }

        holder.getItemScheduleDone.setVisibility(View.VISIBLE);
      } else {
        holder.itemScheduleNum.setVisibility(View.VISIBLE);
        holder.itemScheduleClassname.setTextColor(
            getContext().getResources().getColor(R.color.most_black));
        holder.itemScheduleTime.setTextColor(
            getContext().getResources().getColor(R.color.most_black));
        holder.itemScheduleGymname.setTextColor(getResources().getColor(R.color.text_black));
        holder.itemScheduleNum.setTextColor(getResources().getColor(R.color.text_black));
        holder.itemTeacher.setTextColor(getResources().getColor(R.color.text_black));
        holder.itemScheduleStatus.setImageDrawable(new LoopView(bean.color));
        if (bean.isSingle) {
          holder.itemScheduleNum.setText(bean.count + "人: " + bean.users);
        } else {
          holder.itemScheduleNum.setText(bean.count + "人已预约");
        }

        holder.getItemScheduleDone.setVisibility(View.GONE);
      }
    }

    @Override public int getItemCount() {
      return datas.size();
    }

    @Override public void onClick(View v) {
      if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }
  }
}
