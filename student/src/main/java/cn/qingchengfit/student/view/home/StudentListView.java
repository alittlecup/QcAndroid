package cn.qingchengfit.student.view.home;

import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saascommon.widget.ModifiedFastScroller;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StViewStudentAllotBinding;
import cn.qingchengfit.student.view.allot.AllotChooseCoachPageParams;
import cn.qingchengfit.student.view.allot.AllotChooseSalerPageParams;
import cn.qingchengfit.student.view.allot.AllotSaleShowSelectDialogView;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import io.reactivex.annotations.NonNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

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
  }

  public void selectAll(boolean selectedAll) {
    if (selectedAll) {
      adapter.selectAll(new Integer[0]);
    } else {
      adapter.clearSelection();
    }
    adapter.notifyDataSetChanged();
    mViewModel.mBottomSelectedCount.set(adapter.getSelectedItemCount());
  }

  public List<QcStudentBean> getSelectDataBeans() {
    List<QcStudentBean> studenBeans = new ArrayList<>();
    for (Integer pos : adapter.getSelectedPositions()) {
      studenBeans.add(((StudentItem) adapter.getItem(pos)).getQcStudentBean());
    }
    return studenBeans;
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

  public void setItems(List<? extends StudentItem> items) {
    if (mViewModel != null) {
      mViewModel.items.set(new ArrayList<>(items));
    }
  }

  public StudentListViewModel getViewModel() {
    return mViewModel;
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    adapter.setMode(SelectableAdapter.Mode.MULTI);
    mBinding.recyclerView.setAdapter(adapter);
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.fastScroller.setBarClickListener(new ModifiedFastScroller.onBarClickListener() {
      @Override public int letterToPosition(String letter) {
        List<? extends StudentItem> itemList = mViewModel.items.get();
        int position = 0;
        for (int i = 0; i < itemList.size(); i++) {
          if (itemList.get(i).getHeader() != null) {
            if (itemList.get(i).getHeader() instanceof StickerDateItem) {
              if (((StickerDateItem) itemList.get(i).getHeader()).getDate()
                  .equalsIgnoreCase(letter)) {
                position = i;
              }
            }
          }
        }
        return position;
      }
    });
    adapter.setFastScroller(mBinding.fastScroller);
    adapter.addListener(this);
  }

  public void showFastScroller() {
    mBinding.fastScroller.setEnabled(true);
    mBinding.fastScroller.setVisibility(View.VISIBLE);
  }

  public void hideFastScroller() {
    mBinding.fastScroller.setEnabled(false);
    mBinding.fastScroller.setVisibility(View.GONE);
  }

  @Override public boolean onItemClick(int position) {
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
        routeTo("allot/choosesaler", new AllotChooseCoachPageParams().title(title)
            .studentIds(ids)
            .textContent(
                getString(R.string.choose_saler) + "\n" + getString(
                    R.string.choose_saler_tips))
            .build());
        break;
      case StudentListView.TRAINER_TYPE:
        routeTo("/allot/choosecoach", new AllotChooseSalerPageParams().title(title)
            .studentIds(ids)
            .textContent(getString(R.string.choose_coach))
            .build());
        break;
    }
    selectAll(false);
  }

  public ArrayList<String> getSelectIds() {
    ArrayList<String> ids = new ArrayList<>();
    for (Integer pos : adapter.getSelectedPositions()) {
      ids.add(((StudentItem) adapter.getItem(pos)).getId());
    }
    return ids;
  }

  @StringDef(value = { SELLER_TYPE, TRAINER_TYPE, MSG_TYPE }) @Retention(RetentionPolicy.RUNTIME)
  public @interface AllotType {
  }
}
