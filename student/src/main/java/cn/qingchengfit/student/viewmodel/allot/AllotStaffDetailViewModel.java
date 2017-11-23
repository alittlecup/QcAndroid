package cn.qingchengfit.student.viewmodel.allot;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.student.common.flexble.FlexibleFactory;
import cn.qingchengfit.student.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.student.common.flexble.FlexibleViewModel;
import cn.qingchengfit.student.items.StaffDetailItem;
import cn.qingchengfit.student.respository.StudentRespository;
import cn.qingchengfit.student.viewmodel.SortViewModel;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/11/21.
 */

public class AllotStaffDetailViewModel extends FlexibleViewModel<List<QcStudentBean>, StaffDetailItem, StudentFilter> {
    public final ObservableField<List<AbstractFlexibleItem>> items = new ObservableField<>();
    public final ObservableBoolean isLoading = new ObservableBoolean(false);
    private MutableLiveData<List<String>> letters = new MutableLiveData<>();

    public MutableLiveData<List<String>> getLetters() {
        return letters;
    }

    @Inject
    GymWrapper gymWrapper;
    @Inject
    LoginStatus loginStatus;
    @Inject
    StudentRespository respository;
    StudentFilter filter;

    public void setSalerId(String salerId) {
        this.salerId = salerId;
    }

    public SortViewModel getSortViewModel() {
        return sortViewModel;
    }

    private SortViewModel sortViewModel;

    private String salerId;

    public StudentFilter getFilter() {
        return filter;
    }

    public Integer type;

    @Inject
    public AllotStaffDetailViewModel() {
        filter = new StudentFilter();
        sortViewModel = new SortViewModel();
        sortViewModel.setListener(new SortViewModel.onSortFinishListener() {
            @Override
            public void onLatestSortFinish(List<AbstractFlexibleItem> itemss) {
                items.set(itemss);
            }

            @Override
            public void onlettersSortFinish(List<AbstractFlexibleItem> itemss, List<String> letterss) {
                items.set(itemss);
                letters.setValue(letterss);
            }
        });
    }


    @NonNull
    @Override
    protected LiveData<List<QcStudentBean>> getSource(@NonNull StudentFilter filter) {
        HashMap<String, Object> params = gymWrapper.getParams();
        if (!TextUtils.isEmpty(salerId)) {
            params.put("seller_id", salerId);
        }
        params.put("show_all", 1);
        if (!TextUtils.isEmpty(filter.status)) {
            params.put("status", filter.status);
        }
        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);

        if (filter.referrerBean != null)
            params.put("recommend_user_id", filter.referrerBean.id);

        if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);

        String path = "";
        switch (type) {
            case 0:
                path = "sellers";
                break;
            case 1:
                path = "coaches";
                break;
        }
        isLoading.set(true);
        return Transformations.map(respository.qcGetAllotStaffMembers(loginStatus.staff_id(), path, params), intput -> intput.users);
    }

    @Override
    protected boolean isSourceValid(@Nullable List<QcStudentBean> qcStudentBeans) {
        return qcStudentBeans != null;
    }

    @Override
    protected List<StaffDetailItem> map(@NonNull List<QcStudentBean> qcStudentBeans) {
        return FlexibleItemProvider.with(new StaffDetailItemFactory(type)).from(qcStudentBeans);
    }

    public void refresh() {
        identifier.setValue(filter);
    }

    static class StaffDetailItemFactory implements FlexibleItemProvider.Factory<QcStudentBean, StaffDetailItem> {
        private Integer type;

        public StaffDetailItemFactory(Integer type) {
            this.type = type;
        }

        @NonNull
        @Override
        public StaffDetailItem create(QcStudentBean qcStudentBean) {
            return FlexibleFactory.create(StaffDetailItem.class, qcStudentBean, type);
        }
    }
}
