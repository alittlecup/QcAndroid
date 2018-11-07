package cn.qingchengfit.staffkit.views.student.score;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ScoreRank;
import cn.qingchengfit.model.responese.ScoreRankResponse;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.StaffRespository;
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
 * //Created by yangming on 16/12/22.
 */
public class ScoreHomePresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StaffRespository restRepository;

    @Inject public ScoreHomePresenter(StaffRespository restRepository) {
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
        Observable observable = restRepository.getStaffAllApi().qcGetStudentScoreStatus(App.staffId, params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<ScoreStatus>() {
            @Override protected void _onNext(ScoreStatus scoreStatus) {
                view.onScoreStatus(scoreStatus.getModule().isScore());
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    /**
     * 回去排行榜列表
     */
    public void getScoreRanks() {
        ArrayMap<String, String> params = GymUtils.getParamsV2(gymWrapper.getCoachService(), null);
        Observable observable = restRepository.getStaffAllApi().qcGetStudentScoresRank(App.staffId, params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<ScoreRankResponse>() {
            @Override protected void _onNext(ScoreRankResponse scoreRankResponse) {
                view.onScoreRanks(scoreRankResponse.ranks);
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    public interface PresenterView extends PView {

        void onScoreStatus(boolean score);

        void onScoreRanks(List<ScoreRank> ranks);

        void onShowError(String e);
    }
}