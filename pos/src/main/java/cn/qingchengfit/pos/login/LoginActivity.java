package cn.qingchengfit.pos.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.pos.PosBaseActivity;
import cn.qingchengfit.pos.R;
import com.anbillon.flabellum.annotations.Trunk;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by fb on 2017/10/10.
 */

@Trunk(fragments = {LoginFragment.class})
public class LoginActivity extends PosBaseActivity implements HasSupportFragmentInjector{

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_hold,R.anim.slide_hold)
        .replace(R.id.frag_login,new LoginFragment())
        .commit();
  }


  @Override public String getModuleName() {
    return "login";
  }

}
