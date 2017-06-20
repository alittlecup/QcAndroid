package cn.qingchengfit.testmodule;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.views.activity.BaseAcitivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.qcloud.sdk.Constant;
import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.ui.qcchat.LoginProcessor;
import tencent.tls.platform.TLSErrInfo;

public class MainActivity extends BaseAcitivity implements LoginProcessor.OnLoginListener{

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MyApplication(getApplication());
        Constant.setAccountType(BuildConfig.DEBUG ? 12162 : 12165);
        Constant.setSdkAppid(BuildConfig.DEBUG ? 1400029014 : 1400029022);
        Constant.setXiaomiPushAppid(BuildConfig.DEBUG ? "2882303761517418101" : "2882303761517418101");
        Constant.setBussId(BuildConfig.DEBUG ? 611 : 605);
        Constant.setXiaomiPushAppkey("5361741818101");
        Constant.setHuaweiBussId(612);
        LoginProcessor loginProcessor = null;
        try {
            loginProcessor = new LoginProcessor(getApplicationContext(),
                "qctext_7060", "cloudtest.qingchengfit.cn", this);
            loginProcessor.sientInstall();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final BaseRouter baseRouter = new BaseRouter();
        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe();
        baseRouter.registeRouter("recruit", RecruitActivity.class);
        findViewById(R.id.btn_to_recruit).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                baseRouter.routerTo("recruit", MainActivity.this);
            }
        });
        findViewById(R.id.btn_to_test).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });
    }

    @Override public void onLoginSuccess() {

    }

    @Override public void onLoginFailed(TLSErrInfo tlsErrInfo) {

    }
}
