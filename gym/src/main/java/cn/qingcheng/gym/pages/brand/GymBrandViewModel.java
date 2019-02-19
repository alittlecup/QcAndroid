package cn.qingcheng.gym.pages.brand;

import android.arch.lifecycle.MutableLiveData;
import cn.qingcheng.gym.vo.IGymBrandItemData;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class GymBrandViewModel extends BaseViewModel {
  public MutableLiveData<List<IGymBrandItemData>> datas = new MutableLiveData<>();

  @Inject public GymBrandViewModel() {
    testFunction();
  }

  private void testFunction() {
    List<IGymBrandItemData> data = new ArrayList<>();
    IGymBrandItemData data1 = new IGymBrandItemData() {
      @Override public String getGymName() {
        return "798店";
      }

      @Override public String getSuperUserName() {
        return "kent";
      }

      @Override public String getMyPosition() {
        return "管理员";
      }

      @Override public String getGymPhone() {
        return "1890909090";
      }

      @Override public String getGymPhoto() {
        return null;
      }

      @Override public boolean isEditAble() {
        return true;
      }
    };
    data.add(data1);
    data.add(data1);
    data.add(data1);
    data.add(data1);
    data.add(data1);
    datas.setValue(data);
  }
}
