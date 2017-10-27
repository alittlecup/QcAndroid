package cn.qingchengfit.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.testmodule.R;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.TipDialogFragment;
import cn.qingchengfit.views.fragments.TipTextDialogFragment;

/**
 * Created by fb on 2017/10/25.
 */

public class AlertActivity extends BaseActivity {

  @BindView(R.id.btn_dialog_1) Button btnDialog1;
  @BindView(R.id.btn_dialog_2) Button btnDialog2;
  @BindView(R.id.btn_dialog_3) Button btnDialog3;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dialog);
    ButterKnife.bind(this);

    btnDialog1.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        TipTextDialogFragment.newInstance("仅超级管理员才有权限进行此操作", "知道了", "提示小标题")
            .show(getSupportFragmentManager(), null);
      }
    });

    btnDialog2.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        TipDialogFragment.newInstance("操作成功", "知道了", R.drawable.vd_default_jobfair)
            .show(getSupportFragmentManager(), null);
      }
    });

  }
}
