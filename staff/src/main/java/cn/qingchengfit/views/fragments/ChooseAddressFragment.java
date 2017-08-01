//package cn.qingchengfit.views.fragments;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//import cn.qingchengfit.staffkit.R;
//import cn.qingchengfit.views.fragments.BaseFragment;
//import cn.qingchengfit.RxBus;
//import cn.qingchengfit.events.EventAddress;
//import cn.qingchengfit.staffkit.views.custom.CitiesChooser;
//import cn.qingchengfit.widgets.CommonInputView;
//import cn.qingchengfit.utils.CompatUtils;
//import cn.qingchengfit.utils.DialogUtils;
//import cn.qingchengfit.utils.GpsUtil;
//import cn.qingchengfit.utils.ToastUtils;
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps2d.AMap;
//import com.amap.api.maps2d.CameraUpdateFactory;
//import com.amap.api.maps2d.MapView;
//import com.amap.api.maps2d.UiSettings;
//import com.amap.api.maps2d.model.CameraPosition;
//import com.amap.api.maps2d.model.LatLng;
//import com.amap.api.services.geocoder.GeocodeResult;
//import com.amap.api.services.geocoder.GeocodeSearch;
//import com.amap.api.services.geocoder.RegeocodeResult;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 2017/4/1.
// */
//
//public class ChooseAddressFragment extends BaseFragment {
//
//    @BindView(R.id.mapview) MapView mMapview;
//    AMap mAMap;
//    @BindView(R.id.city_name) TextView cityName;
//    @BindView(R.id.address) CommonInputView address;
//    @BindView(R.id.toolbar) Toolbar toolbar;
//    @BindView(R.id.toolbar_title) TextView toolbarTitle;
//    private AMapLocationClient mLocationClient;
//    private AMapLocationClientOption mLocationOption;
//    private GeocodeSearch geocoderSearch;
//    private LatLng mLatLng;
//    private String mCityCode;
//    private Unbinder unbinder;
//    private CitiesChooser mCitiesChooser;
//
//    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_choose_address, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        initToolbar(toolbar);
//        toolbarTitle.setText(R.string.address);
//        toolbar.inflateMenu(R.menu.menu_comfirm);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override public boolean onMenuItemClick(MenuItem item) {
//                if (mLatLng == null) {
//                    ToastUtils.showDefaultStyle("尚未获取gp信息,请打开Gps定位，并设置为高精度模式");
//                    return true;
//                }
//                try {
//                    int cityCode = Integer.parseInt(mCityCode);
//                } catch (Exception e) {
//                    ToastUtils.showDefaultStyle("获取城市信息失败，请打开Gps定位，并设置为高精度模式");
//                    return true;
//                }
//
//                RxBus.getBus()
//                    .post(new EventAddress.Builder().city_code(Integer.parseInt(mCityCode))
//                        .city(cityName.getText().toString().trim())
//                        .address(address.getContent().trim())
//                        .lat(mLatLng == null ? 0 : mLatLng.latitude)
//                        .log(mLatLng == null ? 0 : mLatLng.longitude)
//                        .build());
//                getActivity().onBackPressed();
//                return true;
//            }
//        });
//        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override public void onGlobalLayout() {
//                CompatUtils.removeGlobalLayout(toolbar.getViewTreeObserver(), this);
//                mLocationClient = new AMapLocationClient(getContext());
//                mLocationOption = new AMapLocationClientOption();
//
//                mLocationClient.setLocationListener(new AMapLocationListener() {
//                    @Override public void onLocationChanged(AMapLocation aMapLocation) {
//                        mLocationClient.stopLocation();
//                        hideLoading();
//                        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(
//                            new CameraPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 18, 0, 0)));
//
//                        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//                        //设置城市
//                        cityName.setText(aMapLocation.getCity());
//                        mCityCode = aMapLocation.getAdCode();
//
//                        //设置地理位置
//                        address.setContent(aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum());
//                    }
//                });
//                switch (GpsUtil.isOPen(getContext())) {
//                    case 0:
//                        DialogUtils.instanceDelDialog(getContext(), "您未打开GPS定位，需要打开吗？", new MaterialDialog.SingleButtonCallback() {
//                            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                if (which == DialogAction.POSITIVE) {
//                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                    startActivityForResult(intent, 0);
//                                } else if (which == DialogAction.NEGATIVE) {
//                                    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//                                }
//                            }
//                        }).show();
//                        break;
//                    case 1:
//                        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//                        break;
//                    case 2:
//                        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
//                        break;
//                    case 3:
//                        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//                        break;
//                }
//                mLocationClient.setLocationOption(mLocationOption);
//                mLocationClient.startLocation();
//            }
//        });
//
//        showLoading();
//        return view;
//    }
//
//    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mMapview.onCreate(savedInstanceState);
//        if (mAMap == null) {
//            mAMap = mMapview.getMap();
//            UiSettings uiSettings = mAMap.getUiSettings();
//            uiSettings.setZoomGesturesEnabled(true);
//            geocoderSearch = new GeocodeSearch(getContext());
//            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//                @Override public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//                    if (i == 1000) {
//
//                    }
//                }
//
//                @Override public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//                }
//            });
//
//            mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
//                @Override public void onCameraChange(CameraPosition cameraPosition) {
//                }
//
//                @Override public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                    mLatLng = cameraPosition.target;
//                }
//            });
//        }
//    }
//
//    @OnClick(R.id.layout_city) public void onCity() {
//        if (mCitiesChooser == null) {
//            mCitiesChooser = new CitiesChooser(getContext());
//            mCitiesChooser.setOnCityChoosenListener(new CitiesChooser.OnCityChoosenListener() {
//                @Override public void onCityChoosen(String provice, String city, String district, int id) {
//                    cityName.setText(city);
//                    mCityCode = id + "";
//                }
//            });
//        }
//        mCitiesChooser.show(getView());
//    }
//
//    @Override public void onPause() {
//        super.onPause();
//        mMapview.onPause();
//    }
//
//    @Override public void onResume() {
//        super.onResume();
//        mMapview.onResume();
//    }
//
//    @Override public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mMapview.onSaveInstanceState(outState);
//    }
//
//    @Override public String getFragmentName() {
//        return null;
//    }
//
//    @Override public void onDestroyView() {
//        super.onDestroyView();
//        mMapview.onDestroy();
//        unbinder.unbind();
//    }
//
//    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0) {
//            if (GpsUtil.isOPen(getContext()) > 0) {
//                mLocationClient.startLocation();
//            }
//        }
//    }
//}
