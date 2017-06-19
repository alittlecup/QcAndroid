package cn.qingchengfit.staffkit.views.allotsales;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.MenuRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.inject.commpont.StaffWrapperComponent;
import cn.qingchengfit.inject.model.StaffWrapper;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.AllotSaleEvent;
import cn.qingchengfit.staffkit.rxbus.event.AllotSaleSelectAllEvent;
import cn.qingchengfit.staffkit.views.BaseActivity;
import cn.qingchengfit.staffkit.views.FragCallBack;
import cn.qingchengfit.staffkit.views.custom.MyDrawerLayout;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterFragment;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterFragmentBuilder;
import cn.qingchengfit.utils.MeasureUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AllotSalesActivity extends BaseActivity implements FragCallBack, DrawerLayout.DrawerListener {

    public StaffWrapperComponent component;

    public StaffWrapper staffWrapper;
    @BindView(R.id.drawer) public MyDrawerLayout drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;
    @BindView(R.id.student_frag) FrameLayout studentFrag;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.rb_select_all) CheckBox selectAll;
    @BindView(R.id.frame_student_filter) FrameLayout filterFrameLayout;

    Observable<AllotSaleEvent> observable;
    TextView tvCancel;

    StudentFilterFragment filterFragment;
    //SaleDetailFragment saleDetailFragment;
    //SalesListFragment salesListFragment;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allot_sales);
        tvCancel = new TextView(getApplicationContext());
        ButterKnife.bind(this);
        staffWrapper = new StaffWrapper();
        initDI();
        initToolBar();

        if (getIntent() != null && getIntent().getIntExtra("type", 0) != 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, new SaleDetailFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, new SalesListFragment()).commit();
        }

        selectAll.setButtonDrawable(R.drawable.radio_selector_allotsale_white);

        setFilterFragment();

        observable = RxBus.getBus().register(AllotSaleEvent.class);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Action1<AllotSaleEvent>() {
            @Override public void call(AllotSaleEvent allotSaleEvent) {
                if (allotSaleEvent.isSearching) {
                    selectAll.setClickable(false);
                } else {
                    toolbar.setNavigationIcon(null);
                    selectAll.setVisibility(View.VISIBLE);
                    selectAll.setClickable(true);
                    selectAll.setChecked(false);
                    selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            RxBus.getBus().post(new AllotSaleSelectAllEvent(b));
                        }
                    });
                }
            }
        });
    }

    public void removeFilterFragment() {
        filterFrameLayout.setVisibility(View.GONE);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void setFilterFragment() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        filterFrameLayout.setVisibility(View.VISIBLE);
        drawer.addDrawerListener(this);
        filterFragment = new StudentFilterFragmentBuilder(1).build();
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out)
            .replace(R.id.frame_student_filter, filterFragment)
            .commit();
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        selectAll.setVisibility(View.GONE);
        initToolBar();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        RxBus.getBus().unregister(AllotSaleEvent.class.getName(), observable);
    }

    private void initDI() {
        //component = ((App) getApplication()).getAppCompoent().plus(new StaffWrapperMoudle(staffWrapper));
        //component.inject(this);
    }

    private void initToolBar() {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void initTextToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(null);
        tvCancel.setVisibility(View.VISIBLE);
        tvCancel.setText("取消");
        tvCancel.setTextSize(MeasureUtils.dpToPx(4.8f, getApplicationContext().getResources()));
        tvCancel.setTextColor(getApplicationContext().getResources().getColor(android.R.color.white));
        tvCancel.setGravity(GravityCompat.START);
        if (toolbar.getChildCount() == 1) {
            toolbar.addView(tvCancel);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                view.setVisibility(View.GONE);
                initToolBar();
                onBackPressed();
            }
        });
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        if (tvCancel != null) {
                            tvCancel.setVisibility(View.GONE);
                        }
                    }
                }, 200);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override public int getFragId() {
        return R.id.student_frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        setBar(bean);
    }

    @Override public void cleanToolbar() {
        selectAll.setVisibility(View.GONE);
        initToolBar();
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
            toolbar.setOnMenuItemClickListener(bar.listener == null ? null : bar.listener);
        }
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return false;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {
        Timber.e("onDrawerSlide");
    }

    @Override public void onDrawerOpened(View drawerView) {
        Timber.e("onDrawerOpened");
    }

    @Override public void onDrawerClosed(View drawerView) {
        Timber.e("onDrawerSlide");
    }

    @Override public void onDrawerStateChanged(int newState) {
        Timber.e("onDrawerStateChanged:" + newState);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
