package cn.qingchengfit.saascommon.filter;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import java.util.HashMap;
import javax.inject.Inject;

/**
 * Created by fb on 2017/11/20.
 */

public class FilterViewModel extends ViewModel{

  public MutableLiveData<HashMap<String, Object>> filtlerData;
  @Inject
  public FilterViewModel(){
  }

  public MutableLiveData<HashMap<String, Object>> getFitlerData() {
    if (filtlerData == null){
      filtlerData = new MediatorLiveData<>();
    }
    return filtlerData;
  }


  public interface OnFilterChangeListener{
    void onChanged(HashMap<String, Object> changedMap);
  }

}
