package com.qingchengfit.fitcoach.fragment.unlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.GuideActivity;
import com.qingchengfit.fitcoach.activity.LoginActivity;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

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
 * Created by Paper on 2017/5/19.
 */
public class UnLoginScheduleAdFragment extends BaseFragment {

    @BindView(R.id.btn_use_now) Button btnUseNow;
    @BindView(R.id.btn_login) Button btnLogin;
    @Inject LoginStatus loginStatus;
    @BindView(R.id.img) ImageView img;
    @BindView(R.id.tv1) TextView tv1;

    Unbinder unbinder;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unlog_schedules, container, false);
        unbinder = ButterKnife.bind(this, view);
        Glide.with(getContext()).load(R.drawable.img_guide2).into(img);
        tv1.setText(R.string.str_guide_title_1);
        if (!loginStatus.isLogined()) {
            btnLogin.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.GONE);
        }
        RxBusAdd(EventLoginChange.class).observeOn(AndroidSchedulers.mainThread()).subscribe(eventLoginChange -> {
            if (!loginStatus.isLogined()) {
                btnLogin.setVisibility(View.VISIBLE);
            } else {
                btnLogin.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override public String getFragmentName() {
        return UnLoginScheduleAdFragment.class.getName();
    }

    /**
     * 注册
     */
    @OnClick(R.id.btn_use_now) public void onClickUseNow() {
        if (!loginStatus.isLogined()) {
            Intent toLogin = new Intent(getActivity(), LoginActivity.class);
            toLogin.putExtra("isRegiste", 1);
            startActivity(toLogin);
        }else {
            startActivity(new Intent(getActivity(), GuideActivity.class));
        }
    }

    /**
     * 立即登录
     */
    @OnClick(R.id.btn_login) public void onLogin() {
        Intent toLogin = new Intent(getActivity(), LoginActivity.class);
        toLogin.putExtra("isRegiste", 0);
        startActivity(toLogin);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
