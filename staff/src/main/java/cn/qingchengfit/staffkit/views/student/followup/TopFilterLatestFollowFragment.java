package cn.qingchengfit.staffkit.views.student.followup;

import cn.qingchengfit.staffkit.rxbus.RxBus;

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
 * //
 * //Created by yangming on 16/12/7.
 */
public class TopFilterLatestFollowFragment extends TopFilterRegisterFragment {
    public TopFilterLatestFollowFragment() {
    }

    @Override public void initView() {
        super.initView();
        tvDesc.setText("搜索某时间段内最新跟进的会员");
    }

    @Override public void postEvent() {
        RxBus.getBus()
            .post(new FollowUpFilterEvent.Builder().withEventType(FollowUpFilterEvent.EVENT_LATEST_FOLLOW_UP_CONFIRM_CLICK)
                .withPosition(-1)
                .withFilter(filter)
                .build());
    }
}
