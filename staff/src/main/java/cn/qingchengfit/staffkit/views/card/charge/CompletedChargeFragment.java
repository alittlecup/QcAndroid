package cn.qingchengfit.staffkit.views.card.charge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.ChargeBody;
import cn.qingchengfit.model.responese.ImageThreeTextBean;
import cn.qingchengfit.saasbase.cards.views.WriteDescFragment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.card.detail.RealCardDetailFragment;
import cn.qingchengfit.staffkit.views.cardtype.CardProtocolActivity;
import cn.qingchengfit.staffkit.views.custom.BottomPayDialog;
import cn.qingchengfit.staffkit.views.custom.BottomPayDialogBuilder;
import cn.qingchengfit.staffkit.views.custom.SimpleChooseFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ColorUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.RealCardUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

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
 * <p/>
 * Created by Paper on 16/3/23 2016.
 */
public class CompletedChargeFragment extends BaseFragment implements CompletedChargeView {
  @BindView(R.id.cardname) TextView cardname;
  @BindView(R.id.cardid) TextView cardid;
  @BindView(R.id.valid_date) TextView validDate;
  @BindView(R.id.students) TextView students;
  @BindView(R.id.realcard_num) TextView realcardNum;
  @BindView(R.id.balance) TextView balance;
  @BindView(R.id.pay_money) TextView payMoney;
  @BindView(R.id.comfirm) Button comfirm;

  @Inject RealcardWrapper realCard;
  @BindView(R.id.sale) CommonInputView sale;
  @BindView(R.id.mark) CommonInputView mark;
  @BindView(R.id.pay_method) CommonInputView payMethod;
  @BindView(R.id.card) CardView card;
  @BindView(R.id.card_bg) LinearLayout cardBg;
  List<Staff> mSalers;
  @Inject CompletedChargePresenter presenter;
  @BindView(R.id.input_card_protocol) CommonInputView inputCardProtocol;
  private ChargeBody chargeBody;

