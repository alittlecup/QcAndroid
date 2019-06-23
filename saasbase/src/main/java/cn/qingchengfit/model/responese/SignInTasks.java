package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.Staff;
import com.google.gson.annotations.SerializedName;
import cn.qingchengfit.model.base.Space;
import java.util.List;

/**
 * Created by yangming on 16/9/5.
 * 会员签到/签出状态：
 * status:0 已签到
 * status:1 已撤销
 * status:2 待签到
 * status:3 待签出
 * status:4 已签出
 */
public class SignInTasks {

  @SerializedName("data") public Data data;

  public static class SignInTask {

    @SerializedName("status") private Integer status;
    @SerializedName("card_name") private String cardName;
    @SerializedName("user_phone") private String userPhone;
    @SerializedName("user_avatar") private String userAvatar;
    @SerializedName("checkin_avatar") private String checkinAvatar;
    @SerializedName("created_by_name") private String createdByName;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("checkout_at") private String checkoutAt;
    @SerializedName("checkout_by") private CheckOutBy checkoutBy;
    @SerializedName("pop") private Boolean pop;
    @SerializedName("shop_name") private String shopName;
    @SerializedName("shop_id") private Integer shopId;
    @SerializedName("schedules") private List<Schedule> schedules;//= new ArrayList<Schedule>();
    @SerializedName("user_gender") private Integer userGender;
    @SerializedName("user_id") private String userId;
    @SerializedName("status_text") private String statusText;
    @SerializedName("user_name") private String userName;
    @SerializedName("id") private Integer id;
    @SerializedName("unit") private String unit;
    @SerializedName("cost") private float cost;
    @SerializedName("locker") private Locker locker;

    @SerializedName("card") private Card card;

    @SerializedName("modify_at") private String modifyAt;
    @SerializedName("order") private SigninReportDetail.CheckinsBean.CheckinOrder order;

    public String getCheckinAvatar() {
      return checkinAvatar;
    }

    public void setCheckinAvatar(String checkinAvatar) {
      this.checkinAvatar = checkinAvatar;
    }

    public String getModifyAt() {
      return modifyAt;
    }

    public void setModifyAt(String modifyAt) {
      this.modifyAt = modifyAt;
    }

    public Card getCard() {
      return card;
    }

    public void setCard(Card card) {
      this.card = card;
    }

    public String getCheckoutAt() {
      return checkoutAt;
    }

    public void setCheckoutAt(String checkoutAt) {
      this.checkoutAt = checkoutAt;
    }

    public CheckOutBy getCheckoutBy() {
      return checkoutBy;
    }

    public void setCheckoutBy(CheckOutBy checkoutBy) {
      this.checkoutBy = checkoutBy;
    }

    /**
     * @return The status
     */
    public Integer getStatus() {
      return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
      this.status = status;
    }

    /**
     * @return The cardName
     */
    public String getCardName() {
      return cardName;
    }

    /**
     * @param cardName The card_name
     */
    public void setCardName(String cardName) {
      this.cardName = cardName;
    }

    /**
     * @return The userPhone
     */
    public String getUserPhone() {
      return userPhone;
    }

    /**
     * @param userPhone The user_phone
     */
    public void setUserPhone(String userPhone) {
      this.userPhone = userPhone;
    }

    /**
     * @return The userAvatar
     */
    public String getUserAvatar() {
      return userAvatar;
    }

    /**
     * @param userAvatar The user_avatar
     */
    public void setUserAvatar(String userAvatar) {
      this.userAvatar = userAvatar;
    }

    /**
     * @return The createdByName
     */
    public String getCreatedByName() {
      return createdByName;
    }

    /**
     * @param createdByName The created_by_name
     */
    public void setCreatedByName(String createdByName) {
      this.createdByName = createdByName;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
      return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
      this.createdAt = createdAt;
    }

    /**
     * @return The pop
     */
    public Boolean getPop() {
      return pop;
    }

    /**
     * @param pop The pop
     */
    public void setPop(Boolean pop) {
      this.pop = pop;
    }

    /**
     * @return The shopName
     */
    public String getShopName() {
      return shopName;
    }

