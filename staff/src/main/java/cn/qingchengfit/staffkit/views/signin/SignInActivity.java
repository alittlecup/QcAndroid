package cn.qingchengfit.staffkit.views.signin;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.model.responese.SignInUrl;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.signin.config.SigninConfigListFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by yangming on 16/8/30.
 */
public class SignInActivity extends BaseActivity implements FragCallBack, SignInPresenter.SignInView {

    public static Integer checkinWithLocker = 1;
    public static String checkin_url = "";
    public static String checkout_url = "";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.student_frag) FrameLayout studentFrag;
    @BindView(R.id.down) ImageView down;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SignInPresenter presenter;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_toolbar);

        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.onNewSps();

        presenter.getSignInConfig();
        presenter.getSigninUrl();
        toolbarLayout.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                AppUtils.hideKeyboard(SignInActivity.this);
                onBackPressed();
            }
        });
        if (getIntent() != null && getIntent().getAction() != null) {
            if (getIntent().getAction().equalsIgnoreCase(getString(R.string.qc_action))) {
                String path = getIntent().getData().toString();
                if (path.contains("checkin/confirm")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, SignInHomeFragment.newInstance()).commit();
                } else if (path.contains("checkin/detail")) {
                    getSupportFragmentManager().beginTransaction().replace(getFragId(), new SignInLogFragment()).commit();
                } else if (path.contains("checkout/confirm")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, SignInHomeFragment.newInstance(1)).commit();
                } else if (path.contains("checkout/detail")) {
                    getSupportFragmentManager().beginTransaction().replace(getFragId(), new SignInLogFragment()).commit();
                } else if (path.contains("checkin/config")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, new SigninConfigListFragment()).commit();
                }
                return;
            }
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, SignInHomeFragment.newInstance()).commit();
    }

    @Override protected void onDestroy() {
        AppUtils.hideKeyboard(this);
        super.onDestroy();
        presenter.unattachView();
    }

    @Override public int getFragId() {
        return R.id.student_frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        toolbarTitile.setText(title);
        down.setVisibility(showRight ? View.VISIBLE : View.GONE);
        if (title != null) {
            toolbarTitile.setOnClickListener(titleClick);
        } else {
            toolbarTitile.setOnClickListener(null);
        }
        toolbar.getMenu().clear();
        if (menu != 0) {
            toolbar.inflateMenu(menu);
            toolbar.setOnMenuItemClickListener(listener);
        }
    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {
        toolbarTitile.setText(bar.title);
        down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
        if (bar.onClickListener != null) {
            titileLayout.setOnClickListener(bar.onClickListener);
        } else {
            titileLayout.setOnClickListener(null);
        }
        toolbar.getMenu().clear();
        if (bar.menu != 0) {
            toolbar.inflateMenu(bar.menu);
            toolbar.setOnMenuItemClickListener(bar.listener);
        }
    }

    @Override public void onGetSignInConfig(List<SignInConfig.Config> signInConfig) {
        if (signInConfig.get(0).getValue() instanceof Double) {
            checkinWithLocker = signInConfig.get(0).getValueInt();
        }
    }

    @Override public void onGetSignInUrl(SignInUrl signInUrl) {
        checkin_url = signInUrl.data.getCheckin_url();
        checkout_url = signInUrl.data.getCheckout_url();
    }

    @Override public void onFail() {

    }
}
