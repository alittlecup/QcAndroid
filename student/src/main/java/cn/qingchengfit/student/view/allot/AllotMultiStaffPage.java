package cn.qingchengfit.student.view.allot;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.student.other.ChooseStaffParams;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageAllotMultiStaffBinding;
import cn.qingchengfit.student.items.StaffDetailItem;
import cn.qingchengfit.student.viewmodel.allot.AllotMultiStaffViewModel;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/11/23.
 */
@Leaf(module = "student", path = "/allotstaff/multi")
public class AllotMultiStaffPage extends StudentBaseFragment<PageAllotMultiStaffBinding, AllotMultiStaffViewModel> implements FlexibleAdapter.OnItemClickListener {
    @Need
    String title;
    @Need
    Staff staff;
    @Need
    Integer type;
    CommonFlexAdapter adapter;

    @Override
    protected void initViewModel() {
        mViewModel.getLiveItems().observe(this, items -> {
            mViewModel.items.set(new ArrayList<>(items));
            mViewModel.isLoading.set(false);
            mBinding.includeFilter.setItems(new ArrayList<>(items));
        });
        mViewModel.getLetters().observe(this, letters -> {
            mBinding.fastScroller.setLetters(letters.toArray(new String[letters.size()]));
        });
        mViewModel.getIsDialogShow().observe(this, showed -> {
            if (showed) showDialog();
        });
        mViewModel.getIsRemoveSuccess().observe(this, aBoolean -> {
            ToastUtils.show("移除成功");
            getActivity().onBackPressed();
        });
        mViewModel.getSelectAll().observe(this, aBoolean -> {
            if (aBoolean) {
                adapter.selectAll(new Integer[0]);
            } else {
                adapter.clearSelection();
            }
        });

        mViewModel.getRouteTitle().observe(this, this::routeTo);
        mViewModel.setSalerId(staff.id);
        mViewModel.type = type;
    }

    @Override
    public PageAllotMultiStaffBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageAllotMultiStaffBinding.inflate(inflater, container, false);
        mBinding.setViewModel(mViewModel);
        mBinding.includeFilter.setFilter(mViewModel.getSortViewModel());
        initToolBar();
        initRecyclerView();
        mViewModel.loadSource(mViewModel.getStudentFilter());
        mBinding.setItemClickListener(this);
        return mBinding;
    }

    private void initRecyclerView() {
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.fastScroller.setBarClickListener(letter -> {
            List<AbstractFlexibleItem> itemList = mViewModel.items.get();
            int position = 0;
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i) instanceof StickerDateItem) {
                    if (((StickerDateItem) itemList.get(i)).getDate().equalsIgnoreCase(letter)) {
                        position = i;
                    }
                }
            }
            return position;
        });
    }

    @Override
    protected void onFinishAnimation() {
        super.onFinishAnimation();
        adapter = (CommonFlexAdapter) mBinding.recyclerview.getAdapter();
    }

    private void initToolBar() {
        ToolbarModel toolbarModel = new ToolbarModel(title);
        toolbarModel.setMenu(cn.qingchengfit.saasbase.R.menu.menu_cancel);
        toolbarModel.setListener(item -> {
            getActivity().onBackPressed();
            return false;
        });
        mBinding.setToolbarModel(toolbarModel);
    }

    @Override
    public boolean onItemClick(int position) {
        adapter.toggleSelection(position);
        mViewModel.bottomTextCount.set(adapter.getSelectedItemCount());
        if (adapter.getSelectedItemCount() == adapter.getItemCount()) {
            mViewModel.selectAllChecked.set(true);
        }
        return false;
    }

    private void showDialog() {
        new MaterialDialog.Builder(getContext()).autoDismiss(true)
                .content("确认将选中会员从" + staff.username + "名下移除?")
                .positiveText(cn.qingchengfit.saasbase.R.string.common_comfirm)
                .negativeText(cn.qingchengfit.saasbase.R.string.common_cancel)
                .autoDismiss(true)
                .onPositive((dialog, which) -> {
                    // 批量移除
                    mViewModel.removeStudentIds(concat(getSelectIds()));
                })
                .show();
    }

    private String concat(ArrayList<String> list) {
        String ret = "";
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                ret = TextUtils.concat(ret, list.get(i), ",").toString();
            } else {
                ret = TextUtils.concat(ret, list.get(i)).toString();
            }
        }
        return ret;
    }

    private void routeTo(String title) {
        ArrayList<String> ids = getSelectIds();
        Uri uri = Uri.parse("");
        routeTo(uri, new ChooseStaffParams()
                .title(title)
                .curId(staff.id)
                .studentIds(ids)
                .textContent(getString(cn.qingchengfit.saasbase.R.string.choose_coach))
                .build());
    }

    private ArrayList<String> getSelectIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (Integer pos : adapter.getSelectedPositions()) {
            ids.add(((StaffDetailItem) adapter.getItem(pos)).getId());
        }
        return ids;
    }
}
