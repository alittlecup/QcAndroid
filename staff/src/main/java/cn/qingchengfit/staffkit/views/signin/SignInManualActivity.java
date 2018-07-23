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




import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.signin.in.SignInManualFragment;
import cn.qingchengfit.staffkit.views.signin.out.SignOutManualFragment;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * 手动签到
 * Created by yangming on 16/8/29.
 */
public class SignInManualActivity extends BaseActivity implements FragCallBack {

	Toolbar toolbar;
	TextView toolbarTitile;
	ImageView down;
	LinearLayout titileLayout;
	EditText searchviewEt;
	ImageView searchviewClear;
	Button searchviewCancle;
	LinearLayout searchview;
	RelativeLayout toolbarLayout;
	FrameLayout studentFrag;
    @Inject SignInManualFragment signInManualFragment;
    @Inject SignOutManualFragment signOutManualFragment;
    @Inject StudentWrap studentWrapper;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_toolbar);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
      down = (ImageView) findViewById(R.id.down);
      titileLayout = (LinearLayout) findViewById(R.id.titile_layout);
      searchviewEt = (EditText) findViewById(R.id.searchview_et);
      searchviewClear = (ImageView) findViewById(R.id.searchview_clear);
      searchviewCancle = (Button) findViewById(R.id.searchview_cancle);
      searchview = (LinearLayout) findViewById(R.id.searchview);
      toolbarLayout = (RelativeLayout) findViewById(R.id.toolbar_layout);
      studentFrag = (FrameLayout) findViewById(R.id.student_frag);

      StudentBean studentBean = getIntent().getParcelableExtra("student");

        boolean isSignIn = getIntent().getBooleanExtra("isSignIn", true);

        initToolbar(toolbar);
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
