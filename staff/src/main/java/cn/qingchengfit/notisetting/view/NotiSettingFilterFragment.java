package cn.qingchengfit.notisetting.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import cn.qingchengfit.common.FilterTimesFragment;
import cn.qingchengfit.common.FilterTimesFragmentBuilder;
import cn.qingchengfit.events.EventFilterDate;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.FilterFragment;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/8/1.
 */
public class NotiSettingFilterFragment extends BaseFragment {

  public int showPos = 0;
  FilterFragment channelFilterFragment;
  FilterFragment statusFilterFragment;
  FilterTimesFragment timesFilterFragment;
  HashMap<String, Object> params = new HashMap<>();
  private String[] filters = new String[] { "channel", "status", "times" };
  private FilterResult filterResultListener;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    channelFilterFragment = new FilterFragment();
    statusFilterFragment = new FilterFragment();
    timesFilterFragment = new FilterTimesFragmentBuilder(-1, 1).build();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    FrameLayout frameLayout = new FrameLayout(getContext());
    frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
    frameLayout.setId(R.id.frag_noti_setting_filter);
    RxBusAdd(EventFilterDate.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventFilterDate>() {
          @Override public void call(EventFilterDate eventFilterDate) {
            params.put("start", eventFilterDate.getStart());
            params.put("end", eventFilterDate.getEnd());
            onResult();
          }
        });
    showPos(showPos);
    return frameLayout;
  }

  @Override public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    if (!hidden) {
      showPos(showPos);
    }
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  public void showPos(final int pos) {
    if (pos < 0 || pos > 2) return;
    FragmentTransaction ts = getChildFragmentManager().beginTransaction();
    Fragment f = getChildFragmentManager().findFragmentByTag(filters[pos]);
    for (int i = 0; i < 3; i++) {
      if (i != pos) {
        Fragment fra = getChildFragmentManager().findFragmentByTag(filters[i]);
        if (fra != null) ts.hide(fra);
      }
    }

    if (f == null) {
      switch (pos) {
        case 1:
          List<AbstractFlexibleItem> statusItems = new ArrayList<>();
          for (String s : getResources().getStringArray(R.array.noti_setting_filter_status)) {
            statusItems.add(new FilterCommonLinearItem(s));
          }
          statusFilterFragment.setItemList(statusItems);
          statusFilterFragment.setOnSelectListener(new FilterFragment.OnSelectListener() {
            @Override public void onSelectItem(int position) {
              if (position == 0) {
                params.put("is_successful", null);
              } else if (position == 1) {//发送成功
                params.put("is_successful", 1);
              } else if (position == 2) {//发送失败
                params.put("is_successful", 0);
              }
              onResult();
            }
          });
          f = statusFilterFragment;
          break;
        case 2:
          f = timesFilterFragment;

          break;
        default:
          List<AbstractFlexibleItem> channelItems = new ArrayList<>();
          for (String s : getResources().getStringArray(R.array.noti_setting_filter_channel)) {
            channelItems.add(new FilterCommonLinearItem(s));
          }
          channelFilterFragment.setItemList(channelItems);
          channelFilterFragment.setOnSelectListener(new FilterFragment.OnSelectListener() {
            @Override public void onSelectItem(int position) {
              //筛选结果
              if (position == 0) {
                params.put("type", null);
              } else {
                params.put("type", position);
              }
              onResult();
            }
          });
          f = channelFilterFragment;
          break;
      }
      ts.add(R.id.frag_noti_setting_filter, f, filters[pos]);
    }
    ts.setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out);
    ts.show(f);
    ts.commit();
  }

  void onResult() {
    if (filterResultListener != null) {
      filterResultListener.onFilterResult(params);
      int type = -1;
      if (params.get("type") != null) type = (int) params.get("type");
      int status = -1;
      if (params.get("is_successful") != null) {
        status = ((int) params.get("is_successful") == 1) ? 1 : 2;
      }
      String t = null;
      if (params.get("start") != null && params.get("end") != null) {
        String start = (String) params.get("start");
        String end = (String) params.get("end");
        if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end)) {
          t = start + "至" + end;
        }
      }

      filterResultListener.onFilterString(type == -1 ? null
              : getResources().getStringArray(R.array.noti_setting_filter_channel)[type],
          status == -1 ? null
              : getResources().getStringArray(R.array.noti_setting_filter_status)[status], t);
    }
  }

  public FilterResult getFilterResultListener() {
    return filterResultListener;
  }

  public void setFilterResultListener(FilterResult filterResultListener) {
    this.filterResultListener = filterResultListener;
  }

  @Override public String getFragmentName() {
    return NotiSettingFilterFragment.class.getName();
  }

  public interface FilterResult {
    void onFilterResult(HashMap<String, Object> p);

    void onFilterString(String channel, String status, String time);
  }
}
