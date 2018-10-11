package cn.qingchengfit.student.view.attendance;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Color;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import cn.qingchengfit.student.Utils;
import cn.qingchengfit.student.bean.AttendanceCharDataBean;
import cn.qingchengfit.student.bean.FollowUpDataStatistic;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.Util;
import com.github.mikephil.charting.data.LineData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/11/15.
 */

public class AttendanceStudentViewModel extends BaseViewModel {

  public ObservableField<List<FollowUpDataStatistic.DateCountsBean>> datas =
      new ObservableField<>(new ArrayList<>());

  public ObservableInt offDay = new ObservableInt(6);

  public final ObservableBoolean qcFBChecked = new ObservableBoolean(false);

  private final MutableLiveData<String> toUri = new MutableLiveData<>();

  private final MutableLiveData<Integer> filterIndex = new MutableLiveData<>();

  @Inject GymWrapper gymWrapper;

  @Inject StudentRepository respository;

  @Inject LoginStatus loginStatus;

  private final MutableLiveData<Integer> offSetDay = new MutableLiveData<>();
  private LiveData<AttendanceCharDataBean> response;

  @Inject public AttendanceStudentViewModel() {
    response = Transformations.switchMap(offSetDay, input -> loadData(input));
  }

  private LiveData<AttendanceCharDataBean> loadData(int offSetDay) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("start", DateUtils.minusDay(new Date(), offSetDay));
    params.put("end", DateUtils.getStringToday());
    return Transformations.map(respository.qcGetAttendanceChart(loginStatus.staff_id(), params),
        this::dealResource);
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
    toUri.setValue("/attendance/absent");
  }

  public void toStudentRank() {
    toUri.setValue("/attendance/rank");
  }

  public void toStudentNosign() {
    toUri.setValue("/attendance/nosign");
  }

  public LineData getData() {
    return Utils.transformBean2Data(datas.get(), offDay.get(), Color.parseColor("#FF8CB4B9"),
        Color.parseColor("#648CB4B9"));
  }
  //<!--
  //app:data='@{StudentBusinessUtils.transformBean2Data(viewModel.datas, viewModel.offSetDay
  //    , Color.parseColor("#FF8CB4B9"), Color.parseColor("#648CB4B9")}'
  //  -->

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
