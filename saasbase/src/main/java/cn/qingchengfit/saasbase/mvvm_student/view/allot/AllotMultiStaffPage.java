//package cn.qingchengfit.saasbase.mvvm_student.view.allot;
//
//import android.arch.lifecycle.ViewModelProviders;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.view.GravityCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import cn.qingchengfit.items.StickerDateItem;
//import cn.qingchengfit.model.base.QcStudentBean;
//import cn.qingchengfit.model.others.ToolbarModel;
//import cn.qingchengfit.saasbase.R;
//import cn.qingchengfit.saasbase.databinding.PageAllotMultiStaffBinding;
//import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
//import cn.qingchengfit.saasbase.mvvm_student.items.StaffDetailItem;
//import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentFilterView;
//import cn.qingchengfit.saasbase.mvvm_student.viewmodel.allot.AllotMultiStaffViewModel;
//import cn.qingchengfit.saasbase.mvvm_student.viewmodel.home.StudentFilterViewModel;
//import cn.qingchengfit.saascommon.item.StudentItem;
//import cn.qingchengfit.utils.DialogUtils;
//import cn.qingchengfit.utils.ToastUtils;
//import cn.qingchengfit.widgets.CommonFlexAdapter;
//import com.afollestad.materialdialogs.DialogAction;
//import com.anbillon.flabellum.annotations.Leaf;
//import com.anbillon.flabellum.annotations.Need;
//import eu.davidea.flexibleadapter.FlexibleAdapter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by huangbaole on 2017/11/23.
// */
//@Leaf(module = "student", path = "/allotstaff/multi") public class AllotMultiStaffPage
//    extends StudentBaseFragment<PageAllotMultiStaffBinding, AllotMultiStaffViewModel>
//    implements FlexibleAdapter.OnItemClickListener {
//  @Need String title;
//
//  CommonFlexAdapter adapter;
//  private StudentFilterViewModel filterViewModel;
//  private StudentFilterView filterView;
//
//  @Override protected void subscribeUI() {
//    mViewModel.getLiveItems().observe(this, items -> {
//      if (items.isEmpty()) return;
//      mViewModel.items.set(mViewModel.getSortViewModel().sortItems(items));
//      mViewModel.isLoading.set(false);
//      mBinding.includeFilter.setItems(new ArrayList<>(items));
//    });
//    mViewModel.getIsDialogShow().observe(this, aVoid -> {
//      showDialog();
//    });
//    mViewModel.getIsRemoveSuccess().observe(this, aBoolean -> {
//      ToastUtils.show("移除成功");
//      getActivity().onBackPressed();
//    });
//    mViewModel.getSelectAll().observe(this, aBoolean -> {
//      if (aBoolean) {
//        adapter.selectAll(new Integer[0]);
//        mViewModel.selectAllChecked.set(true);
//      } else {
//        adapter.clearSelection();
//        mViewModel.selectAllChecked.set(false);
//      }
//      adapter.notifyDataSetChanged();
//      mViewModel.bottomTextCount.set(adapter.getSelectedItemCount());
//    });
//    mViewModel.getEditAfterTextChange().observe(this, filter -> {
//      adapter.setSearchText(filter);
//      adapter.filterItems();
//    });
//    mViewModel.getRemoveSelectPos().observe(this, pos -> {
//      Integer integer = adapter.getSelectedPositions().get(pos);
//      onItemClick(integer);
//    });
//
//    mViewModel.getRouteTitle().observe(this, this::routeTo);
//    mViewModel.setSalerId(getActivityViewModel().getStaffId());
//    mViewModel.type = getActivityViewModel().getAllotType();
//
//    mViewModel.getSortViewModel().getFilterEvent().observe(this, aVoid -> {
//      openDrawer();
//      filterViewModel =
//          ViewModelProviders.of(filterView, factory).get(StudentFilterViewModel.class);
//      filterViewModel.getmFilterMap().observe(this, map -> {
//        // REFACTOR: 2017/12/6 Map与Studentfilter的对决
//        if (map != null) {
//          closeDrawer();
//          mViewModel.loadSource(map);
//          filterViewModel.getmFilterMap().setValue(null);
//        }
//      });
//    });
//    mViewModel.sortEvent.observe(this, aVoid -> {
//      mBinding.recyclerview.post(new Runnable() {
//        @Override public void run() {
//          adapter.clearSelection();
//          if (ids != null) {
//            for (String id : ids) {
//              for (int index = 0; index < adapter.getItemCount(); index++) {
//                if (adapter.getItem(index) instanceof StudentItem) {
//                  String id1 = ((StudentItem) adapter.getItem(index)).getId();
//                  if (id.equals(id1)) {
//                    adapter.addSelection(index);
//                  }
//                }
//              }
//            }
//          }
//          adapter.notifyDataSetChanged();
//        }
//      });
//    });
//  }
//
//  private void openDrawer() {
//    mBinding.drawer.openDrawer(GravityCompat.END);
//  }
//
//  private void closeDrawer() {
//    mBinding.drawer.closeDrawer(GravityCompat.END);
//  }
//
//  @Override
//  public PageAllotMultiStaffBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
//      Bundle savedInstanceState) {
//    mBinding = PageAllotMultiStaffBinding.inflate(inflater, container, false);
//    mBinding.setViewModel(mViewModel);
//    mBinding.includeFilter.setFilter(mViewModel.getSortViewModel());
//    initToolBar();
//    initRecyclerView();
//    initFragment();
//    mViewModel.loadSource(new HashMap<>());
//    mBinding.setItemClickListener(this);
//    mViewModel.hasName.set(
//        !TextUtils.isEmpty(getActivityViewModel().getAllotStaff().getValue().username));
//
//    return mBinding;
//  }
//
//  private void initFragment() {
//    filterView = new StudentFilterView();
//    stuff(R.id.frame_student_filter, filterView);
//  }
//
//  private void initRecyclerView() {
//    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
//    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//    mBinding.fastScroller.setBarClickListener(letter -> {
//      List<StudentItem> itemList = mViewModel.items.get();
//      int position = 0;
//      for (int i = 0; i < itemList.size(); i++) {
//        if (itemList.get(i).getHeader() != null) {
//          if (itemList.get(i).getHeader() instanceof StickerDateItem) {
//            if (((StickerDateItem) itemList.get(i).getHeader()).getDate()
//                .equalsIgnoreCase(letter)) {
//              position = i;
//            }
//          }
//        }
//      }
//      return position;
//    });
//    adapter.setFastScroller(mBinding.fastScroller);
//  }
//
//  private void initToolBar() {
//    ToolbarModel toolbarModel = new ToolbarModel(title);
//    toolbarModel.setMenu(cn.qingchengfit.saasbase.R.menu.menu_cancel);
//    toolbarModel.setListener(item -> {
//      getActivity().onBackPressed();
//      return false;
//    });
//    mBinding.setToolbarModel(toolbarModel);
//  }
//
//  private List<String> ids;
//
//  @Override public boolean onItemClick(int position) {
//    adapter.toggleSelection(position);
//    adapter.notifyDataSetChanged();
//    mViewModel.bottomTextCount.set(adapter.getSelectedItemCount());
//    ids = getSelectIds();
//    return true;
//  }
//
//  private void showDialog() {
//    DialogUtils.showConfirm(getContext(), "",
//        "确认将选中会员从" + getActivityViewModel().getAllotStaff().getValue().username + "名下移除?",
//        (dialog, action) -> {
//          dialog.dismiss();
//          if (action == DialogAction.POSITIVE) mViewModel.removeStudentIds(concat(getSelectIds()));
//        });
//  }
//
//  private String concat(ArrayList<String> list) {
//    String ret = "";
//    for (int i = 0; i < list.size(); i++) {
//      if (i < list.size() - 1) {
//        ret = TextUtils.concat(ret, list.get(i), ",").toString();
//      } else {
//        ret = TextUtils.concat(ret, list.get(i)).toString();
//      }
//    }
//    return ret;
//  }
//
//  private void routeTo(String title) {
//    ArrayList<String> ids = getSelectIds();
//    if ("showSelected".equalsIgnoreCase(title)) {
//      mViewModel.getSelectedDatas().setValue(getSelectDataBeans());
//      AllotSaleShowSelectDialogView f = new AllotSaleShowSelectDialogView();
//      f.setTargetFragment(this, 0);
//      f.show(getFragmentManager(), "");
//      return;
//    }
//    switch (getActivityViewModel().getAllotType()) {
//      case 0:
//        Uri toSaler = Uri.parse("qcstaff://student/allot/choosesaler");
//        routeTo(toSaler, new AllotChooseCoachPageParams().title(title)
//            .studentIds(ids)
//            .textContent(
//                getString(cn.qingchengfit.saasbase.R.string.choose_saler) + "\n" + getString(
//                    cn.qingchengfit.saasbase.R.string.choose_saler_tips))
//            .build());
//        break;
//      case 1:
//        Uri toCoach = Uri.parse("qcstaff://student/allot/choosecoach");
//        routeTo(toCoach, new AllotChooseSalerPageParams().title(title)
//            .studentIds(ids)
//            .textContent(getString(cn.qingchengfit.saasbase.R.string.choose_coach))
//            .build());
//        break;
//    }
//    mViewModel.getSelectAll().setValue(false);
//  }
//
//  private ArrayList<String> getSelectIds() {
//    ArrayList<String> ids = new ArrayList<>();
//    for (Integer pos : adapter.getSelectedPositions()) {
//      ids.add(((StaffDetailItem) adapter.getItem(pos)).getId());
//    }
//    return ids;
//  }
//
//  public List<QcStudentBean> getSelectDataBeans() {
//    List<QcStudentBean> studenBeans = new ArrayList<>();
//    for (Integer pos : adapter.getSelectedPositions()) {
//      studenBeans.add(((StudentItem) adapter.getItem(pos)).getQcStudentBean());
//    }
//    return studenBeans;
//  }
//}
