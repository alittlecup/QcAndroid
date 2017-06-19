package cn.qingchengfit.testmodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import cn.qingchengfit.views.fragments.TagInputFragment;

public class TestActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag, new TagInputFragment()).commit();
    }
}
