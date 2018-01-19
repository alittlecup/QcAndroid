package cn.qingchengfit.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.items.ChooseItem;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.testmodule.R;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TwoScrollPicker;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/10/25.
 */

public class ItemActivity extends BaseActivity implements FlexibleAdapter.OnItemClickListener {

  @BindView(R.id.recycler_item) RecyclerView recyclerItem;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.picker_single) Button pickerSingle;
  @BindView(R.id.picker_two) Button pickerTwo;
  @BindView(R.id.list_bottom) Button listBottom;
  private CommonFlexAdapter adapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item);
    ButterKnife.bind(this);
    setToolbar();
    initView();
  }

  @OnClick(R.id.picker_single)
  public void onShowSingle(){
    SimpleScrollPicker picker = new SimpleScrollPicker(ItemActivity.this);
    ArrayList<String> dataList = new ArrayList<>();
    dataList.add("test 1");
    dataList.add("test 2");
    dataList.add("test 3");
    picker.show(dataList, 0);
  }

  @OnClick(R.id.picker_two)
  public void onShowTwo(){
    TwoScrollPicker picker = new TwoScrollPicker(ItemActivity.this);
    ArrayList<String> dataList = new ArrayList<>();
    ArrayList<String> rightDataList = new ArrayList<>();
    dataList.add("test 1");
    dataList.add("test 2");
    dataList.add("test 3");
    rightDataList.add("II 1");
    rightDataList.add("II 2");
    rightDataList.add("II 3");
    picker.show(dataList, rightDataList, 0, 0);
  }

  @OnClick(R.id.list_bottom)
  public void onBottomList(){
    BottomListFragment bottomListFragment = BottomListFragment.newInstance("");
    bottomListFragment.loadData(itemList);
    bottomListFragment.show(getSupportFragmentManager(), null);
  }

  private void setToolbar() {
    toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        ToastUtils.show(
            "选择了" + adapter.getSelectedItemCount() + "个; --> " + adapter.getSelectedPositions());
        return false;
      }
    });
  }

  private void initView() {
    List<String> dataList = new ArrayList<>();
    dataList.add("test 1");
    dataList.add("test 2");
    dataList.add("test 3");
    dataList.add("test 4");
    for (String str : dataList) {
      itemList.add(new ChooseItem(str));
    }
    dataList.clear();
    dataList.add("test 5");
    itemList.add(new FilterCommonLinearItem(dataList.get(0)));
    adapter = new CommonFlexAdapter(itemList, this);
    recyclerItem.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    recyclerItem.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
    recyclerItem.setAdapter(adapter);
  }

  @Override public boolean onItemClick(int position) {
    if (adapter.isSelected(position)) {
      adapter.removeSelection(position);
    } else {
      adapter.addSelection(position);
    }
    adapter.notifyItemChanged(position);
    return false;
  }
}
