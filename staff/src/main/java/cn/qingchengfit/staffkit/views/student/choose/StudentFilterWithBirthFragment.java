package cn.qingchengfit.staffkit.views.student.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Date;

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
 * Created by Paper on 2017/3/15.
 */
//@FragmentWithArgs
public class StudentFilterWithBirthFragment extends StudentFilterFragment {
    protected TimeDialogWindow pwBirth;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FragmentArgs.inject(this);
        fromPage = 1;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        layoutFilterBirthday.setVisibility(View.VISIBLE);
        return v;
    }

    @Override public void onClickBirthDay(View view) {
        switch (view.getId()) {
            case R.id.tv_student_birthday_start:
                if (pwBirth == null) {
                  pwBirth = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.MONTH_DAY);
                }
                pwBirth.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        tvStudentBirthdayStart.setText(DateUtils.Date2MMDD(date));
                        pwBirth.dismiss();
                    }
                });
                pwBirth.showAtLocation(getView(), Gravity.BOTTOM, 0, 0,
                    DateUtils.formatDatefromMMDD(tvStudentBirthdayStart.getText().toString()));
                break;
            case R.id.tv_student_birthday_end:
                if (pwBirth == null) {
                  pwBirth = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.MONTH_DAY);
                }
                pwBirth.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        if (date.getTime() < DateUtils.formatDatefromMMDD(tvStudentBirthdayStart.getText().toString()).getTime()) {
                            ToastUtils.show("生日结束日期不能小于开始日期");
                            return;
                        }
                        tvStudentBirthdayEnd.setText(DateUtils.Date2MMDD(date));
                        pwBirth.dismiss();
                    }
                });
                pwBirth.showAtLocation(getView(), Gravity.BOTTOM, 0, 0,
                    DateUtils.formatDatefromMMDD(tvStudentBirthdayEnd.getText().toString()));

                break;
        }
    }
}
