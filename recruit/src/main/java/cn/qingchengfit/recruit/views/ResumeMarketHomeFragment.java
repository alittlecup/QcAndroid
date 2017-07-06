package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.items.FilterHeadItem;
import cn.qingchengfit.items.SearchCenterItem;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.HorizonImageShowItem;
import cn.qingchengfit.recruit.item.JobFairFooterItem;
import cn.qingchengfit.recruit.item.JobFairItem;
import cn.qingchengfit.recruit.item.RecruitManageItem;
import cn.qingchengfit.recruit.item.ResumeItem;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.model.Resume;
import cn.qingchengfit.recruit.presenter.ResumeMarketPresenter;
import cn.qingchengfit.support.animator.FlipAnimation;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcLeftRightDivider;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2017/6/6.
 */
public class ResumeMarketHomeFragment extends BaseFragment
    implements ResumeMarketPresenter.MVPView, FlexibleAdapter.OnStickyHeaderChangeListener,
    FlexibleAdapter.OnItemClickListener {

  private static final int PULS_ITEM_COUNTS = 4;//非主要item的数量
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.rv) RecyclerView rv;
  @BindView(R2.id.srl) SwipeRefreshLayout srl;
  @Inject RecruitRouter router;
  @Inject ResumeMarketPresenter presenter;
  private CommonFlexAdapter commonFlexAdapter;
  private HorizonImageShowItem horizonImageShowItem;
  private RecruitManageItem recruitmanage;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_market_home, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    inflatJobFairs();
    srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        srl.setRefreshing(false);
      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("求职招聘");
    toolbar.inflateMenu(R.menu.menu_i_seek_job);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        getActivity().onBackPressed();
        return true;
      }
    });
  }

  @Override public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
    if (nextAnim == R.anim.card_flip_left_in
        || nextAnim == R.anim.card_flip_right_in
        || nextAnim == R.anim.card_flip_left_out
        || nextAnim == R.anim.card_flip_right_out) {
      Animation animation;
      if (nextAnim == R.anim.card_flip_left_in) {
        animation = FlipAnimation.create(FlipAnimation.LEFT, enter, 300);
      } else if (nextAnim == R.anim.card_flip_right_in) {
        animation = FlipAnimation.create(FlipAnimation.RIGHT, enter, 300);
      } else if (nextAnim == R.anim.card_flip_left_out) {
        animation = FlipAnimation.create(FlipAnimation.LEFT, enter, 300);
      } else {
        animation = FlipAnimation.create(FlipAnimation.RIGHT, enter, 300);
      }

      animation.setAnimationListener(new Animation.AnimationListener() {
        @Override public void onAnimationStart(Animation animation) {

        }

        @Override public void onAnimationEnd(Animation animation) {
          onFinishAnimation();
        }

        @Override public void onAnimationRepeat(Animation animation) {

        }
      });
      return animation;
    } else {
      return super.onCreateAnimation(transit, enter, nextAnim);
    }
  }

  public void inflatJobFairs() {
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    //List<String> tabs = new ArrayList<>();
    //tabs.add("期望城市");
    //tabs.add("期望薪资");
    //tabs.add("工作经验");
    //tabs.add("要求");
    //List<View> views = new ArrayList<>();
    //views.add(new TextView(getContext()));
    //views.add(new TextView(getContext()));
    //views.add(new TextView(getContext()));
    //views.add(new TextView(getContext()));
    //DropDownMenu dropDownMenu = (DropDownMenu) LayoutInflater.from(getContext()).inflate(R.layout.layout_filter_drop_menu,null);
    //dropDownMenu.setDropDownMenu(tabs,views,new View(getContext()));

    commonFlexAdapter.setStickyHeaders(true).setDisplayHeadersAtStartUp(true);
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rv.setAdapter(commonFlexAdapter);

    rv.addItemDecoration(
        new QcLeftRightDivider(getContext(), 15, R.layout.item_recruit_manage, 0, 0));
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    commonFlexAdapter.addItem(new SearchCenterItem(false, "搜索关键字"));
    horizonImageShowItem = new HorizonImageShowItem(new ArrayList<AbstractFlexibleItem>());
    commonFlexAdapter.addItem(horizonImageShowItem);
    recruitmanage = new RecruitManageItem(1, false);
    commonFlexAdapter.addItem(recruitmanage);
    FilterHeadItem filterHeadItem =
        new FilterHeadItem(new String[] { "期望城市", "期望薪资", "工作经验", "要求" });
    commonFlexAdapter.addItem(filterHeadItem);
    presenter.queryMyJobFairList();
  }

  @Override public String getFragmentName() {
    return ResumeMarketHomeFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onResumeList(List<Resume> resumes, int page, int total) {
    if (commonFlexAdapter == null || rv == null) return;
    commonFlexAdapter.setEndlessTargetCount(total + PULS_ITEM_COUNTS);
    if (page == 1) {
      commonFlexAdapter.removeItemsOfType(R.layout.item_resume);
    }
    for (Resume resume : resumes) {
      commonFlexAdapter.addItem(new ResumeItem(resume));
    }
  }

  @Override public void onJobFaris(List<JobFair> jobfairs, int job_count) {
    if (horizonImageShowItem != null) {
      List<AbstractFlexibleItem> items = new ArrayList<>();
      if (jobfairs != null) {
        for (JobFair jobfair : jobfairs) {
          items.add(new JobFairItem(jobfair));
        }
        items.add(new JobFairFooterItem(jobfairs.size()));
      }
      horizonImageShowItem.refresh(items);
    }
    if (recruitmanage != null) {
      recruitmanage.setJobCounts(job_count);
      commonFlexAdapter.notifyItemChanged(commonFlexAdapter.getGlobalPositionOf(recruitmanage));
    }
    presenter.queryResumeMarkets(true);
  }

  @Override public void onStickyHeaderChange(int i) {
    ToastUtils.show("stickheader:" + i);
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = commonFlexAdapter.getItem(i);
    if (item instanceof RecruitManageItem) {
      router.toManageRecruit();
      return false;
    }
    if (item instanceof ResumeItem){
      router.toResumeDetail();
      return false;
    }

    return false;
  }
}
