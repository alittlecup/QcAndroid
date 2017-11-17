package cn.qingchengfit.staffkit.views.cardtype.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.body.CardtplBody;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rxbus.event.EventLimitBuyCount;
import cn.qingchengfit.staffkit.views.bottom.BottomBuyLimitFragment;
import cn.qingchengfit.staffkit.views.cardtype.standard.CardStandardFragment;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.staffkit.views.gym.WriteDescFragment;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 16/3/16 2016.
 */
public class EditCardTypeFragment extends BaseFragment implements EditCardTypeView {

  @BindView(R.id.type) CommonInputView type;
  @BindView(R.id.desc) CommonInputView desc;
  //@BindView(R.id.switch_name) TextView name;
  @BindView(R.id.switcher) ExpandedLayout switcher;
  @BindView(R.id.pre_order_count) CommonInputView preOrderCount;
  @BindView(R.id.during_count) CommonInputView duringCount;
  @BindView(R.id.set_card_standard) CommonInputView setCardStandard;
  @BindView(R.id.cardname) CommonInputView cardname;
  @BindView(R.id.support_gyms) CommonInputView supportGyms;
  @BindView(R.id.support_gyms_layout) LinearLayout supportGymsLayout;

  @BindView(R.id.cardtpl_info_edit1) LinearLayout cardtplInfoEdit1;
  @BindView(R.id.cardtpl_info_show) LinearLayout cardtplInfoShow;
  //@BindView(R.id.cardtpl_info_edit2) LinearLayout cardtplInfoEdit2;
  @BindView(R.id.show_name) TextView showName;
  @BindView(R.id.show_desc) TextView showDesc;
  @BindView(R.id.show_limit) TextView showLimit;
  @BindView(R.id.limit_bug_count) CommonInputView mCivLimitBugCount;
  List<CoachService> mCoachServices;
  @Inject EditCardTypePresenter presenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @BindView(R.id.expand_card_protocol) ExpandedLayout expandCardProtocol;
  private CardTpl card_tpl;
  private int mType = 0;//0 是新建 1 是修改
  private CardtplBody body = new CardtplBody();
  private Toolbar.OnMenuItemClickListener menuListener = new Toolbar.OnMenuItemClickListener() {
    @Override public boolean onMenuItemClick(MenuItem item) {
      onComfirm();
      return true;
    }
  };

  public static EditCardTypeFragment newInstance(int type) {

    Bundle args = new Bundle();
    args.putInt("type", type);
    EditCardTypeFragment fragment = new EditCardTypeFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static EditCardTypeFragment newInstance(CardTpl card_tpl) {

    Bundle args = new Bundle();
    args.putInt("type", 1);
    args.putParcelable("cardtpl", card_tpl);
    EditCardTypeFragment fragment = new EditCardTypeFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getResources() != null) {
      mType = getArguments().getInt("type", 0);
      card_tpl = getArguments().getParcelable("cardtpl");
    }
    //
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_edit_cardtype, container, false);
    unbinder = ButterKnife.bind(this, view);

    presenter.attachView(this);
    if (mType != 0) {//
      mCallbackActivity.setToolbar(getString(R.string.title_cardtype_edit), false, null,
          SerPermisAction.checkMuti(PermissionServerUtils.CARDSETTING_CAN_CHANGE,
              card_tpl.getShopIds()) ? R.menu.menu_save : 0, menuListener);
      type.setVisibility(View.GONE);
      if (card_tpl.has_service_term){

      }else{

      }
    } else {
      mCallbackActivity.setToolbar(getString(R.string.title_cardtype_add), false, null,
          R.menu.menu_save, menuListener);
      expandCardProtocol.setEnabled(false);
    }

