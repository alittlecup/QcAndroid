package cn.qingchengfit.saascommon.views.commonuserselect;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.databinding.CmViewUserSelectBinding;
import cn.qingchengfit.saascommon.item.CommonUserItem;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

public class CommonUserSelectView extends SaasCommonFragment
    implements FlexibleAdapter.OnItemClickListener {
  protected CmViewUserSelectBinding binding;
  protected List<IFlexible> datas = new ArrayList<>();
  protected CommonFlexAdapter<CommonUserItem> adapter = new CommonFlexAdapter(datas, this);
  protected BottomViewSelectUser bottomViewSelectUser = new BottomViewSelectUser();

  public MutableLiveData<List<CommonUserItem>> selectItems = new MutableLiveData<>();
  public MutableLiveData<Boolean> selectedAll=new MutableLiveData<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = CmViewUserSelectBinding.inflate(inflater);
    binding.setLifecycleOwner(this);
    binding.setPage(this);

    initRv();
    initListener();

    selectItems.setValue(new ArrayList<>());
    return binding.getRoot();
  }

  private void initListener() {
    binding.llShowSelect.setOnClickListener(view -> {
      bottomViewSelectUser.show(getChildFragmentManager(), "");
    });
  }


  public void selectAll() {
    adapter.selectAll();
    selectItems.setValue(adapter.getMainItems());
    adapter.notifyDataSetChanged();
  }

  public void clearSelect() {
    adapter.clearSelection();
    selectItems.setValue(new ArrayList<>());
    adapter.notifyDataSetChanged();
  }

  private void initRv() {
    binding.rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    binding.rv.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withBottomEdge(true).withOffset(1));
    adapter.setMode(FlexibleAdapter.Mode.MULTI);
    binding.rv.setAdapter(adapter);
  }

  public void filter(String filter) {
    if (adapter == null) return;
    adapter.setSearchText(filter);
    adapter.filterItems();
  }

  public List<ICommonUser> getSelectUser() {
    List<ICommonUser> ret = new ArrayList<>();
    for (CommonUserItem item : selectItems.getValue()) {
      ret.add(item.getUser());
    }
    return ret;
  }

  public void setDatas(List<? extends CommonUserItem> d) {
    this.datas.clear();
    this.datas.addAll(d);
    this.adapter.updateDataSet(this.datas);
  }

  public void setSelectedItem(List<CommonUserItem> items) {
    adapter.clearSelection();
    for (CommonUserItem item : items) {
      adapter.addSelection(adapter.index(item));
    }
    selectItems.setValue(items);
    adapter.notifyDataSetChanged();
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item == null) return true;
    if (item instanceof CommonUserItem) {
      List<CommonUserItem> si = selectItems.getValue();
      if (si.contains((CommonUserItem) item)) {
        ((CommonUserItem) item).setSelected(false);
        si.remove(item);
      } else {
        si.add((CommonUserItem) item);
        ((CommonUserItem) item).setSelected(true);
      }
      selectItems.setValue(si);
    }
    adapter.toggleSelection(position);
    adapter.notifyDataSetChanged();
    return true;
  }

  public void setBtnLeftListener(View.OnClickListener listener) {
    if (binding.btnLeft != null) {
      binding.btnLeft.setOnClickListener(listener);
    }
  }

  public void setBtnRightListener(View.OnClickListener listener) {
    if (binding.btnRight != null) {
      binding.btnRight.setOnClickListener(listener);
    }
  }
}
