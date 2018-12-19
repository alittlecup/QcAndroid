package cn.qingchengfit.saasbase.turnovers;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class TurnoversVM extends BaseViewModel {
  public MutableLiveData<List<ITurnoverFilterItemData>> getPayments() {
    return payments;
  }

  public MutableLiveData<List<ITurnoverFilterItemData>> getFeature() {
    return feature;
  }

  private MutableLiveData<List<ITurnoverFilterItemData>> payments = new MutableLiveData<>();
  private MutableLiveData<List<ITurnoverFilterItemData>> feature = new MutableLiveData<>();
  public MutableLiveData<String> filterDate = new MutableLiveData<>();
  public MutableLiveData<String> filterFeature = new MutableLiveData<>();
  public MutableLiveData<String> filterSeller = new MutableLiveData<>();
  public MutableLiveData<String> filterPayment = new MutableLiveData<>();

  public final MutableLiveData<Boolean> filterVisible = new MutableLiveData<>();


  public MutableLiveData<List<ITurnoverOrderItemData>> getOrderDatas() {
    return orderDatas;
  }

  private MutableLiveData<List<ITurnoverOrderItemData>> orderDatas = new MutableLiveData<>();

  @Inject public TurnoversVM() {
    payments.setValue(initTestDataPay());
    feature.setValue(initTestDatProject());
    orderDatas.setValue(initTestOrderDatas());
  }

  private List<ITurnoverOrderItemData> initTestOrderDatas() {
    List<ITurnoverOrderItemData> data = new ArrayList<>();
    data.add(new TestData(""));
    for (int i = 0; i < 10; i++) {
      data.add(new TestData("payment index  " + i));
    }
    return data;
  }

  private List<ITurnoverFilterItemData> initTestDataPay() {
    List<ITurnoverFilterItemData> data = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      data.add(new TestData("payment index  " + i));
    }
    return data;
  }

  private List<ITurnoverFilterItemData> initTestDatProject() {
    List<ITurnoverFilterItemData> data = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      data.add(new TestData("项目 index  " + i));
    }
    return data;
  }
}
