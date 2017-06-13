package cn.qingchengfit.staffkit.views.cardtype.list;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.CardTpls;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.CardTypeUsecase;
import cn.qingchengfit.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
public class CardTypeListPresenter extends BasePresenter {

    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject CardTypeUsecase cardTypeUsecase;
    private CardTypeListView view;

    @Inject public CardTypeListPresenter() {

    }

    public void onItemClick(int pos) {

    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CardTypeListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        this.view = null;
    }

    public void queryCardTypeNoNeedPermission() {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("key", PermissionServerUtils.CARDSETTING);
        params.put("method", "get");
        RxRegiste(restRepository.getGet_api()
            .qcGetCardTplsPermission(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<QcResponseData<CardTpls>>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {
                }

                @Override public void onNext(QcResponseData<CardTpls> qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        List<CardTpl> card_tpls = new ArrayList<>();
                        for (CardTpl card_tpl : qcResponse.data.card_tpls) {
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
                            card_tpls.add(card_tpl);
                        }

                        view.onGetData(card_tpls);
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }));
    }

    public void queryCardTypePermission(String staffid) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("key", PermissionServerUtils.CARDSETTING);
        params.put("method", "get");

        RxRegiste(restRepository.getGet_api()
            .qcGetCardTplsPermission(staffid, params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponseData<CardTpls>>() {
                @Override public void call(QcResponseData<CardTpls> qcResponseCardTpls) {
                    if (qcResponseCardTpls.getStatus() == ResponseConstant.SUCCESS) {
                        List<CardTpl> card_tpls = new ArrayList<>();
                        for (CardTpl card_tpl : qcResponseCardTpls.data.card_tpls) {
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
                            //                                    card_tpl.setGymid(service.getId());
                            //                                    card_tpl.setGymModel(service.getModel());
                            card_tpls.add(card_tpl);
                        }

                        view.onGetData(card_tpls);
                    } else {
                        Timber.e(qcResponseCardTpls.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            }));
    }

    public void queryCardTypeList(String staffid, final int type, boolean isEnable) {

        RxRegiste(restRepository.getGet_api()
            .qcGetCardTpls(staffid, gymWrapper.getParams(), type == 0 ? null : Integer.toString(type), isEnable ? "1" : "0")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<CardTpls>>() {
                @Override public void call(QcResponseData<CardTpls> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                            List<CardTpl> card_tpls = new ArrayList<>();
                            for (CardTpl card_tpl : qcResponse.data.card_tpls) {
                                if (type == 0 || card_tpl.getType() == type) {
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
                                    card_tpls.add(card_tpl);
                                }
                            }

                            view.onGetData(card_tpls);
                        } else {
                            view.onShowError(qcResponse.getMsg());
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError("error!");
                }
            }));
    }
}
