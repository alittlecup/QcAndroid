package cn.qingchengfit.saascommon.views.commonuserselect;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.saascommon.item.CommonUserItem;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.List;

public class CommonUserSelectVM  extends BaseViewModel{
  public MutableLiveData<List<CommonUserItem>> selectItems = new MutableLiveData<>();
  public MutableLiveData<List<CommonUserItem>> items = new MutableLiveData<>();

  public void addPosition(){

  }

}
