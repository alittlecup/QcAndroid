package cn.qingchengfit.saasbase.course.course.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.course.course.network.body.CourseBody;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EditCoursePresenter extends BasePresenter<EditCoursePresenter.EditCourseView> {

  @Inject IPermissionModel serPermisAction;
  @Inject ICourseModel courseModel;

  @Inject public EditCoursePresenter() {
  }

  public void judgePermission(CourseType courseDetail) {
    //if (gymWrapper.inBrand()) {
    //    if ((courseDetail.is_private() && serPermisAction.checkMuti(PermissionServerUtils.PRISETTING_CAN_CHANGE,
    //        courseDetail.getShopIdList())) || (!courseDetail.is_private() && serPermisAction.checkMuti(
    //        PermissionServerUtils.TEAMSETTING_CAN_CHANGE, courseDetail.getShopIdList()))) { //连锁运营下 全权限
    //        mvpView.showBaseInfoHint(null);
    //        mvpView.editBaseInfo(courseDetail);
    //        mvpView.showSuitGymHint(null);
    //    } else {
    //        mvpView.showBaseInfoHint("仅具有全部适用场馆管理员身份的用户才能编辑");
    //        mvpView.showSuitGymHint(null);
    //        mvpView.showbaseInfo(courseDetail);//连锁运营下部分权限
    //    }
    //    mvpView.editSuitGym(courseDetail);
    //} else {//场馆下
    //    if (gymWrapper.singleMode()) { //但场馆模式
    //        mvpView.showBaseInfoHint(null);
    //        mvpView.editBaseInfo(courseDetail);
    //    } else {//多场馆进入
    //        if (courseDetail.getShopIdList().size() > 1) {
    //            mvpView.showBaseInfoHint("请在「连锁运营」中修改基本信息");
    //            mvpView.showbaseInfo(courseDetail);
    //        } else if ((courseDetail.is_private() && serPermisAction.check(courseDetail.getShopIdList().get(0),
    //            PermissionServerUtils.PRISETTING_CAN_CHANGE)) || (!courseDetail.is_private() && serPermisAction.check(
    //            courseDetail.getShopIdList().get(0), PermissionServerUtils.TEAMSETTING_CAN_CHANGE))) {
    //            mvpView.showBaseInfoHint(null);
    //            mvpView.editBaseInfo(courseDetail);
    //        } else {
    //            mvpView.showBaseInfoHint("抱歉!您没有修改权限");
    //            mvpView.showbaseInfo(courseDetail);
    //        }
    //    }
    //    mvpView.showSuitGymHint("请在「连锁运营」中修改所属场馆");
    //    mvpView.showSuitGym(courseDetail);
    //}
  }

  public void editCourse(String courseid, CourseBody body) {
    RxRegiste(courseModel.qcUpdateCourse(courseid, body)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<QcResponse>() {
        @Override public void call(QcResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mvpView.onSucceed();
          } else {
            mvpView.onFailed(qcResponse.getMsg());
          }
        }
      }, new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {

        }
      }));
  }

  public void editCourseShops( String courseid, String shopids) {
    //HashMap<String, Object> params = gymWrapper.getParams();
    //params.put("shop_ids", shopids);
    // TODO: 2017/11/30 shops ids 怎么传过去
    RxRegiste(courseModel
      .qcEditCourseShops(courseid)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<QcResponse>() {
        @Override public void call(QcResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mvpView.onSucceedShops();
          } else {
            mvpView.onFailed(qcResponse.getMsg());
          }
        }
      }, new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
          mvpView.onFailed(throwable.getMessage());
        }
      }));
  }

  public interface EditCourseView extends CView {
    void showBaseInfoHint(String hint);

    void showSuitGymHint(String hint);

    void editBaseInfo(CourseType courseDetail);

    void showbaseInfo(CourseType courseDetail);

    void editSuitGym(CourseType courseDetail);

    void showSuitGym(CourseType courseDetail);

    void onSucceed();

    void onFailed(String err);

    void onSucceedShops();
  }
}