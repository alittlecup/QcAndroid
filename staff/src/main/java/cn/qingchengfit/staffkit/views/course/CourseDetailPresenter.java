package cn.qingchengfit.staffkit.views.course;

import android.support.annotation.StringRes;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.CourseType;
import cn.qingchengfit.model.responese.CourseTypeResponse;
import cn.qingchengfit.model.responese.TeacherImpression;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.bean.CourseDetailTeacher;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/7/30.
 */
public class CourseDetailPresenter extends BasePresenter {

    @Inject GymWrapper gymWrapper;
    @Inject LoginStatus loginStatus;
    @Inject SerPermisAction serPermisAction;
    private RestRepository restRepository;
    private CourseDetailView view;

    @Inject public CourseDetailPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void attachView(PView v) {
        view = (CourseDetailView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
    }

    public void queryDetail(String courseid) {
        RxRegiste(restRepository.getGet_api()
            .qcGetCourseDetail(loginStatus.staff_id(), courseid, gymWrapper.getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<CourseTypeResponse>>() {
                @Override public void call(QcDataResponse<CourseTypeResponse> qcResponseCourseDetail) {
                    if (ResponseConstant.checkSuccess(qcResponseCourseDetail)) {
                        if (qcResponseCourseDetail.data != null && qcResponseCourseDetail.data.courseDetail != null) {
                            view.setScore(qcResponseCourseDetail.data.courseDetail.getTeacher_score(),
                                qcResponseCourseDetail.data.courseDetail.getCourse_score(),
                                qcResponseCourseDetail.data.courseDetail.getService_score());
                            view.setJacket(qcResponseCourseDetail.data.courseDetail.getPhotos());
                            view.setImpress(qcResponseCourseDetail.data.courseDetail.getImpressions());
                            view.setCourseInfo(qcResponseCourseDetail.data.courseDetail);
                            view.setCourseDescripe(qcResponseCourseDetail.data.courseDetail.getDescription());
                            view.setTeachers(qcResponseCourseDetail.data.courseDetail.getTeachers());
                        }
                    } else {
                        view.onDelfailed(qcResponseCourseDetail.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onDelfailed("server error!");
                }
            }));
    }

    /**
     * 判定权限
     */
    public boolean hasAllEditPermission(CourseType mCourseDetail) {
        if (gymWrapper.inBrand()) {
            if (mCourseDetail.is_private() && !serPermisAction.checkMuti(PermissionServerUtils.PRISETTING_CAN_CHANGE,
                mCourseDetail.getShopIdList()) || !mCourseDetail.is_private() && !serPermisAction.checkMuti(
                PermissionServerUtils.TEAMSETTING_CAN_CHANGE, mCourseDetail.getShopIdList())) {
                view.showDelFailed(R.string.alert_edit_course_all_permission);
                return false;
            }
        } else {
            if (mCourseDetail.getShops().size() == 1) {
                if (mCourseDetail.is_private() && !serPermisAction.check(mCourseDetail.getShops().get(0).id,
                    PermissionServerUtils.PRISETTING_CAN_CHANGE) || !mCourseDetail.is_private() && !serPermisAction.check(
                    mCourseDetail.getShops().get(0).id, PermissionServerUtils.TEAMSETTING_CAN_CHANGE)) {
                    view.showDelFailed(R.string.alert_permission_forbid);
                    return false;
                }
            } else if (gymWrapper.singleMode()) {
                view.showDelFailed(R.string.alert_edit_course_all_permission);
            } else {
                view.showDelFailed("此课程种类适用于多个场馆，请在【连锁运营】里进行编辑");
                return false;
            }
        }

        return true;
    }

    public void delCourse(String staffid, String courseid) {
        RxRegiste(restRepository.getPost_api()
            .qcDelCourse(staffid, courseid, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onDelSuccess();
                    } else {
                        view.onDelfailed(qcResponse.getMsg());
                    }
                }
            }));
    }

    public void judgeDel(CourseType courseDetail, int shopcount) {
        if (gymWrapper.inBrand()) {
            if (courseDetail.is_private() && serPermisAction.checkMuti(PermissionServerUtils.PRISETTING_CAN_DELETE,
                courseDetail.getShopIdList()) || !courseDetail.is_private() && serPermisAction.checkMuti(
                PermissionServerUtils.TEAMSETTING_CAN_DELETE, courseDetail.getShopIdList())) {
                view.showDelDialog(String.format(Locale.CHINA, App.context.getString(R.string.alert_del_course), shopcount));
            } else {
                view.showDelFailed(String.format(Locale.CHINA, App.context.getString(R.string.alert_del_course_forbid), shopcount));
            }
        } else {
            if (shopcount == 1) {
                if (courseDetail.is_private() && serPermisAction.check(gymWrapper.shop_id(), PermissionServerUtils.PRISETTING_CAN_DELETE)
                    || !courseDetail.is_private() && serPermisAction.check(gymWrapper.shop_id(),
                    PermissionServerUtils.TEAMSETTING_CAN_DELETE)) {
                    view.showDelDialog("删除后，已有的排期和课程预约都不会受到影响");
                } else {
                    view.showDelFailed("抱歉!您没有该功能权限");
                }
            } else if (gymWrapper.singleMode()) {
                view.showDelFailed(String.format(Locale.CHINA, App.context.getString(R.string.alert_del_course_forbid), shopcount));
            } else {
                view.showDelFailed("该课程适用于多家场馆,请到【连锁运营】里进行删除");
            }
        }
    }

    public interface CourseDetailView extends PView {
        /**
         *
         */
        void setCourseInfo(CourseType course);

        void setJacket(List<String> photos);

        void setScore(float teacherScore, float courseScore, float serverScore);

        /**
         * @param teachers all teacher of this course
         */
        void setTeachers(List<CourseDetailTeacher> teachers);

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
