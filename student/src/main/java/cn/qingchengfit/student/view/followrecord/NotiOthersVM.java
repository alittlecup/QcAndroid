package cn.qingchengfit.student.view.followrecord;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import javax.inject.Inject;

public class NotiOthersVM extends BaseViewModel {
  @Inject  public NotiOthersVM() {
  }

  public MutableLiveData<String> getEditAfterTextChange() {
    return editAfterTextChange;
  }

  private MutableLiveData<String> editAfterTextChange = new MutableLiveData<>();

  /**
   * 输入框输入监听
   */
  public void onEditTextChange(String text) {
    editAfterTextChange.setValue(text);
  }

}
