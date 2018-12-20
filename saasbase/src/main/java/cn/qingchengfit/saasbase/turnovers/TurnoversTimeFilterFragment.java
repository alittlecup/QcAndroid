package cn.qingchengfit.saasbase.turnovers;

import android.os.Bundle;
import android.support.annotation.IntDef;
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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TurnoversTimeFilterFragment extends SaasCommonFragment {

  TurnoversTiemFilterFragmentBinding mBinding;
  private TimeDialogWindow pwTime;

  @IntDef(value = {
      TimeType.DAY, TimeType.MONTH, TimeType.YEAR, TimeType.WEEK, TimeType.CUSTOMIZE
  }) @Retention(RetentionPolicy.SOURCE) public @interface TimeType {
    int YEAR = 1;
    int MONTH = 2;
    int WEEK = 3;
    int DAY = 5;
    int CUSTOMIZE = -1;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = TurnoversTiemFilterFragmentBinding.inflate(inflater, container, false);
    mBinding.btnDay.setSelected(true);
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
        if (authDataCorrect()) {
          if (listener != null) {
            listener.onDateConfirm(mBinding.etDateStart.getText().toString(),
                mBinding.etDateEnd.getText().toString(), TimeType.CUSTOMIZE);
          }
        }
      }
    });
    mBinding.btnDay.setOnClickListener(v -> {
      if (listener != null) {
        listener.onDateConfirm(DateUtils.getStringToday(), DateUtils.getStringToday(),
            TimeType.DAY);
      }
    });
    mBinding.btnWeek.setOnClickListener(v -> {
      if (listener != null) {
        listener.onDateConfirm(DateUtils.getMondayOfThisWeek(new Date()),
            DateUtils.getSundayOfThisWeek(new Date()), TimeType.WEEK);
      }
    });
    mBinding.btnMonth.setOnClickListener(v -> {
      if (listener != null) {
        listener.onDateConfirm(DateUtils.getStartDayOfMonth(new Date()),
            DateUtils.getEndDayOfMonthNew(new Date()), TimeType.MONTH);
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
        .equals("结束日期")) {
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

  public void setListener(onDateConfirmListener listener) {
    this.listener = listener;
  }

  private onDateConfirmListener listener;

  interface onDateConfirmListener {
    void onDateConfirm(String start, String end, @TimeType int type);
  }
}
