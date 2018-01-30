package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.animator.FadeInUpItemAnimator;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.course.event.EventCourse;
import cn.qingchengfit.saasbase.course.course.views.CourseListParams;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.saasbase.gymconfig.views.MsgNotiParams;
import cn.qingchengfit.saasbase.gymconfig.views.OrderLimitParams;
import cn.qingchengfit.saasbase.qrcode.views.QRActivity;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.sensorsdata.analytics.android.sdk.SensorsDataTrackFragmentAppViewScreen;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import java.util.ArrayList;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

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
 * Created by Paper on 2017/1/9.
 */
@SensorsDataTrackFragmentAppViewScreen
public abstract class BatchListFragment extends SaasBaseFragment
    implements FlexibleAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

  @BindView(R2.id.toolbar_title) protected TextView toolbarTitile;
  @BindView(R2.id.toolbar) protected Toolbar toolbar;
  @BindView(R2.id.srl) protected SwipeRefreshLayout srl;
  @BindView(R2.id.rv_batch_list) RecyclerView rv;
  @BindView(R2.id.add_batch_btn) FloatingActionButton addBatchBtn;

  protected CommonFlexAdapter commonFlexAdapter;
  @Inject IPermissionModel serPermisAction;
  private LinearLayoutManager linearLayoutManager;
  protected final static String TARGET = "BatchListFragment";

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(),this);
    RxBus.getBus().register(EventCourse.class)
      .compose(bindToLifecycle())
      .compose(doWhen(FragmentEvent.CREATE_VIEW))
      .filter(eventCourse -> CmStringUtils.isEmpty(eventCourse.getSrc()) || TARGET.equalsIgnoreCase(eventCourse.getSrc()))
      .subscribe(new BusSubscribe<EventCourse>() {
        @Override public void onNext(EventCourse eventCourse) {
          routeTo("/batch/add/",new AddBatchParams()
            .mCourse(eventCourse.getCourse())
            .build());
        }
      });
    RxBus.getBus().register(EventSaasFresh.BatchList.class)
      .compose(bindToLifecycle())
      .compose(doWhen(FragmentEvent.CREATE_VIEW))
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventSaasFresh.BatchList>() {
        @Override public void onNext(EventSaasFresh.BatchList batchList) {
          onRefresh();
        }
      });
  }

  /**
   * flow sheet弹出菜单选择
   * @param pos        位置
   * @param isPrivate  是否为私教
   */
  protected void menuSelected(int pos,boolean isPrivate){
    switch (pos) {
      case 1://课程预约限制
        /**
         * 预约限制 {@link OrderLimitFragment}
         */
        if (!serPermisAction.check(PermissionServerUtils.TEAM_COURSE_LIMIT)) {
          showAlert(R.string.sorry_for_no_permission);
          return;
        }
        routeTo("gym","/orderlimit/",new OrderLimitParams().mIsPrivate(isPrivate).build());
        break;
      case 2://预约短信通知
        /**
         * 短信通知{@link MsgNotiFragment}
         */
        if (!serPermisAction.check(PermissionServerUtils.TEAM_COURSE_MSG_SETTING)) {
          showAlert(R.string.sorry_for_no_permission);
          return;
        }
        routeTo("gym","/msgnoti/",new MsgNotiParams().mIsPrivate(isPrivate).build());
        break;
      case 3://课件
        /**
         * 课件
         */
        if (!serPermisAction.check(PermissionServerUtils.PLANSSETTING)) {
          showAlert(R.string.sorry_for_no_permission);
          return;
        }
        QRActivity.start(getContext(), QRActivity.PLANS_SETTING_GROUP );
        break;
      default://课程种类
        routeTo("/list/",new CourseListParams().mIsPrivate(isPrivate).build());
        break;
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_sass_course_type_batch, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    linearLayoutManager = new LinearLayoutManager(getContext());
    rv.setLayoutManager(linearLayoutManager);
    rv.addItemDecoration(new FlexibleItemDecoration(getContext())
        .withDefaultDivider()
        .withOffset(1)
        .withBottomEdge(true));
    rv.setAdapter(commonFlexAdapter);
    rv.setItemAnimator(new FadeInUpItemAnimator());
    if (savedInstanceState != null && savedInstanceState.containsKey("p")) {
      linearLayoutManager.scrollToPosition(savedInstanceState.getInt("p", 0));
    }
    srl.setOnRefreshListener(this);
    return view;
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    if (linearLayoutManager != null)
      outState.putInt("p", linearLayoutManager.findFirstVisibleItemPosition());
    super.onSaveInstanceState(outState);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    onRefresh();
  }

  @OnClick(R2.id.add_batch_btn)
  public void clickAddBatch(){

  }

  @Override public String getFragmentName() {
    return BatchListFragment.class.getName();
  }

}
