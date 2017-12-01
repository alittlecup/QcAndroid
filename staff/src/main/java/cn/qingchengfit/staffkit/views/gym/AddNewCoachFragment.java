package cn.qingchengfit.staffkit.views.gym;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.CoachResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.LoadingEvent;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.custom.PhoneEditText;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import dagger.android.support.AndroidSupportInjection;
import java.io.File;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/1/29 2016.
 */
//@SuppressWarnings("unused")
public class AddNewCoachFragment extends BaseDialogFragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.header_img) ImageView headerImg;
    @BindView(R.id.down_trainer_app) TextView downTrainerApp;
    @BindView(R.id.name) CommonInputView name;
    //    @BindView(R.id.gender_male)
    //    RadioButton genderMale;
    //    @BindView(R.id.gender_female)
    //    RadioButton genderFemale;
    @BindView(R.id.course_type_rg) RadioGroup courseTypeRg;
    //    @BindView(R.id.gender_layout)
    //    RelativeLayout genderLayout;
    @BindView(R.id.btn) Button btn;
    @BindView(R.id.phone_num) PhoneEditText phoneNum;
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private String uploadImageUrl = "";

    //    @Bind(R.id.name)
    //    CommonInputView name;
    //    @Bind(R.id.course_type_rg)
    //    RadioGroup courseTypeRg;
    //    //    @Bind(R.id.gender_layout)
    ////    RelativeLayout genderLayout;
    //    @Bind(R.id.phone)
    //    CommonInputView phone;
    //    @Bind(R.id.btn)
    //    Button btn;
    private TextChange textChange = new TextChange();

    public static void start(Fragment fragment, int requestCode) {
        BaseDialogFragment f = newInstance();
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static AddNewCoachFragment newInstance() {
        AddNewCoachFragment fragment = new AddNewCoachFragment();

        return fragment;
    }

    public static AddNewCoachFragment newInstance(String id, String model) {

        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("model", model);
        AddNewCoachFragment fragment = new AddNewCoachFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Override public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_coach, container, false);
        unbinder = ButterKnife.bind(this, view);
        name.addTextWatcher(textChange);
        phoneNum.addTextChangedListener(textChange);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AddNewCoachFragment.this.dismiss();
            }
        });
        toolbarTitile.setText(getString(R.string.course_add_new_coach));
        Glide.with(getActivity()).load(Configs.HEADER_COACH_MALE).asBitmap().into(new CircleImgWrapper(headerImg, getContext()));
        courseTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.gender_male) {
                    Glide.with(getActivity())
                        .load(Configs.HEADER_COACH_MALE)
                        .asBitmap()
                        .into(new CircleImgWrapper(headerImg, getContext()));
                } else {
                    Glide.with(getActivity())
                        .load(Configs.HEADER_COACH_FEMALE)
                        .asBitmap()
                        .into(new CircleImgWrapper(headerImg, getContext()));
                }
            }
        });
        SpannableString ss = new SpannableString(getString(R.string.down_trainer_app_hint));
        Drawable d;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          d = getResources().getDrawable(R.drawable.ic_laucher_trainer, null);
        } else {
          d = getResources().getDrawable(R.drawable.ic_laucher_trainer);
        }
        if (d != null) d.setBounds(0, 0, MeasureUtils.dpToPx(15f, getResources()), MeasureUtils.dpToPx(15f, getResources()));
        ss.setSpan(new ImageSpan(d, ImageSpan.ALIGN_BASELINE), 18, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(CompatUtils.getColor(getContext(), R.color.text_orange)), 20, 29,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色

        downTrainerApp.setText(ss, TextView.BufferType.SPANNABLE);

        return view;
    }

    @OnClick(R.id.gender_layout) public void changeGender() {
        courseTypeRg.check(courseTypeRg.getCheckedRadioButtonId() == R.id.gender_male ? R.id.gender_female : R.id.gender_male);
    }

    @OnClick(R.id.btn) public void onComfirm() {
        if (TextUtils.isEmpty(name.getContent())) {
            ToastUtils.show("请填写教练姓名");
            return;
        }
        if (!phoneNum.checkPhoneNum()) return;

        //if (TextUtils.isEmpty(mId) || TextUtils.isEmpty(mModel)) {
        //    SystemInitBody body = (SystemInitBody) App.caches.get("init");
        //    if (body != null) {
        //        body.teachers.add(new Staff(name.getContent(),phoneNum.getPhoneNum(),uploadImageUrl  ,phoneNum.getDistrictInt(), courseTypeRg.getCheckedRadioButtonId() == R.id.gender_male ? 0:1));
        //
        //        getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK, IntentUtils.instanceStringIntent(name.getContent(), phoneNum.getPhoneNum()));
        //        this.dismiss();
        //    } else {
        //
        //    }
        //} else {
        Staff coach = new Staff(name.getContent(), phoneNum.getPhoneNum(), uploadImageUrl,
            courseTypeRg.getCheckedRadioButtonId() == R.id.gender_male ? 0 : 1);
        coach.area_code = phoneNum.getDistrictInt();
        showLoading();
        restRepository.getPost_api()
            .qcAddCoach(App.staffId, gymWrapper.id(), gymWrapper.model(), coach)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<CoachResponse>>() {
                @Override public void call(QcDataResponse<CoachResponse> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK,
                            IntentUtils.instanceStringsIntent(qcResponse.data.teacher.getUsername(), qcResponse.data.teacher.getId(),
                                qcResponse.data.teacher.getAvatar()));
                        ToastUtils.show(getString(R.string.add_success));
                        hideLoading();
                        dismiss();
                    } else {
                        // ToastUtils.logHttp(qcResponse);
                        hideLoading();
                    }
                }
            });

        //}

    }

    @OnClick(R.id.header_layout) public void onHeaderClick() {
        ChoosePictureFragmentDialog choosePictureFragmentDialog = ChoosePictureFragmentDialog.newInstance();
        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
            @Override public void onChoosePicResult(boolean isSuccess, final String filePath) {
                if (isSuccess) {
                    Observable.create(new Observable.OnSubscribe<String>() {
                        @Override public void call(Subscriber<? super String> subscriber) {
                            String upImg = UpYunClient.upLoadImg("/header/", new File(filePath));
                            subscriber.onNext(upImg);
                        }
                    })
                        .observeOn(AndroidSchedulers.mainThread())
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<String>() {
                        @Override public void onCompleted() {

                        }

                        @Override public void onError(Throwable e) {
                            RxBus.getBus().post(new LoadingEvent(false));
                        }

                        @Override public void onNext(String upImg) {
                            if (TextUtils.isEmpty(upImg)) {
                                ToastUtils.showDefaultStyle("图片上传失败");
                            } else {
                                Glide.with(getContext()).load(PhotoUtils.getSmall(upImg)).into(headerImg);
                                uploadImageUrl = upImg;
                            }
                            RxBus.getBus().post(new LoadingEvent(false));
                        }
                    });
                } else {
                    ToastUtils.showDefaultStyle("图片上传失败");
                }
            }
        });
        choosePictureFragmentDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.down_trainer_app) public void onClick() {
        WebActivity.startWebForResult(Configs.DOWNLOAD_TRAINER, getContext(), 444);
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
            btn.setEnabled((!TextUtils.isEmpty(name.getContent())) && !TextUtils.isEmpty(phoneNum.getPhoneNum()));
        }
    }
}
