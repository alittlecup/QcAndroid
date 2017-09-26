package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.ImageGridAdapter;
import com.qingchengfit.fitcoach.component.FullyGridLayoutManager;
import com.qingchengfit.fitcoach.component.GalleryPhotoViewDialog;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.AddBodyTestBean;
import com.qingchengfit.fitcoach.http.bean.Measure;
import com.qingchengfit.fitcoach.http.bean.QcBodyTestTemplateRespone;
import com.qingchengfit.fitcoach.http.bean.QcGetBodyTestResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyBodyTestFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.delete) TextView delete;
    @BindView(R.id.other_data) LinearLayout otherData;
    @BindView(R.id.photos_title) TextView photosTitle;
    @BindView(R.id.test_date) CommonInputView testDate;
    @BindView(R.id.height) CommonInputView height;
    @BindView(R.id.weight) CommonInputView weight;
    @BindView(R.id.bmi) CommonInputView bmi;
    @BindView(R.id.body_fat_rate) CommonInputView bodyFatRate;
    @BindView(R.id.left_upper) CommonInputView leftUpper;
    @BindView(R.id.right_upper) CommonInputView rightUpper;
    @BindView(R.id.chest) CommonInputView chest;
    @BindView(R.id.waistline) CommonInputView waistline;
    @BindView(R.id.hipline) CommonInputView hipline;
    @BindView(R.id.left_thigh) CommonInputView leftThigh;
    @BindView(R.id.right_thigh) CommonInputView rightThigh;
    @BindView(R.id.left_calf) CommonInputView leftCalf;
    @BindView(R.id.right_calf) CommonInputView rightCalf;
    private TimeDialogWindow pwTime;
    private View view;
    private ImageGridAdapter imageGridAdapter;
    private FullyGridLayoutManager gridLayoutManager;
    private List<AddBodyTestBean.Photo> datas = new ArrayList<>();

    private String mModel;
    private String mModelId;
    private String measureId;
    private String mStudentId;
    private MaterialDialog delBatchComfirmDialog;
    private MaterialDialog loadingDialog;
    private Unbinder unbinder;
    private Subscription spUpImg;

    public ModifyBodyTestFragment() {
    }

    public static ModifyBodyTestFragment newInstance(String measureid, String model, String modelid, String studentid) {

        Bundle args = new Bundle();
        args.putString("measureid", measureid);
        args.putString("model", model);
        args.putString("modelid", modelid);
        args.putString("studentid", studentid);
        ModifyBodyTestFragment fragment = new ModifyBodyTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        measureId = getArguments().getString("measureid");
        mModel = getArguments().getString("model");
        mModelId = getArguments().getString("modelid");
        mStudentId = getArguments().getString("studentid");
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_modify_body_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                saveInfo();
                return true;
            }
        });
        if (TextUtils.isEmpty(measureId)) {//添加
            toolbar.setTitle("添加体测");
            delete.setVisibility(View.GONE);
            testDate.setContent(DateUtils.Date2YYYYMMDD(new Date()));
            addTest();
        } else {
            toolbar.setTitle("编辑体测");
            getInfo();
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        imageGridAdapter = new ImageGridAdapter(datas);
        imageGridAdapter.setIsEditable(true);
        gridLayoutManager = new FullyGridLayoutManager(getContext(), 3);
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
                        ChoosePictureFragmentDialog choosePictureFragmentDialog = new ChoosePictureFragmentDialog();
                        choosePictureFragmentDialog.show(getFragmentManager(), "choose");
                        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
                            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                                choosePictureFragmentDialog.dismiss();
                                if (isSuccess) {
                                    ShowLoading("正在上传图片...");
                                    spUpImg = UpYunClient.rxUpLoad("/course/", filePath)

                                        .observeOn(AndroidSchedulers.mainThread())
                                        .onBackpressureBuffer()
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Subscriber<String>() {
                                            @Override public void onCompleted() {

                                            }

                                            @Override public void onError(Throwable e) {
                                                hideLoading();
                                            }

                                            @Override public void onNext(String upImg) {
                                                hideLoading();
                                                if (TextUtils.isEmpty(upImg)) {
                                                    ToastUtils.showDefaultStyle("图片上传失败");
                                                } else {
                                                    AddBodyTestBean.Photo photo = new AddBodyTestBean.Photo();
                                                    photo.photo = upImg;
                                                    datas.add(photo);
                                                    imageGridAdapter.refresh(datas);
                                                    //                                                        imageGridAdapter.notifyDataSetChanged();

                                                }
                                            }
                                        });
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

    public void getInfo() {
        QcCloudClient.getApi().getApi.qcGetBodyTest(measureId, getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<QcGetBodyTestResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcGetBodyTestResponse qcGetBodyTestResponse) {
                    Measure mMeasure = qcGetBodyTestResponse.data.measure;
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
                    if (!TextUtils.isEmpty(mMeasure.circumference_of_left_thigh)) {
                        leftThigh.setVisibility(View.VISIBLE);
                        leftThigh.setContent(String.format("%s", mMeasure.circumference_of_left_thigh));
                    }
                    if (!TextUtils.isEmpty(mMeasure.circumference_of_right_thigh)) {
                        rightThigh.setVisibility(View.VISIBLE);
                        rightThigh.setContent(String.format("%s", mMeasure.circumference_of_right_thigh));
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
                    if (!mModel.equalsIgnoreCase("service")) {
                        imageGridAdapter.setIsEditable(true);
                        photosTitle.setVisibility(View.VISIBLE);
                        if (qcGetBodyTestResponse.data.measure.extra != null) {
                            for (QcBodyTestTemplateRespone.Extra extra : qcGetBodyTestResponse.data.measure.extra) {
                                CommonInputView commonInputView = new CommonInputView(getContext());

                                commonInputView.setTag(R.id.tag_0, extra.name);
                                commonInputView.setTag(R.id.tag_1, extra.id);
                                commonInputView.setTag(R.id.tag_2, extra.unit);
                                otherData.addView(commonInputView);
                                commonInputView.setLabel(extra.name + "(" + extra.unit + ")");
                                commonInputView.setContent(extra.value);
                            }
                        }
                        //                        for (AddBodyTestBean.Photo photo :qcGetBodyTestResponse.data.measure.photos){
                        //                            datas.add(new ImageGridBean(photo.photo));
                        //                        }

                        if (qcGetBodyTestResponse.data.measure.photos != null) datas.addAll(qcGetBodyTestResponse.data.measure.photos);
                        imageGridAdapter.refresh(datas);
                    } else {
                        imageGridAdapter.setIsEditable(false);
                        datas.clear();
                        imageGridAdapter.refresh(datas);
                        photosTitle.setVisibility(View.GONE);
                    }
                }
            });
    }

    public void addTest() {
        QcCloudClient.getApi().getApi.qcGetBodyTestModel(getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<QcBodyTestTemplateRespone>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcBodyTestTemplateRespone qcBodyTestTemplateRespone) {
                    QcBodyTestTemplateRespone.Base mBase = qcBodyTestTemplateRespone.data.template.base;
                    if (mBase.show_bmi) {
                        bmi.setVisibility(View.VISIBLE);
                    } else {
                        bmi.setVisibility(View.GONE);
                    }
                    if (mBase.show_body_fat_rate) {
                        bodyFatRate.setVisibility(View.VISIBLE);
                    } else {
                        bodyFatRate.setVisibility(View.GONE);
                    }
                    if (mBase.show_circumference_of_left_calf) {
                        leftCalf.setVisibility(View.VISIBLE);
                    } else {
                        leftCalf.setVisibility(View.GONE);
                    }
                    if (mBase.show_circumference_of_right_calf) {
                        rightCalf.setVisibility(View.VISIBLE);
                    } else {
                        rightCalf.setVisibility(View.GONE);
                    }

                    chest.setVisibility(mBase.show_circumference_of_chest ? View.VISIBLE : View.GONE);
                    leftThigh.setVisibility(mBase.show_circumference_of_left_thigh ? View.VISIBLE : View.GONE);
                    rightThigh.setVisibility(mBase.show_circumference_of_right_thigh ? View.VISIBLE : View.GONE);
                    leftUpper.setVisibility(mBase.show_circumference_of_left_upper ? View.VISIBLE : View.GONE);
                    rightUpper.setVisibility(mBase.show_circumference_of_right_upper ? View.VISIBLE : View.GONE);
                    height.setVisibility(mBase.show_height ? View.VISIBLE : View.GONE);
                    hipline.setVisibility(mBase.show_hipline ? View.VISIBLE : View.GONE);
                    waistline.setVisibility(mBase.show_waistline ? View.VISIBLE : View.GONE);
                    weight.setVisibility(mBase.show_weight ? View.VISIBLE : View.GONE);
                    if (!mModel.equalsIgnoreCase("service")) {
                        photosTitle.setVisibility(View.VISIBLE);
                        imageGridAdapter.setIsEditable(true);
                        for (QcBodyTestTemplateRespone.Extra extra : qcBodyTestTemplateRespone.data.template.extra) {
                            CommonInputView commonInputView = new CommonInputView(getContext());
                            commonInputView.setTag(R.id.tag_0, extra.name);
                            commonInputView.setTag(R.id.tag_1, extra.id);
                            commonInputView.setTag(R.id.tag_2, extra.unit);
                            otherData.addView(commonInputView);
                            commonInputView.setLabel(extra.name + "(" + extra.unit + ")");
                            commonInputView.setContent(extra.value);
                            commonInputView.setContentColor(
                                ContextCompat.getColor(getContext(), R.color.text_dark));
                        }
                    } else {
                        datas.clear();
                        imageGridAdapter.setIsEditable(false);
                        imageGridAdapter.refresh(datas);
                        photosTitle.setVisibility(View.GONE);
                    }
                }
            });
    }

    @OnClick(R.id.test_date) public void onClickDate() {
        chooseDate();
    }

    public void setDate(String s) {
        testDate.setContent(s);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (spUpImg != null && spUpImg.isUnsubscribed()) spUpImg.unsubscribe();
    }

    public Measure getMeasure() {
        Measure measure = new Measure();
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
        if (pwTime == null) pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
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
        Measure mMeasure = getMeasure();
        AddBodyTestBean addBodyTestBean = new AddBodyTestBean();
        addBodyTestBean.model = mModel;
        addBodyTestBean.id = mModelId;
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
        if (!mModel.equalsIgnoreCase("service")) {
            List<QcBodyTestTemplateRespone.Extra> extras = new ArrayList<>();
            for (int i = 0; i < otherData.getChildCount(); i++) {
                CommonInputView v = (CommonInputView) otherData.getChildAt(i);
                QcBodyTestTemplateRespone.Extra extra = new QcBodyTestTemplateRespone.Extra();
                extra.id = (String) v.getTag(R.id.tag_1);
                extra.unit = (String) v.getTag(R.id.tag_2);
                extra.name = (String) v.getTag(R.id.tag_0);
                extra.value = v.getContent();
                extras.add(extra);
            }
            addBodyTestBean.extra = new Gson().toJson(extras);
            addBodyTestBean.photos = datas;
        }

        addBodyTestBean.user_id = mStudentId;
        if (!TextUtils.isEmpty(measureId)) {//修改
            QcCloudClient.getApi().postApi.qcUpdateBodyTest(measureId, addBodyTestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onNext(QcResponse qcResponse) {
                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            ToastUtils.showDefaultStyle("保存成功");

                            getActivity().onBackPressed();
                        }
                    }
                });
        } else { //添加
            QcCloudClient.getApi().postApi.qcAddBodyTest(addBodyTestBean)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onNext(QcResponse qcResponse) {
                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            ToastUtils.showDefaultStyle("保存成功");
                            RxBus.getBus().post(RxBus.BUS_REFRESH);
                            getActivity().finish();
                        }
                    }
                });
        }
    }

    @OnClick(R.id.delete) public void OnDel() {
        delBatchComfirmDialog = new MaterialDialog.Builder(getActivity()).autoDismiss(true)
            .content("是否删除此条体测信息?")
            .positiveText("确定")
            .negativeText("取消")
            .callback(new MaterialDialog.ButtonCallback() {
                @Override public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog);
                    QcCloudClient.getApi().postApi.qcDelBodyTest(measureId, getParams())
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<QcResponse>() {
                            @Override public void onCompleted() {

                            }

                            @Override public void onError(Throwable e) {
                                ToastUtils.showDefaultStyle("删除失败");
                            }

                            @Override public void onNext(QcResponse qcResponse) {
                                ToastUtils.showDefaultStyle("删除成功");
                                RxBus.getBus().post(RxBus.BUS_REFRESH);
                                getActivity().finish();
                            }
                        });
                }
            })
            .build();
        delBatchComfirmDialog.show();
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", mModelId);
        return params;
    }

    public void ShowLoading(String content) {
        if (loadingDialog == null) {
            loadingDialog = new MaterialDialog.Builder(getContext()).content("请稍后").progress(true, 0).cancelable(false).build();
        }
        if (content != null) loadingDialog.setContent(content);
        loadingDialog.show();
    }

    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public List<String> getImages() {
        List<String> images = new ArrayList<>();
        for (AddBodyTestBean.Photo p : datas) {
            images.add(p.photo);
        }
        return images;
    }
}
