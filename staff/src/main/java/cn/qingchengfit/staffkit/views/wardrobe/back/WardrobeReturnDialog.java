package cn.qingchengfit.staffkit.views.wardrobe.back;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.body.ReturnWardrobeBody;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventContinueHire;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.utils.DateUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.List;
import javax.inject.Inject;

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
public class WardrobeReturnDialog extends BaseDialogFragment implements WardrobeReturnDialogPresenter.MVPView {

    @BindView(R.id.tv_status) TextView tvStatus;
    @BindView(R.id.layout_status) LinearLayout layoutStatus;
    @BindView(R.id.tv_time_limit) TextView tvTimeLimit;
    @BindView(R.id.layout_period) LinearLayout layoutPeriod;
    @BindView(R.id.tv_student) TextView tvStudent;
    @BindView(R.id.layout_student) LinearLayout layoutStudent;
    @BindView(R.id.tv_remind_day) TextView tvRemindDay;
    @BindView(R.id.layout_remind_day) LinearLayout layoutRemindDay;
    @BindView(R.id.btn_continue_hire) Button btnContinueHire;

    @Inject WardrobeReturnDialogPresenter mPresenter;
    @BindView(R.id.locker_name) TextView lockerName;
    @BindView(R.id.locker_rg) TextView lockerRg;

    private Locker mLocker;

    public static WardrobeReturnDialog newInstance(Locker locker) {

        Bundle args = new Bundle();
        args.putParcelable("l", locker);
        WardrobeReturnDialog fragment = new WardrobeReturnDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocker = getArguments().getParcelable("l");
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_wardrobe_return, container, false);
        unbinder = ButterKnife.bind(this, view);

        delegatePresenter(mPresenter, this);

        //        mPresenter.queryInfo();
        onLockerInfo(mLocker);
        return view;
    }

    void onLockerInfo(Locker locker) {
        lockerName.setText(locker.name);
        if (locker.region != null && locker.region.name != null) lockerRg.setText(locker.region.name);
        tvStatus.setText(locker.is_long_term_borrow ? getString(R.string.long_term_hire) : getString(R.string.short_term_hire));
        tvStudent.setText(getString(R.string.user_phone, locker.user.getUsername(), locker.user.getPhone()));
        if (locker.is_long_term_borrow) {
            layoutRemindDay.setVisibility(View.VISIBLE);
            layoutPeriod.setVisibility(View.VISIBLE);
            btnContinueHire.setVisibility(View.VISIBLE);
            try {
                tvTimeLimit.setText(getString(R.string.start_to_end, DateUtils.getYYYYMMDDfromServer(locker.start),
                    DateUtils.getYYYYMMDDfromServer(locker.end)));
                tvRemindDay.setText(DateUtils.dayNumFromToday(DateUtils.formatDateFromServer(locker.end)) + "天");
            } catch (Exception e) {
                //                FIR.sendCrashManually(e);
            }
        } else {
            layoutRemindDay.setVisibility(View.GONE);
            layoutPeriod.setVisibility(View.GONE);
            btnContinueHire.setVisibility(View.GONE);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.btn_wardrobe_return, R.id.btn_continue_hire }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_wardrobe_return:
                if (mLocker.is_long_term_borrow)//归还长期更衣柜
                {
                    RxBus.getBus().post(new EventContinueHire.Builder().locker(mLocker).type(EventContinueHire.BACK).build());
                } else {

                    new MaterialDialog.Builder(getActivity()).content(getString(R.string.alert_retrun_wradrobe, mLocker.name))
                        .positiveText(R.string.common_comfirm)
                        .negativeText(R.string.pickerview_cancel)
                        .canceledOnTouchOutside(true)
                        .autoDismiss(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                showLoading();
                                mPresenter.returnShortTerm(App.staffId,
                                    new ReturnWardrobeBody.Builder().is_long_term_borrow(false).locker_id(mLocker.id).build());
                            }
                        })
                        .show();
                }
                break;
            case R.id.btn_continue_hire:
                RxBus.getBus().post(new EventContinueHire.Builder().type(EventContinueHire.CONTINUE).locker(mLocker).build());
                break;
        }
        dismiss();
    }

    @Override public void onShowError(String e) {
        hideLoading();
    }

    @Override public void onShowError(@StringRes int e) {
        hideLoading();
    }

  @Override public void showAlert(String s) {

  }

  @Override public void showAlert(@StringRes int s) {

  }

    @Override public void showSelectSheet(String title, List<String> strs,
      AdapterView.OnItemClickListener listener) {

    }

    @Override public void popBack() {

    }

    @Override public void popBack(int count) {

    }

    @OnClick(R.id.btn_close) public void onClick() {
        dismiss();
    }

    @Override public void onReturnOk() {
        hideLoading();
        RxBus.getBus().post(new EventFresh());
        dismiss();
    }
}
