package cn.qingchengfit.saasbase.student.bean;

import android.databinding.ObservableField;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by huangbaole on 2017/11/7.
 */

public final class FollowUpFilterModel {
    public final ObservableField<String> studentStatus = new ObservableField<>("会员状态");
    public final ObservableField<String> gender = new ObservableField<>("性别");
    public final ObservableField<String> today = new ObservableField<>("今日");
    public final ObservableField<String> topLatestDay = new ObservableField<>("最近七天");
    public final ObservableField<String> salerName = new ObservableField<>("销售");
    public final ObservableField<String> topSalerName = new ObservableField<>("销售");


    public FollowUpFilterModel() {

    }

    public void clear() {
        studentStatus.set("会员状态");
        gender.set("性别");
        today.set("今日");
        topLatestDay.set("最近七天");
        salerName.set("销售");
        topSalerName.set("销售");
    }
}
