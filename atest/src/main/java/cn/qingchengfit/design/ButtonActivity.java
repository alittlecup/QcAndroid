package cn.qingchengfit.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.testmodule.R;
import cn.qingchengfit.views.activity.BaseActivity;

/**
 * Created by fb on 2017/10/24.
 */

public class ButtonActivity extends BaseActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_button);
  }
}
