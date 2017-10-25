package cn.qingchengfit.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import cn.qingchengfit.testmodule.R;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.CommonInputView;

/**
 * Created by fb on 2017/10/25.
 */

public class CommonInputActivity extends BaseActivity{

  private CommonInputView input;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_input);
    input = (CommonInputView) findViewById(R.id.input_click_test);

    input.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        DialogUtils.showAlert(CommonInputActivity.this, "点击效果");
      }
    });
  }
}
