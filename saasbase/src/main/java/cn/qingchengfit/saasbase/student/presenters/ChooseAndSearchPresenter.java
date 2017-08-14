package cn.qingchengfit.saasbase.student.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import javax.inject.Inject;

public class ChooseAndSearchPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  private MVPView view;

  @Inject public ChooseAndSearchPresenter() {
  }

  public void getAllStudents(){

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
