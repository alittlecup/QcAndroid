package cn.qingchengfit.writeoff;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.writeoff.routers.WriteoffRouterCenter;
import cn.qingchengfit.writeoff.view.detail.WriteOffTicketPage;
import cn.qingchengfit.writeoff.view.list.WriteOffListPage;
import cn.qingchengfit.writeoff.view.verify.WriteOffCheckPage;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = {
    WriteOffTicketPage.class, WriteOffListPage.class, WriteOffCheckPage.class
}) public class WriteOffActivity extends SaasCommonActivity {
  @Inject WriteoffRouterCenter routerCenter;

  @Override public String getModuleName() {
    return "writeoff";
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getExtras());
  }
}
