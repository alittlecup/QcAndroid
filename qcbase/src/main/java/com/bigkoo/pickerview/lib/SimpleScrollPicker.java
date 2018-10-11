package com.bigkoo.pickerview.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import cn.qingchengfit.widgets.R;
import com.bigkoo.pickerview.BaseWheelDialog;
import com.bigkoo.pickerview.BaseWheelDialog.OnOptionsSelectListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/11.
 */

public class SimpleScrollPicker extends BaseWheelDialog {

  private String label;

  public SimpleScrollPicker(@NonNull Context context) {
    this(context, -1);
  }

  public SimpleScrollPicker(@NonNull Context context, @StyleRes int themeResId) {
    this(context, -1, true, null);
  }

  public SimpleScrollPicker(@NonNull Context context, @StyleRes int themeResId, boolean cancelable,
      @Nullable OnCancelListener cancelListener) {
    super(context);
    setCanceledOnTouchOutside(cancelable);
    if (cancelListener != null) {
      rootView.findViewById(R.id.btn_cancel)
          .setOnClickListener(v -> cancelListener.onCancel(SimpleScrollPicker.this));
    }
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void show(List<String> datas, int pos) {
    setPicker(new ArrayList<>(datas));
    setSelectOptions(pos);
    setLabels(label);
    show();
  }

  public void show(int min, int max, int pos) {
    show(min, max, 1, pos);
  }

  public void show(int min, int max, int interval, int pos) {
    ArrayList<String> datas = new ArrayList<>();
    for (int i = 0; i < interval; i++) {
      while (min <= max) {
        datas.add(Integer.toString(min));
        min++;
      }
    }
    show(datas, pos);
  }

  public SelectItemListener getListener() {
    return listener;
  }

  private SelectItemListener listener;

  public void setListener(SelectItemListener listener) {
    this.listener = listener;
    setOnoptionsSelectListener((options1, option2, options3) -> {
      if (listener != null) {
        listener.onSelectItem(options1);
      }
    });
  }

  public interface SelectItemListener {
    void onSelectItem(int pos);
  }
}
