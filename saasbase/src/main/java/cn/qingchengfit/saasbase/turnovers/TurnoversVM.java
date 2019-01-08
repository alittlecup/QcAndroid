package cn.qingchengfit.saasbase.turnovers;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.util.Pair;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.turnovers.TurnoversTimeFilterFragment.TimeType;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.LiveDataTransfer;
import cn.qingchengfit.saascommon.mvvm.SingleLiveEvent;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class TurnoversVM extends BaseViewModel {

  @Inject IStaffModel staffModel;

  public MutableLiveData<List<? extends ITurnoverFilterItemData>> getPayments() {
    return payments;
  }

  public MutableLiveData<List<? extends ITurnoverFilterItemData>> getFeature() {
    return feature;
  }

  private MutableLiveData<List<? extends ITurnoverFilterItemData>> payments =
      new MutableLiveData<>();
  private MutableLiveData<List<? extends ITurnoverFilterItemData>> feature =
      new MutableLiveData<>();

  public MutableLiveData<List<ICommonUser>> getSellers() {
    return sellers;
  }

  private MutableLiveData<List<ICommonUser>> sellers = new MutableLiveData<>();
  public LiveData<String> filterDate;
  public MutableLiveData<String> filterFeature = new MutableLiveData<>();
  public MutableLiveData<String> filterSeller = new MutableLiveData<>();
  public MutableLiveData<String> filterPayment = new MutableLiveData<>();
  public MutableLiveData<String> filterSellerId = new MutableLiveData<>();
  public MutableLiveData<String> filterFeatureType = new MutableLiveData<>();
  public MutableLiveData<String> filterPaymentChannel = new MutableLiveData<>();
  public LiveData<String> dateText;

  public final MutableLiveData<Boolean> filterVisible = new MutableLiveData<>();

  public final MutableLiveData<Integer> dateType = new MutableLiveData<>();
  public final MutableLiveData<Pair<String, String>> date = new MutableLiveData<>();
  public MutableLiveData<String> orderListUpdateTime = new MutableLiveData<>();

  public LiveData<Pair<List<ITurnoverChartData>, Float>> getChartDatas() {
    return chartDatas;
  }

  public LiveData<TurnoversChartStatDataResponse> getChartResponse() {
    return chartResponse;
  }

  public LiveData<Boolean> isRest;
  private LiveData<TurnoversChartStatDataResponse> chartResponse;
  private MediatorLiveData<Pair<List<ITurnoverChartData>, Float>> chartDatas =
      new MediatorLiveData<>();
  public MutableLiveData<TurnoversChartStatData> totalRate = new MutableLiveData<>();
  private MediatorLiveData<Map<String, Object>> params = new MediatorLiveData<>();
  private MediatorLiveData<Map<String, Object>> chartParams = new MediatorLiveData<>();
  public MutableLiveData<Boolean> chartVisible = new MutableLiveData<>();
  public SingleLiveEvent<List<TurOrderListData>> loadMoreOrderDatas = new SingleLiveEvent<>();
  public SingleLiveEvent<List<TurOrderListData>> orderDatas = new SingleLiveEvent<>();
  public MutableLiveData<Boolean> rightEnable = new MutableLiveData<>();
  public MutableLiveData<Boolean> leftEnable = new MutableLiveData<>();
  public MutableLiveData<Integer> totalCount = new MutableLiveData<>();

  @Inject public TurnoversVM() {
    filterDate = Transformations.map(dateType, input -> {
      String dateText = "";
      rightEnable.setValue(false);
      leftEnable.setValue(true);
      switch (input) {
        case TimeType.DAY:
          dateText = "今日";
          break;
        case TimeType.WEEK:
          dateText = "本周";
          break;
        case TimeType.MONTH:
          dateText = "本月";
          break;
        case TimeType.CUSTOMIZE:
          dateText = "自定义";
          leftEnable.setValue(false);
          break;
      }
      return dateText;
    });
    dateText = Transformations.map(date, input -> {
      String start = input.first;
      String end = input.second;
      if (start.equals(end)) {
        return start.replace("-", "/");
      } else {
        return start.replace("-", "/") + "-" + end.replace("-", "/");
      }
    });
    params.addSource(date, input -> updateDate(input, params));
    params.addSource(filterSellerId, id -> updateLoadPramsData("seller_id", id, params));
    params.addSource(filterFeatureType, type -> updateLoadPramsData("trade_type", type, params));
    params.addSource(filterPaymentChannel,
        channel -> updateLoadPramsData("channel", channel, params));
    isRest = Transformations.map(params, new Function<Map<String, Object>, Boolean>() {
      @Override public Boolean apply(Map<String, Object> input) {
        pageReset();
        loadMore();
        return true;
      }
    });
    chartParams.addSource(date, date -> updateDate(date, chartParams));
    chartParams.addSource(filterSellerId, id -> updateLoadPramsData("seller_id", id, chartParams));
    chartParams.addSource(filterPaymentChannel,
        channel -> updateLoadPramsData("channel", channel, chartParams));

    chartResponse = Transformations.switchMap(chartParams,
        input -> Transformations.map(loadTurnoverChartStat(input),
            input12 -> dealResource(input12) == null ? null : dealResource(input12)));

    chartDatas.addSource(chartResponse, response -> {
      totalRate.setValue(response.total);
      upDateChartDatas(response, Integer.valueOf(
          filterFeatureType.getValue() == null ? "-1" : filterFeatureType.getValue()));
    });
    chartDatas.addSource(filterFeatureType, type -> {
      upDateChartDatas(chartResponse.getValue(), Integer.valueOf(
          filterFeatureType.getValue() == null ? "-1" : filterFeatureType.getValue()));
    });

    dateType.setValue(TimeType.DAY);
    date.setValue(new Pair<>(DateUtils.getStringToday(), DateUtils.getStringToday()));
    chartVisible.setValue(false);
    rightEnable.setValue(false);
    leftEnable.setValue(true);
    filterFeature.setValue("项目");
    filterSeller.setValue("业绩归属");
    filterPayment.setValue("支付方式");

    subscribe = RxBus.getBus()
        .register(Staff.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<Staff>() {
          @Override public void onNext(Staff staff) {
            if (!StringUtils.isEmpty(turId)) {
              putTurnoverSellerId(turId, staff.getId());
            }
          }
        });
  }

  Subscription subscribe;

  @Override protected void onCleared() {
    super.onCleared();
    if (subscribe != null && !subscribe.isUnsubscribed()) {
      subscribe.unsubscribe();
      subscribe = null;
    }
  }

  public void setTurId(String turId) {
    this.turId = turId;
  }

  public String turId = "";
  int curPage = 1, totalPage = 1;

  private void pageReset() {
    curPage = 1;
    totalPage = 1;
  }

  public void loadMore() {
    if (curPage <= totalPage) {
      Map<String, Object> value = params.getValue();
      value.put("page", curPage);
      staffModel.qcGetTurnoverOrderItems(value)
          .compose(RxHelper.schedulersTransformer())
          .subscribe(response -> {
            if (ResponseConstant.checkSuccess(response)) {
              if (curPage == 1) {
                orderDatas.setValue(response.data.shop_turnovers);
              } else {
                loadMoreOrderDatas.setValue(response.data.shop_turnovers);
              }
              orderListUpdateTime.setValue(
                  DateUtils.Date2HHmm(DateUtils.formatDateFromServer(response.data.update_time)));
              curPage++;
              totalPage = response.data.pages;
              totalCount.setValue(response.data.total_count);
            } else {
              ToastUtils.show(response.getMsg());
            }
          }, throwable -> {
          });
    }else{
      loadMoreOrderDatas.setValue(null);
    }
  }

  private void upDateChartDatas(TurnoversChartStatDataResponse response, int type) {
    if (response != null && response.total != null && response.total.getAmount() > 0) {
      List<ITurnoverChartData> iTurnoverChartData;
      if (type != -1) {
        iTurnoverChartData =
            convertChartStats(filterWithTradeType(response.stat, type), response.total.getAmount());
      } else {
        iTurnoverChartData = convertChartStats(response.stat, response.total.getAmount());
      }
      chartDatas.setValue(new Pair<>(iTurnoverChartData, response.total.getAmount()));
      chartVisible.setValue(true);
    } else {
      chartVisible.setValue(false);
    }
  }

  private List<TurnoversChartStatData> filterWithTradeType(List<TurnoversChartStatData> statData,
      int traderType) {
    List<TurnoversChartStatData> data = new ArrayList<>();
    if (statData != null && !statData.isEmpty()) {
      float otherCount = 0f;
      for (TurnoversChartStatData stat : statData) {
        if (stat.getTrade_type() == traderType) {
          data.add(stat);
        } else {
          otherCount += stat.getAmount();
        }
      }
      TurnoversChartStatData other = new TurnoversChartStatData();
      other.setTrade_type(-1);
      other.setAmount(otherCount);
      data.add(other);
    }
    return data.size() >= 2 ? data : new ArrayList<>();
  }

  private List<ITurnoverChartData> convertChartStats(List<TurnoversChartStatData> stat,
      float total) {
    List<ITurnoverChartData> datas = new ArrayList<>();

    if (stat != null && !stat.isEmpty()) {
      Collections.sort(stat, (o1, o2) -> (int) (o1.getAmount() * 100 - o2.getAmount() * 100));
      for (TurnoversChartStatData data : stat) {
        TurnoverTradeType turnoverTradeType =
            TurnoversHomePage.trade_types.get(data.getTrade_type());

        if (data.getAmount() > 0 && data.getAmount() * 100 / total <= 1) {
          datas.add(new TurnoverChartStat(total / 100f, turnoverTradeType.getColor(),
              "￥" + data.getAmount() + "/" + turnoverTradeType.getDesc()));
        } else if (data.getAmount() * 100 / total > 1) {
          datas.add(new TurnoverChartStat(data.getAmount(), turnoverTradeType.getColor(),
              "￥" + data.getAmount() + "/" + turnoverTradeType.getDesc()));
        }
      }
    }

    return datas;
  }

  private void updateDate(Pair<String, String> date, MutableLiveData<Map<String, Object>> params) {
    Map<String, Object> value = params.getValue();
    if (value == null) {
      value = new HashMap<>();
    }
    value.put("from_date", date.first);
    value.put("to_date", date.second);
    params.setValue(value);
  }

  private void updateLoadPramsData(String key, String value,
      MutableLiveData<Map<String, Object>> params) {
    Map<String, Object> data = params.getValue();
    if (data == null) {
      data = new HashMap<>();
    }
    if ((value == null || value.equals("-1")) && data.containsKey(key)) {
      data.remove(key);
    } else {
      data.put(key, value);
    }
    params.setValue(data);
  }

  public void turnDateNext() {
    if (canTurnDateNext()) {
      String first = date.getValue().first;
      String second = date.getValue().second;
      String start = DateUtils.changeDate(first, 1, dateType.getValue());
      if (dateType.getValue().equals(TimeType.MONTH)) {
        date.setValue(
            new Pair<>(start, DateUtils.getEndDayOfMonth(DateUtils.formatDateFromYYYYMMDD(start))));
      } else {
        date.setValue(new Pair<>(DateUtils.changeDate(first, 1, dateType.getValue()),
            DateUtils.changeDate(second, 1, dateType.getValue())));
      }
      rightEnable.setValue(canTurnDateNext());
    }
  }

  private boolean canTurnDateNext() {
    if (dateType.getValue() != TimeType.CUSTOMIZE && !DateUtils.isOverCurrent(
        DateUtils.formatDateFromYYYYMMDD(date.getValue().second))) {
      if (dateType.getValue() == TimeType.DAY && DateUtils.getStringToday()
          .equals(date.getValue().second)) {
        return false;
      }
      return true;
    } else {
      return false;
    }
  }

  private boolean canTurnDatePre() {
    return TimeType.CUSTOMIZE != dateType.getValue();
  }

  public void turnDatePre() {
    if (canTurnDatePre()) {
      String first = date.getValue().first;
      String second = date.getValue().second;
      String start = DateUtils.changeDate(first, -1, dateType.getValue());
      if (dateType.getValue().equals(TimeType.MONTH)) {
        date.setValue(
            new Pair<>(start, DateUtils.getEndDayOfMonth(DateUtils.formatDateFromYYYYMMDD(start))));
      } else {
        date.setValue(new Pair<>(DateUtils.changeDate(first, -1, dateType.getValue()),
            DateUtils.changeDate(second, -1, dateType.getValue())));
      }
      rightEnable.setValue(true);
    }
  }

  public void loadFilterOptions() {
    staffModel.qcGetTurnoversFilterItems()
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            upDateFilterItems(response.data);
          } else {
            ToastUtils.show(response.getMsg());
          }
        }, throwable -> {
        });
  }

  private void upDateFilterItems(TurFilterResponse response) {
    TurFilterData turFilterData = new TurFilterData();
    turFilterData.setDesc("全部");
    turFilterData.setChannel(null);
    response.channels.add(0, turFilterData);
    TurnoverTradeType turnoverTradeType = new TurnoverTradeType();
    turnoverTradeType.setDesc("全部项目");
    turnoverTradeType.setTrade_type(-1);
    response.trade_types.add(0, turnoverTradeType);
    payments.setValue(response.channels);
    feature.setValue(response.trade_types);
  }

  public void loadSellerItems() {
    staffModel.getSalers().compose(RxHelper.schedulersTransformer()).subscribe(response -> {
      if (ResponseConstant.checkSuccess(response)) {
        Staff all = new Staff();
        all.setUsername("全部");
        all.setId(null);
        response.data.sellers.add(0, all);
        sellers.setValue(new ArrayList<>(response.data.sellers));
      } else {
        ToastUtils.show(response.getMsg());
      }
    }, throwable -> {
    });
  }

  private LiveData<Resource<TurOrderListResponse>> loadTurnoversOrderLists(
      Map<String, Object> params) {
    return LiveDataTransfer.fromPublisher(
        RxJavaInterop.toV2Flowable(staffModel.qcGetTurnoverOrderItems(params))
            .compose(RxHelper.schedulersTransformerFlow()));
  }

  private MutableLiveData<List<TurOrderListData>> moreItems = new MutableLiveData<>();

  private LiveData<Resource<TurnoversChartStatDataResponse>> loadTurnoverChartStat(
      Map<String, Object> params) {
    Integer value = dateType.getValue();
    if (value != null && value != TimeType.CUSTOMIZE) {
      params.put("need_growth_rate", 1);
    } else {
      params.put("need_growth_rate", 0);
    }
    return LiveDataTransfer.fromPublisher(
        RxJavaInterop.toV2Flowable(staffModel.qcGetTurnoverChartStat(params))
            .compose(RxHelper.schedulersTransformerFlow()));
  }

  public SingleLiveEvent<ITurnoverOrderItemData> getOrderItemDataMutableLiveData() {
    return orderItemDataMutableLiveData;
  }

  private SingleLiveEvent<ITurnoverOrderItemData> orderItemDataMutableLiveData =
      new SingleLiveEvent<>();

  public void putTurnoverSellerId(String turId, String sellerID) {
    if (sellerID.equals("0")) {
      sellerID = "";
    }
    staffModel.qcPutTurnoverOrderDetail(turId, sellerID)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            orderItemDataMutableLiveData.setValue(response.getData().shop_turnover);
          } else {
            ToastUtils.show("修改业绩归属失败");
          }
        }, throwable -> {
          ToastUtils.show("修改业绩归属失败");
        });
  }

  public MutableLiveData<List<TurOrderListData>> getMoreItems() {
    return moreItems;
  }
}
