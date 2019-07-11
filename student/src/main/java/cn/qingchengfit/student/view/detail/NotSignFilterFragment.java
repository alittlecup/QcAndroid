package cn.qingchengfit.student.view.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.saascommon.filtertime.FilterCustomFragment;
import cn.qingchengfit.saascommon.filtertime.FilterCustomFragmentBuilder;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.ClassRecords;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.FilterFragment;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/9/19.
 */

public class NotSignFilterFragment extends BaseFragment
    implements FilterCustomFragment.OnBackFilterDataListener {

  private static final String TYPE = "type";
  public static final String GROUP = "class";
  private static final String STATUS = "status";
  public static final String STATUS_FILTER = "status_filter";
  private static final String START = "start";
  private static final String END = "end";
  private static final String SHOP_ID = "shop_ids";
  public static final String SHOP_NAME = "shop_name";
  public static final String TIME_LABEL = "time_label";

  FrameLayout fragStudentFilter;

  @Inject NotSignFilterPresenter presenter;

  protected String[] filterType = new String[] { "gym", "type", "status", "time" };
  private FilterFragment gymFilter;
  private FilterFragment typeFilter;
  private FilterFragment statusFilter;
  private FilterFragment timeFitler;
  private FilterCustomFragment filterCustomFragment;
  private HashMap<String, Object> params = new HashMap<>();
  private HashMap<String, Object> statusParams = new HashMap<>();
  private List<String> typeList = new ArrayList<>();
  private List<String> statusList = new ArrayList<>();
  private List<String> timeList = new ArrayList<>();
  private List<ClassRecords.Shop> shopList = new ArrayList<>();
  private OnNotSignFilterListener onNotSignFilterListener;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_not_sign_filter, container, false);
    fragStudentFilter = (FrameLayout) view.findViewById(R.id.frag_student_filter);
    initFilter();
    return view;
  }

  private void initFilter() {
    gymFilter = new FilterFragment();
    typeFilter = new FilterFragment();
    statusFilter = new FilterFragment();
    timeFitler = new FilterFragment();

    initGymFilter(new ArrayList<ClassRecords.Shop>(), "");
    filterCustomFragment = FilterCustomFragmentBuilder.newFilterCustomFragment("时间段");
    filterCustomFragment.setSelectTime(true);
    filterCustomFragment.setOnBackFilterDataListener(this);
    typeList = presenter.getClassFilterList();
    timeList = presenter.getTimeFiler();
    statusParams.put(SHOP_NAME, "全部");
    statusParams.put(GROUP, "全部");
    statusParams.put(STATUS_FILTER, "全部");
    statusParams.put(TIME_LABEL, "不限");
    gymFilter.setOnSelectListener(new FilterFragment.OnSelectListener() {
      @Override public void onSelectItem(int position) {
        if (position > 0) {
          params.put(SHOP_ID, shopList.get(position - 1).id);
          statusParams.put(SHOP_NAME, shopList.get(position - 1).name);
        } else {
          params.put(SHOP_ID, "0");
          statusParams.put(SHOP_NAME, "全部");
        }
        refresh();
        hideAll("gym");
      }
    });
    typeFilter.setOnSelectListener(new FilterFragment.OnSelectListener() {
      @Override public void onSelectItem(int position) {
        params.put(TYPE, getType(typeList.get(position)));
        statusParams.put(GROUP, typeList.get(position));
        initStatusFilter(presenter.getStatusFilter(typeList.get(position)));
        refresh();
        hideAll("type");
      }
    });
    List<AbstractFlexibleItem> typeList = new ArrayList<>();
    for (String str : presenter.getClassFilterList()) {
      typeList.add(new FilterCommonLinearItem(str));
    }
    typeFilter.setItemList(typeList);
    statusFilter.setOnSelectListener(new FilterFragment.OnSelectListener() {
      @Override public void onSelectItem(int position) {
        params.put(STATUS, getStatusCode(statusList.get(position)));
        statusParams.put(STATUS_FILTER, statusList.get(position));
        refresh();
        hideAll("status");
      }
    });

    List<AbstractFlexibleItem> timeItemList = new ArrayList<>();
    initStatusFilter(presenter.getStatusFilter("全部"));
    for (String str : presenter.getTimeFiler()) {
      timeItemList.add(new FilterCommonLinearItem(str));
    }
    timeFitler.setItemList(timeItemList);
    timeFitler.setOnSelectListener(new FilterFragment.OnSelectListener() {
      @Override public void onSelectItem(int position) {
        switch (position) {
          case 0:
            params.put(START, "");
            params.put(END, "");
            statusParams.put(TIME_LABEL, "不限");
            break;
          case 1:
            params.put(START, DateUtils.getStringToday());
            params.put(END, DateUtils.getStringToday());
            statusParams.put(TIME_LABEL, "今日");
            break;
          case 2:
            params.put(START, DateUtils.minusDay(new Date(), 6));
            params.put(END, DateUtils.getStringToday());
            statusParams.put(TIME_LABEL, "最近7天");
            break;
          case 3:
            params.put(START, DateUtils.minusDay(new Date(), 29));
            params.put(END, DateUtils.getStringToday());
            statusParams.put(TIME_LABEL, "最近30天");
            break;
          case 4:
            getChildFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                    R.anim.slide_left_in, R.anim.slide_right_out)
                .replace(R.id.frag_student_filter, filterCustomFragment)
                .addToBackStack(null)
                .commit();
            return;
        }
        refresh();
        hideAll("time");
      }
    });

    if (!typeFilter.isAdded()) {
      getChildFragmentManager().beginTransaction()
          .add(R.id.frag_student_filter, typeFilter, "type")
          .commit();
    }
    if (!statusFilter.isAdded()) {
      getChildFragmentManager().beginTransaction()
          .add(R.id.frag_student_filter, statusFilter, "status")
          .commit();
    }
    if (!timeFitler.isAdded()) {
      getChildFragmentManager().beginTransaction()
          .add(R.id.frag_student_filter, timeFitler, "time")
          .commit();
    }
  }

  private int getStatusCode(String status) {
    switch (status) {
      case "已预约":
        return 1;
      case "已取消":
        return 2;
      case "已完成":
        return 3;
      case "已签课":
        return 4;
      case "已签到":
        return 5;
      case "已签出":
        return 6;
    }
    return 0;
  }

  private String getType(String type) {
    switch (type) {
      case "全部":
        return "";
      case "团课":
        return "group";
      case "私教":
        return "private";
      case "自由训练":
        return "checkin";
    }
    return "";
  }

  public void setOnNotSignFilterListener(OnNotSignFilterListener onNotSignFilterListener) {
    this.onNotSignFilterListener = onNotSignFilterListener;
  }

  private void refresh() {
    if (onNotSignFilterListener != null) {
      onNotSignFilterListener.onFilter(params);
      onNotSignFilterListener.onFilterStatus(statusParams);
    }
  }

  private void initStatusFilter(List<String> datas) {
    statusList.clear();
    statusList.addAll(datas);
    List<AbstractFlexibleItem> itemList = new ArrayList<>();
    for (String str : datas) {
      itemList.add(new FilterCommonLinearItem(str));
    }
    statusFilter.setItemList(itemList);
  }

  public void show(String s) {
    hideAllandShow(s);
  }

  public void initGymFilter(List<ClassRecords.Shop> shops, String curShopId) {
    if (shops == null) {
      shops = new ArrayList<>();
    }
    shopList.clear();
    shopList.addAll(shops);
    List<AbstractFlexibleItem> itemList = new ArrayList<>();
    itemList.add(new FilterCommonLinearItem("全部"));
    for (ClassRecords.Shop shop : shops) {
      itemList.add(new FilterCommonLinearItem(shop.name));
      if (shop.id.equals(curShopId)) {
        statusParams.put(SHOP_NAME, shop.name);
      }
    }
    gymFilter.setItemList(itemList);
    if (shops.size() == 0) {
      params.put(SHOP_ID, "0");
    }
    if (!gymFilter.isAdded()) {
      getChildFragmentManager().beginTransaction()
          .add(R.id.frag_student_filter, gymFilter, "gym")
          .commit();
    }
  }

  @Override public void onSettingData(String start, String end) {
    statusParams.put(TIME_LABEL,
        start.substring(5, start.length()) + "至" + end.substring(5, end.length()));
    params.put(START, start);
    params.put(END, end);
    refresh();
    onBack();
    hideAll("time");
  }

  public void hideAllandShow(String m) {
    FragmentTransaction ft = getChildFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out);
    for (String s : filterType) {
      Fragment f1 = getChildFragmentManager().findFragmentByTag(s);
      if (f1 != null) {
        if (s.equals(m)) {
          ft.show(f1);
        } else {
          ft.hide(f1);
        }
      }
    }
    ft.commit();
  }

  private void hideAll(String m) {
    FragmentTransaction ft = getChildFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out);
    for (String s : filterType) {
      Fragment f1 = getChildFragmentManager().findFragmentByTag(s);
      if (f1 != null) {
        if (s.equals(m)) {
          ft.hide(f1);
        }
      }
    }
    ft.commit();
  }

  @Override public void onBack() {
    getChildFragmentManager().popBackStackImmediate();
  }

  public interface OnNotSignFilterListener {
    void onFilter(HashMap<String, Object> params);

    void onFilterStatus(HashMap<String, Object> params);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
