package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventAddress;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.CitiesChooser;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;

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

    @BindView(R2.id.mapview) MapView mMapview;
    @BindView(R2.id.city_name) TextView cityName;
    @BindView(R2.id.address) CommonInputView address;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_title) TextView toolbarTitle;
    AMap mAMap;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private GeocodeSearch geocoderSearch;
    private LatLng mLatLng;
    private String mCityCode;
    private Unbinder unbinder;
    private CitiesChooser mCitiesChooser;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_address, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(toolbar.getViewTreeObserver(), this);
                mLocationClient = new AMapLocationClient(getContext());
                mLocationOption = new AMapLocationClientOption();
                mLocationClient.setLocationOption(mLocationOption);

                mLocationClient.startLocation();
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mLocationClient.setLocationListener(new AMapLocationListener() {
                    @Override public void onLocationChanged(AMapLocation aMapLocation) {
                        mLocationClient.stopLocation();
                        hideLoading();
                        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 18, 0, 0)));

                        //设置城市
                        cityName.setText(aMapLocation.getCity());
                        mCityCode = aMapLocation.getAdCode();

                        //设置地理位置
                        address.setContent(aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum());
                    }
                });
            }
        });

        showLoading();
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapview.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mMapview.getMap();
            UiSettings uiSettings = mAMap.getUiSettings();
            uiSettings.setZoomGesturesEnabled(true);
            geocoderSearch = new GeocodeSearch(getContext());
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (i == 1000) {

                    }
                }

                @Override public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                }
            });

            mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override public void onCameraChange(CameraPosition cameraPosition) {
                }

                @Override public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    mLatLng = cameraPosition.target;
                }
            });
        }
    }

    @OnClick(R2.id.layout_city) public void onCity() {
        if (mCitiesChooser == null) {
            mCitiesChooser = new CitiesChooser(getContext());
            mCitiesChooser.setOnCityChoosenListener(new CitiesChooser.OnCityChoosenListener() {
                @Override public void onCityChoosen(String provice, String city, String district, int id) {
                    cityName.setText(city);
                    mCityCode = id + "";
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
    }
}
