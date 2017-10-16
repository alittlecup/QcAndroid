package cn.qingchengfit.pos.bill.filter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/12.
 */

public class BillFilterPresenter extends BasePresenter {

  private MVPView view;

  @Inject
  public BillFilterPresenter(){

  }

  @Override public void attachView(PView v) {
    this.view = (MVPView)v;
  }

  public interface MVPView extends CView {
    void onGetAccount();
  }

}
