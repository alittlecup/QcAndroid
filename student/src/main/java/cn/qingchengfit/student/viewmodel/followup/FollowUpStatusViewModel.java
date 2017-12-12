package cn.qingchengfit.student.viewmodel.followup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StudentReferrerBean;
import cn.qingchengfit.saasbase.student.items.FollowUpItem;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.network.body.QcStudentBeanWithFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrappeForFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentSourceBean;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.student.common.flexble.FlexibleFactory;
import cn.qingchengfit.student.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.student.common.flexble.FlexibleViewModel;
import cn.qingchengfit.student.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.student.respository.StudentRespository;
import cn.qingchengfit.utils.DateUtils;

/**
 * Created by huangbaole on 2017/11/20.
 */

public class FollowUpStatusViewModel extends FlexibleViewModel<StudentListWrappeForFollow, FollowUpItem, StudentFilter> {

    public final ObservableField<String> studentStatus = new ObservableField<>("会员状态");
    public final ObservableField<String> gender = new ObservableField<>("性别");
    public final ObservableField<String> today = new ObservableField<>("今日");
    public final ObservableField<String> topLatestDay = new ObservableField<>("最近七天");
    public final ObservableField<String> salerName = new ObservableField<>("销售");
    public final ObservableField<String> topSalerName = new ObservableField<>("销售");
    public final ObservableField<List<FollowUpItem>> items = new ObservableField<>();
    public final ObservableBoolean appBarLayoutExpanded = new ObservableBoolean(true);
    public final ObservableBoolean filterVisible = new ObservableBoolean(false);

    public ObservableField<FollowUpDataStatistic.NewCreateUsersBean> datas = new ObservableField<>(new FollowUpDataStatistic.NewCreateUsersBean());


    private final MutableLiveData<Integer> filterIndex = new MutableLiveData<>();

    public ActionLiveEvent getFilterClick() {
        return filterClick;
    }

    private final ActionLiveEvent filterClick = new ActionLiveEvent();

    private final MutableLiveData<String> status = new MutableLiveData<>();
    private final MutableLiveData<String> genderM = new MutableLiveData<>();
    private final MutableLiveData<Pair<String, String>> days = new MutableLiveData<>();
    private final MutableLiveData<Staff> staff = new MutableLiveData<>();

    private final MutableLiveData<Pair<String, String>> topDays = new MutableLiveData<>();
    private final MutableLiveData<Staff> topStaff = new MutableLiveData<>();

    public MutableLiveData<Pair<String, String>> getTopDays() {
        return topDays;
    }

    public MutableLiveData<Staff> getTopStaff() {
        return topStaff;
    }

    private final MediatorLiveData<StudentFilter> topFilter = new MediatorLiveData<>();


    public LiveData<FollowUpDataStatistic.NewCreateUsersBean> getTopDatas() {
        return topDatas;
    }

    private final LiveData<FollowUpDataStatistic.NewCreateUsersBean> topDatas;

    public MutableLiveData<String> getStatus() {
        return status;
    }

    public MutableLiveData<String> getGenderM() {
        return genderM;
    }

    public MutableLiveData<Pair<String, String>> getDays() {
        return days;
    }

    public MutableLiveData<Staff> getStaff() {
        return staff;
    }

    public MutableLiveData<Integer> getFilterIndex() {
        return filterIndex;
    }

    @Inject
    StudentRespository respository;
    @Inject
    GymWrapper gymWrapper;
    @Inject
    LoginStatus loginStatus;

    public Integer dataType = -1;


    private StudentFilter filter = new StudentFilter();

    private StudentFilter topStudentFilter = new StudentFilter();

    @Inject
    public FollowUpStatusViewModel() {
        super();
        identifier.addSource(status, integer -> {
            filter.status = integer;
            identifier.setValue(filter);
        });
        identifier.addSource(genderM, gender -> {
            filter.gender = gender;
            identifier.setValue(filter);
        });
        identifier.addSource(days, days -> {
            filter.registerTimeStart = days.first;
            filter.registerTimeEnd = days.second;
            identifier.setValue(filter);
        });
        identifier.addSource(staff, staff -> {
            filter.sale = staff;
            identifier.setValue(filter);
        });
        topFilter.addSource(topStaff, staff -> {
            topStudentFilter.sale = staff;
            topFilter.setValue(topStudentFilter);
        });
        topFilter.addSource(topDays, days -> {
            topStudentFilter.registerTimeStart = days.first;
            topStudentFilter.registerTimeEnd = days.second;

            topFilter.setValue(topStudentFilter);
        });
        topDatas = Transformations.switchMap(topFilter, input -> {
            return Transformations.map(loadTopSource(input), followupDataStatustic -> {
                FollowUpDataStatistic.NewCreateUsersBean beans = null;
                switch (dataType) {
                    case 0:
                        beans = followupDataStatustic.new_create_users;
                        break;
                    case 1:
                        beans = followupDataStatustic.new_following_users;
                        break;
                    case 2:
                        beans = followupDataStatustic.new_member_users;
                        break;
                }
                return beans;
            });
        });

    }

