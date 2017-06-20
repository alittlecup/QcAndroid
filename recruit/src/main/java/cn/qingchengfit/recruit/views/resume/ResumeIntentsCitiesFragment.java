package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.event.EventIntentCities;
import cn.qingchengfit.views.Cities2Chooser;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/6/13.
 * 1. 期望城市默认为“不限”；城市选择器是2级的即可，最上面加一个不限，第一级选了不限后，第二级就只有一个不限；
 * 2.若只填了一个期望城市，简历里则只展示一个，三个都填了就展示3个。
 */

@FragmentWithArgs public class ResumeIntentsCitiesFragment extends BaseFragment {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.civ_first) CommonInputView civFirst;
  @BindView(R2.id.civ_second) CommonInputView civSecond;
  @BindView(R2.id.civ_third) CommonInputView civThird;
  Cities2Chooser cities2Chooser;
  @Arg ArrayList<CityBean> cityBeanList;
  private int chooseCityId1 = 0;
  private int chooseCityId2 = 0;
  private int chooseCityId3 = 0;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ResumeIntentsCitiesFragmentBuilder.injectArguments(this);
    cities2Chooser = new Cities2Chooser(getContext());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_intent_cities, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    if (cityBeanList.size() > 0) {
      chooseCityId1 = cityBeanList.get(0).id;
      civFirst.setContent(cityBeanList.get(0).getName());
    }
    if (cityBeanList.size() > 1) {
      chooseCityId2 = cityBeanList.get(1).id;
      civSecond.setContent(cityBeanList.get(1).getName());
    }
    if (cityBeanList.size() > 2) {
      chooseCityId3 = cityBeanList.get(2).id;
      civThird.setContent(cityBeanList.get(1).getName());
    }
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("期望城市");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        List<CityBean> p = new ArrayList<CityBean>();
        if (chooseCityId1 != 0) p.add(new CityBean(chooseCityId1, civFirst.getContent()));
        if (chooseCityId2 != 0) p.add(new CityBean(chooseCityId2, civSecond.getContent()));
        if (chooseCityId3 != 0) p.add(new CityBean(chooseCityId3, civThird.getContent()));
        RxBus.getBus().post(new EventIntentCities(p));
        getActivity().onBackPressed();
        return false;
      }
    });
  }

  @Override public String getFragmentName() {
    return ResumeIntentsCitiesFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 第一期望城市
   */
  @OnClick(R2.id.civ_first) public void onCivFirstClicked() {
    cities2Chooser.setOnCityChoosenListener(new Cities2Chooser.OnCityChoosenListener() {
      @Override public void onCityChoosen(String provice, String city, String district, int id) {
        civFirst.setContent(city);
        chooseCityId1 = id;
      }
    });
    cities2Chooser.show(getView());
  }

  /**
   * 第二
   */
  @OnClick(R2.id.civ_second) public void onCivSecondClicked() {
    cities2Chooser.setOnCityChoosenListener(new Cities2Chooser.OnCityChoosenListener() {
      @Override public void onCityChoosen(String provice, String city, String district, int id) {
        civSecond.setContent(city);
        chooseCityId2 = id;
      }
    });
    cities2Chooser.show(getView());
  }

  /**
   * 第三
   */
  @OnClick(R2.id.civ_third) public void onCivThirdClicked() {
    cities2Chooser.setOnCityChoosenListener(new Cities2Chooser.OnCityChoosenListener() {
      @Override public void onCityChoosen(String provice, String city, String district, int id) {
        civThird.setContent(city);
        chooseCityId3 = id;
      }
    });
    cities2Chooser.show(getView());
  }
}
