package cn.qingchengfit.saasbase.cards.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;



import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.presenters.CardTplDetailPresenter;
import cn.qingchengfit.saasbase.network.model.Shop;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * Created by fb on 2017/12/28.
 */

//多场馆下编辑无权限页面
@Leaf(module = "card", path = "/path/edit/card/nopermission")
public class EditCardTplInBrandNoPermissionFragment extends SaasBaseFragment {

	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout toolbarLayout;
	TextView tvCardTplNameBrand;
	TextView tvCardTplDesc;
	TextView tvCardTplLimit;
	TextView tvCardTplTerm;
	CommonInputView inputCardTplBrandTerm;
	CommonInputView inputCardTplBrandSupportGym;
  @Inject CardTplDetailPresenter presenter;
  @Need CardTpl cardTpl;
  private CardtplBody body = new CardtplBody();


  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_edit_cardtpl_no_permission, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    tvCardTplNameBrand = (TextView) view.findViewById(R.id.tv_card_tpl_name_brand);
    tvCardTplDesc = (TextView) view.findViewById(R.id.tv_card_tpl_desc);
    tvCardTplLimit = (TextView) view.findViewById(R.id.tv_card_tpl_limit);
    tvCardTplTerm = (TextView) view.findViewById(R.id.tv_card_tpl_term);
    inputCardTplBrandTerm = (CommonInputView) view.findViewById(R.id.input_card_tpl_brand_term);
    inputCardTplBrandSupportGym =
        (CommonInputView) view.findViewById(R.id.input_card_tpl_brand_support_gym);
    view.findViewById(R.id.input_card_tpl_brand_term)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            onTerm();
          }
        });
    view.findViewById(R.id.input_card_tpl_brand_support_gym)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            onSupportGym();
          }
        });

    initView();
    return view;
  }


  public void onTerm(){
    CardProtocolActivity.startWeb(cardTpl.card_tpl_service_term.content_link, getContext(), true,
        "", cardTpl);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK){
      switch (requestCode){
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
            //TODO 调这个接口什么意思
            //if (mType != 0) presenter.FixGyms(App.staffId, card_tpl.getId(), ids);
            inputCardTplBrandSupportGym.setContent(shops.size() + "家");
          }
          break;
      }
    }
  }


  public void onSupportGym(){
    if (!TextUtils.isEmpty(body.shops)) {
      MutiChooseGymFragment.start(EditCardTplInBrandNoPermissionFragment.this, false,
          (ArrayList<String>) StringUtils.Str2List(body.shops),
          PermissionServerUtils.CARDSETTING_CAN_CHANGE, 4);
    } else {
      MutiChooseGymFragment.start(EditCardTplInBrandNoPermissionFragment.this, false, null,
          PermissionServerUtils.CARDSETTING_CAN_WRITE, 4);
    }

  }

  private void initView(){
    initToolbar(toolbar);
    toolbarTitle.setText("编辑会员卡种类");
    toolbar.inflateMenu(R.menu.menu_save);
    tvCardTplNameBrand.setText(cardTpl.name);
    body.shops = StringUtils.List2Str(cardTpl.getShopIds());
    inputCardTplBrandSupportGym.setContent(cardTpl.getShopIds().size() + "家");
    tvCardTplDesc.setText(TextUtils.isEmpty(cardTpl.getDescription()) ? "无" : cardTpl.getDescription());
    if (cardTpl.is_open_service_term && cardTpl.card_tpl_service_term != null){
      inputCardTplBrandTerm.setVisibility(View.VISIBLE);
    }else{
      inputCardTplBrandTerm.setVisibility(View.GONE);
    }
    if (cardTpl.is_limit()) {
      StringBuilder sb = new StringBuilder();
      if (cardTpl.buy_limit > 0){
        sb.append(getResources().getString(R.string.buylimit, cardTpl.buy_limit));
      }
      if (cardTpl.day_times > 0){
        sb.append(getResources().getString(R.string.everyday, cardTpl.day_times));
      }
      if (cardTpl.week_times > 0){
        sb.append(getResources().getString(R.string.everyweek, cardTpl.week_times));
      }
      if (cardTpl.month_times > 0){
        sb.append(getResources().getString(R.string.everymonth, cardTpl.month_times));
      }
      tvCardTplLimit.setText(sb.toString());
    }
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        presenter.editCardTpl(body);
        return false;
      }
    });
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
