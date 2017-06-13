package cn.qingchengfit.staffkit.rxbus.event;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/4/20 2016.
 */
public class EditStudentEvent {
    public static final int ADD = 0;
    public static final int EDIT = 1;
    public static final int DEL = 2;
    int type;
    private String shop_ids;

    public EditStudentEvent(int type) {
        this.type = type;
    }

    private EditStudentEvent(Builder builder) {
        setType(builder.type);
        setShop_ids(builder.shop_ids);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShop_ids() {
        return shop_ids;
    }

    public void setShop_ids(String shop_ids) {
        this.shop_ids = shop_ids;
    }

    public static final class Builder {
        private int type;
        private String shop_ids;

        public Builder() {
        }

        public Builder type(int val) {
            type = val;
            return this;
        }

        public Builder shop_ids(String val) {
            shop_ids = val;
            return this;
        }

        public EditStudentEvent build() {
            return new EditStudentEvent(this);
        }
    }
}
