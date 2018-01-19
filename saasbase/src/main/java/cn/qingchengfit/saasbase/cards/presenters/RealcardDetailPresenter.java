package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import javax.inject.Inject;

public class RealcardDetailPresenter extends BasePresenter {
  private MVPView view;

  @Inject GymWrapper gymWrapper;

  @Inject public RealcardDetailPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onBindStudents(String s);
    void onSupportGyms(String s);
    void onConsumeHisotry(String s);
    void onRealCardNum(String no);
  }
}
