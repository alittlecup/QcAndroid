package cn.qingchengfit.student.view.attendance.rank;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.Attendance;
import cn.qingchengfit.student.bean.AttendanceListWrap;
import cn.qingchengfit.student.item.AttendanceRankItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.utils.DateUtils;

/**
 * Created by huangbaole on 2017/11/16.
 */

public class AttendanceRankViewModel extends
    FlexibleViewModel<List<Attendance>, AttendanceRankItem, AttendanceRankLoadData> {


    public ObservableBoolean qcFilterCheck = new ObservableBoolean(false);
    public ObservableField<List<AttendanceRankItem>> items = new ObservableField<>();
    public ObservableField<String> sortText = new ObservableField<>("出勤天数");
    public ObservableField<String> filterText = new ObservableField<>("最近30天");
    public ObservableField<String> textDetail = new ObservableField<>("");
    public ObservableBoolean filterVisible = new ObservableBoolean(false);
    public ObservableBoolean sortRevert = new ObservableBoolean(false);
    public ObservableInt pos = new ObservableInt(0);



    public AttendanceRankLoadData getLoadData() {
        return loadData;
    }

    private AttendanceRankLoadData loadData;

    private MutableLiveData<Integer> filterIndex = new MutableLiveData<>();

    public MutableLiveData<Integer> getFilterIndex() {
        return filterIndex;
    }

    @Inject StudentRepository respository;
    @Inject
    GymWrapper gymWrapper;
    @Inject
    LoginStatus loginStatus;


    @Inject
    public AttendanceRankViewModel() {
        loadData = new AttendanceRankLoadData(new Pair<>(
                DateUtils.minusDay(new Date(), 29)
                , DateUtils.getStringToday())
                , true
                , "days"
        );
    }

    @NonNull
    @Override
    public LiveData<List<Attendance>> getSource(@NonNull AttendanceRankLoadData data) {
        HashMap<String, Object> params = gymWrapper.getParams();
        String orderStr = (!data.isRevert() ? "" : "-").concat(data.getSortType());
        params.put("order_by", orderStr);
        params.put("start", data.getDay().first);
        params.put("end", data.getDay().second);
        params.put("show_all", 1);

        return Transformations.map(respository.qcGetUsersAttendances(loginStatus.staff_id(),params),input -> {
            // REFACTOR: 2017/11/17 在这里修改UI是有风险的
            AttendanceListWrap attendanceListWrap = dealResource(input);
            if(attendanceListWrap==null)return null;
            textDetail.set(filterText.get() + "出勤会员共" + attendanceListWrap.total_count + "人");
            return attendanceListWrap.attendances;
        });
    }

    private boolean isSortChecked = false;

    public void onTopFilterClick(int index) {
        if (index == 0) {
            if (!qcFilterCheck.get()) {
                filterIndex.setValue(index);
                filterVisible.set(true);
                qcFilterCheck.set(true);
            } else {
                filterVisible.set(false);
                qcFilterCheck.set(false);
            }
        } else if (index == 1) {
            qcFilterCheck.set(false);
            if (!isSortChecked) {
                filterIndex.setValue(index);
                filterVisible.set(true);
            } else {
                filterVisible.set(false);
            }
            isSortChecked = !isSortChecked;
        }
    }

    @Override
    protected boolean isSourceValid(@Nullable List<Attendance> attendances) {
        return attendances != null && !attendances.isEmpty();
    }

    @Override
    public void loadSource(@NonNull AttendanceRankLoadData attendanceRankLoadData) {
        this.identifier.setValue(attendanceRankLoadData);
    }

    @Override
    protected List<AttendanceRankItem> map(@NonNull List<Attendance> attendances) {
        return FlexibleItemProvider.with(new AttendanceAttendanceRankItemFactory()).from(attendances);
    }

    static class AttendanceAttendanceRankItemFactory
            implements FlexibleItemProvider.Factory<Attendance, AttendanceRankItem> {
        @NonNull
        @Override
        public AttendanceRankItem create(Attendance attendance) {
            return FlexibleFactory.create(AttendanceRankItem.class, attendance);
        }
    }
}
