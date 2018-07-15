package com.bigkoo.pickerview.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import cn.qingchengfit.widgets.R;
import com.bigkoo.pickerview.BaseWheelDialog;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2018/4/11.
 */

public class TwoScrollPicker extends BaseWheelDialog {

  private TwoSelectItemListener listener;

  public TwoScrollPicker(@NonNull Context context) {
    this(context, -1);
  }

  public TwoScrollPicker(@NonNull Context context, @StyleRes int themeResId) {
    this(context, -1, true, null);
  }

  protected TwoScrollPicker(@NonNull Context context, @StyleRes int themeResId, boolean cancelable,
      @Nullable OnCancelListener cancelListener) {
    super(context);
    setCanceledOnTouchOutside(cancelable);
    if (cancelListener != null) {
      rootView.findViewById(R.id.btn_cancel)
          .setOnClickListener(v -> cancelListener.onCancel(TwoScrollPicker.this));
    }
  }

  @Override protected int getLayoutId() {
    return R.layout.two_scroll_picker;
  }

  public void show(ArrayList<String> leftdatas, ArrayList<String> rightdatas, int leftPos,
      int rightPos) {
    show(leftdatas, rightdatas, leftPos, rightPos, null);
  }

  public void show(ArrayList<String> leftdatas, ArrayList<String> rightdatas, int leftPos,
      int rightPos, String label) {
    ArrayList<ArrayList<String>> rightDatas = new ArrayList<>();
    rightDatas.add(rightdatas);
    setPicker(leftdatas, rightDatas, false);
    setSelectOptions(leftPos, rightPos);
    setLabels(label, label);
    show();
  }

  public void show(int minL, int maxL, int minR, int maxR, int interval, int posL, int posR) {
    show(getDatas(minL, maxL, interval), getDatas(minR, maxR, interval), posL, posR);
  }

  private ArrayList<String> getDatas(int min, int max, int interval) {
    ArrayList<String> datas = new ArrayList<>();
    for (int i = 0; i < interval; i++) {
      while (min <= max) {
        datas.add(Integer.toString(min));
        min++;
      }
    }
    return datas;
  }


  public TwoSelectItemListener getListener() {
    return listener;
  }

  public void setListener(TwoSelectItemListener listener) {
    this.listener = listener;
    setOnoptionsSelectListener((options1, option2, options3) -> {
      if (listener != null) {
        listener.onSelectItem(options1, option2);
      }
    });
  }

  public interface TwoSelectItemListener {
    void onSelectItem(int left, int right);
  }
}
