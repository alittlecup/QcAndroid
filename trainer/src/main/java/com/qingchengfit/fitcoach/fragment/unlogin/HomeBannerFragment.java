package com.qingchengfit.fitcoach.fragment.unlogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.VpFragment;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleIndicator;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 2017/5/16.
 */
public class HomeBannerFragment extends BaseFragment {

  ViewPager vp;
  CircleIndicator splashIndicator;
  LinearLayout  llGym;
  @Inject LoginStatus loginStatus;
  ArrayList<Fragment> list = new ArrayList<>();
  private int[] imgs = new int[] {
      R.drawable.img_guide1, R.drawable.img_guide3, R.drawable.img_guide4,
  };
  private int[] titles = new int[] {
      R.string.str_guide_title_2, R.string.str_guide_title_3, R.string.str_guide_title_4,
  };
  private int[] contents = new int[] {
      R.string.str_guide_content_1, R.string.str_guide_content_2, R.string.str_guide_content_3,
      R.string.str_guide_content_4,
  };
  private FragmentAdapter viewPaperAdapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    list.clear();
    for (int i = 0; i < 3; i++) {
      list.add(new UnloginAdPhotoFragmentBuilder(contents[i], imgs[i], titles[i]).build());
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home_banner, container, false);
    vp = (ViewPager) view.findViewById(R.id.vp);
    splashIndicator = (CircleIndicator) view.findViewById(R.id.splash_indicator);
    llGym = view.findViewById(R.id.ll_add_gym);
    viewPaperAdapter = new FragmentAdapter(getChildFragmentManager(), list);
    vp.setAdapter(viewPaperAdapter);
    splashIndicator.setViewPager(vp);
    view.findViewById(R.id.btn_add_gym).setOnClickListener(v -> {
      if(loginStatus.isLogined()){
        routeTo("gym", "/gym/search", null);
      }else{
        onLogin();

      }
    });
    view.findViewById(R.id.tv_create_gym).setOnClickListener(v -> {
      if(loginStatus.isLogined()){
        routeTo("gym", "/gym/create", null);
      }else{
        onLogin();
      }
    });
    return view;
  }

  @Override protected void onVisible() {
    super.onVisible();
  }


  public void onLogin() {
    Intent toLogin = new Intent(getContext().getPackageName(),
        Uri.parse(AppUtils.getCurAppSchema(getContext()) + "://login/"));
    toLogin.putExtra("isRegiste", false);
    startActivity(toLogin);
  }

  @Override public String getFragmentName() {
    return HomeBannerFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public class FragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;
    FragmentManager fm;

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fs) {
      super(fm);
      this.fragments = fs;
      this.fm = fm;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
      Fragment fragment = (Fragment) super.instantiateItem(container, position);
      return fragment;
    }

    @Override public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override public int getCount() {
      return fragments.size();
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      super.destroyItem(container, position, object);
      if (position >= getCount()) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();
      }
    }

    @Override public CharSequence getPageTitle(int position) {
      Fragment f = fragments.get(position);
      if (f instanceof VpFragment) {
        return ((VpFragment) f).getTitle();
      } else {
        return "";
      }
    }
  }
}
