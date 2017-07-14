package cn.qingchengfit.recruit.views.jobfair;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.qingchengfit.recruit.views.ResumeListFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import java.util.ArrayList;
import javax.inject.Inject;
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
public class JobfairDetailFragment extends BaseFragment implements JobFairDetailPresenter.MVPView {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @BindView(R2.id.rv) RecyclerView rv;
  @BindView(R2.id.tv_name) TextView tvName;
  @BindView(R2.id.tv_during) TextView tvDuring;
  @Inject JobFairDetailPresenter presenter;
  @Inject RecruitRouter router;
  CommonFlexAdapter commonFlexAdapter;
  @BindView(R2.id.smooth_app_bar_layout) AppBarLayout smoothAppBarLayout;
  @BindView(R2.id.image_recruit) ImageView imageRecruit;
  private int type = 0;//0是招聘端的  1：是求职端
  private JobFair jobFair;
  private FragmentListItem resumeListItem;

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
    rv.setLayoutManager(new LinearLayoutManager(getContext()));
    rv.setAdapter(commonFlexAdapter);
    initToolbar(toolbar);
    initAppbar();
    initBus();
    return view;
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

  public void queryListData() {
    if (type == 0) {
      if (DateUtils.isOutOfDate(DateUtils.formatDateFromServer(jobFair.end))) {
        commonFlexAdapter.addItem(new TextItem("场馆报名已结束!", R.style.QcTextStyleStandardWarm));
      } else {
        commonFlexAdapter.addItem(new PrimaryBtnItem("申请参加"));
      }
      commonFlexAdapter.addItem(new TitleHintItem("招聘会人才"));
      commonFlexAdapter.addItem(
          new FilterHeadItem(getResources().getStringArray(R.array.filter_resume)));
    } else {
      commonFlexAdapter.addItem(new TitleHintItem("本期热招职位"));
      commonFlexAdapter.addItem(
          new FilterHeadItem(getResources().getStringArray(R.array.filter_jobs)));
    }
  }
}
