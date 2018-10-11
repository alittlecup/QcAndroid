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

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_base_frag);
        mModel = getIntent().getStringExtra("model");
        mModelId = getIntent().getStringExtra("modelid");
        measureId = getIntent().getStringExtra("id");
        mUserId = getIntent().getStringExtra("studentid");
        mGender = getIntent().getIntExtra("gender", 0);

        if (getIntent().getIntExtra("type", 0) == 0) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.web_frag_layout, BodyTestFragment.newInstance(measureId, mGender))
                .commit();
        } else {
            //添加
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.web_frag_layout, ModifyBodyTestFragment.newInstance(measureId, mModel, mModelId, mUserId))
                .commit();
        }
    }

    public void goModify() {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.web_frag_layout, ModifyBodyTestFragment.newInstance(measureId, mModel, mModelId, mUserId))
            .addToBackStack(null)
            .commit();
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", mModelId);
        return params;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }
}
