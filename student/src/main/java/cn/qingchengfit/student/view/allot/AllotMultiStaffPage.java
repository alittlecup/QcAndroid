package cn.qingchengfit.student.view.allot;

import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.student.items.StudentItem;
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
public class AllotMultiStaffPage extends StudentBaseFragment<PageAllotMultiStaffBinding, AllotMultiStaffViewModel>
        implements FlexibleAdapter.OnItemClickListener {
    @Need
    String title;

    CommonFlexAdapter adapter;

    @Override
    protected void subscribeUI() {
        mViewModel.getLiveItems().observe(this, items -> {
            if (items.isEmpty()) return;
            mViewModel.items.set(mViewModel.getSortViewModel().sortItems(items));
            mViewModel.isLoading.set(false);
            mBinding.includeFilter.setItems(new ArrayList<>(items));
        });
//        mViewModel.getLetters().observe(this, letters -> {
//            mBinding.fastScroller.setLetters(letters.toArray(new String[letters.size()]));
//        });
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
                mViewModel.selectAllChecked.set(true);
            } else {
                adapter.clearSelection();
                mViewModel.selectAllChecked.set(false);
            }
            adapter.notifyDataSetChanged();
            mViewModel.bottomTextCount.set(adapter.getSelectedItemCount());
        });
        mViewModel.getEditAfterTextChange().observe(this,filter->{
            adapter.setSearchText(filter);
            adapter.filterItems();
        });
        mViewModel.getRemoveSelectPos().observe(this, pos -> {
            Integer integer = adapter.getSelectedPositions().get(pos);
            onItemClick(integer);
        });

        mViewModel.getRouteTitle().observe(this, this::routeTo);
        mViewModel.setSalerId(getActivityViewModel().getAllotStaff().getValue().id);
        mViewModel.type = getActivityViewModel().getAllotType().getValue();
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
        mViewModel.hasName.set(!TextUtils.isEmpty(getActivityViewModel().getAllotStaff().getValue().username));
        mBinding.addOnRebindCallback(new OnRebindCallback<PageAllotMultiStaffBinding>() {
            @Override
            public void onBound(PageAllotMultiStaffBinding binding) {
                if(binding.recyclerview.getAdapter()!=adapter){
                    adapter= (CommonFlexAdapter) binding.recyclerview.getAdapter();
                    adapter.setFastScroller(binding.fastScroller);
                }
            }
        });
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
        adapter.notifyItemChanged(position);
        mViewModel.bottomTextCount.set(adapter.getSelectedItemCount());

//        if (adapter.getSelectedItemCount() == adapter.getItemCount()) {
//            mViewModel.selectAllChecked.set(true);
//        }else{
//            mViewModel.selectAllChecked.set(false);
//        }

        return true;
    }


    private void showDialog() {
        new MaterialDialog.Builder(getContext()).autoDismiss(true)
                .content("确认将选中会员从" + getActivityViewModel().getAllotStaff().getValue().username + "名下移除?")
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
        if ("showSelected".equalsIgnoreCase(title)) {
            mViewModel.getSelectedDatas().setValue(getSelectDataBeans());
            AllotSaleShowSelectDialogView f = new AllotSaleShowSelectDialogView();
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), "");
            return;
        }
        switch (getActivityViewModel().getAllotType().getValue()) {
            case 0:
                Uri toSaler = Uri.parse("student://student/allot/choosesaler");
                routeTo(toSaler, new cn.qingchengfit.student.view.allot.AllotChooseCoachPageParmas()
                        .title(title)
                        .studentIds(ids)
                        .textContent(getString(cn.qingchengfit.saasbase.R.string.choose_saler) + "\n" + getString(cn.qingchengfit.saasbase.R.string.choose_saler_tips))
                        .build());
                break;
            case 1:
                Uri toCoach = Uri.parse("student://student/allot/choosecoach");
                routeTo(toCoach, new cn.qingchengfit.student.view.allot.AllotChooseCoachPageParmas()
                        .title(title)
                        .studentIds(ids)
                        .textContent(getString(cn.qingchengfit.saasbase.R.string.choose_coach))
                        .build());
                break;
        }
        mViewModel.getSelectAll().setValue(false);
    }

    private ArrayList<String> getSelectIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (Integer pos : adapter.getSelectedPositions()) {
            ids.add(((StaffDetailItem) adapter.getItem(pos)).getId());
        }
        return ids;
    }

    public List<QcStudentBean> getSelectDataBeans() {
        List<QcStudentBean> studenBeans = new ArrayList<>();
        for (Integer pos : adapter.getSelectedPositions()) {
            studenBeans.add(((StudentItem) adapter.getItem(pos)).getQcStudentBean());
        }
        return studenBeans;
    }
}
