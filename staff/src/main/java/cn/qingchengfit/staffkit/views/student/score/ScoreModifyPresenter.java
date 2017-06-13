package cn.qingchengfit.staffkit.views.student.score;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Post_Api;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import cn.qingchengfit.utils.GymUtils;
import javax.inject.Inject;
import rx.Observable;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/27.
 */
public class ScoreModifyPresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentWrapper studentBean;
    private RestRepositoryV2 restRepository;

    @Inject public ScoreModifyPresenter(RestRepositoryV2 restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        this.view = (PresenterView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void postScoreChange(String scoreChange, String remarks) {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);
        ArrayMap<String, Object> body = new ArrayMap<>();
        body.put("change_score", Double.valueOf(scoreChange));
        body.put("remarks", remarks);
        Observable observable = restRepository.getApi(Post_Api.class).qcPostScoreHistory(App.staffId, studentBean.id(), params, body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                view.onSuccess();
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    public interface PresenterView extends PView {
        void onSuccess();

        void onShowError(String message);
    }
}