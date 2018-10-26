package com.qingchengfit.fitcoach.fragment.manage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.gymconfig.bean.GymTypeData;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.utils.ToastUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.fragment.guide.GuideSetGymFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import java.util.HashMap;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 2016/12/1.
 */
 public class EditGymFragment extends GuideSetGymFragment {
    private static final String TAG = "EditGymFragment";

    private String id;
    private String model;

    private String name;
    private String photo;
    private String phone;
    private String address;
    private String description;
    private int area;
    private int gym_type;

    public static EditGymFragment newInstance(String id, String model, String name) {
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("model", model);
        args.putString("name", name);
        EditGymFragment fragment = new EditGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static EditGymFragment newInstance(CoachService coachService) {
        Bundle args = new Bundle();
        args.putString("id", coachService.id);
        args.putString("name", coachService.name);
        args.putString("photo", coachService.photo);
        args.putString("phone", coachService.phone);
        args.putString("address", coachService.address);
        args.putInt("gym_type", coachService.gym_type);
        args.putInt("area", coachService.area);
        args.putString("description",coachService.description);
        EditGymFragment fragment = new EditGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            id = getArguments().getString("id");
            model = getArguments().getString("model");
            brandNameStr = getArguments().getString("name");
            lng = getArguments().getDouble("gd_lng");
            lat = getArguments().getDouble("gd_lat");

            name = getArguments().getString("name");
            photo = getArguments().getString("photo");
            phone = getArguments().getString("phone");
            address = getArguments().getString("address");
            gym_type = getArguments().getInt("gym_type");
            area = getArguments().getInt("area");
            description = getArguments().getString("description");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        layoutBrand.setVisibility(View.GONE);
        hint.setVisibility(View.GONE);
        Button btn = ((Button) view.findViewById(R.id.next_step));
        btn.setBackgroundResource(R.drawable.btn_delete);
        btn.setText("删除该场馆");
        ((LinearLayout.LayoutParams) btn.getLayoutParams()).setMargins(0, MeasureUtils.dpToPx(16f, getResources()), 0, 0);
        btn.setTextColor(ContextCompat.getColor(getContext(), R.color.btn_delete_red));
        if (view instanceof LinearLayout) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.common_toolbar, null);
            Toolbar tb = (Toolbar) v.findViewById(R.id.toolbar);
            ((TextView) v.findViewById(R.id.toolbar_title)).setText("完善资料");
            tb.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
            tb.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
            tb.inflateMenu(R.menu.menu_comfirm);
            tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    //修改会员
                    if (TextUtils.isEmpty(gymName.getContent())) {
                        ToastUtils.show("场馆名称不能为空");
                        return true;
                    }
                    if(gymType.isEmpty()) {
                        com.qingchengfit.fitcoach.Utils.ToastUtils.showDefaultStyle(getString(R.string.err_write_type));
                        return true;
                    }
                    if(gymPhone.isEmpty()) {
                        com.qingchengfit.fitcoach.Utils.ToastUtils.showDefaultStyle(getString(R.string.err_write_phone));
                        return true;
                    }
                    if(gymSize.isEmpty()) {
                        com.qingchengfit.fitcoach.Utils.ToastUtils.showDefaultStyle(getString(R.string.err_write_size));
                        return true;
                    }
                    if (lat == 0 || lng == 0) {
                        ToastUtils.show("请重新选择场馆地址");
                        return true;
                    }
                    if (getActivity() instanceof FragActivity) {
                        int gymSizeInt = Integer.parseInt(gymSize.getContent().substring(0, gymSize.getContent().length()-4));
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("id", ((FragActivity) getActivity()).getCoachService().getId());
                        params.put("model", ((FragActivity) getActivity()).getCoachService().getModel());
                        Shop shop = new Shop.Builder()
                                .photo(imgUrl)
                                .gd_district_id(city_code + "")
                                .gd_lat(lat)
                                .gd_lng(lng)
                                .gym_type(gym_type)
                                .name(gymName.getContent())
                                .phone(gymPhone.getContent())
                                .area(gymSizeInt)
                                .address(gymAddress.getContent())
                                .build();
                        RxRegiste(QcCloudClient.getApi().postApi.qcEditGym(id, params, shop)
                            .onBackpressureBuffer()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<QcResponse>() {
                                @Override public void call(QcResponse qcResponse) {
                                    if (ResponseConstant.checkSuccess(qcResponse)) {
                                        getActivity().onBackPressed();
                                    } else {
                                        ToastUtils.show(qcResponse.getMsg());
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override public void call(Throwable throwable) {
                                    ToastUtils.show(throwable.getMessage());
                                }
                            }));
                    }

                    return false;
                }
            });
            TypedValue tv = new TypedValue();
            if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
                ((LinearLayout) view).addView(v, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionBarHeight));
            }
        }
      view.findViewById(R.id.next_step).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onNextStep();
        }
      });
      SensorsUtils.trackScreen(this.getClass().getCanonicalName());
      return view;
    }

//    @Override public void initData() {
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("id", id);
//        params.put("model", model);
//        RxRegiste(QcCloudClient.getApi().getApi.qcGetCoachServer(App.coachid + "", params)
//            .onBackpressureBuffer()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Action1<QcResponseServiceDetail>() {
//                @Override public void call(QcResponseServiceDetail qcResponse) {
//                    if (ResponseConstant.checkSuccess(qcResponse)) {
//                        Glide.with(getContext())
//                            .load(qcResponse.data.gym.getPhoto())
//                            .asBitmap()
//                            .into(new CircleImgWrapper(gymImg, getContext()));
//                        gymName.setContent(qcResponse.data.gym.getName());
//                        gymAddress.setContent(qcResponse.data.gym.getDistrictStr());
//                        lat = qcResponse.data.gym.gd_lat;
//                        lng = qcResponse.data.gym.gd_lng;
//                        city_code = Integer.valueOf(qcResponse.data.gym.getGd_district().city.id);
//                    } else {
//                        ToastUtils.show(qcResponse.getMsg());
//                    }
//                }
//            }, new NetWorkThrowable()));
//    }


    @Override
        public void initData() {
        gymNameStr = name;
        addressStr = address;
        phoneStr = phone;
        descriptionStr = description;
        sizeStr = area + " m2";
        typeInt = gym_type;

        Glide.with(getContext()).load(photo)
                .asBitmap()
                .into(new CircleImgWrapper(gymImg, getContext()));
    }

    public void onNextStep() {
//        showAlert(R.string.contact_gm);
        DialogUtils.showConfirm(getContext(), "提示", "确定要删除该场馆吗？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                if(which == DialogAction.POSITIVE) {
                    RxRegiste((Subscription) QcCloudClient.getApi().postApi.qcDelGym(id)
                            .onBackpressureBuffer()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<QcResponse>() {
                                @Override
                                public void call(QcResponse qcResponse) {
                                    if(qcResponse.status == 200) {
                                        ToastUtils.showDefaultStyle("删除场馆成功!");
                                        Intent toMain = new Intent(getActivity(), Main2Activity.class);
                                        toMain.putExtra(Main2Activity.ACTION, Main2Activity.INIT);
                                        startActivity(toMain);
                                        getActivity().finish();
                                    } else {
                                        showAlert("删除场馆失败!");
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    showAlert("删除场馆失败!");
                                }
                            }));
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });
    }
}