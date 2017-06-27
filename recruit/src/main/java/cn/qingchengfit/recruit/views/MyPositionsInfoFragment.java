package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.RecruitPositionItem;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.presenter.MinePositionPresenter;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2017/5/26.
 */
public class MyPositionsInfoFragment extends BaseFragment
    implements MinePositionPresenter.MVPView, FlexibleAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
    FlexibleAdapter.EndlessScrollListener {
  public static final int MY_SENT = 0;
  public static final int MY_INVITED = 1;
  public static final int MY_STARED = 2;

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;

  @Inject RecruitRouter router;
  @Inject MinePositionPresenter presenter;
  RecruitPositionsFragment positionsFragment;
  private int type = 0;//0 我投递的  1 邀请我的 2 我收藏的

  public static MyPositionsInfoFragment newMySent() {
    return new MyPositionsInfoFragment();
  }

  public static MyPositionsInfoFragment newMyInvited() {
    MyPositionsInfoFragment m = new MyPositionsInfoFragment();
    m.type = MY_INVITED;
    return m;
  }

  public static MyPositionsInfoFragment newMyStared() {
    MyPositionsInfoFragment m = new MyPositionsInfoFragment();
    m.type = MY_STARED;
    return m;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    switch (type) {
      case MY_INVITED:
        positionsFragment = new RecruitPositionsInvitedFragment();
        break;
      case MY_STARED:
        positionsFragment = new RecruitPositionsStarredFragment();
        break;
      default:
        positionsFragment = new RecruitPositionsSentFragment();
        break;
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_my_positions, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    switch (type) {
      case MY_INVITED:
        toolbarTitile.setText("收到的邀约");
        break;
      case MY_STARED:
        toolbarTitile.setText("我收藏的职位");
        break;
      default:
        toolbarTitile.setText("我投递的职位");
    }
    positionsFragment.setListener(this);
    return view;
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof RecruitPositionsFragment) {
      freshData();
    }
  }

  private void freshData() {
    switch (type) {
      case MY_INVITED:
        presenter.queryPositionOfInvited(1);
        break;
      case MY_STARED:
        presenter.queryPositionOfStarred(1);
        break;
      default:
        presenter.queryPositionOfSent(1);
    }
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(positionsFragment);
  }

  @Override public String getFragmentName() {
    return MyPositionsInfoFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onJobList(List<Job> jobs, int page, int totalCount) {
    if (jobs == null) {
      positionsFragment.stopLoadMore();
    } else {
      positionsFragment.setTotalCount(totalCount);
      if (page == 1) {
        positionsFragment.setData(jobs);
      } else {
        positionsFragment.addData(jobs);
      }
    }
  }

  @Override public void onRefresh() {
    freshData();
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = positionsFragment.getItem(i);
    if (item != null && item instanceof RecruitPositionItem) {
      Job job = ((RecruitPositionItem) item).getJob();
      router.goJobDetail(job);
    }
    return true;
  }

  @Override public int getLayoutRes() {
    return R.id.frag_my_postion;
  }

  @Override public void noMoreLoad(int i) {
    positionsFragment.stopLoadMore();
  }

  @Override public void onLoadMore(int i, int i1) {
    switch (type) {
      case MY_INVITED:
        presenter.queryPositionOfInvited(0);
        break;
      case MY_STARED:
        presenter.queryPositionOfStarred(0);
        break;
      default:
        presenter.queryPositionOfSent(0);
    }
  }
}
