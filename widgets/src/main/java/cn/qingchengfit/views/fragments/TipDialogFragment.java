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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import javax.inject.Inject;

/**
 * Created by fb on 2017/7/10.
 */

//dialog 最上方为icon的提示对话框
public class TipDialogFragment extends DialogFragment {

  @BindView(R2.id.img_close_tips) ImageView imgCloseTips;
  @BindView(R2.id.img_tips_top) ImageView imgTipsTop;
  @BindView(R2.id.text_tips) TextView textTips;
  @BindView(R2.id.text_button_click) TextView textButtonClick;

  Unbinder unbinder;
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
    unbinder = ButterKnife.bind(this, view);
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

  @OnClick(R2.id.text_button_click) public void onDoClick(View v) {
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

  @OnClick(R2.id.img_close_tips) public void onCloseDialog(View v) {
    dismiss();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  public interface OnDialogListener {
    void onDoClick(View v);

    void onDismissListener();
  }
}