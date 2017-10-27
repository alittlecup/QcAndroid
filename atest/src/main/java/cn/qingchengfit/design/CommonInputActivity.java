package cn.qingchengfit.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import cn.qingchengfit.testmodule.R;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Date;

/**
 * Created by fb on 2017/10/25.
 */

public class CommonInputActivity extends BaseActivity{

  private CommonInputView input;
  private CommonInputView inputTime;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_input);
    input = (CommonInputView) findViewById(R.id.input_click_test);
    inputTime = (CommonInputView) findViewById(R.id.common_input_time);

    input.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        DialogUtils.showAlert(CommonInputActivity.this, "点击效果");
      }
    });

    inputTime.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        TimeDialogWindow timeDialogWindow = new TimeDialogWindow(CommonInputActivity.this, TimePopupWindow.Type.YEAR_MONTH_DAY);
        timeDialogWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
          @Override public void onTimeSelect(Date date) {
            inputTime.setContent(DateUtils.Date2MMDDHHmm(date));
          }
        });
        timeDialogWindow.show();
      }
    });

  }
}
