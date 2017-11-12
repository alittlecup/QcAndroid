package cn.qingchengfit.testmodule;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {
    public ObservableField<String> textfile=new ObservableField<>("1111");
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
      //Gym gym = new Gym();
      //gym.id =
      getSupportFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out)
          .replace(R.id.frag, new TextFragment())
          .commit();
    }
}
