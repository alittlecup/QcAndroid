package cn.qingchengfit.staffkit.views.student.detail;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Provider;
import rx.functions.Action1;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/19 2016.
 */
public class StudentsDetailActivity extends BaseActivity implements FragCallBack, HasSupportFragmentInjector {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    LinkedList<ToolbarBean> toolbarList = new LinkedList<>();
    @Inject Provider<StudentWrapperComponent.Builder> requestComponentProvider;
    @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;
    @Inject StudentWrapper studentWrapper;
    private int statusTOTab = -1;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        ButterKnife.bind(this);
        StudentBean studentBean = getIntent().getParcelableExtra("student");
        if (studentBean != null) {
            studentWrapper.setStudentBean(studentBean);
        }
        if (getIntent().hasExtra("status_to_tab")) {
            statusTOTab = getIntent().getIntExtra("status_to_tab", -1);
        }
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        StudentHomeFragment studentHomeFragment = new StudentHomeFragment();
        studentHomeFragment.statusToTab = statusTOTab;

        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
            .replace(getFragId(), studentHomeFragment)
            .commit();
    }

    @Override protected void onDestroy() {
        studentWrapper.setStudentBean(null);
        super.onDestroy();
    }

    @Override public int getFragId() {
        return R.id.frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        toolbarList.add(bean);
        setBar(bean);
    }

    @Override public void cleanToolbar() {
        toolbarList.clear();
    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    public void setBar(ToolbarBean bar) {
        toolbarTitile.setText(bar.title);
        down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
        if (bar.onClickListener != null) {
            toolbarTitile.setOnClickListener(bar.onClickListener);
        } else {
            toolbarTitile.setOnClickListener(null);
        }
        toolbar.getMenu().clear();
        if (bar.menu != 0) {
            toolbar.inflateMenu(bar.menu);
            toolbar.setOnMenuItemClickListener(bar.listener);
        }
    }

    @Override public void onChangeFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(getFragId(), fragment).addToBackStack(fragment.getFragmentName());
        transaction.commit();
    }

    @Override public void onBackPressed() {
        if (toolbarList.size() > 1) {
            toolbarList.removeLast();
            setBar(toolbarList.getLast());
        }

        super.onBackPressed();
    }

    @Override public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }
}
