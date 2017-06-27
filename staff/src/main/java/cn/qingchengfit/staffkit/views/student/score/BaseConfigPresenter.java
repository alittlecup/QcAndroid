package cn.qingchengfit.staffkit.views.student.score;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ScoreRule;
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
 * //Created by yangming on 16/12/26.
 */
public class BaseConfigPresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private RestRepositoryV2 restRepository;

    @Inject public BaseConfigPresenter(RestRepositoryV2 restRepository) {
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

    public void postScoreBaseConfig(ScoreRule rule) {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);

        ArrayMap<String, Object> body = new ArrayMap<>();

        body.put("teamarrange_enable", rule.teamarrange_enable);
        body.put("teamarrange", rule.teamarrange);
        body.put("priarrange_enable", rule.priarrange_enable);
        body.put("priarrange", rule.priarrange);
        body.put("checkin_enable", rule.checkin_enable);
        body.put("checkin", rule.checkin);
        body.put("buycard_enable", rule.buycard_enable);
        body.put("buycard", rule.buycard);
        body.put("chargecard_enable", rule.chargecard_enable);
        body.put("chargecard", rule.chargecard);

        Observable observable = restRepository.getApi(Post_Api.class).qcPutScoreRules(App.staffId, params, body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                view.onConfigSuccess();
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    public void postScoreStatus() {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);

        ArrayMap<String, Object> body = new ArrayMap<>();
        body.put("score", true);
        Observable observable = restRepository.getApi(Post_Api.class).qcPutScoreStatus(App.staffId, params, body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                view.onOpenSuccess();
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    public interface PresenterView extends PView {
        void onConfigSuccess();

        void onOpenSuccess();

        void onShowError(String e);
    }
}