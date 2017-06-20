package cn.qingchengfit.recruit.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.utils.LogUtil;

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
 * Created by Paper on 2017/5/27.
 */
public class DialogSendResumeFragment extends DialogFragment {

    @BindView(R2.id.tv_content) TextView tvContent;
    @BindView(R2.id.btn_go_resume) TextView btnGoResume;
    @BindView(R2.id.ck_never_noti) CheckBox ckNeverNoti;
    @BindView(R2.id.btn_cancel) TextView btnCancel;
    @BindView(R2.id.btn_sent) TextView btnSent;
    Unbinder unbinder;
    private int completedPersent = 0;
    private OnSendResumeListener onSendResumeListener;

    public static DialogSendResumeFragment newCompletedSend(int persent, OnSendResumeListener listener) {
        DialogSendResumeFragment d = new DialogSendResumeFragment();
        d.completedPersent = persent;
        d.setOnSendResumeListener(listener);
        return d;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_send_resume, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnGoResume.setCompoundDrawablesWithIntrinsicBounds(0,0,btnGoResume.getMinimumWidth(),btnGoResume.getMinimumHeight());
        btnGoResume.setCompoundDrawablesWithIntrinsicBounds(null, null,
            ContextCompat.getDrawable(getContext(), R.drawable.vd_arrow_right_blue_11x5dp), null);
        if (completedPersent > 80) {
            ckNeverNoti.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.GONE);
            btnGoResume.setText("查看我的简历");
        } else {
            ckNeverNoti.setVisibility(View.GONE);
            tvContent.setText(getString(R.string.please_to_complete_your_resume, completedPersent));
            btnGoResume.setText("完善我的简历");
        }
        return view;
    }

    public void setOnSendResumeListener(OnSendResumeListener onSendResumeListener) {
        this.onSendResumeListener = onSendResumeListener;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //@NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    //    Dialog dialog = super.onCreateDialog(savedInstanceState);
    //    dialog.setCanceledOnTouchOutside(true);
    //    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //    Window window = dialog.getWindow();
    //    window.getDecorView().setPadding(0, 0, 0, 0);
    //    WindowManager.LayoutParams wlp = window.getAttributes();
    //    wlp.gravity = Gravity.CENTER;
    //    wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
    //    wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    //    window.setAttributes(wlp);
    //    window.setWindowAnimations(R.style.ButtomDialogStyle);
    //    return dialog;
    //}

    /**
     * 查看简历
     */
    @OnClick(R2.id.btn_go_resume) public void onBtnGoResumeClicked() {
        LogUtil.d(this.getClass(), "查看简历");
    }

    /**
     * 取消
     */
    @OnClick(R2.id.btn_cancel) public void onBtnCancelClicked() {
        dismiss();
    }

    /**
     * 发送简历
     */
    @OnClick(R2.id.btn_sent) public void onBtnSentClicked() {
        LogUtil.d(this.getClass(), "发送简历");
        dismiss();
        if (onSendResumeListener != null){
            onSendResumeListener.onSend();
        }
    }

    public interface OnSendResumeListener{
        void onSend();
    }
}
