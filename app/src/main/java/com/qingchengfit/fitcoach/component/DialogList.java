package com.qingchengfit.fitcoach.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.MeasureUtils;
import com.qingchengfit.fitcoach.R;

import java.util.List;

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
public class DialogList extends Dialog {

    private LinearLayout view;
    private Context context;

    public DialogList(Context context) {
        super(context, R.style.ChoosePicDialogStyle);
        this.context = context;
        view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        this.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public DialogList(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogList(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static DialogList builder(Context context) {
        return new DialogList(context);
    }

    public DialogList title(String title) {
        TextView textView = new TextView(context, null, R.style.Qc_TextCommonGrey);
//        textView.setTextAppearance(R.style.Qc_TextCommonBlack);
        textView.setTextColor(context.getResources().getColor(R.color.text_grey));
        textView.setTextSize(14);
        textView.setPadding(MeasureUtils.dpToPx(30f, context.getResources()), textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
        textView.setText(title);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.qc_item_height));
        view.addView(textView, 0, params);
        return this;
    }

    public DialogList list(List<String> list, AdapterView.OnItemClickListener listener) {
        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_only_text, list);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
        listView.setOnItemClickListener(listener);
        int height = 0;
        if (list.size() < 6) {
            height = MeasureUtils.dpToPx(48f, context.getResources()) * list.size();
        } else {
            height = MeasureUtils.dpToPx(48f, context.getResources()) * 6;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        view.addView(listView, params);
        return this;
    }

    @Override
    public void show() {
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
