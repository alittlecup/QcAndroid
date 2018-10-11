package cn.qingchengfit.staffkit.views.student.score;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;
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

public class ScoreDetailActivity extends BaseActivity implements FragCallBack {

    //@Extra User_Student student;

	Toolbar toolbar;
	CheckBox rbSelectAll;
	TextView toolbarTitile;
	ImageView down;
	LinearLayout titileLayout;
	LinearLayout searchview;

	FrameLayout frag;

    @Inject ScoreDetailFragment scoreDetailFragment;



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

      initView();
        initToolBar();
    }



    private void initView() {
        getSupportFragmentManager().beginTransaction().replace(getFragId(), scoreDetailFragment).commit();
    }

    private void initToolBar() {
        if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup){
            ((ViewGroup) toolbar.getParent()).setPadding(0, MeasureUtils.getStatusBarHeight(this),0,0);
        }
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResult(RESULT_OK);
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

    @Override public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
