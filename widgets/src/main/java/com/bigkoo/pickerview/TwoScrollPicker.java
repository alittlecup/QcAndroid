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
import com.bigkoo.pickerview.lib.WheelView;
import java.util.ArrayList;

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

public class TwoScrollPicker extends Dialog {
    private LayoutInflater mLayoutInflater;
    private View rootView;
    private WheelView wheelviewleft;
    private WheelView wheelviewright;
    private TwoSelectItemListener listener;

    public TwoScrollPicker(@NonNull Context context) {
        this(context, true, null);
    }

    public TwoScrollPicker(@NonNull Context context, @StyleRes int themeResId) {
        this(context, true, null);
    }

    protected TwoScrollPicker(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, R.style.ChoosePicDialogStyle);
        init(context);
    }

    private void init(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.dialog_two_picker, null);
        wheelviewleft = (WheelView) rootView.findViewById(R.id.left);
        wheelviewright = (WheelView) rootView.findViewById(R.id.right);
        rootView.findViewById(R.id.btn_comfirm).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (listener != null) {
                    listener.onSelectItem(wheelviewleft.getCurrentItem(), wheelviewright.getCurrentItem());
                }
                dismiss();
            }
        });
    }

    public void show(ArrayList<String> leftdatas, ArrayList<String> rightdatas, int leftPos, int rightPos) {
        wheelviewleft.setAdapter(new ArrayWheelAdapter<String>(leftdatas));// 设置"年"的显示数据
        wheelviewleft.TEXT_SIZE = 50;
        wheelviewleft.setCyclic(false);
        wheelviewleft.setCurrentItem(leftPos);
        wheelviewright.setAdapter(new ArrayWheelAdapter<>(rightdatas));
        wheelviewright.TEXT_SIZE = 50;
        wheelviewright.setCyclic(false);
        wheelviewright.setCurrentItem(rightPos);
        show();
    }

    public TwoSelectItemListener getListener() {
        return listener;
    }

    public void setListener(TwoSelectItemListener listener) {
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

    public interface TwoSelectItemListener {
        void onSelectItem(int left, int right);
    }
}
