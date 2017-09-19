package cn.qingchengfit.staffkit.views.setting;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.main.SettingPresenter;
import cn.qingchengfit.staffkit.views.main.SettingView;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;
import rx.functions.Action1;

public class SettingActivity extends BaseActivity implements FragCallBack, SettingView {

  public static final String ACTION = "action";
    public static final int ACTION_FIX_SELF_INFO = 1;
    @Inject SettingPresenter presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        //component = ((App) getApplication()).getAppCompoent().plus(new GymMoudle(
        //        new CoachService(),
        //        new Brand(),
        //        new GymStatus(false)
        //));
        //component.inject(this);

        presenter.attachView(this);
        presenter.getSelfInfo();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        presenter.unattachView();
    }

    @Override public int getFragId() {
        return R.id.frag;
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

    @Override public void onSelfInfo(Staff bean) {
        getSupportFragmentManager().beginTransaction().replace(getFragId(), FixSelfInfoFragment.newInstance(bean, true)).commit();
    }
}
