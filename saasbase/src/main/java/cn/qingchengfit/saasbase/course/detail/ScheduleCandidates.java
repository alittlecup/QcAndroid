package cn.qingchengfit.saasbase.course.detail;

import cn.qingchengfit.network.response.QcListData;
import java.util.List;

public class ScheduleCandidates extends QcListData {
  public List<ScheduleCandidate> candidates;
  public ScheduleDetail schedule;
}
