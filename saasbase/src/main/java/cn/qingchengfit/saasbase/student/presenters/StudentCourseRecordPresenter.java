package cn.qingchengfit.saasbase.student.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import javax.inject.Inject;

public class StudentCourseRecordPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  private MVPView view;
  /**
   *  选择场馆的shop id
   */
  private String shopid;

  @Inject public StudentCourseRecordPresenter() {
  }

  public String getShopid() {
    return shopid;
  }

  public void setShopid(String shopid) {
    this.shopid = shopid;
    getAllRecords();
  }

  /**
   * 获取所有上课记录
   */
  public void getAllRecords(){

  }


  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onRecords();
  }
}
