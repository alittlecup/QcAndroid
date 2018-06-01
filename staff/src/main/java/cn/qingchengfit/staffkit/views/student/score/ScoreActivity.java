package cn.qingchengfit.staffkit.views.student.score;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.qingchengfit.inject.model.StaffWrapper;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import rx.functions.Action1;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/22.
 */

public class ScoreActivity extends BaseActivity implements FragCallBack {

	Toolbar toolbar;
	CheckBox rbSelectAll;
	TextView toolbarTitile;
	ImageView down;
	LinearLayout titileLayout;
	LinearLayout searchview;

	FrameLayout frag;

    private StaffWrapper staffWrapper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_score);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      rbSelectAll = (CheckBox) findViewById(R.id.rb_select_all);
      toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
      down = (ImageView) findViewById(R.id.down);
      titileLayout = (LinearLayout) findViewById(R.id.titile_layout);
      searchview = (LinearLayout) findViewById(R.id.searchview);
      frag = (FrameLayout) findViewById(R.id.frag);

      initDI();
        initView();
        initToolBar();
    }

    private void initDI() {
        staffWrapper = new StaffWrapper();
        //component = ((App)getApplication()).getAppCompoent().plus(new StaffWrapperMoudle(staffWrapper));
        //component.inject(this);
    }

    private void initView() {
        getSupportFragmentManager().beginTransaction().replace(getFragId(), new ScoreHomeFragment()).commit();
    }

    private void initToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override public int getFragId() {
        return R.id.frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        ToolbarBean toolbarBean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        setBar(toolbarBean);
    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {
        searchview.setVisibility(View.GONE);
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
            toolbar.setOnMenuItemClickListener(bar.listener == null ? null : bar.listener);
        }
    }
}
