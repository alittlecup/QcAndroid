package cn.qingchengfit.staffkit.train;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.train.fragment.MemberOperationFragment;
import cn.qingchengfit.staffkit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import rx.functions.Action1;

/**
 * Created by fb on 2017/3/21.
 */

public class SignUpChooseActivity extends BaseActivity implements FragCallBack {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_choose);

        getSupportFragmentManager().beginTransaction().replace(getFragId(), new MemberOperationFragment()).commit();
    }

    @Override public int getFragId() {
        return R.id.sign_up_choose_frag;
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
