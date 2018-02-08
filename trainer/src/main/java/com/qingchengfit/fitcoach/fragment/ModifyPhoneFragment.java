//package com.qingchengfit.fitcoach.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//import cn.qingchengfit.RxBus;
//import cn.qingchengfit.network.response.QcResponse;
//import cn.qingchengfit.widgets.PasswordView;
//import cn.qingchengfit.widgets.PhoneEditText;
//import com.qingchengfit.fitcoach.App;
//import com.qingchengfit.fitcoach.R;
//import com.qingchengfit.fitcoach.activity.MainActivity;
//import cn.qingchengfit.bean.SendSmsCode;
//import com.qingchengfit.fitcoach.http.QcCloudClient;
//import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
//import com.qingchengfit.fitcoach.http.bean.ModifyPhoneNum;
//import com.qingchengfit.fitcoach.http.bean.ResponseResult;
//import java.lang.ref.WeakReference;
//import rx.Observer;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class ModifyPhoneFragment extends BaseSettingFragment {
//    public static final String TAG = ModifyPhoneFragment.class.getName();
//    @BindView(R.id.modifyphone_comfirm_btn) Button modifyphoneComfirmBtn;
//    @BindView(R.id.password) PasswordView password;
//    @BindView(R.id.checkcode) PasswordView checkcode;
//    @BindView(R.id.phone_num) PhoneEditText phoneNum;
//    //private  PostMsgHandler handler;
//    private Unbinder unbinder;
//    private PostMsgHandler handler;
//
//    public ModifyPhoneFragment() {
//    }
//
//    @Override public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        handler = new PostMsgHandler(getContext());
//    }
//
//    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_modify_phone, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        fragmentCallBack.onToolbarMenu(0, 0, "更改手机号码");
//        checkcode.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                if (phoneNum.checkPhoneNum()) {
//                    getCode();
//                    RxBus.getBus().post(new SendSmsCode());
//                }
//            }
//        });
//        return view;
//    }
//
//    /**
//     * 获取验证码
//     */
//    public void getCode() {
//        if (phoneNum.checkPhoneNum()) {
//            QcCloudClient.getApi().postApi.qcGetCode(
//                new GetCodeBean.Builder().area_code(phoneNum.getDistrictInt()).phone(phoneNum.getPhoneNum()).build())
//                .onBackpressureBuffer()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<QcResponse>() {
//                    @Override public void onCompleted() {
//
//                    }
//
//                    @Override public void onError(Throwable e) {
//
//                    }
//
//                    @Override public void onNext(QcResponse qcResponse) {
//
//                        if (qcResponse.status == ResponseResult.SUCCESS) {
//                            handler.sendEmptyMessage(0);
//                        } else {
//                            //LogUtil.d(":" + qcResponse.msg);
//                        }
//                    }
//                });
//        }
//    }
//
//    /**
//     * 确认按钮按下
//     */
//    @OnClick(R.id.modifyphone_comfirm_btn) public void onConfirm() {
//        if (phoneNum.checkPhoneNum() && password.checkValid() && checkcode.checkValid()) {
//
//            fragmentCallBack.ShowLoading("请稍后");
//            QcCloudClient.getApi().postApi.qcModifyPhoneNum(App.coachid, new ModifyPhoneNum.Builder().phone(phoneNum.getPhoneNum())
//                .area_code(phoneNum.getDistrictInt())
//                .code(checkcode.getCode())
//                .password(password.getCode()).build())
//                .onBackpressureBuffer()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<QcResponse>() {
//                @Override public void onCompleted() {
//
//                }
//
//                @Override public void onError(Throwable e) {
//                    fragmentCallBack.hideLoading();
//                    Toast.makeText(App.AppContex, "修改失败,请稍后再试", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override public void onNext(QcResponse qcResponse) {
//                    fragmentCallBack.hideLoading();
//                    if (qcResponse.status == ResponseResult.SUCCESS) {
//
//                        Toast.makeText(App.AppContex, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
//                        handler.removeMessages(0);
//                        Intent it = new Intent(getActivity(), MainActivity.class);
//                        it.putExtra(MainActivity.ACTION, MainActivity.LOGOUT);
//                        startActivity(it);
//                    } else {
//
//                        Toast.makeText(App.AppContex, "修改失败,请稍后再试", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }
//
//    @Override public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    public class PostMsgHandler extends Handler {
//        WeakReference<Context> context;
//        int count = 60;
//
//        PostMsgHandler(Context c) {
//            context = new WeakReference<Context>(c);
//        }
//
//        @Override public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (checkcode != null) {
//                StringBuffer stringBuffer = new StringBuffer();
//                stringBuffer.append(Integer.toString(count));
//                stringBuffer.append(getString(R.string.login_resend_msg));
//
//                checkcode.setRightText(stringBuffer.toString());
//                if (count == 60) checkcode.blockRightClick(true);
//                if (count > 0) {
//                    count--;
//                    handler.sendEmptyMessageDelayed(0, 1000);
//                } else {
//                    count = 60;
//                    checkcode.blockRightClick(false);
//                    checkcode.setRightText(getResources().getString(R.string.login_getcode));
//                }
//            }
//        }
//    }
//}
