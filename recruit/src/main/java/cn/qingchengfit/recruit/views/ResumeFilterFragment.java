package cn.qingchengfit.recruit.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.model.base.ProvinceBean;
import cn.qingchengfit.model.common.CitiesData;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.FilterFragment;
import cn.qingchengfit.views.fragments.FilterLeftRightFragment;
import com.google.gson.Gson;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Created by Paper on 2017/7/3.
 */
public class ResumeFilterFragment extends BaseFragment
    implements FilterLeftRightFragment.OnLeftRightSelectListener {

  public int showPos = 0;
  protected FilterDamenFragment filterDamenFragment;
  protected String[] filterType = new String[] { "city", "salary", "workyear", "other" };
  private FilterLeftRightFragment filterLeftRightFragment;
  private FilterFragment filterSalaryFragment;
  private FilterFragment filterWorkYearFragment;
  private List<ProvinceBean> provinceBeanList = new ArrayList<>();
  private List<CityBean> cityBeanList = new ArrayList<>();
  private CitiesData citiesData;
  private HashMap<String, Object> params = new HashMap<>();
  private ResumeFilterListener listener;
  private String selectedCityName;

  public static ResumeFilterFragment newResumeFilter() {
    Bundle args = new Bundle();
    ResumeFilterFragment fragment = new ResumeFilterFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public void putParams(HashMap<String, Object> p) {
    params.putAll(p);
  }
  public ResumeFilterListener getListener() {
    return listener;
  }

  public void setListener(ResumeFilterListener listener) {
    this.listener = listener;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    filterDamenFragment = FilterDamenFragment.newInstanceResumeFilter();
    filterSalaryFragment = new FilterFragment();
    filterWorkYearFragment = new FilterFragment();
    filterLeftRightFragment = new FilterLeftRightFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_resume_filter, container, false);
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    view.findViewById(R.id.layout_bg).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
    showStrategy();
    return view;
  }

  protected void showStrategy() {
    show(filterType[showPos]);
  }

  @Override public String getFragmentName() {
    return ResumeFilterFragment.class.getName();
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
  }

  @Override protected void onVisible() {
    super.onVisible();
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof FilterLeftRightFragment) dealCityFilter();
  }

  public void show(int p) {
    showPos = p;
    showStrategy();
  }

  public void show(String module) {
    Fragment f = getChildFragmentManager().findFragmentByTag(module);
    if (f == null) {
      switch (module) {
        case "city":
          f = filterLeftRightFragment;
          filterLeftRightFragment.setListener(this);
          break;
        case "salary":
          f = filterSalaryFragment;
          List<AbstractFlexibleItem> salaryitems = new ArrayList<>();
          for (String strItem : getResources().getStringArray(R.array.resume_filter_salary)) {
            salaryitems.add(new FilterCommonLinearItem(strItem));
          }
          filterSalaryFragment.setItemList(salaryitems);
          filterSalaryFragment.setOnSelectListener(new FilterFragment.OnSelectListener() {
            @Override public void onSelectItem(int position) {
              //点击薪水
              params = RecruitBusinessUtils.getSalaryFilter(position, params);
              onChooseitem();
            }
          });
          break;
        case "workyear":
          f = filterWorkYearFragment;
          List<AbstractFlexibleItem> workYearItems = new ArrayList<>();
          for (String strItem : getResources().getStringArray(R.array.resume_filter_work_year)) {
            workYearItems.add(new FilterCommonLinearItem(strItem));
          }
          filterWorkYearFragment.setItemList(workYearItems);
          filterWorkYearFragment.setOnSelectListener(new FilterFragment.OnSelectListener() {
            @Override public void onSelectItem(int position) {
              //点击工作年限
              params = RecruitBusinessUtils.getWrokExpParams(position - 1, params);
              onChooseitem();
            }
          });
          break;
        default://其他
          f = filterDamenFragment;
          filterDamenFragment.setListener(new FilterDamenFragment.OnDemandsListener() {
            @Override public void onDemands(HashMap<String, Object> p) {
              params.putAll(p);
              onChooseitem();
            }

            @Override public void onDemandsReset() {
              params = RecruitBusinessUtils.getGenderParams(-1, params);
              params = RecruitBusinessUtils.getDegreeParams(-1, params);
              params = RecruitBusinessUtils.getAgeParams(-1, params);
              params = RecruitBusinessUtils.getWorkYearParams(-1, params);
              params = RecruitBusinessUtils.getHeightParams(-1, params);
              params = RecruitBusinessUtils.getWeightParams(-1, params);
              onChooseitem();
            }
          });
          break;
      }
      getChildFragmentManager().beginTransaction().add(R.id.frag_resume_filter, f, module).commit();
    }
    hideAllandShow(module);
  }

  public void hideAllandShow(String m) {
    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
    for (String s : filterType) {
      Fragment f1 = getChildFragmentManager().findFragmentByTag(s);
      if (f1 != null) {
        if (s.equals(m)) {
          ft.show(f1);
        } else {
          ft.hide(f1);
        }
      }
    }
    ft.commit();
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  @Override public void onLeftSelected(int position) {
    cityBeanList = provinceBeanList.get(position).cities;
    filterLeftRightFragment.onChangedCity(cityBeanList);
  }

  @Override public void onRightSelected(int position) {
    CityBean cityBean = cityBeanList.get(position);
    selectedCityName = cityBean.name;
    if (cityBean.getId() == -1) {
      params.put("city_id", null);
      selectedCityName = null;
    } else {
      params.put("city_id", cityBean.getId());
    }
    onChooseitem();
  }

  /**
   * 当选择完一个选项之后，关闭筛选页面
   */
  private void onChooseitem() {
    if (listener != null) listener.onFilterDone(this.params, selectedCityName);
    dismiss();

  }

  public void dismiss() {
    if (listener != null) listener.onDismiss();
    getFragmentManager().beginTransaction().hide(this).commit();
  }

  public void dealCityFilter() {
    DealCityFilterAsyc dealCityFilterAsyc = new DealCityFilterAsyc();
    dealCityFilterAsyc.execute("cities.json");
  }

  public interface ResumeFilterListener {
    void onFilterDone(HashMap<String, Object> params, String cityName);

    void onDismiss();
  }

  class DealCityFilterAsyc extends AsyncTask<String, Integer, List<String>> {

    @Override protected List<String> doInBackground(String... params) {
      Gson gson = new Gson();
      citiesData =
          gson.fromJson(FileUtils.getJsonFromAssert(params[0], getContext()), CitiesData.class);
      provinceBeanList = citiesData.provinces;
      //在provinceBeanList添加全部
      ProvinceBean provinceBeanAll = new ProvinceBean();
      provinceBeanAll.name = "全部";
      provinceBeanAll.id = "-1";
      CityBean cb = new CityBean("-1", "全部");
      provinceBeanAll.cities = new ArrayList<>();
      provinceBeanAll.cities.add(cb);
      provinceBeanList.add(0, provinceBeanAll);
      List<String> cityFilterLeftList = new ArrayList<String>();
      for (ProvinceBean provinceBean : citiesData.provinces) {
        cityFilterLeftList.add(provinceBean.name);
      }
      return cityFilterLeftList;
    }

    @Override protected void onPostExecute(List<String> strings) {
      filterLeftRightFragment.setLeftItemList(strings);
    }
  }
}
