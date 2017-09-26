package cn.qingchengfit.saasbase.cards.cardtypes.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import javax.inject.Inject;

public class CardListPresenter extends BasePresenter {
  private MVPView view;

  @Inject GymWrapper gymWrapper;

  @Inject public CardListPresenter() {
  }

  public void queryAllCards(){

  }




  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {

  }
}
