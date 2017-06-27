package cn.qingchengfit.staffkit.views.student.score;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ScoreRule;
import cn.qingchengfit.model.responese.ScoreRuleAward;
import cn.qingchengfit.model.responese.ScoreRuleAwardResponse;
import cn.qingchengfit.model.responese.ScoreRuleResponse;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.Post_Api;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
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
 * //Created by yangming on 16/12/23.
 */
public class ConfigPresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private RestRepositoryV2 restRepository;

    @Inject public ConfigPresenter(RestRepositoryV2 restRepository) {
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

    /**
     * 获取积分功能是否开启
     */
    public void getScoreStatus() {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);
        Observable observable = restRepository.getApi(Get_Api.class).qcGetStudentScoreStatus(App.staffId, params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<ScoreStatus>() {
            @Override protected void _onNext(ScoreStatus scoreStatus) {
                view.onScoreStatus(scoreStatus.getModule().isScore());
            }

            @Override protected void _onError(String message) {
                view.onScoreStatusFail(message);
            }
        }));
    }

    /**
     * get 基础积分
     */
    public void getScoreRuls() {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);
        Observable observable = restRepository.getApi(Get_Api.class).qcGetStudentScoreRules(App.staffId, params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<ScoreRuleResponse>() {
            @Override protected void _onNext(ScoreRuleResponse rule) {
                view.onScoreRules(rule.rule);
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    /**
     * get 奖励规则
     */
    public void getScoreAward() {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);
        Observable observable = restRepository.getApi(Get_Api.class).qcGetStudentScoreAward(App.staffId, params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<ScoreRuleAwardResponse>() {
            @Override protected void _onNext(ScoreRuleAwardResponse awards) {
                view.onScoreAward(awards.favors);
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    public void postScoreStatus() {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);

        ArrayMap<String, Object> body = new ArrayMap<>();
        body.put("score", false);
        Observable observable = restRepository.getApi(Post_Api.class).qcPutScoreStatus(App.staffId, params, body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                view.onCloseSuccess();
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    public void openScoreStatus() {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);

        ArrayMap<String, Object> body = new ArrayMap<>();
        body.put("score", true);
        Observable observable = restRepository.getApi(Post_Api.class).qcPutScoreStatus(App.staffId, params, body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                //                view.onCloseSuccess();
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    public interface PresenterView extends PView {

        void onScoreStatus(boolean score);

        void onScoreStatusFail(String e);

        void onScoreRules(ScoreRule scoreRule);

        void onScoreAward(List<ScoreRuleAward> awards);

        void onCloseSuccess();

        void onShowError(String e);
    }
}