    public Integer getOffSetDays() {

        if (topDays.getValue() == null) {
            return 6;
        }
        return DateUtils.interval(topDays.getValue().first, topDays.getValue().second);
    }

    public Integer getDataType() {
        return dataType;
    }

    private LiveData<FollowUpDataStatistic> loadTopSource(StudentFilter filter) {

        HashMap<String, Object> params = gymWrapper.getParams();

        if (null != filter) {
            if (!StringUtils.isEmpty(filter.registerTimeStart) && !StringUtils.isEmpty(filter.registerTimeEnd)) {
                params.put("start", filter.registerTimeStart);
                params.put("end", filter.registerTimeEnd);
            }
            if (filter.sale != null && !"-1".equals(filter.sale.id)) {// -1是全部
                params.put("seller_id", filter.sale.id);//无销售seller_id=0
            }
        } else {
            params.put("start", DateUtils.minusDay(new Date(), 29));
            params.put("end", DateUtils.getStringToday());
        }

        return respository.qcGetTrackStudentsStatistics(loginStatus.staff_id(), params);

    }

    public void onQcButtonFilterClick(boolean isChecked, int index) {
        if (isChecked) {
            appBarLayoutExpanded.set(false);
            filterIndex.setValue(index);
            filterVisible.set(true);
        } else {
            filterVisible.set(false);
        }
    }

    public void onRightFilterClick() {
        filterClick.call();
    }

    @NonNull
    @Override
    protected LiveData<StudentListWrappeForFollow> getSource(@NonNull StudentFilter filter) {
        HashMap<String, Object> params = gymWrapper.getParams();
        if (!TextUtils.isEmpty(filter.status)) {
            params.put("status", filter.status);
        }
        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);

        if (filter.referrerBean != null) params.put("recommend_user_id", filter.referrerBean.id);

        if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);

        if (filter.sale != null) params.put("seller_id", filter.sale.getId());

        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        params.put("page", 1);
        String type = "";
        switch (dataType) {
            case 0:
                type = "create";
                break;
            case 1:
                type = "follow";
                break;
            case 2:
                type = "member";
                break;
        }
        return respository.qcGetTrackStudents(loginStatus.staff_id(), type, params);
    }

    public void setFilterMap(Map<String, ?> map) {
        if (map.isEmpty()) return;
        for (String key : map.keySet()) {
            switch (key) {
                case "status_ids":
                    filter.status = (String) map.get(key);
                    break;
                case "start":
                    filter.registerTimeStart = (String) map.get(key);
                    break;
                case "gender":
                    filter.gender = (String) map.get(key);
                    break;
                case "end":
                    filter.registerTimeEnd = (String) map.get(key);
                    break;
                case "origin_id":
                    if (filter.sourceBean == null) {
                        filter.sourceBean = new StudentSourceBean();
                    }
                    filter.sourceBean.id = (String) map.get(key);
                    break;
                case "recommend_user_id":
                    if (filter.referrerBean == null) {
                        filter.referrerBean = new StudentReferrerBean();
                    }
                    filter.referrerBean.id = (String) map.get(key);
                    break;
                case "seller_id":
                    if (filter.sale == null) {
                        filter.sale = new Staff();
                    }
                    filter.sale.id = (String) map.get(key);
                    break;
            }
        }
        identifier.setValue(filter);
    }

    @Override
    protected boolean isSourceValid(@Nullable StudentListWrappeForFollow studentListWrappeForFollow) {
        return studentListWrappeForFollow != null && studentListWrappeForFollow.users != null;
    }


    @Override
    protected List<FollowUpItem> map(@NonNull StudentListWrappeForFollow studentListWrappeForFollow) {
        return FlexibleItemProvider.with(new FollowUpItemFactory(dataType)).from(studentListWrappeForFollow.users);
    }


    static class FollowUpItemFactory implements FlexibleItemProvider.Factory<QcStudentBeanWithFollow, FollowUpItem> {
        private Integer type;

        public FollowUpItemFactory(Integer type) {
            this.type = type;
        }

        @NonNull
        @Override
        public FollowUpItem create(QcStudentBeanWithFollow beanWithFollow) {
            return FlexibleFactory.create(FollowUpItem.class, beanWithFollow, type);
        }
    }
}
