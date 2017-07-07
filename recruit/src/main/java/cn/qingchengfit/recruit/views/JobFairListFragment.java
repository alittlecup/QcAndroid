package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.JobFairHorizonItem;
import cn.qingchengfit.recruit.item.JobFairVertialItem;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.presenter.JobFairPresenter;
import cn.qingchengfit.views.fragments.BaseListFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/7/5.
 */

public class JobFairListFragment extends BaseListFragment
    implements JobFairPresenter.MVPView, SwipeRefreshLayout.OnRefreshListener,
    FlexibleAdapter.OnItemClickListener {
  @Inject public JobFairPresenter presenter;
  @Inject public RecruitRouter router;
  private int type = 0;

  public static JobFairListFragment newDoingJobFairs() {
    Bundle args = new Bundle();
    JobFairListFragment fragment = new JobFairListFragment();
    args.putInt("type", 0);
    fragment.setArguments(args);
    return fragment;
  }

  public static JobFairListFragment newOutDateJobFairs() {
    Bundle args = new Bundle();
    JobFairListFragment fragment = new JobFairListFragment();
    args.putInt("type", 1);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) type = getArguments().getInt("type", 0);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    delegatePresenter(presenter, this);
    initListener(this);
    onRefresh();
    return view;
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.no_find_job;
  }

  @Override public String getNoDataStr() {
    return "无相关内容";
  }

  @Override public void onJobFairList(List<JobFair> jobfairs) {
    if (jobfairs != null) {
      List<AbstractFlexibleItem> items = new ArrayList<>();
      for (JobFair jobfair : jobfairs) {
        items.add(new JobFairVertialItem(jobfair));
      }
      setDatas(items, 1);
    }
  }

  @Override public void onRefresh() {
    presenter.queryJobFairs(type);
  }

  @Override public boolean onItemClick(int i) {
    if (getItem(i) instanceof JobFairHorizonItem) {
      router.toJobFairDetail(((JobFairHorizonItem) getItem(i)).getJobFair());
    }
    return false;
  }
}
