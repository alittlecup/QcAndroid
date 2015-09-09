package com.qingchengfit.fitcoach.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyPhoneFragment extends Fragment {
    public static final String TAG = ModifyPhoneFragment.class.getName();

    @Bind(R.id.modifyphone_pw)
    EditText modifyphonePw;
    @Bind(R.id.modifyphone_phone)
    EditText modifyphonePhone;
    @Bind(R.id.modifyphone_getcode_btn)
    TextView modifyphoneGetcodeBtn;
    @Bind(R.id.modifyphone_code)
    EditText modifyphoneCode;
    @Bind(R.id.modifyphone_comfirm_btn)
    Button modifyphoneComfirmBtn;
    Handler handler = new Handler() {
        int count = 60;

        @Override
        public void handleMessage(Message msg) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(Integer.toString(count));
            stringBuffer.append(getString(R.string.login_resend_msg));

            modifyphoneGetcodeBtn.setText(stringBuffer.toString());
            if (count == 60)
                modifyphoneGetcodeBtn.setEnabled(false);
            if (count > 0) {
                count--;
                handler.sendEmptyMessageDelayed(0, 1000);
            } else {
                count = 60;
                modifyphoneGetcodeBtn.setEnabled(true);
                modifyphoneGetcodeBtn.setText(getResources().getString(R.string.login_getcode));
            }
        }
    };


    public ModifyPhoneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_phone, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.modifyphone_getcode_btn)
    public void getCode() {
        String phone = modifyphonePhone.getText().toString().trim();
        if (phone.length() < 11) {
            Toast.makeText(App.AppContex, getString(R.string.err_login_phonenum), Toast.LENGTH_SHORT).show();
            return;
        }
        QcCloudClient.getApi()
                .postApi
                .qcGetCode(new GetCodeBean(phone))
                .subscribeOn(Schedulers.newThread())
                .subscribe(qcResponse -> {
                    if (qcResponse.status == ResponseResult.SUCCESS) {
                        LogUtil.d("succ");
                        handler.sendEmptyMessage(0);
                    } else {
                        LogUtil.d(":" + qcResponse.msg);
                    }
                })
        ;
    }

    /**
     * 确认按钮按下
     */
    @OnClick(R.id.modifyphone_comfirm_btn)
    public void onConfirm() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
