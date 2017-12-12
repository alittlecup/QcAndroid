package cn.qingchengfit.student.viewmodel.allot;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.student.items.CoachChooseItem;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.student.common.flexble.FlexibleFactory;
import cn.qingchengfit.student.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.student.common.flexble.FlexibleViewModel;
import cn.qingchengfit.student.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.student.items.ChooseStaffItem;
import cn.qingchengfit.student.items.StaffDetailItem;
import cn.qingchengfit.student.respository.StudentRespository;
import cn.qingchengfit.student.respository.StudentRespository;
import cn.qingchengfit.student.viewmodel.SortViewModel;
import cn.qingchengfit.utils.LogUtil;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/11/23.
 */

public class AllotMultiStaffViewModel extends FlexibleViewModel<List<QcStudentBean>, ChooseStaffItem,  Map<String,?>> {

    public final ObservableField<List<AbstractFlexibleItem>> items = new ObservableField<>();
    public final ObservableBoolean isLoading = new ObservableBoolean(false);
    public final ObservableBoolean bottomShowing = new ObservableBoolean(false);
    public final ObservableBoolean hasName = new ObservableBoolean(false);
    public final ObservableInt bottomTextCount = new ObservableInt(0);
    public final ObservableBoolean selectAllChecked = new ObservableBoolean(false);

    public ActionLiveEvent getIsDialogShow() {
        return dialogShow;
    }

    private final ActionLiveEvent dialogShow = new ActionLiveEvent();

    public MutableLiveData<String> getRouteTitle() {
        return routeTitle;
    }

    private MutableLiveData<String> routeTitle = new MutableLiveData<>();

    public MutableLiveData<List<String>> getLetters() {
        return letters;
    }

    private MutableLiveData<String> ids = new MutableLiveData<>();

    private final MutableLiveData<List<String>> letters = new MutableLiveData<>();

    public MutableLiveData<Boolean> getSelectAll() {
        return selectAll;
    }

    private final MutableLiveData<Boolean> selectAll = new MediatorLiveData<>();

    public LiveData<Boolean> getIsRemoveSuccess() {
        return isRemoveSuccess;
    }

    private LiveData<Boolean> isRemoveSuccess;

    private SortViewModel sortViewModel;

    public SortViewModel getSortViewModel() {
        return sortViewModel;
    }


    public MutableLiveData<List<QcStudentBean>> getSelectedDatas() {
        return selectedDatas;
    }

    public void setSelectedDatas(MutableLiveData<List<QcStudentBean>> selectedDatas) {
        this.selectedDatas = selectedDatas;
    }

    private MutableLiveData<List<QcStudentBean>> selectedDatas = new MutableLiveData<>();


    public MutableLiveData<Integer> getRemoveSelectPos() {
        return removeSelectPos;
    }

    private MutableLiveData<Integer> removeSelectPos = new MutableLiveData<>();

    public MutableLiveData<String> getEditAfterTextChange() {
        return editAfterTextChange;
    }

    private MutableLiveData<String> editAfterTextChange=new MutableLiveData<>();



    @Inject
    LoginStatus loginStatus;
    @Inject
    GymWrapper gymWrapper;
    @Inject
    StudentRespository respository;
    public Integer type;
    public String salerId;



    public void setSalerId(String salerId) {
        this.salerId = salerId;
    }

    @Inject
    public AllotMultiStaffViewModel() {
        sortViewModel = new SortViewModel();
        selectedDatas.setValue(new ArrayList<>());
        sortViewModel.setListener(new SortViewModel.onSortFinishListener() {
            @Override
            public void onSortFinish(List<AbstractFlexibleItem> itemss) {
                items.set(itemss);
            }
        });

        isRemoveSuccess = Transformations.switchMap(ids, this::removeStudents);

    }

    public void removeStudentIds(String ids) {
        this.ids.setValue(ids);
    }

    @NonNull
    @Override
    protected LiveData<List<QcStudentBean>> getSource(@NonNull Map<String,?> map) {
        HashMap<String, Object> params = gymWrapper.getParams();
        if (!TextUtils.isEmpty(salerId)) {
            params.put("seller_id", salerId);
        }
        params.put("show_all", 1);
//        if (!TextUtils.isEmpty(filter.status)) {
//            params.put("status", filter.status);
//        }
//        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
//            params.put("start", filter.registerTimeStart);
//            params.put("end", filter.registerTimeEnd);
//        }
//        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);
//
//        if (filter.referrerBean != null)
//            params.put("recommend_user_id", filter.referrerBean.id);
//
//        if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);

        if(!map.isEmpty()){
            params.putAll(map);
        }

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

    private LiveData<Boolean> removeStudents(String ids) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("user_ids", ids);
        String path = "";
        switch (type) {
            case 0:
                path = "sellers";
                params.put("seller_id", salerId);
                break;
            case 1:
                params.put("coach_id", salerId);
                path = "coaches";
                break;
        }
        return respository.qcRemoveStaff(loginStatus.staff_id(), path, params);
    }

    @Override
    protected boolean isSourceValid(@Nullable List<QcStudentBean> qcStudentBeans) {
        return qcStudentBeans != null;
    }

    @Override
    protected List<ChooseStaffItem> map(@NonNull List<QcStudentBean> qcStudentBeans) {
        return FlexibleItemProvider.with(new ChooseStaffItemFactory(type)).from(qcStudentBeans);
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        identifier.setValue(identifier.getValue());
    }

    /**
     * 移除会员按钮点击
     */
    public void onRemoveBtnClick() {
        dialogShow.call();
    }

    /**
     * 分配会员按钮点击
     */
    public void onAllotBtnClick(String text) {
        routeTitle.setValue(text);
    }

    /**
     * 底部展示选中的按钮点击事件
     */
    public void onBottomShowSelected() {
        routeTitle.setValue("showSelected");
    }
    /**
     * 输入框输入监听
     */
    public void onEditTextChange(String text){
        editAfterTextChange.setValue(text);
    }
    /**
     * 全选按钮点击事件
     */
    public void onAllSelectClick(boolean isChecked) {
        selectAll.setValue(isChecked);
    }

    static class ChooseStaffItemFactory implements FlexibleItemProvider.Factory<QcStudentBean, ChooseStaffItem> {
        private Integer type;

        public ChooseStaffItemFactory(Integer type) {
            this.type = type;
        }

        @NonNull
        @Override
        public ChooseStaffItem create(QcStudentBean qcStudentBean) {
            return FlexibleFactory.create(ChooseStaffItem.class, qcStudentBean, type);
        }
    }
}
