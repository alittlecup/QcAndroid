package cn.qingchengfit.saasbase.course.batch.views;

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
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
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
 * Created by Paper on 2017/1/22.
 */
public class UpgradeInfoDialogFragment extends BaseDialogFragment {

    @BindView(R2.id.tv_title) TextView tvTitle;
    @BindView(R2.id.btn_left) Button btnLeft;
    @BindView(R2.id.btn_right) Button btnRight;

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
            btnLeft.setBackgroundResource(R.drawable.btn_qc_rect_round_coner_grey);
            btnLeft.setTextColor(CompatUtils.getColor(getContext(), R.color.text_black));
        } else {
            btnRight.setVisibility(View.GONE);
            btnLeft.setBackgroundResource(R.drawable.btn_prime);
            btnLeft.setTextColor(CompatUtils.getColor(getContext(), R.color.white));
        }
        return view;
    }



    @OnClick( R2.id.btn_close) public void onClick(View view) {
                dismiss();
    }
    @OnClick(R2.id.btn_left)
    public void onLeftClick() {
        //Intent toRenewal = new Intent(getActivity(), PopFromBottomActivity.class);
        //toRenewal.putExtra(BaseActivity.ROUTER, Router.BOTTOM_RENEWAL);
        //startActivity(toRenewal);
        dismiss();
    }
    @OnClick(R2.id.btn_right)
    public void onRightClick() {
        // TODO: 2017/9/15  试用
        //RxRegiste(
        //    .qcGymTrial(loginStatus.staff_id(), GymUtils.getParams(gymWrapper.getCoachService(), gymWrapper.getBrand()))
        //    .onBackpressureBuffer()
        //    .subscribeOn(Schedulers.io())
        //    .observeOn(AndroidSchedulers.mainThread())
        //    .subscribe(new Action1<QcResponse>() {
        //        @Override public void call(QcResponse qcResponse) {
        //            if (ResponseConstant.checkSuccess(qcResponse)) {
        //                new TrialProDialogFragment().show(getFragmentManager(), "");
        //                dismiss();
        //            }
        //        }
        //    }, new Action1<Throwable>() {
        //        @Override public void call(Throwable throwable) {
        //        }
        //    }));
    }
}

