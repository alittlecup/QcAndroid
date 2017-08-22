package cn.qingchengfit.model.responese;

/**
 * Created by yangming on 16/9/29.
 */

public class SigninNoticeConfig {

    /**
     * id : 59
     * name : 798店F2
     * address : 706北三街
     */

    private ShopBean shop;
    /**
     * shop : {"id":59,"name":"798店F2","address":"706北三街"}
     * name : app签到/签出推送通知开关
     * key : app_checkin_notification
     * model : staff_gym
     * brand : {"id":2,"name":"青橙健身"}
     * id : 28
     * value : 1
     * user : {}
     */

    private String name;
    private String key;
    private String model;
    /**
     * id : 2
     * name : 青橙健身
     */

    private BrandBean brand;
    private int id;
    private int value;

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BrandBean getBrand() {
        return brand;
    }

    public void setBrand(BrandBean brand) {
        this.brand = brand;
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

    public static class ShopBean {
        private int id;
        private String name;
        private String address;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class BrandBean {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
