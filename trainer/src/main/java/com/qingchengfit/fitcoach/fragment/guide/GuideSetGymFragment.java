package com.qingchengfit.fitcoach.fragment.guide;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.bean.CoachInitBean;
import cn.qingchengfit.bean.EventStep;
import cn.qingchengfit.events.EventAddress;
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.saasbase.gymconfig.bean.GymType;
import cn.qingchengfit.saasbase.gymconfig.bean.GymTypeData;
import cn.qingchengfit.saasbase.network.response.QcResponseSystenInit;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.views.fragments.CommonInputTextFragment;
import cn.qingchengfit.widgets.BottomChooseDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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
 * Created by Paper on 16/11/10.
 */
@FragmentWithArgs public class GuideSetGymFragment extends BaseFragment {
    @Arg public String brandid;
    @Arg public String brandImgUrl;
    @Arg public String brandNameStr;
    public String imgUrl;
	protected ImageView brandImg;
	protected TextView brandName;
	protected ImageView gymImg;
	protected CommonInputView gymName;
	protected Button nextStep;
	protected CommonInputView gymAddress;
    protected CommonInputView gymPhone;
    protected CommonInputView gymType;
    protected CommonInputView gymSize;
    protected CommonInputView gymDescribe;
	protected TextView hint;
	protected LinearLayout layoutBrand;

    protected double lat;
    protected double lng;
    protected int city_code;
    protected int gym_type;
    @Inject RepoCoachServiceImpl repoCoachService;

    protected String addressStr;
    protected String gymNameStr;
    protected String phoneStr;
    protected String sizeStr;
    protected String descriptionStr;
    protected int typeInt;

