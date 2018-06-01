package com.qingchengfit.fitcoach.fragment.unlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;




import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.saasbase.login.LoginActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.GuideActivity;
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

	Button btnUseNow;
	Button btnLogin;
    @Inject LoginStatus loginStatus;
	ImageView img;
	TextView tv1;



    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unlog_schedules, container, false);
      btnUseNow = (Button) view.findViewById(R.id.btn_use_now);
      btnLogin = (Button) view.findViewById(R.id.btn_login);
      img = (ImageView) view.findViewById(R.id.img);
      tv1 = (TextView) view.findViewById(R.id.tv1);
      view.findViewById(R.id.btn_use_now).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickUseNow();
        }
      });
      view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onLogin();
        }
      });

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
 public void onClickUseNow() {
        if (!loginStatus.isLogined()) {
            Intent toLogin = new Intent(getActivity(), LoginActivity.class);
            toLogin.putExtra("isRegiste", true);
            startActivity(toLogin);
        }else {
            startActivity(new Intent(getActivity(), GuideActivity.class));
        }
    }

    /**
     * 立即登录
     */
 public void onLogin() {
        Intent toLogin = new Intent(getActivity(), LoginActivity.class);
        toLogin.putExtra("isRegiste", false);
        startActivity(toLogin);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }
}
