package cn.qingchengfit.student.viewmodel.attendance.absent;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.student.bean.Absentce;
import cn.qingchengfit.saasbase.student.items.AttendanceStudentItem;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.student.common.flexble.FlexibleFactory;
import cn.qingchengfit.student.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.student.common.flexble.FlexibleViewModel;
import cn.qingchengfit.student.respository.StudentRespository;

/**
 * Created by huangbaole on 2017/11/16.
 */

public class AttendanceAbsentViewModel extends FlexibleViewModel<List<Absentce>, AttendanceStudentItem, Pair<String, String>> {

    public final ObservableBoolean filterChecked = new ObservableBoolean(false);
    public final ObservableField<String> filterText = new ObservableField<>("缺勤7-30天");

    public final ObservableField<List<AttendanceStudentItem>> items = new ObservableField<>();

    public final ObservableInt count = new ObservableInt(0);


    private final MutableLiveData<Integer> filterIndex = new MutableLiveData<>();


    public MutableLiveData<Integer> getFilterIndex() {
        return filterIndex;
    }

    @Inject
    GymWrapper gymWrapper;

    @Inject
    StudentRespository respository;

    @Inject
    LoginStatus loginStatus;


    @Inject
    public AttendanceAbsentViewModel() {
        super();
    }

    @NonNull
    @Override
    protected LiveData<List<Absentce>> getSource(@NonNull Pair<String, String> pair) {

        HashMap<String, Object> params = gymWrapper.getParams();
        params = handleData(params, pair.first, pair.second);
        params.put("show_all", 1);
        return Transformations.map(respository.qcGetUsersAbsences(loginStatus.staff_id(), params), input -> {
            // REFACTOR: 2017/11/17 在这里修改UI是有风险的
            count.set(input.total_count);
            return input.attendances;
        });

    }

    //根据条件处理参数
    private HashMap<String, Object> handleData(HashMap<String, Object> hashMap, String startStr, String endStr) {
        if (StringUtils.isEmpty(startStr) || StringUtils.isEmpty(endStr)) {
            return hashMap;
        }
        int start = Integer.valueOf(startStr);
        int end = Integer.valueOf(endStr);
        if (start == 7 && end == 30) {
            hashMap.put("absence__gte", "7");
            hashMap.put("absence__lte", "30");
            return hashMap;
        }
        if (start == 60 && end == -1) {
            hashMap.put("absence__gt", "60");
            return hashMap;
        }
        hashMap.put("absence__gte", start);
        hashMap.put("absence__lte", end);
        return hashMap;
    }

    public void onFilterClick(int index) {
        if (!filterChecked.get()) {
            filterChecked.set(true);
            filterIndex.setValue(index);
        } else {
            filterChecked.set(false);
        }
    }


    @Override
    protected boolean isSourceValid(@Nullable List<Absentce> absentces) {
        return absentces != null && !absentces.isEmpty();
    }

    @Override
    protected List<AttendanceStudentItem> map(@NonNull List<Absentce> absentces) {
        return FlexibleItemProvider.with(new AttendanceStudentItemFactory()).from(absentces);
    }

    static class AttendanceStudentItemFactory
            implements FlexibleItemProvider.Factory<Absentce, AttendanceStudentItem> {
        @NonNull
        @Override
        public AttendanceStudentItem create(Absentce absentce) {
            return FlexibleFactory.create(AttendanceStudentItem.class, absentce);
        }
    }

}
