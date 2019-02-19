package cn.qingcheng.gym;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingcheng.gym.pages.my.MyGymsPage;
import cn.qingchengfit.gym.routers.GymRouterCenter;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = {
    MyGymsPage.class
}) public class GymActivity extends SaasCommonActivity {
  @Inject GymRouterCenter routerCenter;

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getExtras());
  }

  @Override public String getModuleName() {
    return "gym";
  }
}
