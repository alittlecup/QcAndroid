package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.BodyTestActivity;
import com.qingchengfit.fitcoach.adapter.ImageGridAdapter;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.FullyGridLayoutManager;
import com.qingchengfit.fitcoach.component.GalleryPhotoViewDialog;
import com.qingchengfit.fitcoach.component.InterupteLinearLayout;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.AddBodyTestBean;
import com.qingchengfit.fitcoach.http.bean.Measure;
import com.qingchengfit.fitcoach.http.bean.QcBodyTestTemplateRespone;
import com.qingchengfit.fitcoach.http.bean.QcGetBodyTestResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.http.HEAD;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BodyTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BodyTestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.img_model)
    ImageView imgModel;
    @Bind(R.id.upper)
    TextView upper;
    @Bind(R.id.hipline)
    TextView hipline;
    @Bind(R.id.chest)
    TextView chest;
    @Bind(R.id.thigh)
    TextView thigh;
    @Bind(R.id.waistline)
    TextView waistline;
    @Bind(R.id.calf)
    TextView calf;
    @Bind(R.id.height)
    TextView height;
    @Bind(R.id.height_layout)
    LinearLayout heightLayout;
    @Bind(R.id.weight)
    TextView weight;
    @Bind(R.id.weight_layout)
    LinearLayout weightLayout;
    @Bind(R.id.bmi)
    TextView bmi;
    @Bind(R.id.bmi_layout)
    LinearLayout bmiLayout;
    @Bind(R.id.body_fat_rate)
    TextView bodyFatRate;
    @Bind(R.id.body_fat_rate_layout)
    LinearLayout bodyFatRateLayout;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.other_data)
    InterupteLinearLayout otherData;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.test_pic_title)
    TextView testPicTitle;

    private ImageGridAdapter imageGridAdapter;
    private FullyGridLayoutManager gridLayoutManager;
    private String mMeasureId;
    private List<AddBodyTestBean.Photo> datas = new ArrayList<>();
    // TODO: Rename and change types of parameters
//    private QcBodyTestTemplateRespone.Base mBase;


    public BodyTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BodyTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BodyTestFragment newInstance(String measreid) {
        BodyTestFragment fragment = new BodyTestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, measreid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMeasureId = getArguments().getString(ARG_PARAM1);
//            mMeasure = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body_test, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setTitle("体测数据");
        toolbar.inflateMenu(R.menu.menu_text_edit);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //跳到编辑页面
                ((BodyTestActivity)getActivity()).goModify();
                return true;
            }
        });
        initInfo();
        imageGridAdapter = new ImageGridAdapter(datas);
        gridLayoutManager = new FullyGridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(imageGridAdapter);
        imageGridAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if (v.getId() == R.id.delete) {
                    datas.remove(pos);
                    imageGridAdapter.notifyDataSetChanged();
                } else {
                    if (pos < datas.size()) {
                        GalleryPhotoViewDialog galleryPhotoViewDialog = new GalleryPhotoViewDialog(getContext());
                        galleryPhotoViewDialog.setImage(getImages());
                        galleryPhotoViewDialog.show();
                    } else {
                        ChoosePictureFragmentDialog choosePictureFragmentDialog = new ChoosePictureFragmentDialog();
                        choosePictureFragmentDialog.show(getFragmentManager(), "choose");
                        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
                            @Override
                            public void onChoosePicResult(boolean isSuccess, String filePath) {
                                choosePictureFragmentDialog.dismiss();
                                if (isSuccess) {
                                    Observable.create(new Observable.OnSubscribe<String>() {
                                        @Override
                                        public void call(Subscriber<? super String> subscriber) {
                                            String upImg = UpYunClient.upLoadImg("course/", new File(filePath));
                                            subscriber.onNext(upImg);
                                        }
                                    }).observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new Subscriber<String>() {
                                                @Override
                                                public void onCompleted() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }

                                                @Override
                                                public void onNext(String upImg) {
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
        QcCloudClient.getApi().getApi.qcGetBodyTest(mMeasureId, ((BodyTestActivity) getActivity()).getParams())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcGetBodyTestResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcGetBodyTestResponse qcGetBodyTestResponse) {
                        initView(qcGetBodyTestResponse.data.measure);
                        datas.clear();
                        if (qcGetBodyTestResponse.data.measure.photos != null)
                            datas.addAll(qcGetBodyTestResponse.data.measure.photos);
                        else
                            testPicTitle.setVisibility(View.GONE);

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
                            }
                        }
//                        for (AddBodyTestBean.Photo photo :qcGetBodyTestResponse.data.measure.photos){
//                            datas.add(new ImageGridBean(photo.photo));
//                        }


                    }
                });
    }

    public void initView(Measure mMeasure) {
        title.setText(DateUtils.getServerDateDay(DateUtils.formatDateFromServer(mMeasure.created_at)) + "体测数据");
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
        if (!TextUtils.isEmpty(mMeasure.circumference_of_calf)) {
            calf.setVisibility(View.VISIBLE);
            calf.setText(String.format("小腿围: %s cm", mMeasure.circumference_of_calf));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_chest)) {
            chest.setVisibility(View.VISIBLE);
            chest.setText(String.format("胸围: %s cm", mMeasure.circumference_of_chest));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_thigh)) {
            thigh.setVisibility(View.VISIBLE);
            thigh.setText(String.format("大腿围: %s cm", mMeasure.circumference_of_thigh));
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_upper)) {
            upper.setVisibility(View.VISIBLE);
            upper.setText(String.format("上臂围: %s cm", mMeasure.circumference_of_upper));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
