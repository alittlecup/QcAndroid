package cn.qingchengfit.pos.main;

import android.os.Bundle;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.views.activity.BaseActivity;

public class MainActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_hold,R.anim.slide_hold)
        .replace(R.id.frag_main,new PosMainFragment())
        .commit();
  }
}
