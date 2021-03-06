package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CompleteActivity extends AppCompatActivity {

	Toolbar toolbar;
	TextInputLayout compleName;
	TextInputLayout compleCity;
	TextInputLayout complePw;
	TextInputLayout complePwRe;
	Button compleBtn;

    String mCode;   //服务器验证验证码后返回的code

	RadioGroup compleGender;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_complete_info);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      compleName = (TextInputLayout) findViewById(R.id.comple_name);
      compleCity = (TextInputLayout) findViewById(R.id.comple_city);
      complePw = (TextInputLayout) findViewById(R.id.comple_pw);
      complePwRe = (TextInputLayout) findViewById(R.id.comple_pw_re);
      compleBtn = (Button) findViewById(R.id.comple_btn);
      compleGender = (RadioGroup) findViewById(R.id.comple_gender);
      findViewById(R.id.comple_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          OnComplete();
        }
      });

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

        if (compleGender.getCheckedRadioButtonId() == R.id.comple_gender_female) {
            bean.setGender(1); //女
        } else {
            bean.setGender(0);//男
        }

        TrainerRepository.getStaticTrainerAllApi().qcRegister(bean)

            .onBackpressureBuffer()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponse -> {
            if (qcResponse.status == ResponseResult.SUCCESS) {
                //TODO 注册成功
                this.finish();
            } else {

            }
        }, throwable -> {
        }, () -> {
        });
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
