package cn.qingchengfit.saasbase.mvvm_student.view.home;

import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.databinding.ViewStudentAllotBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.view.allot.AllotSaleShowSelectDialogView;
import cn.qingchengfit.saasbase.student.widget.ModifiedFastScroller;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class StudentListView
    extends StudentBaseFragment<ViewStudentAllotBinding, StudentListViewModel>implements
    FlexibleAdapter. {
  public static final String SELLER_TYPE = "seller";
  public static final String TRAINER_TYPE = "trainer";
  public static final String MSG_TYPE = "msg";
  @Need String curType;
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

  }

  @Override
  public ViewStudentAllotBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = ViewStudentAllotBinding.inflate(inflater, container, false);
    initRecyclerView();
    initBottomButton();
    initListener();
    mViewModel.getLiveItems();
    return mBinding;
  }

  private void initListener() {
    mBinding.llShowSelect.setOnClickListener(view -> {
      //TODO 保存选中的条目
      //mViewModel.getSelectedDatas().setValue(getSelectDataBeans());
      AllotSaleShowSelectDialogView f = new AllotSaleShowSelectDialogView();
      f.setTargetFragment(this, 0);
      f.show(getFragmentManager(), "");
    });
  }

  private void initBottomButton() {
    if (curType.equals(SELLER_TYPE)) {
      mBinding.btnModifySale.setText("分配销售");
    } else if (curType.equals(TRAINER_TYPE)) {
      mBinding.btnModifySale.setText("分配教练");
    } else if (curType.equals(MSG_TYPE)) {
      mBinding.btnModifySale.setText("群发短信");
    }
  }
  public StudentListViewModel getViewModel(){
    return mViewModel;
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    adapter.setMode(SelectableAdapter.Mode.MULTI);
    mBinding.recyclerView.setAdapter(adapter);
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.fastScroller.setBarClickListener(new ModifiedFastScroller.onBarClickListener() {
      @Override public int letterToPosition(String letter) {
        // TODO: 2018/6/19  点击回调
        return 0;
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

  @StringDef(value = { SELLER_TYPE, TRAINER_TYPE, MSG_TYPE }) @Retention(RetentionPolicy.RUNTIME)
  @interface AllotType {
  }
}
