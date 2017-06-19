package cn.qingchengfit.staffkit.views.signin;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.inject.commpont.StudentWrapperComponent;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.BaseActivity;
import cn.qingchengfit.staffkit.views.FragCallBack;
import cn.qingchengfit.staffkit.views.signin.in.SignInManualFragment;
import cn.qingchengfit.staffkit.views.signin.out.SignOutManualFragment;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * 手动签到
 * Created by yangming on 16/8/29.
 */
public class SignInManualActivity extends BaseActivity implements FragCallBack {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;
    @BindView(R.id.searchview_et) EditText searchviewEt;
    @BindView(R.id.searchview_clear) ImageView searchviewClear;
    @BindView(R.id.searchview_cancle) Button searchviewCancle;
    @BindView(R.id.searchview) LinearLayout searchview;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
    @BindView(R.id.student_frag) FrameLayout studentFrag;
    @Inject SignInManualFragment signInManualFragment;
    @Inject SignOutManualFragment signOutManualFragment;
    @Inject StudentWrapper studentWrapper;
    private StudentWrapperComponent component;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_toolbar);
        ButterKnife.bind(this);

        StudentBean studentBean = getIntent().getParcelableExtra("student");

        boolean isSignIn = getIntent().getBooleanExtra("isSignIn", true);

        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                onBackPressed();
            }
        });
        studentWrapper.setStudentBean(studentBean);

        if (isSignIn) {
            getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, signInManualFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, signOutManualFragment).commit();
        }
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
}
