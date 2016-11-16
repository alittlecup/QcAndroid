package com.qingchengfit.fitcoach.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.ModifyPwBean;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.LogUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyPwFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyPwFragment extends BaseSettingFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.modifypw_new_pw)
    EditText modifypwNewPw;
    @BindView(R.id.modifypw_comfirm_pw)
    EditText modifypwComfirmPw;
    @BindView(R.id.modifypw_comfirm_btn)
    Button modifypwComfirmBtn;
    Gson gson = new Gson();
    @BindView(R.id.modifyphone_phone)
    EditText modifyphonePhone;
    @BindView(R.id.modifyphone_getcode_btn)
    TextView modifyphoneGetcodeBtn;
    @BindView(R.id.modifyphone_code)
    EditText modifyphoneCode;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PostMsgHandler handler;
    private Unbinder unbinder;

    public ModifyPwFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModifyPwFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyPwFragment newInstance(String param1, String param2) {
        ModifyPwFragment fragment = new ModifyPwFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modify_pw, container, false);
        unbinder=ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, 0, "更改密码");
        handler = new PostMsgHandler(getContext());
        return view;
    }


    @OnClick(R.id.modifypw_comfirm_btn)
    public void onConfirm() {
        if (TextUtils.isEmpty(modifyphoneCode.getText())) {
            Toast.makeText(getContext(), "请填写验证码", Toast.LENGTH_LONG).show();
            return;
        }
        String phone = modifyphonePhone.getText().toString().trim();
        String code = modifyphoneCode.getText().toString().trim();
        String now = modifypwNewPw.getText().toString().trim();
        String re = modifypwComfirmPw.getText().toString().trim();

        if (phone.length() < 11) {
            Toast.makeText(getContext(), "请填写正确的手机号", Toast.LENGTH_LONG).show();
            return;
        }


        if (!now.equals(re)) {
            Toast.makeText(getContext(), "密码不一致", Toast.LENGTH_LONG).show();
            return;
        }
        if (now.length() < 6 || now.length() > 16) {
            Toast.makeText(getContext(), "密码长度请保持6-16位", Toast.LENGTH_LONG).show();
            return;
        }
//        String id = PreferenceUtils.getPrefString(App.AppContex, "coach", "");
//        if (TextUtils.isEmpty(id)) {
//            //TODO error
//        }
//        Coach coach = gson.fromJson(id, Coach.class);
        fragmentCallBack.ShowLoading("请稍后");
        QcCloudClient.getApi().postApi.qcMoidfyPw(App.coachid, new ModifyPwBean(phone, code, now))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QcResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        fragmentCallBack.hideLoading();
                        Toast.makeText(App.AppContex, "修改失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(QcResponse qcResponse) {
                        fragmentCallBack.hideLoading();
                        if (qcResponse.status == ResponseResult.SUCCESS) {

                            Toast.makeText(App.AppContex, "修改成功", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        } else {
                            Toast.makeText(App.AppContex, qcResponse.msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
                        Toast.makeText(App.AppContex, "发送验证码失败", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
