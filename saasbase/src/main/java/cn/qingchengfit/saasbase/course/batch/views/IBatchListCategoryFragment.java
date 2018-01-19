package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
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
 * Created by Paper on 2017/11/29.
 */
public abstract class IBatchListCategoryFragment extends BaseListFragment implements
  FlexibleAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {

  @Inject ICourseModel courseModel;

  protected Toolbar toolbar;
  protected TextView toolbarTitle;
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
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

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View child = super.onCreateView(inflater, container, savedInstanceState);
    setBackPress();
    ViewGroup parent = (ViewGroup) inflater.inflate(R.layout.layout_toolbar_container,container,false);
    parent.addView(child,1);
    toolbar = parent.findViewById(R.id.toolbar);
    toolbarTitle = parent.findViewById(R.id.toolbar_title);
    initToolbar(toolbar);
    return parent;
  }


  @Override protected void addDivider() {
    initListener(this);
    rv.addItemDecoration(new FlexibleItemDecoration(getContext())
      .addItemViewType(R.layout.item_saas_batch,15)
      .addItemViewType(R.layout.item_batch_category,1)
      .withBottomEdge(true)
    );
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    if (srl != null)
      srl.setRefreshing(true);
    onRefresh();
  }

  @Override public int getFbIcon() {
    return R.drawable.vd_add_batch;
  }

  @Override public String getFragmentName() {
    return IBatchListCategoryFragment.class.getName();
  }

  @Override public int getNoDataIconRes() {
    return 0;
  }

  @Override public String getNoDataStr() {
    return null;
  }


}
