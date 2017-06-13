package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.utils.ToastUtils;
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
 * Created by Paper on 2017/2/13.
 */
public class SinginConfigWardrobeFragment extends BaseFragment implements SigninConfigWradrobePresenter.MVPView {

    @BindView(R.id.swt_signin_config_locker) SwitcherLayout swtSigninConfigLocker;
    @BindView(R.id.swt_signin_config_locker_back) SwitcherLayout swtSigninConfigLockerBack;

    @Inject SigninConfigWradrobePresenter presenter;

    /**
     * GET -- GSON
     * 签到是否分配更衣柜
     */
    List<SignInConfig.Config> signInConfig = new ArrayList<>();
    /**
     * GET -- GSON
     * 签出是否自动归还更衣柜
     */
    List<SignInConfig.Config> signOutConfig = new ArrayList<>();
    @BindView(R.id.view_no_permission) View viewNoPermission;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_config_wardrobe, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);

        swtSigninConfigLocker.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swtSigninConfigLocker.setOpen(b);
                swtSigninConfigLockerBack.setVisibility(b ? View.VISIBLE : View.GONE);
                swtSigninConfigLockerBack.setOpen(b);
            }
        });
        presenter.getSignInConfig();
        presenter.getSignOutConfig();
        viewNoPermission.setVisibility(presenter.checkPermission() ? View.GONE : View.VISIBLE);
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("更衣柜联动设置");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (!presenter.checkPermission()) {
                    onClickNopermiss();
                    return true;
                }
                if (signInConfig != null && !signInConfig.isEmpty()) {
                    presenter.putCheckinWithLocker(signInConfig.get(0).getId() + "", swtSigninConfigLocker.isOpen());
                } else if (signOutConfig != null && !signOutConfig.isEmpty()) {
                    presenter.putCheckoutWithReturnLocker(signOutConfig.get(0).getId() + "", swtSigninConfigLockerBack.isOpen());
                }
                return true;
            }
        });
    }

    @Override public String getFragmentName() {
        return SinginConfigWardrobeFragment.class.getName();
    }

    @Override public void onGetSignInConfig(List<SignInConfig.Config> signInConfig) {
        this.signInConfig = signInConfig;
        if (signInConfig == null || signInConfig.isEmpty()) {
            ToastUtils.show("没有签到配置信息");
            return;
        }
        if (signInConfig.get(0).getValue() instanceof Number && signInConfig.get(0).getValueInt() == 1) {
            swtSigninConfigLocker.setOpen(true);
        } else {
            swtSigninConfigLocker.setOpen(false);
            swtSigninConfigLocker.setShowDivier(false);
        }
    }

    @OnClick(R.id.view_no_permission) void onClickNopermiss() {
        showAlert(R.string.sorry_for_no_permission);
    }

    @Override public void onGetSignOutConfig(List<SignInConfig.Config> signInConfig) {
        this.signOutConfig = signInConfig;
        if (signInConfig == null || signInConfig.isEmpty()) {
            ToastUtils.show("没有签出配置信息");
            return;
        }
        if (signInConfig.get(0).getValue() instanceof Number && signInConfig.get(0).getValueInt() == 1) {
            swtSigninConfigLockerBack.setOpen(true);
        } else {
            swtSigninConfigLockerBack.setOpen(false);
        }
    }

    @Override public void onCheckInConfigComplete() {
        presenter.putCheckoutWithReturnLocker(signOutConfig.get(0).getId() + "", swtSigninConfigLockerBack.isOpen());
        ToastUtils.show("设置成功");
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onCheckOutConfigComplete() {

    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
