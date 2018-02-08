//package com.qingchengfit.fitcoach.fragment;
//
//import android.content.Context;
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
//import cn.qingchengfit.network.response.QcResponse;
//import cn.qingchengfit.utils.LogUtil;
//import cn.qingchengfit.widgets.PasswordView;
//import cn.qingchengfit.widgets.PhoneEditText;
//import com.google.gson.Gson;
//import com.qingchengfit.fitcoach.App;
//import com.qingchengfit.fitcoach.R;
//import com.qingchengfit.fitcoach.http.QcCloudClient;
//import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
//import com.qingchengfit.fitcoach.http.bean.ModifyPwBean;
//import com.qingchengfit.fitcoach.http.bean.ResponseResult;
//import java.lang.ref.WeakReference;
//import rx.Observer;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ModifyPwFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class ModifyPwFragment extends BaseSettingFragment {
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    @BindView(R.id.modifypw_comfirm_btn) Button modifypwComfirmBtn;
//    Gson gson = new Gson();
//    @BindView(R.id.phone_num) PhoneEditText phoneNum;
//    @BindView(R.id.checkcode) PasswordView checkcode;
//    @BindView(R.id.password) PasswordView password;
//
//    private String mParam1;
//    private String mParam2;
//
//    private PostMsgHandler handler;
//    private Unbinder unbinder;
//
//    public ModifyPwFragment() {
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ModifyPwFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ModifyPwFragment newInstance(String param1, String param2) {
//        ModifyPwFragment fragment = new ModifyPwFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_modify_pw, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        fragmentCallBack.onToolbarMenu(0, 0, "更改密码");
//        handler = new PostMsgHandler(getContext());
//        checkcode.setOnClickListener(v -> getCode());
//        return view;
//    }
//
//    @OnClick(R.id.modifypw_comfirm_btn) public void onConfirm() {
//        if (phoneNum.checkPhoneNum() && checkcode.checkValid() && password.checkValid()) {
//            fragmentCallBack.ShowLoading("请稍后");
//            QcCloudClient.getApi().postApi.qcMoidfyPw(App.coachid, new ModifyPwBean.Builder().phone(phoneNum.getPhoneNum())
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
//                    Toast.makeText(App.AppContex, "修改失败", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override public void onNext(QcResponse qcResponse) {
//                    fragmentCallBack.hideLoading();
//                    if (qcResponse.status == ResponseResult.SUCCESS) {
//
//                        Toast.makeText(App.AppContex, "修改成功", Toast.LENGTH_SHORT).show();
//                        getActivity().onBackPressed();
//                    } else {
//                        Toast.makeText(App.AppContex, qcResponse.msg, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }
//
//    /**
//     * 获取验证码
//     */
//    public void getCode() {
//        if (phoneNum.checkPhoneNum()) {
//            QcCloudClient.getApi().postApi.qcGetCode(
//                new GetCodeBean.Builder().phone(phoneNum.getPhoneNum()).area_code(phoneNum.getDistrictInt()).build())
//                .onBackpressureBuffer()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<QcResponse>() {
//                    @Override public void onCompleted() {
//
//                    }
//
//                    @Override public void onError(Throwable e) {
//                        Toast.makeText(App.AppContex, "发送验证码失败", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override public void onNext(QcResponse qcResponse) {
//
//                        if (qcResponse.status == ResponseResult.SUCCESS) {
//                            LogUtil.d("succ");
//                            handler.sendEmptyMessage(0);
//                        } else {
//                            LogUtil.d(":" + qcResponse.msg);
//                        }
//                    }
//                });
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
