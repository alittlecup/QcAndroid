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
import cn.qingchengfit.views.fragments.BaseListFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
  @Inject ResumeMarketPresenter presenter;
  @Inject RecruitRouter router;
  @Inject QcRestRepository restRepository;
  private boolean hasItem = false;

  public static ResumeListFragment newResumeListInstance() {
    ResumeListFragment fragment = new ResumeListFragment();
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    delegatePresenter(presenter, this);
    View view = super.onCreateView(inflater, container, savedInstanceState);

    rv.setNestedScrollingEnabled(false);
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

  @Override public void onResumeList(List<Resume> resumes, int total, int page) {
    if (resumes != null) {
      if (page == 1) {
        commonFlexAdapter.clear();
        if (resumes.size() == 0) {
          hasItem = false;
          setDatas(new ArrayList<AbstractFlexibleItem>(), 1);
        } else {
          hasItem = true;
        }
      }
      commonFlexAdapter.setEndlessTargetCount(total);
      for (Resume resume : resumes) {
        commonFlexAdapter.addItem(new ResumeItem(resume));
      }
    }
  }

  public boolean isHasItem() {
    return hasItem;
  }

  public void setHasItem(boolean hasItem) {
    this.hasItem = hasItem;
  }

  @Override public void onJobFaris(List<JobFair> jobfairs, int job_count) {

  }

  @Override public void noMoreLoad(int i) {
    stopLoadMore();
  }

  @Override public void onLoadMore(int i, int i1) {
    presenter.queryResumeMarkets(false, params);
  }

  @Override public boolean onItemClick(int i) {
    if (getItem(i) instanceof ResumeItem) {
      router.toResumeDetail(((ResumeItem) getItem(i)).getResume().id,
          restRepository.getHost() + RecruitConstants.RESUME_WEB_PATH);
    }
    return true;
  }

  @Override public void onRefresh() {
    initLoadMore();
    presenter.queryResumeMarkets(true, params);
  }
}
