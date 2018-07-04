package cn.qingchengfit.saasbase.student.views.followup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentFollowUpStatusBinding;
import cn.qingchengfit.saasbase.student.bean.FollowUpFilterModel;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.student.items.FollowUpItem;
import cn.qingchengfit.saasbase.student.items.TotalCountFooterItem;
import cn.qingchengfit.saasbase.student.network.body.QcStudentBeanWithFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrappeForFollow;
import cn.qingchengfit.saasbase.student.presenters.followup.FollowUpStatusPresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/11/6.
 */
@Leaf(module = "student", path = "/follow/status")
public class FollowUpStatusFragment extends SaasBaseFragment implements FollowUpStatusPresenter.MVPView, FlexibleAdapter.EndlessScrollListener {

    FragmentFollowUpStatusBinding binding;
    @Need
    String title;
    @Need
    Integer type;

    @Inject
    FollowUpFilterModel model;
    @Inject
    FollowUpStatusPresenter presenter;
    @Inject
    LoginStatus loginStatus;

    FollowUpFilterFragment filterFragment;
    private CommonFlexAdapter flexibleAdapterToday;
    private List<AbstractFlexibleItem> itemsToday = new ArrayList<>();
    ProgressItem progress;
    private TotalCountFooterItem footItem;
    private FollowUpStatusTopFragment followUpStatusTopFragment;
    @Named("commonFilter")
    @Inject
    StudentFilter filter;

