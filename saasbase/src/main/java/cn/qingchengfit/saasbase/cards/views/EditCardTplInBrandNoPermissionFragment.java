package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.presenters.CardBuyPresenter;
import cn.qingchengfit.saasbase.cards.presenters.CardTplDetailPresenter;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import dagger.multibindings.ElementsIntoSet;
import javax.inject.Inject;

/**
 * Created by fb on 2017/12/28.
 */

@Leaf(module = "card", path = "/path/edit/card/nopermission")
public class EditCardTplInBrandNoPermissionFragment extends SaasBaseFragment {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.tv_card_tpl_name_brand) TextView tvCardTplNameBrand;
  @BindView(R2.id.tv_card_tpl_desc) TextView tvCardTplDesc;
  @BindView(R2.id.tv_card_tpl_limit) TextView tvCardTplLimit;
  @BindView(R2.id.tv_card_tpl_term) TextView tvCardTplTerm;
  @BindView(R2.id.input_card_tpl_brand_term) CommonInputView inputCardTplBrandTerm;
  @BindView(R2.id.input_card_tpl_brand_support_gym) CommonInputView inputCardTplBrandSupportGym;
  @Inject CardTplDetailPresenter presenter;
  @Need CardTpl cardTpl;g


  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_edit_cardtpl_no_permission, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  private void initView(){
    initToolbar(toolbar);
    toolbarTitle.setText("编辑会员卡种类");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {

        return false;
      }
    });
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
