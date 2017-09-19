package cn.qingchengfit.staffkit.views.student.score;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.responese.Score;
import cn.qingchengfit.model.responese.ScoreHistory;
import cn.qingchengfit.model.responese.ScoreHistoryResponse;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import cn.qingchengfit.utils.GymUtils;
import java.util.List;
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
public class ScoreDetailPresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentWrapper studentBean;
    private RestRepositoryV2 restRepository;

    @Inject public ScoreDetailPresenter(RestRepositoryV2 restRepository) {
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

    public void getScoreHistory() {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);
        Observable observable = restRepository.getApi(Get_Api.class).qcGetStudentScoreHistory(App.staffId, studentBean.id(), params);
        HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<ScoreHistoryResponse>() {
            @Override protected void _onNext(ScoreHistoryResponse rule) {
                if (view != null) view.onHsitory(rule.histories);
            }

            @Override protected void _onError(String message) {
                if (view != null) view.onShowError(message);
            }
        });
    }

    public void getScore() {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);

        ArrayMap<String, Object> body = new ArrayMap<>();
        body.put("score", true);
        Observable observable = restRepository.getApi(Get_Api.class).qcGetStudentScore(App.staffId, studentBean.id(), params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<Score>() {
            @Override protected void _onNext(Score score) {
                view.onStudentScore(score.shopbrand.score);
            }

            @Override protected void _onError(String message) {
                view.onStudentScoreFail(message);
            }
        }));
    }

    public interface PresenterView extends PView {

        void onHsitory(List<ScoreHistory> scoreHistories);

        void onShowError(String e);

        void onStudentScore(String score);

        void onStudentScoreFail(String s);
    }
}