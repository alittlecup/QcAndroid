package com.qingchengfit.fitcoach.fragment.course;

import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.model.bean.CourseDetail;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.bean.Brand;
import cn.qingchengfit.staffkit.usecase.bean.CoachService;
import cn.qingchengfit.staffkit.usecase.response.QcResponseShopComment;
import cn.qingchengfit.staffkit.usecase.response.ResponseConstant;
import cn.qingchengfit.staffkit.utils.GymUtils;
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
 * Created by Paper on 16/8/1.
 */
public class ShopCommentPresenter extends BasePresenter {

    RestRepository restRepository;
    Brand brand;
    CoachService coachService;
    private ShopCommentView view;

    @Inject
    public ShopCommentPresenter(RestRepository restRepository, Brand brand, CoachService coachService) {
        this.restRepository = restRepository;
        this.brand = brand;
        this.coachService = coachService;
    }


    @Override
    public void attachView(PView v) {
        view = (ShopCommentView)v;
    }

    public void queryShopComments(String courseid){
        RxRegiste(restRepository.getGet_api().qcGetShopComment(App.staffId,courseid, GymUtils.getParams(coachService,brand))
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Action1<QcResponseShopComment>() {
                    @Override
                    public void call(QcResponseShopComment qcResponseShopComment) {
                        if (ResponseConstant.checkSuccess(qcResponseShopComment)){
                            view.onCourse(qcResponseShopComment.data.scores.course);
                            view.onGetComment(qcResponseShopComment.data.scores.shops);

                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                })
        );
    }



    public interface ShopCommentView extends PView{
        void onGetComment(List<QcResponseShopComment.CommentShop> shops);
        void onCourse(CourseDetail courseDetail);

    }
}
