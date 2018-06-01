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



import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
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

	CommonInputView text;
    @Inject DistrictAddPresenter presenter;
	TextView del;
	LinearLayout layoutDel;
	View noPermission;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
	Toolbar toolbar;
	TextView toolbarTitile;
	FrameLayout toolbarLayout;

    private LockerRegion mLockerRegion;
    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            if (mLockerRegion == null) {//新增
                presenter.addRegion(App.staffId, text.getContent());
            } else { //编辑
                if (!serPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.LOCKER_SETTING_CAN_CHANGE)) {
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
      text = (CommonInputView) view.findViewById(R.id.text);
      del = (TextView) view.findViewById(R.id.del);
      layoutDel = (LinearLayout) view.findViewById(R.id.layout_del);
      noPermission = (View) view.findViewById(R.id.no_permission);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
      view.findViewById(R.id.no_permission).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onNoPermission();
        }
      });
      view.findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          DistrictAddFragment.this.onClick();
        }
      });

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
                serPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.LOCKER_SETTING_CAN_CHANGE) ? View.GONE
                    : View.VISIBLE);
            layoutDel.setVisibility(
                serPermisAction.check(gymWrapper.shop_id(), PermissionServerUtils.LOCKER_SETTING_CAN_DELETE) ? View.VISIBLE : View.GONE);
            text.setContent(mLockerRegion.name);
        }
    }

 public void onNoPermission() {
        showAlert(R.string.alert_permission_forbid);
    }

    @Override public String getFragmentName() {
        return DistrictAddFragment.class.getName();
    }

    @Override public void onDestroyView() {
        AppUtils.hideKeyboard(getActivity());
        super.onDestroyView();
    }

 public void onClick() {
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
