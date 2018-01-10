package cn.qingchengfit.weex.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import cn.qingchengfit.weex.R;
import cn.qingchengfit.weex.utils.WeexLoadView;

/**
 * Activity展示页，同时也是新打开的js文件默认页
 */
public class WxPageActivity extends AppCompatActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.weex_container);
    String url = getIntent().getStringExtra("url");
    Uri data=Uri.parse(url);
    new WeexLoadView().loadUri(data,this, (ViewGroup) findViewById(R.id.container));
  }

}
