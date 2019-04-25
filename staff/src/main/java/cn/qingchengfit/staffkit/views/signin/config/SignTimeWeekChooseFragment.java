package cn.qingchengfit.staffkit.views.signin.config;

import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.Constants;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CmBottomListChosenItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.FragmentSigntimeWeekBinding;
import cn.qingchengfit.staffkit.views.signin.bean.SignChooseWeekEvent;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangbaole
 */
public class SignTimeWeekChooseFragment extends SaasCommonFragment
    implements FlexibleAdapter.OnItemClickListener {
  FragmentSigntimeWeekBinding mBinding;
  CommonFlexAdapter adapter;
  private MutableLiveData<List<Integer>> weeks = new MutableLiveData<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = FragmentSigntimeWeekBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    weeks.observe(this, this::upDateWeeks);
    return mBinding.getRoot();
  }

  private void initRecyclerView() {
    List<CmBottomListChosenItem> datas = new ArrayList<>();
    for (String week : Constants.WEEKS) {
      datas.add(new CmBottomListChosenItem(week, null));
    }
    adapter = new CommonFlexAdapter(datas, this);
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter);
  }

  private void upDateWeeks(List<Integer> week) {
    if (week != null && !week.isEmpty()) {
      for (Integer index : week) {
        adapter.addSelection(index);
      }
      adapter.notifyDataSetChanged();
    }else{
      adapter.selectAll(R.layout.item_cm_bottom_list_choose);
    }
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("周期");
    toolbarModel.setMenu(R.menu.menu_comfirm);
    toolbarModel.setListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem menuItem) {
        sendBackWeeks();
        return false;
      }
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void sendBackWeeks() {
    List<Integer> selectedPositions = adapter.getSelectedPositions();
    if (selectedPositions.isEmpty()) {
      ToastUtils.show("请选择星期");
      return;
    }
    RxBus.getBus().post(new SignChooseWeekEvent(selectedPositions));
    getActivity().onBackPressed();
  }

  @Override public boolean onItemClick(int position) {
    adapter.toggleSelection(position);
    adapter.notifyItemChanged(position);
    return false;
  }

  public void setChoosedWeek(List<Integer> weeks) {
    this.weeks.setValue(weeks);
  }
}
