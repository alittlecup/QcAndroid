package cn.qingchengfit.student.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import cn.qingchengfit.student.R;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.fragments.BaseDialogFragment;

public class InputDialog extends DialogFragment {
  EditText et;
  private int maxInputLimit = -1;//do not work
  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.dialog_input, null);
    et = v.findViewById(R.id.content);
    v.findViewById(R.id.btn_cancel).setOnClickListener(v1 -> {
      dismiss();
    });
    v.findViewById(R.id.btn_confirm).setOnClickListener(v1 -> {

      if (listener != null){
        listener.onInputDone(et.getText().toString().trim());
      }
      et.setText("");
      dismiss();
    });
    return v;
  }

  public void setMaxInputLimit(int maxInputLimit) {
    this.maxInputLimit = maxInputLimit;
  }

  private InputListener listener;

  public InputListener getListener() {
    return listener;
  }

  public void setListener(InputListener listener) {
    this.listener = listener;
  }

  public interface InputListener {
    void onInputDone(String s);
  }
}
