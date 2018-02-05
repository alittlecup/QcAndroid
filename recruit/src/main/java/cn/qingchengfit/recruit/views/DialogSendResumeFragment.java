package cn.qingchengfit.recruit.views;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import butterknife.Unbinder;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.databinding.DialogSendResumeBinding;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import dagger.android.support.AndroidSupportInjection;
import javax.inject.Inject;

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

  @Inject RecruitRouter router;
  Unbinder unbinder;
  private int completedPersent = 0;
  private OnSendResumeListener onSendResumeListener;
  DialogSendResumeBinding db;

  public static DialogSendResumeFragment newCompletedSend(int persent,
    OnSendResumeListener listener) {
    DialogSendResumeFragment d = new DialogSendResumeFragment();
    d.completedPersent = persent;
    d.setOnSendResumeListener(listener);
    return d;
  }

  public static boolean needShow(Context context) {
    return !PreferenceUtils.getPrefBoolean(context, "send_resume_hint", false);
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

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    try {
      AndroidSupportInjection.inject(this);
    } catch (Exception e) {
      LogUtil.e("not find fragment:" + this.getClass().getName());
    }
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    db = DataBindingUtil.inflate(inflater, R.layout.dialog_send_resume, container, false);
    db.btnGoResume.setCompoundDrawablesWithIntrinsicBounds(0, 0, db.btnGoResume.getMinimumWidth(),
      db.btnGoResume.getMinimumHeight());
    db.btnGoResume.setCompoundDrawablesWithIntrinsicBounds(null, null,
      ContextCompat.getDrawable(getContext(), R.drawable.vd_arrow_right_blue_11x5dp), null);
    if (completedPersent > 80) {
      db.ckNeverNoti.setVisibility(View.VISIBLE);
      db.tvContent.setVisibility(View.GONE);
      db.btnGoResume.setText("查看我的简历");
    } else {
      db.ckNeverNoti.setVisibility(View.GONE);
      db.tvContent.setText(getString(R.string.please_to_complete_your_resume, completedPersent));
      db.btnGoResume.setText("完善我的简历");
    }
    db.ckNeverNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PreferenceUtils.setPrefBoolean(getContext(), "send_resume_hint", true);
      }
    });
    db.btnGoResume.setOnClickListener(view -> onBtnGoResumeClicked());
    db.btnCancel.setOnClickListener(view -> onBtnCancelClicked());
    db.btnSent.setOnClickListener(view -> onBtnSentClicked());
    return db.getRoot();
  }

  public void setOnSendResumeListener(OnSendResumeListener onSendResumeListener) {
    this.onSendResumeListener = onSendResumeListener;
  }

  /**
   * 查看简历
   */
  public void onBtnGoResumeClicked() {
    router.toMyResume();
    dismiss();
  }

  /**
   * 取消
   */
  public void onBtnCancelClicked() {
    dismiss();
  }

  /**
   * 发送简历
   */
  public void onBtnSentClicked() {
    LogUtil.d(this.getClass(), "发送简历");
    dismiss();
    if (onSendResumeListener != null) {
      onSendResumeListener.onSend();
    }
  }

  public interface OnSendResumeListener {
    void onSend();
  }
}
