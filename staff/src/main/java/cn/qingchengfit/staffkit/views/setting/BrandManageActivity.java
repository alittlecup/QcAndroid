package cn.qingchengfit.staffkit.views.setting;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.views.setting.brand.BrandDetailFragment;
import cn.qingchengfit.staffkit.views.setting.brand.BrandEditFragment;
import cn.qingchengfit.staffkit.views.setting.brand.BrandManageFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import rx.functions.Action1;

public class BrandManageActivity extends BaseActivity implements FragCallBack {

  Toolbar toolbar;
  TextView toolbarTitile;
  ImageView down;
  LinearLayout titileLayout;
  RelativeLayout toolbarLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_brand_manage);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
    down = (ImageView) findViewById(R.id.down);
    titileLayout = (LinearLayout) findViewById(R.id.titile_layout);
    toolbarLayout = (RelativeLayout) findViewById(R.id.toolbar_layout);

    toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
    toolbar.setNavigationOnClickListener(v -> onBackPressed());
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup) {
      ((ViewGroup) toolbar.getParent()).setPadding(0, MeasureUtils.getStatusBarHeight(this), 0, 0);
    }
    String to = getIntent().getStringExtra("to");
    if("edit".equals(to)){
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.frag, BrandEditFragment.newInstance(
              (Brand) getIntent().getParcelableExtra(Configs.EXTRA_BRAND)))
          .commit();
      return;
    }
    if (getIntent() != null && getIntent().getParcelableExtra(Configs.EXTRA_BRAND) != null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.frag, BrandDetailFragment.newInstance(
              (Brand) getIntent().getParcelableExtra(Configs.EXTRA_BRAND)))
          .commit();
    } else {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.frag, new BrandManageFragment())
          .commit();
    }
  }

  @Override public int getFragId() {
    return R.id.frag;
  }

  @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick,
      @MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {
    ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
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
}
