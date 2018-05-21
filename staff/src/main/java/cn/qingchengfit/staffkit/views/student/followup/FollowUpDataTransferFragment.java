package cn.qingchengfit.staffkit.views.student.followup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.qingchengfit.common.FilterTimesFragment;
import cn.qingchengfit.common.FilterTimesFragmentBuilder;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.FollowUpConver;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.QcFilterToggle;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * // 转化率 筛选 + 图表
 * //Created by yangming on 16/12/5.
 */
public class FollowUpDataTransferFragment extends BaseFragment
    implements FollowUpDataTransferPresenter.PresenterView, FlexibleAdapter.OnItemClickListener {

  QcFilterToggle qftSaler;
  QcFilterToggle qftTimes;
  TextView tvStudentListHint;
  RecyclerView recyclerView;
  Toolbar toolbar;
  TextView toolbarTitile;
  FrameLayout fragTranser;
  FrameLayout layoutBg;
  //@BindView(R.id.layout_container) DragHelerLinearLayout layoutContainer;
  @Inject FollowUpDataTransferPresenter presenter;
  @Inject SerPermisAction serPermisAction;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject FollowUpDataTransfer0Fragment followUpDataTransfer0Fragment;
  @Inject TopFilterSaleFragment saleFragment;
  FilterTimesFragment latestTimeFragment;
  private StudentFilter filter = new StudentFilter();
  private List<AbstractFlexibleItem> datas = new ArrayList<>();
  private CommonFlexAdapter commonFlexAdapter;

  @Inject public FollowUpDataTransferFragment() {
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    latestTimeFragment = new FilterTimesFragmentBuilder(-1, 0).build();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_follow_up_data_transfer, container, false);
    qftSaler = (QcFilterToggle) view.findViewById(R.id.qft_saler);
    qftTimes = (QcFilterToggle) view.findViewById(R.id.qft_times);
    tvStudentListHint = (TextView) view.findViewById(R.id.tv_student_list_hint);
    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    fragTranser = (FrameLayout) view.findViewById(R.id.frag_transfer_graph);
    layoutBg = (FrameLayout) view.findViewById(R.id.frag_follow_up_filter_container);
    view.findViewById(R.id.frag_follow_up_filter_container)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            onClickBg();
          }
        });
    view.findViewById(R.id.qft_saler).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FollowUpDataTransferFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.qft_times).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FollowUpDataTransferFragment.this.onClick(v);
      }
    });

    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    if (!serPermisAction.checkHasAllMember()) {
      qftSaler.setText(loginStatus.staff_name());
    }
    initView();
    initBus();
    filter.registerTimeStart = DateUtils.minusDay(new Date(), 6);
    filter.registerTimeEnd = DateUtils.getStringToday();
    presenter.getStudentsConver(filter);
    isLoading = true;
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("会员转化");
  }

  public void onClickBg() {
    getChildFragmentManager().beginTransaction()
        .hide(saleFragment)
        .hide(latestTimeFragment)
        .commit();
    layoutBg.setVisibility(View.GONE);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void initView() {
    if (!isLoading) {
      getChildFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out)
          .add(R.id.frag_follow_up_filter_container, saleFragment)
          .add(R.id.frag_follow_up_filter_container, latestTimeFragment)
          .hide(saleFragment)
          .hide(latestTimeFragment)
          .commit();
    }

    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_transfer_graph, followUpDataTransfer0Fragment)
        .commit();
    if (commonFlexAdapter == null) {
      commonFlexAdapter = new CommonFlexAdapter(datas, this);
    }
    recyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recyclerView.setNestedScrollingEnabled(true);
    recyclerView.setAdapter(commonFlexAdapter);
  }

  private void initBus() {

    RxBusAdd(FollowUpFilterEvent.class).onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<FollowUpFilterEvent>() {
          @Override public void call(final FollowUpFilterEvent followUpFilterEvent) {
            switch (followUpFilterEvent.eventType) {
              case FollowUpFilterEvent.EVENT_SALE_ITEM_CLICK:
                filter.sale = followUpFilterEvent.filter.sale;
                onClickBg();
                break;

              case FollowUpFilterEvent.EVENT_LATEST_TIME_CLICK:
                if (followUpFilterEvent.position < 2) {
                  onClickBg();
                  Calendar calendar = Calendar.getInstance();
                  calendar.setTime(new Date(System.currentTimeMillis()));
                  String end = DateUtils.Date2YYYYMMDD(new Date(System.currentTimeMillis()));
                  if (followUpFilterEvent.position == 0) {
                    calendar.add(Calendar.DATE, -6);
                    filter.dayOff = "7";
                  } else {
                    calendar.add(Calendar.DATE, -29);
                    filter.dayOff = "30";
                  }
                  String start = DateUtils.Date2YYYYMMDD(calendar.getTime());
                  filter.registerTimeEnd = end;
                  filter.registerTimeStart = start;
                  qftTimes.setText(followUpFilterEvent.position == 0 ? "最近7天" : "最近30天");
                  tvStudentListHint.setText(
                      (followUpFilterEvent.position == 0 ? "最近7天" : "最近30天") + "注册的会员");
                }

                break;
              case FollowUpFilterEvent.EVENT_LATEST_TIME_CUSTOM_DATA:
                onClickBg();
                filter.registerTimeStart = followUpFilterEvent.start;
                filter.registerTimeEnd = followUpFilterEvent.end;
                qftTimes.setText("自定义");
                tvStudentListHint.setText(
                    filter.registerTimeStart + "至" + filter.registerTimeEnd + "注册的会员");
                break;
            }
            followUpDataTransfer0Fragment.refreshData(filter, "自定义");
            presenter.getStudentsConver(filter);
          }
        });
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public String getFragmentName() {
    return this.getClass().getName();
  }

  public void onClick(View view) {
    FragmentTransaction ts = getChildFragmentManager().beginTransaction();
    switch (view.getId()) {
      case R.id.qft_saler:
        if (!serPermisAction.checkHasAllMember()) {
          ToastUtils.show("您只能查看自己名下的会员");
          return;
        }
        qftSaler.toggle();
        if (saleFragment.isHidden()) {
          layoutBg.setVisibility(View.VISIBLE);
          ts.show(saleFragment);
        } else {
          layoutBg.setVisibility(View.GONE);
          ts.hide(saleFragment);
        }
        ts.hide(latestTimeFragment).commitAllowingStateLoss();
        break;

      case R.id.qft_times:
        qftTimes.toggle();
        if (latestTimeFragment.isHidden()) {
          layoutBg.setVisibility(View.VISIBLE);
          ts.show(latestTimeFragment);
        } else {
          layoutBg.setVisibility(View.GONE);
          ts.hide(latestTimeFragment);
        }
        ts.hide(saleFragment).commitAllowingStateLoss();
        break;
    }
  }

  @Override public void onFollowUpConver(FollowUpConver conver) {
    if (conver != null && conver.users != null) {
      commonFlexAdapter.clear();

      for (Student s : conver.users) {
        datas.add(new FollowUpItem(this, s, 4));
      }
      commonFlexAdapter.updateDataSet(datas);
    }
  }

  @Override public boolean onItemClick(int position) {
    if (position < commonFlexAdapter.getItemCount()) {
      // 跳转会员详情
      Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
      StudentBean bean = ((FollowUpItem) commonFlexAdapter.getItem(position)).data.toStudentBean();
      bean.setSupport_shop_ids(gymWrapper.shop_id());
      it.putExtra("student", bean);
      getActivity().startActivity(it);
    }
    return false;
  }

  @Override public void onShowError(String e) {
    ToastUtils.show(e);
  }
}
