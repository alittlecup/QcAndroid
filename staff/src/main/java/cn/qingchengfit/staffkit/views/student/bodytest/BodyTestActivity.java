package cn.qingchengfit.staffkit.views.student.bodytest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import cn.qingchengfit.staffkit.R;
import java.util.HashMap;

public class BodyTestActivity extends AppCompatActivity {

    private String mModel;
    private String mModelId;
    private String measureId;
    private String mUserId;
    private int mGender;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_no_toolbar);
        mModel = getIntent().getStringExtra("model");
        mModelId = getIntent().getStringExtra("modelid");
        measureId = getIntent().getStringExtra("id");
        mUserId = getIntent().getStringExtra("studentid");
        mGender = getIntent().getIntExtra("gender", 0);

        if (getIntent().getIntExtra("type", 0) == 0) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.student_frag, BodyTestFragment.newInstance(measureId, mGender))
                .commit();
        } else {
            //添加
            //            getSupportFragmentManager().beginTransaction()
            //                    .replace(R.id.student_frag, ModifyBodyTestFragment.newInstance(measureId, mModel, mModelId, mUserId))
            //                    .commit();
        }
    }

    public void goModify() {
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", mModelId);
        return params;
    }

    @Override protected void onDestroy() {
        //        RxBus.getBus().unregister(RxBus.BUS_CHOOSEPIC.getClass().getName(), mChosepicOb);
        super.onDestroy();
    }
}
