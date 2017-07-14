package cn.qingchengfit.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/10/28 2015.
 */
public class DialogSheet extends Dialog {

    private LinearLayout view;
    private Context context;

    public DialogSheet(Context context) {
        super(context, R.style.ChoosePicDialogStyle);
        this.context = context;
        view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        this.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public DialogSheet(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogSheet(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static DialogSheet builder(Context context) {
        return new DialogSheet(context);
    }

    public DialogSheet addButton(String text, View.OnClickListener listener) {
        TextView textView = new TextView(context, null, R.style.Qc_TextCommonBlack);
        textView.setTextColor(getContext().getResources().getColor(R.color.text_black));
      textView.setPadding(30, textView.getPaddingTop(), textView.getPaddingRight(),
          textView.getPaddingBottom());
      textView.setText(text);
      textView.setGravity(Gravity.CENTER_VERTICAL);
      textView.setBackgroundResource(R.drawable.qc_backgroud_selector);
      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              (int) context.getResources().getDimension(R.dimen.qc_item_height));
      view.addView(textView, params);
      textView.setOnClickListener(listener);
      return this;
    }

  public DialogSheet addButton(String text, @ColorRes int textColor,
      View.OnClickListener listener) {
    TextView textView = new TextView(context, null, R.style.Qc_TextCommonBlack);
    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black));
        textView.setPadding(30, textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setBackgroundResource(R.drawable.qc_backgroud_selector);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            (int) context.getResources().getDimension(R.dimen.qc_item_height));
        view.addView(textView, params);
        textView.setOnClickListener(listener);
        return this;
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
        super.show();
    }
}
