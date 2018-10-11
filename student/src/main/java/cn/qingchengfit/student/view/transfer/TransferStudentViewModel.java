package cn.qingchengfit.student.view.transfer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.StudentTransferBean;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.utils.DateUtils;

/**
 * Created by huangbaole on 2017/11/17.
 */

public class TransferStudentViewModel extends
        FlexibleViewModel<List<QcStudentBeanWithFollow>, ChooseDetailItem, TransferStudentViewModel.DataHolder> {

    public ObservableField<List<ChooseDetailItem>> items = new ObservableField<>();
    public MutableLiveData<Boolean> filterVisible = new MutableLiveData();
    public ObservableBoolean appBarLayoutExpend = new ObservableBoolean(true);
    public ObservableField<String> staffName = new ObservableField<>("全部");
    public ObservableField<String> days = new ObservableField<>("最近7天");
    public ObservableField<List<Float>> funnelTwoViewDatas = new ObservableField<>(new ArrayList<>());


    private MutableLiveData<Staff> staff = new MutableLiveData<>();
    private MutableLiveData<Integer> filterIndex = new MutableLiveData<>();
    private MutableLiveData<Pair<String, String>> mutabkleDays = new MutableLiveData<>();


    public MutableLiveData<Integer> getFilterIndex() {return filterIndex;}



    public MutableLiveData<Pair<String, String>> getMutabkleDays() {
        return mutabkleDays;
    }
    public MutableLiveData<Staff> getStaff() {
        return staff;
    }
    private DataHolder dataHolder;
    public DataHolder getDataHolder() {
        return dataHolder;
    }

    @Inject StudentRepository respository;

    @Inject
    LoginStatus loginStatus;

    @Inject
    GymWrapper gymWrapper;

    @Inject
    public TransferStudentViewModel() {
        dataHolder = new DataHolder(null,
                new Pair<>(DateUtils.minusDay(new Date(), 6), DateUtils.getStringToday()));

        identifier.addSource(mutabkleDays, days -> {
            dataHolder.setDays(days);
            identifier.setValue(dataHolder);
        });
        identifier.addSource(staff, staff -> {
            dataHolder.setStaff(staff);
            identifier.setValue(dataHolder);
        });
    }

    public void onTopFilterClick(boolean isChecked, int index) {
        if (isChecked) {
            filterIndex.setValue(index);
            filterVisible.setValue(true);
        } else {
            filterVisible.setValue(false);
        }
    }

    @NonNull
    @Override
    protected LiveData<List<QcStudentBeanWithFollow>> getSource(@NonNull DataHolder dataHolder) {
        HashMap<String, Object> params = gymWrapper.getParams();

        if (dataHolder.getStaff() != null && !"-1".equals(dataHolder.getStaff().id)) {// -1是全部
            params.put("seller_id", dataHolder.getStaff().id);//无销售seller_id=0
        }

        if (!StringUtils.isEmpty(dataHolder.getDays().first) && !StringUtils.isEmpty(dataHolder.getDays().second)) {
            params.put("start", dataHolder.getDays().first);
            params.put("end", dataHolder.getDays().second);
        }
        return Transformations.map(respository.qcGetTrackStudentsConver(loginStatus.staff_id(), params), bean -> {
          StudentTransferBean studentTransferBean = dealResource(bean);
          if(studentTransferBean==null){
            return null;
          }
          setFunnelViewData(studentTransferBean);
            return studentTransferBean.users;
        });
    }

    @Override
    public void loadSource(@NonNull DataHolder dataHolder) {
        this.identifier.setValue(dataHolder);
    }

    @Override
    protected boolean isSourceValid(@Nullable List<QcStudentBeanWithFollow> studentTransferBeans) {
        return studentTransferBeans != null && !studentTransferBeans.isEmpty();
    }

    @Override
    protected List<ChooseDetailItem> map(@NonNull List<QcStudentBeanWithFollow> studentTransferBeans) {
        return FlexibleItemProvider.with(new FollowUpItemFactory()).from(studentTransferBeans);
    }

    public void setFunnelViewData(StudentTransferBean funnelViewData) {
        if (funnelViewData != null) {
            List<Float> datas = new ArrayList<>();
            datas.add(funnelViewData.create_count);
            datas.add(funnelViewData.following_count);
            datas.add(funnelViewData.member_count);
            funnelTwoViewDatas.set(datas);
        }
    }

    static class FollowUpItemFactory implements FlexibleItemProvider.Factory<QcStudentBeanWithFollow, ChooseDetailItem> {
        @NonNull
        @Override
        public ChooseDetailItem create(QcStudentBeanWithFollow bean) {
            return FlexibleFactory.create(ChooseDetailItem.class, bean, 4);
        }
    }

    public static class DataHolder {
        private Staff staff;

        private Pair<String, String> days;

        public DataHolder(Staff staff, Pair<String, String> days) {
            this.staff = staff;
            this.days = days;
        }

        public Staff getStaff() {
            return staff;
        }

        public void setStaff(Staff staff) {
            this.staff = staff;
        }

        public Pair<String, String> getDays() {
            return days;
        }

        public void setDays(Pair<String, String> days) {
            this.days = days;
        }
    }
}
