package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.ImageGridAdapter;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.FullyGridLayoutManager;
import com.qingchengfit.fitcoach.component.GalleryPhotoViewDialog;
import com.qingchengfit.fitcoach.component.InterupteLinearLayout;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.fragment.BodyTestFragment;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
import com.qingchengfit.fitcoach.fragment.ModifyBodyTestFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.AddBodyTestBean;
import com.qingchengfit.fitcoach.http.bean.Measure;
import com.qingchengfit.fitcoach.http.bean.QcBodyTestTemplateRespone;
import com.qingchengfit.fitcoach.http.bean.QcGetBodyTestResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BodyTestActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.other_data)
    InterupteLinearLayout otherData;
    @Bind(R.id.rootview)
    CoordinatorLayout rootview;
    @Bind(R.id.delete)
    TextView delete;

    private ImageGridAdapter imageGridAdapter;
    private FullyGridLayoutManager gridLayoutManager;
    private List<AddBodyTestBean.Photo> datas = new ArrayList<>();
    private boolean isModify = false;
    private String mModel;
    private String mModelId;
    private String measureId;
    private QcBodyTestTemplateRespone.Base mBase;
    private ModifyBodyTestFragment mModifyBodyTestFragment;
    private BodyTestFragment mBodyTestFragment;
    private Measure mBodyMeasure;
    private TimeDialogWindow pwTime;
    private Observable<String> mChosepicOb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_test);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        mModel = getIntent().getStringExtra("model");
        mModelId = getIntent().getStringExtra("modelid");
        measureId = getIntent().getStringExtra("id");
        if (getIntent().getIntExtra("type", 0) == 0) {
            //展示
            isModify = false;
        } else {
            //添加
            isModify = true;
        }
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment, BodyTestFragment.newInstance(, ""))
//                .commit();
        otherData.setCanChildClick(false);
        imageGridAdapter = new ImageGridAdapter(datas);
        gridLayoutManager = new FullyGridLayoutManager(getApplicationContext(), 3);
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
                    if (pos < datas.size() - (isModify ? 1 : 0)) {
                        GalleryPhotoViewDialog galleryPhotoViewDialog = new GalleryPhotoViewDialog(BodyTestActivity.this);
                        galleryPhotoViewDialog.setImage(getImages());
                        galleryPhotoViewDialog.show();
                    } else {
                        ChoosePictureFragmentDialog choosePictureFragmentDialog = new ChoosePictureFragmentDialog();
                        choosePictureFragmentDialog.show(getSupportFragmentManager(), "choose");
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


        if (!TextUtils.isEmpty(measureId))
            getInfo();
        else addTest();

        mChosepicOb = RxBus.getBus().register(RxBus.BUS_CHOOSEPIC);
        mChosepicOb.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                LogUtil.e("s:" + s);
                chooseDate();
            }
        });
    }

    public List<String> getImages() {
        List<String> images = new ArrayList<>();
        for (AddBodyTestBean.Photo p : datas) {
            images.add(p.photo);
        }
        return images;
    }

    public void addTest() {
        QcCloudClient.getApi().getApi.qcGetBodyTestModel(getParams())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcBodyTestTemplateRespone>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcBodyTestTemplateRespone qcBodyTestTemplateRespone) {
                        mBase = qcBodyTestTemplateRespone.data.template.base;
                        mModifyBodyTestFragment = ModifyBodyTestFragment.newInstance(mBase);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, mModifyBodyTestFragment)
                                .commit();
                    }
                });
    }

    public void getInfo() {
        QcCloudClient.getApi().getApi.qcGetBodyTest(measureId, getParams())
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
                        mBodyMeasure = qcGetBodyTestResponse.data.measure;
                        mBodyTestFragment = BodyTestFragment.newInstance(mBase, mBodyMeasure);
                        mModifyBodyTestFragment = ModifyBodyTestFragment.newInstance(mBodyMeasure);
                        datas = mBodyMeasure.photos;
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, mBodyTestFragment)
                                .commit();
                        for (QcBodyTestTemplateRespone.Extra extra : qcGetBodyTestResponse.data.measure.extra) {
                            CommonInputView commonInputView = new CommonInputView(BodyTestActivity.this);

                            commonInputView.setTag(R.id.tag_0, extra.name);
                            commonInputView.setTag(R.id.tag_1, extra.id);
                            commonInputView.setTag(R.id.tag_2, extra.unit);
                            otherData.addView(commonInputView);
                            commonInputView.setLabel(extra.name + "(" + extra.unit + ")");
                            commonInputView.setContent(extra.value);
                        }
