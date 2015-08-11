package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioGroup;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

public class CompleteActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.comple_name)
    TextInputLayout compleName;
    @Bind(R.id.comple_city)
    TextInputLayout compleCity;
    @Bind(R.id.comple_pw)
    TextInputLayout complePw;
    @Bind(R.id.comple_pw_re)
    TextInputLayout complePwRe;
    @Bind(R.id.comple_btn)
    Button compleBtn;

    String mCode;   //服务器验证验证码后返回的code

    @Bind(R.id.comple_gender)
    RadioGroup compleGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_complete_info);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("完善资料");
        getSupportActionBar().setIcon(R.drawable.ic_arrow_left);

        mCode = getIntent().getExtras().getString("code");
        initView();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.comple_fragment,new CompleteInfoFragment())
//                .commit();
    }

    private void initView() {
        compleName.setHint(getString(R.string.comple_name));
        compleCity.setHint("城市");
        complePw.setHint("设置登录密码");
        complePwRe.setHint("确认密码");

    }

    @OnClick(R.id.comple_btn)
    public void OnComplete() {
        RegisteBean bean = new RegisteBean();
        bean.setCode(mCode);
        bean.setCity(compleCity.getEditText().getText().toString().trim());
        String pw = complePw.getEditText().getText().toString().trim();
        String pwRe = complePwRe.getEditText().getText().toString().trim();
        if (!pw.equals(pwRe)) {
            complePwRe.setError(getString(R.string.err_password_not_match));
            return;
        }
        if (pw.length() < 6 || pw.length() > 16) {
            complePw.setError(getString(R.string.err_password_length));
            return;
        }
        bean.setPassword(pw);


        if (compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female){
            bean.setGender(1); //女
        }else bean.setGender(0);//男

        QcCloudClient.getApi()
                .postApi
                .qcRegister(bean)

                .subscribeOn(Schedulers.newThread())

                .subscribe(qcResponse ->
                        {
                            if (qcResponse.status == ResponseResult.SUCCESS){
                                //TODO 注册成功

                                this.finish();
                            }else {

                            }
                        }
                );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
