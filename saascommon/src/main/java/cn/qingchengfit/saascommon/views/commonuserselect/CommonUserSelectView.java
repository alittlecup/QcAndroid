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

public class CommonUserSelectView extends BaseFragment implements
  FlexibleAdapter.OnItemClickListener{
  protected CmViewUserSelectBinding binding;
  protected List<IFlexible> datas = new ArrayList<>();
  protected CommonFlexAdapter<CommonUserItem> adapter = new CommonFlexAdapter(datas,this);
  protected BottomViewSelectUser bottomViewSelectUser = new BottomViewSelectUser();

  public MutableLiveData<List<CommonUserItem>> selectItems = new MutableLiveData<>();


  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    binding = CmViewUserSelectBinding.inflate(inflater);
    binding.setLifecycleOwner(this);
    binding.setPage(this);

    initRv();
    initListener();

    selectItems.setValue(new ArrayList<>());
    selectItems.observe(this, items -> {
      adapter.clearSelection();
      for (CommonUserItem item : items) {
        adapter.addSelection(adapter.index(item));
      }
      adapter.notifyDataSetChanged();
    });

    return binding.getRoot();
  }



  private void initListener() {
    binding.llShowSelect.setOnClickListener(view -> {
      bottomViewSelectUser.show(getChildFragmentManager(),"");
    });
  }

  public void selectAll(){
    adapter.selectAll();
    adapter.notifyDataSetChanged();
  }
  public void clearSelect(){
    adapter.clearSelection();
    adapter.notifyDataSetChanged();
  }


  private void initRv() {
    binding.rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    binding.rv.addItemDecoration(new FlexibleItemDecoration(getContext())
      .withBottomEdge(true).withOffset(1)
    );
    adapter.setMode(FlexibleAdapter.Mode.MULTI);
    binding.rv.setAdapter(adapter);
  }

  public void filter(String filter){
    adapter.setSearchText(filter);
    adapter.filterItems();
  }

  public List<ICommonUser> getSelectUser(){
    List<ICommonUser> ret = new ArrayList<>();
    for (Integer integer : adapter.getSelectedPositions()) {
      ret.add(((CommonUserItem)adapter.getItem(integer)).getUser());
    }
    return ret;
  }

  public void setDatas(List<? extends IFlexible> d){
    this.datas.clear();
    this.datas.addAll(d);
    this.adapter.updateDataSet(this.datas);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item == null ) return true;
    if (item instanceof CommonUserItem){
    List<CommonUserItem> si = selectItems.getValue();
    if (si.contains((CommonUserItem)item))
      si.remove(item);
    else si.add((CommonUserItem) item);
    selectItems.setValue(si);
    }
    return true;
  }

  public void setBtnLeftListener(View.OnClickListener listener){
    if (binding.btnLeft != null){
      binding.btnLeft.setOnClickListener(listener);
    }
  }

  public void setBtnRightListener(View.OnClickListener listener){
    if (binding.btnRight != null){
      binding.btnRight.setOnClickListener(listener);
    }
  }
}
