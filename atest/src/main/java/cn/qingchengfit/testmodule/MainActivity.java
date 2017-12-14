package cn.qingchengfit.testmodule;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import cn.qingchengfit.testmodule.databinding.ActivityMainBinding;
import cn.qingchengfit.views.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new MyApplication(getApplication());
//        Constant.setAccountType(BuildConfig.DEBUG ? 12162 : 12165);
//        Constant.setSdkAppid(BuildConfig.DEBUG ? 1400029014 : 1400029022);
//        Constant.setXiaomiPushAppid(BuildConfig.DEBUG ? "2882303761517418101" : "2882303761517418101");
//        Constant.setBussId(BuildConfig.DEBUG ? 611 : 605);
//        Constant.setXiaomiPushAppkey("5361741818101");
//        Constant.setHuaweiBussId(612);
//        LoginProcessor loginProcessor = null;
//        try {
//            loginProcessor = new LoginProcessor(getApplicationContext(), "qctest_7060",
//                "cloudtest.qingchengfit.cn", this);
//            loginProcessor.sientInstall();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        final BaseRouter baseRouter = new BaseRouter();
//        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe();
//        baseRouter.registeRouter("recruit", RecruitActivity.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        findViewById(R.id.btn_to_recruit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("student://student/home/student");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_to_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("student://student/home/student");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_design).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, DesignActivity.class));
            }
        });
    }
}

