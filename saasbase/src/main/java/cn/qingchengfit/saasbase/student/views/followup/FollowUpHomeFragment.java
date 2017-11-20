package cn.qingchengfit.saasbase.student.views.followup;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentFollowUpBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.student.items.DataStatisticsItem;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.presenters.followup.FollowUpHomePresenter;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpStatusParams;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

/**
 * Created by huangbaole on 2017/11/3.
 */
@Leaf(module = "student", path = "/follow/home")
public class FollowUpHomeFragment extends SaasBaseFragment implements FollowUpHomePresenter.MVPView,FlexibleAdapter.OnItemClickListener {

    FragmentFollowUpBinding binding;
    @Inject
    FollowUpHomePresenter presenter;
    @Inject
    LoginStatus loginStatus;
    @Inject
    GymWrapper gymWrapper;
    protected CommonFlexAdapter commonFlexAdapter;

    List<DataStatisticsItem> datas = new ArrayList<>();

    @Override
    protected boolean isfitSystemPadding() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_follow_up, container, false);
        ToolbarModel toolbarModel = new ToolbarModel(getString(R.string.qc_student_follow_up));
        binding.setToolbarModel(toolbarModel);
        initToolbar(binding.includeToolbar.toolbar);
        delegatePresenter(presenter, this);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        commonFlexAdapter = new CommonFlexAdapter(datas);
        binding.recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        binding.recyclerview.setAdapter(commonFlexAdapter);
        commonFlexAdapter.addListener(this);
    }


    @Override
    protected void onFinishAnimation() {
        showLoadingTrans();
        presenter.getStudentsStatistics(loginStatus.staff_id(),null);
    }

    @Override
    public void onFollowUpStatistics(FollowUpDataStatistic statistics) {
        hideLoadingTrans();
        datas.clear();
        datas.add(new DataStatisticsItem("新增注册", statistics.new_create_users));
        datas.add(new DataStatisticsItem("新增跟进", statistics.new_following_users));
        datas.add(new DataStatisticsItem("新增会员", statistics.new_member_users));
        commonFlexAdapter.updateDataSet(datas);
    }
    @Override
    public boolean onItemClick(int position) {
        Uri uri=Uri.parse("student://student/follow/status");
        routeTo(uri,new FollowUpStatusParams().title(datas.get(position).getTitle()).type(position).build());
        return false;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FollowUpStatus {
        int NEW_CREATE_USERS = 0;
        int NEW_FOLLOWING_USERS = 1;
        int NEW_MEMBER_USERS = 2;
    }
}
