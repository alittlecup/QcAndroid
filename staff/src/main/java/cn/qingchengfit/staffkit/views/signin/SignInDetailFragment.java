package cn.qingchengfit.staffkit.views.signin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.rxbus.event.SignInCancelEvent;
import cn.qingchengfit.staffkit.views.custom.SimpleImgDialog;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 签到详情页面
 * Created by yangming on 16/8/30.
 */
public class SignInDetailFragment extends BaseFragment
    implements SignInDetailPresenter.SignInDetailView {

  @Inject SignInDetailPresenter presenter;
  ImageView imgSignoutItemFace;
  TextView tvSignoutItemName;
  ImageView ivSignoutItemGender;
  TextView tvSignoutItemPhone;
  TextView tvSignoutItemCard;
  TextView tvSignoutItemFee;
  TextView tvSignoutItemGui;
  ImageView imageView;
  TextView tvSigninInTime;
  TextView tvSigninInOprator;
  TextView tvSigninOutTime;
  TextView tvSigninOutOprator;
  LinearLayout llSigninDetailCancel;
  TextView tvSigninDetailCancel;

  ImageView ivSigninDetailOutIcon;

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  @Inject StudentAction studentAction;
  private int checkInId = 0;
  private SignInTasks.SignInTask mSignInTask;

  public SignInDetailFragment() {
  }

  public static SignInDetailFragment newInstance(int checkInId) {
    SignInDetailFragment fragment = new SignInDetailFragment();
    Bundle args = new Bundle();
    args.putInt("id", checkInId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    checkInId = getArguments().getInt("id");
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signin_detail, container, false);
    initToolbar(view.findViewById(R.id.toolbar));
    ((TextView) view.findViewById(R.id.toolbar_title)).setText(R.string.sign_in_detail_title);
    imgSignoutItemFace = (ImageView) view.findViewById(R.id.img_signout_item_face);
    tvSignoutItemName = (TextView) view.findViewById(R.id.tv_signout_item_name);
    ivSignoutItemGender = (ImageView) view.findViewById(R.id.iv_signout_item_gender);
    tvSignoutItemPhone = (TextView) view.findViewById(R.id.tv_signout_item_phone);
    tvSignoutItemCard = (TextView) view.findViewById(R.id.tv_signout_item_card);
    tvSignoutItemFee = (TextView) view.findViewById(R.id.tv_signout_item_fee);
    tvSignoutItemGui = (TextView) view.findViewById(R.id.tv_signout_item_gui);
    imageView = (ImageView) view.findViewById(R.id.imageView);
    tvSigninInTime = (TextView) view.findViewById(R.id.tv_signin_in_time);
    tvSigninInOprator = (TextView) view.findViewById(R.id.tv_signin_in_oprator);
    tvSigninOutTime = (TextView) view.findViewById(R.id.tv_signin_out_time);
    tvSigninOutOprator = (TextView) view.findViewById(R.id.tv_signin_out_oprator);
    llSigninDetailCancel = (LinearLayout) view.findViewById(R.id.ll_signin_detail_cancel);
    tvSigninDetailCancel = (TextView) view.findViewById(R.id.tv_signin_detail_cancel);
    ivSigninDetailOutIcon = (ImageView) view.findViewById(R.id.iv_signin_detail_out_icon);
    view.findViewById(R.id.img_signout_item_face).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFace();
      }
    });
    view.findViewById(R.id.ll_signin_detail_cancel).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SignInDetailFragment.this.onClick(v);
      }
    });

    //
    delegatePresenter(presenter, this);

    mCallbackActivity.setToolbar(getString(R.string.sign_in_detail_title), false, null, 0, null);

    Observable<SignInCancelEvent> observable = RxBusAdd(SignInCancelEvent.class);
    observable.subscribe(new Action1<SignInCancelEvent>() {
      @Override public void call(SignInCancelEvent signInCancelEvent) {
        presenter.cancelSignIn(String.valueOf(signInCancelEvent.getCheckInId()));
      }
    });
    RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
      @Override public void call(EventChooseImage eventChooseImage) {
        showLoading();
        RxRegiste(UpYunClient.rxUpLoad("/signin/", eventChooseImage.filePath)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<String>() {
              @Override public void call(String s) {
                Glide.with(getContext())
                    .load(PhotoUtils.getSmall(s))
                    .asBitmap()
                    .placeholder(R.drawable.img_default_checkinphoto)
                    .into(imgSignoutItemFace);

                presenter.changeImage(s, mSignInTask.getUserId());
                studentAction.updateStudentCheckin(mSignInTask.getUserId(), s);
                hideLoading();
              }
            }));
      }
    });
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    showLoading();
    presenter.queryData(checkInId + "");
  }

  @Override public String getFragmentName() {
    return SignInDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void onClickFace() {
    if (mSignInTask != null) {
      if (StringUtils.isEmpty(mSignInTask.getCheckinAvatar())) {
        if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
          showAlert(R.string.alert_permission_forbid);
          return;
        }

        ChoosePictureFragmentDialog dialog = ChoosePictureFragmentDialog.newInstance(true);
        dialog.show(getFragmentManager(), "");
      } else {
        SimpleImgDialog.newInstance(mSignInTask.getCheckinAvatar()).show(getFragmentManager(), "");
      }
    }
  }

  @Override public void cancelComplete() {
    tvSigninDetailCancel.setText("已撤销");
    Resources resource = (Resources) getActivity().getResources();
    ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.text_color_gray);
    tvSigninDetailCancel.setTextColor(csl);
    llSigninDetailCancel.setClickable(false);
  }

  @Override public void onData(SignInTasks.SignInTask signInTask) {
    hideLoading();
    mSignInTask = signInTask;
    if (signInTask == null) {
      ToastUtils.show("详情信息请求失败");
      llSigninDetailCancel.setClickable(false);
      return;
    }
    tvSignoutItemName.setText(signInTask.getUserName());
    tvSignoutItemPhone.setText(
        getActivity().getString(R.string.sign_in_untreated_phone, signInTask.getUserPhone()));
    ivSignoutItemGender.setImageResource(
        signInTask.getUserGender() == 0 ? R.drawable.ic_gender_signal_male
            : R.drawable.ic_gender_signal_female);
    Glide.with(getActivity())
        .load(PhotoUtils.getSmall(signInTask.getCheckinAvatar()))
        .asBitmap()
        .placeholder(R.drawable.img_default_checkinphoto)
        .into(imgSignoutItemFace);
    String balance = "";
    if (signInTask.getCard().getCardType() != null && signInTask.getCard().getBalance() != null) {
      if (signInTask.getCard().getCardType() == Configs.CATEGORY_VALUE) {
        tvSignoutItemFee.setVisibility(View.VISIBLE);
        balance = TextUtils.concat("(余额:", String.valueOf(signInTask.getCard().getBalance()), "元)")
            .toString();
      } else if (signInTask.getCard().getCardType() == Configs.CATEGORY_TIMES) {
        tvSignoutItemFee.setVisibility(View.VISIBLE);
        balance = TextUtils.concat("(余额:", String.valueOf(signInTask.getCard().getBalance()), "次)")
            .toString();
      } else {
        tvSignoutItemFee.setVisibility(View.GONE);
      }
    }

    StringBuffer payString = new StringBuffer();
    SignInTasks.Card card = signInTask.getCard();
    if (card != null && card.getId() != null && card.getId() >= 1) {
      payString.append(signInTask.getCard().getName()).append(balance);
      tvSignoutItemCard.setText(
          getActivity().getString(R.string.sign_in_untreated_card, payString.toString()));
    } else {
      payString.append("支付方式:");
      if (signInTask.getOrder() != null) {
        String channel = signInTask.getOrder().getChannel();
        if (!TextUtils.isEmpty(channel)) {
          if (channel.equals("WEIXIN")) {
            payString.append("微信支付");
          }
        }
      }
      tvSignoutItemCard.setText(payString.toString());
    }
    tvSignoutItemFee.setText(getActivity().getString(R.string.sign_in_untreated_fee,
        signInTask.getCost() + signInTask.getUnit()));

    tvSignoutItemGui.setText(getActivity().getString(R.string.sign_out_untreated_gui,
        TextUtils.isEmpty(signInTask.getLocker().name) ? "" : signInTask.getLocker().name));

    tvSigninInTime.setText(getActivity().getString(R.string.sign_in_log_in,
        signInTask.getCreatedAt().replace("T", " ")));
    tvSigninInOprator.setText(
        getActivity().getString(R.string.sign_in_log_opretor, signInTask.getCreatedByName()));

    if (TextUtils.isEmpty(signInTask.getCheckoutAt())) {
      tvSigninOutTime.setText("暂未签出");
      ivSigninDetailOutIcon.setImageResource(R.drawable.ic_sign_out_nomarl);
      tvSigninOutOprator.setVisibility(View.GONE);
    } else {
      tvSigninOutTime.setText(getActivity().getString(R.string.sign_in_log_out,
          signInTask.getCheckoutAt().replace("T", " ")));
      tvSigninOutOprator.setText(getActivity().getString(R.string.sign_in_log_opretor,
          signInTask.getCheckoutBy().getUsername()));
    }

    if (signInTask.getStatus() == 1) {//已撤销
      tvSigninDetailCancel.setText("已撤销");
      Resources resource = (Resources) getActivity().getResources();
      ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.text_color_gray);
      tvSigninDetailCancel.setTextColor(csl);
      llSigninDetailCancel.setClickable(false);
    }
  }

  public void onClick(View view) {
    if (!serPermisAction.check(PermissionServerUtils.CHECKIN_LIST_CAN_DEL)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }

    CancelDialogFragment fragment = CancelDialogFragment.newInstance(checkInId);
    fragment.show(getFragmentManager(), "");
  }
}