    /**
     * @param shopName The shop_name
     */
    public void setShopName(String shopName) {
      this.shopName = shopName;
    }

    /**
     * @return The shopId
     */
    public Integer getShopId() {
      return shopId;
    }

    /**
     * @param shopId The shop_id
     */
    public void setShopId(Integer shopId) {
      this.shopId = shopId;
    }

    /**
     * @return The schedules
     */
    public List<Schedule> getSchedules() {
      return schedules;
    }

    /**
     * @param schedules The schedules
     */
    public void setSchedules(List<Schedule> schedules) {
      this.schedules = schedules;
    }

    /**
     * @return The userGender
     */
    public Integer getUserGender() {
      return userGender;
    }

    /**
     * @param userGender The user_gender
     */
    public void setUserGender(Integer userGender) {
      this.userGender = userGender;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
      return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
      this.userId = userId;
    }

    /**
     * @return The statusText
     */
    public String getStatusText() {
      return statusText;
    }

    /**
     * @param statusText The status_text
     */
    public void setStatusText(String statusText) {
      this.statusText = statusText;
    }

    /**
     * @return The userName
     */
    public String getUserName() {
      return userName;
    }

    /**
     * @param userName The user_name
     */
    public void setUserName(String userName) {
      this.userName = userName;
    }

    /**
     * @return The id
     */
    public Integer getId() {
      return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
      this.id = id;
    }

    /**
     * @return The unit
     */
    public String getUnit() {
      return unit;
    }

    /**
     * @param unit The unit
     */
    public void setUnit(String unit) {
      this.unit = unit;
    }

    /**
     * @return The cost
     */
    public float getCost() {
      return cost;
    }

    /**
     * @param cost The cost
     */
    public void setCost(float cost) {
      this.cost = cost;
    }

    /**
     * @return The locker
     */
    public Locker getLocker() {
      return locker;
    }

    /**
     * @param locker The locker
     */
    public void setLocker(Locker locker) {
      this.locker = locker;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      SignInTask that = (SignInTask) o;

      return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override public int hashCode() {
      return id != null ? id.hashCode() : 0;
    }

    public SigninReportDetail.CheckinsBean.CheckinOrder getOrder() {
      return order;
    }

    public void setOrder(SigninReportDetail.CheckinsBean.CheckinOrder order) {
      this.order = order;
    }
  }

  public class Data {
    @SerializedName("check_in") public List<SignInTask> check_in;
  }

  public class Schedule {

    @SerializedName("teacher") private Staff teacher;
    @SerializedName("space") private  Space space;
    @SerializedName("name") private String name;
    @SerializedName("time") private String time;

    /**
     * @return The teacher
     */
    public Staff getTeacher() {
      return teacher;
    }

    /**
     * @param teacher The teacher
     */
    public void setTeacher(Staff teacher) {
      this.teacher = teacher;
    }

    /**
     * @return The space
     */
    public Space getSpace() {
      return space;
    }

    /**
     * @param space The space
     */
    public void setSpace(Space space) {
      this.space = space;
    }

    /**
     * @return The name
     */
    public String getName() {
      return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * @return The time
     */
    public String getTime() {
      return time;
    }

    /**
     * @param time The time
     */
    public void setTime(String time) {
      this.time = time;
    }
  }

  public class Card {

    @SerializedName("balance") private Float balance;
    @SerializedName("id") private Integer id;
    @SerializedName("card_tpl_type") private Integer cardtype;
    @SerializedName("name") private String name;
    @SerializedName("start") private String start;
    @SerializedName("end") private String end;

    public Integer getCardtype() {
      return cardtype;
    }

    public void setCardtype(Integer cardtype) {
      this.cardtype = cardtype;
    }

    public String getStart() {
      return start;
    }

    public void setStart(String start) {
      this.start = start;
    }

    public String getEnd() {
      return end;
    }

    public void setEnd(String end) {
      this.end = end;
    }

    public Float getBalance() {
      return balance;
    }

    public void setBalance(Float balance) {
      this.balance = balance;
    }

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public Integer getCardType() {
      return cardtype;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public class CheckOutBy {

    @SerializedName("username") private String username;
    @SerializedName("id") private Integer id;

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }
  }
}
