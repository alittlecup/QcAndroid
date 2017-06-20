package cn.qingchengfit.views;

import android.content.Context;
import android.view.View;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.model.base.DistrictBean;
import cn.qingchengfit.model.base.ProvinceBean;
import cn.qingchengfit.model.common.CitiesData;
import cn.qingchengfit.utils.FileUtils;
import com.bigkoo.pickerview.OptionsDialog;
import com.google.gson.Gson;
import java.util.ArrayList;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/18 2015.
 */
public class CitiesChooser {

    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
    private OptionsDialog pwOptions;
    private OnCityChoosenListener onCityChoosenListener;

    public CitiesChooser(Context context) {
        pwOptions = new OptionsDialog(context);
        Gson gson = new Gson();
        final CitiesData citiesData = gson.fromJson(FileUtils.getJsonFromAssert("cities.json", context), CitiesData.class);
        for (int i = 0; i < citiesData.provinces.size(); i++) {
            ProvinceBean provinceBean = citiesData.provinces.get(i);
            String provinceName = "";
            if (provinceBean.name.length() > 5) {
                provinceName = provinceBean.name.substring(0, 4).concat("...");
            } else {
                provinceName = provinceBean.name;
            }
            options1Items.add(provinceName);
            ArrayList<String> citynames = new ArrayList<>();
            ArrayList<ArrayList<String>> option3 = new ArrayList<ArrayList<String>>();
            for (int j = 0; j < provinceBean.cities.size(); j++) {
                CityBean cityBean = provinceBean.cities.get(j);
                String cityName = "";
                if (cityBean.name.length() > 5) {
                    cityName = cityBean.name.substring(0, 4).concat("...");
                } else {
                    cityName = cityBean.name;
                }

                citynames.add(cityName);
                ArrayList<String> district = new ArrayList<>();
                for (int k = 0; k < cityBean.districts.size(); k++) {
                    DistrictBean districtBean = cityBean.districts.get(k);
                    String dName = "";
                    if (districtBean.name.length() > 5) {
                        dName = districtBean.name.substring(0, 4).concat("...");
                    } else {
                        dName = districtBean.name;
                    }

                    district.add(dName);
                }
                option3.add(district);
            }
            options2Items.add(citynames);
            options3Items.add(option3);
        }
        pwOptions.setPicker(options1Items, options2Items, options3Items, true);
        pwOptions.setLabels("", "", "");
        pwOptions.setSelectOptions(0, 0, 0);
        pwOptions.setOnoptionsSelectListener(new OptionsDialog.OnOptionsSelectListener() {
            @Override public void onOptionsSelect(int i, int i1, int i2) {
                if (onCityChoosenListener != null) {
                    if (options2Items.get(i).get(i1).startsWith(options1Items.get(i))) {
                      onCityChoosenListener.onCityChoosen("", options2Items.get(i).get(i1), "",
                          citiesData.provinces.get(i).cities.get(i1).districts.get(i1).id);
                    } else {
                        onCityChoosenListener.onCityChoosen(options1Items.get(i), options2Items.get(i).get(i1),
                            options3Items.get(i).get(i1).get(i2), citiesData.provinces.get(i).cities.get(i1).districts.get(i2).id);
                    }
                }
            }
        });
    }

    public void setOnCityChoosenListener(OnCityChoosenListener onCityChoosenListener) {
        this.onCityChoosenListener = onCityChoosenListener;
    }

    public void show(View v) {
        pwOptions.show();
    }

    public void show(int distrcitid) {
        pwOptions.show();
    }

    public void hide() {
        pwOptions.hide();
    }

    public interface OnCityChoosenListener {
        void onCityChoosen(String provice, String city, String district, int id);
    }
}
