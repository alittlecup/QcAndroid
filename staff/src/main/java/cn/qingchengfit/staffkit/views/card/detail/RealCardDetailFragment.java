package cn.qingchengfit.staffkit.views.card.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.card.FixRealCardBindStudentFragment;
import cn.qingchengfit.staffkit.views.card.FixRealcardNumFragment;
import cn.qingchengfit.staffkit.views.card.FixRealcardStudentFragment;
import cn.qingchengfit.staffkit.views.card.charge.CardFixValidDayFragment;
import cn.qingchengfit.staffkit.views.card.charge.CardRefundFragment;
import cn.qingchengfit.staffkit.views.card.charge.RealValueCardChargeFragment;
import cn.qingchengfit.staffkit.views.card.offday.OffDayListFragment;
import cn.qingchengfit.staffkit.views.card.spendrecord.SpendRecordFragment;
import cn.qingchengfit.staffkit.views.custom.DialogSheet;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.utils.CardBusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.ConnerTag;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * Created by Paper on 16/3/18 2016.
 */
public class RealCardDetailFragment extends BaseFragment implements RealCardDetailView {

  @BindView(R.id.cardname) TextView cardname;
  @BindView(R.id.divider) View divider;
  @BindView(R.id.bundle_students) TextView bundleStudents;
  @BindView(R.id.usage_period) TextView usagePeriod;
  @BindView(R.id.balance) TextView balance;
  @BindView(R.id.registe_mode) View registeMode;
  @BindView(R.id.realcard_no) TextView realcardNo;
  @BindView(R.id.text_consume_detail) TextView textConsumeDetail;
  @BindView(R.id.card_status) ConnerTag cardStatus;
  @BindView(R.id.btn_charge) LinearLayout btnCharge;
  @BindView(R.id.btn_spend) LinearLayout btnSpend;
  @BindView(R.id.ask_offday) LinearLayout askOffday;
  @BindView(R.id.resume_card) LinearLayout resumeCard;
  @BindView(R.id.layout_card) CardView card;
  @BindView(R.id.title_balance) TextView textTitleBalance;

  @BindView(R.id.support_gyms) TextView supportGyms;
  @BindView(R.id.card_bg) RelativeLayout cardBg;

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  @Inject RealCardDetailPresenter presenter;
  @Inject RealcardWrapper realCard;
  @BindView(R.id.frame_bg_card_detail) FrameLayout frameBgCardDetail;
  @BindView(R.id.ll_card_protocol) RelativeLayout llCardProtocol;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_realcard_detail, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    mCallbackActivity.setToolbar("会员卡详情", false, null, 0, null);
    onGetRealCardDetail(realCard.getRealCard());
    presenter.queryGetCardDetail();

