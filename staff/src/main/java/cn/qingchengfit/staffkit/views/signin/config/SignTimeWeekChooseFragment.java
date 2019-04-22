package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.Constants;
import cn.qingchengfit.items.CmBottomListChosenItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.FragmentSigntimeWeekBinding;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

public class SignTimeWeekChooseFragment extends SaasCommonFragment implements FlexibleAdapter.OnItemClickListener {
  FragmentSigntimeWeekBinding mBinding;
  CommonFlexAdapter adapter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = FragmentSigntimeWeekBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    return mBinding.getRoot();
  }

  private void initRecyclerView() {
    List<CmBottomListChosenItem> datas = new ArrayList<>();
    for (String week : Constants.WEEKS) {
      datas.add(new CmBottomListChosenItem(week, null));
    }
    adapter = new CommonFlexAdapter(datas,this);
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter);
    adapter.selectAll(R.layout.item_cm_bottom_list_choose);
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("周期");
    toolbarModel.setMenu(R.menu.menu_comfirm);
    toolbarModel.setListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem menuItem) {

        return false;
      }
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }
  @Override public boolean onItemClick(int position) {
    adapter.toggleSelection(position);
    adapter.notifyItemChanged(position);
    return false;
  }
}
