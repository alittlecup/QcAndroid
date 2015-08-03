package com.qingchengfit.fitcoach;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.registe_phone_num)
    TextInputLayout registePhoneNum;
    @Bind(R.id.registe_username)
    TextInputLayout registeUsername;
    @Bind(R.id.registe_password)
    TextInputLayout registePassword;
    @Bind(R.id.registe_phone_verity)
    TextInputLayout registePhoneVerity;
    @Bind(R.id.registe_getcode_btn)
    Button registeGetcodeBtn;
    @Bind(R.id.registe_checkcode_layout)
    LinearLayout registeCheckcodeLayout;
    @Bind(R.id.registe_btn)
    Button registeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.registe_btn)
    public void onRegisge(){
        Gson gson = new Gson();
        RegisteBean bean = new RegisteBean("123456778","papap","123123");
        QcCloudClient.getApi().qcCloudServer
                .qcRegister(PreferenceUtils.getPrefString(this,"token",""),"csrftoken="+PreferenceUtils.getPrefString(this,"token",""),bean)
                .subscribe(qcResponse -> LogUtil.e(qcResponse.msg))
                ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
