package cn.qingchengfit.pos.cash;

import android.support.annotation.IntRange;
import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponse;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponseWrap;
import cn.qingchengfit.saasbase.repository.IBillModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CashierDeskPresenterPresenter extends BasePresenter {
  private MVPView view;

  @Inject GymWrapper gymWrapper;
  @Inject IBillModel billModel;
  @Inject public CashierDeskPresenterPresenter() {
  }

  long total = 0;
  long current = 0;
  List<Long> history = new ArrayList<>();
  String cmdLine = "";
  public void pressNum(@IntRange(from = 0,to = 9) int num){
    current = current*10 + num;
    showView();
  }

  public void pressAdd(){
    if (current == 0)
      return;
    long tmp = current;
    history.add(tmp);
    cmdLine = getCmd().concat("+");
    current = 0;
    showView();
  }

  public void pressBackSpace(){
    if (current != 0) {
      current = current / 10;
      //BigDecimal   b  =   new BigDecimal(current);
      //current = b.setScale(2,BigDecimal.ROUND_DOWN).floatValue();
      if (current < 1)
        current = 0;
    }
    else {
      if (history.size() > 0){
        current = history.get(history.size() -1);
        history.remove(history.size() -1);
      }
    }
    cmdLine = TextUtils.isEmpty(getCmd())?"":getCmd().concat("+");
    showView();
  }

  public void pressClear(){
    history.clear();
    total = 0;
    current = 0;
    cmdLine = "";
    showView();
  }

  private void showView(){
    //float digital = new BigDecimal((getTotal() + current)/100f).setScale(2, RoundingMode.DOWN).floatValue();
    view.showTotal((getTotal() + current)/100f);
    view.showCurrent(cmdLine+ (current == 0?"":formatFloat2Dot(current/100f)));
  }

  private long getTotal(){
    long ret = 0;
    if (history != null){
      for (int i = 0; i < history.size(); i++) {
        ret += history.get(i);
      }
    }
    return ret;
  }

  private String formatFloat2Dot(float f){
    return String.format(Locale.CHINA, "%.2f", f);
  }

  private String getCmd(){
    StringBuilder ss = new StringBuilder();
    if (history != null){
      for (int i = 0; i < history.size(); i++) {
        ss.append(formatFloat2Dot(history.get(i)/100f));
        if (i < history.size() -1)
          ss.append("+");
      }
    }
    return ss.toString();
  }



  public void pay() {
    long amont = getTotal()+current;
    if (amont == 0){
      view.showAlert("请输入金额");
      return;
    }
    RxRegiste(billModel.directPay(amont)
          .onBackpressureLatest()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new NetSubscribe<QcDataResponse<PayBusinessResponseWrap>>() {
                @Override public void onNext(QcDataResponse<PayBusinessResponseWrap> qcResponse) {
                  if (ResponseConstant.checkSuccess(qcResponse)) {
                    view.onBusinessOrder(qcResponse.data.order);
                  } else {
                    view.onShowError(qcResponse.getMsg());
                  }
                }
              }));
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }


  public interface MVPView extends CView {
    void showTotal(float f);
    void showCurrent(String s);
    void onBusinessOrder(PayBusinessResponse payBusinessResponse);
  }
}
