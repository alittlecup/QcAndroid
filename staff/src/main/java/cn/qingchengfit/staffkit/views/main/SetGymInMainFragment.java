package cn.qingchengfit.staffkit.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventAddress;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.common.Shop;
import cn.qingchengfit.model.responese.QcResponseSystenInit;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.cards.views.WriteDescFragment;
import cn.qingchengfit.saascommon.model.GymBaseInfoAction;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.rxbus.event.EventUnloginHomeLevel;
import cn.qingchengfit.staffkit.rxbus.event.SaveEvent;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
 * Created by Paper on 2017/2/23.
 */
@FragmentWithArgs public class SetGymInMainFragment extends BaseFragment {
  ImageView brandImg;
  TextView brandName;
  ImageView gymImg;
  CommonInputView gymName;
  CommonInputView address;
  CommonInputView phone;
  CommonInputView descripe;
  Button comfirm;
  Toolbar toolbar;
  TextView toolbarTitle;

  @Arg Brand mBrand;
  ImageView guideStep1;

  @Inject GymBaseInfoAction gymBaseInfoAction;
  @Inject StaffRespository restRepository;
  private String gymImgStr;
  private String descripeStr;
  private String cityStr;
  private String addressStr;
  private TextChange textChange = new TextChange();
  private int city_code;
  private double lat, lng;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_set_gym, container, false);
    brandImg = (ImageView) view.findViewById(R.id.brand_img);
    brandName = (TextView) view.findViewById(R.id.brand_name);
    gymImg = (ImageView) view.findViewById(R.id.header);
    gymName = (CommonInputView) view.findViewById(R.id.gym_name);
    address = (CommonInputView) view.findViewById(R.id.address);
    phone = (CommonInputView) view.findViewById(R.id.phone);
    descripe = (CommonInputView) view.findViewById(R.id.descripe);
    comfirm = (Button) view.findViewById(R.id.comfirm);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    guideStep1 = (ImageView) view.findViewById(R.id.guide_step_1);
    view.findViewById(R.id.layout_brand).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFun(v);
      }
    });
    view.findViewById(R.id.layout_gym_img).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFun(v);
      }
    });
    view.findViewById(R.id.comfirm).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFun(v);
      }
    });
    view.findViewById(R.id.address).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickContent(v);
      }
    });
    view.findViewById(R.id.descripe).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickContent(v);
      }
    });

    brandName.setText(mBrand.getName());
    Glide.with(getContext())
        .load(PhotoUtils.getSmall(mBrand.getPhoto()))
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(brandImg, getContext()));
    gymName.addTextWatcher(textChange);
    address.addTextWatcher(textChange);
    phone.addTextWatcher(textChange);
    guideStep1.setVisibility(View.GONE);
    initToolbar(toolbar);
    RxBusAdd(EventAddress.class).subscribe(new Action1<EventAddress>() {
      @Override public void call(EventAddress eventAddress) {
        address.setContent(eventAddress.address);
        addressStr = eventAddress.address;
        city_code = eventAddress.city_code;
        lat = eventAddress.lat;
        lng = eventAddress.log;
      }
    });
    //告诉 未登录home页，新增场馆到第几步了
    RxBus.getBus().post(new EventUnloginHomeLevel(2));
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    toolbarTitle.setText("创建您的场馆");
    super.initToolbar(toolbar);
  }

  @Override public String getFragmentName() {
    return "";
  }

  public void onClickFun(View view) {
    switch (view.getId()) {
      case R.id.layout_brand:

        break;

      case R.id.layout_gym_img:
        ChoosePictureFragmentDialog dialog = new ChoosePictureFragmentDialog();
        dialog.show(getFragmentManager(), "");
        dialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
          @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
            SetGymInMainFragment.this.RxRegiste(
                UpYunClient.rxUpLoad("gym/", filePath).subscribe(new Action1<String>() {
                  @Override public void call(String s) {
                    Glide.with(getContext())
                        .load(PhotoUtils.getSmall(s))
                        .asBitmap()
                        .into(new CircleImgWrapper(gymImg, getContext()));
                    gymImgStr = s;
                  }
                }));
          }
        });
        break;
      case R.id.comfirm:

        SystemInitBody body = new SystemInitBody();
        body.brand_id = mBrand.getId();
        body.shop = new Shop();
        body.shop.address = address.getContent();
        body.shop.description = descripeStr;
        body.shop.name = gymName.getContent().trim();
        body.shop.phone = phone.getContent();
        body.shop.photo = gymImgStr;
        body.shop.gd_district_id = city_code + "";
        body.shop.gd_lat = lat;
        body.shop.gd_lng = lng;
        RxBus.getBus().post(new SaveEvent());
        body.auto_trial = false;
        showLoading();
        RxRegiste(restRepository.getStaffAllApi()
            .qcSystemInit(body)
            .retry(new Func2<Integer, Throwable, Boolean>() {
              @Override public Boolean call(Integer integer, Throwable throwable) {
                return integer < 3 && throwable instanceof SocketTimeoutException;
              }
            })
            .doOnError(throwable -> {
              if (throwable != null) Timber.e("retrofit:" + throwable.getMessage());
            })
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponseSystenInit>() {
              @Override public void call(QcResponseSystenInit qcResponseSystenInit) {
                if (ResponseConstant.checkSuccess(qcResponseSystenInit)) {
                  if (getActivity() instanceof MainActivity) {
                    List<CoachService> coachServices = new ArrayList<>();
                    coachServices.add(qcResponseSystenInit.data);
                    gymBaseInfoAction.writeGyms(coachServices);
                    //记录创建场馆动作
                    SensorsUtils.track("QcSaasCreateShop")
                        .addProperty("qc_brand_shops_count", "0")
                        .commit(getContext());
                    RxBus.getBus().post(new EventLoginChange());
                  }
                } else {
                  ToastUtils.showDefaultStyle(qcResponseSystenInit.getMsg());
                }
              }
            }));
        break;
    }
  }

  public void onClickContent(View view) {
    switch (view.getId()) {
      case R.id.address:
        Intent toAddress = new Intent(getContext(), ChooseActivity.class);
        toAddress.putExtra("to", ChooseActivity.CHOOSE_ADDRESS);
        startActivity(toAddress);
        break;
      case R.id.descripe:
        WriteDescFragment.start(this, 2, getString(R.string.title_write_gym_descript),
            getString(R.string.guide_write_gym_descript));
        break;
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case 1:
        cityStr = IntentUtils.getIntentString(data);
        addressStr = IntentUtils.getIntentString2(data);
        address.setContent(cityStr + addressStr);

        break;
      case 2:
        descripe.setContent(getString(R.string.common_have_setting));
        descripeStr = IntentUtils.getIntentString(data);
      default:
        break;
    }
  }

  class TextChange implements TextWatcher {

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {
      comfirm.setEnabled(!TextUtils.isEmpty(gymName.getContent())
          && !TextUtils.isEmpty(address.getContent())
          && !TextUtils.isEmpty(phone.getContent()));
    }
  }
}
