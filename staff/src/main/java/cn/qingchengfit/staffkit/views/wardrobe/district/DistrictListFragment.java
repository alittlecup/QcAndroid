package cn.qingchengfit.staffkit.views.wardrobe.district;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.wardrobe.add.DistrictAddFragment;
import cn.qingchengfit.staffkit.views.wardrobe.item.DistrictAddItem;
import cn.qingchengfit.staffkit.views.wardrobe.item.DistrictItem;
import cn.qingchengfit.utils.ToastUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/25.
 */
public class DistrictListFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener, DistrictListPresenter.MVPView {

    @BindView(R.id.rv) RecyclerView rv;
    @Inject DistrictListPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
    private List<AbstractFlexibleItem> mData = new ArrayList<>();
    private CommonFlexAdapter mAdatper;

    public static DistrictListFragment newInstance() {

        Bundle args = new Bundle();

        DistrictListFragment fragment = new DistrictListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_district_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(presenter, this);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv.setHasFixedSize(true);
        mAdatper = new CommonFlexAdapter(mData, this);
        rv.setAdapter(mAdatper);
        presenter.queryList(App.staffId);
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("更衣柜区域");
    }

    @Override public String getFragmentName() {
        return DistrictListFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {
        if (mData.get(position) instanceof DistrictAddItem) {//新增
            if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.LOCKER_SETTING_CAN_WRITE)) {
                showAlert(R.string.alert_permission_forbid);
                return true;
            }
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), new DistrictAddFragment())
                .addToBackStack(getFragmentName())
                .commit();
        } else if (mData.get(position) instanceof DistrictItem) {//编辑区域
            if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.LOCKER_SETTING_CAN_CHANGE)
                && !SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.LOCKER_SETTING_CAN_DELETE)) {
                showAlert(R.string.alert_permission_forbid);
                return true;
            }
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), DistrictAddFragment.newInstance(((DistrictItem) mData.get(position)).lockerRegion))
                .addToBackStack(getFragmentName())
                .commit();
        }
        return false;
    }

    @Override public void onList(List<LockerRegion> data) {
        mData.clear();
        mData.add(new DistrictAddItem());
        if (data != null) {
            for (LockerRegion lr : data) {
                mData.add(new DistrictItem(lr,
                    SerPermisAction.check(gymWrapper.shop_id(), PermissionServerUtils.LOCKER_SETTING_CAN_DELETE) || SerPermisAction.check(
                        gymWrapper.shop_id(), PermissionServerUtils.LOCKER_SETTING_CAN_CHANGE)));
            }
        }
        mAdatper.notifyDataSetChanged();
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}