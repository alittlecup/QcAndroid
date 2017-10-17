package cn.qingchengfit.pos.bill;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.pos.PosBaseActivity;
import cn.qingchengfit.pos.R;
import com.anbillon.flabellum.annotations.Trunk;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by fb on 2017/10/13.
 */

@Trunk(fragments = { FragmentBillHome.class, }) public class BillActivity extends PosBaseActivity
    implements HasSupportFragmentInjector {


  @BindView(R.id.web_frag_layout) FrameLayout webFragLayout;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base_frag);
    ButterKnife.bind(this);
    webFragLayout.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    //routerCenter.registe(iBill);
    onNewIntent(getIntent());
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return new Fragment();
    //return routerCenter.getFragment(intent.getData(), intent.getBundleExtra("b"));
  }

  @Override public String getModuleName() {
    return "bill";
  }


}
