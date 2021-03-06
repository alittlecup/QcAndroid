package cn.qingchengfit.staffkit.train.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.train.model.GroupBean;
import cn.qingchengfit.staffkit.train.model.SignUpDetailResponse;
import cn.qingchengfit.staffkit.train.presenter.SignUpDetailPresenter;
import cn.qingchengfit.staffkit.train.presenter.SignUpView;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.AutoLineGroup;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by fb on 2017/3/21.
 */

@FragmentWithArgs public class SignUpPersonalDetailFragment extends BaseFragment implements SignUpView<SignUpDetailResponse> {

	LinearLayout llStudentCall;
	LinearLayout llStudentMsg;
	TextView textSignJoin;
	TextView textSignCost;
	AutoLineGroup llSignInfoGroup;
	ImageView header;
	ImageView gender;
	TextView textName;
	TextView textPhone;

    @Arg String orderId;
	Toolbar toolbar;
	TextView toolbarTitile;
    @Inject SignUpDetailPresenter signUpDetailPresenter;
    RxPermissions rxPermissions;
    private SignUpDetailResponse.SignUpDetailBean bean;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_detail, container, false);
      llStudentCall = (LinearLayout) view.findViewById(R.id.ll_student_call);
      llStudentMsg = (LinearLayout) view.findViewById(R.id.ll_student_msg);
      textSignJoin = (TextView) view.findViewById(R.id.text_sign_joined);
      textSignCost = (TextView) view.findViewById(R.id.text_sign_info_cost);
      llSignInfoGroup = (AutoLineGroup) view.findViewById(R.id.ll_sign_info_group);
      header = (ImageView) view.findViewById(R.id.header);
      gender = (ImageView) view.findViewById(R.id.gender);
      textName = (TextView) view.findViewById(R.id.name);
      textPhone = (TextView) view.findViewById(R.id.phone);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.ll_student_call).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onStartInfo(v);
        }
      });
      view.findViewById(R.id.ll_student_msg).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onStartInfo(v);
        }
      });

      delegatePresenter(signUpDetailPresenter, this);
        initToolbar(toolbar);
        toolbarTitile.setText("详情 ");

        llSignInfoGroup.setSpacing(16, 10);
        llSignInfoGroup.getLayoutParams().height = MeasureUtils.dpToPx(24f, getResources());

        rxPermissions = new RxPermissions(getActivity());

        getData();

        return view;
    }

    private void getData() {
        signUpDetailPresenter.querySignDetail(orderId);
    }

 public void onStartInfo(View v) {
        switch (v.getId()) {
            case R.id.ll_student_call:
                new MaterialDialog.Builder(getContext()).autoDismiss(true)
                    .content(new StringBuilder().append("确定呼叫号码\n").append(bean.getPhone()).append("吗？").toString())
                    .positiveText(R.string.common_comfirm)
                    .negativeText(R.string.common_cancel)
                    .autoDismiss(true)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            rxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(new Action1<Boolean>() {
                                @Override public void call(Boolean aBoolean) {
                                    if (aBoolean) {
                                        Uri uri = Uri.parse(new StringBuilder().append("tel:").append(bean.getPhone()).toString());
                                        Intent dialntent = new Intent(Intent.ACTION_DIAL, uri);
                                      dialntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(dialntent);
                                    } else {
                                        ToastUtils.show("请在应用设置中开启拨打电话权限");
                                    }
                                }
                            });
                        }
                    })
                    .show();
                break;
            case R.id.ll_student_msg:
                rxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(new Action1<Boolean>() {
                    @Override public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            AppUtils.doSendSMSTo(getContext(), bean.getPhone());
                        } else {
                            ToastUtils.show("请在应用设置中开启发短信权限");
                        }
                    }
                });

                break;
        }
    }

    @Override public void onGetSignUpDataSuccess(SignUpDetailResponse data) {
        setData(data);
    }

    @Override public void onFailed(String msg) {
        ToastUtils.show(msg);
    }

    @Override public void onSuccess() {

    }

    @Override public void onDelSuccess() {

    }

    private void setData(SignUpDetailResponse data) {
        this.bean = data.signUpDetailBean;
        getChildFragmentManager().beginTransaction()
            .replace(R.id.personal_count_frag, RankCountFragment.newInstance(bean.attendance))
            .commit();

        textSignJoin.setText(
            getString(R.string.sign_up_personal_time, DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(bean.created_at))));
        textSignCost.setText(getString(R.string.sign_up_personal_cost, bean.getOrderPrice()));

        llSignInfoGroup.removeAllViews();
        for (GroupBean groupBean : bean.teams) {
            TextView textView = new TextView(getContext());
            ContextCompat.getDrawable(getContext(), R.drawable.ic_form_group)
                .setBounds(0, 0, ContextCompat.getDrawable(getContext(), R.drawable.ic_form_group).getMinimumWidth(),
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_form_group).getMinimumHeight());
            textView.setPadding(MeasureUtils.dpToPx(8f, getContext().getResources()), 6,
                MeasureUtils.dpToPx(8f, getContext().getResources()), 6);
            textView.setCompoundDrawables(ContextCompat.getDrawable(getContext(), R.drawable.ic_form_group), null, null, null);
            textView.setCompoundDrawablePadding(MeasureUtils.dpToPx(8f, getResources()));
            textView.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_border_cornor_grey));
            textView.setText(groupBean.name);
            textView.setTextSize(12);
            llSignInfoGroup.addView(textView);
        }

        llSignInfoGroup.invalidate();
        Glide.with(getContext()).load(PhotoUtils.getSmall(bean.avatar())).asBitmap().into(new CircleImgWrapper(header, getContext()));

        textName.setText(bean.username());

        gender.setImageDrawable(bean.gender().equals("0") ? getResources().getDrawable(R.drawable.ic_gender_signal_male)
            : getResources().getDrawable(R.drawable.ic_gender_signal_female));

        textPhone.setText(bean.phone());
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return SignUpPersonalDetailFragment.class.getName();
    }
}
