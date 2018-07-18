package cn.qingchengfit.saascommon.views.common_user_select;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.item.CommonUserItem;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.List;

public class CommonUserSelectVM  extends BaseViewModel{
  MutableLiveData<List<CommonUserItem>> selectItems = new MutableLiveData<>();


}
