package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.animator.FadeInUpItemAnimator;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.batch.bean.BatchSchedule;
import cn.qingchengfit.saasbase.course.batch.items.BatchScheduleItem;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchScheduleListPresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
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
 * Created by Paper on 2017/9/22.
 */
@Leaf(module = "course", path = "/batch/schedule/list/")
public class BatchScheduleListFragment extends SaasBaseFragment implements
    FlexibleAdapter.OnItemClickListener,BatchScheduleListPresenter.MVPView{

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.rv) RecyclerView rv;
  @BindView(R2.id.btn_del_selected) Button btnDelSelected;

  @Inject BatchScheduleListPresenter presenter;
  CommonFlexAdapter adapter ;
  @Need String batchId;
  @Need Boolean isPrivate = false;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter.setBatchId(batchId);
    presenter.setPrivate(isPrivate);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_saas_batch_schedule_list, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    adapter = new CommonFlexAdapter(new ArrayList(), this);
    rv.setItemAnimator(new FadeInUpItemAnimator());
    rv.setLayoutManager(new LinearLayoutManager(getContext()));
    rv.addItemDecoration(new FlexibleItemDecoration(getContext())
      .withOffset(1).withBottomEdge(true)
    );
    rv.setAdapter(adapter);
    presenter.queryList();
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();

  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(R.string.t_batch_schedule_list);
    editable = true;
    toggleAction();
  }

  boolean editable = true;//是否item可以选中
  public void toggleAction(){
    editable = !editable;
    if (!editable) {
      toolbar.getMenu().clear();
      toolbar.getMenu().add("编辑").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          adapter.setStatus(1);
          adapter.notifyDataSetChanged();
          toggleAction();
          return false;
        }
      });
    }else {
      toolbar.getMenu().clear();
      toolbar.getMenu().add("取消").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          adapter.setStatus(0);
          adapter.notifyDataSetChanged();
          toggleAction();
          return false;
        }
      });
    }
  }


  @Override public String getFragmentName() {
    return BatchScheduleListFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item == null) return true;
    if (editable){
      adapter.toggleSelection(position);
      adapter.notifyItemChanged(position);
    }else {
      if (item instanceof BatchScheduleItem) {
        BatchSchedule batchLoop = ((BatchScheduleItem) item).getBatchSchedule();
        if (batchLoop == null) return true;
        if (!DateUtils.isOutOfDate(DateUtils.formatDateFromServer(batchLoop.start))) {
          //跳去单个排课页面
          routeTo("/batch/schedule/single/",
            new cn.qingchengfit.saasbase.course.batch.views.BatchSingleParams()
              .scheduleId(presenter.getBatchId())
              .isPrivate(isPrivate)
              .build());
        }
      }
    }
    return true;
  }

  @Override public void onList(List<BatchSchedule> list) {
    try {
      List<IFlexible> datas = new ArrayList<>();
      String curDay = "";
      for (BatchSchedule batchSchedule : list) {
        if (!curDay.equalsIgnoreCase(DateUtils.getYYMMfromServer(batchSchedule.start))){
          curDay = DateUtils.getYYMMfromServer(batchSchedule.start);
          datas.add(new StickerDateItem(curDay+"课程"));
        }
        datas.add(new BatchScheduleItem(batchSchedule,presenter.isPrivate()));
      }
      adapter.updateDataSet(datas ,true);
    }catch (Exception e){
      LogUtil.e(e.getMessage());
    }

  }

  @Override public void onSuccess() {

  }
}
