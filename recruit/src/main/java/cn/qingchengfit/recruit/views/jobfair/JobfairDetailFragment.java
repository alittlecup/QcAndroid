package cn.qingchengfit.recruit.views.jobfair;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.items.FilterHeadItem;
import cn.qingchengfit.items.PrimaryBtnItem;
import cn.qingchengfit.items.TextItem;
import cn.qingchengfit.items.TitleHintItem;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.ExpendedTextviewItem;
import cn.qingchengfit.recruit.item.FragmentListItem;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.presenter.JobFairDetailPresenter;
import cn.qingchengfit.recruit.presenter.RecruitPermissionPresenter;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.recruit.views.JobsFilterFragment;
import cn.qingchengfit.recruit.views.ResumeFilterFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
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
 * Created by Paper on 2017/7/7.
 */
public class JobfairDetailFragment extends BaseFragment
    implements JobFairDetailPresenter.MVPView, ResumeFilterFragment.ResumeFilterListener {

  protected FilterHeadItem itemfilterHeader;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @BindView(R2.id.rv) RecyclerView rv;
  @BindView(R2.id.tv_name) TextView tvName;
  @BindView(R2.id.tv_during) TextView tvDuring;
  @BindView(R2.id.frag_filter) FrameLayout layoutFilter;
  @Inject JobFairDetailPresenter presenter;
  @Inject RecruitPermissionPresenter PermissonPresenter;
  @Inject RecruitRouter router;
  @BindView(R2.id.smooth_app_bar_layout) AppBarLayout smoothAppBarLayout;
  @BindView(R2.id.image_recruit) ImageView imageRecruit;
  CommonFlexAdapter commonFlexAdapter;
  private int type = 0;//0是招聘端的  1：是求职端
  private JobFair jobFair;
  private FragmentListItem resumeListItem;
  private HashMap<String, Object> params = new HashMap<>();
  private LinearLayoutManager linearLayoutManager;

  public static JobfairDetailFragment newStaffJobFair(JobFair jobfair) {
    Bundle args = new Bundle();
    args.putParcelable("jobfair", jobfair);
    args.putInt("type", 0);
    JobfairDetailFragment fragment = new JobfairDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static JobfairDetailFragment newUserJobFair(JobFair jobfair) {
    Bundle args = new Bundle();
    args.putParcelable("jobfair", jobfair);
    args.putInt("type", 1);
    JobfairDetailFragment fragment = new JobfairDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      type = getArguments().getInt("type");
      jobFair = getArguments().getParcelable("jobfair");
    }
    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    //  getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
    //  getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    //}
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_jobfair_detail, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    linearLayoutManager = new SmoothScrollLinearLayoutManager(getContext());
    rv.setLayoutManager(linearLayoutManager);
    rv.addItemDecoration(
        new FlexibleItemDecoration(getContext()).addItemViewType(R.layout.item_resume, 1)
            .addItemViewType(R.layout.item_recruit_position, 1)
            .addItemViewType(R.layout.item_horizon_qcradiogroup, 1)
            .withDivider(R.drawable.divider_qc_base_line)
            .withBottomEdge(true));
    commonFlexAdapter.setStickyHeaders(true)
        .setDisplayHeadersAtStartUp(true)
        .setStickyHeaderElevation(1);
    rv.setAdapter(commonFlexAdapter);
    initToolbar(toolbar);
    initAppbar();
    initBus();
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("招聘会详情");
  }

  private void initAppbar() {
    smoothAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        LogUtil.e("offset:" + verticalOffset);
        if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
          tvDuring.setVisibility(View.GONE);
          tvName.setVisibility(View.GONE);
          toolbarTitle.setVisibility(View.VISIBLE);
        } else {
          tvDuring.setVisibility(View.VISIBLE);
          tvName.setVisibility(View.VISIBLE);
          toolbarTitle.setVisibility(View.GONE);
        }
      }
    });
  }

  private void initBus() {
    RxBusAdd(EventClickViewPosition.class).subscribe(new Action1<EventClickViewPosition>() {
      @Override public void call(EventClickViewPosition eventClickViewPosition) {
        if (eventClickViewPosition.getId() == R.layout.item_primary_rect_stroke_btn) {
          router.toSignUpFair(jobFair);
        }
      }
    });
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.queryJobFairDetail(jobFair.id);
  }

  @Override public String getFragmentName() {
    return JobfairDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onJobfairDetail(JobFair jobfair) {
    PhotoUtils.middle(imageRecruit, jobfair.banner);
    tvName.setText(jobfair.name);
    tvDuring.setText(DateUtils.getDuringFromServer(jobfair.start, jobfair.end));
    commonFlexAdapter.addItem(new ExpendedTextviewItem(jobfair.description));
    queryListData();
  }

  @Override public void onList(List list) {
    if (list != null) {
      commonFlexAdapter.removeItemsOfType(R.layout.item_resume, R.layout.item_recruit_position);
      rv.setPadding(0, 0, 0, MeasureUtils.autoPaddingBottom(108f, list.size(), getContext(),
          MeasureUtils.dpToPx(48f, getResources())));
      for (Object o : list) {
        if (o instanceof IFlexible) commonFlexAdapter.addItem((IFlexible) o);
      }
    }
  }

  public void showFilter(int pos) {
    layoutFilter.setVisibility(View.VISIBLE);
    Fragment f = getChildFragmentManager().findFragmentByTag("filter");
    if (f == null) {
      if (type == 0) {
        f = new JobsFilterFragment();
      } else {
        f = ResumeFilterFragment.newResumeFilter();
      }
      ((ResumeFilterFragment) f).showPos = pos;
      ((ResumeFilterFragment) f).putParams(params);
      ((ResumeFilterFragment) f).setListener(this);
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
      getChildFragmentManager().beginTransaction().show(f).commit();
    }
  }

  public void queryListData() {
    if (type == 0) {
      if (DateUtils.isOutOfDate(DateUtils.formatDateFromServer(jobFair.end))) {
        commonFlexAdapter.addItem(new TextItem("场馆报名已结束!", R.style.QcTextStyleStandardWarm));
      } else {
        commonFlexAdapter.addItem(new PrimaryBtnItem("申请参加"));
      }
      commonFlexAdapter.addItem(new TitleHintItem("招聘会人才"));
      itemfilterHeader = new FilterHeadItem(getResources().getStringArray(R.array.filter_resume));

      commonFlexAdapter.addItem(itemfilterHeader);

    } else {
      commonFlexAdapter.addItem(new TitleHintItem("本期热招职位"));
      itemfilterHeader = new FilterHeadItem(getResources().getStringArray(R.array.filter_resume));
      commonFlexAdapter.addItem(itemfilterHeader);

    }
    itemfilterHeader.setListener(new FilterHeadItem.FilterHeadListener() {
      @Override public void onPositionClick(int pos) {
        if (pos == -1) {
          getChildFragmentManager().popBackStack();
          clearFilterToggle();
          return;
        }
        smoothAppBarLayout.setExpanded(false);
        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() < 2) {
          linearLayoutManager.smoothScrollToPosition(rv, null, 3);
        }
        RxRegiste(Observable.just(pos)
            .delay(linearLayoutManager.findFirstCompletelyVisibleItemPosition() < 2 ? 300 : 1,
                TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Integer>() {
              @Override public void call(Integer integer) {
                showFilter(integer);
              }
            }));
      }
    });
    onRefresh();
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

  private void onRefresh() {
    if (type == 0) {
      presenter.queryJobFairResumes(jobFair.id, params);
    } else {
      presenter.queryJobFairJobs(jobFair.id, params);
    }
  }

  @Override public void onDismiss() {
    clearFilterToggle();
  }

  public void clearFilterToggle() {
    itemfilterHeader.clearAll();
    commonFlexAdapter.notifyItemChanged(commonFlexAdapter.getGlobalPositionOf(itemfilterHeader));
  }

}
