package cn.qingchengfit.staffkit.views.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.usecase.bean.FixPhoneBody;
import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
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

public class FixPhoneFragment extends BaseDialogFragment implements FixPwView {
    @BindView(R.id.modifyphone_comfirm_btn) Button modifyphoneComfirmBtn;

    @Inject FixPhonePresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.phone_num) PhoneEditText phoneNum;
    @BindView(R.id.check_code) PasswordView checkCode;
    @BindView(R.id.password) PasswordView password;
    private Subscription mSendMsgSp;

    public static void start(Fragment fragment, int requestCode) {
        BaseDialogFragment f = newInstance();
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static FixPhoneFragment newInstance() {
        Bundle args = new Bundle();
        FixPhoneFragment fragment = new FixPhoneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_phone, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        toolbarTitile.setText(R.string.title_change_phone);
        presenter.attachView(this);
        checkCode.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onGetCode();
            }
        });
        return view;
    }

    public void onGetCode() {
        if (phoneNum.checkPhoneNum()) {

            presenter.onQueryCode(new GetCodeBody.Builder().phone(phoneNum.getPhoneNum()).area_code(phoneNum.getDistrictInt()).build());
            checkCode.blockRightClick(true);
            mSendMsgSp = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(60).onBackpressureBuffer().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Action1<Long>() {
                    @Override public void call(Long aLong) {

                        if (aLong >= 59) {
                            checkCode.blockRightClick(false);
                            checkCode.setRightText(getResources().getString(R.string.login_getcode));
                        } else {
                            checkCode.setRightText(String.format(getString(R.string.login_resend_msg), (int) (60 - aLong)));
                        }
                    }
                });
        }
    }

    @OnClick(R.id.modifyphone_comfirm_btn) public void onConfirm() {

        if (phoneNum.checkPhoneNum() && checkCode.checkValid() && password.checkValid()) {
            presenter.fixPhone(new FixPhoneBody.Builder().phone(phoneNum.getPhoneNum())
                .area_code(phoneNum.getDistrictInt())
                .code(checkCode.getCode())
                .password(password.getCode())
                .build());
        }
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        if (mSendMsgSp != null) mSendMsgSp.unsubscribe();
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
