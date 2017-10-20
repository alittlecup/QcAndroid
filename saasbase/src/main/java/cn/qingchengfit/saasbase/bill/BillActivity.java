package cn.qingchengfit.saasbase.bill;


import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.bill.view.PayRequestListFragment;
import com.anbillon.flabellum.annotations.Trunk;

/**
 * Created by fb on 2017/10/13.
 */

@Trunk(fragments = { BillHomeFragment.class, PayRequestListFragment.class }) public class BillActivity extends
  SaasContainerActivity {

  //@Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
  //@Inject RouterCenter routerCenter;
  //@BindView(R2.id.web_frag_layout) FrameLayout webFragLayout;
  //
  //@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
  //  super.onCreate(savedInstanceState);
  //  setContentView(R.layout.activity_base_frag);
  //  ButterKnife.bind(this);
  //  webFragLayout.setOnTouchListener(new View.OnTouchListener() {
  //    @Override public boolean onTouch(View v, MotionEvent event) {
  //      return true;
  //    }
  //  });
  //  //routerCenter.registe(iBill);
  //  onNewIntent(getIntent());
  //}

  //@Override protected Fragment getRouterFragment(Intent intent) {
  //  return routerCenter.getFragment(intent.getData(), intent.getBundleExtra("b"));
  //}

  @Override public String getModuleName() {
    return "bill";
  }


}
