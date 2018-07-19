package cn.qingchengfit.student.bingdings;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;

public class DataBindings {

  @BindingAdapter(value = {"bindCircleSrc"},requireAll = false)
  public static void bindSrc(ImageView v,String url){
    PhotoUtils.smallCircle(v,url);
  }

  @BindingAdapter(value = {"bindSmallSrc"},requireAll = false)
  public static void bindSmallSrc(ImageView v,String url){
    PhotoUtils.small(v,url);
  }


  @BindingAdapter(value = {"civ_content"},requireAll = false)
  public static void setContent(CommonInputView v,String c){
    v.setContent(c);
  }

}
