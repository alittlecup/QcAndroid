package cn.qingchengfit.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.ShareBean;
import cn.qingchengfit.testmodule.R;
import cn.qingchengfit.views.ShareDialogWithExtendFragment;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;

/**
 * Created by fb on 2017/10/25.
 */

public class BottomActivity extends BaseActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.btn_bottom_list) Button btnBottomList;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bottom);
    ButterKnife.bind(this);
    initToolbar();
    initView();
  }

  private void initToolbar() {
    toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    toolbarTitle.setText("Bottom List");
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        ShareDialogWithExtendFragment.newInstance(new ShareBean())
            .show(getSupportFragmentManager(), "");
        return false;
      }
    });
  }

  private void initView(){
    btnBottomList.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ChoosePictureFragmentNewDialog.newInstance().show(getSupportFragmentManager(), null);
      }
    });
  }

}
