//package cn.qingchengfit.saasbase.coach.views;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.text.style.ForegroundColorSpan;
//import android.text.style.ImageSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.qingchengfit.RxBus;
//import cn.qingchengfit.di.model.GymWrapper;
//import cn.qingchengfit.di.model.LoginStatus;
//import cn.qingchengfit.model.base.Staff;
//import cn.qingchengfit.network.QcRestRepository;
//import cn.qingchengfit.saasbase.R;
//import cn.qingchengfit.saasbase.R2;
//import cn.qingchengfit.saasbase.coach.event.LoadingEvent;
//import cn.qingchengfit.saasbase.coach.presenter.AddNewCoachPresenter;
//import cn.qingchengfit.saasbase.constant.Configs;
//import cn.qingchengfit.saasbase.utils.IntentUtils;
//import cn.qingchengfit.utils.CircleImgWrapper;
//import cn.qingchengfit.utils.CompatUtils;
//import cn.qingchengfit.utils.MeasureUtils;
//import cn.qingchengfit.utils.PhotoUtils;
//import cn.qingchengfit.utils.ToastUtils;
//import cn.qingchengfit.views.activity.WebActivity;
//import cn.qingchengfit.views.fragments.BaseDialogFragment;
//import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
//import cn.qingchengfit.widgets.CommonInputView;
//import cn.qingchengfit.widgets.PhoneEditText;
//import com.bumptech.glide.Glide;
//import dagger.android.support.AndroidSupportInjection;
//import javax.inject.Inject;
//
///**
// * power by
// * <p/>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p/>
// * <p/>
// * Created by Paper on 16/1/29 2016.
// */
////@SuppressWarnings("unused")
//public class AddNewCoachFragment extends BaseDialogFragment implements
//    AddNewCoachPresenter.MVPView {
//
//    @BindView(R2.id.toolbar) Toolbar toolbar;
//    @BindView(R2.id.toolbar_title) TextView toolbarTitile;
//    @BindView(R2.id.header_img) ImageView headerImg;
//    @BindView(R2.id.down_trainer_app) TextView downTrainerApp;
//    @BindView(R2.id.name) CommonInputView name;
//    @BindView(R2.id.course_type_rg) RadioGroup courseTypeRg;
//    @BindView(R2.id.btn) Button btn;
//    @BindView(R2.id.phone_num) PhoneEditText phoneNum;
//    @Inject QcRestRepository restRepository;
//    @Inject LoginStatus loginStatus;
//    @Inject GymWrapper gymWrapper;
//    @Inject AddNewCoachPresenter presenter;
//    private String uploadImageUrl = "";
//    private String staffId;     //TODO 传staffId
//
//    private TextChange textChange = new TextChange();
//
//    public static void start(Fragment fragment, int requestCode, String staffId) {
//        BaseDialogFragment f = newInstance(staffId);
//        f.setTargetFragment(fragment, requestCode);
//        f.show(fragment.getFragmentManager(), "");
//    }
//
//    public static AddNewCoachFragment newInstance() {
//        AddNewCoachFragment fragment = new AddNewCoachFragment();
//
//        return fragment;
//    }
//
//    public static AddNewCoachFragment newInstance(String staffId) {
//        Bundle args = new Bundle();
//        args.putString("staffId", staffId);
//        AddNewCoachFragment fragment = new AddNewCoachFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public static AddNewCoachFragment newInstance(String id, String model) {
//
//        Bundle args = new Bundle();
//        args.putString("id", id);
//        args.putString("model", model);
//        AddNewCoachFragment fragment = new AddNewCoachFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
//        if (getArguments() != null){
//            staffId = getArguments().getString("staffId");
//        }
//    }
//
//    @Override public void onAttach(Context context) {
//        AndroidSupportInjection.inject(this);
//        super.onAttach(context);
//    }
//
//    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_add_new_coach_saas, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        delegatePresenter(presenter, this);
//        name.addTextWatcher(textChange);
//        phoneNum.addTextChangedListener(textChange);
//        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                AddNewCoachFragment.this.dismiss();
//            }
//        });
//        toolbarTitile.setText(getString(R.string.course_add_new_coach));
//        Glide.with(getActivity()).load(Configs.HEADER_COACH_MALE).asBitmap().into(new CircleImgWrapper(headerImg, getContext()));
//        courseTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.gender_male) {
//                    Glide.with(getActivity())
//                        .load(Configs.HEADER_COACH_MALE)
//                        .asBitmap()
//                        .into(new CircleImgWrapper(headerImg, getContext()));
//                } else {
//                    Glide.with(getActivity())
//                        .load(Configs.HEADER_COACH_FEMALE)
//                        .asBitmap()
//                        .into(new CircleImgWrapper(headerImg, getContext()));
//                }
//            }
//        });
//        SpannableString ss = new SpannableString(getString(R.string.down_trainer_app_hint));
//        Drawable d;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//          d = getResources().getDrawable(R.drawable.ic_laucher_trainer, null);
//        } else {
//          d = getResources().getDrawable(R.drawable.ic_laucher_trainer);
//        }
//        if (d != null) d.setBounds(0, 0, MeasureUtils.dpToPx(15f, getResources()), MeasureUtils.dpToPx(15f, getResources()));
//        ss.setSpan(new ImageSpan(d, ImageSpan.ALIGN_BASELINE), 18, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(CompatUtils.getColor(getContext(), R.color.text_orange)), 20, 29,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色
//
//        downTrainerApp.setText(ss, TextView.BufferType.SPANNABLE);
//
//        return view;
//    }
//
//    @OnClick(R2.id.gender_layout) public void changeGender() {
//        courseTypeRg.check(courseTypeRg.getCheckedRadioButtonId() == R.id.gender_male ? R.id.gender_female : R.id.gender_male);
//    }
//
//    @OnClick(R2.id.btn) public void onComfirm() {
//        if (TextUtils.isEmpty(name.getContent())) {
//            ToastUtils.show("请填写教练姓名");
//            return;
//        }
//        if (!phoneNum.checkPhoneNum()) return;
//
//        showLoadingTrans();
//        presenter.confirmAdd(staffId, name.getContent(), phoneNum.getPhoneNum(), uploadImageUrl,
//            courseTypeRg.getCheckedRadioButtonId() == R.id.gender_male ? 0 : 1, phoneNum.getDistrictInt());
//        //}
//
//    }
//
//    @OnClick(R2.id.header_layout) public void onHeaderClick() {
//        ChoosePictureFragmentNewDialog choosePictureFragmentDialog = ChoosePictureFragmentNewDialog.newInstance();
//        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
//            @Override public void onChoosefile(String filePath) {
//
//            }
//
//            @Override public void onUploadComplete(String filePaht, String url) {
//                PhotoUtils.smallCircle(headerImg,url);
//                onUpLoadSucc(url);
//            }
//        });
//        choosePictureFragmentDialog.show(getFragmentManager(), "");
//    }
//
//    @OnClick(R2.id.down_trainer_app) public void onClick() {
//        WebActivity.startWebForResult(Configs.DOWNLOAD_TRAINER, getContext(), 444);
//    }
//
//    @Override public void onDestroyView() {
//        super.onDestroyView();
//    }
//
//    @Override public void onUpLoadSucc(String upImg) {
//        if (TextUtils.isEmpty(upImg)) {
//            ToastUtils.showDefaultStyle("图片上传失败");
//        } else {
//            uploadImageUrl = upImg;
//        }
//        RxBus.getBus().post(new LoadingEvent(false));
//    }
//
//    @Override public void onConfirm(Staff teacher) {
//        getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK,
//            IntentUtils.instanceStringsIntent(teacher.getUsername(), teacher.getId(), teacher.getAvatar()));
//        ToastUtils.show(getString(R.string.add_success));
//        hideLoadingTrans();
//        dismiss();
//    }
//
//    @Override public void onFailed() {
//        hideLoadingTrans();
//    }
//
//    @Override
//    public void popBack(int count) {
//
//    }
//
//    class TextChange implements TextWatcher {
//
//        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override public void afterTextChanged(Editable s) {
//            btn.setEnabled((!TextUtils.isEmpty(name.getContent())) && !TextUtils.isEmpty(phoneNum.getPhoneNum()));
//        }
//    }
//}
