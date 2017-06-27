package cn.qingchengfit.staffkit.views.cardtype.detail;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.CardStandard;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.CardTplOption;
import cn.qingchengfit.model.responese.CardTplResponse;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponseOption;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.CardTypeUsecase;
import cn.qingchengfit.utils.StringUtils;
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
 * Created by Paper on 16/3/16 2016.
 */
public class CardtypeDetailPresenter extends BasePresenter {

    @Inject CardTypeUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository mRestRepository;
    private CardtypeDetailView view;

    @Inject public CardtypeDetailPresenter(CardTypeUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CardtypeDetailView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryCardType(String id) {
        RxRegiste(mRestRepository.getGet_api()
            .qcGetCardTplsDetail(App.staffId, id, gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<CardTplResponse>>() {
                @Override public void call(QcResponseData<CardTplResponse> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        CardTpl card_tpl = qcResponse.getData().card_tpl;
                        if (card_tpl.is_limit()) {
                            StringBuffer ss = new StringBuffer();
                            if (card_tpl.getPre_times() != 0) {
                                ss.append("限制: 可提前预约");
                                ss.append(card_tpl.getPre_times());
                                ss.append("节课");
                            }
                            if (card_tpl.getDay_times() != 0) {
                                if (card_tpl.getPre_times() != 0) ss.append(",");
                                ss.append("每天共计可上").append(card_tpl.getDay_times()).append("节课");
                            } else if (card_tpl.getWeek_times() != 0) {
                                if (card_tpl.getPre_times() != 0) ss.append(",");
                                ss.append("每周共计可上").append(card_tpl.getWeek_times()).append("节课");
                            } else if (card_tpl.getMonth_times() != 0) {
                                if (card_tpl.getPre_times() != 0) ss.append(",");
                                ss.append("每月共计可上").append(card_tpl.getMonth_times()).append("节课");
                            } else if (card_tpl.getBuy_limit() != 0) {
                                if (!StringUtils.isEmpty(ss.toString())) ss.append(",");
                                ss.append("每个会员限购").append(card_tpl.getBuy_limit()).append("张");
                            }
                            if (TextUtils.isEmpty(ss.toString())) {
                                card_tpl.setLimit("限制: 无");
                            } else {
                                card_tpl.setLimit(ss.toString());
                            }
                        } else {
                            card_tpl.setLimit("限制: 无");
                        }
                        if (TextUtils.isEmpty(card_tpl.getDescription())) {
                            card_tpl.setDescription("简介: 无");
                        } else {
                            card_tpl.setDescription(TextUtils.concat("简介: ", card_tpl.getDescription()).toString());
                        }
                        view.onGetCardTypeInfo(card_tpl);
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void delCardtype(String id) {

        usecase.delCardTpl(id, gymWrapper.id(), gymWrapper.model(), gymWrapper.inBrand() ? gymWrapper.brand_id() : null,
            new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onDelSucceess();
                    } else {
                        view.onDelFailed(qcResponse.getMsg());
                    }
                }
            });
    }

    public void resumeCardtype(String staffid, String id) {
        RxRegiste(mRestRepository.getPost_api()
            .qcResumeCardtpl(staffid, id, gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onResumeOk();
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onFailed(throwable.getMessage());
                }
            }));
    }

    public void queryOptions(String cardtype_id) {
        usecase.queryCardTpl(cardtype_id, gymWrapper.id(), gymWrapper.model(), gymWrapper.brand_id(), new Action1<QcResponseOption>() {

            @Override public void call(QcResponseOption qcResponseOption) {
                if (qcResponseOption.getStatus() == ResponseConstant.SUCCESS) {
                    List<CardStandard> datas = new ArrayList<CardStandard>();
                    for (CardTplOption option : qcResponseOption.data.options) {
                        String support =
                            "适用于:" + (option.can_create ? "新购卡" : "") + ((option.can_create && option.can_charge) ? "、" : "") + (
                                option.can_charge ? "充值" : "");
                        String unit = "";
                        if (option.card_tpl.getCardTypeInt() == 1) {
                            unit = "元";
                        } else if (option.card_tpl.getCardTypeInt() == 2) {
                            unit = "次";
                        } else {
                            unit = "天";
                        }
                        CardStandard standard = new CardStandard(option.charge + unit, "(售价: " + option.price + "元)", support,
                            option.limit_days ? "有效期: " + option.days + "天" : "有效期: 不限");
                        standard.setId(option.id);
                        standard.setSupportCharge(option.can_charge);
                        standard.setSupportCreate(option.can_create);
                        standard.setCharge(option.charge + "");
                        standard.setIncome((int) Float.parseFloat(option.price));
                        standard.setLimitDay(option.limit_days ? option.days : 0);
                        standard.for_staff = option.for_staff;
                        if (option.card_tpl.getCardTypeInt() == Configs.CATEGORY_DATE) standard.setValid_date(null);
                        datas.add(standard);
                    }
                    view.onGetStandards(datas);
                }
            }
        });
    }
}
