package cn.qingchengfit.staffkit.views.student.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import rx.functions.Action1;

/**
 * Created by fb on 2017/3/6.
 */
public class AttendanceActivity extends BaseActivity implements FragCallBack {

	Toolbar toolbar;

	CheckBox rbSelectAll;
	TextView toolbarTitile;
	ImageView down;
	LinearLayout titileLayout;
	EditText searchviewEt;
	ImageView searchviewClear;
	Button searchviewCancle;
	LinearLayout searchview;
	RelativeLayout toolbarLayout;
	FrameLayout activityAttendanceFrag;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      rbSelectAll = (CheckBox) findViewById(R.id.rb_select_all);
      toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
      down = (ImageView) findViewById(R.id.down);
      titileLayout = (LinearLayout) findViewById(R.id.titile_layout);
      searchviewEt = (EditText) findViewById(R.id.searchview_et);
      searchviewClear = (ImageView) findViewById(R.id.searchview_clear);
      searchviewCancle = (Button) findViewById(R.id.searchview_cancle);
      searchview = (LinearLayout) findViewById(R.id.searchview);
      toolbarLayout = (RelativeLayout) findViewById(R.id.toolbar_layout);
      activityAttendanceFrag = (FrameLayout) findViewById(R.id.activity_attendance_frag);

      initToolbar();

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_attendance_frag, new AttendanceHomeFragment()).commit();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                onBackPressed();
            }
        });

        if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup) {
            ((ViewGroup) toolbar.getParent()).setPadding(0,
                MeasureUtils.getStatusBarHeight(getBaseContext()), 0, 0);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override public int getFragId() {
        return R.id.activity_attendance_frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        setBar(bean);
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

    public void toDetail(View view) {

    }
}
