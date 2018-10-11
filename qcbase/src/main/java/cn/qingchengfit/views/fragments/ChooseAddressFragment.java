package cn.qingchengfit.views.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventAddress;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.CitiesChooser;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.R;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
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
 * Created by Paper on 16/11/9.
 */
public class ChooseAddressFragment extends BaseFragment {

	MapView mMapview;
	TextView cityName;
	CommonInputView address;
	Toolbar toolbar;
	TextView toolbarTitle;
  AMap mAMap;
  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption;
  private LatLng mLatLng;
  private String mCityCode;

  private CitiesChooser mCitiesChooser;
  private Double g_lon;
  private Double g_lat;
  private String defaultAddress;
  private String city;
  private View root;

  public static ChooseAddressFragment newInstance() {
    ChooseAddressFragment fragment = new ChooseAddressFragment();
    return fragment;
  }

  public static ChooseAddressFragment newInstance(Double g_lon, Double g_lat, String address,
      String city, String cityCode) {

    Bundle args = new Bundle();
    args.putDouble("g_lon", g_lon);
    args.putDouble("g_lat", g_lat);
    args.putString("address", address);
    args.putString("city", city);
    args.putString("cityCode", cityCode);
    ChooseAddressFragment fragment = new ChooseAddressFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    root = inflater.inflate(R.layout.fragment_choose_address, container, false);
    mMapview = (MapView) root.findViewById(R.id.mapview);
    cityName = (TextView) root.findViewById(R.id.city_name);
    address = (CommonInputView) root.findViewById(R.id.address);
    toolbar = (Toolbar) root.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) root.findViewById(R.id.toolbar_title);
    root.findViewById(R.id.layout_city).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCity();
      }
    });

    if (getArguments() != null) {
      g_lon = getArguments().getDouble("g_lon");
      g_lat = getArguments().getDouble("g_lat");
      defaultAddress = getArguments().getString("address");
      city = getArguments().getString("city");
      mCityCode = getArguments().getString("cityCode");
      mLatLng = new LatLng(g_lat, g_lon);
      isInputByHand = true;
    }
    initToolbar(toolbar);
    toolbarTitle.setText("选择地址");
    toolbar.inflateMenu(R.menu.menu_comfirm);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (mLatLng == null) {
          ToastUtils.showDefaultStyle("尚未获取gp信息");
          return true;
        }
        try {
          int cityCode = Integer.parseInt(mCityCode);
        } catch (Exception e) {
          ToastUtils.showDefaultStyle("获取城市信息失败，请重试");
          return true;
        }
        if (address.isEmpty()) {
          ToastUtils.showDefaultStyle("请填写详细地址");
          return true;
        }

        RxBus.getBus()
            .post(new EventAddress.Builder().city_code(Integer.parseInt(mCityCode))
                .city(cityName.getText().toString().trim())
                .address(address.getContent().trim())
                .lat(mLatLng == null ? 0 : mLatLng.latitude)
                .log(mLatLng == null ? 0 : mLatLng.longitude)
                .build());
        getActivity().onBackPressed();
        return true;
      }
    });
    root.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    return root;
  }

  private ViewTreeObserver.OnGlobalLayoutListener listener =
      new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override public void onGlobalLayout() {
          Rect r = new Rect();
          root.getWindowVisibleDisplayFrame(r);
          int heightDiff = root.getRootView().getHeight() - (r.bottom - r.top);
          if (heightDiff > 100) {
            hasOpenSoftKey = true;
          }else{
            if(isInputByHand){
              String s = address.getEditText().getText().toString();
              if(!TextUtils.isEmpty(s)){
                if (geocodeSearch != null) {
                  // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
                  GeocodeQuery query = new GeocodeQuery(s, cityName.getText().toString());
                  geocodeSearch.getFromLocationNameAsyn(query);
                }
              }
            }
          }
        }
      };
  boolean hasOpenSoftKey = false;

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    initAmap();
  }

  void initAmap() {
    mLocationClient = new AMapLocationClient(getContext());
    mLocationOption = new AMapLocationClientOption();
    mLocationClient.setLocationOption(mLocationOption);
    //cur position button
    mAMap.setLocationSource(new LocationSource() {
      @Override public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationClient.startLocation();
      }

      @Override public void deactivate() {

      }
    });
    UiSettings mUiSettings = mAMap.getUiSettings();
    mUiSettings.setMyLocationButtonEnabled(true);

    if (mLatLng == null || (mLatLng.latitude == 0 && mLatLng.longitude == 0)) {
      mLocationClient.startLocation();
      //showLoading();
    } else {
      cityName.setText(city);
      address.setContent(defaultAddress);
      mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(
          new CameraPosition(new LatLng(g_lat, g_lon), 18, 0, 0)));
    }
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    mLocationClient.setLocationListener(locationListener);
  }

  AMapLocationListener locationListener = new AMapLocationListener() {
    @Override public void onLocationChanged(AMapLocation aMapLocation) {
      mLocationClient.stopLocation();
      hideLoading();
      mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(
          new CameraPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()),
              18, 0, 0)));

      //设置城市
      cityName.setText(aMapLocation.getCity());
      mCityCode = aMapLocation.getAdCode();

      //设置地理位置
      address.setContent(
          aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum());
    }
  };

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mMapview.onCreate(savedInstanceState);
    if (mAMap == null) {
      mAMap = mMapview.getMap();
      UiSettings uiSettings = mAMap.getUiSettings();
      uiSettings.setZoomGesturesEnabled(true);

      mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
        @Override public void onCameraChange(CameraPosition cameraPosition) {
        }

        @Override public void onCameraChangeFinish(CameraPosition cameraPosition) {
          mLatLng = cameraPosition.target;
          LatLng target = cameraPosition.target;
          if (geocodeSearch != null && !isInputByHand) {
            RegeocodeQuery query =
                new RegeocodeQuery(new LatLonPoint(target.latitude, target.longitude), 100,
                    geocodeSearch.AMAP);
            geocodeSearch.getFromLocationAsyn(query);
          }
        }
      });
      geocodeSearch = new GeocodeSearch(getContext());
      geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
        @Override public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
          if (i == 1000) {
            cityName.setText(regeocodeResult.getRegeocodeAddress().getCity());
            List<PoiItem> pois = regeocodeResult.getRegeocodeAddress().getPois();
            if (pois != null && !pois.isEmpty()) {
              address.setContent(pois.get(0).getTitle());
            } else {
              address.setContent(regeocodeResult.getRegeocodeAddress().getFormatAddress());
            }
          }
        }

        @Override public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
          List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
          if (geocodeAddressList != null && !geocodeAddressList.isEmpty()) {
            GeocodeAddress geocodeAddress = geocodeAddressList.get(0);
            LatLonPoint aMapLocation = geocodeAddress.getLatLonPoint();
            mLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            mAMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(mLatLng, 18, 0, 0)));
          }
        }
      });
    }

    // Handle the ENTER key down.
    address.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_DONE || (
            event != null
                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_DOWN)) {
          String text = v.getText().toString();
          if (!TextUtils.isEmpty(text)) {
            isInputByHand = true;
            if (geocodeSearch != null) {
              // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
              GeocodeQuery query = new GeocodeQuery(text, cityName.getText().toString());
              geocodeSearch.getFromLocationNameAsyn(query);
            }
          }
        }
        return true;
      }
    });
    address.getEditText().addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
          if(hasOpenSoftKey){
            isInputByHand=true;
          }
      }
    });
    address.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
  }

  private boolean isInputByHand = false;

  private GeocodeSearch geocodeSearch;

 public void onCity() {
    if (mCitiesChooser == null) {
      mCitiesChooser = new CitiesChooser(getContext());
      mCitiesChooser.setOnCityChoosenListener((provice, city, district, id) -> {
        cityName.setText(city);
        mCityCode = id + "";
        if (geocodeSearch != null) {
          // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
          GeocodeQuery query = new GeocodeQuery(TextUtils.isEmpty(district)?city:district, cityName.getText().toString());
          geocodeSearch.getFromLocationNameAsyn(query);
        }
      });
    }
    mCitiesChooser.show(getView());
  }

  @Override public void onPause() {
    super.onPause();
    mMapview.onPause();
  }

  @Override public void onResume() {
    super.onResume();
    mMapview.onResume();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mMapview.onSaveInstanceState(outState);
  }

  @Override protected void lazyLoad() {

  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mMapview.onDestroy();
    root.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
  }
}
