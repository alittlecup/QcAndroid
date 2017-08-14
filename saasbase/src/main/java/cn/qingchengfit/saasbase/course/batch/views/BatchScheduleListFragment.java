package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.course.batch.bean.BatchLoop;
import cn.qingchengfit.saasbase.course.batch.bean.BatchSchedule;
import cn.qingchengfit.saasbase.course.batch.items.BatchScheduleItem;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchScheduleListPresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
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
public class BatchScheduleListFragment extends BaseFragment implements
    FlexibleAdapter.OnItemClickListener,BatchScheduleListPresenter.MVPView{

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.rv) RecyclerView rv;
  @BindView(R2.id.btn_del_selected) Button btnDelSelected;

  @Inject BatchScheduleListPresenter presenter;
  CommonFlexAdapter adapter ;


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_saas_batch_schedule_list, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    adapter = new CommonFlexAdapter(new ArrayList(), this);
    rv.setLayoutManager(new LinearLayoutManager(getContext()));
    rv.setAdapter(adapter);
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.queryList();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(R.string.t_batch_schedule_list);
    toggleAction();
  }

  boolean editable = true;
  public void toggleAction(){
    editable = !editable;
    if (editable) {
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
    if (adapter.getItem(position) instanceof BatchLoop) {
      BatchLoop batchLoop = (BatchLoop) adapter.getItem(position);
      if (!DateUtils.isOutOfDate(batchLoop.dateStart)){
        //跳去单个排课页面
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
      adapter.updateDataSet(datas);
    }catch (Exception e){
      LogUtil.e(e.getMessage());
    }

  }

  @Override public void onSuccess() {

  }
}
