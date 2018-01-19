package cn.qingchengfit.recruit.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitConstants;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.views.activity.BaseActivity;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/5/27.
 */

public class RecruitActivity extends BaseActivity implements HasSupportFragmentInjector {
  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
  @Inject RecruitRouter router;
  @Inject LoginStatus loginStatus;
  @Inject BaseRouter baseRouter;
  @Inject QcRestRepository restRepository;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recruit);
    onNewIntent(getIntent());
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    String to = "";
    String id = null;
    String page = "";

    if (intent.getData() != null && !TextUtils.isEmpty(intent.getData().getHost())) {
      to = intent.getData().getHost();
      if ("job".equals(to)) {
        id = intent.getData().getPath().replace("/", "");
      } else if ("resume".equals(to)) {
        id = intent.getData().getQueryParameter("id");
      } else if ("recruit".equals(to)) {
        id = intent.getData().getQueryParameter("gym_id");
        page = intent.getData().getPath().toLowerCase();
      }
    } else if (!TextUtils.isEmpty(intent.getStringExtra("action"))) {
      to = intent.getStringExtra("action");
    }

    switch (to) {
      case "resume":
        if (TextUtils.isEmpty(id)) {
          router.resumeHome();
        } else {
          BaseRouter.routerToWeb(restRepository.getHost() + RecruitConstants.RESUME_WEB_PATH + id,
            this);
          this.finish();
          return;
        }
        break;
      case "job":
        if (TextUtils.isEmpty(id)) {
          router.home();
        } else {
          router.initJobDetail(new Job.Builder().id(id).build());
        }
        break;
      case "fair":
        break;
      default:
        if (page.contains("employer/gym")) {
          router.notificaitonGymDetail(id);
        } else {
          router.home();
        }
        break;
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == BaseRouter.RESULT_LOGIN) {
        onNewIntent(getIntent());
      }
    } else {
      if (requestCode == BaseRouter.RESULT_LOGIN) {
        this.finish();
      }
    }
  }

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }

  @IdRes
  @Override public int getFragId() {
    return cn.qingchengfit.recruit.R.id.web_frag_layout;
  }

  public void init(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
      .setCustomAnimations(cn.qingchengfit.widgets.R.anim.slide_hold,
        cn.qingchengfit.widgets.R.anim.slide_hold)
      .replace(getFragId(), fragment)
      .commitAllowingStateLoss();
  }

  public void add(Fragment s) {
    getSupportFragmentManager().beginTransaction()
      .setCustomAnimations(cn.qingchengfit.widgets.R.anim.slide_right_in,
        cn.qingchengfit.widgets.R.anim.slide_left_out, cn.qingchengfit.widgets.R.anim.slide_left_in,
        cn.qingchengfit.widgets.R.anim.slide_right_out)
      .add(getFragId(), s)
      .addToBackStack(null)
      .commitAllowingStateLoss();
  }

  public void addNoStack(Fragment s) {
    getSupportFragmentManager().popBackStackImmediate();
    getSupportFragmentManager().beginTransaction()
      .setCustomAnimations(cn.qingchengfit.widgets.R.anim.slide_right_in,
        cn.qingchengfit.widgets.R.anim.slide_left_out, cn.qingchengfit.widgets.R.anim.slide_left_in,
        cn.qingchengfit.widgets.R.anim.slide_right_out)
      .add(getFragId(), s)
      .commitAllowingStateLoss();
  }

  public void remove(Fragment fragment) {
    getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
  }
}
