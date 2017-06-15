package cn.qingchengfit.staffkit.allocate.coach.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.CommonAllocateItem;
import cn.qingchengfit.staffkit.allocate.coach.AllocateCoachActivity;
import cn.qingchengfit.staffkit.allocate.coach.model.Coach;
import cn.qingchengfit.staffkit.allocate.coach.presenter.AllocateCoachListPresenter;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/5/4.
 */

public class AllocateCoachListFragment extends BaseFragment
    implements AllocateCoachListPresenter.CoachListView, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_coaches_list) RecyclerView list;

    @Inject AllocateCoachListPresenter presenter;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;

    private List<CommonAllocateItem> itemList = new ArrayList<>();
    private CommonFlexAdapter adapter;

    @Override public String getFragmentName() {
        return AllocateCoachListFragment.class.getName();
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coaches_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        getData();
        setToolbar(toolbar);

        adapter = new CommonFlexAdapter(itemList, this);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        list.setAdapter(adapter);
        return view;
    }

    public void setToolbar(Toolbar toolbar) {
        initToolbar(toolbar);
        toolbarTitile.setText("教练列表");
    }

    @Override public void onResume() {
        if (getActivity() instanceof AllocateCoachActivity) {
            ((AllocateCoachActivity) getActivity()).removeFilterFragment();
        }
        super.onResume();
    }

    private void getData() {
        presenter.getCoachPreviewList();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onCoachessPreview(List<Coach> list) {
        hideLoadingTrans();
        itemList.clear();
        for (Coach coach : list) {
            itemList.add(new CommonAllocateItem(coach));
        }
        adapter.notifyDataSetChanged();
    }

    @Override public boolean onItemClick(int position) {
        getFragmentManager().beginTransaction()
            .replace(R.id.frame_allocate_coach,
                CoachStudentDetailFragmentBuilder.newCoachStudentDetailFragment(itemList.get(position).getData().coach.id,
                    itemList.get(position).getData().coach.username))
            .addToBackStack(null)
            .commit();
        return false;
    }
}