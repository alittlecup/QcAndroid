package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.items.FilterHeadItem;
import cn.qingchengfit.items.SearchCenterItem;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.MyJobsItem;
import cn.qingchengfit.recruit.item.RecruitPositionItem;
import cn.qingchengfit.recruit.item.ResumeAndJobItem;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.network.response.JobListIndex;
import cn.qingchengfit.recruit.presenter.SeekPositionPresenter;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.support.animator.FlipAnimation;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
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
 * Created by Paper on 2017/5/23.
 */
public class SeekPositionHomeFragment extends JobsListFragment
    implements SeekPositionPresenter.MVPView, ResumeFilterFragment.ResumeFilterListener {

  protected HashMap<String, Object> params = new HashMap<>();
  protected FilterHeadItem itemfilterHeader;
  @Inject SeekPositionPresenter positionPresenter;
  @Inject RecruitRouter router;
  Toolbar toolbar;
  TextView toolbarTitile;
  SwipeRefreshLayout layoutFilter;
  LinearLayout layoutSearch;
  EditText searchEt;
  ImageView imgClear;
  Button btnCancel;
  private long lastInvintedTime;
  private long nowInvintedTime;
  private MyJobsItem itemMyJobs;
  private ResumeAndJobItem itemRj;//我的简历和我的专场招聘会

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    lastInvintedTime = PreferenceUtils.getPrefLong(getContext(), "recruit_last_invented_time", 0);
    isChild = true;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    LinearLayout view = (LinearLayout) inflater.inflate(R.layout.layout_toolbar_container, null);
    layoutFilter = (SwipeRefreshLayout) inflater.inflate(R.layout.layout_filter_container, null);
    layoutFilter.setOnRefreshListener(this);
    ((FrameLayout) layoutFilter.getChildAt(1)).addView(v, 0);
    toolbar = ButterKnife.findById(view, R.id.toolbar);
    toolbarTitile = ButterKnife.findById(view, R.id.toolbar_title);
    searchEt = ButterKnife.findById(view, R.id.tb_searchview_et);
    layoutSearch = ButterKnife.findById(view, R.id.searchview);
    imgClear = ButterKnife.findById(view, R.id.tb_searchview_clear);
    btnCancel = ButterKnife.findById(view, R.id.tb_searchview_cancle);
    view.addView(layoutFilter, 1);
    delegatePresenter(positionPresenter, this);
    initToolbar(toolbar);
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
    PreferenceUtils.setPrefInt(getContext(), "recruit_home", 0);
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

  @Override public boolean onItemClick(int i) {
    if (getItem(i) instanceof SearchCenterItem) {
      onClickFakeSearch();
      return true;
    }
    return super.onItemClick(i);
  }

  private void initBus() {
    RxBusAdd(EventClickViewPosition.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventClickViewPosition>() {
          @Override public void call(EventClickViewPosition eventClickViewPosition) {
            if (eventClickViewPosition.getId() == R.id.layout_i_sent) {
              router.mySent();
            } else if (eventClickViewPosition.getId() == R.id.layout_i_invited) {
              router.myInvited();
            } else if (eventClickViewPosition.getId() == R.id.layout_i_stared) {
              router.myStarred();
            } else if (eventClickViewPosition.getId() == R.id.layout_my_resume) {
              router.toMyResume();
            } else if (eventClickViewPosition.getId() == R.id.layout_my_jobfair) {
              router.myUserJobFair();
            }
          }
        });
  }

  @Override protected void initView() {
    commonFlexAdapter.setStickyHeaders(true)
        .setDisplayHeadersAtStartUp(true)
        .setStickyHeaderElevation(1);
    super.initView();
    //rv.setBackgroundResource(R.color.white);
    rv.setClipToPadding(false);
    rv.addItemDecoration(
        new FlexibleItemDecoration(getContext()).addItemViewType(R.layout.item_recruit_position, 1)
            .addItemViewType(R.layout.item_resume_and_jobfair, 1)
            .addItemViewType(R.layout.item_my_jobs, 1)
            .addItemViewType(R.layout.item_horizon_qcradiogroup, 1)
            .removeItemViewType(R.layout.layout_search_hint_center)
            .withDivider(R.drawable.divider_qc_base_line)
            .withBottomEdge(true));
    commonFlexAdapter.addItem(new SearchCenterItem(false, "搜索职位公司"));
    itemMyJobs = new MyJobsItem(false);
    commonFlexAdapter.addItem(itemMyJobs);
    itemRj = new ResumeAndJobItem();
    commonFlexAdapter.addItem(itemRj);
    itemfilterHeader = new FilterHeadItem(getResources().getStringArray(R.array.filter_jobs));
    itemfilterHeader.setListener(new FilterHeadItem.FilterHeadListener() {
      @Override public void onPositionClick(int pos) {
        if (pos == -1) {
          getChildFragmentManager().popBackStack();
          clearFilterToggle();
          return;
        }
        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() < 3) {
          linearLayoutManager.smoothScrollToPosition(rv, null, 3);
        }
        RxRegiste(Observable.just(pos)
            .delay(linearLayoutManager.findFirstCompletelyVisibleItemPosition() < 3 ? 500 : 1,
                TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Integer>() {
              @Override public void call(Integer integer) {
                showFilter(integer);
              }
            }));
      }
    });
    commonFlexAdapter.addItem(itemfilterHeader);
  }

  @Override protected void addDivider() {
    //rv.addItemDecoration(new QcLeftRightDivider(getContext(), 1, R.layout.item_resume, 0, 0));
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.inflateMenu(R.menu.menu_i_publish_job);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        router.resumeMarketHome();
        return false;
      }
    });
    toolbarTitile.setText("求职招聘");
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    positionPresenter.queryIndex();
  }

  @Override public void onRefresh() {
    initLoadMore();
    positionPresenter.queryList(true, params);
  }

  public void showFilter(int pos) {
    layoutFilter.setVisibility(View.VISIBLE);
    Fragment f = getChildFragmentManager().findFragmentByTag("filter");
    if (f == null) {
      f = new JobsFilterFragment();
      ((JobsFilterFragment) f).putParams(params);
      ((JobsFilterFragment) f).setListener(this);
      getChildFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out)
          .add(R.id.frag_filter, f, "filter")
          .addToBackStack(null)
          .commit();
    }
    ((JobsFilterFragment) f).showPos = pos;

    if (f.isVisible()) {
      if (f instanceof JobsFilterFragment) {
        ((JobsFilterFragment) f).show(pos);
      }
    } else {
      getChildFragmentManager().beginTransaction().show(f).commit();
    }
  }

  public void clearFilterToggle() {
    itemfilterHeader.clearAll();
    commonFlexAdapter.notifyItemChanged(commonFlexAdapter.getGlobalPositionOf(itemfilterHeader));
  }

  @Override public String getFragmentName() {
    return SeekPositionHomeFragment.class.getName();
  }

  /**
   * 数据结果
   *
   * @param jobs 工作列表
   * @param page 当前页面
   * @param totalCount 总共数量
   */
  @Override public void onList(List<Job> jobs, int page, int totalCount) {
    layoutFilter.setRefreshing(false);
    if (page == 1) {
      commonFlexAdapter.removeRange(4, commonFlexAdapter.getItemCount() - 4);
    }
    if (jobs != null && jobs.size() != 0) {
      commonFlexAdapter.setEndlessTargetCount(totalCount);
      rv.setPadding(0, 0, 0, MeasureUtils.autoPaddingBottom(108f, totalCount, getContext(),
          MeasureUtils.dpToPx(48f, getResources())));
      List<IFlexible> tm = new ArrayList<>();
      for (Job resume : jobs) {
        if (page == 1) {
          commonFlexAdapter.addItem(new RecruitPositionItem(resume));
        } else {
          tm.add(new RecruitPositionItem(resume));
        }
      }
      if (page != 1) commonFlexAdapter.onLoadMoreComplete(tm);
    } else {
      commonFlexAdapter.onLoadMoreComplete(null);
    }
    if (page == 1 && commonFlexAdapter.getItemCountOfTypes(R.layout.item_recruit_position) == 0) {
      addEmptyPage();
    }
  }

  @Override public void onLoadMore(int i, int i1) {
    positionPresenter.queryList(false, params);
  }

  /**
   * 简历 和 招聘会信息
   */
  @Override public void onJobsIndex(JobListIndex jobListIndex) {
    itemRj.setJobListIndex(jobListIndex);
    commonFlexAdapter.notifyItemChanged(2);
    nowInvintedTime = DateUtils.formatDateFromServer(jobListIndex.invited_at).getTime();
    boolean showRed =
        (!TextUtils.isEmpty(jobListIndex.invited_at)) && (lastInvintedTime < nowInvintedTime);
    itemMyJobs.setHasNewInvited(showRed);
    commonFlexAdapter.notifyItemChanged(1);
  }

  @Override public void onGym(Gym service) {

  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

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

  @Override public void onFilterDone(HashMap<String, Object> params, String cityName) {
    this.params.putAll(params);
    Object min = params.get("min_salary");
    Object max = params.get("max_salary");
    int ms = min == null ? -1 : (int) min;
    int ma = max == null ? -1 : (int) max;
    itemfilterHeader.setStrings(cityName == null ? "城市" : cityName,
        RecruitBusinessUtils.getSalary(ms, ma, "薪资"), "要求");
    commonFlexAdapter.notifyItemChanged(3);
    onRefresh();
  }

  @Override public void onDismiss() {
    clearFilterToggle();
  }
}
