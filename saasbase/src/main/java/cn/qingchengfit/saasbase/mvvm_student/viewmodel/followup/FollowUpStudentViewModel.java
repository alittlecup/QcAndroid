package cn.qingchengfit.saasbase.mvvm_student.viewmodel.followup;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import cn.qingchengfit.saasbase.mvvm_student.respository.StudentRespository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.student.items.DataStatisticsItem;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.utils.DateUtils;

/**
 * Created by huangbaole on 2017/11/20.
 */

public class FollowUpStudentViewModel extends FlexibleViewModel<FollowUpDataStatistic, DataStatisticsItem, StudentFilter> {

    public ObservableField<List<DataStatisticsItem>> items = new ObservableField<>();


    @Inject
    GymWrapper gymWrapper;
    @Inject StudentRespository respository;
    @Inject
    LoginStatus loginStatus;
    private StudentFilter filter = new StudentFilter();

    public StudentFilter getFilter() {
        return filter;
    }

    @Inject
    public FollowUpStudentViewModel() {
        super();
        filter.registerTimeStart = DateUtils.minusDay(new Date(), 29);
        filter.registerTimeEnd = DateUtils.getStringToday();
    }


    @NonNull
    @Override
    protected LiveData<FollowUpDataStatistic> getSource(@NonNull StudentFilter filter) {
        HashMap<String, Object> params = gymWrapper.getParams();

        if (!StringUtils.isEmpty(filter.registerTimeStart) && !StringUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        if (filter.sale != null && !"-1".equals(filter.sale.id)) {// -1是全部
            params.put("seller_id", filter.sale.id);//无销售seller_id=0
        }


        return  respository.qcGetTrackStudentsStatistics(loginStatus.staff_id(), params);
    }

    @Override
    protected boolean isSourceValid(@Nullable FollowUpDataStatistic followUpDataStatistic) {
        return followUpDataStatistic != null;
    }

    @Override
    protected List<DataStatisticsItem> map(@NonNull FollowUpDataStatistic followUpDataStatistic) {
        List<Pair<FollowUpDataStatistic.NewCreateUsersBean, String>> beans = new ArrayList<>();
        beans.add(new Pair<>(followUpDataStatistic.new_create_users, "新增注册"));
        beans.add(new Pair<>(followUpDataStatistic.new_following_users, "新增跟进"));
        beans.add(new Pair<>(followUpDataStatistic.new_member_users, "新增会员"));
        return FlexibleItemProvider.with(new DataStatisticsItemFactory()).from(beans);
    }


    static class DataStatisticsItemFactory
            implements FlexibleItemProvider.Factory<Pair<FollowUpDataStatistic.NewCreateUsersBean, String>, DataStatisticsItem> {


        @NonNull
        @Override
        public DataStatisticsItem create(Pair<FollowUpDataStatistic.NewCreateUsersBean, String> pair) {
            return FlexibleFactory.create(DataStatisticsItem.class, pair.second, pair.first);
        }
    }
}
