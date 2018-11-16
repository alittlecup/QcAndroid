package cn.qingchengfit.wxpreview.old.newa;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.wxpreview.R;
import cn.qingchengfit.wxpreview.old.CompletedConnectFragment;
import cn.qingchengfit.wxpreview.old.ConnectWechatFragment;
import cn.qingchengfit.wxpreview.old.HomePageQrCodeFragment;
import com.anbillon.flabellum.annotations.Trunk;

@Trunk(fragments = {
    ConnectWechatFragment.class, HomePageQrCodeFragment.class,CompletedConnectFragment.class
}) public class WxPreviewEmptyActivity extends SaasCommonActivity {
  /**
   * to 1-ConnectWechatFragment 2- HomePageQrCodeFragment
   */

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    webFragLayout.setBackgroundColor(Color.TRANSPARENT);
    int to = getIntent().getIntExtra("to", -1);
    if (to == 1) {
      ConnectWechatFragment fragment = new ConnectWechatFragment();
      fragment.setArguments(getIntent().getExtras());
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.web_frag_layout, fragment)
          .commit();
    } else if (to == 2) {
      HomePageQrCodeFragment homePageQrCodeFragment = new HomePageQrCodeFragment();
      homePageQrCodeFragment.setArguments(getIntent().getExtras());
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.web_frag_layout, homePageQrCodeFragment)
          .commit();
    }
  }

  public void toCompleteWxChat(Bundle bundle) {
    CompletedConnectFragment completedConnectFragment = new CompletedConnectFragment();
    completedConnectFragment.setArguments(bundle);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.web_frag_layout, completedConnectFragment)
        .addToBackStack("")
        .commit();
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
  }
}
