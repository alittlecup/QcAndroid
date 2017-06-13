package cn.qingchengfit.staffkit.allocate.coach.event;

import cn.qingchengfit.staffkit.allocate.CommonAllocateDetailItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/5/3.
 */

public class SortEvent {

    public static final int SORT_BY_REGEISTER = 101;
    public static final int SORT_BY_ALPHABET = 102;

    public int SORT_TYPE;
    public List<CommonAllocateDetailItem> datas = new ArrayList<>();

    public SortEvent(int SORT_TYPE, List<CommonAllocateDetailItem> datas) {
        this.SORT_TYPE = SORT_TYPE;
        this.datas = datas;
    }
}
