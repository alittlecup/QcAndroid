package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.RecruitPositionItem;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.presenter.JobListPresenter;
import cn.qingchengfit.views.fragments.BaseListFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Created by Paper on 2017/7/8.
 */

public class JobsListFragment extends BaseListFragment
    implements FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener,
    SwipeRefreshLayout.OnRefreshListener, JobListPresenter.MVPView {

  protected HashMap<String, Object> params = new HashMap<>();
  protected boolean hasItem;
  @Inject JobListPresenter presenter;
  @Inject RecruitRouter router;

  public static JobsListFragment newAllJobsList() {
    Bundle args = new Bundle();
    JobsListFragment fragment = new JobsListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    delegatePresenter(presenter, this);
    View view = super.onCreateView(inflater, container, savedInstanceState);
    initListener(this);
    return view;
  }

  public void query(HashMap<String, Object> p, String keyword) {
    this.params.putAll(p);
    this.params.put("q", keyword);
    onRefresh();
  }

  @Override protected void clearItems() {
    commonFlexAdapter.removeItemsOfType(R.layout.item_recruit_position);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    onRefresh();
  }

  @Override public void noMoreLoad(int i) {
    stopLoadMore();
  }

  @Override public void onLoadMore(int i, int i1) {
    presenter.queryList(false, params);
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = commonFlexAdapter.getItem(i);
    if (item != null && item instanceof RecruitPositionItem) {
      Job job = ((RecruitPositionItem) item).getJob();
      router.goJobDetail(job);
    }
    return true;
  }

  @Override public void onRefresh() {
    initLoadMore();
    presenter.queryList(true, params);
  }

  @Override public void initLoadMore() {
    if (commonFlexAdapter != null) commonFlexAdapter.setEndlessScrollListener(this, progressItem);
  }

  @Override public int getNoDataIconRes() {
    return R.color.transparent;
  }

  @Override public String getNoDataStr() {
    return "没有满足条件的职位";
  }

  public boolean isHasItem() {
    return hasItem;
  }

  @Override public void onList(List<Job> jobs, int page, int total) {
    if (jobs != null) {
      if (page == 1) {
        commonFlexAdapter.clear();
        if (jobs.size() == 0) {
          hasItem = false;
          setDatas(new ArrayList<AbstractFlexibleItem>(), 1);
        } else {
          hasItem = true;
        }
      }
      commonFlexAdapter.setEndlessTargetCount(total);
      for (Job resume : jobs) {
        commonFlexAdapter.addItem(new RecruitPositionItem(resume));
      }
    }
  }
}
