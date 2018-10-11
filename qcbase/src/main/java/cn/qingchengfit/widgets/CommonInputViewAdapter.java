package cn.qingchengfit.widgets;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.text.TextUtils;
import cn.qingchengfit.widgets.CommonInputView;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/12/19.
 */
@InverseBindingMethods(@InverseBindingMethod(type = CommonInputView.class,
    attribute = "civ_content", event = "civ_contentAttrChanged", method = "getCivContent"))
public class CommonInputViewAdapter {
  @BindingAdapter(value = {"civ_content"})
  public static void setContent(CommonInputView v,String content){
    if (!TextUtils.isEmpty(content) && content.equalsIgnoreCase(v.getContent())){
      return;
    }
    v.setContent(content);
  }

  @BindingAdapter(value = { "civ_showright" })
  public static void setCommonInputViewShowRight(CommonInputView inputView, Boolean showRight) {
    inputView.setShowRight(showRight);
  }

  @BindingAdapter(value = { "civ_lable" }) public static void setCommonInputView(
      CommonInputView inputView, String label) {
    inputView.setLabel(label);
  }

  @BindingAdapter(value = {"civ_contentAttrChanged"}, requireAll = false)
  public static void setCivContentChanged(CommonInputView view, InverseBindingListener bindingListener){

    CommonInputView.OnTextChangedListener listener;

    if (bindingListener == null){
      listener = null;
    }else {
      listener = bindingListener::onChange;
    }
    if (listener != null) {
      view.setOnTextChangedListener(listener);
    }
  }

  @InverseBindingAdapter(attribute = "civ_content", event = "civ_contentAttrChanged")
  public static String getCivContent(CommonInputView view){
    return view.getContent();
  }

}
