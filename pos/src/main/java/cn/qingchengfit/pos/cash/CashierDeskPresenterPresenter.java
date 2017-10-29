package cn.qingchengfit.pos.cash;

import android.support.annotation.IntRange;
import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

public class CashierDeskPresenterPresenter extends BasePresenter {
  private MVPView view;

  @Inject GymWrapper gymWrapper;

  @Inject public CashierDeskPresenterPresenter() {
  }

  float total = 0;
  float current = 0;
  List<Float> history = new ArrayList<>();
  String cmdLine = "";
  public void pressNum(@IntRange(from = 0,to = 9) int num){
    current = current*10 + ((float) num/100);
    showView();
  }

  public void pressAdd(){
    if (current == 0)
      return;
    float tmp = current;
    history.add(tmp);
    cmdLine = getCmd().concat("+");
    current = 0;
    showView();
  }

  public void pressBackSpace(){
    if (current != 0) {
      current = current / 10;
      if (current < 0.01)
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
    view.showTotal(getTotal() + current);
    view.showCurrent(cmdLine+ (current == 0?"":formatFloat2Dot(current)));
  }

  private float getTotal(){
    float ret = 0;
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
        ss.append(formatFloat2Dot(history.get(i)));
        if (i < history.size() -1)
          ss.append("+");
      }
    }
    return ss.toString();
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
