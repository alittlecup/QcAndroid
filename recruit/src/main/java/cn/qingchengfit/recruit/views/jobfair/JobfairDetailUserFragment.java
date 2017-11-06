package cn.qingchengfit.recruit.views.jobfair;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.items.FilterHeadItem;
import cn.qingchengfit.items.TitleHintItem;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.item.ExpendedTextviewItem;
import cn.qingchengfit.recruit.item.RecruitPositionItem;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.presenter.JobFairDetailPresenter;
import cn.qingchengfit.recruit.views.SeekPositionHomeFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
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
 * Created by Paper on 2017/7/16.
 */

public class JobfairDetailUserFragment extends SeekPositionHomeFragment
    implements JobFairDetailPresenter.MVPView {

  CoordinatorLayout coordinatorLayout;
  AppBarLayout smoothAppBarLayout;
  TextView tvName;
  TextView tvDuring;
  Toolbar toolbar;
  ImageView imageRecruit;
  @Inject JobFairDetailPresenter presenter;

  private JobFair jobFair;

  public static JobfairDetailFragment newUserJobFair(JobFair jobfair) {
    Bundle args = new Bundle();
    args.putParcelable("jobfair", jobfair);
    JobfairDetailFragment fragment = new JobfairDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) jobFair = getArguments().getParcelable("jobfair");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    coordinatorLayout =
        (CoordinatorLayout) inflater.inflate(R.layout.layout_jobfair_toolbar_container, null);
    coordinatorLayout.addView(v, 1);
    smoothAppBarLayout = container.findViewById(R.id.smooth_app_bar_layout);

    presenter.queryJobFairDetail(jobFair.id);
    return coordinatorLayout;
  }

  @Override public void onRefresh() {
    presenter.queryJobFairJobs(jobFair.id, params);
  }

  private void initAppbar() {
    smoothAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
          tvDuring.setVisibility(View.VISIBLE);
          tvName.setVisibility(View.VISIBLE);
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
          tvDuring.setVisibility(View.GONE);
          tvName.setVisibility(View.GONE);
        } else {
        }
      }
    });
  }

  @Override protected void initView(Bundle sav) {
    commonFlexAdapter.setStickyHeaders(true).setDisplayHeadersAtStartUp(true);
    super.initView(sav);
    rv.setClipToPadding(false);
  }

  @Override public void onJobfairDetail(JobFair jobfair) {
    PhotoUtils.origin(imageRecruit, jobfair.banner);
    tvName.setText(jobfair.name);
    tvDuring.setText(DateUtils.getDuringFromServer(jobfair.start, jobfair.end));
    commonFlexAdapter.addItem(new ExpendedTextviewItem(jobfair.description));
    commonFlexAdapter.addItem(new TitleHintItem("本期热招职位"));
    commonFlexAdapter.addItem(
        new FilterHeadItem(getResources().getStringArray(R.array.filter_jobs)));

    itemfilterHeader = new FilterHeadItem(getResources().getStringArray(R.array.filter_jobs));
    itemfilterHeader.setListener(new FilterHeadItem.FilterHeadListener() {
      @Override public void onPositionClick(int pos) {
        if (pos == -1) {
          getChildFragmentManager().popBackStack();
          clearFilterToggle();
          return;
        }
        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() < 3) {
          linearLayoutManager.smoothScrollToPosition(rv, null, 2);
        }
        RxRegiste(Observable.just(pos)
            .delay(linearLayoutManager.findFirstCompletelyVisibleItemPosition() < 2 ? 500 : 1,
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
    onRefresh();
  }

  @Override public void onList(List list) {
    if (list != null) {
      commonFlexAdapter.removeItemsOfType(R.layout.item_recruit_position);
      for (Object o : list) {
        if (o instanceof RecruitPositionItem) {
          commonFlexAdapter.addItem((RecruitPositionItem) o);
        }
      }
    }
  }
}
