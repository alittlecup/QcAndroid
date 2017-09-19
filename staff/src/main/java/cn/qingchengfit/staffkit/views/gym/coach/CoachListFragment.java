package cn.qingchengfit.staffkit.views.gym.coach;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.QRActivity;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.gym.AddNewCoachFragment;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
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
 * Created by Paper on 16/5/10 2016.
 */
public class CoachListFragment extends BaseFragment implements CoachListView {
    public static final int RESULT_FLOW = 1;
    public static final int RESULT_ADD = 11;
    public CoachAapter adapter;
    @BindView(R.id.recyclerview) RecycleViewWithNoImg recyclerview;
    List<Staff> datas = new ArrayList<>();
    @Inject CoachListPresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private boolean isLoading = false;
    private SearchView mSearchView;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);

        adapter = new CoachAapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adapter);
        recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.queryData(null);
            }
        });
        presenter.queryData(null);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                goDetail(datas.get(pos));
            }
        });
        if (isLoading) recyclerview.stopLoading();
        isLoading = true;
        return view;
    }

    @Override public void initToolbar(@NonNull final Toolbar toolbar) {
        if (getActivity() instanceof ChooseActivity) {
            toolbarLayout.setVisibility(View.GONE);
        } else {
            super.initToolbar(toolbar);
            toolbarTitile.setText("教练");
            toolbar.inflateMenu(R.menu.menu_search_flow_searchview);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_flow) {
                        BottomSheetListDialogFragment.start(CoachListFragment.this, RESULT_FLOW, new String[] { "教练权限设置" });
                    }
                    return true;
                }
            });

            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            MenuItem searchMenuItem = toolbar.getMenu().getItem(0);
            mSearchView = (SearchView) searchMenuItem.getActionView();
            if (mSearchView != null) {
                mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            }
            mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override public boolean onClose() {
                    toolbarTitile.setVisibility(View.VISIBLE);
                    presenter.queryData(null);
                    return false;
                }
            });
            mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override public void onFocusChange(View view, boolean b) {
                    if (b) toolbarTitile.setVisibility(View.GONE);
                }
            });
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override public boolean onQueryTextSubmit(String query) {
                    presenter.queryData(query);
                    return false;
                }

                @Override public boolean onQueryTextChange(String newText) {

                    return false;
                }
            });
        }
    }

    @OnClick(R.id.fab_add_coach) public void addCoach() {
        if (!SerPermisAction.checkAll(PermissionServerUtils.COACHSETTING_CAN_WRITE)) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }

        AddNewCoachFragment.start(CoachListFragment.this, RESULT_ADD);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_ADD) {
                presenter.queryData(null);
            } else if (requestCode == RESULT_FLOW) {
                int pos = Integer.parseInt(IntentUtils.getIntentString(data));
                if (pos == 0) {
                    if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.POSITION_SETTING)) {
                        showAlert(R.string.sorry_for_no_permission);
                        return;
                    }

                    Intent toScan = new Intent(getActivity(), QRActivity.class);
                    toScan.putExtra(QRActivity.LINK_URL, Configs.Server
                        + "app2web/?id="
                        + gymWrapper.id()
                        + "&model="
                        + gymWrapper.model()
                        + "&module="
                        + GymFunctionFactory.PERMISSION_TRAINER);
                    startActivity(toScan);
                } else {

                }
            }
        }
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onList(List<Staff> coach) {
        recyclerview.setFresh(false);
        datas.clear();
        datas.addAll(coach);
        adapter.notifyDataSetChanged();
        recyclerview.setNoData(datas.size() == 0);
    }

    @Override public void onFailed() {
        recyclerview.setFresh(false);
    }

    @Override public void goDetail(Staff coach) {
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), CoachDetailFragment.newInstance(coach))
            .addToBackStack(null)
            .commit();
    }
}
