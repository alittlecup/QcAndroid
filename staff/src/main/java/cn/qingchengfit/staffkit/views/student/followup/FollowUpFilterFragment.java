package cn.qingchengfit.staffkit.views.student.followup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEventBuilder;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.widgets.QcRadioGroup;
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
 * Created by Paper on 2017/5/8.
 */

public class FollowUpFilterFragment extends StudentFilterFragment {
    TopFilterSaleFragment saleFragment = new TopFilterSaleFragment();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        layoutSaler.setVisibility(View.VISIBLE);
        fromPage = 1;
        qrgRegistTimes.setVisibility(View.VISIBLE);
        qrgRegistTimes.setCheckedChange(new QcRadioGroup.CheckedChange() {
            @Override public void onCheckedChange() {
                timePosition = qrgRegistTimes.getCheckedPos();
                switch (qrgRegistTimes.getCheckedPos()) {
                    case 0:
                        tvStudentFilterTimeStart.setText(DateUtils.getStringToday());
                        tvStudentFilterTimeEnd.setText(DateUtils.getStringToday());
                        break;
                    case 1:
                        tvStudentFilterTimeStart.setText(DateUtils.minusDay(new Date(), 6));
                        tvStudentFilterTimeEnd.setText(DateUtils.getStringToday());
                        break;
                    case 2:
                        tvStudentFilterTimeStart.setText(DateUtils.minusDay(new Date(), 29));
                        tvStudentFilterTimeEnd.setText(DateUtils.getStringToday());
                        break;
                    default:
                        break;
                }
            }
        });
        return v;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().beginTransaction().replace(R.id.frag_student_filter_saler, saleFragment).commitAllowingStateLoss();
    }

    public void selectTimePos(int pos) {
        if (pos < 3 && pos >= 0 && qrgRegistTimes != null) {
            qrgRegistTimes.setCheckPos(pos);
            switch (pos) {
                case 0:
                    tvStudentFilterTimeStart.setText(DateUtils.getStringToday());
                    tvStudentFilterTimeEnd.setText(DateUtils.getStringToday());
                    break;
                case 1:
                    tvStudentFilterTimeStart.setText(DateUtils.minusDay(new Date(), 6));
                    tvStudentFilterTimeEnd.setText(DateUtils.getStringToday());
                    break;
                case 2:
                    tvStudentFilterTimeStart.setText(DateUtils.minusDay(new Date(), 29));
                    tvStudentFilterTimeEnd.setText(DateUtils.getStringToday());
                    break;
            }
        }
    }

    public void selectStatusPos(int pos) {
        if (qrgStatus != null) {
            qrgStatus.setCheckPos(pos);
        }
    }

    public void selectGenderPos(int pos) {
        if (cbbGenderFemale != null && cbbGenderMale != null) {
            if (pos == 0) {
                cbbGenderFemale.setChecked(false);
                cbbGenderMale.setChecked(true);
            } else if (pos == 1) {
                cbbGenderFemale.setChecked(true);
                cbbGenderMale.setChecked(false);
            } else {
                cbbGenderFemale.setChecked(false);
                cbbGenderMale.setChecked(false);
            }
        }
    }

    @Override protected void doReset() {
        saleFragment.selectSaler("-1");
        qrgRegistTimes.setCheckPos(0);
        super.doReset();
    }

    @Override public void resetView(StudentFilter filter) {
        super.resetView(filter);
    }

    @Override protected void onComfirm() {
        if (!TextUtils.isEmpty(tvStudentFilterTimeStart.getText().toString())) {
            start = DateUtils.formatDateFromYYYYMMDD(tvStudentFilterTimeStart.getText().toString());
            filter.registerTimeStart = DateUtils.Date2YYYYMMDD(start);
        }
        if (!TextUtils.isEmpty(tvStudentFilterTimeEnd.getText().toString())) {
            end = DateUtils.formatDateFromYYYYMMDD(tvStudentFilterTimeEnd.getText().toString());
            filter.registerTimeEnd = DateUtils.Date2YYYYMMDD(end);
        }
        if (start != null && end != null) {
            if (end.getTime() < start.getTime()) {
                DialogUtils.showAlert(getContext(), "结束时间不得早于开始时间");
                return;
            }
        }
        filter.referrerBean = referrerFragment.getSelectedList();// 获取选中的推荐人
        filter.sourceBean = sourceFragment.getSelectedList(); // 获取选中的来源
        filter.sale = saleFragment.getChoosenSaler();
        // 遍历会员状态
        filter.timePos = timePosition;
        filter.status = qrgStatus.getCheckedPos() >= 0 ? qrgStatus.getCheckedPos() + "" : null;
        //性别
        filter.gender = null;
        if (cbbGenderFemale.isChecked()) filter.gender = "1";
        if (cbbGenderMale.isChecked()) filter.gender = "0";
        // 确认
        RxBus.getBus()
            .post(new StudentFilterEventBuilder().eventType(StudentFilterEvent.EVENT_CONFIRM_CLICK)
                .eventFrom(StudentFilterEvent.EVENT_FROM_FOLLOW_UP)
                .filter(filter)
                .createStudentFilterEvent());
    }

    @Override public void showAllSaler() {
        saleFragment.showAllClick();
        VectorDrawableCompat drawableR = VectorDrawableCompat.create(getResources(),
            !saleFragment.isShowShort() ? R.drawable.vector_arrow_up_grey : R.drawable.vector_arrow_down_grey, null);
        drawableR.setBounds(0, 0, drawableR.getMinimumWidth(), drawableR.getMinimumHeight());
        imgSalersAll.setImageDrawable(drawableR);
    }
}
