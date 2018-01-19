package cn.qingchengfit.staffkit.views.student.followup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventFilterClick;
import cn.qingchengfit.model.responese.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.QcFilterToggle;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/5.
 */
@FragmentWithArgs public class FollowUpDataStatisticsFragment extends BaseFragment
    implements FollowUpDataStatisticsPresenter.PresenterView {
  @Arg int statusChangeType;//新增注册 新增跟进 和 新增会员

  FollowUpDataStatisticsBaseFragment fragment0;

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject FollowUpDataStatisticsPresenter presenter;
  @Inject SerPermisAction serPermisAction;
  int offDays = 7;
  //TopFilterSaleFragment saleFragment;
  //FilterTimesFragment latestTimeFragment;
  @BindView(R.id.qft_saler) QcFilterToggle qftSaler;
  @BindView(R.id.qft_times) QcFilterToggle qftTimes;
  //@BindView(R.id.frag_contianer) FrameLayout layoutBg;
  private StudentFilter filter = new StudentFilter();
  //private OnCheckedChangeListener filterListenr = new OnCheckedChangeListener() {
  //  @Override public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
  //    layoutBgHalfTrans.setVisibility(b ? View.VISIBLE : View.GONE);
  //    if (!b) hideAll();
  //    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
  //    valueAnimator.setInterpolator(new LinearInterpolator());
  //    valueAnimator.setDuration(200);
  //    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
  //      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
  //        ViewGroup.LayoutParams layoutParams = layoutBg.getLayoutParams();
  //        if (!b) {
  //          layoutParams.height = (int) ((1 - (float) valueAnimator.getAnimatedValue())
  //              * MeasureUtils.getScreenHeight(getResources())) + MeasureUtils.dpToPx(200f,
  //              getResources());
  //          layoutBg.setLayoutParams(layoutParams);
  //          getView().postInvalidateOnAnimation();
  //        } else {
  //
  //          layoutParams.height =
  //              (int) ((float) valueAnimator.getAnimatedValue() * MeasureUtils.getScreenHeight(
  //                  getResources()));
  //          layoutBg.setLayoutParams(layoutParams);
  //          getView().postInvalidateOnAnimation();
  //        }
  //      }
  //    });
  //    valueAnimator.start();
  //  }
  //};

  public FollowUpDataStatisticsFragment() {
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_follow_up_data_statistics, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initBus();
    switch (statusChangeType) {
      case 1://新增跟进
        fragment0 = new FollowUpDataStatistics1Fragment();
        break;
      case 2://新增会员
        fragment0 = new FollowUpDataStatistics2Fragment();
        break;
      default://新增注册
        fragment0 = new FollowUpDataStatistics0Fragment();
        break;
    }

    fragment0.touchable = true;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(System.currentTimeMillis()));
    String end = DateUtils.Date2YYYYMMDD(new Date(System.currentTimeMillis()));
    calendar.add(Calendar.DATE, -6);
    String start = DateUtils.Date2YYYYMMDD(calendar.getTime());
    filter.registerTimeEnd = end;
    filter.registerTimeStart = start;

    if (!serPermisAction.checkHasAllMember()) {
      qftSaler.setText(loginStatus.staff_name());
    }


    isLoading = true;
    return view;
  }

  @Override protected void onFinishAnimation() {

    getChildFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.alpha_in, R.anim.slide_hold)
        .replace(R.id.frag_status_graph_0, fragment0)
        .commit();
    presenter.getStudentsStatistics(filter);
  }

  /**
   * 点击背景关闭弹窗
   */
  //@OnClick(R.id.frag_follow_up_filter_container) public void onClickBg() {
    //filterListenr.onCheckedChanged(null, false);
  //}

  private void initBus() {
    RxBusAdd(FollowUpFilterEvent.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<FollowUpFilterEvent>() {
          @Override public void call(final FollowUpFilterEvent followUpFilterEvent) {
            RxBus.getBus().post(new EventFilterClick(0));
            if (followUpFilterEvent.page > 0) return;
            switch (followUpFilterEvent.eventType) {
              case FollowUpFilterEvent.EVENT_SALE_ITEM_CLICK:
                filter.sale = followUpFilterEvent.filter.sale;
                if (filter.sale == null){
                  qftSaler.setText("销售");
                }else
                  qftSaler.setText(filter.sale.getUsername());
                qftSaler.setChecked(false);
                break;

              case FollowUpFilterEvent.EVENT_LATEST_TIME_CLICK:
                if (followUpFilterEvent.position < 2) {
                  Calendar calendar = Calendar.getInstance();
                  calendar.setTime(new Date(System.currentTimeMillis()));
                  String end = DateUtils.Date2YYYYMMDD(new Date(System.currentTimeMillis()));
                  if (followUpFilterEvent.position == 0) {
                    calendar.add(Calendar.DATE, -7);
                  } else {
                    calendar.add(Calendar.DATE, -30);
                  }
                  String start = DateUtils.Date2YYYYMMDD(calendar.getTime());
                  filter.registerTimeEnd = end;
                  filter.registerTimeStart = start;
                  qftTimes.setText(followUpFilterEvent.position == 0 ? "最近7天" : "最近30天");
                  qftTimes.setChecked(false);
                  offDays = followUpFilterEvent.position == 0 ? 7 : 30;
                }
                break;
              case FollowUpFilterEvent.EVENT_LATEST_TIME_CUSTOM_DATA:
                qftTimes.setChecked(false);
                filter.registerTimeStart = followUpFilterEvent.start;
                filter.registerTimeEnd = followUpFilterEvent.end;
                qftTimes.setText(filter.registerTimeStart + "-" + filter.registerTimeEnd);
                offDays = DateUtils.interval(followUpFilterEvent.start, followUpFilterEvent.end);
                break;
            }
            getActivity().runOnUiThread(new Runnable() {
              @Override public void run() {
                showLoading();
              }
            });
            presenter.getStudentsStatistics(filter);
          }
        });
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public String getFragmentName() {
    return this.getClass().getName();
  }

  @OnClick({
      R.id.qft_saler, R.id.qft_times
  }) public void onClick(View view) {
    FragmentTransaction ts = getChildFragmentManager().beginTransaction();
    switch (view.getId()) {
      case R.id.qft_saler:
        if (!serPermisAction.checkHasAllMember()) {
          ToastUtils.show("您只能查看自己名下的会员");
          return;
        }
        RxBus.getBus().post(new EventFilterClick(FollowUpStatusFragment.FILTER_TOP_SALES));
        break;
      case R.id.qft_times:
        RxBus.getBus().post(new EventFilterClick(FollowUpStatusFragment.FILTER_TOP_DAYS));

        break;
    }
  }

  @Override public void onFollowUpStatistics(FollowUpDataStatistic statistics) {
    hideLoading();
    switch (statusChangeType) {
      case 1://新增跟进
        fragment0.setData(statistics.new_following_users, offDays);
        break;
      case 2://新增会员
        fragment0.setData(statistics.new_member_users, offDays);
        break;
      default://新增注册
        fragment0.setData(statistics.new_create_users, offDays);
        break;
    }
  }
}
