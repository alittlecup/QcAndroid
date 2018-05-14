//package cn.qingchengfit.saasbase.common.bindingadapter;
//
//import android.databinding.BindingAdapter;
//import android.databinding.InverseBindingListener;
//import android.databinding.InverseBindingMethod;
//import android.databinding.InverseBindingMethods;
//import android.databinding.ObservableField;
//import android.text.Editable;
//import android.text.TextWatcher;
//import cn.qingchengfit.widgets.CommonInputView;
//
///**
// * 在使用的时候应该注意的是要加'='
// * app:civ_content="@={content}"
// *
// * content为{@link ObservableField}
// *
// * Created by huangbaole on 2017/12/20.
// */
//@InverseBindingMethods({
//    @InverseBindingMethod(type = cn.qingchengfit.widgets.CommonInputView.class, attribute = "civ_content", event = "onConentChange", method = "getContent"),
//}) public class CommonInputViewAdapter {
//
//  //@InverseBindingAdapter(attribute = "civ_content",event = "onConentChange")
//  //public static String getTextString(CommonInputView inputView){
//  //  return inputView.getContent();
//  //}
//  @BindingAdapter(value = { "civ_lable" }) public static void setCommonInputView(
//      CommonInputView inputView, String label) {
//    inputView.setLabel(label);
//  }
//
//  @BindingAdapter(value = { "civ_content" })
//  public static void setCommonInputViewContent(CommonInputView inputView, String content) {
//    if (!inputView.getContent().equals(content)) {
//      inputView.setContent(content);
//    }
//  }
//
//  @BindingAdapter(value = { "civ_showright" })
//  public static void setCommonInputViewShowRight(CommonInputView inputView, Boolean showRight) {
//    inputView.setShowRight(showRight);
//  }
//
//  @BindingAdapter(value = "onConentChange")
//  public static void setContentChangeListener(CommonInputView inputView,
//      InverseBindingListener listener) {
//    inputView.addTextWatcher(new TextWatcher() {
//      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//      }
//
//      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//      }
//
//      @Override public void afterTextChanged(Editable s) {
//        listener.onChange();
//      }
//    });
//  }
//}
