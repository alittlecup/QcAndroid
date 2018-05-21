package cn.qingchengfit.staffkit.views.signin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.SignInLogAdapter;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.List;
import javax.inject.Inject;

/**
 * 签到记录列表
 * Created by yangming on 16/8/30.
 */
public class SignInLogFragment extends BaseFragment implements SignInLogPresenter.SignInLogView {

	Toolbar toolbar;
	TextView toolbarTitle;
	RecyclerView studentlistRv;

  @Inject SignInLogPresenter presenter;
  List<SignInTasks.SignInTask> list;
  SignInLogAdapter adapter;
  private LinearLayoutManager mLinearLayoutManager;

  public SignInLogFragment() {
  }

  public static SignInLogFragment newInstance() {
    SignInLogFragment fragment = new SignInLogFragment();
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signin_log, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    studentlistRv = (RecyclerView) view.findViewById(R.id.studentlist_rv);

    delegatePresenter(presenter, this);

    initView();
    initToolbar(toolbar);
    showLoading();
    presenter.queryListData();

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(R.string.sign_in_log_title);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override public String getFragmentName() {
    return SignInLogFragment.class.getName();
  }



  private void initView() {
    mLinearLayoutManager = new LinearLayoutManager(getContext());
    studentlistRv.setLayoutManager(mLinearLayoutManager);
  }

  @Override public void onData(List<SignInTasks.SignInTask> list) {
    hideLoading();
    this.list = list;
    if (list == null || list.isEmpty()) {
      ToastUtils.show("没有签到记录");
      return;
    }
    if (adapter == null) {
      SignInLogAdapter adapter = new SignInLogAdapter(this.list);
      studentlistRv.setAdapter(adapter);
      adapter.setListener(new OnRecycleItemClickListener() {
        @Override public void onItemClick(View v, int checkInId) {
          getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), SignInDetailFragment.newInstance(checkInId))
            .addToBackStack(null)
            .commit();
        }
      });
    } else {
      adapter.notifyDataSetChanged();
    }
  }
}
