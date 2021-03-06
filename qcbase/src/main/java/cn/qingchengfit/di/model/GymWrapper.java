package cn.qingchengfit.di.model;

import android.text.TextUtils;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.GymUtils;
import java.util.HashMap;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/3/1.
 */

public class GymWrapper {

  private CoachService coachService;
  private Brand brand;
  private SuperUser superuser;
  private String custumNo;  //pos机的客户号
  private boolean noService;
  private boolean isOutOfDate;
  private boolean isSingleMode;
  private boolean hasFirst; //是否有首月优惠
  private String firstPrice;//首月优惠价格
  private String socket_channel_id;

  @Inject
  public GymWrapper(){

  }

  public GymWrapper(Builder builder) {
    coachService = builder.coachService;
    brand = builder.brand;
  }

  public String getCustumNo() {
    return custumNo;
  }

  public void setCustumNo(String custumNo) {
    this.custumNo = custumNo;
  }

  public boolean isNoService() {
    return noService;
  }

  public void setNoService(boolean noService) {
    this.noService = noService;
  }

  public boolean isOutOfDate() {
    return isOutOfDate;
  }

  public void setOutOfDate(boolean outOfDate) {
    isOutOfDate = outOfDate;
  }

  public boolean inBrand() {
    return coachService == null || TextUtils.isEmpty(coachService.getId());
  }

  public String getSocket_channel_id() {
    return socket_channel_id;
  }

  public void setSocket_channel_id(String socket_channel_id) {
    this.socket_channel_id = socket_channel_id;
  }

  public boolean isHasFirst() {
    return hasFirst;
  }

  public void setHasFirst(boolean hasFirst) {
    this.hasFirst = hasFirst;
  }

  public String getFirstPrice() {
    return firstPrice;
  }

  public void setFirstPrice(String firstPrice) {
    this.firstPrice = firstPrice;
  }

  public boolean isPro() {
    return GymUtils.getSystemEndDay(coachService) >= 0 && !isOutOfDate;
  }

  public HashMap<String, Object> getParams() {
    return GymUtils.getParams(coachService, brand);
  }

  public HashMap<String, Object> getShopParams() {
    if (!TextUtils.isEmpty(coachService.shop_id()) && TextUtils.isEmpty(coachService.brand_id())) {
      HashMap<String, Object> hashMap = new HashMap<>();
      hashMap.put("brand_id", coachService.brand_id());
      hashMap.put("shop_id", coachService.shop_id());
      return hashMap;
    } else {
      return GymUtils.getParams(coachService, brand);
    }
  }

  public String id() {
    if (coachService != null) {
      return coachService.id();
    } else {
      return "";
    }
  }


  public String getGymId(){
    if (coachService != null) {
      return coachService.gym_id();
    } else {
      return "";
    }
  }

  /**
   * 是否为单场馆模式
   */
  public boolean singleMode() {
    return isSingleMode;
  }

  public void setSingleMode(boolean s) {
    this.isSingleMode = s;
  }

  public String model() {
    if (coachService != null) {
      return coachService.model();
    } else {
      return "";
    }
  }

  public String system_end() {
    if (coachService != null) {
      return coachService.system_end();
    } else {
      return "";
    }
  }

  public String photo() {
    if (coachService != null) {
      return coachService.photo();
    } else {
      return "";
    }
  }

  public String phone() {
    if (coachService != null) {
      return coachService.phone();
    } else {
      return "";
    }
  }

  public String shop_id() {
    if (coachService != null) {
      return coachService.shop_id();
    } else {
      return "";
    }
  }

  public String name() {
    if (coachService != null) {
      return coachService.name();
    } else {
      return "";
    }
  }

  public boolean can_trial() {
    if (coachService != null) {
      return coachService.can_trial();
    } else {
      return false;
    }
  }

  public String brand_id() {
    if (brand != null) return brand.getId();
    if (coachService != null) return coachService.brand_id();
    return "";
  }

  public String brand_name() {
    if (brand != null) return brand.getName();
    if (coachService != null) return coachService.brand_name();
    return "";
  }

  public CoachService getCoachService() {
    return coachService;
  }

  public void setCoachService(CoachService coachService) {
    this.coachService = coachService;
  }

  public void setSuperuser(SuperUser superuser) {
    this.superuser = superuser;
  }

  public Brand getBrand() {
    if (brand == null && coachService != null) {
      return new Brand.Builder().id(coachService.brand_id())
          .name(coachService.brand_name())
          .build();
    }
    return brand;
  }

  public void setBrand(Brand brand) {
    this.brand = brand;
  }

  public static class Builder {
    private CoachService coachService;
    private Brand brand;

    public Builder() {
    }

    public Builder coachService(CoachService val) {
      coachService = val;
      return this;
    }

    public Builder brand(Brand val) {
      brand = val;
      return this;
    }

    public GymWrapper build() {
      return new GymWrapper(this);
    }
  }
}
