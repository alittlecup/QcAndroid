package cn.qingchengfit.staffkit.allocate.coach.comparator;

import cn.qingchengfit.staffkit.allocate.CommonAllocateDetailItem;
import java.util.Comparator;

/**
 * Created by fb on 2017/5/5.
 */

public class ItemComparatorJoinAt implements Comparator<CommonAllocateDetailItem> {
    @Override public int compare(CommonAllocateDetailItem commonAllocateDetailItem, CommonAllocateDetailItem t1) {
        return (commonAllocateDetailItem.getData().joined_at).compareTo(t1.getData().joined_at);
    }
}
