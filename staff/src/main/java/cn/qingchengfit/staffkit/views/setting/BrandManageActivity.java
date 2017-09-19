package cn.qingchengfit.staffkit.views.setting;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.setting.brand.BrandDetailFragment;
import cn.qingchengfit.staffkit.views.setting.brand.BrandManageFragment;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.LinkedList;
import rx.functions.Action1;

public class BrandManageActivity extends BaseActivity implements FragCallBack {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
    LinkedList<ToolbarBean> toolbarList = new LinkedList<>();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_manage);
        ButterKnife.bind(this);
        //component = DaggerBrandManageComponent.builder().appComponent(((App)getApplication()).getAppCompoent()).build();
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent() != null && getIntent().getParcelableExtra(Configs.EXTRA_BRAND) != null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag, BrandDetailFragment.newInstance((Brand) getIntent().getParcelableExtra(Configs.EXTRA_BRAND)))
                .commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new BrandManageFragment()).commit();
        }
    }

    @Override public int getFragId() {
        return R.id.frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        //        toolbarList.add(bean);
        setBar(bean);
    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    public void setBar(ToolbarBean bar) {
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

    @Override public void onBackPressed() {
        //        if (toolbarList.size() > 1) {
        //            toolbarList.removeLast();
        //            setBar(toolbarList.getLast());
        //        }

        super.onBackPressed();
    }
}
