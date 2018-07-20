package cn.qingchengfit.student.view.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringDef;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.item.IItemData;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saascommon.widget.ModifiedFastScroller;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StViewStudentAllotBinding;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.item.ChooseStaffItem;
import cn.qingchengfit.student.item.FollowUpItem;
import cn.qingchengfit.student.item.StaffDetailItem;
import cn.qingchengfit.student.view.allot.AllotChooseCoachPageParams;
import cn.qingchengfit.student.view.allot.AllotChooseSellerPageParams;
import cn.qingchengfit.student.view.allot.AllotSaleShowSelectDialogView;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.statuslayout.StatusLayoutManager;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.amap.api.services.poisearch.PoiSearch;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.annotations.NonNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentListView
    extends StudentBaseFragment<StViewStudentAllotBinding, StudentListViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  public static final String SELLER_TYPE = "seller";
  public static final String TRAINER_TYPE = "trainer";
  public static final String MSG_TYPE = "msg";
  CommonFlexAdapter adapter;
  private List<String> ids;
  String curType;

  @Override protected void subscribeUI() {
    mViewModel.getRemoveSelectPos().observe(this, pos -> {
      Integer integer = adapter.getSelectedPositions().get(pos);
      onItemClick(integer);
    });
    mViewModel.getSelectedDatas().observe(this, items -> {
      if (items == null || items.isEmpty()) {
        adapter.clearSelection();
      }
    });
  }

  public static StudentListView newInstanceWithType(@AllotType String type) {
    StudentListView listView = new StudentListView();
    Bundle bundle = new Bundle();
    bundle.putString("type", type);
    listView.setArguments(bundle);
    return listView;
  }

  @Override
  public StViewStudentAllotBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StViewStudentAllotBinding.inflate(inflater, container, false);
    mBinding.setLifecycleOwner(this);
    mBinding.setViewModel(mViewModel);
    initRecyclerView();
    if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("type"))) {
      curType = getArguments().getString("type");
      mBinding.btnModifySale.setText(getStringByType(curType));
    } else {
      mBinding.llBottom.setVisibility(View.GONE);
    }
    initListener();
    return mBinding;
  }

  private void initListener() {
    mBinding.llShowSelect.setOnClickListener(view -> {
      mViewModel.getSelectedDatas().setValue(getSelectDataBeans());
      AllotSaleShowSelectDialogView f = new AllotSaleShowSelectDialogView();
      f.setTargetFragment(this, 0);
      f.show(getFragmentManager(), "");
    });
    mBinding.btnModifySale.setOnClickListener(view -> {
      routeTo(getStringByType(curType));
    });
    mBinding.btnExclude.setOnClickListener(view -> {

    });
  }

  public void selectAll(boolean selectedAll) {
    if (adapter.isEmpty() || adapter.getMainItems().get(0) instanceof CommonNoDataItem) {
      return;
    }
    if (selectedAll) {
      adapter.selectAll();
    } else {
      adapter.clearSelection();
    }
    adapter.notifyDataSetChanged();
    mViewModel.mBottomSelectedCount.set(adapter.getSelectedItemCount());
  }

  public List<QcStudentBean> getSelectDataBeans() {
    List<QcStudentBean> studentBeans = new ArrayList<>();
    for (Integer pos : adapter.getSelectedPositions()) {
      if (adapter.getItem(pos) instanceof StudentItem) {
        studentBeans.add(((StudentItem) adapter.getItem(pos)).getQcStudentBean());
      } else if (adapter.getItem(pos) instanceof ChooseDetailItem) {
        studentBeans.add(((ChooseDetailItem) adapter.getItem(pos)).getData());
      }
    }
    return studentBeans;
  }

  public static String getStringByType(@NonNull String type) {
    if (type.equals(SELLER_TYPE)) {
      return "分配销售";
    } else if (type.equals(TRAINER_TYPE)) {
      return "分配教练";
    } else if (type.equals(MSG_TYPE)) {
      return "群发短信";
    }
    return "";
  }

  private Map<String, Object> adapterTags = new HashMap<>();

  public void setAdapterTag(String key, Object value) {
    if (adapter != null) {
      adapter.setTag(key, value);
    } else {
      adapterTags.put(key, value);
    }
  }

  public void setItems(List<? extends AbstractFlexibleItem> items) {
    if (items == null || items.isEmpty()) {
      showEmptyLayout(R.drawable.vd_img_empty_universe, "暂无数据", "");
    } else {
      restoreLayout();
      if (mViewModel != null) {
        mViewModel.items.set(new ArrayList<>(items));
      }
    }
  }

  public StudentListViewModel getViewModel() {
    return mViewModel;
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    adapter.setMode(SelectableAdapter.Mode.MULTI);
    mBinding.recyclerView.setAdapter(adapter);
    mBinding.recyclerView.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.fastScrollerBar.setBarClickListener(letter -> {
      List<? extends AbstractFlexibleItem> itemList = mViewModel.items.get();
      int position = 0;
      for (int i = 0; i < itemList.size(); i++) {
        if (itemList.get(i) instanceof StudentItem) {
          StudentItem item = (StudentItem) itemList.get(i);
          if (item.getHeader() != null) {
            if (item.getHeader() instanceof StickerDateItem) {
              if (((StickerDateItem) item.getHeader()).getDate().equalsIgnoreCase(letter)) {
                position = i;
              }
            }
          }
        }
      }
      return position;
    });
    adapter.setFastScroller(mBinding.fastScrollerBar);
    adapter.addListener(this);
    if (!adapterTags.isEmpty()) {
      for (String key : adapterTags.keySet()) {
        adapter.setTag(key, adapterTags.get(key));
      }
    }
    setRefreshEnable(false);
  }

  public void filter(String filter) {
    adapter.setSearchText(filter);
    adapter.filterItems();
  }

  public void showFastScroller() {
    mBinding.fastScrollerBar.setEnabled(true);
    mBinding.fastScrollerBar.setVisibility(View.VISIBLE);
  }

  public void setRefreshEnable(boolean enable) {
    mBinding.refresh.setEnabled(enable);
  }

  public void hideFastScroller() {
    mBinding.fastScrollerBar.setEnabled(false);
    mBinding.fastScrollerBar.setVisibility(View.GONE);
  }

  public void setCurType(@AllotType String type) {
    this.curType = type;
    if (mBinding != null) {
      mBinding.btnModifySale.setText(getStringByType(curType));
      if (type.equals(TRAINER_TYPE)) {
        adapter.setTag("choose", 1);
      } else if (type.equals(SELLER_TYPE)) {
        adapter.setTag("choose", 0);
      } else {
        adapter.setTag("choose", -2);
      }
      adapter.setTag("selected", true);
      adapter.notifyDataSetChanged();
    }
  }

  public void reset() {
    this.curType = "";
    if (mBinding != null) {
      mBinding.btnModifySale.setText(getStringByType(curType));
      mBinding.llBottom.setVisibility(View.GONE);
      adapter.setTag("choose", -1);
      adapter.setTag("selected", false);
      adapter.clearSelection();
      adapter.notifyDataSetChanged();
    }
  }

  @Override public boolean onItemClick(int position) {
    Object choose = adapter.getTag("choose");
    if (choose == null || (int) choose == -1) {
      QcStudentBean qcStudentBean = null;
      IFlexible item = adapter.getItem(position);
      if (item instanceof IItemData) {
        qcStudentBean = ((IItemData) item).getData();
      } else if (item instanceof FollowUpItem) {
        qcStudentBean = ((FollowUpItem) item).getData();
      }
      if (qcStudentBean == null) return false;
      QcRouteUtil.setRouteOptions(new RouteOptions("staff").setActionName("/home/student")
          .addParam("qcstudent", qcStudentBean)).call();
      return false;
    }
    if (TextUtils.isEmpty(mBinding.btnModifySale.getText())) {
      return false;
    }
    adapter.toggleSelection(position);
    adapter.notifyDataSetChanged();
    mViewModel.mBottomSelectedCount.set(adapter.getSelectedItemCount());
    ids = getSelectIds();
    return false;
  }

  private void routeTo(String title) {
    ArrayList<String> ids = getSelectIds();
    switch (curType) {
      case StudentListView.SELLER_TYPE:
        routeTo("allot/chooseseller", new AllotChooseCoachPageParams().title(title)
            .studentIds(ids)
            .curId(curID)
            .textContent(
                getString(R.string.choose_saler) + "\n" + getString(R.string.choose_saler_tips))
            .build());
        break;
      case StudentListView.TRAINER_TYPE:
        routeTo("/allot/choosecoach", new AllotChooseSellerPageParams().title(title)
            .studentIds(ids)
            .curId(curID)
            .textContent(getString(R.string.choose_coach))
            .build());
        break;
    }
    selectAll(false);
  }

  public ArrayList<String> getSelectIds() {
    ArrayList<String> ids = new ArrayList<>();
    for (Integer pos : adapter.getSelectedPositions()) {
      if ((adapter.getItem(pos) instanceof StudentItem)) {
        ids.add(((StudentItem) adapter.getItem(pos)).getId());
      } else if (adapter.getItem(pos) instanceof ChooseDetailItem) {
        ids.add(((ChooseDetailItem) adapter.getItem(pos)).getData().getId());
      }
    }
    return ids;
  }

  private String curID;

  public void setCurId(String curID) {
    this.curID = curID;
    if (mBinding != null) {
      mBinding.btnExclude.setVisibility(TextUtils.isEmpty(curID) ? View.GONE : View.VISIBLE);
    }
  }

  @StringDef(value = { SELLER_TYPE, TRAINER_TYPE, MSG_TYPE }) @Retention(RetentionPolicy.RUNTIME)
  public @interface AllotType {
  }

  public void showEmptyLayout(@DrawableRes int drawable, String title, String content) {
    //layoutManager =
    //    new StatusLayoutManager.Builder(mBinding.container).setEmptyLayoutDrawableRes(drawable)
    //        .setEmptyLayoutTitle(title)
    //        .setEmptyLayoutContent(content)
    //        .build();
    //layoutManager.showEmptyLayout();
    CommonNoDataItem commonNoDataItem = new CommonNoDataItem(drawable, content, "");
    List<AbstractFlexibleItem> items = new ArrayList<>();
    items.add(commonNoDataItem);
    adapter.updateDataSet(items);
  }

  public void restoreLayout() {
    //if (layoutManager != null) layoutManager.restoreLayout();
  }
}