    //name.setText("设置限制条件");
    switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          body.is_limit = true;
          preOrderCount.setVisibility(View.VISIBLE);
          duringCount.setVisibility(View.VISIBLE);
          mCivLimitBugCount.setVisibility(View.VISIBLE);
        } else {
          body.is_limit = false;
          preOrderCount.setVisibility(View.GONE);
          duringCount.setVisibility(View.GONE);
          mCivLimitBugCount.setVisibility(View.GONE);
        }
      }
    });
    switcher.setExpanded(false);
    body.is_limit = false;
    onCardTpl(card_tpl);

    if (!gymWrapper.inBrand()) {
      supportGymsLayout.setVisibility(View.GONE);
    }

    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });

    RxBusAdd(EventLimitBuyCount.class).subscribe(new Action1<EventLimitBuyCount>() {
      @Override public void call(EventLimitBuyCount eventLimitBuyCount) {
        body.buy_limit = eventLimitBuyCount.buy_count;
        mCivLimitBugCount.setContent(eventLimitBuyCount.text);
      }
    });

    return view;
  }

  @Override public void onDestroyView() {
    presenter.unattachView();
    super.onDestroyView();
  }

  @Override public void onEditSucceess() {
    hideLoading();
    getActivity().onBackPressed();
  }

  @Override public void onEditFailed(String s) {
    hideLoading();
    ToastUtils.show(s);
  }

  @Override
  public void onGetCardTypeInfo(String name, String id, String content, String lim, String desc,
      String t, String valid, String charge, String in) {
    cardname.setContent(name);
    type.setContent(t);
  }

  @Override public void onCardTpl(CardTpl card_tpl) {
    if (card_tpl != null) {

      if (SerPermisAction.checkMuti(PermissionServerUtils.CARDSETTING_CAN_CHANGE,
          card_tpl.getShopIds())) {
        cardtplInfoEdit1.setVisibility(View.VISIBLE);
        //cardtplInfoEdit2.setVisibility(View.VISIBLE);
        cardtplInfoShow.setVisibility(View.GONE);

        cardname.setContent(card_tpl.getName());
        type.setContent(getResources().getStringArray(R.array.cardtype)[card_tpl.getType() - 1]);
        body.name = card_tpl.getName();
        body.type = card_tpl.getType();
        body.id = card_tpl.getId();
        body.shops = StringUtils.List2Str(card_tpl.getShopIds());
        supportGyms.setContent(card_tpl.getShopIds().size() + "家");

        if (card_tpl.is_limit()) {
          body.is_limit = true;
          body.pre_times = card_tpl.getPre_times();
          body.buy_limit = card_tpl.getBuy_limit();
          switcher.setExpanded(true);
          preOrderCount.setContent(card_tpl.getPre_times() + "");
          mCivLimitBugCount.setContent(
              card_tpl.getBuy_limit() == 0 ? getString(R.string.buy_card_no_limit)
                  : getString(R.string.card_count_unit, card_tpl.getBuy_limit()));
          if (card_tpl.getMonth_times() > 0) {
            duringCount.setContent("每月," + card_tpl.getMonth_times() + "节");
            body.month_times = card_tpl.getMonth_times();
          } else if (card_tpl.getWeek_times() > 0) {
            duringCount.setContent("每周," + card_tpl.getWeek_times() + "节");
            body.week_times = card_tpl.getWeek_times();
          } else {
            duringCount.setContent("每天," + card_tpl.getDay_times() + "节");
            body.day_times = card_tpl.getDay_times();
          }
        } else {
          switcher.setExpanded(false);
          body.is_limit = false;
        }
      } else {
        cardtplInfoEdit1.setVisibility(View.GONE);
        //cardtplInfoEdit2.setVisibility(View.GONE);
        cardtplInfoShow.setVisibility(View.VISIBLE);
        showName.setText(card_tpl.getName() == null ? "" : card_tpl.getName());
        showDesc.setText(card_tpl.getDescription());
        showLimit.setText(card_tpl.getLimit());
        supportGyms.setContent(card_tpl.getShopIds().size() + "家");
        if (!gymWrapper.inBrand()) {
          mCallbackActivity.setToolbar(getString(R.string.title_cardtype_edit), false, null, 0,
              null);
        }
      }
    } else {
      if (!gymWrapper.inBrand()) {
        body.shops = gymWrapper.shop_id();
      }
    }
  }

  @Override public void onSuccessShops() {
    hideLoading();
    ToastUtils.show("修改场馆成功!");
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case 0:

          break;
        case 1://类型
          type.setContent(getResources().getStringArray(R.array.cardtype)[Integer.parseInt(
              IntentUtils.getIntentString(data))]);
          body.type = Integer.parseInt(IntentUtils.getIntentString(data)) + 1;
          break;
        case 2://简介
          desc.setContent("已填写");
          body.description = IntentUtils.getIntentString(data);
          break;
        case 3://设置限制条件
          int circ_type = Integer.parseInt(IntentUtils.getIntentString(data, 0));
          int circ_time = Integer.parseInt(IntentUtils.getIntentString(data, 1));
          duringCount.setContent(getResources().getStringArray(R.array.class_frequent)[circ_type]
              + ","
              + circ_time
              + "次");
          if (circ_type == 0) {
            body.day_times = circ_time;
          } else if (circ_type == 1) {
            body.week_times = circ_time;
          } else if (circ_type == 2) {
            body.month_times = circ_time;
          }
          break;
        case 4:
          String ids = "";
          ArrayList<Shop> shops = data.getParcelableArrayListExtra(IntentUtils.RESULT);
          if (shops != null) {
            for (int i = 0; i < shops.size(); i++) {
              if (i < shops.size() - 1) {
                ids = TextUtils.concat(ids, shops.get(i).id, ",").toString();
              } else {
                ids = TextUtils.concat(ids, shops.get(i).id).toString();
              }
            }
            body.shops = ids;
            if (mType != 0) presenter.FixGyms(App.staffId, card_tpl.getId(), ids);
            supportGyms.setContent(shops.size() + "家");
          }
          break;

        default:
          break;
      }
    }
  }

  @OnClick({
      R.id.type, R.id.desc, R.id.pre_order_count, R.id.during_count, R.id.set_card_standard,
      R.id.support_gyms_layout, R.id.limit_bug_count
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.type:
        BottomSheetListDialogFragment.start(this, 1,
            getResources().getStringArray(R.array.cardtype));
        break;
      case R.id.desc:
        WriteDescFragment.start(this, 2, getString(R.string.title_cardtype_edit_desc), "请填写简介信息",
            card_tpl == null ? body.description
                : card_tpl.getDescription().substring(3, card_tpl.getDescription().length()));
        break;
      case R.id.pre_order_count:
        final DialogList list = new DialogList(getContext());
        list.list(1, 99, new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            list.dismiss();
            preOrderCount.setContent(Integer.toString(position + 1));
            body.pre_times = position + 1;
          }
        });
        list.show();
        break;
      case R.id.during_count:
        ClassLimitBottomFragment.start(this, 3);
        break;
      case R.id.limit_bug_count:
        BottomBuyLimitFragment.newInstance(body.buy_limit).show(getFragmentManager(), "");
        break;
      case R.id.set_card_standard:
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), new CardStandardFragment())
            .commit();
        break;
      case R.id.support_gyms_layout:
        if (card_tpl != null && card_tpl.getShopIds() != null) {
          MutiChooseGymFragment.start(EditCardTypeFragment.this, false,
              (ArrayList<String>) card_tpl.getShopIds(),
              mType == 0 ? PermissionServerUtils.CARDSETTING_CAN_WRITE
                  : PermissionServerUtils.CARDSETTING_CAN_CHANGE, 4);
        } else if (!TextUtils.isEmpty(body.shops)) {
          MutiChooseGymFragment.start(EditCardTypeFragment.this, false,
              (ArrayList<String>) StringUtils.Str2List(body.shops),
              mType == 0 ? PermissionServerUtils.CARDSETTING_CAN_WRITE
                  : PermissionServerUtils.CARDSETTING_CAN_CHANGE, 4);
        } else {
          MutiChooseGymFragment.start(EditCardTypeFragment.this, false, null,
              mType == 0 ? PermissionServerUtils.CARDSETTING_CAN_WRITE
                  : PermissionServerUtils.CARDSETTING_CAN_CHANGE, 4);
        }
        break;
      default:
        break;
    }
  }

  public void onComfirm() {
    if (TextUtils.isEmpty(cardname.getContent())) {
      ToastUtils.show("请填写名称");
      return;
    }
    body.name = cardname.getContent();
    if (body.type <= 0) {
      ToastUtils.show("请选择卡种类");
      return;
    }
    if (mType != 0) {
      showLoading();
      if (cardtplInfoEdit1.getVisibility() == View.VISIBLE) {
        presenter.editCardInfo(App.staffId, body);
      } else {
        CardtplBody shopsbody = new CardtplBody();
        shopsbody.shops = body.shops;
        presenter.editCardInfo(App.staffId, body);
      }
    } else {
      body.name = cardname.getContent();
      if (gymWrapper.inBrand() && TextUtils.isEmpty(body.shops)) {
        ToastUtils.show("请选择支持健身房");
        return;
      }
      showLoading();
      presenter.addCardInfo(App.staffId, body);
    }
  }

  @Override public String getFragmentName() {
    return EditCardTypeFragment.class.getName();
  }
}
