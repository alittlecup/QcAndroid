package cn.qingchengfit.staffkit.rxbus.event;

/**
 * Created by yangming on 16/10/18.
 */

public class AllotSaleSelectAllEvent {
    public static final boolean TYPE_SELECT_ALL = true;
    public static final boolean TYPE_REMOVE_ALL = false;

    public boolean isSelectAll = false;

    public AllotSaleSelectAllEvent(boolean isSelectAll) {
        this.isSelectAll = isSelectAll;
    }
}
