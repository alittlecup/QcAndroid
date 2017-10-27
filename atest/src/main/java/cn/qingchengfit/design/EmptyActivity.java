package cn.qingchengfit.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.design.fragment.EmptyFragment;
import cn.qingchengfit.testmodule.R;
import cn.qingchengfit.views.activity.BaseActivity;

/**
 * Created by fb on 2017/10/24.
 */

public class EmptyActivity extends BaseActivity {

  @BindView(R.id.frag_empty) FrameLayout fragEmpty;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_empty);
    ButterKnife.bind(this);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.frag_empty, EmptyFragment.newInstance("空页面", "暂无数据", "空页面文案"))
        .commit();
  }
}