  public static CompletedChargeFragment newInstance(ChargeBody body) {

    Bundle args = new Bundle();
    args.putParcelable("body", body);
    CompletedChargeFragment fragment = new CompletedChargeFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      chargeBody = getArguments().getParcelable("body");
    }
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_complete_charge, container, false);
    unbinder = ButterKnife.bind(this, view);
    mCallbackActivity.setToolbar("完善信息", false, null, 0, null);
    cardname.setText(realCard.name());
    if (chargeBody.isCheck_valid()) {
      validDate.setText("有效期: " + chargeBody.getValid_from() + "至" + chargeBody.getValid_to());
    } else {
      validDate.setText("有效期: 不限");
    }
    students.setText("绑定会员: " + realCard.getRealCard().getUsersStr());
    ColorDrawable colorDrawable =
        new ColorDrawable(ColorUtils.parseColor(realCard.getRealCard().getColor()));
    colorDrawable.setAlpha(200);
    CompatUtils.setBg(cardBg, colorDrawable);
    card.setCardBackgroundColor(
        ColorUtils.parseColor(realCard.getRealCard().getColor(), 200).getColor());

    switch (realCard.type()) {
      case Configs.CATEGORY_VALUE:
      case Configs.CATEGORY_TIMES:
        balance.setText(RealCardUtils.getCardBlance(realCard.getRealCard()));
        break;
      case Configs.CATEGORY_DATE:
        balance.setText(
            (DateUtils.dayNumFromToday(DateUtils.formatDateFromYYYYMMDD(chargeBody.getEnd())) + 1)
                + "天");
        validDate.setText(
            DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromYYYYMMDD(chargeBody.getStart()))
                + "至"
                + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromYYYYMMDD(chargeBody.getEnd())));
        break;
    }
    setProtocol();
    payMoney.setText(chargeBody.getPrice() + "元");
    presenter.attachView(this);
    chargeBody.setCharge_type(7);
    comfirm.requestFocus();
    AppUtils.hideKeyboard(getActivity());

    return view;
  }

  private void setProtocol() {
    if (realCard.getRealCard().is_open_service_term) {
      inputCardProtocol.setVisibility(View.VISIBLE);
    } else {
      inputCardProtocol.setVisibility(View.GONE);
    }
  }

  @OnClick(R.id.input_card_protocol) public void onOpenProtocol() {
    CardProtocolActivity.startWeb(realCard.getRealCard().card_tpl_service_term.content_link,
        getContext(), false, "");
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick({ R.id.sale, R.id.mark, R.id.pay_method, R.id.comfirm }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.sale: //销售
        presenter.getSalers(chargeBody.getShop_id());
        break;
      case R.id.mark:
        WriteDescFragment.start(this, 2, "备注信息", "请填写备注信息");
        break;
      case R.id.pay_method:
        BottomPayDialog f = new BottomPayDialogBuilder(presenter.hasEditPermission()).build();
        f.setTargetFragment(this, 3);
        f.show(getFragmentManager(), "");
        break;
      case R.id.comfirm://发送信息,生成code
        if (sale.isEmpty()) {
          ToastUtils.show("请选择销售");
          return;
        }
        //                chargeBody.setApp_id(getString(R.string.wechat_code));
        showLoading();
        chargeBody.setOrigin(2);
        presenter.commitCharge(chargeBody,
            StringUtils.List2Str(realCard.getRealCard().getUserIds()));
        break;
    }
  }

  /**
   * (1, '现金'),  pos  2
   * (2, '刷卡支付'),      3
   * (3, '转账'),         4
   * (4, '其他'),        5
   * (5, '赠送'),
   * (7 "wx          0
   * (6, '在线支付'),     ,1
   */
  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 2) {//备注信息
        if (TextUtils.isEmpty(IntentUtils.getIntentString(data))) {
          mark.setContent("未填写");
        } else {
          mark.setContent("已填写");
          chargeBody.setRemarks(IntentUtils.getIntentString(data));
        }
      } else if (requestCode == 1) {//选择销售
        int pos = Integer.parseInt(IntentUtils.getIntentString(data));
        if (pos > 0) {
          sale.setContent(mSalers.get(pos - 1).username);
          chargeBody.setSeller_id(mSalers.get(pos - 1).id);
        } else {
          sale.setContent(getString(R.string.no_saler));
          chargeBody.setSeller_id(null);
        }
      } else if (requestCode == 3) {//付款方式
        int p = Integer.parseInt(IntentUtils.getIntentString(data));
        try {
          payMethod.setContent(getResources().getStringArray(R.array.pay_method)[p]);
          switch (p) {
            case 0:
              chargeBody.setCharge_type(7);
              break;
            case 1:
              chargeBody.setCharge_type(6);
              break;
            case 2:
              chargeBody.setCharge_type(1);
              break;
            case 3:
              chargeBody.setCharge_type(2);
              break;
            case 4:
              chargeBody.setCharge_type(3);
              break;
            case 5:
              chargeBody.setCharge_type(4);
              break;
          }
        } catch (Exception e) {
          Timber.e(e.getMessage());
        }
      } else if (requestCode == 404) {
        //判断是否充值完成
        if (data != null) {

          //int ret = data.getIntExtra(IntentUtils.RESULT, -1);
          //if (ret == 0) {
            //                        onSuccess();
            presenter.cacluScore(chargeBody.getPrice(),
                StringUtils.List2Str(realCard.getRealCard().getUserIds()));
          //} else {
          //  ToastUtils.show("充值失败");
          //}
        }
      }
    }
  }

  @Override public String getFragmentName() {
    return CompletedChargeFragment.class.getName();
  }

  @Override public void onSalers(List<Staff> salers) {
    mSalers = salers;
    ArrayList<ImageThreeTextBean> d = new ArrayList<>();
    d.add(new ImageThreeTextBean("", "无销售", "", ""));
    for (Staff saler : salers) {
      d.add(new ImageThreeTextBean(saler.avatar, saler.username, "", ""));
    }
    SimpleChooseFragment.start(this, 1, "选择销售", d);
  }

  @Override public void onSuccess() {
    hideLoading();
    ToastUtils.show("充值成功");
    //mCallbackActivity.cleanToolbar();
    //getFragmentManager().popBackStack(RealCardDetailFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    getFragmentManager().popBackStack(null, 1);
    getFragmentManager().beginTransaction()
        .replace(mCallbackActivity.getFragId(), new RealCardDetailFragment())
        .commitAllowingStateLoss();
  }

  @Override public void onFailed(String s) {
    hideLoading();
    ToastUtils.show(s);
  }

  @Override public void onWechat(String url) {

    //        WebActivity.startWebForResult(url, getContext(),404);
    Intent toWeb = new Intent(getContext(), WebActivity.class);
    toWeb.putExtra("url", url);
    this.startActivityForResult(toWeb, 404);
    hideLoading();
  }

  @Override public void onScoreHint(String s) {
    hideLoading();
    new MaterialDialog.Builder(getContext()).autoDismiss(true)
        .canceledOnTouchOutside(false)
        .title("充值成功!")
        .positiveText(R.string.common_comfirm)
        .content(getString(R.string.caclu_score_hint, s))
        .cancelable(false)
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            onSuccess();
          }
        })
        .show();
  }
}
