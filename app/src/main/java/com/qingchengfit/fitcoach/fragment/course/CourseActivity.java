package com.qingchengfit.fitcoach.fragment.course;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.inject.commpont.DaggerStatementComponent;
import cn.qingchengfit.staffkit.inject.commpont.StatementComponent;
import cn.qingchengfit.staffkit.inject.moudle.GymMoudle;
import cn.qingchengfit.staffkit.inject.moudle.GymStatus;
import cn.qingchengfit.staffkit.model.bean.ToolbarBean;
import cn.qingchengfit.staffkit.usecase.bean.Brand;
import cn.qingchengfit.staffkit.usecase.bean.CoachService;
import cn.qingchengfit.staffkit.views.BaseActivity;
import cn.qingchengfit.staffkit.views.FragCallBack;
import rx.functions.Action1;

public class CourseActivity extends BaseActivity implements FragCallBack {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_titile)
    TextView toolbarTitile;
    @Bind(R.id.down)
    ImageView down;
    @Bind(R.id.titile_layout)
    LinearLayout titileLayout;
    @Bind(R.id.toolbar_layout)
    RelativeLayout toolbarLayout;

    private StatementComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        setSupportActionBar(toolbar);

        mComponent = DaggerStatementComponent.builder().appComponent(((App) getApplication()).getAppCompoent())
                .gymMoudle(new GymMoudle(
                        (CoachService) getIntent().getParcelableExtra(Configs.EXTRA_GYM_SERVICE),
                        (Brand) getIntent().getParcelableExtra(Configs.EXTRA_BRAND),
                        (GymStatus)getIntent().getParcelableExtra(Configs.EXTRA_GYM_STATUS)

                ))
                .build();
        mComponent.inject(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag, new CourseFragment())
                .commit();
    }

    public StatementComponent getComponent() {
        return mComponent;
    }

    @Override
    public int getFragId() {
        return R.id.frag;
    }

    @Override
    public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {
        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        setBar(bean);
    }



    @Override
    public void cleanToolbar() {

    }

    @Override
    public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override
    public void onChangeFragment(BaseFragment fragment) {

    }

    @Override
    public void setBar(ToolbarBean bar) {
        toolbarTitile.setText(bar.title);
        down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
        if (bar.onClickListener != null)
            titileLayout.setOnClickListener(bar.onClickListener);
        else titileLayout.setOnClickListener(null);
        toolbar.getMenu().clear();
        if (bar.menu != 0) {
            toolbar.inflateMenu(bar.menu);
            toolbar.setOnMenuItemClickListener(bar.listener);
        }
    }
}
