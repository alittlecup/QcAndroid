package cn.qingchengfit.student.view.attendance.absent;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

//import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.student.bean.Absentce;
import cn.qingchengfit.student.bean.AbsentceListWrap;
import cn.qingchengfit.student.item.AttendanceStudentItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;

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

    @Inject StudentRepository respository;

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
            AbsentceListWrap absentceListWrap = dealResource(input);
            if(absentceListWrap==null)return null;
            count.set(absentceListWrap.total_count);
            return absentceListWrap.attendances;
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
        return absentces != null;
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
