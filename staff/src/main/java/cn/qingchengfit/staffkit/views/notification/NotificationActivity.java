package cn.qingchengfit.staffkit.views.notification;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.notification.page.NotificationFragment;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;
import rx.functions.Action1;

public class NotificationActivity extends BaseActivity implements FragCallBack {


    @Inject StaffRespository mRestRepository;
    @Inject GymWrapper gymWrapper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportFragmentManager().beginTransaction()
            .replace(getFragId(), NotificationFragment.newInstance(getIntent().getStringExtra("type")))
            .commit();
    }

    @Override protected void onDestroy() {
        gymWrapper.setCoachService(null);
        super.onDestroy();
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
}
