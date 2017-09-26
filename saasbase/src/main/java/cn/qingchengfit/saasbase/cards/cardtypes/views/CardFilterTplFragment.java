package cn.qingchengfit.saasbase.cards.cardtypes.views;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/9/28.
 */
public class CardFilterTplFragment extends BaseFragment {

  @BindView(R2.id.rv_left) RecyclerView rvLeft;
  @BindView(R2.id.rv_right) RecyclerView rvRight;
  @BindArray(R2.array.card_filter_cardtype_category) String[] cardCategory;
  CommonFlexAdapter adapterLeft, adapterRight;
  private HashMap<String,List<CardTpl>> mAllDatas = new HashMap<>();
  @Inject ICardModel cardModel;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card_filter_tpl, container, false);
    unbinder = ButterKnife.bind(this, view);
    adapterLeft = new CommonFlexAdapter(new ArrayList(), leftClickListener);
    adapterRight = new CommonFlexAdapter(new ArrayList(), rightClickListener);
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    RxRegiste(cardModel.qcGetCardFilterTpls(true)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .flatMap(new Func1<QcDataResponse<CardTplListWrap>, Observable<HashMap<String,List<CardTpl>>>>() {
          @Override
          public Observable<HashMap<String,List<CardTpl>>> call(QcDataResponse<CardTplListWrap> cardTplListWrapQcDataResponse) {
            HashMap<String,List<CardTpl>> ret = new HashMap<String, List<CardTpl>>();
            if (ResponseConstant.checkSuccess(cardTplListWrapQcDataResponse)) {
              ret.put(cardCategory[0],cardTplListWrapQcDataResponse.data.card_tpls);
              ret.put(cardCategory[1],new ArrayList<CardTpl>());
              ret.put(cardCategory[2],new ArrayList<CardTpl>());
              ret.put(cardCategory[3],new ArrayList<CardTpl>());
              for (CardTpl card_tpl : cardTplListWrapQcDataResponse.data.card_tpls) {
                ret.get(cardCategory[card_tpl.type]).add(card_tpl);
              }
            } else {
              LogUtil.e(cardTplListWrapQcDataResponse.getMsg());
            }
            return Observable.just(ret).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
          }
        })
        .subscribe(new NetSubscribe<HashMap<String,List<CardTpl>>>() {
          @Override public void onNext(HashMap<String,List<CardTpl>> hashmap) {
            adapterLeft.clear();
            adapterRight.clear();
            mAllDatas = hashmap;
            for (String s : cardCategory) {
              if (hashmap.get(s).size() > 0){
                adapterLeft.addItem(new FilterCommonLinearItem(s,false));
              }
            }
          }
        }));
  }

  @Override public String getFragmentName() {
    return CardFilterTplFragment.class.getName();
  }

  private FlexibleAdapter.OnItemClickListener leftClickListener =
      new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
          adapterLeft.toggleSelection(position);
          adapterLeft.notifyItemChanged(position);
          adapterRight.clear();
          adapterRight.clearSelection();
          adapterRight.addItems(0,mAllDatas.get(cardCategory[position]));
          return true;
        }
      };
  private FlexibleAdapter.OnItemClickListener rightClickListener =
      new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
          //选中某一个卡种类
          return true;
        }
      };
}
