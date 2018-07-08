package cn.qingchengfit.saascommon.mvvm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * ViewModel 的基本类，用于添加和解除订阅
 * <p>
 * 关于ViewModel中持有的数据类型
 * 1. 用于和UI进行双向绑定的Observable
 * 2. 用于保存当前A/F中的临时变量String,int 等
 * 3. 用于保存数据并且进行相互通知的LiveData
 * <p>
 * <p>
 * Created by huangbaole on 2017/11/15.
 */

public class BaseViewModel extends ViewModel {
  public MutableLiveData<String> getErrorMsg() {
    return errormsg;
  }

  private MutableLiveData<String> errormsg = new MutableLiveData<>();

  public <T> T dealResource(Resource<T> resource) {
    switch (resource.status) {
      case SUCCESS:
        return resource.data;
      case ERROR:
        errormsg.setValue(resource.message);
        break;
      case LOADING:
        break;
    }
    return null;
  }

}
