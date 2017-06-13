package cn.qingchengfit.staffkit.rxbus.event;

/**
 * Created by yangming on 16/10/17.
 */

public class AllotSaleEvent {

    public boolean isSearching = false;

    public AllotSaleEvent(boolean isSearching) {
        this.isSearching = isSearching;
    }

    public AllotSaleEvent() {
    }

    public boolean isSearching() {
        return isSearching;
    }

    public void setSearching(boolean searching) {
        isSearching = searching;
    }
}
