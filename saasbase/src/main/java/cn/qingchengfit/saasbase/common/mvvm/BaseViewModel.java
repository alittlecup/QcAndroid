package cn.qingchengfit.saasbase.common.mvvm;

import android.arch.lifecycle.ViewModel;


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

}
