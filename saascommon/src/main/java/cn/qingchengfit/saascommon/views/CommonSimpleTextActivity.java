package cn.qingchengfit.saascommon.views;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.saascommon.databinding.ActivityCommonSimpleTextBinding;
import cn.qingchengfit.utils.DrawableUtils;

public class CommonSimpleTextActivity extends SaasCommonActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityCommonSimpleTextBinding mBinding =
        DataBindingUtil.setContentView(this, R.layout.activity_common_simple_text);
    String content = getIntent().getStringExtra("content");
    TextView tvContent = findViewById(R.id.tv_content);
    tvContent.setText(content);
    mBinding.setToolbarModel(new ToolbarModel("提示"));
    Toolbar toolbar = mBinding.includeToolbar.toolbar;
    initToolbar(toolbar);
    toolbar.setNavigationIcon(
        DrawableUtils.tintDrawable(this, R.drawable.vd_linear_delete_black, R.color.bg_white));
  }
}
