package cn.qingchengfit.staffkit.views.wardrobe.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
public class DistrictAddFragment extends BaseFragment implements DistrictAddPresenter.MVPView {

    @BindView(R.id.text) CommonInputView text;
    @Inject DistrictAddPresenter presenter;
    @BindView(R.id.del) TextView del;
    @BindView(R.id.layout_del) LinearLayout layoutDel;
    @BindView(R.id.no_permission) View noPermission;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;

    private LockerRegion mLockerRegion;
    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            if (mLockerRegion == null) {//新增
                presenter.addRegion(App.staffId, text.getContent());
            } else { //编辑
                if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.LOCKER_SETTING_CAN_CHANGE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return true;
                }

                presenter.editRegion(App.staffId, text.getContent(), mLockerRegion.id + "");
            }
            return true;
        }
    };

    public static DistrictAddFragment newInstance(LockerRegion lr) {

        Bundle args = new Bundle();
        args.putParcelable("l", lr);
        DistrictAddFragment fragment = new DistrictAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) mLockerRegion = getArguments().getParcelable("l");
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wardrobe_district_add, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(presenter, this);
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        if (mLockerRegion == null) {
            //新增区域
            toolbarTitile.setText("添加新区域");
            toolbar.inflateMenu(R.menu.menu_save);
            toolbar.setOnMenuItemClickListener(listener);
        } else {
            toolbarTitile.setText("编辑区域");
            toolbar.inflateMenu(R.menu.menu_save);
            toolbar.setOnMenuItemClickListener(listener);
            noPermission.setVisibility(
                SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.LOCKER_SETTING_CAN_CHANGE) ? View.GONE
                    : View.VISIBLE);
            layoutDel.setVisibility(
                SerPermisAction.check(gymWrapper.shop_id(), PermissionServerUtils.LOCKER_SETTING_CAN_DELETE) ? View.VISIBLE : View.GONE);
            text.setContent(mLockerRegion.name);
        }
    }

    @OnClick(R.id.no_permission) public void onNoPermission() {
        showAlert(R.string.alert_permission_forbid);
    }

    @Override public String getFragmentName() {
        return DistrictAddFragment.class.getName();
    }

    @Override public void onDestroyView() {
        AppUtils.hideKeyboard(getActivity());
        super.onDestroyView();
    }

    @OnClick(R.id.del) public void onClick() {
        //            showAlert(getString(R.string.titile_forbid_del_region), getString(R.string.alert_del_region_deny));
        if (mLockerRegion.has_used_lockers) {
            new MaterialDialog.Builder(getContext()).title(getString(R.string.titile_forbid_del_region))
                .content(R.string.alert_del_region)
                .positiveText(R.string.common_comfirm)
                .autoDismiss(true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //                            if (mLockerRegion != null)
                        //                                presenter.delRegion(App.staffId, mLockerRegion.id + "");
                    }
                })
                .show();
        } else {
            new MaterialDialog.Builder(getContext()).content("确定删除该区域?")
                .positiveText(R.string.common_comfirm)
                .negativeText(R.string.common_cancel)
                .autoDismiss(true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (mLockerRegion != null) presenter.delRegion(App.staffId, mLockerRegion.id + "");
                    }
                })
                .show();
        }
    }

    @Override public void onAddSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onEditSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onDelSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        hideLoading();
        onShowError(getString(e));
    }
}
