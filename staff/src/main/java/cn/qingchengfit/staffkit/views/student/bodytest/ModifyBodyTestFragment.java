package cn.qingchengfit.staffkit.views.student.bodytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.body.BodyTestBody;
import cn.qingchengfit.model.responese.BodyTestExtra;
import cn.qingchengfit.model.responese.BodyTestMeasure;
import cn.qingchengfit.model.responese.BodyTestTemplateBase;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.adapter.ImageGridAdapter;
import cn.qingchengfit.staffkit.views.custom.GalleryPhotoViewDialog;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyBodyTestFragment extends BaseFragment implements ModifyBodyTestView {

    @BindView(R.id.test_date) CommonInputView testDate;
    @BindView(R.id.height) CommonInputView height;
    @BindView(R.id.weight) CommonInputView weight;
    @BindView(R.id.bmi) CommonInputView bmi;
    @BindView(R.id.body_fat_rate) CommonInputView bodyFatRate;
    @BindView(R.id.chest) CommonInputView chest;
    @BindView(R.id.waistline) CommonInputView waistline;
    @BindView(R.id.hipline) CommonInputView hipline;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.delete) TextView delete;
    @BindView(R.id.other_data) LinearLayout otherData;
    @BindView(R.id.photos_title) TextView photosTitle;
    @BindView(R.id.left_upper) CommonInputView leftUpper;
    @BindView(R.id.right_upper) CommonInputView rightUpper;
    @BindView(R.id.left_thigh) CommonInputView leftThigh;
    @BindView(R.id.right_thigh) CommonInputView rightThigh;
    @BindView(R.id.left_calf) CommonInputView leftCalf;
    @BindView(R.id.right_calf) CommonInputView rightCalf;

    @Inject ModifyBodyTestPresenter presenter;
    @Inject StudentWrapper studentBean;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    @Inject GymBaseInfoAction gymBaseInfoAction;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;

    private TimeDialogWindow pwTime;
    private View view;
    private ImageGridAdapter imageGridAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<BodyTestBody.Photo> datas = new ArrayList<>();
    private String measureId;
    private MaterialDialog delBatchComfirmDialog;
    private MaterialDialog loadingDialog;
    private String mCurId, mCurModel;

    public ModifyBodyTestFragment() {
    }

    /**
     * @return
     */
    public static ModifyBodyTestFragment newInstance(String measureid) {

        Bundle args = new Bundle();
        args.putString("measureid", measureid);
        ModifyBodyTestFragment fragment = new ModifyBodyTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        measureId = getArguments().getString("measureid");
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Shop shops = (Shop) IntentUtils.getParcelable(data);
                if (shops != null) {
                    CoachService gym = gymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), shops.id);
                    if (gym != null) {
                        presenter.queryBodyModel();
                        mCurId = gym.getId();
                        mCurModel = gym.getModel();
                    }
                }
            }
        } else {
            if (requestCode == 1) {
                getActivity().onBackPressed();
            }
        }
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        if (TextUtils.isEmpty(measureId)) {//添加
            toolbarTitile.setText("添加体测");
            toolbar.inflateMenu(R.menu.menu_save);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    saveInfo();
                    return false;
                }
            });
        } else {
            toolbarTitile.setText("编辑体测");
            toolbar.inflateMenu(R.menu.menu_save);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    saveInfo();
                    return false;
                }
            });
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_modify_body_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter,this);
        initToolbar(toolbar);
        if (TextUtils.isEmpty(measureId)) {//添加
            mCallbackActivity.setToolbar("添加体测", false, null, R.menu.menu_save, new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    saveInfo();
                    return true;
                }
            });
            delete.setVisibility(View.GONE);
            testDate.setContent(DateUtils.Date2YYYYMMDD(new Date()));
            if (gymWrapper.inBrand()) {
                ArrayList<String> list = new ArrayList<>();
                list.addAll(studentBean.getStudentBean().getSupportIdList());
                MutiChooseGymFragment.start(this, true, list, PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE, 1);
            } else {
                mCurId = gymWrapper.id();
                mCurModel = gymWrapper.model();
                showLoading();
                presenter.queryBodyModel();
            }
        } else {

            showLoading();
            presenter.queryInfo(measureId);
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        imageGridAdapter = new ImageGridAdapter(datas);
        imageGridAdapter.setIsEditable(true);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(imageGridAdapter);
        imageGridAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (v.getId() == R.id.delete) {
                    datas.remove(pos);
                    imageGridAdapter.notifyDataSetChanged();
                } else {
                    if (pos < datas.size()) {
                        GalleryPhotoViewDialog galleryPhotoViewDialog = new GalleryPhotoViewDialog(getContext());
                        galleryPhotoViewDialog.setImage(getImages());
                        galleryPhotoViewDialog.setSelected(pos);
                        galleryPhotoViewDialog.show();
                    } else {
                        final ChoosePictureFragmentDialog choosePictureFragmentDialog = new ChoosePictureFragmentDialog();
                        choosePictureFragmentDialog.show(getFragmentManager(), "choose");
                        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
                            @Override public void onChoosePicResult(boolean isSuccess, final String filePath) {
                                choosePictureFragmentDialog.dismiss();
                                if (isSuccess) {
                                    showLoading();
                                    RxRegiste(UpYunClient.rxUpLoad("/course/", filePath)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .onBackpressureBuffer()
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Subscriber<String>() {
                                            @Override public void onCompleted() {

                                            }

                                            @Override public void onError(Throwable e) {
                                                LogUtil.e(e.getMessage());
                                                hideLoading();
                                            }

                                            @Override public void onNext(String upImg) {
                                                hideLoading();
                                                if (TextUtils.isEmpty(upImg)) {
                                                    ToastUtils.showDefaultStyle("图片上传失败");
                                                } else {
                                                    BodyTestBody.Photo photo = new BodyTestBody.Photo();
                                                    photo.photo = upImg;
                                                    datas.add(photo);
                                                    imageGridAdapter.notifyDataSetChanged();
                                                    //                                                        imageGridAdapter.notifyDataSetChanged();

                                                }
                                            }
                                        }));
                                } else {
                                    hideLoading();
                                    ToastUtils.showDefaultStyle("上传图片失败");
                                }
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    @OnClick(R.id.test_date) public void onClickDate() {
        chooseDate();
    }

    public void setDate(String s) {
        testDate.setContent(s);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    public BodyTestMeasure getMeasure() {
        BodyTestMeasure measure = new BodyTestMeasure();
        measure.hipline = hipline.getContent();
        measure.waistline = waistline.getContent();
        measure.circumference_of_left_upper = leftUpper.getContent();
        measure.circumference_of_right_upper = rightUpper.getContent();
        measure.circumference_of_chest = chest.getContent();
        measure.circumference_of_left_calf = leftCalf.getContent();
        measure.circumference_of_right_calf = rightCalf.getContent();
        measure.circumference_of_left_thigh = leftThigh.getContent();
        measure.circumference_of_right_thigh = rightThigh.getContent();
        measure.bmi = bmi.getContent();
        measure.created_at = testDate.getContent();
        measure.body_fat_rate = bodyFatRate.getContent();
        measure.weight = weight.getContent();
        measure.height = height.getContent();

        return measure;
    }

    public void chooseDate() {
      if (pwTime == null) {
        pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
      }
        pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
            Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
        pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
                testDate.setContent(DateUtils.Date2YYYYMMDD(date));
                pwTime.dismiss();
            }
        });
        pwTime.showAtLocation(view, Gravity.BOTTOM, 0, 0, new Date());
    }

    public void saveInfo() {
        BodyTestMeasure mMeasure = getMeasure();
        BodyTestBody addBodyTestBean = new BodyTestBody();
        addBodyTestBean.model = mCurModel;
        addBodyTestBean.id = mCurId;
        addBodyTestBean.created_at = testDate.getContent() + "T00:00:00";
        if (!TextUtils.isEmpty(mMeasure.bmi)) {
            addBodyTestBean.bmi = mMeasure.bmi;
        }
        if (!TextUtils.isEmpty(mMeasure.weight)) {
            addBodyTestBean.weight = mMeasure.weight;
        }
        if (!TextUtils.isEmpty(mMeasure.height)) {
            addBodyTestBean.height = mMeasure.height;
        }
        if (!TextUtils.isEmpty(mMeasure.body_fat_rate)) {
            addBodyTestBean.body_fat_rate = mMeasure.body_fat_rate;
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_left_calf)) {
            addBodyTestBean.circumference_of_left_calf = mMeasure.circumference_of_left_calf;
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_right_calf)) {
            addBodyTestBean.circumference_of_right_calf = mMeasure.circumference_of_right_calf;
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_chest)) {
            addBodyTestBean.circumference_of_chest = mMeasure.circumference_of_chest;
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_left_thigh)) {
            addBodyTestBean.circumference_of_left_thigh = mMeasure.circumference_of_left_thigh;
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_right_thigh)) {
            addBodyTestBean.circumference_of_right_thigh = mMeasure.circumference_of_right_thigh;
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_left_upper)) {
            addBodyTestBean.circumference_of_left_upper = mMeasure.circumference_of_left_upper;
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_right_upper)) {
            addBodyTestBean.circumference_of_right_upper = mMeasure.circumference_of_right_upper;
        }
        if (!TextUtils.isEmpty(mMeasure.hipline)) {
            addBodyTestBean.hipline = mMeasure.hipline;
        }
        if (!TextUtils.isEmpty(mMeasure.waistline)) {
            addBodyTestBean.waistline = mMeasure.waistline;
        }
        //        if (!mModel.equalsIgnoreCase("service")) {
        List<BodyTestExtra> extras = new ArrayList<>();
        for (int i = 0; i < otherData.getChildCount(); i++) {
            CommonInputView v = (CommonInputView) otherData.getChildAt(i);
            BodyTestExtra extra = new BodyTestExtra();
            extra.id = (String) v.getTag(R.id.tag_1);
            extra.unit = (String) v.getTag(R.id.tag_2);
            extra.name = (String) v.getTag(R.id.tag_0);
            extra.value = v.getContent();
            extras.add(extra);
        }
        addBodyTestBean.extra = new Gson().toJson(extras);
        addBodyTestBean.photos = datas;
        addBodyTestBean.user_id = studentBean.id();

        if (!TextUtils.isEmpty(measureId)) {//修改
            if (gymWrapper.inBrand()) {
                new MaterialDialog.Builder(getContext()).content("请到场馆中修改会员体测数据")
                    .autoDismiss(true)
                    .positiveText(R.string.common_comfirm)
                    .show();
            } else {
                presenter.modifyBodyTest(measureId, gymWrapper.id(), gymWrapper.model(), addBodyTestBean);
            }
        } else { //添加
            showLoading();
            presenter.addBodyTest(addBodyTestBean, mCurId, mCurModel);
        }
    }

    @OnClick(R.id.delete) public void OnDel() {
        if (serPermisAction.checkMuti(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE, studentBean.getStudentBean().getSupportIdList())) {

            delBatchComfirmDialog = new MaterialDialog.Builder(getActivity()).autoDismiss(true)
                .content("是否删除此条体测信息?")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        materialDialog.dismiss();
                        showLoading();
                        presenter.delBodyTest(measureId);
                    }
                })
                .build();
            delBatchComfirmDialog.show();
        } else {
            showAlert(getString(R.string.alert_permission_forbid));
        }
    }

    public List<String> getImages() {
        List<String> images = new ArrayList<>();
        for (BodyTestBody.Photo p : datas) {
            images.add(p.photo);
        }
        return images;
    }

    @Override public void onShowBase(BodyTestTemplateBase mBase) {
        hideLoading();
        bmi.setVisibility(mBase.show_bmi ? View.VISIBLE : View.GONE);
        bodyFatRate.setVisibility(mBase.show_body_fat_rate ? View.VISIBLE : View.GONE);
        leftCalf.setVisibility(mBase.show_circumference_of_left_calf ? View.VISIBLE : View.GONE);
        rightCalf.setVisibility(mBase.show_circumference_of_right_calf ? View.VISIBLE : View.GONE);
        chest.setVisibility(mBase.show_circumference_of_chest ? View.VISIBLE : View.GONE);
        leftThigh.setVisibility(mBase.show_circumference_of_left_thigh ? View.VISIBLE : View.GONE);
        rightThigh.setVisibility(mBase.show_circumference_of_right_thigh ? View.VISIBLE : View.GONE);
        leftUpper.setVisibility(mBase.show_circumference_of_left_upper ? View.VISIBLE : View.GONE);
        rightUpper.setVisibility(mBase.show_circumference_of_right_upper ? View.VISIBLE : View.GONE);
        height.setVisibility(mBase.show_height ? View.VISIBLE : View.GONE);
        hipline.setVisibility(mBase.show_hipline ? View.VISIBLE : View.GONE);
        waistline.setVisibility(mBase.show_waistline ? View.VISIBLE : View.GONE);
        weight.setVisibility(mBase.show_weight ? View.VISIBLE : View.GONE);
    }

    @Override public void onShowMeasure(BodyTestMeasure mMeasure) {
        hideLoading();
        testDate.setContent(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(mMeasure.created_at)));

        if (!TextUtils.isEmpty(mMeasure.bmi)) {
            bmi.setVisibility(View.VISIBLE);
            bmi.setContent(String.format("%s", mMeasure.bmi));
        }
        if (!TextUtils.isEmpty(mMeasure.weight)) {
            weight.setVisibility(View.VISIBLE);
            weight.setContent(String.format("%s", mMeasure.weight));
        }
        if (!TextUtils.isEmpty(mMeasure.height)) {
            height.setVisibility(View.VISIBLE);
            height.setContent(String.format("%s", mMeasure.height));
        }
        if (!TextUtils.isEmpty(mMeasure.body_fat_rate)) {
            bodyFatRate.setVisibility(View.VISIBLE);
            bodyFatRate.setContent(String.format("%s", mMeasure.body_fat_rate));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_left_calf)) {
            leftCalf.setVisibility(View.VISIBLE);
            leftCalf.setContent(String.format("%s", mMeasure.circumference_of_left_calf));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_right_calf)) {
            rightCalf.setVisibility(View.VISIBLE);
            rightCalf.setContent(String.format("%s", mMeasure.circumference_of_right_calf));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_chest)) {
            chest.setVisibility(View.VISIBLE);
            chest.setContent(String.format("%s", mMeasure.circumference_of_chest));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_right_thigh)) {
            rightThigh.setVisibility(View.VISIBLE);
            rightThigh.setContent(String.format("%s", mMeasure.circumference_of_right_thigh));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_left_thigh)) {
            leftThigh.setVisibility(View.VISIBLE);
            leftThigh.setContent(String.format("%s", mMeasure.circumference_of_left_thigh));
        }

        if (!TextUtils.isEmpty(mMeasure.circumference_of_left_upper)) {
            leftUpper.setVisibility(View.VISIBLE);
            leftUpper.setContent(String.format("%s", mMeasure.circumference_of_left_upper));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_right_upper)) {
            rightUpper.setVisibility(View.VISIBLE);
            rightUpper.setContent(String.format("%s", mMeasure.circumference_of_right_upper));
        }
        if (!TextUtils.isEmpty(mMeasure.hipline)) {
            hipline.setVisibility(View.VISIBLE);
            hipline.setContent(String.format("%s", mMeasure.hipline));
        }
        if (!TextUtils.isEmpty(mMeasure.waistline)) {
            waistline.setVisibility(View.VISIBLE);
            waistline.setContent(String.format("%s", mMeasure.waistline));
        }
    }

    @Override public void onShowExtra(boolean show, List<BodyTestBody.Photo> photos, List<BodyTestExtra> extras) {
        if (show) {
            imageGridAdapter.setIsEditable(true);
            photosTitle.setVisibility(View.VISIBLE);
            if (extras != null) {
                for (BodyTestExtra extra : extras) {
                    CommonInputView commonInputView = new CommonInputView(getContext());

                    commonInputView.setTag(R.id.tag_0, extra.name);
                    commonInputView.setTag(R.id.tag_1, extra.id);
                    commonInputView.setTag(R.id.tag_2, extra.unit);
                    commonInputView.setMaxLines(100);
                    otherData.addView(commonInputView);
                    commonInputView.setContentColor(
                        ContextCompat.getColor(getContext(), R.color.text_dark));
                    commonInputView.setLabel(extra.name + "(" + extra.unit + ")");
                    commonInputView.setContent(extra.value);
                }
            }
            if (photos != null) {
                datas.clear();
                datas.addAll(photos);
                imageGridAdapter.notifyDataSetChanged();
            }
        } else {
            datas.clear();
            imageGridAdapter.setIsEditable(false);
            imageGridAdapter.notifyDataSetChanged();
            photosTitle.setVisibility(View.GONE);
        }
    }

    @Override public void onFailed(String s) {
        hideLoading();
        ToastUtils.show(s);
    }

    @Override public void onSuccess() {
        hideLoading();
        ToastUtils.showS("操作成功");
        getActivity().onBackPressed();
    }

    @Override public String getFragmentName() {
        return ModifyBodyTestFragment.class.getName();
    }
}
