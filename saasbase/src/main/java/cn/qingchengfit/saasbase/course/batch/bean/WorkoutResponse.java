package cn.qingchengfit.saasbase.course.batch.bean;

import cn.qingchengfit.network.response.QcListData;
import java.util.List;

public class WorkoutResponse extends QcListData {
  public List<WorkoutPlan.Workout> workouts;
}
