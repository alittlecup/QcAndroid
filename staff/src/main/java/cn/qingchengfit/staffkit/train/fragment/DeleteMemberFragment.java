package cn.qingchengfit.staffkit.train.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.train.event.RefreshListEvent;
import cn.qingchengfit.staffkit.train.item.SignUpGroupMemberItem;
import cn.qingchengfit.staffkit.train.presenter.SignUpGroupDetailPresenter;
import cn.qingchengfit.staffkit.train.presenter.SignUpView;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/3/29.
 */

@FragmentWithArgs public class DeleteMemberFragment extends BaseFragment implements SignUpView, FlexibleAdapter.OnItemClickListener {

	Toolbar toolbar;
	TextView textToolbarRight;
	TextView toolbarTitile;
	RecyclerView recyclerMemberDelete;

    @Arg ArrayList<QcStudentBean> memberList;
    @Arg String teamId;
    @Inject SignUpGroupDetailPresenter presenter;
    private List<SignUpGroupMemberItem> itemList = new ArrayList<>();
    private CommonFlexAdapter adapter;
    private List<String> deleteList = new ArrayList<>();

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delete_member, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      textToolbarRight = (TextView) view.findViewById(R.id.text_toolbar_right);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      recyclerMemberDelete = (RecyclerView) view.findViewById(R.id.recycler_member_delete);

      initView();

        delegatePresenter(presenter, this);
        recyclerMemberDelete.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMemberDelete.setItemAnimator(new DefaultItemAnimator());
        adapter = new CommonFlexAdapter(itemList, this);
        recyclerMemberDelete.setAdapter(adapter);
        return view;
    }

    private void initView() {
        for (QcStudentBean bean : memberList) {
            SignUpGroupMemberItem memberItem = new SignUpGroupMemberItem(bean);
            memberItem.setOperation(true);
            itemList.add(memberItem);
        }
        toolbarTitile.setText("移除成员");
        toolbar.inflateMenu(R.menu.menu_compelete);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (itemList != null) {
                    for (SignUpGroupMemberItem item1 : itemList) {
                        deleteList.add(item1.getStudentBean().id());
                    }
                    presenter.operationGroup(teamId, "", deleteList);
                }
                return false;
            }
        });
        textToolbarRight.setVisibility(View.VISIBLE);
        textToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                deleteList.clear();
                getActivity().onBackPressed();
            }
        });
    }

    @Override public String getFragmentName() {
        return DeleteMemberFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        presenter.unattachView();
    }

    @Override public void onGetSignUpDataSuccess(Object data) {
    }

    @Override public void onFailed(String msg) {
        ToastUtils.show(msg);
    }

    @Override public void onSuccess() {
        ToastUtils.show("删除成功");
        getActivity().onBackPressed();
        RxBus.getBus().post(new RefreshListEvent());
    }

    @Override public void onDelSuccess() {

    }

    @Override public boolean onItemClick(int position) {
        adapter.removeItem(position);
        adapter.notifyDataSetChanged();
        return false;
    }
}
