package cn.qingchengfit.staffkit.rxbus.event;

/**
 * Created by yangming on 16/9/29.
 */

public class SignInNoticeConfigEvent {
    private int brandId;
    private int shopId;
    private int id;
    private int value;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
