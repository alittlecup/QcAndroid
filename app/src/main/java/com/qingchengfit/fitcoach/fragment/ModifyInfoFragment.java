package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.CompatUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CitiesChooser;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.ModifyCoachInfo;
import com.qingchengfit.fitcoach.http.bean.QcCoachRespone;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.qingchengfit.fitcoach.service.UpyunService;
import java.io.File;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * create an instance of this fragment.
 */
public class ModifyInfoFragment extends BaseSettingFragment implements ChoosePictureFragmentDialog.ChoosePicResult {
    public static final String TAG = ModifyInfoFragment.class.getName();
    public static int SELECT_PIC_KITKAT = 10;
    public static int SELECT_PIC = 11;
    public static int SELECT_CAM = 12;
    private static String FILE_PATH = Configs.ExternalPath + "header.png";

    @BindView(R.id.modifyinfo_header_pic) ImageView modifyinfoHeaderPic;
    @BindView(R.id.comple_gender) RadioGroup compleGender;
    Gson gson = new Gson();
    @BindView(R.id.mofifyinfo_name) CommonInputView mofifyinfoName;
    @BindView(R.id.comple_gender_label) TextView compleGenderLabel;
    @BindView(R.id.comple_gender_male) RadioButton compleGenderMale;
    @BindView(R.id.comple_gender_female) RadioButton compleGenderFemale;
    @BindView(R.id.mofifyinfo_city) CommonInputView mofifyinfoCity;
    @BindView(R.id.mofifyinfo_wechat) CommonInputView mofifyinfoWechat;
    @BindView(R.id.mofifyinfo_weibo) CommonInputView mofifyinfoWeibo;
    @BindView(R.id.modifyinfo_label) TextView modifyinfoLabel;
    @BindView(R.id.modifyinfo_sign_et) EditText modifyinfoSignEt;
    @BindView(R.id.modifyinfo_right_arrow) ImageView modifyinfoRightArrow;
    @BindView(R.id.modifyinfo_brief) RelativeLayout modifyinfoBrief;
    @BindView(R.id.modifyinfo_name) EditText modifyinfoName;
    @BindView(R.id.modifyinfo_desc) EditText modifyinfoDesc;
    @BindView(R.id.modifyinfo_inputpan) LinearLayout modifyinfoInputpan;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;


    private QcCoachRespone.DataEntity.CoachEntity user;
    private ModifyCoachInfo mModifyCoachInfo;

    private Coach coach;
    private CitiesChooser citiesChooser;
    private Unbinder unbinder;

    public ModifyInfoFragment() {
    }

