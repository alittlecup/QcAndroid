package cn.qingcheng.gym.pages.my;

import android.arch.lifecycle.MutableLiveData;
import cn.qingcheng.gym.vo.IMyGymsItemData;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MyGymsViewModel extends BaseViewModel {

  public MutableLiveData<List<IMyGymsItemData>> datas = new MutableLiveData<>();

  @Inject public MyGymsViewModel() {
    testData();
  }

  private void testData() {
    List<IMyGymsItemData> data = new ArrayList<>();
    IMyGymsItemData data1 = new IMyGymsItemData() {
      @Override public String getBrandName() {
        return " 引力工厂";
      }

      @Override public String getBrandID() {
        return "123123";
      }

      @Override public String getBrandCreateName() {
        return "刘姐";
      }

      @Override public String getBrandPhoto() {
        return null;
      }

      @Override public boolean isManaged() {
        return true;
      }
    };
    data.add(data1);
    data.add(data1);
    data.add(data1);
    data.add(data1);
    datas.setValue(data);
  }
}
