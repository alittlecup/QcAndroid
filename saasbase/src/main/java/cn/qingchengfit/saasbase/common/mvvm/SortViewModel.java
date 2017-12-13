package cn.qingchengfit.saasbase.common.mvvm;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

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
        List<StudentItem> value = sortLatest(items);
        if (listener != null) {
            listener.onSortFinish(new ArrayList<>(value));
        }
    }

    @NonNull
    private <T extends StudentItem> List<T> sortLatest(List<T> items) {
        Collections.sort(items, (r1, r2) -> r2.getQcStudentBean().joined_at.compareTo(r1.getQcStudentBean().joined_at));
        return items;
    }


    public void onLetterClick(List items, boolean isChecked) {
        latestChecked.set(false);
        letterChecked.set(true);
        if (isChecked) return;
        if (items == null || items.isEmpty()) return;
        List<AbstractFlexibleItem> letterList = sortLetter(items);
        if (listener != null) {
            listener.onSortFinish(letterList);
        }
    }

    @NonNull
    private <T extends StudentItem> List<AbstractFlexibleItem> sortLetter(List<T> items) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#";

        Collections.sort(items, (staffDetailItem, t1) -> staffDetailItem.getQcStudentBean().head.compareTo(t1.getQcStudentBean().head));
        for (T item : items) {
            if (StringUtils.isEmpty(item.getQcStudentBean().head) || !letters.contains(item.getQcStudentBean().head())) {
                item.getQcStudentBean().setHead("~");
            }
        }
        List<AbstractFlexibleItem> values = new ArrayList<>();
        String head = items.get(0).getQcStudentBean().head.toUpperCase();
        values.add(new StickerDateItem(head));
        for (T item : items) {
            if (!item.getQcStudentBean().head().equalsIgnoreCase(head)) {
                head = item.getQcStudentBean().head().toUpperCase();
                values.add(new StickerDateItem(head));
            }
            values.add(item);
        }
        return values;
    }

    public <T extends StudentItem> List<AbstractFlexibleItem> sortItems(List<T> items) {
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
        void onSortFinish(List<AbstractFlexibleItem> items);

    }

}
