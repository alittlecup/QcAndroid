package com.bigkoo.pickerview;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import cn.qingchengfit.widgets.R;
import com.bigkoo.pickerview.lib.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.NumericWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/6/8.
 */

public class SimpleScrollPicker extends Dialog {
    private LayoutInflater mLayoutInflater;
    private View rootView;
    private WheelView wheelview;
    private SelectItemListener listener;
    private String label = "";

    public SimpleScrollPicker(@NonNull Context context) {
        this(context, true, null);
    }

    public SimpleScrollPicker(@NonNull Context context, @StyleRes int themeResId) {
        this(context, true, null);
    }

    protected SimpleScrollPicker(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, R.style.ChoosePicDialogStyle);
        init(context);
    }

    private void init(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.dialog_simple_picker, null);
        wheelview = (WheelView) rootView.findViewById(R.id.wheelview);
        rootView.findViewById(R.id.btn_comfirm).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (listener != null) {
                    listener.onSelectItem(wheelview.getCurrentItem());
                }
                dismiss();
            }
        });
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void show(List<String> datas, int pos) {
        wheelview.setAdapter(new ArrayWheelAdapter<String>((ArrayList<String>) datas));// 设置"年"的显示数据
        wheelview.TEXT_SIZE = 50;
        wheelview.setCyclic(false);
        wheelview.setLabel(label);// 添加文字
        wheelview.setCurrentItem(pos);
        show();
    }

    public void show(int min, int max, int pos) {
        show(min, max, 1, pos);
    }

    public void show(int min, int max, int interval, int pos) {
        wheelview.setAdapter(new NumericWheelAdapter(min, max, interval));// 设置"年"的显示数据
        wheelview.TEXT_SIZE = 50;
        wheelview.setCyclic(false);
        wheelview.setCurrentItem(pos);
        wheelview.setLabel(label);// 添加文字

        show();
    }

    public SelectItemListener getListener() {
        return listener;
    }

    public void setListener(SelectItemListener listener) {
        this.listener = listener;
    }

    @Override public void show() {
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        window.setContentView(rootView);
        super.show();
    }

    public interface SelectItemListener {
        void onSelectItem(int pos);
    }
}
