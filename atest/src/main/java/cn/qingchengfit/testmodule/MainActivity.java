package cn.qingchengfit.testmodule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.views.activity.BaseAcitivity;

public class MainActivity extends BaseAcitivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BaseRouter baseRouter = new BaseRouter();
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
}
