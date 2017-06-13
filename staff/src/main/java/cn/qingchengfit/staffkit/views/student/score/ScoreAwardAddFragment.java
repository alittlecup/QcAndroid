package cn.qingchengfit.staffkit.views.student.score;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.StudentScoreAwardRuleBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.lib.TimeDialogWindow;
import com.bigkoo.pickerview.lib.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;

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
 * //Created by yangming on 16/12/26.
 */
public class ScoreAwardAddFragment extends BaseFragment implements ScoreAwardAddPresenter.PresenterView {

    public static final int TYPE_NEW = 445;
    public static final int TYPE_MODIFY = 303;
    public TimeDialogWindow pwTime;
    public StudentScoreAwardRuleBean awardRuleBean;//= new StudentScoreAwardRuleBean();
    @Inject ScoreAwardAddPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.tv_date_start) TextView tvDateStart;
    @BindView(R.id.tv_date_end) TextView tvDateEnd;
    @BindView(R.id.sw_score_config_group) SwitcherLayout swScoreConfigGroup;
    @BindView(R.id.et_award_group) EditText etAwardGroup;
    @BindView(R.id.ll_award_group) LinearLayout llAwardGroup;
    @BindView(R.id.sw_score_config_private) SwitcherLayout swScoreConfigPrivate;
    @BindView(R.id.et_award_private) EditText etAwardPrivate;
    @BindView(R.id.ll_award_private) LinearLayout llAwardPrivate;
    @BindView(R.id.sw_score_config_signin) SwitcherLayout swScoreConfigSignin;
    @BindView(R.id.et_award_signin) EditText etAwardSignin;
    @BindView(R.id.ll_award_signin) LinearLayout llAwardSignin;
    @BindView(R.id.sw_score_config_buy) SwitcherLayout swScoreConfigBuy;
    @BindView(R.id.et_award_buy) EditText etAwardBuy;
    @BindView(R.id.ll_award_buy) LinearLayout llAwardBuy;
    @BindView(R.id.sw_score_config_charge) SwitcherLayout swScoreConfigCharge;
    @BindView(R.id.et_award_charge) EditText etAwardCharge;
    @BindView(R.id.ll_award_charge) LinearLayout llAwardCharge;
    @BindView(R.id.tv_award_delete) TextView tvAwardDelete;
    //private Date dateStart;
    //private Date dateEnd;
    ArrayList<StudentScoreAwardRuleBean> allAwards = new ArrayList<>();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    private int type;
    private int requestCode;

    public static ScoreAwardAddFragment newInstance(Fragment target, int requestCode, ArrayList<StudentScoreAwardRuleBean> allAwards) {
        Bundle args = new Bundle();
        args.putInt("type", TYPE_NEW);
        args.putInt("requestCode", requestCode);
        args.putParcelableArrayList("others", allAwards);
        ScoreAwardAddFragment fragment = new ScoreAwardAddFragment();
        fragment.setTargetFragment(target, requestCode);
        fragment.setArguments(args);
        return fragment;
    }

    public static ScoreAwardAddFragment modify(Fragment target, int requestCode, StudentScoreAwardRuleBean bean,
        ArrayList<StudentScoreAwardRuleBean> allAwards) {
        Bundle args = new Bundle();
        args.putInt("type", TYPE_MODIFY);
        args.putInt("requestCode", requestCode);
        args.putParcelable("award", bean);
        args.putParcelableArrayList("others", allAwards);
        ScoreAwardAddFragment fragment = new ScoreAwardAddFragment();
        fragment.setTargetFragment(target, requestCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type", TYPE_MODIFY);
        requestCode = getArguments().getInt("requestCode", 11);
        allAwards = getArguments().getParcelableArrayList("others");
        if (type == TYPE_MODIFY) awardRuleBean = getArguments().getParcelable("award");
        //
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_score_config_award_add, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        initView();
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(R.string.qc_title_student_score_award_add);
        toolbar.inflateMenu(R.menu.menu_comfirm);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                doConfirm();
                return false;
            }
        });
    }

    private void initView() {
        swScoreConfigGroup.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swScoreConfigGroup.setShowDivier(b);
                swScoreConfigGroup.setOpen(b);
                llAwardGroup.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });
        swScoreConfigPrivate.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swScoreConfigPrivate.setShowDivier(b);
                swScoreConfigPrivate.setOpen(b);
                llAwardPrivate.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });
        swScoreConfigSignin.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swScoreConfigSignin.setShowDivier(b);
                swScoreConfigSignin.setOpen(b);
                llAwardSignin.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });
        swScoreConfigBuy.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swScoreConfigBuy.setShowDivier(b);
                swScoreConfigBuy.setOpen(b);
                llAwardBuy.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        swScoreConfigCharge.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swScoreConfigCharge.setShowDivier(b);
                swScoreConfigCharge.setOpen(b);
                llAwardCharge.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        switch (type) {
            case TYPE_MODIFY:
                tvAwardDelete.setVisibility(View.VISIBLE);

                tvDateStart.setText(DateUtils.getYYYYMMDDfromServer(awardRuleBean.dateStart));
                tvDateEnd.setText(DateUtils.getYYYYMMDDfromServer(awardRuleBean.dateEnd));

                if (awardRuleBean != null && awardRuleBean.groupTimes_enable != null && awardRuleBean.groupTimes_enable) {
                    swScoreConfigGroup.setOpen(true);
                    etAwardGroup.setText(awardRuleBean.groupTimes);
                }

                if (awardRuleBean != null && awardRuleBean.privateTimes_enable != null && awardRuleBean.privateTimes_enable) {
                    swScoreConfigPrivate.setOpen(true);
                    etAwardPrivate.setText(awardRuleBean.privateTimes);
                }

                if (awardRuleBean != null && awardRuleBean.signinTimes_enable != null && awardRuleBean.signinTimes_enable) {
                    swScoreConfigSignin.setOpen(true);
                    etAwardSignin.setText(awardRuleBean.signinTimes);
                }

                if (awardRuleBean != null && awardRuleBean.buyTimes_enable != null && awardRuleBean.buyTimes_enable) {
                    swScoreConfigBuy.setOpen(true);
                    etAwardBuy.setText(awardRuleBean.buyTimes);
                }

                if (awardRuleBean != null && awardRuleBean.chargeTimes_enable != null && awardRuleBean.chargeTimes_enable) {
                    swScoreConfigCharge.setOpen(true);
                    etAwardCharge.setText(awardRuleBean.chargeTimes);
                }

                break;
            case TYPE_NEW:
                tvAwardDelete.setVisibility(View.GONE);
                break;
        }
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

    @OnClick({ R.id.tv_date_start, R.id.tv_date_end }) public void onDateClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date_start:
                if (pwTime == null) {
                    pwTime = new TimeDialogWindow(getActivity(), Type.YEAR_MONTH_DAY);
                }
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        if (awardRuleBean == null) awardRuleBean = new StudentScoreAwardRuleBean();
                        awardRuleBean.dateStart = DateUtils.date2YYMMDDTHHMMSS(date).replace(" ", "T");
                        tvDateStart.setText(DateUtils.Date2YYYYMMDD(date));
                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.tv_date_end:
                if (pwTime == null) {
                    pwTime = new TimeDialogWindow(getActivity(), Type.YEAR_MONTH_DAY);
                }
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        if (awardRuleBean == null) awardRuleBean = new StudentScoreAwardRuleBean();
                        awardRuleBean.dateEnd = DateUtils.date2YYMMDDTHHMMSS(date).replace(" ", "T");
                        tvDateEnd.setText(DateUtils.Date2YYYYMMDD(date));
                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
        }
    }

    @OnClick(R.id.tv_award_delete) public void onDeleteClick() {
        new MaterialDialog.Builder(getContext()).content("删除积分奖励不会影响已发放的积分，确定删除积分奖励？")
            .positiveText(R.string.common_comfirm)
            .negativeText(R.string.common_cancel)
            .autoDismiss(true)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    doDelete();
                }
            })
            .show();
    }

    private void doConfirm() {

        if (TextUtils.isEmpty(tvDateStart.getText())) {
            ToastUtils.show("请选择开始日期");
            return;
        }

        if (TextUtils.isEmpty(tvDateEnd.getText())) {
            ToastUtils.show("请选择结束日期");
            return;
        }

        // 判断日期冲突
        if (!allAwards.isEmpty()) {

            Date dateStart = DateUtils.formatDateFromYYYYMMDD(awardRuleBean.dateStart);
            Date dateEnd = DateUtils.formatDateFromYYYYMMDD(awardRuleBean.dateEnd);

            for (StudentScoreAwardRuleBean scoreAwardRuleBean : allAwards) {
                Date otherStart = DateUtils.formatDateFromYYYYMMDD(scoreAwardRuleBean.dateStart);
                Date otherEnd = DateUtils.formatDateFromYYYYMMDD(scoreAwardRuleBean.dateEnd);
                if ((otherStart.getTime() <= dateStart.getTime() && dateStart.getTime() <= otherEnd.getTime()) || (otherStart.getTime()
                    <= dateEnd.getTime() && dateEnd.getTime() <= otherEnd.getTime())) {
                    new MaterialDialog.Builder(getContext()).content("有效日期与其他奖励的有效日期冲突")
                        .positiveText(R.string.common_comfirm)
                        .negativeText(R.string.common_cancel)
                        .autoDismiss(true)
                        .show();
                    return;
                }
            }
        }

        if (swScoreConfigGroup.isOpen() && TextUtils.isEmpty(etAwardGroup.getText())) {
            ToastUtils.show("请填写团课预约积分奖励倍数");
            return;
        }

        if (swScoreConfigPrivate.isOpen() && TextUtils.isEmpty(etAwardPrivate.getText())) {
            ToastUtils.show("请填写私教预约积分奖励倍数");
            return;
        }

        if (swScoreConfigSignin.isOpen() && TextUtils.isEmpty(etAwardSignin.getText())) {
            ToastUtils.show("请填写签到积分奖励倍数");
            return;
        }

        if (swScoreConfigBuy.isOpen() && TextUtils.isEmpty(etAwardBuy.getText())) {
            ToastUtils.show("请填写新购卡积分奖励倍数");
            return;
        }

        if (swScoreConfigCharge.isOpen() && TextUtils.isEmpty(etAwardCharge.getText())) {
            ToastUtils.show("请填写会员卡续费积分奖励倍数");
            return;
        }

        if (swScoreConfigGroup.isOpen()) {
            awardRuleBean.groupTimes_enable = true;
            awardRuleBean.groupTimes = etAwardGroup.getText().toString();
        } else {
            awardRuleBean.groupTimes_enable = false;
            awardRuleBean.groupTimes = null;
        }
        if (swScoreConfigPrivate.isOpen()) {
            awardRuleBean.privateTimes_enable = true;
            awardRuleBean.privateTimes = etAwardPrivate.getText().toString();
        } else {
            awardRuleBean.privateTimes_enable = false;
            awardRuleBean.privateTimes = null;
        }
        if (swScoreConfigSignin.isOpen()) {
            awardRuleBean.signinTimes_enable = true;
            awardRuleBean.signinTimes = etAwardSignin.getText().toString();
        } else {
            awardRuleBean.signinTimes_enable = false;
            awardRuleBean.signinTimes = null;
        }
        if (swScoreConfigBuy.isOpen()) {
            awardRuleBean.buyTimes_enable = true;
            awardRuleBean.buyTimes = etAwardBuy.getText().toString();
        } else {
            awardRuleBean.buyTimes_enable = false;
            awardRuleBean.buyTimes = null;
        }
        if (swScoreConfigCharge.isOpen()) {
            awardRuleBean.chargeTimes_enable = true;
            awardRuleBean.chargeTimes = etAwardCharge.getText().toString();
        } else {
            awardRuleBean.chargeTimes_enable = false;
            awardRuleBean.chargeTimes = null;
        }

        awardRuleBean.is_active = true;
        // //  16/12/27 post奖励规则
        showLoading();
        if (TextUtils.isEmpty(awardRuleBean.id)) { // 新增
            presenter.postScoreAward(awardRuleBean);
        } else { // 修改
            presenter.putScoreAward(awardRuleBean.id, awardRuleBean);
        }
    }

    private void doDelete() {
        // 16/12/27 delete奖励规则
        awardRuleBean.is_active = false;
        showLoading();
        presenter.putScoreAward(awardRuleBean.id, awardRuleBean);
    }

    @Override public void onSuccess() {
        //getTargetFragment().onActivityResult(requestCode, Activity.RESULT_OK,
        //        IntentUtils.instancePacecle(awardRuleBean));
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }
}