    public static ModifyInfoFragment newInstance(String param1, String param2) {
        ModifyInfoFragment fragment = new ModifyInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private Observable<UpyunService.UpYunResult> uppicObserver;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        citiesChooser = new CitiesChooser(getContext());
        mModifyCoachInfo = new ModifyCoachInfo();
        uppicObserver = RxBus.getBus().register(UpyunService.UpYunResult.class.getName());
        uppicObserver.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UpyunService.UpYunResult>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(UpyunService.UpYunResult upYunResult) {
                mModifyCoachInfo.setAvatar(upYunResult.getUrl());
                user.setAvatar(upYunResult.getUrl());
            }
        });
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, 0, "基本信息设置");
        String coachStr = PreferenceUtils.getPrefString(getContext(), "coach", "");
        coach = gson.fromJson(coachStr, Coach.class);
        refresh.setColorSchemeResources(R.color.primary);
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(refresh.getViewTreeObserver(),this);
                refresh.setRefreshing(true);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                queryData();
            }
        });
        queryData();
        return view;
    }

    public void queryData() {

        QcCloudClient.getApi().getApi.qcGetCoach(Integer.parseInt(coach.id))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<QcCoachRespone>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {
                    refresh.setRefreshing(false);
                }

                @Override public void onNext(QcCoachRespone qcCoachRespone) {
                    if (modifyinfoDesc != null) {
                        user = qcCoachRespone.getData().getCoach();
                        initInfo();
                        refresh.setRefreshing(false);
                    }
                }
            });
    }

    /**
     * 初始化个人信息
     */
    private void initInfo() {
        initHead(user.getAvatar());
        if (user.getDistrict() != null && user.getDistrict().province != null) {
            mModifyCoachInfo.setDistrict_id(user.getDistrict().id);
        }
        mofifyinfoCity.setContent(user.getDistrictStr());
        mofifyinfoName.setContent(user.getUsername());
        mofifyinfoWechat.setContent(user.getWeixin());
        modifyinfoSignEt.setText(user.getShort_description());
        mModifyCoachInfo.setGender(user.getGender());
        if (user.getGender() == 0) {
            compleGender.check(R.id.comple_gender_male);
        } else {
            compleGender.check(R.id.comple_gender_female);
        }
        compleGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.comple_gender_male) {
                mModifyCoachInfo.setGender(0);
            } else {
                mModifyCoachInfo.setGender(1);
            }
        });
    }

    public void initHead(String headurl) {
        int gender = R.drawable.img_default_female;
        if (user.getGender() == 0) gender = R.drawable.img_default_male;
        if (TextUtils.isEmpty(headurl)) {
            Glide.with(App.AppContex).load(gender).asBitmap().into(new CircleImgWrapper(modifyinfoHeaderPic, App.AppContex));
        } else {
            Glide.with(App.AppContex)
                .load(PhotoUtils.getSmall(headurl))
                .asBitmap()
                .into(new CircleImgWrapper(modifyinfoHeaderPic, App.AppContex));
        }
    }

    /**
     * 修改城市
     */
    @OnClick(R.id.mofifyinfo_city) public void onClickCity() {
        citiesChooser.setOnCityChoosenListener((provice, city, district, id) -> {
            mofifyinfoCity.setContent(provice + city);
            mModifyCoachInfo.setDistrict_id(Integer.toString(id));
            citiesChooser.hide();
        });
        citiesChooser.show(modifyinfoBrief);
    }

    @OnClick(R.id.modifyinfo_sign_layout) public void onClickSign() {
        modifyinfoSignEt.requestFocus();
    }

    @OnClick(R.id.modifyinfo_header_layout) public void onChangeHeader() {
        ChoosePictureFragmentDialog choosePictureFragmentDialog = new ChoosePictureFragmentDialog();
        choosePictureFragmentDialog.setResult(this);
        choosePictureFragmentDialog.show(getFragmentManager(), "choose pic");
    }


    /**
     * 修改简介
     */
    @OnClick(R.id.modifyinfo_brief) public void onClickBrief() {
        Fragment fragment = ModifyBrifeFragment.newInstance(user.getDescription());
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.settting_fraglayout, fragment)
            .show(fragment)
            .addToBackStack("")
            .commit();
    }

    /**
     * 确认修改
     */
    @OnClick(R.id.modifyinfo_comfirm) public void onComfirm() {

        mModifyCoachInfo.setWeixin(mofifyinfoWechat.getContent());
        mModifyCoachInfo.setShort_description(modifyinfoSignEt.getText().toString());
        mModifyCoachInfo.setUsername(mofifyinfoName.getContent().trim());
        fragmentCallBack.ShowLoading("请稍后");

        QcCloudClient.getApi().postApi.qcModifyCoach(Integer.parseInt(coach.id), mModifyCoachInfo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<QcResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {
                    Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                    fragmentCallBack.hideLoading();
                }

                @Override public void onNext(QcResponse qcResponse) {
                    fragmentCallBack.hideLoading();
                    if (qcResponse.status == ResponseResult.SUCCESS) {
                        fragmentCallBack.fixCount();

                        //                            Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                        ToastUtils.show("修改成功");
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getContext(), qcResponse.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }


    @Override public void onDestroyView() {
        RxBus.getBus().unregister(UpyunService.UpYunResult.class.getName(), uppicObserver);
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
        if (isSuccess) {
            fragmentCallBack.ShowLoading("正在上传");
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override public void call(Subscriber<? super String> subscriber) {
                    String upImg = UpYunClient.upLoadImg("course/", new File(filePath));
                    subscriber.onNext(upImg);
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<String>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {
                    fragmentCallBack.hideLoading();
                }

                @Override public void onNext(String upImg) {
                    upImg = upImg + "!120x120";
                    if (TextUtils.isEmpty(upImg)) {
                        ToastUtils.showDefaultStyle("图片上传失败");
                    } else {
                        mModifyCoachInfo.setAvatar(upImg);
                        Glide.with(App.AppContex)
                            .load(PhotoUtils.getSmall(upImg))
                            .asBitmap()
                            .into(new CircleImgWrapper(modifyinfoHeaderPic, App.AppContex));
                    }
                    fragmentCallBack.hideLoading();
                }
            });
        } else {
            LogUtil.e("选择图片失败");
        }
    }
}
