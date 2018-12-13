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

  @Inject public TurnoversVM() {
    payments.setValue(initTestDataPay());
    feature.setValue(initTestDatProject());
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
