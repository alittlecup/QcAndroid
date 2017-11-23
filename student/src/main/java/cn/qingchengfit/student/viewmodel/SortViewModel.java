package cn.qingchengfit.student.viewmodel;

import android.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.student.items.StaffDetailItem;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/11/22.
 */

public class SortViewModel {
    // REFACTOR: 2017/11/23 按钮选中之后点击颜色变化
    public final ObservableBoolean latestChecked = new ObservableBoolean(true);
    public final ObservableBoolean letterChecked = new ObservableBoolean(true);
    public final ObservableBoolean filterChecked = new ObservableBoolean(false);

    public void onLatestClick(List items, boolean isChecked) {
        latestChecked.set(true);
        letterChecked.set(false);
        if (!isChecked) return;
        List<StaffDetailItem> value = new ArrayList<>();
        for (Object item : items) {
            if (item instanceof StaffDetailItem) {
                value.add((StaffDetailItem) item);
            }
        }
        Collections.sort(value, (r1, r2) -> r1.getQcStudentBean().joined_at.compareTo(r2.getQcStudentBean().joined_at));
        if (listener != null) {
            listener.onLatestSortFinish(new ArrayList<>(value));
        }
    }


    public void onLetterClick(List items, boolean isChecked) {
        latestChecked.set(false);
        letterChecked.set(true);
        if (!isChecked) return;
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#";
        List<StaffDetailItem> value = new ArrayList<>();
        for (Object item : items) {
            if (item instanceof StaffDetailItem) {
                value.add((StaffDetailItem) item);
            }
        }
        Collections.sort(value, (staffDetailItem, t1) -> staffDetailItem.getQcStudentBean().head.compareTo(t1.getQcStudentBean().head));
        for (StaffDetailItem item : value) {
            if (StringUtils.isEmpty(item.getQcStudentBean().head) || !letters.contains(item.getQcStudentBean().head())) {
                item.getQcStudentBean().setHead("~");
            }
        }
        items.clear();
        String head = value.get(0).getQcStudentBean().head.toUpperCase();
        List<String> letterList = new ArrayList<>();
        items.add(new StickerDateItem(head));
        letterList.add(head);
        for (StaffDetailItem item : value) {
            if (!item.getQcStudentBean().head().equalsIgnoreCase(head)) {
                head = item.getQcStudentBean().head().toUpperCase();
                items.add(new StickerDateItem(head));
                letterList.add(head);
            }
            items.add(item);
        }
        if (listener != null) {
            listener.onlettersSortFinish(new ArrayList<>(items), letterList);
        }
    }


    public void onFilterClick(boolean isChecked) {
        if (!isChecked) return;
    }

    private onSortFinishListener listener;

    public void setListener(onSortFinishListener listener) {
        this.listener = listener;
    }

    public interface onSortFinishListener {
        void onLatestSortFinish(List<AbstractFlexibleItem> items);

        void onlettersSortFinish(List<AbstractFlexibleItem> items, List<String> letters);
    }

}
