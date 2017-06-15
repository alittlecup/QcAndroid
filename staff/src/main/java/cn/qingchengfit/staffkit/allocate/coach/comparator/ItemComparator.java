package cn.qingchengfit.staffkit.allocate.coach.comparator;

import cn.qingchengfit.staffkit.allocate.CommonAllocateDetailItem;
import java.util.Comparator;

/**
 * Created by fb on 2017/5/5.
 */

public class ItemComparator implements Comparator<CommonAllocateDetailItem> {

    @Override public int compare(CommonAllocateDetailItem commonAllocateDetailItem, CommonAllocateDetailItem t1) {
        return (commonAllocateDetailItem.getData().head()).compareTo(t1.getData().head());
    }
}