    @Override
    protected boolean isfitSystemPadding() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_follow_up_status, container, false);
        initToolbar(binding.includeToolbar.toolbar);
        binding.setToolbarModel(new ToolbarModel(title));
        delegatePresenter(presenter, this);
        initTopFragment();
        binding.setFragment(this);
        model.clear();
        binding.setModel(model);
        initView();
        initRecyclerView();
        initFilterEntity();
        loadData();
        return binding.getRoot();
    }

    private void initRecyclerView() {
        binding.recyclerViewToday.setLayoutManager(new SmoothScrollLinearLayoutManager(getActivity()));
        progress = new ProgressItem(getContext());
        footItem = new TotalCountFooterItem(1);
        if (flexibleAdapterToday == null) {
            flexibleAdapterToday = new CommonFlexAdapter(itemsToday, this);
            flexibleAdapterToday.setEndlessScrollListener(this, progress);

        }
        binding.recyclerViewToday.setAdapter(flexibleAdapterToday);

    }

    private void initView() {
        switch (type) {
            case FollowUpHomeFragment.FollowUpStatus.NEW_CREATE_USERS:
                binding.qftGender.setVisibility(View.GONE);
                break;
            case FollowUpHomeFragment.FollowUpStatus.NEW_FOLLOWING_USERS:
                binding.qtbFilter.setVisibility(View.GONE);
                break;
            case FollowUpHomeFragment.FollowUpStatus.NEW_MEMBER_USERS:
                binding.qftStatus.setVisibility(View.GONE);
                binding.qtbFilter.setVisibility(View.GONE);
                break;
        }

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) binding.fragFilter.getLayoutParams();
        lp.topMargin = (int) (MeasureUtils.getActionbarBarHeight(getContext()) + getResources().getDimension(R.dimen.qc_item_height));
        binding.fragFilter.setLayoutParams(lp);

        binding.qcRadioGroup.setCheckedChange(() -> {
            if (binding.qcRadioGroup.getCheckedPos() == -1) {
                binding.fragFilter.setVisibility(View.GONE);
            } else {
                binding.fragFilter.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadData() {
        presenter.getStudentsWithStatus(loginStatus.staff_id(), filter, type);
    }

    private void initTopFragment() {
        filterFragment = new FollowUpFilterFragment();
        filterFragment.setDismissAction(() -> {
            binding.qcRadioGroup.clearCheck();
            binding.fragFilter.setVisibility(View.GONE);
            followUpStatusTopFragment.clearButtonToggle();
        });
        filterFragment.setSelectAction((type) -> {
            if (type == 0) {
                loadData();
            } else {
                followUpStatusTopFragment.loadData();
            }
            binding.qcRadioGroup.clearCheck();
            binding.fragFilter.setVisibility(View.GONE);
            followUpStatusTopFragment.clearButtonToggle();
        });
        followUpStatusTopFragment = FollowUpStatusTopFragment.getInstance(type);
        followUpStatusTopFragment.setOnQcButtonClick(index -> {
            switch (index) {
                case -1:
                    binding.fragFilter.setVisibility(View.GONE);
                    return;
                case 0:
                    showFilterPage(5);
                    break;
                case 1:
                    showFilterPage(3);
                    break;
            }
            binding.fragFilter.setVisibility(View.VISIBLE);
        });
        stuff(R.id.frag_chart, followUpStatusTopFragment);
        stuff(R.id.frag_filter, filterFragment);
    }

    public void onQcBtnClick(View view) {
        binding.layoutCollapsed.setExpanded(false);
        if (view.getId() == R.id.qft_saler) {
            showFilterPage(0);
        } else if (view.getId() == R.id.qft_times) {
            showFilterPage(2);
        } else if (view.getId() == R.id.qft_status) {
            showFilterPage(1);
        } else if (view.getId() == R.id.qft_gender) {
            showFilterPage(4);
        } else if (view.getId() == R.id.qtb_filter) {

        }
    }

    public void showFilterPage(int index) {
        filterFragment.showPage(index);
    }

    @Override
    public void onTrackStudent(StudentListWrappeForFollow students, int curPage) {
        hideLoading();
        if (curPage == 1) {
            itemsToday.clear();
            flexibleAdapterToday.clear();
        }
        List<QcStudentBeanWithFollow> studentList = students.users == null ? (new ArrayList()) : students.users;
        if (curPage == 1 && (studentList == null || studentList.isEmpty())) {
            itemsToday.add(new SimpleTextItemItem("暂无数据", Gravity.CENTER));
            flexibleAdapterToday.notifyDataSetChanged();
            return;
        }
        List<AbstractFlexibleItem> newItems = new ArrayList<>();
        for (QcStudentBeanWithFollow student : studentList) {
            newItems.add(new FollowUpItem( student, type));
        }
        flexibleAdapterToday.onLoadMoreComplete(newItems);
        flexibleAdapterToday.notifyDataSetChanged();
    }

    @Override
    public void onToatalPages(int toatalPags) {
        flexibleAdapterToday.setEndlessTargetCount(toatalPags);
    }

    @Override
    public void onNoMoreLoad() {
        flexibleAdapterToday.onLoadMoreComplete(null);
    }

    @Override
    public void initEndlessLoad() {
        if (flexibleAdapterToday != null)
            flexibleAdapterToday.setEndlessScrollListener(this, progress);
    }

    /**
     * 初始化筛选数据
     */
    private void initFilterEntity() {
        filter.status = null;
        filter.registerTimeStart = DateUtils.getStringToday();
        filter.registerTimeEnd = DateUtils.getStringToday();
//        if (!serPermisAction.checkHasAllMember()) {
//            filter.sale = loginStatus.getLoginUser();
//        }
    }

    @Override
    public void noMoreLoad(int newItemsSize) {
        if (flexibleAdapterToday.getItem(flexibleAdapterToday.getItemCount() - 1) instanceof ProgressItem) {
            flexibleAdapterToday.removeItem(flexibleAdapterToday.getItemCount() - 1);
        }

        if (flexibleAdapterToday.getMainItemCount() > 0
                && !(flexibleAdapterToday.getItem(0) instanceof SimpleTextItemItem)
                && !flexibleAdapterToday.contains(footItem)) {
            flexibleAdapterToday.addScrollableFooter(footItem);
        }
    }

    @Override
    public void onLoadMore(int lastPosition, int currentPage) {
        presenter.loadMore(loginStatus.staff_id(), type);
    }
}
