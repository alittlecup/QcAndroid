package cn.qingchengfit.saasbase.course.course.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.course.course.bean.CommentShop;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ShopCommentPresenter extends BasePresenter {

    @Inject ICourseModel courseModel;
    private ShopCommentView view;

    @Inject public ShopCommentPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (ShopCommentView) v;
    }

    public void queryShopComments(String courseid) {
        RxRegiste(courseModel
            .qcGetShopComment(courseid)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponseShopComment -> {
                if (ResponseConstant.checkSuccess(qcResponseShopComment)) {
                    view.onCourse(qcResponseShopComment.data.scores.course);
                    view.onGetComment(qcResponseShopComment.data.scores.shops);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public interface ShopCommentView extends PView {
        void onGetComment(List<CommentShop> shops);

        void onCourse(CourseType courseDetail);
    }
}
