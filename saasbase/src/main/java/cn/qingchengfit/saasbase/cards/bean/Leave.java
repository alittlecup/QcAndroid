package cn.qingchengfit.saasbase.cards.bean;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.utils.DateUtils;

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
 * 请假记录 bean
 * <p>
 * Created by Paper on 16/3/31 2016.
 */
public class Leave {
    private String id;
    private String message;
    private String remarks;
    private String start;
    private String end;
    private String cancel_at;
    private String created_at;
    private String card_id;
    private Staff created_by;
    private Staff cancel_by;
    private int status;
    private int price;

    public OffDay toOffDay() {
        OffDay offDay = new OffDay();
        offDay.title = message;
        offDay.cancel = status == Configs.OFFDAY_OFF;
        offDay.controler = offDay.cancel ? cancel_by.getUsername() : created_by.getUsername();
        offDay.time = offDay.cancel ? DateUtils.formatToMMFromServer(cancel_at) : DateUtils.formatToMMFromServer(created_at);
        offDay.price = Integer.toString(price);
        offDay.marks = remarks;
        offDay.start = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(start));
        offDay.end = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(end));
        offDay.id = id;
        offDay.status = status;
        return offDay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getCancel_at() {
        return cancel_at;
    }

    public void setCancel_at(String cancel_at) {
        this.cancel_at = cancel_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public Staff getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Staff created_by) {
        this.created_by = created_by;
    }

    public Staff getCancel_by() {
        return cancel_by;
    }

    public void setCancel_by(Staff cancel_by) {
        this.cancel_by = cancel_by;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