    private BottomChooseDialog dialog;
    private GymTypeData gymTypeData;
    private List<GymType> gymTypes;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_setgym, container, false);
      brandImg = (ImageView) view.findViewById(R.id.brand_img);
      brandName = (TextView) view.findViewById(R.id.brand_name);
      gymImg = (ImageView) view.findViewById(R.id.gym_img);
      gymName = (CommonInputView) view.findViewById(R.id.gym_name);
      gymPhone = (CommonInputView) view.findViewById(R.id.gym_phone);
      nextStep = (Button) view.findViewById(R.id.next_step);
      gymAddress = (CommonInputView) view.findViewById(R.id.gym_address);
      gymType = (CommonInputView) view.findViewById(R.id.gym_type);
      gymSize = (CommonInputView) view.findViewById(R.id.gym_size);
      gymDescribe = (CommonInputView) view.findViewById(R.id.gym_describe);
      hint = (TextView) view.findViewById(R.id.hint);
      layoutBrand = (LinearLayout) view.findViewById(R.id.layout_brand);
      gymSize.setContentType(InputType.TYPE_CLASS_NUMBER);
      view.findViewById(R.id.layout_brand).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          GuideSetGymFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.layout_gym_img).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          GuideSetGymFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.gym_address).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          GuideSetGymFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.gym_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideSetGymFragment.this.onClick(v);
            }
      });
      view.findViewById(R.id.gym_describe).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              GuideSetGymFragment.this.onClick(v);
          }
      });
      view.findViewById(R.id.next_step).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onNextStep();
        }
      });

      Glide.with(getContext())
            .load(PhotoUtils.getSmall(brandImgUrl))
            .asBitmap()
            .placeholder(R.drawable.ic_default_header)
            .into(new CircleImgWrapper(brandImg, getContext()));
        brandName.setText(brandNameStr);
        String initStr = PreferenceUtils.getPrefString(getContext(), "initSystem", "");
        CoachInitBean initBean;
        if (initStr == null || initStr.isEmpty()) {
            initBean = new CoachInitBean();
        } else {
            initBean = new Gson().fromJson(initStr, CoachInitBean.class);
        }

        if (initBean.shop != null) {
            gymName.setContent(initBean.shop.name);
            gymNameStr = initBean.shop.name;
            gymAddress.setContent(initBean.shop.address);
            addressStr = initBean.shop.address;
            sizeStr = initBean.shop.area +"";
            gymSize.setContent(sizeStr);
            descriptionStr = initBean.shop.description;
            gymDescribe.setContent(descriptionStr);
            Glide.with(getContext())
                .load(PhotoUtils.getSmall(initBean.shop.photo))
                .asBitmap()
                .placeholder(R.drawable.ic_default_header)
                .error(R.drawable.ic_default_header)
                .into(new CircleImgWrapper(gymImg, getContext()));
        }

        RxBus.getBus().post(new EventStep.Builder().step(0).build());
        RxBusAdd(EventAddress.class).subscribe(new Action1<EventAddress>() {
            @Override public void call(EventAddress eventAddress) {
                gymAddress.setContent(eventAddress.address);
                addressStr = eventAddress.address;
                city_code = eventAddress.city_code;
                lat = eventAddress.lat;
                lng = eventAddress.log;
            }
        });

        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                showLoading();
                UpYunClient.rxUpLoad("/gym/", eventChooseImage.filePath)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            hideLoading();
                            Glide.with(getContext())
                                .load(PhotoUtils.getSmall(s))
                                .asBitmap()
                                .into(new CircleImgWrapper(gymImg, getContext()));
                            imgUrl = s;
                        }
                    },new NetWorkThrowable());
            }
        });
        getGymType();
        initData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        RxBusAdd(EventTxT.class).onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BusSubscribe<EventTxT>() {
                    @Override public void onNext(EventTxT eventTxT) {
                        gymDescribe.setContent(eventTxT.txt);
                    }
                });
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        gymName.setContent(gymNameStr);
        gymAddress.setContent(addressStr);
        gymPhone.setContent(phoneStr);
    }

    public void initData() {
    }

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }

 public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_brand:

                break;
            case R.id.layout_gym_img:
                ChoosePictureFragmentDialog choosePictureFragmentDialog = ChoosePictureFragmentDialog.newInstance(true);;
                choosePictureFragmentDialog.show(getFragmentManager(), "");
                break;
            case R.id.gym_address:
                new RxPermissions(getActivity())
                    .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(new Action1<Boolean>() {
                        @Override public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                Intent toAddress = new Intent(getActivity(), ChooseActivity.class);
                                startActivity(toAddress);
                            } else {
                                ToastUtils.showDefaultStyle("请开启定位权限");
                            }
                        }
                    });
                break;
            case R.id.gym_type:
                showGymTypeDialog();
                break;
            case R.id.gym_describe:
                if(gymName.getContent() == null) {
                    getFragmentManager().beginTransaction().add(R.id.guide_frag,
                            CommonInputTextFragment.newInstance("描述您的场馆", "", "请填写场馆描述"))
                            .addToBackStack(null)
                            .commit();
                } else {
                    routeTo(CommonInputTextFragment.newInstance("描述您的场馆", "", "请填写场馆描述"));
                }
                break;
        }
    }

 public void onNextStep() {
        if(gymName.isEmpty()) {
             ToastUtils.showDefaultStyle(getString(R.string.err_write_gym_name));
             return;
        }
        if(gymType.isEmpty()) {
            ToastUtils.showDefaultStyle(getString(R.string.err_write_type));
            return;
        }
        if(gymPhone.isEmpty()) {
            ToastUtils.showDefaultStyle(getString(R.string.err_write_phone));
            return;
        }
        if(gymAddress.isEmpty()) {
             ToastUtils.showDefaultStyle(getString(R.string.err_write_address));
             return;
        }
        if(gymSize.isEmpty()) {
             ToastUtils.showDefaultStyle(getString(R.string.err_write_size));
             return;
        }
        if (getParentFragment() instanceof GuideFragment) {
            int gymSizeInt = Integer.parseInt(gymSize.getContent());
            ((GuideFragment) getParentFragment()).initBean.shop = new Shop.Builder()
                    .address(gymAddress.getContent())
                    .gd_district_id(city_code + "")
                    .name(gymName.getContent())
                    .gym_type(gym_type)
                    .phone(gymPhone.getContent())
                    .area(gymSizeInt)
                    .description(gymDescribe.getContent())
                    .gd_lat(lat)
                    .gd_lng(lng)
                    .photo(imgUrl)
                    .build();
            gymNameStr = gymName.getContent();
            RxBus.getBus().post(new CoachInitBean());
            showLoading();
            RxRegiste(TrainerRepository.getStaticTrainerAllApi().qcInit(((GuideFragment) getParentFragment()).getInitBean())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseSystenInit>() {
                    @Override public void call(QcResponseSystenInit qcResponse) {
                        hideLoading();
                        if (qcResponse.status == 200) {
                            repoCoachService.createService((CoachService) qcResponse.data);
                            PreferenceUtils.setPrefString(getContext(), "initSystem", "");
                            if (getActivity().getIntent().getBooleanExtra("workexp", false)) {
                                Intent r = new Intent();
                                r.putExtra("gym", qcResponse.data);
                                getActivity().setResult(Activity.RESULT_OK, r);
                            } else {
                                PreferenceUtils.setPrefString(getContext(), "coachservice_id", qcResponse.data.getId());
                                RxBus.getBus().post(qcResponse.data);
                                RxBus.getBus().post(new EventLoginChange());
                                Intent toMain = new Intent(getActivity(), Main2Activity.class);
                                toMain.putExtra(Main2Activity.ACTION, Main2Activity.INIT);
                                toMain.putExtra("service", qcResponse.data);
                                startActivity(toMain);
                            }
                            getActivity().finish();
                        } else {
                            ToastUtils.showDefaultStyle(qcResponse.msg);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        hideLoading();
                        ToastUtils.showDefaultStyle("创建场馆失败!");
                    }
                }));
        }
    }

    private void showGymTypeDialog() {
        if(dialog != null) {
            dialog.show();
            return;
        }
        List<BottomChooseData> datas = new ArrayList<>();
        if(gymTypeData != null) {
            gymTypes = gymTypeData.gym_types;
            for(int i = 0; i<gymTypes.size(); i++) {
                GymType gymType = gymTypes.get(i);
                datas.add(new BottomChooseData(gymType.gym_type_value));
            }
        }
        dialog = new BottomChooseDialog(getContext(), "场馆类型", datas);
        dialog.setOnItemClickListener(new BottomChooseDialog.onItemClickListener() {
            @Override
            public boolean onItemClick(int position) {
                gymType.setContent(datas.get(position).getContent().toString());
                gym_type = gymTypes.get(position).gym_type;
                return true;
            }
        });
        dialog.show();
    }

    private void getGymType() {
        RxRegiste((Subscription) TrainerRepository.getStaticTrainerAllApi().qcGetGymType()
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcDataResponse<GymTypeData>>() {
                    @Override
                    public void call(QcDataResponse<GymTypeData> qcResponse) {
                        if(qcResponse.status == 200) {
                            gymTypeData = qcResponse.data;
                            gymType.setContent(gymTypeData.gym_types.get(0).gym_type_value);
                        } else {
                            ToastUtils.showDefaultStyle("获取场馆类型失败!");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtils.showDefaultStyle("获取场馆类型失败!");
                    }
                }));
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Brand brand = (Brand) IntentUtils.getParcelable(data);
                brandImgUrl = brand.getPhoto();
                brandNameStr = brand.getName();
                Glide.with(getContext())
                    .load(PhotoUtils.getSmall(brand.getPhoto()))
                    .asBitmap()
                    .into(new CircleImgWrapper(brandImg, getContext()));
                brandName.setText(brand.getName());
                if (getParentFragment() instanceof GuideFragment) {
                    ((GuideFragment) getParentFragment()).initBean.brand_id = brand.getId();
                    RxBus.getBus().post(new CoachInitBean());
                }
            }
        }
    }
}
