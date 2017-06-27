package cn.qingchengfit.staffkit.views.gym.staff;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.StaffShip;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.QRActivity;
import cn.qingchengfit.staffkit.views.adapter.StaffAdapter;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.IntentUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
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
 * Created by Paper on 16/5/11 2016.
 */
public class StaffListFragment extends BaseFragment implements StaffListView {

    public static final int RESULT_FLOW = 1;

    @BindView(R.id.recyclerview) RecyclerView recyclerview;

    @Inject StaffListPresenter presenter;
    @BindView(R.id.nodata) LinearLayout nodata;
    @BindView(R.id.loading_layout) LinearLayout loadingLayout;
    @BindView(R.id.su_admin_title) TextView suAdminTitle;
    @BindView(R.id.su_avatar) ImageView suAvatar;
    @BindView(R.id.su_name) TextView suName;
    @BindView(R.id.su_phone) TextView suPhone;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;

    private Staff mSelf;

    private List<StaffShip> datas = new ArrayList<>();
    private StaffAdapter adatper;
    private boolean isLoading = false;

    private Staff mSu;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        suAdminTitle.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_superadmin_crown), null,
            null, null);
        suAdminTitle.setCompoundDrawablePadding(16);

        adatper = new StaffAdapter(datas);
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adatper);
        adatper.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {

                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), StaffDetailFragment.newInstance(datas.get(pos)))
                    .addToBackStack(null)
                    .commit();
            }
        });
        presenter.querSelfInfo(App.staffId);
        presenter.queryData(null);

        if (isLoading) loadingLayout.setVisibility(View.GONE);
        isLoading = true;
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("工作人员");
        toolbar.inflateMenu(R.menu.menu_search_flow_searchview);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_search) {
                } else if (item.getItemId() == R.id.action_flow) {
                    BottomSheetListDialogFragment.start(StaffListFragment.this, RESULT_FLOW, new String[] { "工作人员职位与权限设置" });
                }
                return true;
            }
        });
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = toolbar.getMenu().getItem(0);
        SearchView mSearchView = (SearchView) searchMenuItem.getActionView();
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

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_FLOW) {
                int pos = Integer.parseInt(IntentUtils.getIntentString(data));
                if (pos == 0) {//权限设置
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
                        + GymFunctionFactory.PERMISSION_STAFF);
                    startActivity(toScan);
                } else {//

                }
            }
        }
    }

    @OnClick(R.id.fab_add_staff) public void addStaff() {
        if (!SerPermisAction.checkAll(PermissionServerUtils.MANAGE_STAFF_CAN_WRITE)) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), StaffDetailFragment.newInstance(null))
            .addToBackStack(null)
            .commit();
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onData(List<StaffShip> staffs) {
        datas.clear();
        for (int i = 0; i < staffs.size(); i++) {
            if (staffs.get(i).is_superuser) {
                mSu = staffs.get(i).user;
            } else {
                datas.add(staffs.get(i));
            }
        }
        adatper.notifyDataSetChanged();
        loadingLayout.setVisibility(View.GONE);
        nodata.setVisibility(datas.size() == 0 ? View.VISIBLE : View.GONE);
        if (mSu != null) {
            Glide.with(getContext())
                .load(PhotoUtils.getSmall(mSu.getAvatar()))
                .asBitmap()
                .placeholder(mSu.getGender() == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female)
                .into(new CircleImgWrapper(suAvatar, getContext()));
            suName.setText(mSu.getUsername());
            suPhone.setText(mSu.getPhone());
        }
    }

    @Override public void onSelfInfo(Staff staff) {
        mSelf = staff;
    }

    @Override public void onFailed() {
        //        recyclerview.setFresh(false);
    }

    @OnClick(R.id.layout_su) public void onClickSu() {
        if (mSu != null && mSelf != null && mSu.getPhone().equalsIgnoreCase(mSelf.getPhone())) {
            getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
                .replace(mCallbackActivity.getFragId(), new SuFragmentBuilder(mSu).build())
                .addToBackStack(getFragmentName())
                .commit();
        } else {
            showAlert("仅超级管理员本人有权限查看其基本信息");
        }
    }
}
