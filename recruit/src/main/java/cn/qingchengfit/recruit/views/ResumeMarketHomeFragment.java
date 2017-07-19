package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import cn.qingchengfit.events.EventRecycleClick;
import cn.qingchengfit.items.FilterHeadItem;
import cn.qingchengfit.items.SearchCenterItem;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.HorizonImageShowItem;
import cn.qingchengfit.recruit.item.JobFairFooterItem;
import cn.qingchengfit.recruit.item.JobFairHorizonItem;
import cn.qingchengfit.recruit.item.JobFairHorizonMatchParentItem;
import cn.qingchengfit.recruit.item.RecruitManageItem;
import cn.qingchengfit.recruit.model.EndFairTips;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.model.Resume;
import cn.qingchengfit.recruit.presenter.EndFairPresenter;
import cn.qingchengfit.recruit.presenter.RecruitGymDetailPresenter;
import cn.qingchengfit.recruit.presenter.ResumeMarketPresenter;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.support.animator.FlipAnimation;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.TipDialogFragment;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
public class ResumeMarketHomeFragment extends ResumeListFragment
    implements ResumeMarketPresenter.MVPView, FlexibleAdapter.OnStickyHeaderChangeListener,
    EndFairPresenter.MVPView, TipDialogFragment.OnDialogListener,
    RecruitGymDetailPresenter.MVPView {

  private static final int PULS_ITEM_COUNTS = 4;//非主要item的数量

  Toolbar toolbar;
  TextView toolbarTitile;
  SwipeRefreshLayout layoutFilter;
  LinearLayout layoutSearch;
  EditText searchEt;
  ImageView imgClear;
  Button btnCancel;
  @Inject EndFairPresenter endFairPresenter;
  @Inject RecruitGymDetailPresenter gymDetailPresenter;
  @Inject RecruitRouter router;
  private HorizonImageShowItem horizonImageShowItem;
  private RecruitManageItem recruitmanage;
  private FilterHeadItem filterHeadItem;
  private String gymId;
  private List<EndFairTips> endTipsList = new ArrayList<>();
  private int index;
  private boolean isClosed;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    isChild = true;
    //View view = inflater.inflate(R.layout.fragment_resume_market_home, container, false);
    View v = super.onCreateView(inflater, container, savedInstanceState);
    LinearLayout view = (LinearLayout) inflater.inflate(R.layout.layout_toolbar_container, null);
    layoutFilter = (SwipeRefreshLayout) inflater.inflate(R.layout.layout_filter_container, null);
    layoutFilter.setOnRefreshListener(this);
    delegatePresenter(endFairPresenter, this);
    delegatePresenter(gymDetailPresenter, this);
    ((FrameLayout) layoutFilter.getChildAt(1)).addView(v, 0);
    toolbar = ButterKnife.findById(view, R.id.toolbar);
    toolbarTitile = ButterKnife.findById(view, R.id.toolbar_title);
    searchEt = ButterKnife.findById(view, R.id.tb_searchview_et);
    layoutSearch = ButterKnife.findById(view, R.id.searchview);
    imgClear = ButterKnife.findById(view, R.id.tb_searchview_clear);
    btnCancel = ButterKnife.findById(view, R.id.tb_searchview_cancle);
    view.addView(layoutFilter, 1);
    layoutFilter.setOnRefreshListener(this);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);

    initBus();
    RxTextView.textChangeEvents(searchEt)
        .observeOn(AndroidSchedulers.mainThread())
        .debounce(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<TextViewTextChangeEvent>() {
          @Override public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
            netFilter(textViewTextChangeEvent.text().toString());
          }
        });
    RxView.clicks(btnCancel).subscribe(new Action1<Void>() {
      @Override public void call(Void aVoid) {
        onCancelSearch();
      }
    });
    RxView.clicks(imgClear).subscribe(new Action1<Void>() {
      @Override public void call(Void aVoid) {
        searchEt.setText("");
      }
    });
    PreferenceUtils.setPrefInt(getContext(), "recruit_home", 1);
    return view;
  }

  private void netFilter(String s) {
    if (TextUtils.isEmpty(s)) s = null;
    params.put("q", s);
    onRefresh();
  }

  public void onClickFakeSearch() {
    layoutSearch.setVisibility(View.VISIBLE);
    linearLayoutManager.smoothScrollToPosition(rv, null, 3);
  }

  public void onCancelSearch() {
    layoutSearch.setVisibility(View.GONE);
    params.remove("q");
    onRefresh();
  }

  private void initBus() {
    RxBusAdd(EventRecycleClick.class).subscribe(new Action1<EventRecycleClick>() {
      @Override public void call(EventRecycleClick eventRecycleClick) {
        if (eventRecycleClick.viewId == R.layout.item_more_job_fair) {
          router.toAllJobFair();
        } else if (eventRecycleClick.viewId == R.layout.item_jobfairs) {
          if (horizonImageShowItem.getChildItem(eventRecycleClick.postion) != null
              && horizonImageShowItem.getChildItem(
              eventRecycleClick.postion) instanceof JobFairHorizonItem) {
            router.toStaffJobFairDetail(((JobFairHorizonItem) horizonImageShowItem.getChildItem(
                eventRecycleClick.postion)).getJobFair());
          }
        }
      }
    });
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("招聘端");
    toolbar.inflateMenu(R.menu.menu_i_seek_job);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        router.jobsHome();
        return true;
      }
    });
  }

  @Override protected void initView() {
    commonFlexAdapter.setStickyHeaders(true)
        .setDisplayHeadersAtStartUp(true)
        .setStickyHeaderElevation(1);
    super.initView();
    rv.setClipToPadding(false);
    rv.addItemDecoration(
        new FlexibleItemDecoration(getContext()).addItemViewType(R.layout.item_resume, 1)
            .addItemViewType(R.layout.item_recruit_manage, 1)
            .addItemViewType(R.layout.item_horizon_qcradiogroup, 1)
            .withDivider(R.drawable.divider_qc_base_line)
            .withBottomEdge(true));
    commonFlexAdapter.addItem(new SearchCenterItem(false, "搜索关键字"));
    horizonImageShowItem = new HorizonImageShowItem(new ArrayList<AbstractFlexibleItem>());
    commonFlexAdapter.addItem(horizonImageShowItem);
    recruitmanage = new RecruitManageItem(0, 0, false);
    commonFlexAdapter.addItem(recruitmanage);
    filterHeadItem = new FilterHeadItem(getResources().getStringArray(R.array.filter_resume));
    filterHeadItem.setListener(new FilterHeadItem.FilterHeadListener() {
      @Override public void onPositionClick(int pos) {
        if (pos == -1) {
          getChildFragmentManager().popBackStack();
          clearFilterToggle();
          return;
        }
        if (linearLayoutManager.findFirstVisibleItemPosition() < 3) {
          linearLayoutManager.smoothScrollToPosition(rv, null, 3);
        }
        RxRegiste(Observable.just(pos)
            .delay(linearLayoutManager.findFirstVisibleItemPosition() < 3 ? 500 : 1,
                TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Integer>() {
              @Override public void call(Integer integer) {
                showFilter(integer);
              }
            }));
      }
    });
    commonFlexAdapter.addItem(filterHeadItem);
  }

  @Override protected void onFinishAnimation() {
    endFairPresenter.queryEndFairList();
  }

  @Override public String getFragmentName() {
    return ResumeMarketHomeFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override protected void removeMainItem() {
    //commonFlexAdapter.removeRange(4, commonFlexAdapter.getItemCount() - 4);
    commonFlexAdapter.removeItemsOfType(R.layout.item_resume);
  }

  @Override public void onResumeList(List<Resume> resumes, int total, int page) {
    layoutFilter.setRefreshing(false);
    super.onResumeList(resumes, total, page);
    if (page == 1 && commonFlexAdapter.getItemCountOfTypes(R.layout.item_resume) == 0) {
      addEmptyPage();
    }
  }

  @Override public void onLoadMore(int i, int i1) {
    presenter.queryResumeMarkets(false, params);
  }

  @Override public void onRefresh() {
    presenter.queryMyJobFairList();
    initLoadMore();
    params = ListUtils.mapRemoveNull(params);
    presenter.queryResumeMarkets(true, params);
  }

  /**
   * 招聘会相关数据
   */
  public void onJobFaris(List<JobFair> jobfairs, int fair_count, int job_count, int gym_count) {
    if (horizonImageShowItem != null) {
      List<AbstractFlexibleItem> items = new ArrayList<>();
      if (jobfairs != null) {
        if (jobfairs.size() == 1) {
          items.add(new JobFairHorizonMatchParentItem(jobfairs.get(0)));
        } else {
          for (JobFair jobfair : jobfairs) {
            items.add(new JobFairHorizonItem(jobfair));
          }
          if (items.size() >= 5) items.add(new JobFairFooterItem(fair_count));
        }
      }
      horizonImageShowItem.refresh(items);
    }
    if (recruitmanage != null) {
      recruitmanage.setJobCounts(job_count, gym_count);
      commonFlexAdapter.notifyItemChanged(commonFlexAdapter.getGlobalPositionOf(recruitmanage));
    }

  }

  @Override public void onStickyHeaderChange(int i) {
  }

  /**
   * 展示筛选项
   * @param
   */
  public void showFilter(int pos) {
    layoutFilter.setVisibility(pos >= 0 ? View.VISIBLE : View.GONE);
    Fragment f = getChildFragmentManager().findFragmentByTag("filter");
    if (f == null) {
      f = ResumeFilterFragment.newResumeFilter();
      ((ResumeFilterFragment) f).showPos = pos;
      ((ResumeFilterFragment) f).setListener(new ResumeFilterFragment.ResumeFilterListener() {
        @Override public void onFilterDone(HashMap<String, Object> params, String cityname) {
          ResumeMarketHomeFragment.this.params.putAll(params);
          Object min = params.get("min_salary");
          Object max = params.get("max_salary");
          int ms = min == null ? -1 : (int) min;
          int ma = max == null ? -1 : (int) max;
          filterHeadItem.setStrings(cityname == null ? "城市" : cityname,
              RecruitBusinessUtils.getSalary(ms, ma, "薪资"), "工作经验", "要求");
          commonFlexAdapter.notifyItemChanged(3);
          onRefresh();
        }

        @Override public void onDismiss() {
          clearFilterToggle();
        }
      });

      getChildFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out)
          .add(R.id.frag_filter, f, "filter")
          .addToBackStack(null)
          .commit();
    }
    if (f.isVisible()) {
      if (f instanceof ResumeFilterFragment) {
        ((ResumeFilterFragment) f).show(pos);
      }
    } else {
      ((ResumeFilterFragment) f).showPos = pos;
      getChildFragmentManager().beginTransaction().show(f).commit();
    }
  }

  public void clearFilterToggle() {
    filterHeadItem.clearAll();
    commonFlexAdapter.notifyItemChanged(commonFlexAdapter.getGlobalPositionOf(filterHeadItem));
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = commonFlexAdapter.getItem(i);
    if (item instanceof RecruitManageItem) {
      router.toManageRecruit(((RecruitManageItem) item).getJobsCount());
      return true;
    } else if (item instanceof SearchCenterItem) {
      onClickFakeSearch();
    }
    return super.onItemClick(i);
  }

  /**
   * 自定义切换动画
   */
  @Override public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
    if (nextAnim == R.anim.card_flip_left_in
        || nextAnim == R.anim.card_flip_right_in
        || nextAnim == R.anim.card_flip_left_out
        || nextAnim == R.anim.card_flip_right_out) {
      Animation animation;
      if (nextAnim == R.anim.card_flip_left_in) {
        animation = FlipAnimation.create(FlipAnimation.LEFT, enter, 500);
      } else if (nextAnim == R.anim.card_flip_right_in) {
        animation = FlipAnimation.create(FlipAnimation.RIGHT, enter, 500);
      } else if (nextAnim == R.anim.card_flip_left_out) {
        animation = FlipAnimation.create(FlipAnimation.LEFT, enter, 500);
      } else {
        animation = FlipAnimation.create(FlipAnimation.RIGHT, enter, 500);
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

  @Override public void onEndFairList(List<EndFairTips> endFairList) {
    if (endFairList == null || endFairList.size() <= 0){
      return;
    }
    EndFairTips endFairTips = endFairList.get(0);
    endTipsList.clear();
    this.endTipsList.addAll(endFairList);
    this.gymId = endFairTips.id;
    TipDialogFragment dialogFragment = TipDialogFragment.newInstance(
        getString(R.string.tips_fair_end, endFairTips.name, endFairTips.fair.name), "立即处理",
        R.drawable.ic_dialog_hire_warning);
    dialogFragment.setOnDialogListener(this);
    dialogFragment.show(getChildFragmentManager(), null);
  }

  @Override public void onDoClick(View v) {
    isClosed = true;
    gymDetailPresenter.queryGymDetail(gymId);
  }

  @Override public void onDismissListener() {
    index++;
    if (index < endTipsList.size() && !isClosed) {
      TipDialogFragment dialogFragment = TipDialogFragment.newInstance(
          getString(R.string.tips_fair_end, endTipsList.get(index).name, endTipsList.get(index).fair.name), "立即处理", R.drawable.ic_dialog_hire_warning);
      dialogFragment.setOnDialogListener(this);
      dialogFragment.show(getChildFragmentManager(), null);
    }else{
      isClosed = false;
    }
  }

  @Override public void onGym(Gym gym) {
    if (gym != null) {
      router.toReplaceGymDetail(gym);
    }
  }

  @Override public void onJobList(List<Job> jobs, int page, int totalCount) {

  }

  @Override public void onPermission(boolean has) {

  }
}
