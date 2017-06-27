package cn.qingchengfit.staffkit.views.gym;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.AddBrandActivity;
import cn.qingchengfit.staffkit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

public class GymActivity extends BaseActivity implements ISetGymView, FragCallBack {

    public static final int GYM_DETAIL = 1;
    public static final int GYM_ADD = 2;
    public static final int GYM_GUIDE = 3;
    public static final int GYM_HISORY = 4;
    public static final int GYM_MORE = 5;
    public static final int GYM_INFO = 6;
    public static final String GYM_TO = "gym_to";
    public static final String GYM_ID = "gym_id";
    public static final String GYM_MODEL = "gym_model";
    public static final String GYM_TYPE = "gym_type";
    public static final String GYM_SINGLE = "gym_single";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.frag) FrameLayout frag;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
    @BindView(R.id.searchview_et) EditText searchviewEt;
    @BindView(R.id.searchview) LinearLayout searchview;
    LinkedList<ToolbarBean> toolbarList = new LinkedList<>();
    @Inject SetGymPresenter setGymPresenter;
    @Inject GymWrapper gymWrapper;
    private int to;
    private Subscription spSearch;

    public static void startTo(Activity activity, int to) {
        Intent it = new Intent(activity, GymActivity.class);
        it.putExtra(GYM_TO, to);
        activity.startActivity(it);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        ButterKnife.bind(this);
        //((App) getApplication()).getAppCompoent().inject(this);
        toolbarLayout.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        Fragment fragment;
        to = getIntent().getIntExtra(GYM_TO, 0);
        switch (to) {
            case GYM_DETAIL:
                fragment = new GymDetailFragment();
                break;
            case GYM_ADD:
                return;
            case GYM_GUIDE:
                Intent toBrands2 = new Intent(this, AddBrandActivity.class);
                toBrands2.putExtra(GYM_TO, GYM_GUIDE);
                startActivityForResult(toBrands2, 2);
                return;

            case GYM_HISORY:
                fragment = new RenewalHistoryFragment();
                break;
            case GYM_MORE:
                fragment = new GymMoreFragment();
                break;
            case GYM_INFO:
                //有权限或者超级管理员
                if (getIntent().getBooleanExtra("su", false) || (!TextUtils.isEmpty(gymWrapper.shop_id()) && SerPermisAction.check(
                    gymWrapper.shop_id(), PermissionServerUtils.STUDIO_LIST_CAN_WRITE))) {
                    fragment = GymInfoFragmentBuilder.newGymInfoFragment(gymWrapper.getCoachService());
                } else {
                    fragment = new GymInfoNoEditFragment();
                }
                break;
            default:
                fragment = new SetGymFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frag, fragment).commit();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

        } else {
            if (requestCode == 1) {
                finish();
            } else if (requestCode == 3) {
                finish();
            }
        }
    }

    @Override public void setBrand() {

    }

    @Override public void setHeader(String args) {

    }

    @Override public void setAddress(String args) {

    }

    @Override public void setPhone(String args) {

    }

    @Override public void setDesc(String args) {

    }

    @Override public void onBrandList(List<Brand> brand) {

    }

    @Override public void onCreatGym() {

    }

    @Override public void onSuccess(CoachService coachService) {

    }

    @Override public void onFailed() {

    }

    @Override public void onUpdateGym() {

    }

    @Override public int getFragId() {
        return R.id.frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        hideSearch();
        toolbarLayout.setVisibility(View.VISIBLE);
        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        toolbarList.add(bean);
        setBar(bean);
    }

    @Override public void cleanToolbar() {
        toolbarList.clear();
    }

    @OnClick({ R.id.searchview_clear, R.id.searchview_cancle }) public void onSearch(View v) {
        switch (v.getId()) {
            case R.id.searchview_clear:
                searchviewEt.setText("");
                break;
            case R.id.searchview_cancle:
                hideSearch();
                break;
        }
    }

    public void hideSearch() {
        searchviewEt.setText("");
        searchview.setVisibility(View.GONE);
        if (spSearch != null) spSearch.unsubscribe();
    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {
        searchviewEt.setHint(hint);
        searchview.setVisibility(View.VISIBLE);
        spSearch = RxTextView.textChanges(searchviewEt).subscribe(action1);
    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    public void setBar(ToolbarBean bar) {
        toolbarTitile.setText(bar.title);
        down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
        if (bar.onClickListener != null) {
            toolbarTitile.setOnClickListener(bar.onClickListener);
        } else {
            toolbarTitile.setOnClickListener(null);
        }
        toolbar.getMenu().clear();
        if (bar.menu != 0) {
            toolbar.inflateMenu(bar.menu);
            toolbar.setOnMenuItemClickListener(bar.listener);
        }
    }

    @Override public void onBackPressed() {
        //        if (toolbarList.size() ==2){
        //
        //        }
        if (toolbarList.size() > 1) {
            toolbarList.removeLast();
            setBar(toolbarList.getLast());
            toolbarLayout.setVisibility(View.VISIBLE);
        }

        searchview.setVisibility(View.GONE);
        super.onBackPressed();
    }

    public void hideToolbar() {
        toolbarLayout.setVisibility(View.GONE);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }
}
