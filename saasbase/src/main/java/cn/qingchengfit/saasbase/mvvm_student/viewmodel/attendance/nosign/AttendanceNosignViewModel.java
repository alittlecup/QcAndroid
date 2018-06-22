package cn.qingchengfit.saasbase.mvvm_student.viewmodel.attendance.nosign;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import cn.qingchengfit.saasbase.mvvm_student.respository.StudentRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.student.bean.StudentWIthCount;
import cn.qingchengfit.saasbase.student.items.NotSignClassItem;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.utils.DateUtils;

/**
 * Created by huangbaole on 2017/11/17.
 */

public class AttendanceNosignViewModel extends FlexibleViewModel<List<StudentWIthCount>, NotSignClassItem, AttendanceNosignViewModel.DataHolder> {

    @Inject
    GymWrapper gymWrapper;
    @Inject
    LoginStatus loginStatus;
    @Inject StudentRepository respository;

    public ObservableField<List<NotSignClassItem>> items = new ObservableField<>();
    public ObservableField<String> topDayText = new ObservableField<>("未签课7天内");

    public ObservableInt topCount = new ObservableInt(5);

    public ObservableBoolean filterVisible = new ObservableBoolean(false);
    public ObservableField<String> textDetail=new ObservableField<>();


    public MutableLiveData<Integer> getFilterIndex() {
        return filterIndex;
    }

    private MutableLiveData<Integer> filterIndex = new MutableLiveData<>();

    private MutableLiveData<Pair<String,String>> days=new MutableLiveData<>();
    private MutableLiveData<Integer> count=new MutableLiveData<>();

    public MutableLiveData<Pair<String, String>> getDays() {
        return days;
    }

    public MutableLiveData<Integer> getCount() {
        return count;
    }

    private AttendanceNosignItemFactory factory;

    private DataHolder dataHolder;

    @Inject
    public AttendanceNosignViewModel() {
        factory = new AttendanceNosignItemFactory();
        dataHolder = new DataHolder(new Pair<>(
                DateUtils.minusDay(new Date(), 6)
                , DateUtils.getStringToday()
        ), 6);
        identifier.addSource(days,days->{
            dataHolder.setDays(days);
            identifier.setValue(dataHolder);
        });
        identifier.addSource(count,count->{
            dataHolder.setCount(count);
            identifier.setValue(dataHolder);
        });

    }

    public DataHolder getDataHolder() {
        return dataHolder;
    }

    @NonNull
    @Override
    protected LiveData<List<StudentWIthCount>> getSource(@NonNull DataHolder dataHolder) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("start", dataHolder.getDays().first);
        params.put("end", dataHolder.getDays().second);
        params.put("limit", dataHolder.getCount());
        return respository.qcGetNotSignStudent(loginStatus.staff_id(), params);
    }

    @Override
    protected boolean isSourceValid(@Nullable List<StudentWIthCount> studentWIthCountList) {
        return studentWIthCountList != null && !studentWIthCountList.isEmpty();
    }

    @Override
    protected List<NotSignClassItem> map(@NonNull List<StudentWIthCount> studentWIthCountList) {
        return FlexibleItemProvider.with(factory).from(studentWIthCountList);
    }

    @Override
    public void loadSource(@NonNull DataHolder dataHolder) {
        this.identifier.setValue(dataHolder);
    }


    public void onTopFilterClick(boolean isChecked, int index) {
        if (isChecked) {
            filterIndex.setValue(index);
            filterVisible.set(true);
        } else {
            filterVisible.set(false);
        }
    }


    static class AttendanceNosignItemFactory implements FlexibleItemProvider.Factory<StudentWIthCount, NotSignClassItem> {

        @NonNull
        @Override
        public NotSignClassItem create(StudentWIthCount studentWIthCount) {
            return FlexibleFactory.create(NotSignClassItem.class, studentWIthCount);
        }
    }

    public static class DataHolder {
        private Pair<String, String> days;
        private int count;

        public Pair<String, String> getDays() {
            return days;
        }

        public void setDays(Pair<String, String> days) {
            this.days = days;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public DataHolder(Pair<String, String> days, int count) {
            this.days = days;
            this.count = count;
        }
    }

}
