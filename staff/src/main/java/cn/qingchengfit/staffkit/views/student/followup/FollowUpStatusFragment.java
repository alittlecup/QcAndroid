package cn.qingchengfit.staffkit.views.student.followup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.common.FilterTimesFragment;
import cn.qingchengfit.common.FilterTimesFragmentBuilder;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventFilterClick;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.model.responese.TrackStudents;
import cn.qingchengfit.network.errors.BusEventThrowable;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.FilterFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rxbus.event.EventRouter;
import cn.qingchengfit.saasbase.items.TotalCountFooterItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.student.StudentSearchFragment;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.QcFilterToggle;
import cn.qingchengfit.widgets.QcToggleButton;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
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
 * //
 * //Created by yangming on 16/12/5.
 */
public class FollowUpStatusFragment extends BaseFragment
    implements FollowUpStatusPresenter.PresenterView, FlexibleAdapter.OnItemClickListener,
    FlexibleAdapter.EndlessScrollListener {

  public static final int FILTER_TOP_SALES = 1;
  public static final int FILTER_TOP_DAYS = 2;
  public static final int FILTER_BOTTOM_STU = 3;
  public static final int FILTER_BOTTOM_DAYS = 4;
  public static final int FILTER_BOTTOM_SALES = 5;
  public static final int FILTER_BOTTOM_GENDER = 6;
  public static final int FILTER_BOTTOM_FILTER = 7;
  public int studentStatus;
	RecyclerView recyclerViewToday;
	AppBarLayout layoutCollapsed;
  StudentSearchFragment studentSearchFragment;
	QcFilterToggle qftStatus;
	QcFilterToggle qftTime;
	QcFilterToggle qftSaler;
	QcToggleButton qtbFilter;
	QcFilterToggle qftGender;
	Toolbar toolbar;
	TextView toolbarTitile;
	FrameLayout fragChart;
	FrameLayout fragContaiter;
  FollowUpFilterFragment filterFragment;
  TopFilterSaleFragment saleFragment;
  FilterTimesFragment registerFragment;
  FilterFragment filterStatus;
  FilterFragment filterGender;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject FollowUpStatusPresenter presenter;
  @Inject SerPermisAction serPermisAction;
  @Inject BusEventThrowable busEventThrowable;
  ProgressItem progress;
	CoordinatorLayout coordinatorLayout;
  /**
   * 与type一一对应，不可修改，除非type修改
   */
  private String[] frag_tags = {
      "", "top_days", "top_sale", "btm_stu", "btm_days", "btm_sale", "btm_gen", ""
  };
  private CommonFlexAdapter flexibleAdapterToday;
  private List<AbstractFlexibleItem> itemsToday = new ArrayList<>();
  private List<Student> dataToday = new ArrayList<>();
  private StudentFilter filter = new StudentFilter();
  private boolean isGraphExpand = true;//图表是否展示
  private TotalCountFooterItem footItem;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_follow_up_status, container, false);
    recyclerViewToday = (RecyclerView) view.findViewById(R.id.recycler_view_today);
    layoutCollapsed = (AppBarLayout) view.findViewById(R.id.layout_collapsed);
    qftStatus = (QcFilterToggle) view.findViewById(R.id.qft_status);
    qftTime = (QcFilterToggle) view.findViewById(R.id.qft_times);
    qftSaler = (QcFilterToggle) view.findViewById(R.id.qft_saler);
    qtbFilter = (QcToggleButton) view.findViewById(R.id.qtb_filter);
    qftGender = (QcFilterToggle) view.findViewById(R.id.qft_gender);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    fragChart = (FrameLayout) view.findViewById(R.id.frag_chart);
    fragContaiter = (FrameLayout) view.findViewById(R.id.frag_filter);
    coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
    view.findViewById(R.id.frag_filter).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackClick();
      }
    });
    view.findViewById(R.id.qft_saler).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FollowUpStatusFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.qft_times).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FollowUpStatusFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.qft_status).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FollowUpStatusFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.qtb_filter).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FollowUpStatusFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.qft_gender).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FollowUpStatusFragment.this.onClick(v);
      }
    });

    delegatePresenter(presenter, this);
    setToolbar(toolbar);
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup) {
        FrameLayout.LayoutParams params =
            (FrameLayout.LayoutParams) coordinatorLayout.getLayoutParams();
      params.setMargins(0,
          MeasureUtils.getStatusBarHeight(getContext()) + MeasureUtils.getActionbarBarHeight(
              getContext()), 0, 0);
        coordinatorLayout.setLayoutParams(params);

    }
    if (!serPermisAction.checkHasAllMember()) {
      qftSaler.setText(loginStatus.staff_name());
    }
    initTitleAndFilterBar();
    initFilterEntity();
    initView();
    initBus();
    presenter.getStudentsWithStatus(filter, studentStatus);
    return view;
  }

  public void setToolbar(Toolbar toolbar) {
    initToolbar(toolbar);
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup && isfitSystemPadding()) {
      ((ViewGroup) toolbar.getParent()).setPadding(0, MeasureUtils.getStatusBarHeight(getContext()),
          0, MeasureUtils.getStatusBarHeight(getContext()));
    }
    toolbar.inflateMenu(R.menu.menu_search);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        studentSearchFragment =
            StudentSearchFragment.newInstanceFollow(studentStatus, (ArrayList<Student>) dataToday);
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), studentSearchFragment)
            .addToBackStack(null)
            .commit();
        return true;
      }
    });
  }

  @Override protected void onFinishAnimation() {
    saleFragment = new TopFilterSaleFragment();
    registerFragment = new FilterTimesFragmentBuilder(-1, 1).build();
    saleFragment.page = 1;
    registerFragment.page = 1;

    filterStatus = new FilterFragment();
    List<AbstractFlexibleItem> list = new ArrayList<>();
    list.add(new FilterCommonLinearItem("全部"));
    list.add(new FilterCommonLinearItem("新注册"));
    list.add(new FilterCommonLinearItem("已接洽"));
    list.add(new FilterCommonLinearItem("会员"));
    filterStatus.setItemList(list);
    filterStatus.setOnSelectListener(new FilterFragment.OnSelectListener() {
      @Override public void onSelectItem(int position) {
        switch (position) {
          case 0:
            filter.status = null;
            qftStatus.setText("会员状态");
            break;
          case 1:
            filter.status = "0";
            qftStatus.setText("新注册");
            break;
          case 2:
            filter.status = "1";
            qftStatus.setText("已接洽");
            break;
          case 3:
            filter.status = "2";
            qftStatus.setText("会员");
            break;
          default:
            break;
        }
        qftStatus.setChecked(false);
        if (filterFragment != null) filterFragment.selectStatusPos(position - 1);
        hideAll(null);
        presenter.getStudentsWithStatus(filter, studentStatus);
      }
    });
    filterGender = new FilterFragment();
    List<AbstractFlexibleItem> listGender = new ArrayList<>();
    listGender.add(new FilterCommonLinearItem("全部"));
    listGender.add(new FilterCommonLinearItem("男"));
    listGender.add(new FilterCommonLinearItem("女"));
    filterGender.setItemList(listGender);
    filterGender.setOnSelectListener(new FilterFragment.OnSelectListener() {
      @Override public void onSelectItem(int position) {
        switch (position) {
          case 1:
            filter.gender = "0";
            qftGender.setText("男");
            break;
          case 2:
            filter.gender = "1";
            qftGender.setText("女");
            break;
          default:
            filter.gender = null;
            qftGender.setText("性别");
            break;
        }
        qftGender.setChecked(false);
        hideAll(null);
        presenter.getStudentsWithStatus(filter, studentStatus);
      }
    });

    getFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
        .replace(R.id.frag_chart, new FollowUpDataStatisticsFragmentBuilder(studentStatus).build())
        .commit();

    if (studentStatus == 0) {
      //初始化边侧栏
      filterFragment = new FollowUpFilterFragment();
      ((FollowUpActivity) getActivity()).setUpDrawer(filterFragment);
    }
  }

  private void initBus() {
    RxBusAdd(EventFilterClick.class).onBackpressureLatest()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventFilterClick>() {
          @Override public void call(EventFilterClick eventFilterClick) {
            handlePopFilter(eventFilterClick.type);
          }
        }, new BusEventThrowable());

        /*
         *  监听筛选
         */
    RxBusAdd(FollowUpFilterEvent.class).observeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<FollowUpFilterEvent>() {
          @Override public void call(FollowUpFilterEvent followUpFilterEvent) {
            if (followUpFilterEvent.page == 0) return;
            switch (followUpFilterEvent.eventType) {
              case FollowUpFilterEvent.EVENT_SALE_ITEM_CLICK:
                filter.sale = followUpFilterEvent.filter.sale;
                if (filter.sale != null) {
                  qftSaler.setText(filter.sale.getUsername());
                } else {
                  qftSaler.setText("销售");
                }
                qftSaler.setChecked(false);
                //设置边侧栏同步
                if (filterFragment != null && filter.sale != null) //未分配销售
                {
                  filterFragment.saleFragment.selectSaler(filter.sale.getId());
                }

                hideAll(null);
                break;
              case FollowUpFilterEvent.EVENT_LATEST_TIME_CLICK:
                switch (followUpFilterEvent.position) {
                  case 0:
                    filter.registerTimeStart = DateUtils.getStringToday();
                    filter.registerTimeEnd = DateUtils.getStringToday();
                    qftTime.setText("今日");
                    break;
                  case 1:
                    filter.registerTimeEnd = DateUtils.getStringToday();
                    filter.registerTimeStart = DateUtils.minusDay(new Date(), 6);
                    qftTime.setText("最近7天");
                    break;
                  default:
                    filter.registerTimeEnd = DateUtils.getStringToday();
                    filter.registerTimeStart = DateUtils.minusDay(new Date(), 29);
                    qftTime.setText("最近30天");
                    break;
                }
                //qftTime.setText(filter.registerTimeStart + "至" + filter.registerTimeEnd);
                qftTime.setChecked(false);
                if (filterFragment != null) {
                  filterFragment.selectTimePos(followUpFilterEvent.position);
                }

                hideAll(null);
                break;
              case FollowUpFilterEvent.EVENT_LATEST_TIME_CUSTOM_DATA:
                filter.registerTimeStart = followUpFilterEvent.start;
                filter.registerTimeEnd = followUpFilterEvent.end;
                qftTime.setText("自定义");
                qftTime.setChecked(false);
                if (filterFragment != null) {
                  filterFragment.tvStudentFilterTimeStart.setText(followUpFilterEvent.start);
                  filterFragment.tvStudentFilterTimeEnd.setText(followUpFilterEvent.end);
                }

                hideAll(null);
                break;
            }
            getActivity().runOnUiThread(new Runnable() {
              @Override public void run() {
                showLoading();
              }
            });
            presenter.getStudentsWithStatus(filter, studentStatus);
          }
        });
    RxBusAdd(StudentFilterEvent.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<StudentFilterEvent>() {
          @Override public void call(StudentFilterEvent event) {
            //边侧栏筛选
            if (event.eventFrom == StudentFilterEvent.EVENT_FROM_FOLLOW_UP) {
              RxBus.getBus().post(new EventRouter(RouterFollowUp.DRAWER_CLOSE));
              expandGraph(true);
              filter = event.filter;
              if (!TextUtils.isEmpty(event.filter.status)) {
                qftStatus.setText(
                    BusinessUtils.getStudentStatus(Integer.parseInt(event.filter.status)));
              }
              if (event.filter.sale != null && Integer.parseInt(event.filter.sale.id) > 0) {
                qftSaler.setText(event.filter.sale.getUsername());
              } else {
                qftSaler.setText("销售");
              }
              if (filter.timePos > 0) {
                switch (filter.timePos) {
                  case 1:
                    qftTime.setText("最近7天");
                    break;
                  case 2:
                    qftTime.setText("最近30天");
                    break;
                  default:
                    qftTime.setText("今日");
                    break;
                }
              } else {
                qftTime.setText("自定义");
              }
              if (!TextUtils.isEmpty(filter.gender)) {
                if (filter.gender.equals("0")) {
                  qftGender.setText("男");
                } else {
                  qftGender.setText("女");
                }
              } else {
                qftGender.setText("性别");
              }
              showLoading();
              presenter.getStudentsWithStatus(filter, studentStatus);
            }
          }
        }, busEventThrowable);
  }

  /**
   * 处理筛选弹窗
   */
  private void handlePopFilter(int type) {
    Fragment popF = getChildFragmentManager().findFragmentByTag(frag_tags[type]);
    if (type <= 0 || (popF != null && popF.isVisible())) {
      hideAll(null);
      return;
    }
    switch (type) {
      case FILTER_TOP_SALES:
        if (popF == null) {
          popF = new TopFilterSaleFragment();
          if (popF instanceof TopFilterSaleFragment) {
            ((TopFilterSaleFragment) popF).page = 0;
          }
          getChildFragmentManager().beginTransaction()
              .add(R.id.frag_filter, popF, frag_tags[type])
              .commit();
        }
        break;
      case FILTER_TOP_DAYS:
        if (popF == null) {
          popF = new FilterTimesFragmentBuilder(30, 0).build();
          if (popF instanceof FilterTimesFragment) {
            ((FilterTimesFragment) popF).page = 0;
          }
          getChildFragmentManager().beginTransaction()
              .add(R.id.frag_filter, popF, frag_tags[type])
              .commit();
        }
        break;
      case FILTER_BOTTOM_STU:
        if (popF == null) {
          popF = filterStatus;
          getChildFragmentManager().beginTransaction()
              .add(R.id.frag_filter, popF, frag_tags[type])
              .commit();
        }
        break;
      case FILTER_BOTTOM_DAYS:
        if (popF == null) {
          popF = registerFragment;
          getChildFragmentManager().beginTransaction()
              .add(R.id.frag_filter, popF, frag_tags[type])
              .commit();
        }
        break;
      case FILTER_BOTTOM_SALES:
        if (popF == null) {
          popF = saleFragment;
          getChildFragmentManager().beginTransaction()
              .add(R.id.frag_filter, popF, frag_tags[type])
              .commit();
        }
        break;
      case FILTER_BOTTOM_GENDER:
        if (popF == null) {
          popF = filterGender;
          getChildFragmentManager().beginTransaction()
              .add(R.id.frag_filter, popF, frag_tags[type])
              .commit();
        }
        break;
      case FILTER_BOTTOM_FILTER:

        break;
      default:
        return;
    }
    expandGraph(type < 3);
    hideAll(frag_tags[type]);
  }

  /**
   * 展开图表
   *
   * @param b 是否展示
   */
  private void expandGraph(final boolean b) {
    if (b == isGraphExpand) {
      return;
    } else {
      isGraphExpand = b;
    }
    layoutCollapsed.setExpanded(b);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void initTitleAndFilterBar() {
    String title = "";
    switch (studentStatus) {
      case 0:
        title = getString(R.string.qc_foolow_up_lable_today_new);
        break;
      case 1:
        title = "新增跟进";
        break;
      case 2:
        title = getString(R.string.qc_follow_up_lable_toady_member);
        break;
    }
    toolbarTitile.setText(title);
    //根据状态展示筛选 0是展示 8是不展示
    switch (studentStatus) {
      case 0:
        showFilter(new int[] { 0, 0, 0, 0, 8 });
        break;
      case 1:
        showFilter(new int[] { 0, 0, 0, 8, 0 });
        break;
      case 2:
        showFilter(new int[] { 8, 0, 0, 8, 0 });
        break;
    }
  }

  public void showFilter(int[] show) {
    qftStatus.setVisibility(show[0]);
    qftTime.setVisibility(show[1]);
    qftSaler.setVisibility(show[2]);
    qtbFilter.setVisibility(show[3]);
    qftGender.setVisibility(show[4]);
  }

  /**
   * 初始化筛选数据
   */
  private void initFilterEntity() {
    filter.status = null;
    filter.registerTimeStart = DateUtils.getStringToday();
    filter.registerTimeEnd = DateUtils.getStringToday();
    if (!serPermisAction.checkHasAllMember()) {
      filter.sale = loginStatus.getLoginUser();
    }
  }

  private void initView() {
    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) fragContaiter.getLayoutParams();
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup){
      lp.topMargin =
          (int) (MeasureUtils.getActionbarBarHeight(getContext()) + getResources().getDimension(
              R.dimen.qc_item_height) + MeasureUtils.getStatusBarHeight(getContext()));
    }else {
      lp.topMargin =
          (int) (MeasureUtils.getActionbarBarHeight(getContext()) + getResources().getDimension(R.dimen.qc_item_height));
    }
    fragContaiter.setLayoutParams(lp);
    recyclerViewToday.setLayoutManager(new SmoothScrollLinearLayoutManager(getActivity()));
    progress = new ProgressItem(getContext());
    footItem = new TotalCountFooterItem(1);
    if (flexibleAdapterToday == null) {
      flexibleAdapterToday = new CommonFlexAdapter(itemsToday, this);
      flexibleAdapterToday.setEndlessScrollListener(this, progress);
    }
    recyclerViewToday.setAdapter(flexibleAdapterToday);
  }

  @Override public void onDestroyView() {
    if (studentStatus == 0) ((FollowUpActivity) getActivity()).disableDrawer();
    super.onDestroyView();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public String getFragmentName() {
    return this.getClass().getName();
  }

  /**
   * 背景点击
   */
 public void onBackClick() {
    hideAll(null);
  }

  /**
   * 点击filter条
   *
   * @param view 点击的view
   */
 public void onClick(View view) {
    switch (view.getId()) {
      case R.id.qft_saler:
        if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
          ToastUtils.show("您只能查看自己名下的会员");
          return;
        }
        handlePopFilter(FILTER_BOTTOM_SALES);
        break;
      case R.id.qft_times:
        handlePopFilter(FILTER_BOTTOM_DAYS);
        break;
      case R.id.qft_status:
        handlePopFilter(FILTER_BOTTOM_STU);
        break;
      case R.id.qft_gender:
        handlePopFilter(FILTER_BOTTOM_GENDER);
        break;
      case R.id.qtb_filter:
        RxBus.getBus().post(new EventRouter(RouterFollowUp.DRAWER_OPEN));
        break;
    }
  }

  @Override public void onTrackStudent(TrackStudents students, int curPage) {
    hideLoading();
    if (curPage == 1) {
      dataToday.clear();
      flexibleAdapterToday.clear();
    }
    List<Student> studentList =
        students.users == null ? (new ArrayList<Student>()) : students.users;
    dataToday.addAll(studentList);
    if (curPage == 1 && (studentList == null || studentList.isEmpty())) {
      itemsToday.add(new SimpleTextItemItem("暂无数据", Gravity.CENTER));
      flexibleAdapterToday.updateDataSet(itemsToday);
      return;
    }
    List<AbstractFlexibleItem> newItems = new ArrayList<>();
    for (Student student : studentList) {
      newItems.add(new FollowUpItem(this, student, studentStatus));
    }
    flexibleAdapterToday.onLoadMoreComplete(newItems);

    //if (curPage >= flexibleAdapterToday.getEndlessTargetCount())
    //    flexibleAdapterToday.addItem(new TotalCountFooterItem(flexibleAdapterToday.getItemCount()));
  }

  @Override public void onToatalPages(int toatalPags) {
    flexibleAdapterToday.setEndlessTargetCount(toatalPags);
  }

  @Override public void onNoMoreLoad() {

    flexibleAdapterToday.onLoadMoreComplete(null);
  }

  @Override public void onShowError(String e) {
    hideLoading();
    ToastUtils.show(e);
  }

  @Override public void initEndlessLoad() {
    if (flexibleAdapterToday != null) flexibleAdapterToday.setEndlessScrollListener(this, progress);
  }

  @Override public boolean onItemClick(int position) {
    if (position < flexibleAdapterToday.getItemCount()) {
      if (flexibleAdapterToday.getItem(position) instanceof FollowUpItem) {
        // 跳转会员详情
        Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
        StudentBean bean =
            ((FollowUpItem) flexibleAdapterToday.getItem(position)).data.toStudentBean();
        bean.setSupport_shop_ids(gymWrapper.shop_id());
        it.putExtra("student", bean);
        it.putExtra("status_to_tab", studentStatus);
        getActivity().startActivity(it);
      }
    }
    return false;
  }

  private void closeExcepte(int res) {
    if (res != R.id.qft_saler) qftSaler.setChecked(false);
    if (res != R.id.qft_times) qftTime.setChecked(false);
    if (res != R.id.qft_status) qftStatus.setChecked(false);
    if (res != R.id.qft_gender) qftGender.setChecked(false);
  }

  private void hideAll(String str) {
    fragContaiter.setVisibility(TextUtils.isEmpty(str) ? View.GONE : View.VISIBLE);
    FragmentTransaction ts = getChildFragmentManager().beginTransaction();
    for (String frag_tag : frag_tags) {
      if (TextUtils.isEmpty(frag_tag)) continue;
      if (frag_tag.equalsIgnoreCase(str)) {
        if (getChildFragmentManager().findFragmentByTag(str) != null) {
          ts.show(getChildFragmentManager().findFragmentByTag(str));
        }
      } else if (getChildFragmentManager().findFragmentByTag(frag_tag) != null) {
        ts.hide(getChildFragmentManager().findFragmentByTag(frag_tag));
      }
    }
    ts.commitAllowingStateLoss();
  }

  @Override public void noMoreLoad(int i) {
    if (flexibleAdapterToday.getItem(
        flexibleAdapterToday.getItemCount() - 1) instanceof ProgressItem) {
      flexibleAdapterToday.removeItem(flexibleAdapterToday.getItemCount() - 1);
    }

    if (flexibleAdapterToday.getMainItemCount() > 0 && !(flexibleAdapterToday.getItem(
        0) instanceof SimpleTextItemItem) && !flexibleAdapterToday.contains(footItem)) {
      flexibleAdapterToday.addScrollableFooter(footItem);
    }
  }

  @Override public void onLoadMore(int i, int i1) {
    presenter.loadMore(studentStatus);
  }
}
