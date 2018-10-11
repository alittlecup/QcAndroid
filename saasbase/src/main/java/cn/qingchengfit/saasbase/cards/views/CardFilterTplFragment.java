package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.item.CardCateFilterItem;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.IFlexible;
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

  RecyclerView rvLeft;
  RecyclerView rvRight;
  String[] cardCategory;
  CommonFlexAdapter adapterLeft, adapterRight;
  private int rightSelcet = 0;
  private HashMap<String,List<CardTpl>> mAllDatas = new HashMap<>();
  @Inject ICardModel cardModel;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapterLeft = new CommonFlexAdapter(new ArrayList(), leftClickListener);
    adapterRight = new CommonFlexAdapter(new ArrayList(), rightClickListener);
    adapterLeft.setMode(SelectableAdapter.Mode.SINGLE);
    adapterRight.setMode(SelectableAdapter.Mode.SINGLE);

  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card_filter_tpl, container, false);
    rvLeft = (RecyclerView) view.findViewById(R.id.rv_left);
    rvRight = (RecyclerView) view.findViewById(R.id.rv_right);
    cardCategory = getResources().getStringArray(R.array.card_filter_cardtype_category);
    adapterLeft = new CommonFlexAdapter(new ArrayList(), leftClickListener);
    adapterRight = new CommonFlexAdapter(new ArrayList(), rightClickListener);
    adapterLeft.setMode(SelectableAdapter.Mode.SINGLE);
    adapterRight.setMode(SelectableAdapter.Mode.SINGLE);
    rvLeft.setLayoutManager(new LinearLayoutManager(getContext()));
    rvRight.setLayoutManager(new LinearLayoutManager(getContext()));
    rvLeft.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withDivider(R.drawable.divider_qc_base_line)
            .withBottomEdge(true));
    rvRight.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withDivider(R.drawable.divider_grey_left_margin)
            .withBottomEdge(true));
    rvLeft.setAdapter(adapterLeft);
    rvRight.setAdapter(adapterRight);
    loadData();
    return view;
  }
  private void loadData(){
    RxRegiste(cardModel.qcGetCardFilterTpls(true)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .flatMap(
            new Func1<QcDataResponse<CardTplListWrap>, Observable<HashMap<String, List<CardTpl>>>>() {
              @Override public Observable<HashMap<String, List<CardTpl>>> call(
                  QcDataResponse<CardTplListWrap> cardTplListWrapQcDataResponse) {
                //获取到所有可以筛选的会员卡种类，然后分类
                HashMap<String, List<CardTpl>> ret = new HashMap<String, List<CardTpl>>();
                if (ResponseConstant.checkSuccess(cardTplListWrapQcDataResponse)) {
                  ret.put(cardCategory[0], new ArrayList<CardTpl>());
                  ret.put(cardCategory[1], new ArrayList<CardTpl>());
                  ret.put(cardCategory[2], new ArrayList<CardTpl>());
                  ret.put(cardCategory[3], new ArrayList<CardTpl>());
                  //全部卡种类
                  ret.get(cardCategory[0]).add(new CardTpl("全部卡种类", 0, "", "", ""));
                  ret.get(cardCategory[0]).addAll(cardTplListWrapQcDataResponse.data.card_tpls);
                  ret.get(cardCategory[1]).add(new CardTpl("全部储值卡", 1, "", "", ""));
                  ret.get(cardCategory[2]).add(new CardTpl("全部次卡", 2, "", "", ""));
                  ret.get(cardCategory[3]).add(new CardTpl("全部期限卡", 3, "", "", ""));

                  for (CardTpl card_tpl : cardTplListWrapQcDataResponse.data.card_tpls) {
                    ret.get(cardCategory[card_tpl.type]).add(card_tpl);
                  }
                } else {
                  LogUtil.e(cardTplListWrapQcDataResponse.getMsg());
                }
                return Observable.just(ret)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
              }
            })
        .subscribe(new NetSubscribe<HashMap<String, List<CardTpl>>>() {
          @Override public void onNext(HashMap<String, List<CardTpl>> hashmap) {
            adapterLeft.clear();
            adapterRight.clear();
            mAllDatas = hashmap;
            //有数据的会员卡类型添加进去
            for (String s : cardCategory) {
              if (hashmap.get(s).size() > 0) {
                adapterLeft.addItem(new CardCateFilterItem(s, false));
              }
            }

            if (leftClickListener != null) leftClickListener.onItemClick(0);
            //if (rightClickListener != null) rightClickListener.onItemClick(0);
            adapterRight.toggleSelection(0);
            adapterLeft.notifyDataSetChanged();
            adapterRight.notifyDataSetChanged();
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
          adapterLeft.notifyDataSetChanged();
          adapterRight.clear();
          adapterRight.clearSelection();
          for (CardTpl cardTpl : mAllDatas.get(cardCategory[position])) {
            adapterRight.addItem(new FilterCommonLinearItem(cardTpl.getName()));
          }
          return true;
        }
      };
  private FlexibleAdapter.OnItemClickListener rightClickListener =
      new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
          //选中某一个卡种类
          rightSelcet = position;
          adapterRight.toggleSelection(position);
          adapterRight.notifyDataSetChanged();
          int x = 0;
          try {
            x = adapterLeft.getSelectedPositions().get(0);
          } catch (Exception e) {

          }
          IFlexible item = adapterRight.getItem(position);
          if (item instanceof FilterCommonLinearItem) {
            RxBus.getBus().post(mAllDatas.get(cardCategory[x]).get(position));
          }
          return true;
        }
      };
}
