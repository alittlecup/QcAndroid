//package cn.qingchengfit.saasbase.student.other;
//
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import cn.qingchengfit.saascommon.widget.BaseGirdListFragment;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.anbillon.flabellum.annotations.Leaf;
//import com.anbillon.flabellum.annotations.Need;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import cn.qingchengfit.di.model.LoginStatus;
//import cn.qingchengfit.model.base.Staff;
//import cn.qingchengfit.saasbase.R;
//import cn.qingchengfit.saasbase.SaasBaseFragment;
//import cn.qingchengfit.saasbase.databinding.FragmentWithToolbarBinding;
//import cn.qingchengfit.model.others.ToolbarModel;
//import cn.qingchengfit.utils.DialogUtils;
//import eu.davidea.flexibleadapter.FlexibleAdapter;
//
///**
// * Created by huangbaole on 2017/11/2.
// */
//@Leaf(module = "student", path = "/choose/staff")
//public class ChooseStaffFragment extends SaasBaseFragment implements FlexibleAdapter.OnItemClickListener, ChooseStaffPresenter.MVPView {
//    @Override
//    protected boolean isfitSystemPadding() {
//        return false;
//    }
//
//    FragmentWithToolbarBinding binding;
//    BaseGirdListFragment gridListFragment;
//
//    @Need
//    String title;
//    @Need
//    ArrayList<String> belongStaffIds;
//    @Need
//    ArrayList<String> studentIds;
//    @Need
//    String curId;
//    @Need
//    String textContent;
//
//    @Inject
//    LoginStatus loginStatus;
//    @Inject
//    ChooseStaffPresenter presenter;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_with_toolbar, container, false);
//        initToolbar();
//        initFragment();
//        delegatePresenter(presenter, this);
//        presenter.loadData(loginStatus.staff_id());
//        binding.setTextcontent(textContent);
//        if (belongStaffIds == null) {
//            belongStaffIds = new ArrayList<>();
//        }
//        if (curId != null) {
//            belongStaffIds.add(curId);
//        }
//        return binding.getRoot();
//    }
//
//    private void initFragment() {
//        gridListFragment = new BaseGirdListFragment();
//        stuff(R.id.fragment_container, gridListFragment);
//        gridListFragment.initListener(this);
//    }
//
//    private void initToolbar() {
//        initToolbar(binding.includeToolbar.toolbar);
//        ToolbarModel toolbarModel = new ToolbarModel(title);
//        toolbarModel.setMenu(R.menu.menu_compelete);
//        toolbarModel.setListener(item -> {
//            StringBuilder salernames = new StringBuilder();
//            List<String> selectIds = gridListFragment.getSelectedItemIds();
//            List<String> names = gridListFragment.getSelectedItemNames();
//            for (String name : names) {
//                salernames.append(name).append(",");
//            }
//
//            String msg = "";
//            if (getString(R.string.allot_saler).equals(title)) {//0
//                if (selectIds.size() == 0) {
//                    DialogUtils.showAlert(getContext(), R.string.alert_choose_one_saler);
//                    return true;
//                }
//                salernames.deleteCharAt(salernames.length() - 1);
//                msg = getString(R.string.alert_comfirm_allot, salernames);
//
//            } else if (getString(R.string.qc_allotsale_modify_sale).equals(title)) {//1
//                if (selectIds.size() == 0) {
//                    onShowError(R.string.alert_choose_one_saler);
//                    return true;
//                }
//                salernames.deleteCharAt(salernames.length() - 1);
//                if (!TextUtils.isEmpty(curId) && !selectIds.contains(curId)) {
//                    msg = getString(R.string.alert_comfirm_trans, salernames);
//                } else {
//                    msg = getString(R.string.alert_comfirm_allot, salernames);
//                }
//            }
//
//            new MaterialDialog.Builder(getContext()).content(msg)
//                    .positiveText(R.string.common_comfirm)
//                    .negativeText(R.string.common_cancel)
//                    .autoDismiss(true)
//                    .onPositive((dialog, which) -> presenter.allotSalers(loginStatus.staff_id(), studentIds, selectIds, curId))
//                    .show();
//
//            return true;
//        });
//        binding.setToolbarModel(toolbarModel);
//    }
//
//    @Override
//    public boolean onItemClick(int position) {
//        gridListFragment.toggleSelection(position);
//        return true;
//    }
//
//    @Override
//    public void onStaffList(List<Staff> staffs) {
//        gridListFragment.setStaffs(staffs);
//        for (int i = 0; i < staffs.size(); i++) {
//            if (belongStaffIds.contains(staffs.get(i).id)) {
//                gridListFragment.toggleSelection(i);
//            }
//        }
//    }
//
//    @Override
//    public void onAllotSuccess() {
//        // REFACTOR: 2017/11/3 返回动作
//
//    }
//}
