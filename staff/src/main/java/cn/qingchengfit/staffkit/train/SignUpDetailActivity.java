package cn.qingchengfit.staffkit.train;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.train.fragment.SignUpFormHomeFragment;
import cn.qingchengfit.staffkit.train.fragment.TrainChooseGymFragment;
import cn.qingchengfit.staffkit.train.moudle.TrainIds;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by fb on 2017/3/22.
 */

public class SignUpDetailActivity extends BaseActivity implements FragCallBack {

    @Inject TrainIds trainIds;
    private String gymid;
    private String competitionId;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_detail);

        Intent to = getIntent();
        if (to != null && !TextUtils.isEmpty(to.getData().getHost())) {
            if (to.getData().getPath().contains("select_shop")) {
              LogUtil.e("select_shop");
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_hold,R.anim.slide_hold).replace(getFragId(), new TrainChooseGymFragment()).commit();
            } else if (to.getData().getPath().contains("form")) {
              LogUtil.e("form");
                gymid = to.getData().getQueryParameter("gym_id");
                competitionId = to.getData().getQueryParameter("id");
                trainIds.setGymId(gymid);
                trainIds.setCompetitionId(competitionId);
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_hold,R.anim.slide_hold).replace(getFragId(), new SignUpFormHomeFragment()).commit();
            }
        } else {
            gymid = getIntent().getStringExtra(Configs.EXTRA_GYM_ID);
            trainIds.setGymId(gymid);
            getSupportFragmentManager().beginTransaction().replace(getFragId(), new SignUpFormHomeFragment()).commit();
        }
    }

    @Override public int getFragId() {
        return R.id.sign_up_detail_frag;
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
