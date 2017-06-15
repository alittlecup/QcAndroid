package cn.qingchengfit.staffkit.views.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshUnloginAd;
import cn.qingchengfit.staffkit.rxbus.event.EventUnloginHomeLevel;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
import cn.qingchengfit.staffkit.views.custom.CircleIndicator;
import cn.qingchengfit.staffkit.views.login.LoginActivity;
import java.util.ArrayList;
import rx.functions.Action1;

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
 * Created by Paper on 2017/2/23.
 */
public class UnloginAdFragment extends BaseFragment {

    public static final int RESULT_LOGIN = 2;
    @BindView(R.id.vp) ViewPager vp;
    @BindView(R.id.splash_indicator) CircleIndicator splashIndicator;
    @BindView(R.id.btn_use_now) Button btnUseNow;
    @BindView(R.id.btn_login) Button btnLogin;
    private int[] imgs = new int[] {
        R.drawable.img_guide1, R.drawable.img_guide2, R.drawable.img_guide3, R.drawable.img_guide4,
    };
    private int[] titles = new int[] {
        R.string.str_guide_title_1, R.string.str_guide_title_2, R.string.str_guide_title_3, R.string.str_guide_title_4,
    };
    private int[] contents = new int[] {
        R.string.str_guide_content_1, R.string.str_guide_content_2, R.string.str_guide_content_3, R.string.str_guide_content_4,
    };

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unlogin_ad, container, false);
        unbinder = ButterKnife.bind(this, view);
        //告诉 未登录home页，新增场馆到第几步了
        RxBus.getBus().post(new EventUnloginHomeLevel(0));
        ArrayList<Fragment> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(new UnloginAdPhotoFragmentBuilder(contents[i], imgs[i], titles[i]).build());
        }

        FragmentAdapter viewPaperAdapter = new FragmentAdapter(getChildFragmentManager(), list);
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(viewPaperAdapter);
        splashIndicator.setViewPager(vp);
        if (TextUtils.isEmpty(App.staffId)) {
            btnLogin.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.GONE);
        }
        RxBusAdd(EventFreshUnloginAd.class).subscribe(new Action1<EventFreshUnloginAd>() {
            @Override public void call(EventFreshUnloginAd eventFreshUnloginAd) {
                if (TextUtils.isEmpty(App.staffId)) {
                    btnLogin.setVisibility(View.VISIBLE);
                } else {
                    btnLogin.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    @Override protected void onVisible() {
        super.onVisible();
    }

    @Override public String getFragmentName() {
        return UnloginAdFragment.class.getName();
    }

    @OnClick({ R.id.btn_use_now, R.id.btn_login }) public void onClickUse(View v) {
        if (!TextUtils.isEmpty(App.staffId)) {
            if (getParentFragment() instanceof HomeUnLoginFragment) {
                ((HomeUnLoginFragment) getParentFragment()).replace(new ChooseBrandInMainFragmentBuilder().build(), true);
            }
        } else {
            Intent toLogin = new Intent(getActivity(), LoginActivity.class);
            toLogin.putExtra("isRegiste", v.getId() == R.id.btn_use_now);
            toLogin.putExtra("ad", true);
            startActivityForResult(toLogin, RESULT_LOGIN);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOGIN) {
                if (getParentFragment() instanceof HomeUnLoginFragment) {
                    ((HomeUnLoginFragment) getParentFragment()).replace(new ChooseBrandInMainFragmentBuilder().build(), true);
                }
            }
        }
    }
}
