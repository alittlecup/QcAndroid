package cn.qingchengfit.staffkit.views.signin.zq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/9/14.
 */

public class ZqAccessFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.tv_zq_introduce) TextView tvZqIntroduce;
  @BindView(R.id.tv_zq_buy) TextView tvZqBuy;
  @BindView(R.id.recycler_zq_access) RecyclerView recyclerZqAccess;
  private CommonFlexAdapter adapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();
  private BottomListFragment bottomListFragment;
  private List<String> dataList = new ArrayList<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_zq_access, container, false);
    unbinder = ButterKnife.bind(this, view);
    bottomListFragment = BottomListFragment.newInstance("");
    setToolbar();
    return view;
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

  private void initBottomData(){
    dataList.add("");
  }

  private void initBottomList(){
    List<AbstractFlexibleItem> bottomItems = new ArrayList<>();


  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    return false;
  }
}
