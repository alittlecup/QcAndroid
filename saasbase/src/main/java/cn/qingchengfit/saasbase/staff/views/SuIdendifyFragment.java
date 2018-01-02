package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.network.body.GetCodeBody;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saasbase.staff.model.body.CheckCodeBody;
import cn.qingchengfit.saasbase.staff.presenter.SuIdendifyPresenter;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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

@Leaf(module = "staff", path = "/su/change/")
public class SuIdendifyFragment extends SaasBaseFragment implements SuIdendifyPresenter.MVPView {


    @BindView(R2.id.su_avatar) ImageView suAvatar;
    @BindView(R2.id.su_name) TextView suName;
    @BindView(R2.id.su_phone) TextView suPhone;
    @BindView(R2.id.check_code) CommonInputView checkCode;
    @BindView(R2.id.send_msg) TextView sendMsg;
    @BindView(R2.id.btn_next) Button btnNext;
    @BindView(R2.id.send_hint) TextView sendHint;

    @Inject SuIdendifyPresenter mSuIdendifyPresenter;
    @BindView(R2.id.ret_hint) TextView retHint;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_title) TextView toolbarTitile;
    @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;

    private Subscription mSendMsgSp;
    @Need StaffShip mStaff;


    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_identity, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(mSuIdendifyPresenter, this);
        initToolbar(toolbar);
        Glide.with(getContext())
            .load(PhotoUtils.getSmall(mStaff.getAvatar()))
            .asBitmap()
            .into(new CircleImgWrapper(suAvatar, getContext()));
        suName.setText(mStaff.getUsername());
        suName.setCompoundDrawables(null, null, ContextCompat.getDrawable(getContext(),
            mStaff.getGender() == 0 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female), null);
        suPhone.setText(mStaff.getPhone());
        retHint.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_vector_info_red), null, null,
            null);
        sendHint.setText(getString(R.string.send_msg_to_sb_hint, mStaff.getPhone()));
        checkCode.setEnabled(false);
        checkCode.addTextWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                btnNext.setEnabled(s.length() == 6);
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("身份验证");
    }

    @Override public String getFragmentName() {
        return SuIdendifyFragment.class.getName();
    }

    @OnClick(R2.id.send_msg)
    public void onSend(){
        mSuIdendifyPresenter.sendMsg(new GetCodeBody.Builder().phone(mStaff.getPhone()).area_code(mStaff.getArea_code()).build());
    }

    @OnClick(R2.id.btn_next) public void onClick(View view) {
        showLoading();
        mSuIdendifyPresenter.checkIdendify(new CheckCodeBody.Builder().phone(mStaff.getPhone())
            .code(checkCode.getContent())
            .build());
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (mSendMsgSp != null) mSendMsgSp.unsubscribe();
    }

    @Override public void onSendMSGSuccess() {
        mSendMsgSp = Observable.interval(0, 1, TimeUnit.SECONDS)
            .take(60).onBackpressureBuffer().subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Long>() {
                @Override public void call(Long aLong) {
                    if (aLong >= 59) {
                        sendMsg.setEnabled(true);
                        sendMsg.setTextColor(CompatUtils.getColor(getContext(), R.color.colorPrimary));
                        sendMsg.setText(getResources().getString(R.string.login_getcode));
                        sendMsg.setBackgroundResource(R.drawable.bg_rect_prime);
                    } else {
                        sendMsg.setEnabled(false);
                        sendMsg.setTextColor(CompatUtils.getColor(getContext(), R.color.text_grey));
                        sendMsg.setText(String.format(getString(R.string.login_resend_msg), (int) (60 - aLong)));
                        sendMsg.setBackgroundResource(R.drawable.bg_rect_grey);
                    }
                }
            });
    }

    @Override public void onCheckOk() {
        routeTo("/su/new/",SuNewParams.builder().mStaff(mStaff).build());
        hideLoading();
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
