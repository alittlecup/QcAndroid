package cn.qingchengfit.student.view.allot;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageAllotStaffDetailBinding;
import cn.qingchengfit.student.viewmodel.allot.AllotStaffDetailViewModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/11/21.
 */
@Leaf(module = "student", path = "/allotstaff/detail")
public class AllotStaffDetailPage extends StudentBaseFragment<PageAllotStaffDetailBinding, AllotStaffDetailViewModel> {

    @Need
    Staff staff;
    @Need
    Integer type;
    CommonFlexAdapter adapter;

    @Override
    protected void initViewModel() {
        mViewModel.getLiveItems().observe(this, items -> {
            mViewModel.isLoading.set(false);
            mViewModel.items.set(new ArrayList<>(items));
            mBinding.includeFilter.setItems(new ArrayList<>(items));
        });
        mViewModel.getLetters().observe(this, letters -> {
            mBinding.fastScroller.setLetters(letters.toArray(new String[letters.size()]));
        });
        mViewModel.type = type;
        mViewModel.setSalerId(staff.id);
    }

    @Override
    public PageAllotStaffDetailBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageAllotStaffDetailBinding.inflate(inflater, container, false);
        initToolBar();
        mBinding.setViewModel(mViewModel);
        mBinding.includeFilter.setFilter(mViewModel.getSortViewModel());
        mViewModel.loadSource(mViewModel.getFilter());
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
        return mBinding;
    }


    @Override
    protected void onFinishAnimation() {
        super.onFinishAnimation();
        adapter = (CommonFlexAdapter) mBinding.recyclerview.getAdapter();
        adapter.setFastScroller(mBinding.fastScroller);
    }


    private void initToolBar() {
        boolean empty = TextUtils.isEmpty(staff.id);
        ToolbarModel toolbarModel = new ToolbarModel(empty ? getString(cn.qingchengfit.saasbase.R.string.qc_allotsale_sale_detail_notitle) : getString(cn.qingchengfit.saasbase.R.string.qc_allotsale_sale_detail_title, staff.username));
        toolbarModel.setMenu(empty ? cn.qingchengfit.saasbase.R.menu.menu_multi_allot : cn.qingchengfit.saasbase.R.menu.menu_multi_modify);
        toolbarModel.setListener(item -> {
            Uri uri = Uri.parse("student://student/allotstaff/multi");
            routeTo(uri, new cn.qingchengfit.student.view.allot.AllotMultiStaffPageParmas()
                    .staff(staff)
                    .type(type)
                    .title(item.getTitle().toString()).build());
            return true;
        });
        mBinding.setToolbarModel(toolbarModel);
        if (empty) {
            mBinding.llAddStu.setVisibility(View.GONE);
        }
        initToolbar(mBinding.includeToolbar.toolbar);
    }
}
