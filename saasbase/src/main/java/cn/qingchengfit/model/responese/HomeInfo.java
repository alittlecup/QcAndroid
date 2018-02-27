package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/8/5 2015.
 */
public class HomeInfo {

    int services_total_count;
    List<Banner> banners;
    HomeStatement stat;
    int qingcheng_activity_count;

    public int getQingcheng_activity_count() {
        return qingcheng_activity_count;
    }

    public void setQingcheng_activity_count(int qingcheng_activity_count) {
        this.qingcheng_activity_count = qingcheng_activity_count;
    }

    public int getServices_total_count() {
        return services_total_count;
    }

    public void setServices_total_count(int services_total_count) {
        this.services_total_count = services_total_count;
    }

    public List<Banner> getBanners() {
        if (banners == null) return new ArrayList<>();
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public HomeStatement getStat() {
        return stat;
    }

    public void setStat(HomeStatement stats) {
        this.stat = stats;
    }

    public class Stat {
        @SerializedName("name") String name;
        @SerializedName("value") String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
