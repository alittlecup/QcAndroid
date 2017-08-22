package cn.qingchengfit.recruit.item;

import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.model.JobFair;

public class JobFairHorizonMatchParentItem extends JobFairHorizonItem {

  public JobFairHorizonMatchParentItem(JobFair jobFair) {
    super(jobFair);
  }

  @Override public int getLayoutRes() {
    return R.layout.item_jobfairs_match_parent;
  }
}