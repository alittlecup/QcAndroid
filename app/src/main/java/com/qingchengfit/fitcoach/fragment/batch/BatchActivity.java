package com.qingchengfit.fitcoach.fragment.batch;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.inject.commpont.DaggerGymComponent;
import cn.qingchengfit.staffkit.inject.commpont.GymComponent;
import cn.qingchengfit.staffkit.inject.moudle.GymMoudle;
import cn.qingchengfit.staffkit.inject.moudle.GymStatus;
import cn.qingchengfit.staffkit.model.bean.ToolbarBean;
import cn.qingchengfit.staffkit.usecase.bean.Brand;
import cn.qingchengfit.staffkit.usecase.bean.CoachService;
import cn.qingchengfit.staffkit.views.BaseActivity;
import cn.qingchengfit.staffkit.views.FragCallBack;
import rx.functions.Action1;

public class BatchActivity extends BaseActivity implements FragCallBack {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_titile)
    TextView toolbarTitile;
    @Bind(R.id.down)
    ImageView down;
    @Bind(R.id.titile_layout)
    LinearLayout titileLayout;
    private GymComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        component = DaggerGymComponent.builder()
                .appComponent(((App) getApplication()).getAppCompoent())
                .gymMoudle(new GymMoudle(
                        (CoachService) getIntent().getParcelableExtra(Configs.EXTRA_GYM_SERVICE),
                        (Brand) getIntent().getParcelableExtra(Configs.EXTRA_BRAND),
                        (GymStatus)getIntent().getParcelableExtra(Configs.EXTRA_GYM_STATUS)

                ))
                .build();
        getSupportFragmentManager().beginTransaction()
                .replace(getFragId(),new GymCoursesFragment())
                .commit();
    }

    @Override
    public int getFragId() {
        return R.id.frag;
    }

    @Override
    public GymComponent getComponent() {
        return component;
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
