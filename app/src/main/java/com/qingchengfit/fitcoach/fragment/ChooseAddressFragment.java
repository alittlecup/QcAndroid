package com.qingchengfit.fitcoach.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.EventAddress;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
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
 * Created by Paper on 16/11/9.
 */
public class ChooseAddressFragment extends Fragment {

    @Bind(R.id.mapview)
    MapView mMapview;
    AMap mAMap;
    @Bind(R.id.city_name)
    TextView cityName;
    @Bind(R.id.address)
    CommonInputView address;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private GeocodeSearch geocoderSearch;
    private LatLng mLatLng;
    private String mCityCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_address, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbarTitle.setText(R.string.address);
        toolbar.inflateMenu(R.menu.menu_comfirm);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mLatLng == null){
                    ToastUtils.showDefaultStyle("尚未获取gp信息");
                    return true;
                }

                RxBus.getBus().post(new EventAddress.Builder()
                        .city_code(Integer.parseInt(mCityCode))
                        .city(cityName.getText().toString().trim())
                        .address(address.getContent().trim())
                        .lat(mLatLng == null ? 0:mLatLng.latitude)
                        .log(mLatLng == null ? 0:mLatLng.longitude)
                        .build());
                getActivity().onBackPressed();
                return true;
            }
        });

        mMapview.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mMapview.getMap();
            UiSettings uiSettings = mAMap.getUiSettings();
            uiSettings.setZoomGesturesEnabled(true);
            geocoderSearch = new GeocodeSearch(getContext());
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (i == 1000) {
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                }
            });


            mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(cameraPosition.target.latitude,cameraPosition.target.longitude), 200,GeocodeSearch.AMAP);
//                    geocoderSearch.getFromLocationAsyn(query);
                    mLatLng = cameraPosition.target;
                }
            });
        }

        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
//        mLocationOption.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(mLocationOption);
        RxPermissions.getInstance(getContext())
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) mLocationClient.startLocation();
                        else ToastUtils.showDefaultStyle("请开启定位权限");
                    }
                });
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 18, 0, 0)));

                //设置城市
                cityName.setText(aMapLocation.getCity());
                mCityCode = aMapLocation.getCityCode();

                //设置地理位置
                address.setContent(aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum());
                mLocationClient.stopLocation();

            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapview.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapview.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapview.onDestroy();
        ButterKnife.unbind(this);
    }
}
