package cn.qingchengfit.staffkit.views.gym;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventAddress;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.body.GymBody;
import cn.qingchengfit.model.common.Shop;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.rxbus.event.FreshGymListEvent;
import cn.qingchengfit.staffkit.rxbus.event.RxCompleteGuideEvent;
import cn.qingchengfit.staffkit.rxbus.event.SaveEvent;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

import static cn.qingchengfit.staffkit.views.gym.GymActivity.GYM_TO;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/1/27 2016.
 */
public class SetGymFragment extends BaseFragment implements ISetGymView {
    @BindView(R.id.layout_brand) protected LinearLayout layoutBrand;
    protected String imageStr;
    protected int city_code;
    protected double lat;
    protected double lng;
    protected String desStr;
    @BindView(R.id.header) ImageView header;
    @BindView(R.id.gym_name) CommonInputView gymName;
    @BindView(R.id.address) CommonInputView address;
    @BindView(R.id.phone) CommonInputView phone;
    @BindView(R.id.descripe) CommonInputView descripe;
    @BindView(R.id.comfirm) Button comfirm;
    @Inject SetGymPresenter mSetGymPresenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.brand_img) ImageView brandImg;
    @BindView(R.id.brand_name) TextView brandName;
    @BindView(R.id.guide_step_1) ImageView guideStep1;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
    private String addressStr;
    private GymBody gymBody = new GymBody();
    //edit监听 btn状态
    private TextChange textChange = new TextChange();

    public static SetGymFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        SetGymFragment fragment = new SetGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_gym, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(mSetGymPresenter, this);
        initToolbar(toolbar);

        guideStep1.setVisibility(View.GONE);
        mCallbackActivity.setToolbar(getString(R.string.title_setting_gym), false, null, 0, null);

        initViews();
        RxBusAdd(EventAddress.class).subscribe(new Action1<EventAddress>() {
            @Override public void call(EventAddress eventAddress) {
                address.setContent(eventAddress.address);
                addressStr = eventAddress.address;
                city_code = eventAddress.city_code;
                lat = eventAddress.lat;
                lng = eventAddress.log;
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        toolbarLayout.setVisibility(View.GONE);
        super.initToolbar(toolbar);
        toolbarTitile.setText(gymWrapper.brand_name());
    }

    private void initViews() {
        gymName.addTextWatcher(textChange);
        address.addTextWatcher(textChange);
        phone.addTextWatcher(textChange);
        //        brand.addTextChangedListener(textChange);
        try {
            brandName.setText(gymWrapper.brand_name());
            Glide.with(getContext())
                .load(PhotoUtils.getSmall(gymWrapper.getBrand().getPhoto()))
                .asBitmap()
                .placeholder(R.drawable.ic_default_header)
                .into(new CircleImgWrapper(brandImg, getContext()));
        } catch (Exception e) {

        }
    }

    @OnClick(R.id.address) public void onClickAddress() {
        Intent toAddress = new Intent(getContext(), ChooseActivity.class);
        toAddress.putExtra("to", ChooseActivity.CHOOSE_ADDRESS);
        startActivity(toAddress);
    }

    @OnClick(R.id.comfirm) public void onComfirm() {

        if (phone.getContent().length() != 11) {
            ToastUtils.show("请填写正确的手机号码");
            return;
        }
        gymBody.phone = phone.getContent();
        if (TextUtils.isEmpty(gymName.getContent())) {
            ToastUtils.show("请填写健身房名称");
            return;
        }
        gymBody.name = gymName.getContent().trim();
        if (TextUtils.isEmpty(address.getContent())) {
            ToastUtils.show("请填写地址");
            return;
        }
        SystemInitBody body = new SystemInitBody();
        body.brand_id = gymWrapper.brand_id();
        body.shop = new Shop();
        body.shop.address = address.getContent();
        body.shop.description = desStr;
        body.shop.name = gymName.getContent().trim();
        body.shop.phone = phone.getContent();
        body.shop.photo = imageStr;
        body.shop.gd_district_id = city_code + "";
        body.shop.gd_lat = lat;
        body.shop.gd_lng = lng;
        RxBus.getBus().post(new SaveEvent());
        body.auto_trial = false;
        showLoading();
        mSetGymPresenter.initShop(body);
    }

    @OnClick(R.id.descripe) public void onDescripe() {
        WriteDescFragment.start(this, 2, getString(R.string.title_write_gym_descript), "请填写健身房描述");
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                descripe.setContent(getString(R.string.common_have_setting));
                gymBody.description = IntentUtils.getIntentString(data);
                desStr = IntentUtils.getIntentString(data);
            default:
                break;
        }
    }

    @Override public void setBrand() {

    }

    @Override public void setHeader(String args) {
        //设置头像
    }

    @Override public void setAddress(String args) {
        address.setContent(args);
    }

    @Override public void setPhone(String args) {
        phone.setContent(args);
    }

    @Override public void setDesc(String args) {
        descripe.setContent(args);
    }

    @Override public void onBrandList(final List<Brand> brands) {
        //        final List<String> stringList = new ArrayList<>();
        //        for (Brand b : brands) {
        //            stringList.add(b.getName());
        //        }
        //        stringList.add("新增品牌");
        //        final DialogList list = new DialogList(getContext());
        //        list.list(stringList, new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //                list.dismiss();
        //                if (position == stringList.size() - 1) {
        //                    AddBrandFragment.start(SetGymFragment.this, 3, brand.getText().toString());
        //                } else {
        //                    if (position < brands.size()) {
        //                        brand.setText(brands.get(position).getName());
        //                        gymBody.brand_id = brands.get(position).getId();
        //                    }
        //                }
        //            }
        //        });
        //        list.show();

    }

    @Override public void onCreatGym() {
        RxBus.getBus().post(new FreshGymListEvent());
        getActivity().finish();
    }

    @Override public void onSuccess(CoachService coachService) {
        hideLoading();
        if (getActivity() instanceof MainActivity) {
            List<CoachService> coachServices = new ArrayList<>();
            coachServices.add(coachService);
            GymBaseInfoAction.writeGyms(coachServices);
        } else {
            RxBus.getBus().post(new FreshGymListEvent());//通知健身房列表刷新
            RxBus.getBus().post(new RxCompleteGuideEvent());//通知单店模式
            if (getActivity().getIntent().getBooleanExtra("trainer", false)) {
                getActivity().finish();
            } else {
                Intent toGymDetail = new Intent(getActivity(), GymActivity.class);
                toGymDetail.putExtra(Configs.EXTRA_BRAND, new Brand(coachService.brand_id()));
                toGymDetail.putExtra(Configs.EXTRA_GYM_SERVICE, coachService);
                toGymDetail.putExtra(Configs.EXTRA_GYM_STATUS, new GymStatus(false));
                toGymDetail.putExtra(GYM_TO, GymActivity.GYM_DETAIL);
                startActivity(toGymDetail);
                getActivity().finish();
            }
        }
    }

    @Override public void onFailed() {
        hideLoading();
    }

    @Override public void onUpdateGym() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public String getFragmentName() {
        return SetGymFragment.class.getName();
    }

    @OnClick(R.id.layout_gym_img) public void onHeader() {
        ChoosePictureFragmentDialog dialog = new ChoosePictureFragmentDialog();
        dialog.show(getFragmentManager(), "");
        dialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                SetGymFragment.this.RxRegiste(UpYunClient.rxUpLoad("gym/", filePath).subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        Glide.with(getContext()).load(PhotoUtils.getSmall(s)).asBitmap().into(new CircleImgWrapper(header, getContext()));
                        gymBody.image = s;
                        imageStr = s;
                    }
                }));
            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    class TextChange implements TextWatcher {

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override public void afterTextChanged(Editable s) {
            comfirm.setEnabled(!TextUtils.isEmpty(gymName.getContent()) && !TextUtils.isEmpty(address.getContent()) && !TextUtils.isEmpty(
                phone.getContent()));
        }
    }
}
