//package cn.qingchengfit.staffkit.views.staff;
//
//import android.os.Bundle;
//import android.support.annotation.MenuRes;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.qingchengfit.model.others.ToolbarBean;
//import cn.qingchengfit.saasbase.staff.views.StaffListFragment;
//import cn.qingchengfit.staffkit.App;
//import cn.qingchengfit.staffkit.R;
//import cn.qingchengfit.staffkit.views.gym.coach.CoachListFragment;
//import cn.qingchengfit.staffkit.views.gym.site.SiteListFragment;
//import cn.qingchengfit.views.FragCallBack;
//import cn.qingchengfit.views.activity.BaseActivity;
//import cn.qingchengfit.views.fragments.BaseFragment;
//import com.jakewharton.rxbinding.widget.RxTextView;
//import rx.Subscription;
//import rx.functions.Action1;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 16/8/11.
// */
//public class StaffManageActivity extends BaseActivity implements FragCallBack {
//    public static final int STAFF = 1;
//    public static final int COACH = 2;
//    public static final int SITE = 3;
//    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
//
//    @BindView(R.id.toolbar) Toolbar toolbar;
//    @BindView(R.id.toolbar_title) TextView toolbarTitile;
//    @BindView(R.id.down) ImageView down;
//    @BindView(R.id.titile_layout) LinearLayout titileLayout;
//    @BindView(R.id.searchview_et) EditText searchviewEt;
//    @BindView(R.id.searchview) LinearLayout searchview;
//    private Subscription spSearch;
//
//    @Override protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_frag);
//        ButterKnife.bind(this);
//        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//        Fragment fragment = null;
//        switch (getIntent().getIntExtra("to", 0)) {
//            case STAFF:
//                fragment = new StaffListFragment();
//                break;
//            case COACH:
//                fragment = new CoachListFragment();
//                break;
//            case SITE:
//                fragment = new SiteListFragment();
//                break;
//            default:
//                break;
//        }
//        if (fragment != null) getSupportFragmentManager().beginTransaction().replace(R.id.frag, fragment).commit();
//    }
//
//    @Override public int getFragId() {
//        return R.id.frag;
//    }
//
//    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
//        Toolbar.OnMenuItemClickListener listener) {
//        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
//        setBar(bean);
//    }
//
//    @Override public void cleanToolbar() {
//
//    }
//
//    @OnClick({ R.id.searchview_clear, R.id.searchview_cancle }) public void onSearch(View v) {
//        switch (v.getId()) {
//            case R.id.searchview_clear:
//                searchviewEt.setText("");
//                break;
//            case R.id.searchview_cancle:
//                hideSearch();
//                break;
//        }
//    }
//
//    public void hideSearch() {
//        searchviewEt.setText("");
//        searchview.setVisibility(View.GONE);
//        if (spSearch != null) spSearch.unsubscribe();
//    }
//
//    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {
//        searchviewEt.setHint(hint);
//        searchview.setVisibility(View.VISIBLE);
//        spSearch = RxTextView.textChanges(searchviewEt).subscribe(action1);
//    }
//
//    @Override public void onChangeFragment(BaseFragment fragment) {
//
//    }
//
//    @Override public void setBar(ToolbarBean bar) {
//        toolbarTitile.setText(bar.title);
//        down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
//        if (bar.onClickListener != null) {
//            titileLayout.setOnClickListener(bar.onClickListener);
//        } else {
//            titileLayout.setOnClickListener(null);
//        }
//        toolbar.getMenu().clear();
//        if (bar.menu != 0) {
//            toolbar.inflateMenu(bar.menu);
//            toolbar.setOnMenuItemClickListener(bar.listener);
//        }
//    }
//}
