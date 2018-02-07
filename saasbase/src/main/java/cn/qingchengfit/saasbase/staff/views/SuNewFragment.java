package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.login.bean.GetCodeBody;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saasbase.staff.model.body.ChangeSuBody;
import cn.qingchengfit.saasbase.staff.presenter.SuNewPresenter;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 2016/12/27.
 */
@Leaf(module = "staff", path = "/su/new/")
public class SuNewFragment extends BaseFragment implements SuNewPresenter.MVPView {


    @BindView(R2.id.hint) TextView hint;
    @BindView(R2.id.old_avatar) ImageView oldAvatar;
    @BindView(R2.id.old_name) TextView oldName;
    @BindView(R2.id.new_avatar) ImageView newAvatar;
    @BindView(R2.id.new_name) TextView newName;
    @BindView(R2.id.new_name_civ) CommonInputView newNameCiv;
    @BindView(R2.id.phone_num) PhoneEditText phoneNum;
    @BindView(R2.id.same_hint) TextView sameHint;
    @BindView(R2.id.comfirm) Button comfirm;
    @BindView(R2.id.pw_view) PasswordView pwView;

    @Inject SuNewPresenter mSuNewPresenter;

    private MaterialDialog alertChange;
    private MaterialDialog dialogSuccess;

    @Need StaffShip mStaff;



    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_su_change_to_new_saas, container, false);
        ButterKnife.bind(this, view);
        //
        delegatePresenter(mSuNewPresenter, this);
        mCallbackActivity.setToolbar("填写新超级管理员信息", false, null, 0, null);

        Glide.with(getContext())
            .load(PhotoUtils.getSmall(mStaff.getAvatar()))
            .asBitmap()
            .into(new CircleImgWrapper(oldAvatar, getContext()));
        oldName.setText(mStaff.getUsername());
        pwView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (phoneNum.checkPhoneNum()) {
                    pwView.blockRightClick(true);
                    mSuNewPresenter.sendMsg(
                        new GetCodeBody.Builder().phone(phoneNum.getPhoneNum()).area_code(phoneNum.getDistrictInt()).build());
                }
            }
        });
        hint.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_vector_info_red), null, null,
            null);
        hint.setCompoundDrawablePadding(16);
        hint.setText(getString(R.string.alert_remove_su, mStaff.getUsername()));
        newNameCiv.addTextWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    newName.setText("新超级管理员");
                } else {
                    newName.setText(s.toString());
                }
            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return SuNewFragment.class.getName();
    }

    @OnClick(R2.id.comfirm) public void onClick() {
        if (!phoneNum.checkPhoneNum()) {
            return;
        }
        if (phoneNum.getPhoneNum().trim().equalsIgnoreCase(mStaff.getPhone())) {
            sameHint.setVisibility(View.VISIBLE);
            return;
        } else {
            sameHint.setVisibility(View.GONE);
        }

        if (!pwView.checkValid()) {
            return;
        }

        if (alertChange != null && alertChange.isShowing()) {
            return;
        }
        alertChange = new MaterialDialog.Builder(getContext()).content(
            getString(R.string.alert_change_su, mStaff.getUsername(), mStaff.getPhone(), newName.getText().toString(),
                phoneNum.getPhoneNum()))
            .canceledOnTouchOutside(true)
            .autoDismiss(true)
            .positiveText(R.string.common_comfirm)
            .negativeText(R.string.common_cancel)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    showLoading();
                    mSuNewPresenter.changeSu(new ChangeSuBody.Builder().area_code(phoneNum.getDistrictInt())
                        .code(pwView.getCode())
                        .phone(phoneNum.getPhoneNum())
                        .username(newName.getText().toString())
                        .build());
                }
            })
            .build();
        alertChange.show();
    }

    @Override public void onSendMSGSuccess() {
        ToastUtils.show("验证码已发送");
        RxRegiste(Observable.interval(0, 1, TimeUnit.SECONDS)
            .take(60).onBackpressureBuffer().subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(aLong -> {
                if (pwView != null && !pwView.isPwMode()) {
                    if (aLong >= 59) {
                        pwView.blockRightClick(false);
                    } else {
                        pwView.blockRightClick(true);
                        pwView.setRightText(String.format(getString(R.string.login_resend_msg), (int) (60 - aLong)));
                    }
                }
            }));
    }

    @Override public void onChangeSuccess() {
        if (dialogSuccess != null && dialogSuccess.isShowing()) {
            return;
        }
        hideLoading();
        dialogSuccess = new MaterialDialog.Builder(getContext()).content("超级管理员身份变更成功")
            .positiveText(R.string.common_comfirm)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    getActivity().setResult(111);
                    getActivity().finish();
                }
            })
            .build();
        dialogSuccess.show();
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