//                        for (AddBodyTestBean.Photo photo :qcGetBodyTestResponse.data.measure.photos){
//                            datas.add(new ImageGridBean(photo.photo));
//                        }
                        imageGridAdapter.notifyDataSetChanged();

                    }
                });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!isModify) {
            getMenuInflater().inflate(R.menu.menu_text_edit, menu);
            imageGridAdapter.setIsEditable(false);
        } else {
            getMenuInflater().inflate(R.menu.menu_save, menu);
            imageGridAdapter.setIsEditable(true);
        }
        return true;
    }

    public void changeFragment() {

        if (isModify && mModifyBodyTestFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, mModifyBodyTestFragment)
                    .commit();
        } else if (!isModify && mBodyTestFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, mBodyTestFragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_eidt) {
            isModify = true;
            invalidateOptionsMenu();
            otherData.setCanChildClick(true);
            delete.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, mModifyBodyTestFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.getItemId() == R.id.ic_menu_save) {
            //保存
//            isModify = false;
//            invalidateOptionsMenu();
            saveInfo();

        } else if (item.getItemId() == android.R.id.home) {
            isModify = false;
            otherData.setCanChildClick(false);
            delete.setVisibility(View.GONE);
            invalidateOptionsMenu();
            this.onBackPressed();
        }
//        changeFragment();
        return true;
    }


    public void chooseDate() {
        if (pwTime == null)
            pwTime = new TimeDialogWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR), Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
        pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                mModifyBodyTestFragment.setDate(DateUtils.getServerDateDay(date));
//                testDate.setContent(DateUtils.getServerDateDay(date));
                pwTime.dismiss();
            }
        });
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
    }

    public void otherClickable(boolean click) {
        for (int i = 0; i < otherData.getChildCount(); i++) {
            CommonInputView v = (CommonInputView) otherData.getChildAt(i);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            v.setCanClick(click);
        }
    }

    public void saveInfo() {
        Measure mMeasure = mModifyBodyTestFragment.getMeasure();
        AddBodyTestBean addBodyTestBean = new AddBodyTestBean();
        addBodyTestBean.model = mModel;
        addBodyTestBean.id = mModelId;
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
        if (!TextUtils.isEmpty(mMeasure.circumference_of_calf)) {
            addBodyTestBean.circumference_of_calf = mMeasure.circumference_of_calf;
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_chest)) {
            addBodyTestBean.circumference_of_chest = mMeasure.circumference_of_chest;
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_thigh)) {
            addBodyTestBean.circumference_of_thigh = mMeasure.circumference_of_thigh;
        }
        if (!TextUtils.isEmpty(mMeasure.circumference_of_upper)) {
            addBodyTestBean.circumference_of_upper = mMeasure.circumference_of_upper;
        }
        if (!TextUtils.isEmpty(mMeasure.hipline)) {
            addBodyTestBean.hipline = mMeasure.hipline;
        }
        if (!TextUtils.isEmpty(mMeasure.waistline)) {
            addBodyTestBean.waistline = mMeasure.waistline;
        }
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
        if (!TextUtils.isEmpty(measureId)) {//修改
            QcCloudClient.getApi().postApi.qcUpdateBodyTest(measureId, addBodyTestBean)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<QcResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(QcResponse qcResponse) {
                            if (qcResponse.status == ResponseResult.SUCCESS) {
                                ToastUtils.showDefaultStyle("保存成功");

                                BodyTestActivity.this.onBackPressed();
                            }
                        }
                    });

        } else { //添加
            QcCloudClient.getApi().postApi.qcAddBodyTest(addBodyTestBean)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<QcResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(QcResponse qcResponse) {
                            if (qcResponse.status == ResponseResult.SUCCESS) {
                                ToastUtils.showDefaultStyle("保存成功");
                                BodyTestActivity.this.finish();
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.delete)
    public void OnDel() {
        QcCloudClient.getApi().postApi.qcDelBodyTest(measureId, getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QcResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showDefaultStyle("删除失败");
                    }

                    @Override
                    public void onNext(QcResponse qcResponse) {
                        ToastUtils.showDefaultStyle("删除成功");
                        BodyTestActivity.this.finish();
                    }
                });
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", mModelId);
        return params;
    }


    @Override
    protected void onDestroy() {
        RxBus.getBus().unregister(RxBus.BUS_CHOOSEPIC.getClass().getName(), mChosepicOb);
        super.onDestroy();
    }
}
