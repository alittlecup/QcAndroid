package cn.qingchengfit.staffkit.views.card.offday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.cards.views.WriteDescFragment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/18 2016.
 */
public class AddOffDayFragment extends BaseFragment implements AddOffDayView {

    @BindView(R.id.mark) CommonInputView mark;
    @BindView(R.id.offday_reason) CommonInputView offdayReason;
    @BindView(R.id.start_day) CommonInputView startDay;
    @BindView(R.id.end_day) CommonInputView endDay;
    @BindView(R.id.spend) CommonInputView spend;
    @BindView(R.id.comfirm) Button comfirm;
    @Inject AddOffDayPresenter presenter;
    private TimeDialogWindow pwTime;
    private TextWatcher textchange = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override public void afterTextChanged(Editable s) {
            comfirm.setEnabled(!TextUtils.isEmpty(offdayReason.getContent()) && !TextUtils.isEmpty(spend.getContent()));
        }
    };

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_offday, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        mCallbackActivity.setToolbar("添加请假", false, null, 0, null);
        spend.addTextWatcher(textchange);
        offdayReason.addTextWatcher(textchange);
        startDay.setContent(DateUtils.Date2YYYYMMDD(new Date()));
        endDay.setContent(DateUtils.Date2YYYYMMDD(new Date()));
        return view;
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @OnClick({ R.id.offday_reason, R.id.start_day, R.id.end_day, R.id.mark, R.id.comfirm }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.offday_reason:
                WriteDescFragment.start(this, 1, "请假理由", "请填写请假理由");
                break;
            case R.id.start_day:
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        startDay.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.end_day:
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        endDay.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.mark:
                WriteDescFragment.start(this, 2, "备注信息", "请填写备注信息");
                break;
            case R.id.comfirm:
                if (DateUtils.AlessOrEquelB(startDay.getContent(), endDay.getContent())) {
                    showLoading();
                    presenter.commitOffDay(startDay.getContent(), endDay.getContent(), offdayReason.getContent(), spend.getContent(),
                        mark.getContent());
                    break;
                } else {
                    ToastUtils.show(getString(R.string.err_time_start_less_end));
                    return;
                }
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                //请假理由
                offdayReason.setContent(IntentUtils.getIntentString(data));
            } else if (requestCode == 2) {
                mark.setContent(IntentUtils.getIntentString(data));
            }
        }
    }

    @Override public void onFailed(String s) {
        ToastUtils.show(s);
        hideLoading();
    }

    @Override public void onSucceess() {
        hideLoading();
        ToastUtils.showS("请假成功");
        getActivity().onBackPressed();
    }

    @Override public String getFragmentName() {
        return AddOffDayFragment.class.getName();
    }
}
