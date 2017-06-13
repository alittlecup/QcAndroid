package cn.qingchengfit.staffkit.views.student.score;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.StudentScoreAwardRuleBean;
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
public class ScoreAwardAddPresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private RestRepositoryV2 restRepository;

    @Inject public ScoreAwardAddPresenter(RestRepositoryV2 restRepository) {
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

    public void postScoreAward(StudentScoreAwardRuleBean awardRuleBean) {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);
        ArrayMap<String, Object> body = new ArrayMap<>();

        body.put("is_active", awardRuleBean.is_active);
        body.put("start", awardRuleBean.dateStart);
        body.put("end", awardRuleBean.dateEnd);
        body.put("teamarrange_enable", awardRuleBean.groupTimes_enable);
        body.put("teamarrange", awardRuleBean.groupTimes);
        body.put("priarrange_enable", awardRuleBean.privateTimes_enable);
        body.put("priarrange", awardRuleBean.privateTimes);
        body.put("checkin_enable", awardRuleBean.signinTimes_enable);
        body.put("checkin", awardRuleBean.signinTimes);
        body.put("buycard_enable", awardRuleBean.buyTimes_enable);
        body.put("buycard", awardRuleBean.buyTimes);
        body.put("chargecard_enable", awardRuleBean.chargeTimes_enable);
        body.put("chargecard", awardRuleBean.chargeTimes);

        Observable observable = restRepository.getApi(Post_Api.class).qcPostScoreRulesAward(App.staffId, params, body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                view.onSuccess();
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    public void putScoreAward(String award_id, StudentScoreAwardRuleBean awardRuleBean) {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);
        ArrayMap<String, Object> body = new ArrayMap<>();

        //body.put("id", award_id);
        body.put("is_active", awardRuleBean.is_active);
        body.put("start", awardRuleBean.dateStart);
        body.put("end", awardRuleBean.dateEnd);
        body.put("teamarrange_enable", awardRuleBean.groupTimes_enable);
        body.put("teamarrange", awardRuleBean.groupTimes);
        body.put("priarrange_enable", awardRuleBean.privateTimes_enable);
        body.put("priarrange", awardRuleBean.privateTimes);
        body.put("checkin_enable", awardRuleBean.signinTimes_enable);
        body.put("checkin", awardRuleBean.signinTimes);
        body.put("buycard_enable", awardRuleBean.buyTimes_enable);
        body.put("buycard", awardRuleBean.buyTimes);
        body.put("chargecard_enable", awardRuleBean.chargeTimes_enable);
        body.put("chargecard", awardRuleBean.chargeTimes);

        Observable observable = restRepository.getApi(Post_Api.class).qcPutScoreRulesAward(App.staffId, award_id, params, body);
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

        void onShowError(String e);
    }
}