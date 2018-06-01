package cn.qingchengfit.staffkit.views.student.followup;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.inject.model.StaffWrapper;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.EventRouter;
import cn.qingchengfit.staffkit.rxbus.event.FollowUpStatisticsToDetail;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;
import rx.Observable;
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
 * //Created by yangming on 16/12/5.
 */
public class FollowUpActivity extends BaseActivity implements FragCallBack {

    public final static int TO_NEW_ADD = 1;

    //@BindView(R.id.toolbar) Toolbar toolbar;
    //@BindView(R.id.rb_select_all) CheckBox rbSelectAll;
    //@BindView(R.id.toolbar_title) TextView toolbarTitile;
    //@BindView(R.id.down) ImageView down;
    //@BindView(R.id.titile_layout) LinearLayout titileLayout;
    //
    //@BindView(R.id.searchview_et) EditText searchviewEt;
    //@BindView(R.id.searchview_clear) ImageView searchviewClear;
    //@BindView(R.id.searchview_cancle) Button searchviewCancle;
    //@BindView(R.id.searchview) LinearLayout searchview;
	DrawerLayout drawerLayout;
	FrameLayout frag;

    @Inject RestRepository mRestRepository;

    private Action1<CharSequence> searchAction;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override public void afterTextChanged(Editable s) {
            searchAction.call(s);
        }
    };
    private StaffWrapper staffWrapper;
    private Observable<EventRouter> obRouter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_follow_up);
      drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      frag = (FrameLayout) findViewById(R.id.frag);

      initView();
        initToolBar();
    }

    private void initView() {
        staffWrapper = new StaffWrapper();
        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra("router"))) {
            route(getIntent().getStringExtra("router"), false);
        } else {
            route(RouterFollowUp.MAIN, false);
        }
        //先锁住侧滑栏
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override protected void onResume() {
        super.onResume();
        obRouter = RxBus.getBus().register(EventRouter.class);
        obRouter.subscribe(new Action1<EventRouter>() {
            @Override public void call(EventRouter eventRouter) {
                route(eventRouter.getPath(), true);
            }
        });
    }

    private void route(String p, boolean stack) {
        Fragment f = new Fragment();
        switch (p.toLowerCase()) {
            case RouterFollowUp.MAIN:
                f = new FollowUpFragment();
                break;
            case RouterFollowUp.NEW_REGISTE_ALL:
                FollowUpStatusFragment followUpStatusFragment0 = new FollowUpStatusFragment();
                followUpStatusFragment0.studentStatus = 0;
                f = followUpStatusFragment0;
                break;
            case RouterFollowUp.FOLLOWUPING_ALL:
                FollowUpStatusFragment followUpStatusFragment1 = new FollowUpStatusFragment();

                followUpStatusFragment1.studentStatus = 1;
                f = followUpStatusFragment1;
                break;
            case RouterFollowUp.FOLLOWUPING_TODAY:
                FollowUpStatusFragment followUpStatusFragment2 = new FollowUpStatusFragment();
                followUpStatusFragment2.studentStatus = 1;
                f = followUpStatusFragment2;
                break;
            case RouterFollowUp.STUDENT_CHART:
            case RouterFollowUp.FOLLOWUPING_CHART:
            case RouterFollowUp.NEW_REGISTE_CHART:
                FollowUpDataStatisticsFragment followUpDataStatisticsFragment = new FollowUpDataStatisticsFragment();
                f = followUpDataStatisticsFragment;
                break;
            case RouterFollowUp.NEW_REGISTE_TODAY:
                FollowUpStatusFragment followUpStatusFragment3 = new FollowUpStatusFragment();

                followUpStatusFragment3.studentStatus = 0;
                f = followUpStatusFragment3;
                break;
            case RouterFollowUp.STUDENT_ALL:
                FollowUpStatusFragment followUpStatusFragment4 = new FollowUpStatusFragment();

                followUpStatusFragment4.studentStatus = 2;
                f = followUpStatusFragment4;
                break;
            case RouterFollowUp.STUDENT_TODAY:
                FollowUpStatusFragment followUpStatusFragment5 = new FollowUpStatusFragment();

                followUpStatusFragment5.studentStatus = 2;
                f = followUpStatusFragment5;
                break;

            case RouterFollowUp.TRANSFER:
                f = new FollowUpDataTransferFragment();
                break;
            case RouterFollowUp.DRAWER_OPEN:
                drawerLayout.openDrawer(GravityCompat.END);
                return;
            case RouterFollowUp.DRAWER_CLOSE:
                drawerLayout.closeDrawers();
                return;
        }

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();

        if (stack) {
            tr.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_hold, R.anim.slide_hold, R.anim.slide_right_out);
            tr.add(getFragId(), f);
            tr.addToBackStack(null);
        } else {
            tr.setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold, R.anim.slide_hold, R.anim.slide_hold);
            tr.replace(getFragId(), f);
        }
        tr.commitAllowingStateLoss();
    }

    @Override public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    public void setUpDrawer(Fragment fragment) {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_drawer, fragment).commit();
    }

    public void disableDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override protected void onPause() {
        super.onPause();
        RxBus.getBus().unregister(EventRouter.class.getName(), obRouter);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    public void initToolBar() {
        //toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        //    @Override public void onClick(View v) {
        //        onBackPressed();
        //    }
        //});
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
        //searchview.setVisibility(View.GONE);
    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {
        //searchAction = action1;
        //searchview.setVisibility(View.VISIBLE);
        ////searchviewEt.requestFocus();
        //searchviewEt.setFocusable(true);
        //AppUtils.showKeyboard(this, searchviewEt);
        //setUpSeachView();
    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {
        //searchview.setVisibility(View.GONE);
        //toolbarTitile.setText(bar.title);
        //down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
        //if (bar.onClickListener != null) {
        //    titileLayout.setOnClickListener(bar.onClickListener);
        //} else {
        //    titileLayout.setOnClickListener(null);
        //}
        //toolbar.getMenu().clear();
        //if (bar.menu != 0) {
        //    toolbar.inflateMenu(bar.menu);
        //    toolbar.setOnMenuItemClickListener(bar.listener == null ? null : bar.listener);
        //}
    }

    //搜索栏清除按钮
    //@OnClick(R.id.searchview_clear)
    public void onClear() {
        //searchviewEt.setText("");
    }

    //取消搜索
    //@OnClick(R.id.searchview_cancle)
    public void onClickCancel() {
        //searchviewEt.removeTextChangedListener(textWatcher);
        //if (searchviewEt.getText().toString().length() > 0) {
        //    searchviewEt.setText("");
        //}
        //AppUtils.hideKeyboard(this);
        //searchview.setVisibility(View.GONE);
        //if (searchAction != null) searchAction.call("qc_search_cancel");
    }

    /**
     * 初始化搜索组件
     */
    private void setUpSeachView() {
        //searchviewEt.addTextChangedListener(textWatcher);
    }

    public void toDetail(View view) {
        RxBus.getBus().post(new FollowUpStatisticsToDetail());
    }
}
