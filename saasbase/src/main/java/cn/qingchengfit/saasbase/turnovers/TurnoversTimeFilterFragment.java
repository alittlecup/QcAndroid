package cn.qingchengfit.saasbase.turnovers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoversTiemFilterFragmentBinding;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TurnoversTimeFilterFragment extends SaasCommonFragment {

  TurnoversTiemFilterFragmentBinding mBinding;
  private TimeDialogWindow pwTime;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = TurnoversTiemFilterFragmentBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.etDateStart.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showTipDialog(mBinding.etDateStart);
      }
    });
    mBinding.etDateEnd.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showTipDialog(mBinding.etDateEnd);
      }
    });
    mBinding.btnSureSelf.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if(authDataCorrect()){

        }
      }
    });
  }

  private boolean authDataCorrect() {
    if (mBinding.etDateStart == null || mBinding.etDateEnd == null) {
      return false;
    }
    if (TextUtils.isEmpty(mBinding.etDateStart.getText()) || mBinding.etDateStart.getText()
        .toString()
        .equals("开始日期")) {
      DialogUtils.showAlert(getContext(), "请正确输入天数");
      return false;
    }
    if (TextUtils.isEmpty(mBinding.etDateEnd.getText()) || mBinding.etDateEnd.getText()
        .toString()
        .equals("j结束日期")) {
      DialogUtils.showAlert(getContext(), "请正确输入天数");
      return false;
    }

    Date start = DateUtils.formatDateFromYYYYMMDD(mBinding.etDateStart.getText().toString());

    Date end = DateUtils.formatDateFromYYYYMMDD(mBinding.etDateEnd.getText().toString());

    if (start != null && end != null) {
      if (end.getTime() < start.getTime()) {
        DialogUtils.showAlert(getContext(), "结束时间不得早于开始时间");
        return false;
      }
      if (DateUtils.interval(start, end) > 30) {
        DialogUtils.showAlert(getContext(), getString(R.string.alert_choose_time_cannot_super, 30));
        return false;
      }
    }

    return true;
  }

  private void showTipDialog(final TextView editText) {
    if (pwTime == null) {
      pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }
    pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
        Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
    pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        editText.setText(DateUtils.Date2YYYYMMDD(date));
        pwTime.dismiss();
      }
    });
    pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
  }
}
