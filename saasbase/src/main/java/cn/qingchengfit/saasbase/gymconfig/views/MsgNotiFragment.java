package cn.qingchengfit.saasbase.gymconfig.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.constant.ShopConfigs;
import cn.qingchengfit.saasbase.gymconfig.bean.ShopConfig;
import cn.qingchengfit.saasbase.gymconfig.network.response.ShopConfigBody;
import cn.qingchengfit.saasbase.gymconfig.presenter.MsgNotiPresenter;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.jakewharton.rxbinding.view.RxMenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@Leaf(module = "gym", path = "/msgnoti/") public class MsgNotiFragment extends SaasBaseFragment
  implements MsgNotiPresenter.MVPView {
	CommonInputView swBeginNotiMin;
	ExpandedLayout swBeginNoti;
	CommonInputView swLessNotiMin;
	ExpandedLayout swLessNoti;
	ExpandedLayout swNewNoti;
	ExpandedLayout swOrderNoti;
	ExpandedLayout swCancleNoti;
	ExpandedLayout swSeedToSubtitute;

  /**
   * 是否为私教
   */
  @Need public Boolean mIsPrivate;
  @Inject MsgNotiPresenter mMsgNotiPresenter;
	Toolbar toolbar;
	TextView toolbarTitile;

  private String mBeginNotiId;
  private String mBeginMinuteId;
  private String mLessNotiId;
  private String mLessNotMinuteId;
  private String mCancelId;
  private String mNewNotiId;
  private String mOrderNotiId;
  private String mSubstitueNotiId;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_saas_msg_noti, container, false);
    swBeginNotiMin = (CommonInputView) view.findViewById(R.id.sw_begin_noti_min);
    swBeginNoti = (ExpandedLayout) view.findViewById(R.id.sw_begin_noti);
    swLessNotiMin = (CommonInputView) view.findViewById(R.id.sw_less_noti_min);
    swLessNoti = (ExpandedLayout) view.findViewById(R.id.sw_less_noti);
    swNewNoti = (ExpandedLayout) view.findViewById(R.id.sw_new_noti);
    swOrderNoti = (ExpandedLayout) view.findViewById(R.id.sw_order_noti);
    swCancleNoti = (ExpandedLayout) view.findViewById(R.id.sw_cancle_noti);
    swSeedToSubtitute = (ExpandedLayout) view.findViewById(R.id.sw_seed_to_subtitute);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);

    delegatePresenter(mMsgNotiPresenter, this);
    initToolbar(toolbar);
    String courseTypeStr =
      getString(mIsPrivate ? R.string.course_type_private : R.string.course_type_group);

    swBeginNoti.setLabel(getString(R.string.msg_noti_before, courseTypeStr));
    if (mIsPrivate) {
      swLessNoti.setVisibility(View.GONE);
    } else {
      swLessNoti.setVisibility(View.VISIBLE);
      swLessNoti.setLabel(getString(R.string.msg_less_student));
    }
    swCancleNoti.setLabel(getString(R.string.msg_cancle_order_noti_student, courseTypeStr));
    swNewNoti.setLabel(getString(R.string.msg_new_noti_trainer));
    swOrderNoti.setLabel(getString(R.string.msg_help_order_noti_student, courseTypeStr));
    swSeedToSubtitute.setLabel(getString(R.string.msg_send_to_substitute));
    mMsgNotiPresenter.queryShopConfig(getConfigKeys());
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("预约短信通知");
    toolbar.inflateMenu(R.menu.menu_save);
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          onComfirm();
        }
      });
  }

  void onComfirm() {
    List<ShopConfigBody.Config> data = new ArrayList<>();
    data.add(new ShopConfigBody.Config(mBeginNotiId, swBeginNotiMin.getContent()));
    data.add(new ShopConfigBody.Config(mBeginMinuteId, swBeginNoti.isExpanded() ? "1" : "0"));
    if (!mIsPrivate) {
      data.add(new ShopConfigBody.Config(mLessNotiId, swLessNoti.isExpanded() ? "1" : "0"));
      data.add(new ShopConfigBody.Config(mLessNotMinuteId, swLessNotiMin.getContent()));
    }

    data.add(new ShopConfigBody.Config(mCancelId, swCancleNoti.isExpanded() ? "1" : "0"));
    // 有新预约时提醒教练
    data.add(new ShopConfigBody.Config(mNewNotiId, swNewNoti.isExpanded() ? "1" : "0"));
    data.add(new ShopConfigBody.Config(mOrderNotiId, swOrderNoti.isExpanded() ? "1" : "0"));
    //// TODO: 2017/1/18 候补发送
    //        data.add(new ShopConfigBody.Config(mIsPrivate?ShopConfigs.PRIVATE_SMS_USER_AFTER_TEACHER_ORDER_SAVE:ShopConfigs.TEAM_SMS,swSeedToSubtitute.isExpanded()?"1":"0"));
    ShopConfigBody body = new ShopConfigBody(data);
    mMsgNotiPresenter.editShopConfig(body);
  }

  private String getConfigKeys() {
    return (mIsPrivate ? ShopConfigs.PRIVATE_BEFORE_REMIND_USER_MINUTES
      : ShopConfigs.TEAM_BEFORE_REMIND_USER_MINUTES).concat(",")
      .concat(
        mIsPrivate ? ShopConfigs.PRIVATE_BEFORE_REMIND_USER : ShopConfigs.TEAM_BEFORE_REMIND_USER)
      .concat(",")
      .concat(mIsPrivate ? "" : ShopConfigs.TEAM_COURSE_REMIND_TEACHER)
      .concat(mIsPrivate ? "" : ",")
      .concat(mIsPrivate ? "" : ShopConfigs.TEAM_COURSE_REMIND_TEACHER_MINUTES)
      .concat(mIsPrivate ? "" : ",")
      .concat(mIsPrivate ? ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_CANCEL
        : ShopConfigs.TEAM_SMS_TEACHER_AFTER_USER_ORDER_CANCEL)
      .concat(",")
      .concat(mIsPrivate ? ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_SAVE
        : ShopConfigs.TEAM_SMS_TEACHER_AFTER_USER_ORDER_SAVE)
      .concat(",")
      .concat(mIsPrivate ? ShopConfigs.PRIVATE_SMS_USER_AFTER_TEACHER_ORDER_SAVE
        : ShopConfigs.TEAM_SMS_USER_AFTER_TEACHER_ORDER_SAVE);
  }

  @Override public String getFragmentName() {
    return MsgNotiFragment.class.getName();
  }

  @Override public void onShopConfigs(List<ShopConfig> configs) {
    if (configs != null) {
      for (ShopConfig c : configs) {
        switch (c.getKey()) {
          //提醒会员
          case ShopConfigs.PRIVATE_BEFORE_REMIND_USER_MINUTES:
          case ShopConfigs.TEAM_BEFORE_REMIND_USER_MINUTES:
            if (c.getValue() instanceof Double) {
              swBeginNotiMin.setContent(c.getValueInt() + "");
              mBeginNotiId = c.getId() + "";
            }
            break;
          case ShopConfigs.PRIVATE_BEFORE_REMIND_USER:
          case ShopConfigs.TEAM_BEFORE_REMIND_USER:
            if (c.getValue() instanceof Boolean) {
              swBeginNoti.setExpanded((Boolean) c.getValue());
              mBeginMinuteId = c.getId() + "";
            }
            break;
          //r人数不足提醒教练
          case ShopConfigs.TEAM_COURSE_REMIND_TEACHER:
            //case ShopConfigs.PRIVATE_COURSE_REMIND_TEACHER_MINUTES:
            if (c.getValue() instanceof Boolean) {
              swLessNoti.setExpanded((Boolean) c.getValue());
              mLessNotiId = c.getId() + "";
            }
            break;
          case ShopConfigs.TEAM_COURSE_REMIND_TEACHER_MINUTES:
            if (c.getValue() instanceof Double) {
              swLessNotiMin.setContent(c.getValueInt() + "");
              mLessNotMinuteId = c.getId() + "";
            }
            break;
          case ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_SAVE:
          case ShopConfigs.TEAM_SMS_TEACHER_AFTER_USER_ORDER_SAVE:
            if (c.getValue() instanceof Boolean) {
              swNewNoti.setExpanded((Boolean) c.getValue());
              mNewNotiId = c.getId() + " ";
            }
            break;
          case ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_CANCEL:
          case ShopConfigs.TEAM_SMS_TEACHER_AFTER_USER_ORDER_CANCEL:
            if (c.getValue() instanceof Boolean) {
              swCancleNoti.setExpanded((Boolean) c.getValue());
              mCancelId = c.getId() + "";
            }
            break;
          case ShopConfigs.PRIVATE_SMS_USER_AFTER_TEACHER_ORDER_SAVE:
          case ShopConfigs.TEAM_SMS_USER_AFTER_TEACHER_ORDER_SAVE:
            if (c.getValue() instanceof Boolean) {
              swOrderNoti.setExpanded((Boolean) c.getValue());
              mOrderNotiId = c.getId() + " ";
            }
            break;
        }
      }
    }
  }

  @Override public void onEditOk() {
    hideLoading();
    getActivity().onBackPressed();
  }

  @Override public void onShowError(String e) {
    hideLoading();
    ToastUtils.show(e);
  }

  @Override public void onShowError(@StringRes int e) {
    onShowError(getString(e));
  }
}
