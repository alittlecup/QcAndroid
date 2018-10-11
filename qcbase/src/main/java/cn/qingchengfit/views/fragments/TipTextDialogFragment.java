package cn.qingchengfit.views.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;

/**
 * Created by fb on 2017/10/11.
 */

public class TipTextDialogFragment extends DialogFragment {


	TextView textTipsTop;
	TextView textTips;
	TextView textButtonClick;
  private OnConfirmListener onConfirmListener;

  public static TipTextDialogFragment newInstance(String textTips, String buttonText, String topTips) {
    Bundle args = new Bundle();
    args.putString("tips", textTips);
    args.putString("button", buttonText);
    args.putString("topTips", topTips);
    TipTextDialogFragment fragment = new TipTextDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tips_text_dialog, container, false);
    textTipsTop = (TextView) view.findViewById(R.id.text_tips_top);
    textTips = (TextView) view.findViewById(R.id.text_tips);
    textButtonClick = (TextView) view.findViewById(R.id.text_button_click);
    view.findViewById(R.id.text_button_click).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onConfirm();
      }
    });

    initView();
    return view;
  }

  public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
    this.onConfirmListener = onConfirmListener;
  }


  public void onConfirm(){
    dismiss();
    if (onConfirmListener != null){
      onConfirmListener.onConfirm();
    }
  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    return dialog;

  }

  @Override public void onStart() {
    super.onStart();
    Dialog dialog = getDialog();
    if (dialog != null) {
      DisplayMetrics dm = new DisplayMetrics();
      getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
      dialog.getWindow()
          .setLayout((int) (dm.widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
    }
  }

  private void initView() {
    if (getArguments().containsKey("tips") && !TextUtils.isEmpty(
        getArguments().getString("tips"))) {
      textTips.setText(getArguments().getString("tips"));
    }
    if (getArguments().containsKey("topTips") && !TextUtils.isEmpty(
        getArguments().getString("topTips"))) {
      textTipsTop.setText(getArguments().getString("topTips"));
    }
    if (getArguments().containsKey("button") && !TextUtils.isEmpty(
        getArguments().getString("button"))) {
      textButtonClick.setText(getArguments().getString("button"));
    }
  }

  @Override public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public interface OnConfirmListener{
    void onConfirm();
  }
}
