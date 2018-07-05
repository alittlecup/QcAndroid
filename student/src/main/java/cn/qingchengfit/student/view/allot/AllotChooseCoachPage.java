package cn.qingchengfit.student.view.allot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.databinding.PageAllotChooseSalerBinding;
import cn.qingchengfit.student.item.BaseGirdListFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/12/1.
 */

@Leaf(module = "student", path = "/allot/choosecoach") public class AllotChooseCoachPage
    extends SaasBindingFragment<PageAllotChooseSalerBinding, AllotChooseViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  @Need String title;
  @Need ArrayList<String> belongStaffIds;
  @Need ArrayList<String> studentIds;
  @Need String curId;
  @Need String textContent;

  public BaseGirdListFragment gridListFragment;

  @Override protected void subscribeUI() {
    mViewModel.setStudents(studentIds);
    mViewModel.getStaffs().observe(this, staffs -> {
      gridListFragment.setStaffs(staffs);
      for (int i = 0; i < staffs.size(); i++) {
        if (belongStaffIds.contains(staffs.get(i).id)) {
          gridListFragment.toggleSelection(i);
        }
      }
    });

    mViewModel.getResponseSuccess().observe(this, aBoolean -> {
      if (aBoolean) {
        int id = getFragmentManager().getBackStackEntryAt(1).getId();
        getFragmentManager().popBackStack(id, 1);
      }
    });
  }

  @Override
  public PageAllotChooseSalerBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageAllotChooseSalerBinding.inflate(inflater, container, false);
    initToolbar();
    mBinding.setTextcontent(textContent);
    initFragment();
    if (belongStaffIds == null) {
      belongStaffIds = new ArrayList<>();
    }
    if (curId != null) {
      belongStaffIds.add(curId);
    }
    loadSource();
    return mBinding;
  }

  public void loadSource() {
    mViewModel.loadData();
  }

  public void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel(title);
    toolbarModel.setMenu(R.menu.menu_compelete);
    toolbarModel.setListener(item -> {
      StringBuilder salernames = new StringBuilder();
      // TODO: 2018/7/5
      //List<String> selectIds = gridListFragment.getSelectedItemIds();
      //List<String> names = gridListFragment.getSelectedItemNames();
      //for (String name : names) {
      //  salernames.append(name).append(",");
      //}
      //
      //String msg = "";
      //if (getString(R.string.coach_choose_student).equals(title)) {//0
      //  if (selectIds.size() == 0) {
      //    DialogUtils.showAlert(getContext(),
      //        R.string.alert_choose_one_coach);
      //    return true;
      //  }
      //  salernames.deleteCharAt(salernames.length() - 1);
      //  msg = getString(R.string.alert_comfirm_allot, salernames);
      //} else if (getString(R.string.coach_change_student).equals(
      //    title)) {//1
      //  if (selectIds.size() == 0) {
      //    onShowError(R.string.alert_choose_one_coach);
      //    return true;
      //  }
      //  salernames.deleteCharAt(salernames.length() - 1);
      //  if (!TextUtils.isEmpty(curId) && !selectIds.contains(curId)) {
      //    msg = getString(R.string.alert_comfirm_trans, salernames);
      //  } else {
      //    msg = getString(R.string.alert_comfirm_allot, salernames);
      //  }
      //}
      //DialogUtils.showConfirm(getContext(), "", msg, (dialog, action) -> {
      //  dialog.dismiss();
      //  if (action == DialogAction.POSITIVE) mViewModel.allotCoach(selectIds);
      //});
      return true;
    });
    mBinding.setToolbarModel(toolbarModel);
  }

  private void initFragment() {
    gridListFragment = new BaseGirdListFragment();
    stuff(R.id.fragment_container, gridListFragment);
    gridListFragment.initListener(this);
  }

  @Override public boolean onItemClick(int position) {
    gridListFragment.toggleSelection(position);
    return true;
  }
}
