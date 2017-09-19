package cn.qingchengfit.staffkit.views.card;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.body.FixCard;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.mvpbase.CommonPView;
import cn.qingchengfit.staffkit.usecase.RealCardUsecase;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by Paper on 16/6/16.
 * <p>
 * ((      /|_/|
 * \\.._.'  , ,\
 * /\ | '.__ v /
 * (_ .   /   "
 * ) _)._  _ /
 * '.\ \|( / ( mrf
 * '' ''\\ \\
 */

public class FixRealcardNumPresenter extends BasePresenter implements Presenter {

    @Inject RealCardUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RealcardWrapper realCard;
    private CommonPView view;

    @Inject public FixRealcardNumPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CommonPView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
      super.unattachView();
      view = null;
    }

    public void fixCardNum(String cardnum) {
        FixCard card = new FixCard();
        card.card_no = cardnum;
        usecase.updateCard(realCard.id(), gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model(), card, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    view.onSuccess();
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        });
    }

    public void fixBundleStudent(String users) {
        FixCard card = new FixCard();
        card.user_ids = users;
        usecase.updateCard(realCard.id(), gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model(), card, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    view.onSuccess();
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        });
    }
}
