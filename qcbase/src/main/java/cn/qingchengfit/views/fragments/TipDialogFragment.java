package cn.qingchengfit.views.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;

/**
 * Created by fb on 2017/7/10.
 */

//dialog 最上方为icon的提示对话框
public class TipDialogFragment extends DialogFragment {

	ImageView imgCloseTips;
	ImageView imgTipsTop;
	TextView textTips;
	TextView textButtonClick;


  private OnDialogListener onDialogListener;

  public static TipDialogFragment newInstance(String textTips, String buttonText, int drawableId) {
    Bundle args = new Bundle();
    args.putString("tips", textTips);
    args.putString("button", buttonText);
    args.putInt("img", drawableId);
    TipDialogFragment fragment = new TipDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tips_dialog, container, false);
    imgCloseTips = (ImageView) view.findViewById(R.id.img_close_tips);
    imgTipsTop = (ImageView) view.findViewById(R.id.img_tips_top);
    textTips = (TextView) view.findViewById(R.id.text_tips);
    textButtonClick = (TextView) view.findViewById(R.id.text_button_click);
    view.findViewById(R.id.text_button_click).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onDoClick(v);
      }
    });
    view.findViewById(R.id.img_close_tips).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCloseDialog(v);
      }
    });

    initView();
    return view;
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

  public void setOnDialogListener(OnDialogListener onClickListener) {
    this.onDialogListener = onClickListener;
  }

  private void initView() {
    if (getArguments().containsKey("tips") && !TextUtils.isEmpty(
        getArguments().getString("tips"))) {
      textTips.setText(getArguments().getString("tips"));
    }
    if (getArguments().containsKey("img") && getArguments().getInt("img", 0) != 0) {
      imgTipsTop.setImageResource(getArguments().getInt("img", 0));
    }
    if (getArguments().containsKey("button") && !TextUtils.isEmpty(
        getArguments().getString("button"))) {
      textButtonClick.setText(getArguments().getString("button"));
    }
  }

 public void onDoClick(View v) {
    if (onDialogListener != null) {
      onDialogListener.onDoClick(v);
      dismiss();
    }
  }

  @Override public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    if (onDialogListener != null){
      onDialogListener.onDismissListener();
    }
  }

 public void onCloseDialog(View v) {
    dismiss();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

  }

  public interface OnDialogListener {
    void onDoClick(View v);

    void onDismissListener();
  }
}
