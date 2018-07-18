package cn.qingchengfit.student.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.saascommon.item.IItemData;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.item.ChooseDetailItem;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by huangbaole on 2017/11/22.
 */

public class SortViewModel {
  // REFACTOR: 2017/11/23 按钮选中之后点击颜色变化
  public final ObservableBoolean latestChecked = new ObservableBoolean(true);
  public final ObservableBoolean letterChecked = new ObservableBoolean(false);
  public final ObservableBoolean filterChecked = new ObservableBoolean(false);

  public ActionLiveEvent getFilterEvent() {
    return filterEvent;
  }

  private final ActionLiveEvent filterEvent = new ActionLiveEvent();

  public void onLatestClick(List items, boolean isChecked) {
    latestChecked.set(true);
    letterChecked.set(false);
    if (isChecked) return;
    if (items == null || items.isEmpty()) return;
    List<IItemData> value = sortLatest(items);
    if (listener != null) {
      listener.onSortFinish(new ArrayList<>(value));
    }
  }

  @NonNull private <T extends IItemData> List<T> sortLatest(List<T> items) {
    Collections.sort(items, (r1, r2) -> {
      if (!TextUtils.isEmpty(r1.getData().join_at())) {
        return r2.getData().join_at().compareTo(r1.getData().join_at());
      } else if (!TextUtils.isEmpty(r1.getData().joined_at)) {
        return r2.getData().joined_at.compareTo(r1.getData().joined_at);
      }
      return 1;
    });
    for (T t : items) {
      if (t.getHeader() != null) {
        t.setHeader(null);
      }
    }
    return items;
  }


  public void onLetterClick(List items, boolean isChecked) {
    latestChecked.set(false);
    letterChecked.set(true);
    if (isChecked) return;
    if (items == null || items.isEmpty()) return;
    List<StudentItem> letterList = sortLetter(items);
    if (listener != null) {
      listener.onSortFinish(letterList);
    }
  }

  @NonNull private  <T extends IItemData>List<IItemData> sortLetter(List<T> items) {
    String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#";

    Collections.sort(items,
        (staffDetailItem, t1) -> staffDetailItem.getData().head.compareTo(
            t1.getData().head));
    for (IItemData item : items) {
      if (StringUtils.isEmpty(item.getData().head) || !letters.contains(
          item.getData().head())) {
        item.getData().setHead("~");
      }
    }
    List<IItemData> values = new ArrayList<>();
    IItemData first = items.get(0);
    String head = first.getData().head.toUpperCase();
    first.setHeader(new StickerDateItem(head));
    for (IItemData item : items) {
      if (!item.getData().head().equalsIgnoreCase(head)) {
        head = item.getData().head().toUpperCase();
        item.setHeader(new StickerDateItem(head));
      }
      values.add(item);
    }
    return values;
  }


  public <T extends IItemData> List<IItemData> sortItems(List<T> items) {

    if (latestChecked.get()) {
      return new ArrayList<>(sortLatest(items));
    } else if (letterChecked.get()) {
      return sortLetter(items);
    }
    return new ArrayList<>();
  }


  public void onFilterClick(boolean isChecked) {
    if (isChecked) return;
    filterEvent.call();
  }

  private onSortFinishListener listener;

  public void setListener(onSortFinishListener listener) {
    this.listener = listener;
  }

  public interface onSortFinishListener {
    void onSortFinish(List<? extends IItemData> items);
  }
}
