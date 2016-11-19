package com.qingchengfit.fitcoach.fragment.course;

import com.anbillon.qcmvplib.PView;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Utils.GymUtils;
import com.qingchengfit.fitcoach.Utils.PermissionServerUtils;
import com.qingchengfit.fitcoach.action.SerPermisAction;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import com.qingchengfit.fitcoach.di.BasePresenter;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.CourseBody;
import com.qingchengfit.fitcoach.http.bean.QcResponse;

import java.util.HashMap;

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
public class EditCoursePresenter extends BasePresenter {

    private Brand brand;
    private CoachService coachService;
    private EditCourseView view;
    private RestRepository restRepository;
//    private GymStatus gymStatus;

    @Inject
    public EditCoursePresenter(Brand brand, CoachService coachService, RestRepository restRepository) {
        this.brand = brand;
        this.coachService = coachService;
        this.restRepository = restRepository;
//        this.gymStatus = gymStatus;
    }


    @Override
    public void attachView(PView v) {
        view = (EditCourseView) v;
    }

    @Override
    public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void judgePermission(CourseDetail courseDetail) {
        if (GymUtils.isInBrand(coachService)) {
            if ((courseDetail.is_private() && SerPermisAction.checkMuti(PermissionServerUtils.PRISETTING_CAN_CHANGE, courseDetail.getShopIdList()))
                    || (!courseDetail.is_private() && SerPermisAction.checkMuti(PermissionServerUtils.TEAMSETTING_CAN_CHANGE, courseDetail.getShopIdList()))
                    ) { //连锁运营下 全权限
                view.showBaseInfoHint(null);
                view.editBaseInfo(courseDetail);
                view.showSuitGymHint(null);
            } else {
                view.showBaseInfoHint("仅具有全部适用场馆管理员身份的用户才能编辑");
                view.showSuitGymHint(null);
                view.showbaseInfo(courseDetail);//连锁运营下部分权限

            }
            view.editSuitGym(courseDetail);
        } else {//场馆下
//            if (gymStatus.getSingle()) { //但场馆模式
//                view.showBaseInfoHint(null);
//                view.editBaseInfo(courseDetail);

//            }
//            else{
//          多场馆进入
//                if (courseDetail.getShopIdList().size() > 1){
//                    view.showBaseInfoHint("请在「连锁运营」中修改基本信息");
//                    view.showbaseInfo(courseDetail);
//                } else if ((courseDetail.is_private() && SerPermisAction.check(coachService.getShop_id(), PermissionServerUtils.PRISETTING_CAN_CHANGE))
//                        || (!courseDetail.is_private() && SerPermisAction.check(coachService.getShop_id(), PermissionServerUtils.TEAMSETTING_CAN_CHANGE))
//                        ) {
//                    view.showBaseInfoHint(null);
//                    view.editBaseInfo(courseDetail);
//                } else {
//                    view.showBaseInfoHint("抱歉!您没有修改权限");
//                    view.showbaseInfo(courseDetail);
//                }
//            }
            view.showSuitGymHint("请在「连锁运营」中修改所属场馆");
            view.showSuitGym(courseDetail);
        }
    }

    public void editCourse(String courseid, CourseBody body) {
        RxRegiste(restRepository.getPost_api().qcUpdateCourse(App.staffId, courseid, GymUtils.getParams(coachService, brand), body)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponse>() {
                    @Override
                    public void call(QcResponse qcResponse) {
                        if (ResponseConstant.checkSuccess(qcResponse)) {
                            view.onSucceed();
                        } else {
                            view.onFailed(qcResponse.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                })
        );
    }

    public void editCourseShops(String staffid, String courseid, String shopids) {
        HashMap<String, String> params = GymUtils.getParams(coachService, brand);
        params.put("shop_ids", shopids);
        RxRegiste(restRepository.getPost_api().qcEditCourseShops(staffid, courseid, params)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponse>() {
                    @Override
                    public void call(QcResponse qcResponse) {
                        if (ResponseConstant.checkSuccess(qcResponse)) {
                            view.onSucceedShops();
                        } else view.onFailed(qcResponse.getMsg());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.onFailed(throwable.getMessage());
                    }
                })
        );

    }

    public interface EditCourseView extends PView {
        void showBaseInfoHint(String hint);

        void showSuitGymHint(String hint);

        void editBaseInfo(CourseDetail courseDetail);

        void showbaseInfo(CourseDetail courseDetail);

        void editSuitGym(CourseDetail courseDetail);

        void showSuitGym(CourseDetail courseDetail);

        void onSucceed();

        void onFailed(String err);

        void onSucceedShops();
    }


}
