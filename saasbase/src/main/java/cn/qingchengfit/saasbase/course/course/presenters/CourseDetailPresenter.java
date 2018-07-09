package cn.qingchengfit.saasbase.course.course.presenters;

import android.content.Context;
import android.support.annotation.StringRes;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.Trainer;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.course.course.bean.TeacherImpression;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CourseDetailPresenter extends BasePresenter<CourseDetailPresenter.CourseDetailView> {

  @Inject IPermissionModel serPermisAction;
  @Inject ICourseModel courseModel;
  @Inject GymWrapper gymWrapper;

  @Inject public CourseDetailPresenter() {
  }

  public void queryDetail(String courseid) {
    RxRegiste(courseModel.qcGetCourseDetail(courseid)
      .observeOn(AndroidSchedulers.mainThread())
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .subscribe(qcResponseCourseDetail -> {
        if (ResponseConstant.checkSuccess(qcResponseCourseDetail)) {
          if (qcResponseCourseDetail.data != null
            && qcResponseCourseDetail.data.courseDetail != null) {
            mvpView.setScore(qcResponseCourseDetail.data.courseDetail.getTeacher_score(),
              qcResponseCourseDetail.data.courseDetail.getCourse_score(),
              qcResponseCourseDetail.data.courseDetail.getService_score());
            mvpView.setJacket(qcResponseCourseDetail.data.courseDetail.getPhotos());
            mvpView.setImpress(qcResponseCourseDetail.data.courseDetail.getImpressions());
            mvpView.setCourseInfo(qcResponseCourseDetail.data.courseDetail);
            mvpView.setCourseDescripe(qcResponseCourseDetail.data.courseDetail.getDescription());
            mvpView.setTeachers(qcResponseCourseDetail.data.courseDetail.getTeachers());
          }
        } else {
          mvpView.onDelfailed(qcResponseCourseDetail.getMsg());
        }
      }, new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
          mvpView.onDelfailed("server error!");
        }
      }));
  }

  /**
   * 判定权限
   */
  public boolean hasAllEditPermission(CourseType mCourseDetail) {
    if (gymWrapper.inBrand()) {
      if (mCourseDetail.is_private() && !serPermisAction.check(
        PermissionServerUtils.PRISETTING_CAN_CHANGE, mCourseDetail.getShopIdList())
        || !mCourseDetail.is_private() && !serPermisAction.check(
        PermissionServerUtils.TEAMSETTING_CAN_CHANGE, mCourseDetail.getShopIdList())) {
        mvpView.showDelFailed(R.string.alert_edit_course_all_permission);
        return false;
      }
    } else {
      if (mCourseDetail.getShops().size() == 1) {
        if (mCourseDetail.is_private() && !serPermisAction.check(
          PermissionServerUtils.PRISETTING_CAN_CHANGE)
          || !mCourseDetail.is_private() && !serPermisAction.check(
          PermissionServerUtils.TEAMSETTING_CAN_CHANGE)) {
          mvpView.showDelFailed(R.string.alert_permission_forbid);
          return false;
        }
      } else if (gymWrapper.singleMode()) {
        mvpView.showDelFailed(R.string.alert_edit_course_all_permission);
      } else {
        mvpView.showDelFailed("此课程种类适用于多个场馆，请在【连锁运营】里进行编辑");
        return false;
      }
    }

    return true;
  }

  public void delCourse(String courseid) {
    RxRegiste(courseModel
      .qcDelCourse(courseid)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<QcResponse>() {
        @Override public void call(QcResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mvpView.onDelSuccess();
          } else {
            mvpView.onDelfailed(qcResponse.getMsg());
          }
        }
      }));
  }

  public void judgeDel(CourseType courseDetail, int shopcount,Context context) {
    if (gymWrapper.inBrand()) {
      if (courseDetail.is_private() && serPermisAction.check(
        PermissionServerUtils.PRISETTING_CAN_DELETE, courseDetail.getShopIdList())
        || !courseDetail.is_private() && serPermisAction.check(
        PermissionServerUtils.TEAMSETTING_CAN_DELETE, courseDetail.getShopIdList())) {
        mvpView.showDelDialog(
          String.format(Locale.CHINA, context.getString(R.string.alert_del_course), shopcount));
      } else {
        mvpView.showDelFailed(
          String.format(Locale.CHINA, context.getString(R.string.alert_del_course_forbid),
            shopcount));
      }
    } else {
      if (shopcount == 1) {
        if (courseDetail.is_private() && serPermisAction.check(
          PermissionServerUtils.PRISETTING_CAN_DELETE)
          || !courseDetail.is_private() && serPermisAction.check(
          PermissionServerUtils.TEAMSETTING_CAN_DELETE)) {
          mvpView.showDelDialog("删除后，已有的排期和课程预约都不会受到影响");
        } else {
          mvpView.showDelFailed("抱歉!您没有该功能权限");
        }
      } else if (gymWrapper.singleMode()) {
        mvpView.showDelFailed(
          String.format(Locale.CHINA, context.getString(R.string.alert_del_course_forbid),
            shopcount));
      } else {
        mvpView.showDelFailed("该课程适用于多家场馆,请到【连锁运营】里进行删除");
      }
    }
  }

  public interface CourseDetailView extends CView {
    /**
     *
     */
    void setCourseInfo(CourseType course);

    void setJacket(List<String> photos);

    void setScore(float teacherScore, float courseScore, float serverScore);

    /**
     * @param teachers all teacher of this course
     */
    void setTeachers(List<Trainer> teachers);

    void setImpress(List<TeacherImpression> impresssions);

    /**
     * course descripe
     *
     * @param descripe html
     */
    void setCourseDescripe(String descripe);

    /**
     * check if has permission
     */
    void showDelDialog(String content);

    void showDelFailed(String content);

    void showDelFailed(@StringRes int content);

    void onDelSuccess();

    void onDelfailed(String content);
  }
}