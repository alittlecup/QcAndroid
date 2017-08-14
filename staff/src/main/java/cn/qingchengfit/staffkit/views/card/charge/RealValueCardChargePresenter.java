package cn.qingchengfit.staffkit.views.card.charge;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.responese.CardStandard;
import cn.qingchengfit.model.responese.CardTplOption;
import cn.qingchengfit.model.responese.QcResponseOption;
import cn.qingchengfit.model.responese.QcResponsePostions;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.CardTypeUsecase;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/25 2016.
 */
public class RealValueCardChargePresenter extends BasePresenter {

    @Inject CardTypeUsecase usecase;
    @Inject RealcardWrapper realCard;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    @Inject RestRepository mRestRepository;
    private RealValueCardChargeView view;

    @Inject public RealValueCardChargePresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (RealValueCardChargeView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    boolean hasEditPermission() {
        return serPermisAction.check(PermissionServerUtils.CARDSETTING_CAN_CHANGE);
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryStardard() {
        usecase.queryCardTpl(realCard.getRealCard().getCard_tpl_id(), gymWrapper.id(), gymWrapper.model(), gymWrapper.brand_id(),
            new Action1<QcResponseOption>() {

                @Override public void call(QcResponseOption qcResponseOption) {
                    List<CardStandard> datas = new ArrayList<>();
                    if (qcResponseOption.data.options == null) {
                        view.onStandard(new ArrayList<CardStandard>());
                    } else {
                        for (CardTplOption option : qcResponseOption.data.options) {
                            String support =
                                "适用于: " + (option.can_create ? "新购卡" : "") + ((option.can_create && option.can_charge) ? "、" : "") + (
                                    option.can_charge ? "充值" : "");
                            String unit = "";
                            if (option.card_tpl.type == 1) {
                                unit = "元";
                            } else if (option.card_tpl.type == 2) {
                                unit = "次";
                            } else {
                                unit = "天";
                            }
                            CardStandard standard = new CardStandard(option.charge + unit, "(售价: " + option.price + "元)", null,
                                option.limit_days ? "有效期: " + option.days + "天" : "有效期: 不限");
                            standard.setCharge(option.charge + "");
                            standard.setIncome(Float.parseFloat(option.price));
                            standard.setLimitDay(option.days);
                            if (option.card_tpl.type == Configs.CATEGORY_DATE) {
                                standard.setValid_date(null);
                            }
                            if (option.can_charge) datas.add(standard);
                        }
                        view.onStandard(datas);
                    }
                }
            });
    }

    public void queryPositions(String staffid, String permission) {
        RxRegiste(mRestRepository.getGet_api()
            .qcGetPermissionPostions(staffid, gymWrapper.getParams(), permission)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponsePostions>() {
                @Override public void call(QcResponsePostions qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.data.positions != null) {
                            String retStr = "";
                            for (int i = 0; i < qcResponse.data.positions.size(); i++) {
                                retStr = retStr.concat(qcResponse.data.positions.get(i).getName());
                                if (i < qcResponse.data.positions.size() - 1) {
                                    retStr = retStr.concat(Configs.SEPARATOR);
                                }
                            }
                            view.onPositionStr(retStr);
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }
}
