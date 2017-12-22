package cn.qingchengfit.saasbase.cards.views;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import com.hannesdorfmann.fragmentargs.annotation.Arg;

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
 * Created by Paper on 16/3/25 2016.
 */
     public class BottomPayDialog extends BaseDialogFragment {
    public Unbinder unbinder;
    @Arg boolean hasEditPermission;
    @BindView(R2.id.pay_offline) TextView payOffline;
    @BindView(R2.id.cash) RelativeLayout cash;
    @BindView(R2.id.card) RelativeLayout card;
    @BindView(R2.id.transfer) RelativeLayout transfer;
    @BindView(R2.id.others) RelativeLayout others;

    public static BottomPayDialog newInstance(boolean hasEditPermission) {
        Bundle args = new Bundle();
        args.putBoolean("permission", hasEditPermission);
        BottomPayDialog fragment = new BottomPayDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ChoosePicDialogStyle);
        if (getArguments() != null){
            hasEditPermission = getArguments().getBoolean("permission");
        }
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        //        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_bottom, container, false);
        unbinder = ButterKnife.bind(this, view);
        payOffline.setVisibility(hasEditPermission ? View.VISIBLE : View.GONE);
        cash.setVisibility(hasEditPermission ? View.VISIBLE : View.GONE);
        card.setVisibility(hasEditPermission ? View.VISIBLE : View.GONE);
        transfer.setVisibility(hasEditPermission ? View.VISIBLE : View.GONE);
        others.setVisibility(hasEditPermission ? View.VISIBLE : View.GONE);
        return view;
    }

    @Override public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick({ R2.id.wechat_code, R2.id.wechat_pay, R2.id.cash, R2.id.card, R2.id.transfer, R2.id.others }) public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.wechat_code) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                IntentUtils.instanceStringIntent("0"));
        } else if (i == R.id.wechat_pay) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                IntentUtils.instanceStringIntent("1"));
        } else if (i == R.id.cash) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                IntentUtils.instanceStringIntent("2"));
        } else if (i == R.id.card) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                IntentUtils.instanceStringIntent("3"));
        } else if (i == R.id.transfer) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                IntentUtils.instanceStringIntent("4"));
        } else if (i == R.id.others) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                IntentUtils.instanceStringIntent("5"));
        }
        dismiss();
    }
}
