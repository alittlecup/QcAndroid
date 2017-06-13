package cn.qingchengfit.staffkit.views.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
import cn.qingchengfit.staffkit.usecase.bean.ModifyPwBody;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.PasswordView;
import cn.qingchengfit.widgets.PhoneEditText;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/2/22 2016.
 */
public class FixPwFragment extends BaseDialogFragment implements FixPwView {

    @BindView(R.id.modifypw_new_pw) EditText modifypwNewPw;
    @BindView(R.id.modifypw_comfirm_pw) EditText modifypwComfirmPw;
    @BindView(R.id.modifypw_comfirm_btn) Button modifypwComfirmBtn;

    @Inject FixPwPresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.phone_num) PhoneEditText phoneNum;
    @BindView(R.id.checkcode) PasswordView checkcode;
    private Subscription mSendMsgSp;

    public static void start(Fragment fragment, int requestCode) {
        BaseDialogFragment f = newInstance();
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static FixPwFragment newInstance() {
        Bundle args = new Bundle();
        FixPwFragment fragment = new FixPwFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_pw, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        toolbarTitile.setText(R.string.fix_password);
        presenter.attachView(this);
        checkcode.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (phoneNum.checkPhoneNum()) {
                    presenter.onQueryCode(
                        new GetCodeBody.Builder().phone(phoneNum.getPhoneNum()).area_code(phoneNum.getDistrictInt()).build());
                    checkcode.blockRightClick(true);
                    mSendMsgSp = Observable.interval(0, 1, TimeUnit.SECONDS)
                        .take(60)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override public void call(Long aLong) {

                                if (aLong >= 59) {
                                    checkcode.blockRightClick(false);
                                    checkcode.setRightText(getResources().getString(R.string.login_getcode));
                                } else {
                                    checkcode.setRightText(String.format(getString(R.string.login_resend_msg), (int) (60 - aLong)));
                                }
                            }
                        });
                }
            }
        });
        return view;
    }

    @OnClick(R.id.modifypw_comfirm_btn) public void onComfirm() {
        String pw = modifypwNewPw.getText().toString().trim();
        String pwre = modifypwComfirmPw.getText().toString().trim();
        if (TextUtils.isEmpty(pw)) {
            ToastUtils.showDefaultStyle(getString(R.string.err_password_length));
            return;
        }
        if (!pw.equalsIgnoreCase(pwre)) {
            ToastUtils.showDefaultStyle(getString(R.string.err_password_not_match));
            return;
        }
        if (phoneNum.checkPhoneNum() && checkcode.checkValid()) {
            presenter.onFixPw(new ModifyPwBody.Builder().phone(phoneNum.getPhoneNum()).code(checkcode.getCode()).password(pw).build());
        }
    }

    @Override public void onDestroyView() {
        if (mSendMsgSp != null) mSendMsgSp.unsubscribe();
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onSucceed() {
        ToastUtils.show(getString(R.string.fix_succees));
        this.dismiss();
    }

    @Override public void onError(String e) {
        ToastUtils.show(e);
    }
}
