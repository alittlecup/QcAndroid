package cn.qingchengfit.student.viewmodel.attendance;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.network.body.AttendanceCharDataBean;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.common.mvvm.BaseViewModel;
import cn.qingchengfit.student.respository.StudentRespository;
import cn.qingchengfit.utils.DateUtils;

/**
 * Created by huangbaole on 2017/11/15.
 */

public class AttendanceStudentViewModel extends BaseViewModel implements LifecycleObserver {

    public ObservableField<List<FollowUpDataStatistic.DateCountsBean>> datas = new ObservableField<>(new ArrayList<>());

    public ObservableInt offDay = new ObservableInt(6);

    public final ObservableBoolean qcFBChecked = new ObservableBoolean(false);






    private final MutableLiveData<String> toUri = new MutableLiveData<>();


    private final MutableLiveData<Integer> filterIndex = new MutableLiveData<>();



    @Inject
    IStudentModel studentModel;
    @Inject
    GymWrapper gymWrapper;

    @Inject
    StudentRespository respository;

    @Inject
    LoginStatus loginStatus;




    private final MutableLiveData<Integer> offSetDay = new MutableLiveData<>();
    private LiveData<AttendanceCharDataBean> response;

    @Inject
    public AttendanceStudentViewModel() {
        response = Transformations.switchMap(offSetDay, input -> loadData(input));
    }
    private LiveData<AttendanceCharDataBean> loadData(int offSetDay) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("start", DateUtils.minusDay(new Date(), offSetDay));
        params.put("end", DateUtils.getStringToday());
        return respository.qcGetAttendanceChart(loginStatus.staff_id(), params);
    }

    public MutableLiveData<String> getToUri() {
        return toUri;
    }

    public MutableLiveData<Integer> getOffSetDay() {
        return offSetDay;
    }


    public MutableLiveData<Integer> getFilterIndex() {
        return filterIndex;
    }


    public void toStudentAbsentce() {
        toUri.setValue("student://student/attendance/absent");
    }

    public void toStudentRank() {
        toUri.setValue("student://student/attendance/rank");
    }

    public void toStudentNosign() {
        toUri.setValue("student://student/attendance/nosign");
    }




    public LiveData<AttendanceCharDataBean> getResponse() {
        return response;
    }

    public void onFilterButtonClick(int index) {
        if (!qcFBChecked.get()) {
            qcFBChecked.set(true);
            filterIndex.setValue(index);
        } else {
            qcFBChecked.set(false);
        }
    }

}
