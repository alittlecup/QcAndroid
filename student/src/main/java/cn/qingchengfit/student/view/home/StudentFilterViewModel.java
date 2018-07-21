package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableField;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.filter.ItemFilterCommon;
import cn.qingchengfit.saascommon.filter.ItemFilterList;
import cn.qingchengfit.saascommon.filter.ItemFilterTime;
import cn.qingchengfit.saascommon.filter.model.Content;
import cn.qingchengfit.saascommon.filter.model.FilterModel;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.item.ItemFilterRecommend;
import cn.qingchengfit.student.item.ItemFilterSalerGrid;
import cn.qingchengfit.student.usercase.FilterUserCase;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/6.
 */

public class StudentFilterViewModel extends BaseViewModel
    implements ItemFilterTime.OnTimeChooseListener {

  public final ObservableField<List<AbstractFlexibleItem>> items = new ObservableField<>();
  private final ActionLiveEvent mResetEvent = new ActionLiveEvent();
  private String salerId;

  public MutableLiveData<Map<String, String>> getmFilterMap() {
    return mFilterMap;
  }

  private final MutableLiveData<Map<String, String>> mFilterMap = new MutableLiveData<>();

  public ActionLiveEvent getmResetEvent() {
    return mResetEvent;
  }

  public LiveData<List<FilterModel>> getRemoteFilters() {
    return remoteFilters;
  }

  private final ActionLiveEvent action = new ActionLiveEvent();
  protected final LiveData<List<FilterModel>> remoteFilters;

  public void setFilterTimeVisible(boolean filterTimeVisible) {
    this.filterTimeVisible = filterTimeVisible;
  }

  private boolean filterTimeVisible = true;

  @Inject protected FilterUserCase filterUserCase;
  @Inject protected LoginStatus loginStatus;
  @Inject protected GymWrapper gymWrapper;

  @Inject public StudentFilterViewModel() {
    remoteFilters = Transformations.switchMap(action,
        aVoid -> Transformations.map(filterUserCase.getFilterModel(), input -> input));
  }

  public void loadfilterModel() {
    action.call();
    filterUserCase.excute(loginStatus.staff_id(), salerId, gymWrapper.getParams());
  }

  public void onReset() {
    mResetEvent.call();
  }

  public void onConfirm() {
    for (AbstractFlexibleItem item : items.get()) {
      getDataFromItem(item);
    }
    mFilterMap.setValue(filterMap);
  }

  public Map<String, String> filterMap = new HashMap<>();

  private void getDataFromItem(AbstractFlexibleItem item) {
    if (item instanceof ItemFilterCommon) {
      List<Content> checkedContent = ((ItemFilterCommon) item).getCheckedContent();
      String key = ((ItemFilterCommon) item).getData().key;
      if (checkedContent.isEmpty()) {
        filterMap.remove(key);
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        for (Content content : checkedContent) {
          stringBuilder.append(content.value).append(",");
        }
        filterMap.put(key, stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
      }
    } else if (item instanceof ItemFilterList) {
      String selectedUser = ((ItemFilterList) item).getSelectedUser();
      String key = ((ItemFilterList) item).getFilterModel().key;
      if (StringUtils.isEmpty(selectedUser)) {
        filterMap.remove(key);
      } else {
        filterMap.put(key, selectedUser);
      }
    } else if (item instanceof ItemFilterRecommend) {
      String selectedUser = ((ItemFilterRecommend) item).getSelectedSaler();
      String key = ((ItemFilterRecommend) item).getFilterModel().key;
      if (StringUtils.isEmpty(selectedUser)) {
        filterMap.remove(key);
      } else {
        filterMap.put(key, selectedUser);
      }
    } else if (item instanceof ItemFilterSalerGrid) {
      String selectedUser = ((ItemFilterSalerGrid) item).getSelectedSaler();
      String key = ((ItemFilterSalerGrid) item).getFilterModel().key;
      if (StringUtils.isEmpty(selectedUser)) {
        filterMap.remove(key);
      } else {
        filterMap.put(key, selectedUser);
      }
    }
  }

  /**
   * 用于避免在VM中持有Recycyler,Adapter等，但是不可避免的会持有Item,有待优化。
   */
  public List<AbstractFlexibleItem> getItems(List<FilterModel> filters) {
    List<AbstractFlexibleItem> itemList = new ArrayList<>();
    if (filters == null || filters.isEmpty()) return itemList;

    for (FilterModel filter : filters) {
      if (filter.type == 2) {
        if (filterTimeVisible) {
          itemList.add(new ItemFilterTime(filter, this));
        }
      } else if (filter.type == 3) {
        itemList.add(new ItemFilterList(filter));
      } else if (filter.type == 5) {
        itemList.add(new ItemFilterRecommend(filter));
      } else if (filter.type == 6) {
        itemList.add(new ItemFilterSalerGrid(filter));
      } else {
        itemList.add(new ItemFilterCommon(filter, true));
      }
    }
    return itemList;
  }

  @Override public void onTimeStart(String start, String key) {
    filterMap.put("start", start);
  }

  @Override public void onTimeEnd(String end, String key) {
    filterMap.put("end", end);
  }

  public void setSalerId(String salerId) {
    this.salerId = salerId;
  }
}
