package cn.qingchengfit.checkout.view.scan;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.checkout.repository.CheckoutRepository;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class QcScanActivityModel extends BaseViewModel {
  public final MutableLiveData<ScanResultBean> scanResult=new MutableLiveData<>();
  @Inject CheckoutRepository repository;
  @Inject QcScanActivityModel(){

  }
  public void scanPay(String barCode,String pollingNumber,String orderNumber){
    Map<String,Object> params=new HashMap<>();
    params.put("barcode",barCode);
    params.put("out_trade_no",pollingNumber);
    params.put("pay_trade_no",orderNumber);

    repository.qcPostScanOrder(scanResult,defaultResult,params);
  }


}
