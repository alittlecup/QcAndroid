package cn.qingchengfit.student.viewmodel.followup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

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
import cn.qingchengfit.student.common.flexble.FlexibleFactory;
import cn.qingchengfit.student.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.student.common.flexble.FlexibleViewModel;
import cn.qingchengfit.student.respository.StudentRespository;
import cn.qingchengfit.utils.DateUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/11/20.
 */

public class FollowUpStudentViewModel extends FlexibleViewModel<List<Pair<FollowUpDataStatistic.NewCreateUsersBean, String>>, DataStatisticsItem, StudentFilter> {

    public ObservableField<List<DataStatisticsItem>> items = new ObservableField<>();


    @Inject
    GymWrapper gymWrapper;
    @Inject
    StudentRespository respository;
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
    protected LiveData<List<Pair<FollowUpDataStatistic.NewCreateUsersBean, String>>> getSource(@NonNull StudentFilter filter) {
        HashMap<String, Object> params = gymWrapper.getParams();

        if (!StringUtils.isEmpty(filter.registerTimeStart) && !StringUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        if (filter.sale != null && !"-1".equals(filter.sale.id)) {// -1是全部
            params.put("seller_id", filter.sale.id);//无销售seller_id=0
        }

        return Transformations.map(respository.qcGetTrackStudentsStatistics(loginStatus.staff_id(), params), input -> {
            List<Pair<FollowUpDataStatistic.NewCreateUsersBean, String>> beans = new ArrayList<>();
            beans.add(new Pair<>(input.new_create_users, "新增注册"));
            beans.add(new Pair<>(input.new_following_users, "新增跟进"));
            beans.add(new Pair<>(input.new_member_users, "新增会员"));
            return beans;
        });
    }

    @Override
    protected boolean isSourceValid(@Nullable List<Pair<FollowUpDataStatistic.NewCreateUsersBean, String>> dateCountsBeans) {
        return dateCountsBeans != null && !dateCountsBeans.isEmpty();
    }

    @Override
    protected List<DataStatisticsItem> map(@NonNull List<Pair<FollowUpDataStatistic.NewCreateUsersBean, String>> dateCountsBeans) {
        return FlexibleItemProvider.with(new DataStatisticsItemFactory()).from(dateCountsBeans);
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
