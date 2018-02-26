package cn.qingchengfit.student.view.allot;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qingchengfit.saasbase.student.items.StudentItem;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageAllotStaffDetailBinding;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.viewmodel.allot.AllotStaffDetailViewModel;
import cn.qingchengfit.student.viewmodel.home.StudentFilterViewModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/11/21.
 */
@Leaf(module = "student", path = "/allotstaff/detail")
public class AllotStaffDetailPage extends StudentBaseFragment<PageAllotStaffDetailBinding, AllotStaffDetailViewModel> {

    CommonFlexAdapter adapter;
    private StudentFilterViewModel filterViewModel;
    private StudentFilterView filterView;

    @Override
    protected void subscribeUI() {
        mViewModel.getLiveItems().observe(this, items -> {
            if (items == null || items.isEmpty()) return;
            mViewModel.isLoading.set(false);
            mViewModel.items.set(mViewModel.getSortViewModel().sortItems(items));
            mBinding.includeFilter.setItems(new ArrayList<>(items));
        });

        mViewModel.type = getActivityViewModel().getAllotType();
        mViewModel.setSalerId(getActivityViewModel().getStaffId());


        mViewModel.getSortViewModel().getFilterEvent().observe(this, aVoid -> {
            openDrawer();
            filterViewModel = ViewModelProviders.of(filterView, factory).get(StudentFilterViewModel.class);
            filterViewModel.getmFilterMap().observe(this, map -> {
                if (map != null) {
                    closeDrawer();
                    mViewModel.loadSource(map);
                    filterViewModel.getmFilterMap().setValue(null);
                }
            });
            filterViewModel.setSalerId(getActivityViewModel().getStaffId());
        });

    }

    @Override
    public PageAllotStaffDetailBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageAllotStaffDetailBinding.inflate(inflater, container, false);
        initToolBar();
        mBinding.setViewModel(mViewModel);
        mBinding.includeFilter.setFilter(mViewModel.getSortViewModel());


        initRecyclerView();

        initFragment();

        mViewModel.loadSource(new HashMap<>());

        return mBinding;
    }

    private void initFragment() {
        filterView = new StudentFilterView();
        stuff(R.id.frame_student_filter, filterView);
    }

    private void openDrawer() {
        mBinding.drawer.openDrawer(GravityCompat.END);
    }

    private void closeDrawer() {
        mBinding.drawer.closeDrawer(GravityCompat.END);
    }


    private void initRecyclerView() {
        mBinding.recyclerview.setAdapter(adapter=new CommonFlexAdapter(new ArrayList()));

        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.fastScroller.setBarClickListener(letter -> {
            List<StudentItem> itemList = mViewModel.items.get();
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
        });
        adapter.setFastScroller(mBinding.fastScroller);

    }


    private void initToolBar() {
        boolean empty = TextUtils.isEmpty(getActivityViewModel().getAllotStaff().getValue().id);
        ToolbarModel toolbarModel = new ToolbarModel(empty ? getString(cn.qingchengfit.saasbase.R.string.qc_allotsale_sale_detail_notitle) : getString(cn.qingchengfit.saasbase.R.string.qc_allotsale_sale_detail_title, getActivityViewModel().getAllotStaff().getValue().username));
        toolbarModel.setMenu(empty ? cn.qingchengfit.saasbase.R.menu.menu_multi_allot : cn.qingchengfit.saasbase.R.menu.menu_multi_modify);
        toolbarModel.setListener(item -> {
            Uri uri = Uri.parse("student://student/allotstaff/multi");
            routeTo(uri, new cn.qingchengfit.student.view.allot.AllotMultiStaffPageParams()
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
