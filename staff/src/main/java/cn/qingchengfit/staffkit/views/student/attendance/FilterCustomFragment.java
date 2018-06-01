package cn.qingchengfit.staffkit.views.student.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;



import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fb on 2017/3/7.
 */

@FragmentWithArgs public class FilterCustomFragment extends BaseFragment {

    public static final int ABSENCE = 100;
    public static final int ATTENDANCE = 101;
    public int limitDay;
    @Arg(required = true) String title;
	EditText editAbsenceStart;
	EditText editAbsenceEnd;
	TextView modifySettingTitle;
    private OnBackFilterDataListener onBackFilterDataListener;
    private TimeDialogWindow pwTime;
    private boolean isSelectTime;
    private Date start;
    private Date end;

    public static FilterCustomFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        FilterCustomFragment fragment = new FilterCustomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        if (getArguments() != null){
            title = getArguments().getString("title");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_custom, container, false);
      editAbsenceStart = (EditText) view.findViewById(R.id.edit_absence_start);
      editAbsenceEnd = (EditText) view.findViewById(R.id.edit_absence_end);
      modifySettingTitle = (TextView) view.findViewById(R.id.modify_setting_title);
      view.findViewById(R.id.tv_absence_filter_confirm)
          .setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              onSetData(v);
            }
          });
      view.findViewById(R.id.tv_absence_filter_reset)
          .setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              onSetData(v);
            }
          });

      initView();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return view;
    }

    public void setOnBackFilterDataListener(OnBackFilterDataListener onBackFilterDataListener) {
        this.onBackFilterDataListener = onBackFilterDataListener;
    }

    private void initView() {
        modifySettingTitle.setText(title);
        if (isSelectTime) {
            editAbsenceStart.setHint("开始日期");
            editAbsenceEnd.setHint("结束日期");
            editAbsenceStart.setKeyListener(null);
            editAbsenceEnd.setKeyListener(null);

            editAbsenceStart.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    showTipDialog((EditText) view);
                }
            });

            editAbsenceEnd.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    showTipDialog((EditText) view);
                }
            });
        }
    }

    public void setSelectTime(boolean selectTime) {
        isSelectTime = selectTime;
    }

 public void onSetData(View view) {
        switch (view.getId()) {
            case R.id.tv_absence_filter_confirm:
                if (authDataCorrect()) {
                    onBackFilterDataListener.onSettingData(editAbsenceStart.getText().toString(), editAbsenceEnd.getText().toString());
                }
                break;
            case R.id.tv_absence_filter_reset:
                onBackFilterDataListener.onBack();
                break;
        }
    }

    private boolean authDataCorrect() {
        if (editAbsenceEnd == null || editAbsenceStart == null) {
            return false;
        }
        if (TextUtils.isEmpty(editAbsenceStart.getText()) || editAbsenceStart.getText().toString().equals("开始日期")) {
            DialogUtils.showAlert(getContext(), "请正确输入天数");
            return false;
        }
        if (TextUtils.isEmpty(editAbsenceEnd.getText()) || editAbsenceStart.getText().toString().equals("开始日期")) {
            DialogUtils.showAlert(getContext(), "请正确输入天数");
            return false;
        }

        if (isSelectTime) {
            start = DateUtils.formatDateFromYYYYMMDD(editAbsenceStart.getText().toString());

            end = DateUtils.formatDateFromYYYYMMDD(editAbsenceEnd.getText().toString());

            if (start != null && end != null) {
                if (end.getTime() < start.getTime()) {
                    DialogUtils.showAlert(getContext(), "结束时间不得早于开始时间");
                    return false;
                }
                if (limitDay > 0 && DateUtils.interval(start, end) > limitDay) {
                    DialogUtils.showAlert(getContext(), getString(R.string.alert_choose_time_cannot_super, limitDay));
                    return false;
                }
            }
        }

        return true;
    }

    private void showTipDialog(final EditText editText) {

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

    @Override public String getFragmentName() {
        return FilterCustomFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnBackFilterDataListener {
        void onSettingData(String start, String end);

        void onBack();
    }
}
