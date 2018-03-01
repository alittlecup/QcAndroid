package cn.qingchengfit.saasbase.student.views.allot;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.Trainer;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentCoachDetailBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.student.items.CoachDetailItem;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.presenters.allot.CoashDetailPresenter;
import cn.qingchengfit.saasbase.student.views.home.StudentRecyclerViewFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by huangbaole on 2017/10/31.
 */
@Leaf(module = "student", path = "/coach/detail")
public class CoachDetailFragment extends SaasBaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, FlexibleAdapter.OnItemClickListener, CoashDetailPresenter.MVPView {
    FragmentCoachDetailBinding binding;

    private StudentRecyclerViewFragment studentRecyclerViewFragment;
    @Need
    Staff trainer;
    @Inject
    CoashDetailPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coach_detail, container, false);
        delegatePresenter(presenter, this);
        initFragment();
        initToolbar(binding.includeToolbar.toolbar);
        binding.setFilterModel(studentRecyclerViewFragment);
        initToolbar();
        onRefresh();
        return binding.getRoot();
    }

    @Override
    protected boolean isfitSystemPadding() {
        return false;
    }

    private void initToolbar() {
        boolean empty = TextUtils.isEmpty(trainer.username);
        ToolbarModel toolbarModel = new ToolbarModel(empty ? getString(R.string.qc_allotsale_sale_detail_notitle)
                : getString(R.string.qc_allotsale_sale_detail_title, trainer.username));
        toolbarModel.setMenu(empty ? R.menu.menu_multi_allot : R.menu.menu_multi_modify);
        toolbarModel.setListener(item -> {
            Uri uri = Uri.parse("qcstaff://student/multi/coach");
            routeTo(uri, new cn.qingchengfit.saasbase.student.views.allot.MultiAllotCoachParams().title(item.getTitle().toString()).trainer(trainer).build());
            return true;
        });
        binding.setToolbarModel(toolbarModel);
        if (empty) {
            binding.llAddStu.setVisibility(View.GONE);
        }
    }

    private void initFragment() {
        studentRecyclerViewFragment = new StudentRecyclerViewFragment();
        stuff(R.id.frag_student_list, studentRecyclerViewFragment);
        binding.setFilterModel(studentRecyclerViewFragment);
        studentRecyclerViewFragment.initListener(this);
        studentRecyclerViewFragment.setCallback(CoachDetailItem::new);
    }

    @Override
    public void onRefresh() {
        loadData("7505", new StudentFilter(), trainer.id);
    }

    private void loadData(String staffId, StudentFilter filter, String coachId) {
        presenter.queryStudent(staffId, filter, coachId);
    }

    @Override
    public void onStudentList(List<QcStudentBean> list) {
        studentRecyclerViewFragment.setData(list);

    }

    @Override
    public void onRemoveSuccess() {

    }

    @Override
    public void stopRefresh() {
        studentRecyclerViewFragment.stopRefresh();
    }

    @Override
    public boolean onItemClick(int i) {
        return false;
    }
}
