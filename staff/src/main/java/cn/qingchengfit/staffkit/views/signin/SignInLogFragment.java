package cn.qingchengfit.staffkit.views.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.studentlist_rv) RecyclerView studentlistRv;

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

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_log, container, false);
        unbinder = ButterKnife.bind(this, view);
        //
        delegatePresenter(presenter, this);

        mCallbackActivity.setToolbar(getString(R.string.sign_in_log_title), false, null, 0, null);

        initView();
        showLoading();
        presenter.queryListData();

        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public String getFragmentName() {
        return SignInLogFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
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
