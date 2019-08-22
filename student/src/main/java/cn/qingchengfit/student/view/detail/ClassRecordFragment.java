package cn.qingchengfit.student.view.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.AttendanceRecord;
import cn.qingchengfit.student.bean.ClassRecords;
import cn.qingchengfit.student.item.AttendanceAnalysItem;
import cn.qingchengfit.student.item.AttendanceRecordHeadItem;
import cn.qingchengfit.student.item.AttendanceRecordItem;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.FilterTextLayout;
import cn.qingchengfit.widgets.RecycleViewWithNoImg;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

import static android.view.View.GONE;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * 会员详情 -- 上课记录
 * <p>
 * Created by Paper on 16/3/19 2016.
 */
@Leaf(module = "student", path = "/student/class/record") public class ClassRecordFragment
    extends SaasCommonFragment implements ClassRecordView, FlexibleAdapter.OnItemClickListener,
    NotSignFilterFragment.OnNotSignFilterListener {

  RecycleViewWithNoImg recycleview;
  @Inject ClassRecordPresenter presenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  FilterTextLayout layoutGymFilter;
  FilterTextLayout layoutTypeFilter;
  FilterTextLayout layoutStatusFilter;
  FilterTextLayout layoutTimeFilter;
  FrameLayout fragNotSignFilterLayout;
  View shadow;
  LinearLayout llProxy;

  private CommonFlexAdapter commonFlexAdapter;
  private List<AbstractFlexibleItem> datas = new ArrayList<>();
  private List<ClassRecords.Shop> shops = new ArrayList<>();
  private HashMap<String, Object> params = new HashMap<>();
  private NotSignFilterFragment notSignFilterFragment;
  private TextView orderGroup, orderPrivate;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_class_records, container, false);
    recycleview = (RecycleViewWithNoImg) view.findViewById(R.id.recycleview);
    layoutGymFilter = (FilterTextLayout) view.findViewById(R.id.layout_gym_filter);
    layoutTypeFilter = (FilterTextLayout) view.findViewById(R.id.layout_type_filter);
    layoutStatusFilter = (FilterTextLayout) view.findViewById(R.id.layout_status_filter);
    layoutTimeFilter = (FilterTextLayout) view.findViewById(R.id.layout_time_filter);
    orderGroup = (TextView) view.findViewById(R.id.order_group);
    orderPrivate = (TextView) view.findViewById(R.id.order_private);
    llProxy = view.findViewById(R.id.ll_proxy);
    fragNotSignFilterLayout = (FrameLayout) view.findViewById(R.id.frag_not_sign_filter_layout);
    shadow = (View) view.findViewById(R.id.shadow);
    view.findViewById(R.id.layout_gym_filter).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        gymFilter();
      }
    });
    view.findViewById(R.id.layout_type_filter).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        typeFilter();
      }
    });
    view.findViewById(R.id.layout_status_filter).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        statusFilter();
      }
    });
    view.findViewById(R.id.layout_time_filter).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        timeFilter();
      }
    });

    delegatePresenter(presenter, this);
    SmoothScrollGridLayoutManager manager = new SmoothScrollGridLayoutManager(getContext(), 4);
    recycleview.setLayoutManager(manager);
    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        if (datas.size() > position && datas.get(position) instanceof AttendanceAnalysItem) {
          return 1;
        }
        return 4;
      }
    });
    params.put("shop_ids", "0");
    notSignFilterFragment = new NotSignFilterFragment();
    notSignFilterFragment.setOnNotSignFilterListener(this);
    commonFlexAdapter = new CommonFlexAdapter(datas, this);
    layoutGymFilter.setLabel("全部");
    layoutTypeFilter.setLabel("全部");
    layoutStatusFilter.setLabel("全部");
    layoutTimeFilter.setLabel("全部");
    getChildFragmentManager().beginTransaction()
        .add(R.id.frag_not_sign_filter_layout, notSignFilterFragment)
        .hide(notSignFilterFragment)
        .commit();
    recycleview.setAdapter(commonFlexAdapter);
    recycleview.setOnRefreshListener(() -> presenter.queryData(params));
    presenter.queryData(params);
    initToolbar(view);
    addListener();
    return view;
  }

  private void initToolbar(View view) {
    Toolbar toolbar = view.findViewById(R.id.toolbar);
    initToolbar(toolbar);
    TextView toolbarTitle = view.findViewById(R.id.toolbar_title);
    toolbarTitle.setText("训练记录");
  }

  @Override public void onDestroyView() {
    presenter.unattachView();
    super.onDestroyView();
  }

  private void showFilter(String s) {
    FragmentTransaction tc = getChildFragmentManager().beginTransaction();
    if (!notSignFilterFragment.isAdded()) {
      tc.add(R.id.frag_not_sign_filter_layout, notSignFilterFragment)
          .setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out)
          .commit();
    } else if (notSignFilterFragment.isHidden()) {
      notSignFilterFragment.hideAllandShow(s);
      startShadowAnim(true);
      tc.setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out)
          .show(notSignFilterFragment)
          .commit();
    } else {
      startShadowAnim(false);
      tc.setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out)
          .hide(notSignFilterFragment)
          .commit();
    }
  }

  private void startShadowAnim(boolean isStart) {

    AlphaAnimation animation;
    if (isStart) {
      shadow.setVisibility(View.VISIBLE);
      animation = new AlphaAnimation(0f, 0.6f);
    } else {
      shadow.setVisibility(GONE);
      animation = new AlphaAnimation(0.6f, 0f);
    }
    animation.setFillAfter(true);
    animation.setDuration(200);
    shadow.startAnimation(animation);
  }

  public void gymFilter() {
    showFilter("gym");
  }

  public void typeFilter() {
    showFilter("type");
  }

  public void statusFilter() {
    showFilter("status");
  }

  public void timeFilter() {
    showFilter("time");
  }

  private SparseArray<ClassRecords.Shop> shopArray = new SparseArray<>();

  @Override public void onData(List<AttendanceRecord> attendanceRecords, ClassRecords.Stat stat,
      List<ClassRecords.Shop> ss) {
    hideLoadingTrans();
    shops.clear();
    shops.addAll(ss);
    shopArray.clear();
    //找到当前场馆
    if (TextUtils.isEmpty(layoutGymFilter.getLabel())) {
      if (shops != null && shops.size() > 0) {
        for (ClassRecords.Shop shop : shops) {
          if (shop.id.equals(gymWrapper.shop_id())) {
            layoutGymFilter.setLabel(shop.name);
          }
        }
      } else {
        layoutGymFilter.setLabel("全部");
      }
    }
    if (shops != null && shops.size() > 0) {
      for (ClassRecords.Shop shop : shops) {
        shopArray.put(Integer.valueOf(shop.id), shop);
      }
    }
    notSignFilterFragment.initGymFilter(shops, gymWrapper.shop_id());
    datas.clear();
    datas.add(new AttendanceAnalysItem(stat.days + "", "出勤",
        CompatUtils.getColor(getContext(), R.color.orange), getString(R.string.unit_day)));
    datas.add(new AttendanceAnalysItem(stat.checkin + "", "签到", Color.parseColor("#8cb5ba"),
        getString(R.string.unit_time)));
    datas.add(new AttendanceAnalysItem(stat.group + "", "团课",
        CompatUtils.getColor(getContext(), R.color.blue), getString(R.string.unit_calss)));
    datas.add(new AttendanceAnalysItem(stat.private_count + "", "私教",
        CompatUtils.getColor(getContext(), R.color.purple), getString(R.string.unit_calss)));
    int month = -1;
    List<AttendanceRecord> monthData = new ArrayList<>();
    /**
     * 通过判断月份 来分组
     * 在发现第二组时 统一添加 标题 和第一组的数据
     * g p c用来统计团课私教 签到 当月数量
     */
    int g = 0, p = 0, c = 0;
    for (int i = 0; i < attendanceRecords.size(); i++) {
      AttendanceRecord ar = attendanceRecords.get(i);
      if (DateUtils.getMonth(DateUtils.formatDateFromServer(ar.start)) != month) {
        month = DateUtils.getMonth(DateUtils.formatDateFromServer(ar.start));
        if (i != 0) {
          if (monthData.size() > 0) {
            datas.add(
                new AttendanceRecordHeadItem(DateUtils.formatDateFromServer(monthData.get(0).start),
                    c, g, p));
          }
          int day = 0;
          for (int j = 0; j < monthData.size(); j++) {
            if (DateUtils.getDayOfMonth(DateUtils.formatDateFromServer(monthData.get(j).start))
                != day) {
              day = DateUtils.getDayOfMonth(DateUtils.formatDateFromServer(monthData.get(j).start));
              datas.add(new AttendanceRecordItem(monthData.get(j), true));
            } else {
              datas.add(new AttendanceRecordItem(monthData.get(j), false));
            }
          }
          monthData = new ArrayList<>();
          g = p = c = 0;
        }
      }
      if (ar.type == 1) g++;
      if (ar.type == 2) p++;
      if (ar.type == 3) c++;
      monthData.add(ar);
    }
    if (monthData.size() > 0) {
      datas.add(
          new AttendanceRecordHeadItem(DateUtils.formatDateFromServer(monthData.get(0).start), c, g,
              p));
    }
    int day = 0;
    for (int j = 0; j < monthData.size(); j++) {
      if (DateUtils.getDayOfMonth(DateUtils.formatDateFromServer(monthData.get(j).start)) != day) {
        day = DateUtils.getDayOfMonth(DateUtils.formatDateFromServer(monthData.get(j).start));
        datas.add(new AttendanceRecordItem(monthData.get(j), true));
      } else {
        datas.add(new AttendanceRecordItem(monthData.get(j), false));
      }
    }
    commonFlexAdapter.updateDataSet(datas);
    recycleview.setNoData(datas.size() == 0);
  }

  private int requestUserID = -1;

  @Override public String getFragmentName() {
    return ClassRecordFragment.class.getName();
  }

  @Override public boolean onItemClick(int position) {
    if (datas.get(position) instanceof AttendanceRecordItem) {
      AttendanceRecord attendanceRecord =
          ((AttendanceRecordItem) datas.get(position)).getAttendanceRecord();
      if (attendanceRecord.type != 3) {
        if (checkEnterPermission(attendanceRecord.shop.id, attendanceRecord.teacher.userID)) {
          RouteUtil.routeTo(getContext(), "course", "/schedule/detail",
              new BundleBuilder().withString("scheduleID",
                  ((AttendanceRecordItem) datas.get(position)).getAttendanceRecord().id).build());
        } else {
          showAlert(getString(R.string.alert_permission_forbid));
        }
      }
    }
    return false;
  }

  private boolean checkEnterPermission(String shopID, String teacherID) {
    if (AppUtils.getCurApp(getContext()) == 0) {
      return presenter.requestUserID.equals(teacherID);
    } else {
      if (shopArray != null) {
        return shopArray.get(Integer.valueOf(shopID)).has_staff_permission;
      } else {
        return false;
      }
    }
  }

  @Override public void onFilter(HashMap<String, Object> params) {
    showLoadingTrans();
    this.params.putAll(params);
    presenter.queryData(this.params);
  }

  @Override public void onFilterStatus(HashMap<String, Object> params) {
    startShadowAnim(false);
    layoutGymFilter.setLabel(String.valueOf(params.get(NotSignFilterFragment.SHOP_NAME)));
    layoutTypeFilter.setLabel(String.valueOf(params.get(NotSignFilterFragment.GROUP)));
    layoutStatusFilter.setLabel(String.valueOf(params.get(NotSignFilterFragment.STATUS_FILTER)));
    layoutTimeFilter.setLabel(String.valueOf(params.get(NotSignFilterFragment.TIME_LABEL)));
  }

  @Inject IPermissionModel iPermissionModel;

  private void addListener() {
    orderPrivate.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (iPermissionModel.check(PermissionServerUtils.PRIVATE_ORDER_CAN_WRITE)) {
          if (AppUtils.getCurApp(getContext()) == 0) {
            String path = Configs.Server
                + Configs.SCHEDULE_PRIVATE
                + "?id="
                + gymWrapper.id()
                + "&model="
                + gymWrapper.model()
                + "&student_id="
                + presenter.studentBase.getStudentBean().id;
            WebActivity.startWeb(path, getContext());
          } else {
            WebActivity.startWeb(presenter.studentBase.privateUrl, getContext());
          }
        } else {
          showAlert(getString(R.string.alert_permission_forbid));
        }
      }
    });
    orderGroup.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String permission = "";
        if (AppUtils.getCurApp(getContext()) == 0) {
          permission = PermissionServerUtils.GROUP_ORDER_CAN_WRITE;
        } else {
          permission = PermissionServerUtils.ORDERS_DAY_CAN_WRITE;
        }
        if (iPermissionModel.check(permission)) {
          if (AppUtils.getCurApp(getContext()) == 0) {
            String path = Configs.Server
                + Configs.SCHEDULE_GROUP
                + "?id="
                + gymWrapper.id()
                + "&model="
                + gymWrapper.model()
                + "&student_id="
                + presenter.studentBase.getStudentBean().id;
            WebActivity.startWeb(path, getContext());
          } else {
            WebActivity.startWeb(presenter.studentBase.groupUrl, getContext());
          }
        } else {
          showAlert(getString(R.string.alert_permission_forbid));
        }
      }
    });
  }
}
