package cn.qingchengfit.staffkit.views.signin.zq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.course.SimpleTextItemItem;
import cn.qingchengfit.staffkit.views.signin.zq.model.BottomModel;
import cn.qingchengfit.staffkit.views.signin.zq.model.Guard;
import cn.qingchengfit.staffkit.views.signin.zq.presenter.ZqAccessPresenter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/9/14.
 */

public class ZqAccessFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener,
    ZqAccessPresenter.MVPView, BottomListFragment.ComfirmChooseListener {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.tv_zq_introduce) TextView tvZqIntroduce;
  @BindView(R.id.tv_zq_buy) TextView tvZqBuy;
  @BindView(R.id.recycler_zq_access) RecyclerView recyclerZqAccess;

  @Inject ZqAccessPresenter presenter;

  private CommonFlexAdapter adapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();
  private BottomListFragment bottomListFragment;
  private List<BottomModel> bottomList = new ArrayList<>();
  private BottomModel bottomModel;
  private Guard guard;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_zq_access, container, false);
    unbinder = ButterKnife.bind(this, view);
    bottomListFragment = BottomListFragment.newInstance("");
    setToolbar();
    initData();
    return view;
  }

  private void initData(){
    presenter.getAccess();
    adapter = new CommonFlexAdapter(itemList, this);
    recyclerZqAccess.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerZqAccess.setAdapter(adapter);
  }

  private void setToolbar(){
    initToolbar(toolbar);
    toolbarTitle.setText("智奇门禁");
    toolbar.inflateMenu(R.menu.menu_add);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        return false;
      }
    });
  }

  //bottomList 用于区分不同情况下的bottom弹出菜单
  private void initBottomList(int status){
    List<AbstractFlexibleItem> bottomItems = new ArrayList<>();
    if (presenter.getBottomList(getContext(), status).size() > 0) {
      bottomList.clear();
      bottomList.addAll(presenter.getBottomList(getContext(), status));
    }
    for (BottomModel content : bottomList){
      bottomItems.add(new SimpleTextItemItem(content.name, Gravity.CENTER));
    }
    BottomListFragment bottomListFragment = BottomListFragment.newInstance("");
    bottomListFragment.setListener(this);
    bottomListFragment.loadData(bottomItems);
    bottomListFragment.show(getFragmentManager(), null);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    if (adapter.getItem(position) instanceof ItemZqAccess) {
      guard = ((ItemZqAccess)adapter.getItem(position)).getData();
      initBottomList(((ItemZqAccess)adapter.getItem(position)).getData().status);
    }
    return false;
  }

  @Override public void onGetAccess(List<Guard> guardList) {
    if(guardList != null && guardList.size() > 0){
      itemList.clear();
    }
    for (Guard guard : guardList){
      itemList.add(new ItemZqAccess(guard));
    }
    adapter.notifyDataSetChanged();
  }

  @Override public void changeStatusOk() {

  }

  @Override public void onDeleteOk() {

  }

  @Override public void onAddOk() {

  }

  @Override public void onComfirmClick(List<IFlexible> dats) {
    //TODO 等待合代码之后回调接口包含选择位置
  }

  //1、2、3改变状态 4 删除门禁  0编辑门禁  5取消
  private void changeAccessStatus(int status){
    switch (status){
      case 1:
      case 2:
      case 3:
        presenter.changeZqStatus(guard.id, status);
        break;
      case 0:
        break;
      case 4:
        presenter.deleteZqAccess(guard.id);
        break;
      case 5:
        break;
    }
  }

}
