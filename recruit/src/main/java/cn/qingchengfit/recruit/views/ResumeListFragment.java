package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitConstants;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.ResumeItem;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.model.Resume;
import cn.qingchengfit.recruit.presenter.ResumeMarketPresenter;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.views.fragments.BaseListFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
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
 * Created by Paper on 2017/7/3.
 */
public class ResumeListFragment extends BaseListFragment
    implements ResumeMarketPresenter.MVPView, FlexibleAdapter.OnItemClickListener,
    FlexibleAdapter.EndlessScrollListener, SwipeRefreshLayout.OnRefreshListener {

  protected HashMap<String, Object> params = new HashMap<>();
  protected boolean hasItem = false;
  @Inject ResumeMarketPresenter presenter;
  @Inject RecruitRouter router;
  @Inject QcRestRepository restRepository;

  public static ResumeListFragment newResumeListInstance() {
    ResumeListFragment fragment = new ResumeListFragment();
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

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    onRefresh();
  }

  @Override public String getFragmentName() {
    return ResumeListFragment.class.getName();
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.no_find_job;
  }

  @Override public String getNoDataStr() {
    return "没有匹配的简历";
  }

  protected void removeMainItem() {
    commonFlexAdapter.clear();
  }
  @Override public void onResumeList(List<Resume> resumes, int total, int page) {
    if (srl != null) srl.setRefreshing(false);
    if (resumes != null) {
      if (page == 1) {
        removeMainItem();
        commonFlexAdapter.setEndlessTargetCount(total);
        if (resumes.size() == 0) {
          hasItem = false;
        } else {
          hasItem = true;
        }
      }
      List<IFlexible> items = new ArrayList<>();
      for (Resume resume : resumes) {
        items.add(new ResumeItem(resume));
      }
      commonFlexAdapter.onLoadMoreComplete(items, 500);
    } else {
      commonFlexAdapter.onLoadMoreComplete(null);
    }
  }

  @Override
  public void onJobFaris(List<JobFair> jobfairs, int fair_count, int job_count, int gym_count) {

  }

  public boolean isHasItem() {
    return hasItem;
  }

  public void setHasItem(boolean hasItem) {
    this.hasItem = hasItem;
  }


  @Override public void noMoreLoad(int i) {
    stopLoadMore();
  }

  @Override public void onLoadMore(int i, int i1) {
    presenter.queryResumeMarkets(false, params);
  }

  @Override public boolean onItemClick(int i) {
    if (getItem(i) instanceof ResumeItem) {
      if (commonFlexAdapter.getStatus() == 0) {
        router.toResumeDetail(((ResumeItem) getItem(i)).getResume().id,
            restRepository.getHost() + RecruitConstants.RESUME_WEB_PATH);
      } else {
        commonFlexAdapter.toggleSelection(i);
        commonFlexAdapter.notifyItemChanged(i);
      }
    }
    return true;
  }

  @Override public void onRefresh() {
    initLoadMore();
    params = ListUtils.mapRemoveNull(params);
    presenter.queryResumeMarkets(true, params);
  }
}
