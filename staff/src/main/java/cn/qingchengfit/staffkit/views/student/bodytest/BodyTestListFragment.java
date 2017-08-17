package cn.qingchengfit.staffkit.views.student.bodytest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.responese.BodyTestBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.adapter.SimpleAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/21 2016.
 */
public class BodyTestListFragment extends BaseFragment implements BodyTestListView {

    @BindView(R.id.recycleview) RecycleViewWithNoImg recycleview;

    List<BodyTestBean> datas = new ArrayList<>();
    @Inject BodyTestListPresenter presenter;
    @Inject StudentWrapper studentBean;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;

    private SimpleAdapter adapter;
    private int gender = 0;

    public static BodyTestListFragment newInstance(int gender) {

        Bundle args = new Bundle();
        args.putInt("gender", gender);
        BodyTestListFragment fragment = new BodyTestListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gender = getArguments().getInt("gender");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bodytestlist, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        initToolbar(toolbar);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        adapter = new SimpleAdapter(datas);
        recycleview.setAdapter(adapter);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                getFragmentManager().beginTransaction()
                    .add(mCallbackActivity.getFragId(), BodyTestFragment.newInstance(datas.get(pos).id, gender))
                    .addToBackStack(null)
                    .commit();
            }
        });
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.queryBodyTestList();
            }
        });
        presenter.queryBodyTestList();
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        //mCallbackActivity.setToolbar("体测数据", false, null, 0, null);
        toolbarTitile.setText("体测数据");
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @OnClick(R.id.add_bodytest) public void onClick() {
        boolean hasP = false;
        for (int i = 0; i < studentBean.getStudentBean().getSupportIdList().size(); i++) {
            if (SerPermisAction.check(studentBean.getStudentBean().getSupportIdList().get(i),
                PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
                hasP = true;
                break;
            }
        }

        if (hasP) {

            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), ModifyBodyTestFragment.newInstance(""))
                .addToBackStack(null)
                .commit();
        } else {
            showAlert(getString(R.string.alert_permission_forbid));
        }
    }

    @Override public void onData(List<BodyTestBean> dat) {
        datas.clear();
        datas.addAll(dat);
        adapter.notifyDataSetChanged();
        recycleview.setNoData(datas.size() == 0);
    }

    @Override public String getFragmentName() {
        return BodyTestListFragment.class.getName();
    }
}