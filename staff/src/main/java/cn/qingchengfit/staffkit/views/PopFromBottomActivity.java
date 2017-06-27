package cn.qingchengfit.staffkit.views;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Router;
import cn.qingchengfit.staffkit.views.gym.upgrate.UpgrateGymFragment;
import cn.qingchengfit.views.activity.BaseActivity;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

public class PopFromBottomActivity extends BaseActivity implements FragCallBack {

    @BindView(cn.qingchengfit.staffkit.R.id.toolbar) Toolbar toolbar;
    @BindView(cn.qingchengfit.staffkit.R.id.toolbar_title) TextView toolbarTitile;

    @Inject GymWrapper gymWrapper;
    private Observable<ToolbarBean> mObToolbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.qingchengfit.staffkit.R.layout.activity_pop_from_bottom);
        ButterKnife.bind(this);
        mObToolbar = RxBus.getBus().register(ToolbarBean.class);
        mObToolbar.subscribe(new Action1<ToolbarBean>() {
            @Override public void call(ToolbarBean toolbarBean) {
                toolbar.setNavigationIcon(R.drawable.ic_cross_blace);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        finish();
                        overridePendingTransition(R.anim.slide_hold, R.anim.slide_bottom_out);
                    }
                });
                toolbarTitile.setText(toolbarBean.title);
                toolbar.inflateMenu(toolbarBean.menu);
                toolbar.setOnMenuItemClickListener(toolbarBean.listener);
            }
        });

        switch (getRouter()) {

            case Router.BOTTOM_RENEWAL:
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag, new UpgrateGymFragment()).commit();
                break;
        }
    }

    @Override protected void onDestroy() {
        RxBus.getBus().unregister(ToolbarBean.class.getName(), mObToolbar);
        super.onDestroy();
    }

    @Override public int getFragId() {
        return 0;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {

    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {

    }
}
