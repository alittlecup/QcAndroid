package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saascommon.events.EventSaasFresh;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.github.clans.fab.FloatingActionButton;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
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

  FloatingActionButton fabMutiBatch;
  FloatingActionButton fabCopyBatch;

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
    initFloatButton();
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

  private void initFloatButton() {

    fabCopyBatch = new FloatingActionButton(getContext());
    fabMutiBatch = new FloatingActionButton(getContext());

    fabMutiBatch.setLabelText("批量排期");
    fabCopyBatch.setLabelText("复制排期");

    fab.addMenuButton(fabMutiBatch);
    fab.addMenuButton(fabCopyBatch);

    fabMutiBatch.setImageResource(R.drawable.fab_add);
    fabMutiBatch.setColorNormal(ContextCompat.getColor(getContext(), R.color.copy_batch_add_button_color));
    fabMutiBatch.setColorPressed(ContextCompat.getColor(getContext(), R.color.copy_batch_add_button_color));
    fabMutiBatch.setOnClickListener(v -> {
      onClickAddBatch();
    });

    fabCopyBatch.setImageResource(R.drawable.ic_copy);
    fabCopyBatch.setColorNormal(ContextCompat.getColor(getContext(), R.color.copy_batch_button_color));
    fabCopyBatch.setColorPressed(ContextCompat.getColor(getContext(), R.color.copy_batch_button_color));
    fabCopyBatch.setOnClickListener(v -> {
      onClickCopyBatch();
    });
    //fabCopyBatch.setLabelColors(R.color.white, R.color.white, R.color.white);
    //fabCopyBatch.setLabelTextColor(R.color.white);

    fab.setClosedOnTouchOutside(true);
    fab.setIconAnimated(false);

    ObjectAnimator
        scaleOutX = ObjectAnimator.ofFloat(fab.getMenuIconView(), "scaleX", 1f, 0.6f);
    scaleOutX.setDuration(50);
    ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(fab.getMenuIconView(), "scaleY", 1f, 0.6f);
    scaleOutY.setDuration(50);

    ObjectAnimator scaleInX = ObjectAnimator.ofFloat(fab.getMenuIconView(), "scaleX", 0.6f, 1f);
    scaleInX.setDuration(50);
    ObjectAnimator scaleInY = ObjectAnimator.ofFloat(fab.getMenuIconView(), "scaleY", 0.6f, 1f);
    scaleInY.setDuration(50);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(scaleOutX).with(scaleOutY);
    animatorSet.play(scaleInX).with(scaleInY).after(scaleOutX);

    fab.setOnMenuButtonClickListener(v -> {
      animatorSet.start();
      fab.toggle(true);
    });
  }

  public void onClickAddBatch(){

  }

  public void onClickCopyBatch(){

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
