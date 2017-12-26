package cn.qingchengfit.staffkit.views.wardrobe.hire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.body.HireWardrobeBody;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.saasbase.events.EventSelectedStudent;
import cn.qingchengfit.saasbase.student.views.ChooseAndSearchStudentParams;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.student.MutiChooseStudentActivity;
import cn.qingchengfit.staffkit.views.wardrobe.WardrobePayBottomFragment;
import cn.qingchengfit.staffkit.views.wardrobe.item.PayWardrobeItem;
import cn.qingchengfit.staffkit.views.wardrobe.main.WardrobeMainFragment;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
 * Created by Paper on 16/9/1.
 */
public class WardrobeLongHireFragment extends BaseFragment implements WardrobeLongHirePresenter.MVPView {

    @BindView(R.id.card_id) TextView cardId;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.start_day) CommonInputView startDay;
    @BindView(R.id.end_day) CommonInputView endDay;

    @Inject WardrobeLongHirePresenter mPresenter;
    @BindView(R.id.choose_student) CommonInputView chooseStudent;
    @BindView(R.id.cv_cost) CommonInputView cvCost;

    private Locker mLocker;
    private TimeDialogWindow pwTime;
    private StudentBean mChosStu;
    //    private List<Card> mStuCard = new ArrayList<>();
    private int mPayMode;
    private Card mChooseCard;

    public static WardrobeLongHireFragment newInstance(Locker locker) {

        Bundle args = new Bundle();
        args.putParcelable("l", locker);
        WardrobeLongHireFragment fragment = new WardrobeLongHireFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocker = getArguments().getParcelable("l");
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wardrobe_long_hire, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        unbinder = ButterKnife.bind(this, view);
        //
        delegatePresenter(mPresenter, this);
        RxBusAdd(PayWardrobeItem.class).subscribe(new Action1<PayWardrobeItem>() {
            @Override public void call(PayWardrobeItem payWardrobeItem) {
                mPayMode = payWardrobeItem.getPay_mode();
                mChooseCard = payWardrobeItem.getCard();
                balance.setVisibility(View.GONE);
                cvCost.setVisibility(View.VISIBLE);
                cvCost.setLabel("金额(元)");
                switch (mPayMode) {
                    case 1:
                        if (mChooseCard != null) {
                            cardId.setText(mChooseCard.getName() + "(" + mChooseCard.getId() + ")");
                            balance.setVisibility(View.VISIBLE);
                            balance.setText(mChooseCard.getBalance() + StringUtils.getUnit(getContext(), mChooseCard.getType()));

                            if (mChooseCard.getType() == Configs.CATEGORY_VALUE) {

                            } else if (mChooseCard.getType() == Configs.CATEGORY_TIMES) {
                                cvCost.setLabel("次数");
                            } else if (mChooseCard.getType() == Configs.CATEGORY_DATE) {
                                cvCost.setVisibility(View.GONE);
                            }
                        }

                        break;
                    case 2:
                        cardId.setText("现金支付");
                        break;
                    case 3:
                        cardId.setText("微信支付");

                        break;
                    case 4:
                        cardId.setText("银行卡支付");
                        break;
                }
            }
        });
        RxBusAdd(EventSelectedStudent.class)
          .observeOn(AndroidSchedulers.mainThread())
          .filter(eventSelectedStudent -> CmStringUtils.isEmpty(eventSelectedStudent.getSrc()) || getFragmentName().equalsIgnoreCase(eventSelectedStudent.getSrc()))
          .subscribe(eventSelectedStudent -> {
              chooseStudent.setContent(eventSelectedStudent.getNameFirst());
              StudentBean sb = new StudentBean();
              sb.setId(eventSelectedStudent.getIdFirst());
              mChosStu = sb;
          });
        return view;
    }

    @Override public String getFragmentName() {
        return WardrobeLongHireFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.choose_student, R.id.start_day, R.id.end_day, R.id.layout_pay_method, R.id.comfirm }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_student://选择会员
                //Intent toChooseStudent = new Intent(getContext(), MutiChooseStudentActivity.class);
                //toChooseStudent.putExtra(Configs.EXTRA_PERMISSION_KEY, PermissionServerUtils.LOCKER_SETTING);
                //toChooseStudent.putExtra(Configs.EXTRA_PERMISSION_METHOD, "post");
                //startActivityForResult(toChooseStudent, 1);
                routeTo("student","/choose/student/", ChooseAndSearchStudentParams.builder()
                  .source(getFragmentName())
                  .chooseType(1)
                  .build());
                //                toChooseStudent.putExtra(MutiChooseStudentActivity.EXTRA_STUDENTS)
                break;
            case R.id.start_day: //开始时间
              if (pwTime == null) {
                pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
              }
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        startDay.setContent(DateUtils.Date2YYYYMMDD(date));
                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.end_day://结束时间
              if (pwTime == null) {
                pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
              }
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        endDay.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.layout_pay_method: //选择支付卡
                if (mChosStu == null) {
                    onShowError("请先选择学员");
                    return;
                }
                WardrobePayBottomFragment.newInstance(mChosStu.getId(), true).show(getFragmentManager(), "");
                break;
            case R.id.comfirm://完成租赁
                if (startDay.isEmpty()) {
                    ToastUtils.show("请选择开始时间");
                    return;
                }
                if (endDay.isEmpty()) {
                    ToastUtils.show("请选结束时间");
                    return;
                }

                if (cvCost.getVisibility() == View.VISIBLE && cvCost.isEmpty()) {
                    ToastUtils.show("请输入金额");
                    return;
                }
                showLoading();
                mPresenter.hireLong(App.staffId, new HireWardrobeBody.Builder().is_long_term_borrow(true)
                    .user_id(mChosStu == null ? null : mChosStu.getId())
                    .start(startDay.getContent())
                    .end(endDay.getContent())
                    .locker_id(mLocker.id)
                    .deal_mode(mPayMode)
                    .card_id(mChooseCard == null ? null : mChooseCard.getId())
                    .cost(cvCost.getVisibility() == View.VISIBLE ? cvCost.getContent() : null)
                    .build());
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                StudentBean studentBean = data.getParcelableExtra(MutiChooseStudentActivity.EXTRA_STUDENTS);
                if (studentBean != null) {
                    chooseStudent.setContent(studentBean.getUsername());
                    mChosStu = studentBean;
                }
            }
        }
    }

    @Override public void onHireOk() {
        hideLoading();
        getParentFragment().getFragmentManager()
            .popBackStack(WardrobeMainFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override public void onStuCards(List<Card> cards) {
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
