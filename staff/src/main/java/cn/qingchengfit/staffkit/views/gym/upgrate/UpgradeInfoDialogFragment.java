package cn.qingchengfit.staffkit.views.gym.upgrate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Router;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.BaseActivity;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.PopFromBottomActivity;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.GymUtils;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import javax.inject.Inject;
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
 * Created by Paper on 2017/1/22.
 */
public class UpgradeInfoDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.btn_left) Button btnLeft;
    @BindView(R.id.btn_right) Button btnRight;

    @Inject GymWrapper gymWrapper;
    @Inject LoginStatus loginStatus;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setWindowAnimations(R.style.animate_dialog);
        View view = inflater.inflate(R.layout.fragment_upgrade_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (gymWrapper.can_trial()) {
            btnRight.setVisibility(View.VISIBLE);
            btnLeft.setBackgroundResource(R.drawable.bg_white_click);
            btnLeft.setTextColor(CompatUtils.getColor(getContext(), R.color.text_black));
        } else {
            btnRight.setVisibility(View.GONE);
            btnLeft.setBackgroundResource(R.drawable.bg_prime_click);
            btnLeft.setTextColor(CompatUtils.getColor(getContext(), R.color.white));
        }
        return view;
    }

    @OnClick({ R.id.btn_close, R.id.btn_left, R.id.btn_right }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_left:
                onLeftClick();
                break;
            case R.id.btn_right:
                onRightClick();
                break;
        }
    }

    public void onLeftClick() {
        Intent toRenewal = new Intent(getActivity(), PopFromBottomActivity.class);
        toRenewal.putExtra(BaseActivity.ROUTER, Router.BOTTOM_RENEWAL);
        startActivity(toRenewal);
        dismiss();
    }

    public void onRightClick() {
        RxRegiste(new RestRepository().getPost_api()
            .qcGymTrial(loginStatus.staff_id(), GymUtils.getParams(gymWrapper.getCoachService(), gymWrapper.getBrand()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        new TrialProDialogFragment().show(getFragmentManager(), "");
                        dismiss();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            }));
    }
}

