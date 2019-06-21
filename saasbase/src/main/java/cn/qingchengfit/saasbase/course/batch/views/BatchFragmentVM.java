package cn.qingchengfit.saasbase.course.batch.views;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.course.batch.bean.WorkoutPlan;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.ToastUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class BatchFragmentVM extends BaseViewModel {
  ICourseModel courseModel;

  public MutableLiveData<List<WorkoutPlan.Workout>> getWorks() {
    return works;
  }

  public void setWorks(MutableLiveData<List<WorkoutPlan.Workout>> works) {
    this.works = works;
  }

  public Map<String, List<WorkoutPlan>> getWorkoutPlans() {
    return workoutPlans;
  }

  private MutableLiveData<List<WorkoutPlan.Workout>> works = new MutableLiveData<>();
  private Map<String, List<WorkoutPlan>> workoutPlans = new HashMap<>();
  public MutableLiveData<WorkoutPlan.Workout> selectWork = new MutableLiveData<>();
  public MutableLiveData<WorkoutPlan> selectWorkPlan = new MutableLiveData<>();

  @Inject public BatchFragmentVM(ICourseModel courseModel) {
    this.courseModel = courseModel;
    loadWorkout();
  }

  public void loadWorkout() {
    HashMap<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put("show_all", "1");
    courseModel.qcGetCourseWorkout(stringObjectMap)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            works.setValue(response.data.workouts);
          } else {
            ToastUtils.show(response.msg);
          }
        }, throwable -> {
        });
  }

  public void setSelectWork(WorkoutPlan.Workout work) {
    this.selectWork.setValue(work);
    loadWorkoutPlans(work.getId());
  }

  public void loadPlans() {
    loadWorkoutPlans(selectWork.getValue().getId());
  }

  private void loadWorkoutPlans(String workout_id) {
    if (workoutPlans.containsKey(workout_id)) return;
    HashMap<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put("show_all", "1");
    courseModel.qcGetCourseWorkoutPlans(workout_id, stringObjectMap)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            upDateWorkoutPlans(workout_id, response.data.plans);
          } else {
            ToastUtils.show(response.msg);
          }
        }, throwable -> {
        });
  }

  private void upDateWorkoutPlans(String workoutID, List<WorkoutPlan> plans) {
    workoutPlans.put(workoutID, plans);
  }
}
