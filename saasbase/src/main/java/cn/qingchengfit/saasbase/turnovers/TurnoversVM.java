package cn.qingchengfit.saasbase.turnovers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.util.Pair;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.turnovers.TurnoversTimeFilterFragment.TimeType;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.LiveDataTransfer;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

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

  public LiveData<List<? extends ITurnoverOrderItemData>> getOrderDatas() {
    return orderDatas;
  }

  public MutableLiveData<List<? extends TurnoversChartStatData>> chartDatas =
      new MutableLiveData<>();

  private LiveData<List<? extends ITurnoverOrderItemData>> orderDatas;
  private MediatorLiveData<Map<String, Object>> params = new MediatorLiveData<>();

  @Inject public TurnoversVM() {
    filterDate = Transformations.map(dateType, input -> {
      String dateText = "";
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
    params.addSource(date, this::updateDate);
    params.addSource(filterSellerId, id -> updateLoadPramsData("seller_id", id));
    params.addSource(filterFeatureType, type -> updateLoadPramsData("trade_type", type));
    params.addSource(filterPaymentChannel, channel -> updateLoadPramsData("channel", channel));

    dateType.setValue(TimeType.DAY);
    date.setValue(new Pair<>(DateUtils.getStringToday(), DateUtils.getStringToday()));

    filterFeature.setValue("项目");
    filterSeller.setValue("业绩归属");
    filterPayment.setValue("支付方式");

    orderDatas = Transformations.switchMap(params,
        input -> Transformations.map(loadTurnoversOrderLists(input),
            input1 -> dealResource(input1) == null ? null : dealResource(input1).shop_turnovers));
  }

  private void updateDate(Pair<String, String> date) {
    Map<String, Object> value = params.getValue();
    if (value == null) {
      value = new HashMap<>();
    }
    value.put("from_date", date.first);
    value.put("to_date", date.second);
    params.setValue(value);
  }

  private void updateLoadPramsData(String key, String value) {
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
    }
  }

  private boolean canTurnDateNext() {
    if (dateType.getValue() != TimeType.CUSTOMIZE && !DateUtils.isOverCurrent(
        DateUtils.formatDateFromYYYYMMDD(date.getValue().second))) {
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
}
