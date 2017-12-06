package cn.qingchengfit.student.view.home;

import android.databinding.OnRebindCallback;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageStudentHomeBinding;
import cn.qingchengfit.student.viewmodel.home.StudentHomeViewModel;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/12/5.
 */
@Leaf(module = "student", path = "/home/student")
public class StudentHomePage extends StudentBaseFragment<PageStudentHomeBinding, StudentHomeViewModel> implements FlexibleAdapter.OnItemClickListener {

    CommonFlexAdapter adapter;

    @Override
    protected void subscribeUI() {
        mViewModel.getLiveItems().observe(this, studentItems -> {
            mViewModel.isLoading.set(false);
            if (studentItems == null || studentItems.isEmpty()) return;
            mViewModel.items.set(mViewModel.getSortViewModel().sortItems(studentItems));
            mBinding.includeFilter.setItems(new ArrayList<>(studentItems));
        });
    }

    @Override
    public PageStudentHomeBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageStudentHomeBinding.inflate(inflater, container, false);
        initToolbar();
        initFragment();
        initFastScroller();
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.setItemClickListener(this);
        mBinding.setViewModel(mViewModel);
        mBinding.includeFilter.setFilter(mViewModel.getSortViewModel());
        mBinding.addOnRebindCallback(new OnRebindCallback<PageStudentHomeBinding>() {
            @Override
            public void onBound(PageStudentHomeBinding binding) {
                if (binding.recyclerview.getAdapter() != adapter) {
                    adapter = (CommonFlexAdapter) binding.recyclerview.getAdapter();
                    adapter.setFastScroller(mBinding.fastScroller);
                }
            }
        });
        mViewModel.loadSource(new StudentFilter());
        return mBinding;
    }

    private void initFastScroller() {
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

    private void initFragment() {
        stuff(R.id.frame_student_operation, new StudentOperationView());
        // REFACTOR: 2017/12/5 右侧filter
    }

    private void initToolbar() {
        ToolbarModel toolbarModel = new ToolbarModel("会员");
        toolbarModel.setMenu(cn.qingchengfit.saasbase.R.menu.menu_search);
        toolbarModel.setListener(item -> {
            ToastUtils.show("click");
            return true;
        });
        mBinding.setToolbarModel(toolbarModel);
        initToolbar(mBinding.includeToolbar.toolbar);
    }

    @Override
    public boolean onItemClick(int position) {
        return false;
    }
}
