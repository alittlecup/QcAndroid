package cn.qingchengfit.staffkit.views.student.score;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.ScoreRank;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.widgets.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/22.
 */
public class ScoreHomeFragment extends BaseFragment
    implements ScoreHomePresenter.PresenterView, FlexibleAdapter.OnItemClickListener, View.OnClickListener {

    @Inject ScoreHomePresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;

	RecycleViewWithNoImg recyclerView;
	LinearLayout llStudentScoreLable;
	Toolbar toolbar;
	TextView toolbarTitile;

    private CommonFlexAdapter flexibleAdapter;
    private List<AbstractFlexibleItem> items = new ArrayList();
    private List<Student> datas = new ArrayList<>();

    private boolean isOpen = false;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_score, container, false);
      recyclerView = (RecycleViewWithNoImg) view.findViewById(R.id.recycler_view);
      llStudentScoreLable = (LinearLayout) view.findViewById(R.id.ll_student_score_lable);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);

      initToolbar(toolbar);
        delegatePresenter(presenter, this);
        initTitle();
        initView();

        if (serPermisAction.check(PermissionServerUtils.SCORE_RANK)) {
            presenter.getScoreStatus();
        }
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(R.string.qc_title_student_score);
        toolbar.inflateMenu(R.menu.menu_config);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (!serPermisAction.check(PermissionServerUtils.SCORE_SETTING)) {
                    showAlert(R.string.alert_permission_forbid);
                    return true;
                }
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), ConfigFragment.newInstance(false))
                    .addToBackStack(null)
                    .commit();
                return false;
            }
        });
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public void onResume() {
        super.onResume();
    }

    private void initTitle() {

    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        flexibleAdapter = new CommonFlexAdapter(items, this);
        recyclerView.setAdapter(flexibleAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                if (isOpen) {
                    presenter.getScoreRanks();
                } else {
                    recyclerView.stopLoading();
                }
            }
        });
        if (!serPermisAction.check(PermissionServerUtils.SCORE_RANK)) {
            recyclerView.stopLoading();
            items.clear();
            items.add(new CommonNoDataItem(R.drawable.ic_no_permission, "您没有积分排行榜查看权限"));
            llStudentScoreLable.setVisibility(View.GONE);
            flexibleAdapter.updateDataSet(items);
            flexibleAdapter.notifyDataSetChanged();
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

    @Override public void onScoreStatus(boolean score) {
        isOpen = score;
        if (score) {
            presenter.getScoreRanks();
        } else {
            recyclerView.stopLoading();
            items.clear();
            items.add(new StartItem(this));
            llStudentScoreLable.setVisibility(View.GONE);
            flexibleAdapter.updateDataSet(items);
            flexibleAdapter.notifyDataSetChanged();
        }
    }

    @Override public void onScoreRanks(List<ScoreRank> ranks) {
        recyclerView.stopLoading();

        datas.clear();
        items.clear();
        for (ScoreRank rank : ranks) {
            datas.add(rank.toStudent());
            items.add(new ScoreItem(rank.toStudent()));
        }

        llStudentScoreLable.setVisibility(View.VISIBLE);
        flexibleAdapter.updateDataSet(items);
        flexibleAdapter.notifyDataSetChanged();
    }

    @Override public void onShowError(String e) {

    }

    @Override public boolean onItemClick(int position) {
        if (isOpen) {
            if (!serPermisAction.check(PermissionServerUtils.SCORE_SETTING) && !serPermisAction.check(
                PermissionServerUtils.SCORE_SETTING_CAN_CHANGE)) {
                showAlert(R.string.alert_permission_forbid);
                return true;
            }
            // toStudent
            Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
            StudentBean bean = datas.get(position).toStudentBean();
            it.putExtra("student", bean);
            startActivity(it);
        }
        return false;
    }

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hint:
                if (!serPermisAction.check(PermissionServerUtils.SCORE_SETTING)) {
                    showAlert(R.string.alert_permission_forbid);
                    return;
                }
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), ConfigFragment.newInstance(true))
                    .addToBackStack(null)
                    .commit();
                return;
        }
    }
}
