package cn.qingchengfit.staffkit.views.card.filter;

import android.support.annotation.IntDef;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.CardTpls;
import cn.qingchengfit.model.responese.FilterCardBean;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static cn.qingchengfit.staffkit.App.staffId;

/**
 * Created by fb on 2017/2/22.
 */

public class FilterHeadPresenter extends BasePresenter {

    public static final int STORE_CARD = 1;     //储值卡
    public static final int SECOND_CARD = 2;    //次卡
    public static final int TIME_CARD = 3;      //期限卡

    private static final String ALL_TYPE = "全部类型";
    private static final String STORE_TYPE = "储值类型";
    private static final String SECOND_TYPE = "次卡类型";
    private static final String TIME_TYPE = "期限类型";
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private HashMap<Integer, List<CardTpl>> cardMap = new HashMap<>();
    private List<CardTpl> cardAllList = new ArrayList<>();
    private List<CardTpl> cardStoreList = new ArrayList<>();
    private List<CardTpl> cardSecondList = new ArrayList<>();
    private List<CardTpl> cardTimeList = new ArrayList<>();
    private List<FilterCardBean> parentList = new ArrayList<>();
    private RestRepository restRepository;
    ;
    private OnFilterConditionListener onFilterConditionListener;
    private FilterCardBean allFilterCardBean = new FilterCardBean();
    private FilterCardBean storeFilterCardBean;
    private FilterCardBean secondFilterCardBean;
    private FilterCardBean timeFilterCardBean;

    @Inject public FilterHeadPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    private void initMap() {
        allFilterCardBean.name = ALL_TYPE;
        allFilterCardBean.isChoosen = true;
        allFilterCardBean.type = 0;
        CardTpl cardTpl = new CardTpl();
        cardTpl.name = "全部会员卡种类";
        cardTpl.isChoosen = true;
        cardAllList.add(cardTpl);
        cardMap.put(allFilterCardBean.type, cardAllList);
        parentList.add(allFilterCardBean);
    }

    public void setOnFilterConditionListener(OnFilterConditionListener onFilterConditionListener) {
        this.onFilterConditionListener = onFilterConditionListener;
    }

    public void getCardFilterType(boolean isBalance) {

        HashMap<String, Object> params = gymWrapper.getParams();

        if (isBalance) {
            params.put("is_active", "1");
        }

        RxRegiste(restRepository.getGet_api()
            .qcGetCardFilterCondition(staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<CardTpls>>() {
                @Override public void call(QcResponseData<CardTpls> cardTplsQcResponseData) {
                    for (CardTpl card_tpl : cardTplsQcResponseData.data.card_tpls) {
                        switch (card_tpl.type) {
                            case 1:
                                cardStoreList.add(card_tpl);
                                break;
                            case 2:
                                cardSecondList.add(card_tpl);
                                break;
                            case 3:
                                cardTimeList.add(card_tpl);
                                break;
                        }
                    }
                    handleParentData();
                    handleChildData();
                    onFilterConditionListener.onLoadFinish();
                }
            }));
    }

    public List<FilterCardBean> getParentList() {
        return parentList;
    }

    public List<FilterCardBean> handleParentData() {
        if (parentList.size() > 0) {
            parentList.clear();
            cardAllList.clear();
            cardMap.clear();
        }
        initMap();
        if (cardStoreList.size() != 0) {
            storeFilterCardBean = new FilterCardBean();
            storeFilterCardBean.name = STORE_TYPE;
            storeFilterCardBean.isChoosen = false;
            storeFilterCardBean.type = 1;
            parentList.add(storeFilterCardBean);
        }
        if (cardSecondList.size() != 0) {
            secondFilterCardBean = new FilterCardBean();
            secondFilterCardBean.name = SECOND_TYPE;
            secondFilterCardBean.isChoosen = false;
            secondFilterCardBean.type = 2;
            parentList.add(secondFilterCardBean);
        }
        if (cardTimeList.size() != 0) {
            timeFilterCardBean = new FilterCardBean();
            timeFilterCardBean.name = TIME_TYPE;
            timeFilterCardBean.isChoosen = false;
            timeFilterCardBean.type = 3;
            parentList.add(timeFilterCardBean);
        }

        return parentList;
    }

    public void handleChildData() {
        if (cardMap.size() > 1) {
            cardMap.clear();
        }
        CardTpl cardTpl;
        if (cardStoreList.size() != 0) {
            cardTpl = new CardTpl();
            cardTpl.isChoosen = false;
            cardTpl.name = "全部储值卡种类";
            cardTpl.id = "";
            cardTpl.tpl_type = 1;
            List<CardTpl> list = new ArrayList<>();
            list.addAll(cardStoreList);
            cardStoreList.clear();
            cardStoreList.add(cardTpl);
            cardStoreList.addAll(list);
            cardMap.put(storeFilterCardBean.type, cardStoreList);
        }
        if (cardSecondList.size() != 0) {
            cardTpl = new CardTpl();
            cardTpl.isChoosen = false;
            cardTpl.name = "全部次卡种类";
            cardTpl.id = "";
            cardTpl.tpl_type = 2;
            List<CardTpl> list = new ArrayList<>();
            list.addAll(cardSecondList);
            cardSecondList.clear();
            cardSecondList.add(cardTpl);
            cardSecondList.addAll(list);
            cardMap.put(secondFilterCardBean.type, cardSecondList);
        }
        if (cardTimeList.size() != 0) {
            cardTpl = new CardTpl();
            cardTpl.isChoosen = false;
            cardTpl.name = "全部期限类型";
            cardTpl.id = "";
            cardTpl.tpl_type = 3;
            List<CardTpl> list = new ArrayList<>();
            list.addAll(cardTimeList);
            cardTimeList.clear();
            cardTimeList.add(cardTpl);
            cardTimeList.addAll(list);
            cardMap.put(timeFilterCardBean.type, cardTimeList);
        }
    }

    public void resetCarfFilter(int type) {
        List<CardTpl> list = cardMap.get(type);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isChoosen = false;
        }
    }

    public void resetRight() {
        for (int i : cardMap.keySet()) {
            if (cardMap.get(i) != null) {
                for (CardTpl cardTpl : cardMap.get(i)) {
                    cardTpl.isChoosen = false;
                }
            }
        }
    }

    public void changeChildList(int type, List<CardTpl> list) {
        cardMap.put(type, list);
    }

    public List<CardTpl> getFilterCondition(int type) {
        return cardMap.get(type);
    }

    @IntDef({ STORE_CARD, SECOND_CARD, TIME_CARD }) @Retention(RetentionPolicy.SOURCE) @interface CardType {
    }

    public interface OnFilterConditionListener {
        void onLoadFinish();
    }
}
