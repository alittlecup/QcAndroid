package cn.qingchengfit.design;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.NetWorkDialogEvent;
import cn.qingchengfit.testmodule.R;
import cn.qingchengfit.views.activity.BaseActivity;

/**
 * Created by fb on 2017/10/24.
 */

public class DesignActivity extends BaseActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.btn_alert) Button btnAlert;
  private Button btnColor;
  private Button btnText;
  private Button btnEmpty;
  private Button btnWidget;
  private Button btnCell;
  private Button btnBottom;
  private Button btnChooseItem;
  private Button btnSearch;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_design);
    ButterKnife.bind(this);
    initToolbar();
    btnColor = (Button) findViewById(R.id.btn_color);
    btnText = (Button) findViewById(R.id.btn_text);
    btnEmpty = (Button) findViewById(R.id.btn_empty);
    btnWidget = (Button) findViewById(R.id.btn_widget);
    btnCell = (Button) findViewById(R.id.btn_cell);
    btnBottom = (Button) findViewById(R.id.btn_bottom);
    btnChooseItem = (Button)findViewById(R.id.btn_choose);
    btnSearch = (Button) findViewById(R.id.btn_search);
    btnText.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(TextActivity.class);
      }
    });
    btnColor.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(ColorActivity.class);
      }
    });
    btnEmpty.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(EmptyActivity.class);
      }
    });
    btnWidget.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(ButtonActivity.class);
      }
    });
    btnCell.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(CommonInputActivity.class);
      }
    });
    btnBottom.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(BottomActivity.class);
      }
    });
    btnAlert.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(AlertActivity.class);
      }
    });
    btnChooseItem.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(ItemActivity.class);
      }
    });
    btnSearch.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(SearchActivity.class);
      }
    });
  }

  private void initToolbar() {
    toolbarTitle.setText("首页");
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_POST));
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_HIDE_DIALOG));
          }
        }, 3000);
        return false;
      }
    });
  }

  private void start(Class<? extends Activity> activity) {
    startActivity(new Intent(DesignActivity.this, activity));
  }
}
