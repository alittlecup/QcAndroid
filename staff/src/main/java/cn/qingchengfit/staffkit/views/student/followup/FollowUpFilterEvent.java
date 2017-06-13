package cn.qingchengfit.staffkit.views.student.followup;

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

import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;

/**
 * 0 # 新注册
 * 1 # 跟进中
 * 2 # 会员
 * 3 # 非会员
 */
public class FollowUpFilterEvent {

    public static final int EVENT_SALE_ITEM_CLICK = 1; // 销售-item click
    public static final int EVENT_REFERRER_ITEM_CLICK = 4; // 推荐人-item click
    public static final int EVENT_SOURCE_ITEM_CLICK = 5; // 来源-item click
    public static final int EVENT_LATEST_TIME_CLICK = 6; // 最近跟进时间-item click
    public static final int EVENT_LATEST_TIME_CUSTOM_DATA = 7; // 最近跟进时间-item click
    public static final int EVENT_LATEST_FOLLOW_UP_CONFIRM_CLICK = 88; // 最新跟进时间-确定 click
    public static final int EVENT_REGISTER_CONFIRM_CLICK = 99; // 注册时间-确定 click

    public int eventType = -1; //事件类型
    public int position = -1; // referrer item click position
    public int page = 0;//0 表示趋势图，1 表示列表
    public String start, end;
    public StudentFilter filter;

    public FollowUpFilterEvent(int eventType, int position, int status, StudentFilter filter) {
        this.eventType = eventType;
        this.position = position;
        this.filter = filter;
    }

    public FollowUpFilterEvent() {
    }

    public FollowUpFilterEvent(int eventType, int position) {
        this.eventType = eventType;
        this.position = position;
    }

    public FollowUpFilterEvent(int eventType) {
        this.eventType = eventType;
    }

    private FollowUpFilterEvent(Builder builder) {
        eventType = builder.eventType;
        position = builder.position;
        filter = builder.filter;
    }

    public static Builder newBuilder(FollowUpFilterEvent copy) {
        Builder builder = new Builder();
        builder.filter = copy.filter;
        builder.position = copy.position;
        builder.eventType = copy.eventType;
        return builder;
    }

    public static IEventType builder() {
        return new Builder();
    }

    public interface IBuild {
        FollowUpFilterEvent build();
    }

    public interface IFilter {
        IBuild withFilter(StudentFilter val);
    }

    public interface IPosition {
        IFilter withPosition(int val);
    }

    public interface IEventType {
        IPosition withEventType(int val);
    }

    /**
     * {@code FollowUpFilterEvent} builder static inner class.
     */
    public static final class Builder implements IFilter, IPosition, IEventType, IBuild {
        private StudentFilter filter;
        private int position;
        private int eventType;

        public Builder() {
        }

        /**
         * Sets the {@code filter} and returns a reference to {@code IBuild}
         *
         * @param val the {@code filter} to set
         * @return a reference to this Builder
         */
        @Override public IBuild withFilter(StudentFilter val) {
            filter = val;
            return this;
        }

        /**
         * Sets the {@code position} and returns a reference to {@code IFilter}
         *
         * @param val the {@code position} to set
         * @return a reference to this Builder
         */
        @Override public IFilter withPosition(int val) {
            position = val;
            return this;
        }

        /**
         * Sets the {@code eventType} and returns a reference to {@code IPosition}
         *
         * @param val the {@code eventType} to set
         * @return a reference to this Builder
         */
        @Override public IPosition withEventType(int val) {
            eventType = val;
            return this;
        }

        /**
         * Returns a {@code FollowUpFilterEvent} built from the parameters previously set.
         *
         * @return a {@code FollowUpFilterEvent} built with parameters of this {@code
         * FollowUpFilterEvent.Builder}
         */
        public FollowUpFilterEvent build() {
            return new FollowUpFilterEvent(this);
        }
    }
}
