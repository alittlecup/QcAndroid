package cn.qingchengfit.staffkit.views.gym.site;

import android.app.Activity;
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
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.adapter.ChooseSiteAdapter;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
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
 * Created by Paper on 16/5/10 2016.
 */
public class SiteListFragment extends BaseFragment implements ChooseSiteView {

    @BindView(R.id.recyclerview) RecycleViewWithNoImg recyclerview;
    @Inject ChooseSitePresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    private ChooseSiteAdapter adapter;
    private List<ImageTwoTextBean> datas = new ArrayList<>();
    private boolean isLoading = false;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_site_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        presenter.querySiteList(-1);

        adapter = new ChooseSiteAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adapter);
        recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.querySiteList(-1);
            }
        });
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {

                //跳去详情
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), SiteDetailFragment.newInstance(presenter.getSpaces().get(pos)))
                    .addToBackStack(null)
                    .commit();
            }
        });
        recyclerview.setNodataHint("暂无场地");
        recyclerview.setNoDataImgRes(R.drawable.no_space);
        if (isLoading) {
            recyclerview.stopLoading();
        }
        isLoading = true;
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("场地");
        toolbar.inflateMenu(R.menu.menu_add);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (!SerPermisAction.checkAll(PermissionServerUtils.STUDIO_LIST_CAN_WRITE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return true;
                }
                AddNewSiteFragment.start(SiteListFragment.this, 111, Configs.INIT_TYPE_CHOOSE);
                return true;
            }
        });
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 111) {
                presenter.querySiteList(-1);
            }
        }
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onData(List<ImageTwoTextBean> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
        recyclerview.setFresh(false);
        recyclerview.setNoData(datas.size() == 0);
    }
}
