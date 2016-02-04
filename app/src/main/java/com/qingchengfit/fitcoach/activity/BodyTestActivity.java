package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bigkoo.pickerview.TimeDialogWindow;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.ImageGridAdapter;
import com.qingchengfit.fitcoach.component.FullyGridLayoutManager;
import com.qingchengfit.fitcoach.fragment.BodyTestFragment;
import com.qingchengfit.fitcoach.fragment.ModifyBodyTestFragment;
import com.qingchengfit.fitcoach.http.bean.AddBodyTestBean;
import com.qingchengfit.fitcoach.http.bean.Measure;
import com.qingchengfit.fitcoach.http.bean.QcBodyTestTemplateRespone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

public class BodyTestActivity extends AppCompatActivity {



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
    private String mUserId;
    private int mGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        mModel = getIntent().getStringExtra("model");
        mModelId = getIntent().getStringExtra("modelid");
        measureId = getIntent().getStringExtra("id");
        mUserId = getIntent().getStringExtra("studentid");
        mGender = getIntent().getIntExtra("gender",0);

        if (getIntent().getIntExtra("type", 0) == 0) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.web_frag_layout,BodyTestFragment.newInstance(measureId,mGender))
                    .commit();

        } else {
            //添加
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.web_frag_layout, ModifyBodyTestFragment.newInstance(measureId,mModel,mModelId,mUserId))
                    .commit();
        }

//        mChosepicOb = RxBus.getBus().register(RxBus.BUS_CHOOSEPIC);
//        mChosepicOb.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                LogUtil.e("s:" + s);
//                chooseDate();
//            }
//        });
    }
    public void goModify(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.web_frag_layout, ModifyBodyTestFragment.newInstance(measureId,mModel,mModelId,mUserId))
                .addToBackStack(null)
                .commit();
    }







//    public void otherClickable(boolean click) {
//        for (int i = 0; i < otherData.getChildCount(); i++) {
//            CommonInputView v = (CommonInputView) otherData.getChildAt(i);
//
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            v.setCanClick(click);
//        }
//    }

//    public void saveInfo() {
//        Measure mMeasure = mModifyBodyTestFragment.getMeasure();
//        AddBodyTestBean addBodyTestBean = new AddBodyTestBean();
//        addBodyTestBean.model = mModel;
//        addBodyTestBean.id = mModelId;
//        if (!TextUtils.isEmpty(mMeasure.bmi)) {
//            addBodyTestBean.bmi = mMeasure.bmi;
//        }
//        if (!TextUtils.isEmpty(mMeasure.weight)) {
//            addBodyTestBean.weight = mMeasure.weight;
//        }
//        if (!TextUtils.isEmpty(mMeasure.height)) {
//            addBodyTestBean.height = mMeasure.height;
//        }
//        if (!TextUtils.isEmpty(mMeasure.body_fat_rate)) {
//            addBodyTestBean.body_fat_rate = mMeasure.body_fat_rate;
//        }
//        if (!TextUtils.isEmpty(mMeasure.circumference_of_calf)) {
//            addBodyTestBean.circumference_of_calf = mMeasure.circumference_of_calf;
//        }
//        if (!TextUtils.isEmpty(mMeasure.circumference_of_chest)) {
//            addBodyTestBean.circumference_of_chest = mMeasure.circumference_of_chest;
//        }
//        if (!TextUtils.isEmpty(mMeasure.circumference_of_thigh)) {
//            addBodyTestBean.circumference_of_thigh = mMeasure.circumference_of_thigh;
//        }
//        if (!TextUtils.isEmpty(mMeasure.circumference_of_upper)) {
//            addBodyTestBean.circumference_of_upper = mMeasure.circumference_of_upper;
//        }
//        if (!TextUtils.isEmpty(mMeasure.hipline)) {
//            addBodyTestBean.hipline = mMeasure.hipline;
//        }
//        if (!TextUtils.isEmpty(mMeasure.waistline)) {
//            addBodyTestBean.waistline = mMeasure.waistline;
//        }
//        List<QcBodyTestTemplateRespone.Extra> extras = new ArrayList<>();
//        for (int i = 0; i < otherData.getChildCount(); i++) {
//            CommonInputView v = (CommonInputView) otherData.getChildAt(i);
//            QcBodyTestTemplateRespone.Extra extra = new QcBodyTestTemplateRespone.Extra();
//            extra.id = (String) v.getTag(R.id.tag_1);
//            extra.unit = (String) v.getTag(R.id.tag_2);
//            extra.name = (String) v.getTag(R.id.tag_0);
//            extra.value = v.getContent();
//            extras.add(extra);
//        }
//        addBodyTestBean.extra = new Gson().toJson(extras);
//        addBodyTestBean.photos = datas;
//        if (!TextUtils.isEmpty(measureId)) {//修改
//            QcCloudClient.getApi().postApi.qcUpdateBodyTest(measureId, addBodyTestBean)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(new Subscriber<QcResponse>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(QcResponse qcResponse) {
//                            if (qcResponse.status == ResponseResult.SUCCESS) {
//                                ToastUtils.showDefaultStyle("保存成功");
//
//                                BodyTestActivity.this.onBackPressed();
//                            }
//                        }
//                    });
//
//        } else { //添加
//            QcCloudClient.getApi().postApi.qcAddBodyTest(addBodyTestBean)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(new Subscriber<QcResponse>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(QcResponse qcResponse) {
//                            if (qcResponse.status == ResponseResult.SUCCESS) {
//                                ToastUtils.showDefaultStyle("保存成功");
//                                BodyTestActivity.this.finish();
//                            }
//                        }
//                    });
//        }
//    }
//
//
//
    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", mModelId);
        return params;
    }


    @Override
    protected void onDestroy() {
//        RxBus.getBus().unregister(RxBus.BUS_CHOOSEPIC.getClass().getName(), mChosepicOb);
        super.onDestroy();
    }
}
