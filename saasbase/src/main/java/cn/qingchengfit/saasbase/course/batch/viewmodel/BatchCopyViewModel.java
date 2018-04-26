package cn.qingchengfit.saasbase.course.batch.viewmodel;

import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.util.ArraySet;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.common.mvvm.BaseViewModel;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCopyCoach;
import cn.qingchengfit.saasbase.course.batch.bean.CopySchedule;
import cn.qingchengfit.saasbase.course.batch.network.body.BatchCopyBody;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.db.utils.CommonInputViewAdapter;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static cn.qingchengfit.saasbase.utils.RouterUtils.routeTo;

/**
 * Created by fb on 2018/4/8.
 */

public class BatchCopyViewModel extends BaseViewModel {

  @Inject ICourseModel courseApi;
  private boolean isPrivate;
  private BatchCopyCoach mCoach;
  private CourseType mCourseType;


  @Inject public BatchCopyViewModel() {

    Observable.OnPropertyChangedCallback timeAutoCallback =
        new Observable.OnPropertyChangedCallback() {
          @Override public void onPropertyChanged(Observable sender, int propertyId) {
            if (!TextUtils.isEmpty(startCopyTime.get())
                && !TextUtils.isEmpty(startTime.get())
                && !TextUtils.isEmpty(endTime.get())) {
              endCopyTime.set(DateUtils.addDay(startCopyTime.get(),
                  DateUtils.interval(startTime.get(), endTime.get())));
            }
          }
        };

    startCopyTime.addOnPropertyChangedCallback(timeAutoCallback);

    Observable.OnPropertyChangedCallback callback = new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {

        if (!TextUtils.isEmpty(startTime.get())
            && !TextUtils.isEmpty(endTime.get())) {
          if (DateUtils.interval(startTime.get(), endTime.get()) > 31) {
            endTime.set("");
            ToastUtils.show("所选日期不能超过31天");
            return;
          }
          if(!TextUtils.isEmpty(startCopyTime.get())) {
            endCopyTime.set(DateUtils.addDay(startCopyTime.get(),
                DateUtils.interval(startTime.get(), endTime.get())));
          }
        }
        if (!TextUtils.isEmpty(startTime.get()) && !TextUtils.isEmpty(endTime.get())) {
          getData();
        }
      }
    };

