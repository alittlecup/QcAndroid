package cn.qingchengfit.saasbase.course.course.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.course.course.network.body.EditJacketBody;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class JacketManagerPresenter extends BasePresenter<JacketManagerPresenter.JacketManagerView> {

    @Inject ICourseModel courseModel;


    @Inject public JacketManagerPresenter() {
    }


    public void completeJacket(String courseid, List<String> jackets, boolean openCustom) {
        RxRegiste(courseModel
            .qcEditJacket( courseid,
                new EditJacketBody.Builder().photos(jackets).random_show_photos(!openCustom).build())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        mvpView.onSuccess();
                    } else {
                        mvpView.onFaied(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public void queryJacket(String courseid) {
        RxRegiste(courseModel
            .qcGetJacket( courseid)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponse -> {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    ArrayList<String> r = new ArrayList<String>();
                    for (int i = 0; i < qcResponse.data.photos.size(); i++) {
                        r.add(qcResponse.data.photos.get(i).photo);
                    }
                    mvpView.setJacket(r);
                } else {
                    mvpView.onShowError(qcResponse.getMsg());
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    mvpView.onShowError(throwable.getMessage());
                }
            }));
    }

    public interface JacketManagerView extends CView {
        void setJacket(List<String> jackets);

        void onSuccess();

        void onFaied(String s);

        void onSwitch(boolean s);
    }
}
