package cn.qingchengfit.staffkit.views.student.filter;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * // 推荐人 list item 点击
 * //Created by yangming on 16/12/3.
 */

/**
 * 0 # 新注册
 * 1 # 跟进中
 * 2 # 会员
 * 3 # 非会员
 */
public class StudentFilterEvent {

    public static final int EVENT_REFERRER_ITEM_CLICK = 1; // 筛选-推荐人-item click
    public static final int EVENT_REFERRER_SHOWALL_CLICK = 2; // filter-referrer-show-all
    public static final int EVENT_SOURCE_SHOWALL_CLICK = 3; // filter source show all
    public static final int EVENT_STATUS_CLICK = 4; // filter status click
    public static final int EVENT_SOURCE_ITEM_CLICK = 5; // 筛选-来源-item click
    public static final int EVENT_CLOSE = 6; // 筛选-来源-item click
    public static final int EVENT_RESET_CLICK = 88; // 筛选-来源-item click
    public static final int EVENT_CONFIRM_CLICK = 99; // 筛选-来源-item click

    public static final int EVENT_FROM_STUDENTLIST = 101;   // 事件来源：会员列表
    public static final int EVENT_FROM_SALELIST = 102;      //事件来源：名下会员列表
    public static final int EVENT_FROM_FOLLOW_UP = 103;      //事件来源：会员跟进

    public int eventType = -1; //事件类型
    public int position = -1; // referrer item click position
    public int status = -1; //
    public int eventFrom = -1;
    public StudentFilter filter;

    public StudentFilterEvent(int eventType, int position, int status, StudentFilter filter, int eventFrom) {
        this.eventType = eventType;
        this.position = position;
        this.status = status;
        this.filter = filter;
        this.eventFrom = eventFrom;
    }

    public StudentFilterEvent() {
    }
}
