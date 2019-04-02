package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.bean.CurentPermissions;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.BodyTestActivity;
import com.qingchengfit.fitcoach.adapter.ImageGridAdapter;
import com.qingchengfit.fitcoach.component.FullyGridLayoutManager;
import com.qingchengfit.fitcoach.component.GalleryPhotoViewDialog;
import com.qingchengfit.fitcoach.component.InterupteLinearLayout;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.AddBodyTestBean;
import com.qingchengfit.fitcoach.http.bean.Measure;
import com.qingchengfit.fitcoach.http.bean.QcBodyTestTemplateRespone;
import com.qingchengfit.fitcoach.http.bean.QcGetBodyTestResponse;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BodyTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BodyTestFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
	ImageView imgModel;
	TextView hipline;
	TextView chest;
	TextView waistline;
	TextView height;
	LinearLayout heightLayout;
	TextView weight;
	LinearLayout weightLayout;
	TextView bmi;
	LinearLayout bmiLayout;
	TextView bodyFatRate;
	LinearLayout bodyFatRateLayout;
	TextView title;
	InterupteLinearLayout otherData;
	RecyclerView recyclerview;
	Toolbar toolbar;
	TextView testPicTitle;
	TextView leftUpper;
	TextView rightUpper;
	TextView leftThigh;
	TextView rightThigh;
	TextView rightCalf;
	TextView leftCalf;
    private int mGender;

    private ImageGridAdapter imageGridAdapter;
    private FullyGridLayoutManager gridLayoutManager;
    private String mMeasureId;
    private List<AddBodyTestBean.Photo> datas = new ArrayList<>();

    private Subscription spUpImg;

    public BodyTestFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BodyTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BodyTestFragment newInstance(String measreid, int gender) {
        BodyTestFragment fragment = new BodyTestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, measreid);
        args.putInt(ARG_PARAM2, gender);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMeasureId = getArguments().getString(ARG_PARAM1);
            //            mMeasure = getArguments().getParcelable(ARG_PARAM2);
            mGender = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body_test, container, false);
      imgModel = (ImageView) view.findViewById(R.id.img_model);
      hipline = (TextView) view.findViewById(R.id.hipline);
      chest = (TextView) view.findViewById(R.id.chest);
      waistline = (TextView) view.findViewById(R.id.waistline);
      height = (TextView) view.findViewById(R.id.height);
      heightLayout = (LinearLayout) view.findViewById(R.id.height_layout);
      weight = (TextView) view.findViewById(R.id.weight);
      weightLayout = (LinearLayout) view.findViewById(R.id.weight_layout);
      bmi = (TextView) view.findViewById(R.id.bmi);
      bmiLayout = (LinearLayout) view.findViewById(R.id.bmi_layout);
      bodyFatRate = (TextView) view.findViewById(R.id.body_fat_rate);
      bodyFatRateLayout = (LinearLayout) view.findViewById(R.id.body_fat_rate_layout);
      title = (TextView) view.findViewById(R.id.title);
      otherData = (InterupteLinearLayout) view.findViewById(R.id.other_data);
      recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      testPicTitle = (TextView) view.findViewById(R.id.test_pic_title);
      leftUpper = (TextView) view.findViewById(R.id.left_upper);
      rightUpper = (TextView) view.findViewById(R.id.right_upper);
      leftThigh = (TextView) view.findViewById(R.id.left_thigh);
      rightThigh = (TextView) view.findViewById(R.id.right_thigh);
      rightCalf = (TextView) view.findViewById(R.id.right_calf);
      leftCalf = (TextView) view.findViewById(R.id.left_calf);

      toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setTitle("体测数据");
        if (CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS_CAN_WRITE)) {
            toolbar.inflateMenu(R.menu.menu_text_edit);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                //跳到编辑页面
                ((BodyTestActivity) getActivity()).goModify();
                return true;
            }
        });
        if (mGender == 0) {
            Glide.with(App.AppContex).load(R.drawable.img_model_male).into(imgModel);
        } else {
            Glide.with(App.AppContex).load(R.drawable.img_model_female).into(imgModel);
        }
        initInfo();
        imageGridAdapter = new ImageGridAdapter(datas);
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
                                    spUpImg = UpYunClient.rxUpLoad("course/", filePath)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .onBackpressureBuffer()
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Subscriber<String>() {
                                            @Override public void onCompleted() {

                                            }

                                            @Override public void onError(Throwable e) {

                                            }

                                            @Override public void onNext(String upImg) {
                                                if (TextUtils.isEmpty(upImg)) {
                                                    ToastUtils.showDefaultStyle("图片上传失败");
                                                } else {
                                                    AddBodyTestBean.Photo photo = new AddBodyTestBean.Photo();
                                                    photo.photo = upImg;
                                                    datas.add(photo);
                                                    imageGridAdapter.refresh(datas);
                                                }
                                            }
                                        });
                                } else {
                                    LogUtil.e("选择图片失败");
                                }
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    public void initInfo() {
        TrainerRepository.getStaticTrainerAllApi().qcGetBodyTest(mMeasureId, ((BodyTestActivity) getActivity()).getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<QcGetBodyTestResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcGetBodyTestResponse qcGetBodyTestResponse) {
                    initView(qcGetBodyTestResponse.data.measure);
                    datas.clear();
                    if (qcGetBodyTestResponse.data.measure.photos != null && qcGetBodyTestResponse.data.measure.photos.size() > 0) {
                        datas.addAll(qcGetBodyTestResponse.data.measure.photos);
                    } else {
                        testPicTitle.setVisibility(View.GONE);
                    }

                    imageGridAdapter.refresh(datas);
                    if (qcGetBodyTestResponse.data.measure.extra != null) {
                        for (QcBodyTestTemplateRespone.Extra extra : qcGetBodyTestResponse.data.measure.extra) {
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
                    }

                }
            });
    }

    public void initView(Measure mMeasure) {
        title.setText(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(mMeasure.created_at)) + "体测数据");
        if (!TextUtils.isEmpty(mMeasure.bmi)) {
            bmiLayout.setVisibility(View.VISIBLE);
            bmi.setText(String.format("%s", mMeasure.bmi));
        }
        if (!TextUtils.isEmpty(mMeasure.weight)) {
            weightLayout.setVisibility(View.VISIBLE);
            weight.setText(String.format("%s", mMeasure.weight));
        }
        if (!TextUtils.isEmpty(mMeasure.height)) {
            heightLayout.setVisibility(View.VISIBLE);
            height.setText(String.format("%s", mMeasure.height));
        }
        if (!TextUtils.isEmpty(mMeasure.body_fat_rate)) {
            bodyFatRateLayout.setVisibility(View.VISIBLE);
            bodyFatRate.setText(String.format("%s", mMeasure.body_fat_rate));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_left_calf)) {
            leftCalf.setVisibility(View.VISIBLE);
            leftCalf.setText(String.format("小腿围(左): %s cm", mMeasure.circumference_of_left_calf));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_right_calf)) {
            rightCalf.setVisibility(View.VISIBLE);
            rightCalf.setText(String.format("小腿围(右): %s cm", mMeasure.circumference_of_right_calf));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_chest)) {
            chest.setVisibility(View.VISIBLE);
            chest.setText(String.format("胸围: %s cm", mMeasure.circumference_of_chest));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_left_thigh)) {
            leftThigh.setVisibility(View.VISIBLE);
            leftThigh.setText(String.format("大腿围(左): %s cm", mMeasure.circumference_of_left_thigh));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_right_thigh)) {
            rightThigh.setVisibility(View.VISIBLE);
            rightThigh.setText(String.format("大腿围(右): %s cm", mMeasure.circumference_of_right_thigh));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_left_upper)) {
            leftUpper.setVisibility(View.VISIBLE);
            leftUpper.setText(String.format("上臂围(左): %s cm", mMeasure.circumference_of_left_upper));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_right_upper)) {
            rightUpper.setVisibility(View.VISIBLE);
            rightUpper.setText(String.format("上臂围(右): %s cm", mMeasure.circumference_of_right_upper));
        }
        if (!TextUtils.isEmpty(mMeasure.hipline)) {
            hipline.setVisibility(View.VISIBLE);
            hipline.setText(String.format("臀围: %s cm", mMeasure.hipline));
        }
        if (!TextUtils.isEmpty(mMeasure.waistline)) {
            waistline.setVisibility(View.VISIBLE);
            waistline.setText(String.format("腰围: %s cm", mMeasure.waistline));
        }
    }

    public List<String> getImages() {
        List<String> images = new ArrayList<>();
        for (AddBodyTestBean.Photo p : datas) {
            images.add(p.photo);
        }
        return images;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        if (spUpImg != null && spUpImg.isUnsubscribed()) {
            spUpImg.unsubscribe();
        }
    }
}
