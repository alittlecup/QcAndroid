package cn.qingchengfit.staffkit.views.wardrobe.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.EditWardrobeBody;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.wardrobe.choose.ChooseRegionFragment;
import cn.qingchengfit.staffkit.views.wardrobe.main.WardrobeMainFragment;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import javax.inject.Inject;
import rx.functions.Action1;

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
public class WardrobeEditFragment extends BaseFragment implements WardrobeEditPresenter.MVPView {

    @Inject WardrobeEditPresenter presenter;
    @BindView(R.id.wardrobe_no) CommonInputView wardrobeNo;
    @BindView(R.id.wardrobe_district) CommonInputView wardrobeDistrict;
    @BindView(R.id.permisson_dendy) View permissonDendy;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private Locker mLocker;
    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            showLoading();
            presenter.completeWardrobe(App.staffId, mLocker.id, new EditWardrobeBody.Builder().name(wardrobeNo.getContent())
                //                    .region_id()
                .build());
            return true;
        }
    };

    public static WardrobeEditFragment newInstance(Locker locker) {

        Bundle args = new Bundle();
        args.putParcelable("l", locker);
        WardrobeEditFragment fragment = new WardrobeEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocker = getArguments().getParcelable("l");
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_wardrobe, container, false);
        unbinder = ButterKnife.bind(this, view);
        //
        delegatePresenter(presenter, this);
        //        CommonInputView wardrobeNo = (CommonInputView) view.findViewById(R.id.wardrobe_no);
        //        final CommonInputView wardrobeDistrict = (CommonInputView) view.findViewById(R.id.wardrobe_district);
        boolean permissionChg = SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.LOCKER_SETTING_CAN_CHANGE);
        mCallbackActivity.setToolbar("编辑更衣柜", false, null, permissionChg ? R.menu.menu_compelete : 0, listener);
        permissonDendy.setVisibility(permissionChg ? View.GONE : View.VISIBLE);
        wardrobeNo.setContent(mLocker.name);
        wardrobeDistrict.setContent(mLocker.region.name);
        wardrobeDistrict.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), ChooseRegionFragment.newInstance())
                    .addToBackStack(getFragmentName())
                    .commit();
            }
        });
        RxBusAdd(LockerRegion.class).subscribe(new Action1<LockerRegion>() {
            @Override public void call(LockerRegion lockerRegion) {
                wardrobeDistrict.setContent(lockerRegion.name);
            }
        });

        return view;
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        wardrobeNo.setContent(mLocker.name);
        wardrobeDistrict.setContent(mLocker.region.name);
    }

    @Override public String getFragmentName() {
        return WardrobeEditFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.wardrobe_district, R.id.btn_del, R.id.permisson_dendy }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wardrobe_district://选择区域
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), ChooseRegionFragment.newInstance())
                    .addToBackStack(getFragmentName())
                    .commit();
                break;
            case R.id.btn_del:
                if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.LOCKER_SETTING_CAN_DELETE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return;
                }

                new MaterialDialog.Builder(getContext()).content(getString(R.string.alert_del_wradrobe, mLocker.name))
                    .positiveText(R.string.common_comfirm)
                    .canceledOnTouchOutside(true)
                    .negativeText(R.string.pickerview_cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showLoading();
                            presenter.delWardrobe(App.staffId, mLocker.id);
                        }
                    })
                    .show();

                break;
            case R.id.permisson_dendy:
                showAlert("您没有编辑更衣柜权限");
                break;
        }
    }

    @Override public void onSaveOk() {
        hideLoading();
        getFragmentManager().popBackStack(WardrobeMainFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override public void onDelOk() {
        hideLoading();
        getFragmentManager().popBackStack(WardrobeMainFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override public void onShowError(String e) {
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
