package cn.qingchengfit.shop.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.ui.widget.Corner4dpImgWrapper;
import cn.qingchengfit.shop.vo.RecordAction;
import cn.qingchengfit.utils.PhotoUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

/**
 * Created by huangbaole on 2018/1/29.
 */

public final class ViewUtil {
  @MainThread public static void resetRecyclerViewHeight(RecyclerView recyclerView) {
    ;
    recyclerView.post(new Runnable() {
      @Override public void run() {
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        View childAt = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        layoutParams.height = childAt.getHeight() * recyclerView.getAdapter().getItemCount();
        recyclerView.setLayoutParams(layoutParams);
      }
    });
  }

  public static MaterialDialog instanceDelDialog(Context context, String content,
      MaterialDialog.SingleButtonCallback callback) {
    return new MaterialDialog.Builder(context).content(content)
        .autoDismiss(true)
        .positiveColorRes(cn.qingchengfit.saasbase.R.color.primary)
        .negativeColorRes(cn.qingchengfit.widgets.R.color.text_color_gray)
        .negativeText(cn.qingchengfit.widgets.R.string.pickerview_cancel)
        .positiveText(cn.qingchengfit.widgets.R.string.pickerview_submit)
        .onPositive(callback)
        .build();
  }
  public static MaterialDialog instanceDelDialog(Context context, String content,
      MaterialDialog.SingleButtonCallback poscallback,MaterialDialog.SingleButtonCallback navcallback) {
    return new MaterialDialog.Builder(context).content(content)
        .autoDismiss(true)
        .positiveColorRes(cn.qingchengfit.saasbase.R.color.primary)
        .negativeColorRes(cn.qingchengfit.widgets.R.color.text_color_gray)
        .negativeText(cn.qingchengfit.widgets.R.string.pickerview_cancel)
        .positiveText(cn.qingchengfit.widgets.R.string.pickerview_submit)
        .onPositive(poscallback)
        .onNegative(navcallback)
        .build();
  }

  public  static void setOperatorType(@RecordAction int action,TextView textView){
    Resources resources = textView.getContext().getResources();
    switch (action){
      case  RecordAction.ADD:
        textView.setText(resources.getString(R.string.inventory_add));
        textView.setTextColor(resources.getColor(R.color.primary));
        break;
      case  RecordAction.REDUCE:
        textView.setText(resources.getString(R.string.inventory_reduce));
        textView.setTextColor(resources.getColor(R.color.inventory_reduce));
        break;
      case  RecordAction.SALED:
        textView.setText(resources.getString(R.string.inventory_sale));
        textView.setTextColor(resources.getColor(R.color.inventory_reduce));
        break;
      case  RecordAction.RETURN:
        textView.setText(resources.getString(R.string.inventory_return));
        textView.setTextColor(resources.getColor(R.color.primary));
        break;
    }
  }
  public static void smallCornner4dp(ImageView v, String url){
    Glide.with(v.getContext())
        .load(PhotoUtils.getSmall(url))
        .asBitmap()
        .placeholder(cn.qingchengfit.widgets.R.drawable.img_loadingimage)
        .error(cn.qingchengfit.widgets.R.drawable.img_loadingimage)
        .into(new Corner4dpImgWrapper(v, v.getContext()));
  }

}