    return view;
  }

  @Override public void onDestroyView() {
    presenter.unattachView();
    super.onDestroyView();
  }

  @OnClick({
      R.id.ll_bind_student, R.id.ll_support_gym, R.id.ll_consume_record, R.id.ll_card_number
  }) public void onSelected(View view) {
    switch (view.getId()) {
      //绑定会员
      case R.id.ll_bind_student:
        if (!serPermisAction.check(PermissionServerUtils.MANAGE_COSTS)) {
          showAlert(R.string.alert_permission_forbid);
          return;
        }
        if (gymWrapper.inBrand()) {
          if (realCard.getRealCard().getShopIds() != null) {
            if (realCard.getRealCard().getShopIds().size() > 1) {
              MutiChooseGymFragment.start(RealCardDetailFragment.this, true,
                  (ArrayList<String>) realCard.getRealCard().getShopIds(),
                  PermissionServerUtils.MANAGE_COSTS_CAN_CHANGE, 3);
            } else {
              CoachService gym = GymBaseInfoAction.getGymByShopIdNow(
                  PreferenceUtils.getPrefString(getContext(), Configs.CUR_BRAND_ID, ""),
                  realCard.getRealCard().getShopIds().get(0));
              if (gym != null && !TextUtils.isEmpty(gym.getId()) && !TextUtils.isEmpty(
                  gym.getModel())) {
                mCallbackActivity.onChangeFragment(new FixRealCardBindStudentFragment());
              } else {
                ToastUtils.show(getString(R.string.toast_no_gym_permission));
              }
            }
          }
        } else {
          mCallbackActivity.onChangeFragment(new FixRealCardBindStudentFragment());
        }
        break;
      //查看消费记录
      case R.id.ll_consume_record:
        mCallbackActivity.onChangeFragment(new SpendRecordFragment());
        break;
      //修改实体卡号
      case R.id.ll_card_number:
        if (!serPermisAction.check(PermissionServerUtils.MANAGE_COSTS_CAN_CHANGE)) {
          showAlert(R.string.alert_permission_forbid);
          return;
        }
        mCallbackActivity.onChangeFragment(new FixRealcardNumFragment());
        break;
      case R.id.ll_support_gym:
        showAlert("请在会员卡种类中修改使用场馆");
        break;
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1) {
        //Shop shops = (Shop) IntentUtils.getParcelable(data);
        //if (shops != null) {
        //    getFragmentManager().beginTransaction()
        //        .replace(mCallbackActivity.getFragId(), RealValueCardChargeFragment.newInstance(shops.id))
        //        .addToBackStack(getFragmentName())
        //        .commit();
        //} else {
        //    ToastUtils.show("您没有这个健身房权限");
        //}
      } else if (requestCode == 2) {
        Shop shops = (Shop) IntentUtils.getParcelable(data);
        if (shops != null) {
          //getFragmentManager().beginTransaction()
          //    .replace(mCallbackActivity.getFragId(), CardRefundFragment.newInstance(shops.id))
          //    .addToBackStack(getFragmentName())
          //    .commit();
        } else {
          ToastUtils.show("您没有这个健身房权限");
        }
      } else if (requestCode == 3) {
        Shop shops = (Shop) IntentUtils.getParcelable(data);
        if (shops != null) {
          CoachService service =
              GymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), shops.id);
          if (service != null) {
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(),
                    FixRealcardStudentFragment.newInstance(service.getId(), service.getModel(),
                        FixRealcardStudentFragment.BIND_STUDENT_LIST_DETAIL))
                .addToBackStack(getFragmentName())
                .commit();
          } else {
            ToastUtils.show("您没有这个健身房权限");
          }
        } else {
          ToastUtils.show("您没有这个健身房权限");
        }
      }
    }
  }

  /**
   * 暂时都屏蔽了多场馆的情况  没准什么时候回加回来呢？
   */
  @OnClick({
      R.id.btn_charge, R.id.btn_spend, R.id.ask_offday, R.id.resume_card, R.id.overflow_more
  }) public void onClick(View view) {
    if (SerPermisAction.checkNoOne(PermissionServerUtils.MANAGE_COSTS_CAN_CHANGE)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }

    switch (view.getId()) {
      case R.id.btn_charge:
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), new RealValueCardChargeFragment())
            .addToBackStack(getFragmentName())
            .commit();
        break;

      case R.id.btn_spend://扣费
        mCallbackActivity.onChangeFragment(new CardRefundFragment());
        break;
      case R.id.ask_offday://请假
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), new OffDayListFragment())
            .addToBackStack(getFragmentName())
            .commit();
        break;
      case R.id.resume_card:
        final MaterialDialog unregistmaterialDialog =
            new MaterialDialog.Builder(getContext()).content("确认恢复该会员卡吗?")
                .positiveColorRes(R.color.colorPrimary)
                .negativeColorRes(R.color.text_grey)
                .positiveText(R.string.common_comfirm)
                .negativeText(R.string.pickerview_cancel)
                .autoDismiss(true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                  @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                    showLoading();
                    presenter.resumeCard();
                  }
                })
                .build();
        unregistmaterialDialog.show();

        break;
      case R.id.overflow_more:
        final DialogSheet dialogSheet = new DialogSheet(getContext());
        dialogSheet.addButton(getString(R.string.unregiste_card), new View.OnClickListener() {

          @Override public void onClick(View v) {
            if (SerPermisAction.checkNoOne(PermissionServerUtils.MANAGE_COSTS_CAN_CHANGE)) {
              showAlert(R.string.alert_permission_forbid);
              return;
            }

            new MaterialDialog.Builder(getContext()).title(getString(R.string.unregiste_title))
                .content(getString(R.string.unregiste_text))
                .negativeText(R.string.pickerview_cancel)
                .positiveText(R.string.unregiste_Comfirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    presenter.unRegisteCard();
                    dialog.dismiss();
                  }
                })
                .cancelable(true)
                .autoDismiss(true)
                .build()
                .show();
          }
        });
        dialogSheet.addButton("修改会员卡有效期", new View.OnClickListener() {
          @Override public void onClick(View view) {
            mCallbackActivity.onChangeFragment(new CardFixValidDayFragment());
          }
        });
        dialogSheet.show();

        break;
    }
  }

  @Override public void onGetRealCardDetail(Card card) {
    cardname.setText(card.getName() + "(" + card.getId() + ")");
    bundleStudents.setText(card.getUsersStr());
    if (card.isCheck_valid()) {
      usagePeriod.setText("有效期:"
          + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getValid_from()))
          + "至"
          + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getValid_to())));
    } else {
      usagePeriod.setText("有效期:无");
    }
    //this.card.setCardBackgroundColor(ColorUtils.parseColor(card.getColor(), 200).getColor());
    //CompatUtils.setBg(cardBg, ColorUtils.parseColor(card.getColor(), 200));
    frameBgCardDetail.setBackground(
        DrawableUtils.generateBg(16, CardBusinessUtils.getDefaultCardbgColor(card.getType())));

    if (TextUtils.isEmpty(card.getCard_no())) {
      realcardNo.setText("无");
    } else {
      realcardNo.setText(card.getCard_no());
    }
    if (realCard.type() == Configs.CATEGORY_TIMES) {
      textTitleBalance.setText("剩余");
    }
    textConsumeDetail.setText(
        "累计充值" + card.getTotal_account() + "元, " + "累计消费" + (int) (card.getTotal_cost()) + "元");

    cardStatus.setVisibility(View.GONE);
    btnCharge.setVisibility(View.VISIBLE);
    btnSpend.setVisibility(View.VISIBLE);
    askOffday.setVisibility(View.VISIBLE);
    resumeCard.setVisibility(View.GONE);
    realCard.setRealCard(card);
    if (card.is_locked()) {
      cardStatus.setVisibility(View.VISIBLE);
      cardStatus.setBgColor(ContextCompat.getColor(getContext(), R.color.bg_card_off_day));
      cardStatus.setText(getString(R.string.has_day_off));
    }

    if (card.isExpired()) {
      cardStatus.setVisibility(View.VISIBLE);
      cardStatus.setBgColor(ContextCompat.getColor(getContext(), R.color.bg_card_out_of_day));
      cardStatus.setText(getString(R.string.has_expired));
    }
    if (!card.is_active()) {
      cardStatus.setVisibility(View.VISIBLE);
      cardStatus.setBgColor(ContextCompat.getColor(getContext(), R.color.bg_card_stop));
      cardStatus.setText(getString(R.string.has_stop_card));
      btnCharge.setVisibility(View.GONE);
      btnSpend.setVisibility(View.GONE);
      askOffday.setVisibility(View.GONE);
      resumeCard.setVisibility(View.VISIBLE);
    }
    supportGyms.setText(card.getSupportGyms());
    switch (realCard.type()) {
      case Configs.CATEGORY_VALUE:
      case Configs.CATEGORY_TIMES:
        if (card.isCheck_valid()) {
          usagePeriod.setText(
              DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getValid_from()))
                  + " 至 "
                  + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getValid_to())));
        } else {
          usagePeriod.setText(R.string.no_limit);
        }
        balance.setText(StringUtils.getBalance(getContext(), card.getBalance(), card.getType()));
        break;
      case Configs.CATEGORY_DATE:
        usagePeriod.setText(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getStart()))
            + " 至 "
            + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getEnd())));
        if (!card.isExpired()) {
          balance.setText(StringUtils.getBalance(getContext(), card.getBalance(), card.getType()));
        } else {
          balance.setText("已过期" + (-card.getTrial_days()) + "天");
        }
        break;
    }
  }

  @Override public void onSuccessUnregiste() {

    presenter.queryGetCardDetail();
  }

  @Override public void onSuccessResume() {
    hideLoading();
    presenter.queryGetCardDetail();
  }

  @Override public void onFailed(String s) {

  }

  @Override public String getFragmentName() {
    return RealCardDetailFragment.class.getName();
  }
}
