package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;



import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
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

	SwitcherLayout swtSigninConfigLocker;
	SwitcherLayout swtSigninConfigLockerBack;

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
	View viewNoPermission;
	Toolbar toolbar;
	TextView toolbarTitile;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_config_wardrobe, container, false);
      swtSigninConfigLocker = (SwitcherLayout) view.findViewById(R.id.swt_signin_config_locker);
      swtSigninConfigLockerBack =
          (SwitcherLayout) view.findViewById(R.id.swt_signin_config_locker_back);
      viewNoPermission = (View) view.findViewById(R.id.view_no_permission);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.view_no_permission).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickNopermiss();
        }
      });

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
                List<Pair<String,Boolean>> fix = new ArrayList<>();
                if (signInConfig != null && !signInConfig.isEmpty()) {
                    fix.add(new Pair<String, Boolean>(signInConfig.get(0).getId()+"",swtSigninConfigLocker.isOpen()));
                }
                if (signOutConfig != null && !signOutConfig.isEmpty()) {
                    fix.add(new Pair<String, Boolean>(signOutConfig.get(0).getId()+"",swtSigninConfigLockerBack.isOpen()));
                }
                presenter.putCheckoutWithReturnLocker(fix);

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

 void onClickNopermiss() {
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
        ToastUtils.show("设置成功");
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onCheckOutConfigComplete() {
        ToastUtils.show("设置成功");
        hideLoading();
        getActivity().onBackPressed();
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
