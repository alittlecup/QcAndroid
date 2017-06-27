package cn.qingchengfit.staffkit.views.signin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.SignInCancelEvent;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.utils.MeasureUtils;

/**
 * Created by yangming on 16/8/30.
 */
public class CancelDialogFragment extends BaseDialogFragment {

    @BindView(R.id.cancel) TextView cancel;
    @BindView(R.id.confirm) TextView confirm;

    private int checkInId = 0;

    public static CancelDialogFragment newInstance(int checkInId) {
        CancelDialogFragment fragment = new CancelDialogFragment();
        Bundle args = new Bundle();
        args.putInt("id", checkInId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ChoosePicDialogStyle);
        if (getArguments() != null) {
            checkInId = getArguments().getInt("id");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_sign_in_cancel, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = MeasureUtils.dpToPx(270f, getResources());
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //        wlp.height = MeasureUtils.dpToPx(245f, getResources());
        window.setAttributes(wlp);
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_rect_signin_cancel_dialog));
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        return dialog;
    }

    @OnClick({ R.id.cancel, R.id.confirm }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                RxBus.getBus().post(new SignInCancelEvent(checkInId));
                dismiss();
                break;
        }
    }
}
