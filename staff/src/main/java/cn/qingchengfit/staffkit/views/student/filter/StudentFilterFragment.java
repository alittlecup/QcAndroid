package cn.qingchengfit.staffkit.views.student.filter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpFilterFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CheckBoxButton;
import cn.qingchengfit.widgets.QcRadioGroup;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/11/29.
 */
@FragmentWithArgs public class StudentFilterFragment extends BaseFragment {

    @Arg public int fromPage = -1;          //1、 来自会员列表  2、来自名下会员。

    public ReferrerFragment referrerFragment;
    public SourceFragment sourceFragment;
    @BindView(R.id.tv_student_filter_time_start) public TextView tvStudentFilterTimeStart;
    @BindView(R.id.tv_student_filter_time_end) public TextView tvStudentFilterTimeEnd;
    @BindView(R.id.layout_filter_birthday) public LinearLayout layoutFilterBirthday;
    @BindView(R.id.qrg_status) public QcRadioGroup qrgStatus;
    @BindView(R.id.qrg_regitst_times) public QcRadioGroup qrgRegistTimes;
    @BindView(R.id.layout_saler) public LinearLayout layoutSaler;
    @BindView(R.id.img_saler_all) protected ImageView imgSalersAll;
    protected StudentFilter filter = new StudentFilter();
    @BindView(R.id.tv_student_birthday_start) protected TextView tvStudentBirthdayStart;
    @BindView(R.id.tv_student_birthday_end) protected TextView tvStudentBirthdayEnd;
    @BindView(R.id.cbb_gender_male) protected CheckBoxButton cbbGenderMale;
    @BindView(R.id.cbb_gender_female) protected CheckBoxButton cbbGenderFemale;
    protected Date start;
    protected Date end;
    protected int timePosition = -1;
    @BindView(R.id.tv_student_filter_status_0) CheckBoxButton tvStudentFilterStatus0;
    @BindView(R.id.tv_student_filter_status_1) CheckBoxButton tvStudentFilterStatus1;
    @BindView(R.id.tv_student_filter_status_2) CheckBoxButton tvStudentFilterStatus2;
    @BindView(R.id.img_referrer_showall) ImageView imgReferrerShowall;
    @BindView(R.id.img_source_showall) ImageView imgSourceShowall;
    TimeDialogWindow pwTime;
    private long filterTimeStart;
    private long filterTimeEnd;
    private boolean isReferrerShowAll = false;
    private boolean isSourceShowAll = false;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        referrerFragment = new ReferrerFragmentBuilder(2).build();
        sourceFragment = new SourceFragmentBuilder(2).build();
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void setFromPage(int fromPage) {
        this.fromPage = fromPage;
    }

    public void setFilter(StudentFilter filter) {
        this.filter = filter;
    }

    public void setStartFilter(StudentFilter studentFilter) {
        if (!TextUtils.isEmpty(studentFilter.status)) {
            tvStudentFilterStatus0.setChecked(false);
            tvStudentFilterStatus1.setChecked(false);
            tvStudentFilterStatus2.setChecked(false);
            switch (studentFilter.status) {
                case "0":
                    tvStudentFilterStatus0.setChecked(true);
                    break;
                case "1":
                    tvStudentFilterStatus1.setChecked(true);
                    break;
                case "2":
                    tvStudentFilterStatus2.setChecked(true);
                    break;
            }
        }

        if (studentFilter.registerTimeStart != null && !studentFilter.registerTimeStart.equals("")) {
            tvStudentFilterTimeStart.setText(studentFilter.registerTimeStart);
        }
        if (studentFilter.registerTimeEnd != null && !studentFilter.registerTimeEnd.equals("")) {
            tvStudentFilterTimeEnd.setText(studentFilter.registerTimeEnd);
        }
        // TODO: 2017/5/8
        //if (studentFilter.referrerBean != null ) {
        //    referrerFragment.onReferrers(studentFilter.referrerBean);
        //}
        //
        //if (studentFilter.sourceBeanList != null && studentFilter.sourceBeanList.size() != 0) {
        //    sourceFragment.onSources(studentFilter.sourceBeanList);
        //}
    }

    private void initView() {

        referrerFragment = new ReferrerFragmentBuilder(2).build();
        getChildFragmentManager().beginTransaction().replace(R.id.frame_student_filter_referrer, referrerFragment).commit();

        sourceFragment = new SourceFragmentBuilder(2).build();
        getChildFragmentManager().beginTransaction().replace(R.id.frame_student_filter_source, sourceFragment).commit();

        setStartFilter(filter);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

    @OnClick({
        R.id.tv_student_filter_referrer, R.id.tv_student_filter_source, R.id.img_source_showall, R.id.img_referrer_showall,
        R.id.tv_student_filter_reset, R.id.tv_student_filter_confirm
    }) public void onClickBtn(View view) {
        switch (view.getId()) {
            case R.id.tv_student_filter_referrer:
            case R.id.img_referrer_showall:
                isReferrerShowAll = !isReferrerShowAll;
                VectorDrawableCompat drawableR = VectorDrawableCompat.create(getResources(),
                    isReferrerShowAll ? R.drawable.vector_arrow_up_grey : R.drawable.vector_arrow_down_grey, null);
                drawableR.setBounds(0, 0, drawableR.getMinimumWidth(), drawableR.getMinimumHeight());
                imgReferrerShowall.setImageDrawable(drawableR);
                referrerFragment.showAllClick();
                break;
            case R.id.tv_student_filter_source:
            case R.id.img_source_showall:
                isSourceShowAll = !isSourceShowAll;
                VectorDrawableCompat drawableS = VectorDrawableCompat.create(getResources(),
                    isSourceShowAll ? R.drawable.vector_arrow_up_grey : R.drawable.vector_arrow_down_grey, null);
                drawableS.setBounds(0, 0, drawableS.getMinimumWidth(), drawableS.getMinimumHeight());
                imgSourceShowall.setImageDrawable(drawableS);
                sourceFragment.showAllClick();
                break;
            case R.id.tv_student_filter_reset://

                new MaterialDialog.Builder(getContext()).content("确认重置所有筛选项么？")
                    .positiveText(R.string.common_comfirm)
                    .negativeText(R.string.common_cancel)
                    .autoDismiss(true)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            doReset();
                        }
                    })
                    .show();

