package cn.qingchengfit.saasbase.mvvm_student.viewmodel.allot;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import cn.qingchengfit.saasbase.mvvm_student.items.StaffDetailItem;
import cn.qingchengfit.saasbase.mvvm_student.respository.StudentRepository;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saasbase.common.mvvm.SortViewModel;

/**
 * Created by huangbaole on 2017/11/21.
 */

public class AllotStaffDetailViewModel extends FlexibleViewModel<List<QcStudentBean>, StaffDetailItem, Map<String, ?>> {
    public final ObservableField<List<StudentItem>> items = new ObservableField<>();
    public final ObservableBoolean isLoading = new ObservableBoolean(false);
    private MutableLiveData<List<String>> letters = new MutableLiveData<>();

    public MutableLiveData<List<String>> getLetters() {
        return letters;
    }

    @Inject
    GymWrapper gymWrapper;
    @Inject
    LoginStatus loginStatus;
    @Inject StudentRepository respository;

    public void setSalerId(String salerId) {
        this.salerId = salerId;
    }

    public SortViewModel getSortViewModel() {
        return sortViewModel;
    }

    private SortViewModel sortViewModel;

    private String salerId;


    public Integer type;

    @Inject
    public AllotStaffDetailViewModel() {
        sortViewModel = new SortViewModel();
        sortViewModel.setListener(itemss -> items.set(itemss));
    }


    @NonNull
    @Override
    protected LiveData<List<QcStudentBean>> getSource(@NonNull Map<String, ?> map) {
        HashMap<String, Object> params = gymWrapper.getParams();

        params.put("show_all", 1);
        if (!map.isEmpty()) {
            params.putAll(map);
            if (params.containsKey("status_ids")) {
                params.put("status", params.get("status_ids"));
                params.remove("status_ids");
            }
        }
        String path = "";
        switch (type) {
            case 0:
                path = "sellers";
                if (!TextUtils.isEmpty(salerId)) {
                    params.put("seller_id", salerId);
                }
                break;
            case 1:
                path = "coaches";
                if (!TextUtils.isEmpty(salerId)) {
                    params.put("coach_id", salerId);
                }
                break;
        }
        isLoading.set(true);
        return Transformations.map(respository.qcGetAllotStaffMembers(loginStatus.staff_id(), path, params), intput -> intput.users);
    }

    @Override
    public void loadSource(@NonNull Map<String, ?> map) {
        this.identifier.setValue(map);
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
        identifier.setValue(identifier.getValue());
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
