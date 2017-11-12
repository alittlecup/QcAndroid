package cn.qingchengfit.saasbase.student.other;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentWithToolbarBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.DialogUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by huangbaole on 2017/11/2.
 */
@Leaf(module = "student", path = "/choose/coach")
public class ChooseCoachFragment extends SaasBaseFragment implements FlexibleAdapter.OnItemClickListener, ChooseCoachPresenter.MVPView {
    @Override
    protected boolean isfitSystemPadding() {
        return false;
    }

    FragmentWithToolbarBinding binding;
    BaseGirdListFragment gridListFragment;
    @Need
    String title;
    @Need
    ArrayList<String> belongStaffIds;
    @Need
    ArrayList<String> studentIds;
    @Need
    String curId;
    @Need
    String textContent;
    @Inject
    LoginStatus loginStatus;
    @Inject
    ChooseCoachPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_with_toolbar, container, false);
        initToolbar();
        initFragment();
        delegatePresenter(presenter, this);
        presenter.getCoachPreviewList(loginStatus.staff_id());
        binding.setTextcontent(textContent);
        if (belongStaffIds == null) {
            belongStaffIds = new ArrayList<>();
        }
        if (curId != null) {
            belongStaffIds.add(curId);
        }
        return binding.getRoot();
    }

    private void initFragment() {
        gridListFragment = new BaseGirdListFragment();
        stuff(R.id.fragment_container, gridListFragment);
        gridListFragment.initListener(this);
    }

    private void initToolbar() {
        initToolbar(binding.includeToolbar.toolbar);
        ToolbarModel toolbarModel = new ToolbarModel(title);
        toolbarModel.setMenu(R.menu.menu_compelete);
        toolbarModel.setListener(item -> {
            StringBuilder salernames = new StringBuilder();
            List<String> selectIds = gridListFragment.getSelectedItemIds();
            List<String> names = gridListFragment.getSelectedItemNames();
            for (String name : names) {
                salernames.append(name).append(",");
            }

            String msg = "";
            if (getString(R.string.coach_choose_student).equals(title)) {//0
                if (selectIds.size() == 0) {
                    DialogUtils.showAlert(getContext(), R.string.alert_choose_one_coach);
                    return true;
                }
                salernames.deleteCharAt(salernames.length() - 1);
                msg = getString(R.string.alert_comfirm_allot, salernames);

            } else if (getString(R.string.coach_change_student).equals(title)) {//1
                if (selectIds.size() == 0) {
                    onShowError(R.string.alert_choose_one_coach);
                    return true;
                }
                salernames.deleteCharAt(salernames.length() - 1);
                if (!TextUtils.isEmpty(curId) && !selectIds.contains(curId)) {
                    msg = getString(R.string.alert_comfirm_trans, salernames);
                } else {
                    msg = getString(R.string.alert_comfirm_allot, salernames);
                }
            }

            new MaterialDialog.Builder(getContext()).content(msg)
                    .positiveText(R.string.common_comfirm)
                    .negativeText(R.string.common_cancel)
                    .autoDismiss(true)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            presenter.allotCoaches(loginStatus.staff_id(), studentIds, selectIds);
                        }
                    })
                    .show();

            return true;
        });
        binding.setToolbarModel(toolbarModel);
    }

    @Override
    public boolean onItemClick(int position) {
        gridListFragment.toggleSelection(position);
        return true;
    }


    @Override
    public void onCoaches(List<Staff> staffs) {
        gridListFragment.setStaffs(staffs);
        for (int i = 0; i < staffs.size(); i++) {
            if (belongStaffIds.contains(staffs.get(i).id)) {
                gridListFragment.toggleSelection(i);
            }
        }
    }

    @Override
    public void onAllotSuccess() {
        int id=getFragmentManager().getBackStackEntryAt(1).getId();
        getFragmentManager().popBackStack(id,1);
    }
}