    startTime.addOnPropertyChangedCallback(callback);
    endTime.addOnPropertyChangedCallback(callback);
  }

  public ObservableField<String> startTime = new ObservableField<>();
  public ObservableField<String> endTime = new ObservableField<>();
  public ObservableField<BatchCopyCoach> coach = new ObservableField<>();
  public ObservableField<CourseType> courseValue = new ObservableField<>();
  public ObservableField<String> startCopyTime = new ObservableField<>();
  public ObservableField<String> endCopyTime = new ObservableField<>();
  private TimeDialogWindow pwTime;
  public ObservableField<String> description = new ObservableField<>();
  public ObservableField<Boolean> isCopySuccess = new ObservableField<>();
  private ArrayList<BatchCopyCoach> coachListValue = new ArrayList<>();
  private ArrayList<CourseType> courseListValue = new ArrayList<>();
  @Inject QcRestRepository restRepository;
  private boolean is_course_all = true;
  private boolean is_coach_all = true;
  private HashMap<String, List<String>> coachCourseMap = new HashMap<>();
  public ObservableField<Boolean> isLoading = new ObservableField();

  public void setPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }

  public void setCourseCategory(CourseType course) {
    courseValue.set(course);
    mCourseType = course;
  }

  public void setCoachValue(BatchCopyCoach staff) {
    coach.set(staff);
    mCoach = staff;
  }

  public void setIs_course_all(boolean is_course_all) {
    this.is_course_all = is_course_all;
  }

  public void setIs_coach_all(boolean is_coach_all) {
    this.is_coach_all = is_coach_all;
  }

  public void onSureClick(View view) {
    if (startTime.get() == null || endTime.get() == null || startCopyTime.get() == null) {
      ToastUtils.show("请填写正确时间");
      return;
    }
    if (DateUtils.interval(startTime.get(), endTime.get()) > 31) {
      ToastUtils.show("所选日期不能超过31天");
      return;
    }
    if (DateUtils.interval(startTime.get(), endTime.get()) < 1) {
      ToastUtils.show("结束日期不能早于开始日期");
      return;
    }
    if(courseValue.get() == null || coach.get() == null){
      ToastUtils.show("请完善信息");
      return;
    }
    if (coachCourseMap.get(coach.get().id) != null && !is_coach_all) {
      if (!is_course_all) {
        if (!coachCourseMap.get(coach.get().id).contains(courseValue.get().id)) {
          DialogUtils.showAlert(view.getContext(), "没有可复制的有效排期", view.getContext()
              .getString(R.string.text_copy_batch_error, startTime.get(), endTime.get(), coach.get().username, courseValue.get().name));
          return;
        }
      }else{
        for (int i = 1; i < courseListValue.size(); i++) {
          if (!coachCourseMap.get(coach.get().id).contains(courseListValue.get(i).id)) {
            DialogUtils.showAlert(view.getContext(), "没有可复制的有效排期", view.getContext()
                .getString(R.string.text_copy_batch_error, startTime.get(), endTime.get(),
                    coach.get().username, courseListValue.get(i).name));
            return;
          }
        }
      }
    }
    courseApi.qcCheckBatchConflict(createBatchCheckBody())
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .takeWhile(qcDataResponse -> {
          Boolean b = ResponseConstant.checkSuccess(qcDataResponse);
          if (b) {
            return true;
          } else {
            ToastUtils.show(qcDataResponse.getMsg());
            return false;
          }
        })
        .subscribe(aBoolean -> courseApi.qcSureCopyBatch(createBatchCheckBody())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribe(qcDataResponse -> {
              if (ResponseConstant.checkSuccess(qcDataResponse)) {
                isCopySuccess.set(true);
              } else {
                ToastUtils.show(qcDataResponse.getMsg());
              }
            }, new NetWorkThrowable()), new NetWorkThrowable());
  }

  public void getData() {
    HashMap<String, Object> params = new HashMap<>();
    params.put("start", startTime.get());
    params.put("end", endTime.get());
    params.put("is_private", isPrivate ? 1 : 0);
    if (mCourseType != null){
      params.put("course_id", mCourseType.id);
    }

    if (mCoach != null){
      params.put("teacher_id", mCoach.id);
    }

    isLoading.set(Boolean.TRUE);
    courseApi.qcBatchCopySchedule(params)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(response -> {
          isLoading.set(Boolean.FALSE);
          if (ResponseConstant.checkSuccess(response)) {
            Set<CourseType> tempCourseList = new ArraySet<>();
            Set<BatchCopyCoach> tempCoachList = new ArraySet<>();

            for (CopySchedule schedule : response.data.getSchedules()) {
              tempCoachList.add(schedule.teacher);
              tempCourseList.add(schedule.course);
              if (coachCourseMap.containsKey(schedule.teacher.id)){
                coachCourseMap.get(schedule.teacher.id).add(schedule.course.id);
              }else{
                List<String> list = new ArrayList<>();
                list.add(schedule.course.id);
                coachCourseMap.put(schedule.teacher.id, list);
              }
            }
            if (coachListValue.size() > 0) {
              coachListValue.clear();
            }
            ;
            if (courseListValue.size() > 0) {
              courseListValue.clear();
            }

            if (tempCoachList.size() == 0 && mCoach == null){
              coach.set(null);
            }

            if (tempCourseList.size() == 0 && mCourseType == null){
              courseValue.set(null);
            }

            if (tempCoachList.size() != 0 && tempCourseList.size() != 0) {
              coachListValue.add(0, createAllShip());
              courseListValue.add(0, createAllCourse());
              List<String> list = new ArrayList<>();
              list.add("0");
              coachCourseMap.put("0", list);
            }

            courseListValue.addAll(tempCourseList);
            coachListValue.addAll(tempCoachList);
          }

        }, new NetWorkThrowable());
  }

  private BatchCopyBody createBatchCheckBody() {
    return new BatchCopyBody.Builder().is_private(isPrivate ? 1 : 0)
        .from_start(startTime.get())
        .from_end(endTime.get())
        .course_all(is_course_all)
        .teacher_all(is_coach_all)
        .to_start(startCopyTime.get())
        .course_id(!courseValue.get().id.equals("0") ? courseValue.get().id : null)
        .teacher_id(!coach.get().id.equals("0") ? coach.get().id : null)
        .build();
  }

  public void onCoach(View view) {
    if (TextUtils.isEmpty(startTime.get())){
      ToastUtils.show("请先选择开始日期");
      return;
    }
    if (TextUtils.isEmpty(endTime.get())){
      ToastUtils.show("请先选择结束日期");
      return;
    }
    Bundle b = new Bundle();
    b.putParcelableArrayList("coachList", coachListValue);
    routeTo(view.getContext(), "course", "/batch/choose/trainer", b, new Intent());
  }

  public void onCourse(View view) {
    if (TextUtils.isEmpty(startTime.get())){
      ToastUtils.show("请先选择开始日期");
      return;
    }
    if (TextUtils.isEmpty(endTime.get())){
      ToastUtils.show("请先选择结束日期");
      return;
    }
    Bundle b = new Bundle();
    b.putParcelableArrayList("courseList", courseListValue);
    routeTo(view.getContext(), "course", "/batch/choose/course", b, new Intent());
  }

  public void onTimeStart(View view, String date) {

    if (TextUtils.isEmpty(startTime.get()) && view.getId() != R.id.input_start){
      ToastUtils.show("请先选择开始日期");
      return;
    }
    if (TextUtils.isEmpty(endTime.get()) && view.getId() != R.id.input_start && view.getId() != R.id.input_end){
      ToastUtils.show("请先选择结束日期");
      return;
    }
    if (pwTime == null) {
      pwTime = new TimeDialogWindow(view.getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }
    pwTime.setOnTimeSelectListener(date1 -> {
      if (view instanceof CommonInputView) {
        CommonInputViewAdapter.setContent(((CommonInputView) view), DateUtils.Date2YYYYMMDD(date1));
      }
    });
    pwTime.showAtLocation(view, Gravity.BOTTOM, 0, 0,
        TextUtils.isEmpty(date) ? null : DateUtils.formatDateFromYYYYMMDD(date));
  }

  private CourseType createAllCourse() {
    CourseType course = new CourseType();
    course.setId("0");
    course.setName("全部课程");
    course.setPhoto("http://zoneke-img.b0.upaiyun.com/3fedf1fabde72ee00b8dd08e5547aa57.png");
    return course;
  }

  private BatchCopyCoach createAllShip() {
    BatchCopyCoach ship = new BatchCopyCoach();
    ship.setId("0");
    ship.setAvatar("http://zoneke-img.b0.upaiyun.com/3fedf1fabde72ee00b8dd08e5547aa57.png");
    ship.setName("全部教练");
    return ship;
  }
}
