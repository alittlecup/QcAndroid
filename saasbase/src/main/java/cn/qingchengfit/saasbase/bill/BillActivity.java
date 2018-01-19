package cn.qingchengfit.saasbase.bill;

import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.bill.filter.BillFilterFragment;
import cn.qingchengfit.saasbase.bill.view.BillDetailFragment;
import cn.qingchengfit.saasbase.bill.view.PayRequestListFragment;
import com.anbillon.flabellum.annotations.Trunk;

/**
 * Created by fb on 2017/10/13.
 */

@Trunk(fragments = {
    BillHomeFragment.class, PayRequestListFragment.class, BillFilterFragment.class,
    BillDetailFragment.class,
}) public class BillActivity extends SaasContainerActivity {

  @Override public String getModuleName() {
    return "bill";
  }
}
