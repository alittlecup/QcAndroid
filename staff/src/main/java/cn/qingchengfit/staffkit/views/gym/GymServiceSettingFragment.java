package cn.qingchengfit.staffkit.views.gym;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.FragmentGymServiceSettingBinding;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.gym.items.GymServiceSettingItem;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "staff", path = "/gym/setting") public class GymServiceSettingFragment
    extends SaasCommonFragment {
  FragmentGymServiceSettingBinding mBinding;
  CommonFlexAdapter adapter;
  @Need ArrayList<Integer> types;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = FragmentGymServiceSettingBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    SaasbaseParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar();
    initRecyclerView();
    if(types!=null&&!types.isEmpty()){
      List<GymServiceSettingItem> items=new ArrayList<>();
      for (int type:types){
        items.add(new GymServiceSettingItem(type));
      }
      adapter.updateDataSet(items);
    }
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("服务设置");
    toolbarModel.setMenu(R.menu.menu_skip);
    toolbarModel.setListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {

        return false;
      }
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
  }
}
