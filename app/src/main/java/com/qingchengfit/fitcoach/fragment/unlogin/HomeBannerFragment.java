package com.qingchengfit.fitcoach.fragment.unlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.event.EventLoginChange;
import cn.qingchengfit.views.FragmentAdapter;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.GuideActivity;
import com.qingchengfit.fitcoach.activity.LoginActivity;
import com.qingchengfit.fitcoach.component.CircleIndicator;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
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
 * Created by Paper on 2017/5/16.
 */
public class HomeBannerFragment extends BaseFragment {

    @BindView(R.id.vp) ViewPager vp;
    @BindView(R.id.splash_indicator) CircleIndicator splashIndicator;
    @BindView(R.id.btn_use_now) Button btnUseNow;
    @BindView(R.id.btn_login) Button btnLogin;
    @Inject LoginStatus loginStatus;
    ArrayList<Fragment> list = new ArrayList<>();
    private int[] imgs = new int[] {
        R.drawable.img_guide1, R.drawable.img_guide3, R.drawable.img_guide4,
    };
    private int[] titles = new int[] {
         R.string.str_guide_title_2, R.string.str_guide_title_3, R.string.str_guide_title_4,
    };
    private int[] contents = new int[] {
        R.string.str_guide_content_1, R.string.str_guide_content_2, R.string.str_guide_content_3, R.string.str_guide_content_4,
    };
    private FragmentAdapter viewPaperAdapter;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.clear();
        for (int i = 0; i < 3; i++) {
            list.add(new UnloginAdPhotoFragmentBuilder(contents[i],imgs[i],titles[i] ).build());
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_banner, container, false);
        unbinder = ButterKnife.bind(this, view);
        viewPaperAdapter = new FragmentAdapter(getChildFragmentManager(),list);
        vp.setAdapter(viewPaperAdapter);
        splashIndicator.setViewPager(vp);
        if (!loginStatus.isLogined()) {
            btnLogin.setVisibility(View.VISIBLE);
        }else btnLogin.setVisibility(View.GONE);

        RxBusAdd(EventLoginChange.class).delay(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(eventLoginChange -> {
                if (btnLogin == null)
                    return;
                if (btnLogin != null) {
                    if (!loginStatus.isLogined()) {
                        btnLogin.setVisibility(View.VISIBLE);
                    } else btnLogin.setVisibility(View.GONE);
                }
        });

        return view;
    }

    @Override protected void onVisible() {
        super.onVisible();

    }

    /**
     * 注册
     */
    @OnClick(R.id.btn_use_now)
    public void onClickUseNow(){
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
    @OnClick(R.id.btn_login)
    public void onLogin(){
        Intent toLogin = new Intent(getActivity(), LoginActivity.class);
        toLogin.putExtra("isRegiste", 0);
        startActivity(toLogin);
    }


    @Override public String getFragmentName() {
        return HomeBannerFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
