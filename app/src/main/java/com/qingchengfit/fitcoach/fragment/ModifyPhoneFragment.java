package com.qingchengfit.fitcoach.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.TextpaperUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.ModifyPhoneNum;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyPhoneFragment extends BaseSettingFragment {
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
    //    MaterialDialog materialDialog;
    private  PostMsgHandler handler;
    public ModifyPhoneFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new PostMsgHandler(getContext());
//        materialDialog = new MaterialDialog.Builder(getActivity())
//                .content("修改中请稍后")
//                .progress(true, 0)
//
//                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_phone, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, 0, "更改手机号码");
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QcResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcResponse qcResponse) {

                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            LogUtil.d("succ");
                            handler.sendEmptyMessage(0);
                        } else {
                            LogUtil.d(":" + qcResponse.msg);
                        }
                    }
                });

    }

    /**
     * 确认按钮按下
     */
    @OnClick(R.id.modifyphone_comfirm_btn)
    public void onConfirm() {
        if (TextpaperUtils.isEmpty(modifyphonePw.getText().toString(), modifyphonePhone.getText().toString(), modifyphoneCode.getText().toString())) {
            Toast.makeText(App.AppContex, "请填写正确的信息", Toast.LENGTH_SHORT).show();
            return;
        }

        ModifyPhoneNum modifyPhoneNum = new ModifyPhoneNum(modifyphonePhone.getText().toString()
                , modifyphonePw.getText().toString(), modifyphoneCode.getText().toString()
        );

        fragmentCallBack.ShowLoading("请稍后");
        QcCloudClient.getApi().postApi.qcModifyPhoneNum(App.coachid, modifyPhoneNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QcResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        fragmentCallBack.hideLoading();
                        Toast.makeText(App.AppContex, "修改失败,请稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(QcResponse qcResponse) {
                        fragmentCallBack.hideLoading();
                        if (qcResponse.status == ResponseResult.SUCCESS) {

                            Toast.makeText(App.AppContex, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
                            handler.removeMessages(0);
                            Intent it = new Intent(getActivity(), MainActivity.class);
                            it.putExtra(MainActivity.ACTION, MainActivity.LOGOUT);
                            startActivity(it);

                        } else {

                            Toast.makeText(App.AppContex, "修改失败,请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class PostMsgHandler extends Handler {
        WeakReference<Context> context;
        int count = 60;

        PostMsgHandler(Context c) {
            context = new WeakReference<Context>(c);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (modifyphoneGetcodeBtn != null) {
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
        }
    }
}
