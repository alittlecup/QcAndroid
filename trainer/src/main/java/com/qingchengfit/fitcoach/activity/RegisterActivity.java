package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;

public class RegisterActivity extends AppCompatActivity {

	TextInputLayout registePhoneNum;
	TextInputLayout registeUsername;
	TextInputLayout registePassword;
	TextInputLayout registePhoneVerity;
	Button registeGetcodeBtn;
	LinearLayout registeCheckcodeLayout;
	Button registeBtn;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
      registePhoneNum = (TextInputLayout) findViewById(R.id.registe_phone_num);
      registeUsername = (TextInputLayout) findViewById(R.id.registe_username);
      registePassword = (TextInputLayout) findViewById(R.id.registe_password);
      registePhoneVerity = (TextInputLayout) findViewById(R.id.registe_phone_verity);
      registeGetcodeBtn = (Button) findViewById(R.id.registe_getcode_btn);
      registeCheckcodeLayout = (LinearLayout) findViewById(R.id.registe_checkcode_layout);
      registeBtn = (Button) findViewById(R.id.registe_btn);
      findViewById(R.id.registe_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onRegisge();
        }
      });
    }

 public void onRegisge() {
        Gson gson = new Gson();
        RegisteBean bean = new RegisteBean("123456778", "papap", "123123");
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
