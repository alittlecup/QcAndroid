package cn.qingchengfit.staffkit.views.student.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.common.AttendanceRecord;
import cn.qingchengfit.model.common.Shop;
import cn.qingchengfit.model.responese.ClassRecords;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.ChooseGymPopWin;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.student.attendance.NotSignFilterFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.FilterTextLayout;
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
public class ClassRecordFragment extends BaseFragment
    implements ClassRecordView, TitleFragment, FlexibleAdapter.OnItemClickListener,
    NotSignFilterFragment.OnNotSignFilterListener {

  public String curShopid = "";
  @BindView(R.id.recycleview) RecycleViewWithNoImg recycleview;
  //@BindView(R.id.tv_gym) TextView tvGym;
  @Inject ClassRecordPresenter presenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @BindView(R.id.layout_gym_filter) FilterTextLayout layoutGymFilter;
  @BindView(R.id.layout_type_filter) FilterTextLayout layoutTypeFilter;
  @BindView(R.id.layout_status_filter) FilterTextLayout layoutStatusFilter;
  @BindView(R.id.layout_time_filter) FilterTextLayout layoutTimeFilter;
  @BindView(R.id.frag_not_sign_filter_layout) FrameLayout fragNotSignFilterLayout;
  @BindView(R.id.shadow) View shadow;

  private CommonFlexAdapter commonFlexAdapter;
  private List<AbstractFlexibleItem> datas = new ArrayList<>();
  private List<Shop> shops = new ArrayList<>();
  private ChooseGymPopWin chooseGymPopWin;
  private HashMap<String, Object> params = new HashMap<>();
  private NotSignFilterFragment notSignFilterFragment;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_class_records, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    //tvGym.setText(gymWrapper.name());
    //tvGym.setCompoundDrawablesWithIntrinsicBounds(null, null,
    //    ContextCompat.getDrawable(getContext(), R.drawable.vector_arrow_down_green), null);
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
    //adater = new StudentClassRecordAdapter(datas);
    recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        presenter.queryData(params);
      }
    });
    presenter.queryData(params);
    return view;
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

  private void startShadowAnim(boolean isStart){

    AlphaAnimation animation;
    if (isStart) {
      shadow.setVisibility(View.VISIBLE);
      animation = new AlphaAnimation(0f, 0.6f);
    }else{
      shadow.setVisibility(GONE);
      animation = new AlphaAnimation(0.6f, 0f);
    }
    animation.setFillAfter(true);
    animation.setDuration(200);
    shadow.startAnimation(animation);
  }

  @OnClick(R.id.layout_gym_filter) public void gymFilter() {
    showFilter("gym");
  }

  @OnClick(R.id.layout_type_filter) public void typeFilter() {
    showFilter("type");
  }

  @OnClick(R.id.layout_status_filter) public void statusFilter() {
    showFilter("status");
  }

  @OnClick(R.id.layout_time_filter) public void timeFilter() {
    showFilter("time");
  }

  @Override public void onData(List<AttendanceRecord> attendanceRecords, ClassRecords.Stat stat,
      List<Shop> ss) {
    hideLoadingTrans();
    shops.clear();
    shops.addAll(ss);
    //找到当前场馆
    if (TextUtils.isEmpty(layoutGymFilter.getLabel())) {
      if(shops !=null && shops.size() > 0) {
        for (Shop shop : shops) {
          if (shop.id.equals(gymWrapper.shop_id())) {
            layoutGymFilter.setLabel(shop.name);
          }
        }
      }else{
        layoutGymFilter.setLabel("全部");
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
    monthData = new ArrayList<>();
    g = p = c = 0;

    commonFlexAdapter.updateDataSet(datas);
    recycleview.setNoData(datas.size() == 0);
  }

  @Override public String getTitle() {
    return "上课记录";
  }

  @Override public String getFragmentName() {
    return ClassRecordFragment.class.getName();
  }

  @Override public boolean onItemClick(int position) {
    if (datas.get(position) instanceof AttendanceRecordItem) {
      WebActivity.startWeb(((AttendanceRecordItem) datas.get(position)).getUrl(), getActivity());
    }
    return false;
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
}
