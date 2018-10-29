package cn.qingchengfit.wxpreview.old.newa;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.wxpreview.routers.WxpreviewRouterCenter;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = {
    WxPmViewPage.class,WxMiniProgramPage.class
})
public class WxMiniActivity extends SaasCommonActivity {
  @Override public String getModuleName() {
    return "wxmini";
  }
@Inject WxpreviewRouterCenter wxpreviewRouterCenter;
  @Override protected Fragment getRouterFragment(Intent intent) {
    return wxpreviewRouterCenter.getFragment(intent.getData(), intent.getExtras());
  }
}