                break;
            case R.id.tv_student_filter_confirm://
                onComfirm();
                break;
        }
    }

    /**
     * 收起展示所有销售 交给子类复写，用于{@link FollowUpFilterFragment}
     */
    @OnClick({ R.id.tv_saler_all, R.id.img_saler_all }) public void showAllSaler() {
    }

    protected void onComfirm() {

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
        if (layoutFilterBirthday.getVisibility() == View.VISIBLE) {
            filter.birthdayStart = tvStudentBirthdayStart.getText().toString().trim();
            filter.birthdayEnd = tvStudentBirthdayEnd.getText().toString().trim();
        }

        filter.referrerBean = referrerFragment.getSelectedList();// 获取选中的推荐人
        filter.sourceBean = sourceFragment.getSelectedList(); // 获取选中的来源
        // // 遍历会员状态

        filter.status = qrgStatus.getCheckedPos() >= 0 ? qrgStatus.getCheckedPos() + "" : null;
        //性别
        filter.gender = null;
        if (cbbGenderFemale.isChecked()) filter.gender = "1";
        if (cbbGenderMale.isChecked()) filter.gender = "0";
        // 确认
        if (fromPage == 1) {
            RxBus.getBus()
                .post(new StudentFilterEventBuilder().eventType(StudentFilterEvent.EVENT_CONFIRM_CLICK)
                    .eventFrom(StudentFilterEvent.EVENT_FROM_STUDENTLIST)
                    .filter(filter)
                    .createStudentFilterEvent());
        } else {
            RxBus.getBus()
                .post(new StudentFilterEventBuilder().eventType(StudentFilterEvent.EVENT_CONFIRM_CLICK)
                    .eventFrom(StudentFilterEvent.EVENT_FROM_SALELIST)
                    .filter(filter)
                    .createStudentFilterEvent());
        }
    }

    protected void doReset() {
        // 重置会员状态
        tvStudentFilterStatus0.setChecked(false);
        tvStudentFilterStatus1.setChecked(false);
        tvStudentFilterStatus2.setChecked(false);

        //重置
        cbbGenderFemale.setChecked(false);
        cbbGenderMale.setChecked(false);

        // // 重置注册时间
        tvStudentFilterTimeStart.setText("");
        tvStudentFilterTimeEnd.setText("");
        // 重置推荐人
        referrerFragment.reset();
        // // 重置来源
        sourceFragment.reset();
        // // reset Filter
        filter = new StudentFilter();

        //  // 关闭筛选页
        RxBus.getBus()
            .post(
                new StudentFilterEventBuilder().eventType(StudentFilterEvent.EVENT_RESET_CLICK).filter(filter).createStudentFilterEvent());
    }

    @OnClick({ R.id.tv_student_filter_time_start, R.id.tv_student_filter_time_end }) public void onTimeClick(View view) {
        switch (view.getId()) {
            case R.id.tv_student_filter_time_start:
                if (pwTime == null) {
                  pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
                }
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        qrgRegistTimes.setCheckPos(-1);
                        timePosition = -1;
                        tvStudentFilterTimeStart.setText(DateUtils.Date2YYYYMMDD(date));
                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.tv_student_filter_time_end:
                if (pwTime == null) {
                  pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
                }
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        qrgRegistTimes.setCheckPos(-1);
                        timePosition = -1;
                        tvStudentFilterTimeEnd.setText(DateUtils.Date2YYYYMMDD(date));
                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
        }
    }

    @Override public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public void resetView(StudentFilter filter) {
        this.filter = filter;
        tvStudentFilterStatus0.setChecked(false);
        tvStudentFilterStatus1.setChecked(false);
        tvStudentFilterStatus2.setChecked(false);
        if (filter.status != null) {
            if ("0".equals(filter.status)) {
                tvStudentFilterStatus0.setChecked(true);
            } else if ("1".equals(filter.status)) {
                tvStudentFilterStatus1.setChecked(true);
            } else if ("2".equals(filter.status)) {
                tvStudentFilterStatus2.setChecked(true);
            }
        }

        tvStudentFilterTimeStart.setText("");
        tvStudentFilterTimeEnd.setText("");
        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
            tvStudentFilterTimeStart.setText(filter.registerTimeStart);
            tvStudentFilterTimeEnd.setText(filter.registerTimeEnd);
        }
        //// TODO: 2017/5/8 不知道这里在干什么
        //if (filter.referrerBeanList != null && !filter.referrerBeanList.isEmpty()) {
        //    //referrerFragment.resetView(filter);
        //} else {
        //    referrerFragment.reset();
        //}
        //
        //if (filter.sourceBeanList != null && !filter.sourceBeanList.isEmpty()) {
        //    //sourceFragment.resetView(filter);
        //} else {
        //    sourceFragment.reset();
        //}
        referrerFragment.reset();
        sourceFragment.reset();
    }

    /**
     * 用来给子类 调用生日筛选
     */
    @OnClick({ R.id.tv_student_birthday_start, R.id.tv_student_birthday_end }) public void onClickBirthDay(View view) {
    }
}
