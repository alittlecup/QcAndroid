package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import com.anbillon.flabellum.annotations.Leaf;
import javax.inject.Inject;

/**
 * Created by fb on 2018/1/9.
 */

@Leaf(module = "card", path = "/cardtpl/detail/brand/")
public class CardTplDetailInBrandFragment extends CardTplDetailFragment {

  @Inject IPermissionModel permissionModel;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    supportGyms.setVisibility(View.VISIBLE);
    supportGyms.setContent(cardTpl.getShopIds().size() + "家");


    return view;
  }

  @Override public void onCheckPermission() {
    if (!permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_CHANGE,
        cardTpl.getShopIds())) {
      isEnable(false);
      layoutCardDetail.setOnTouchListener(new View.OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
          layoutCardDetail.performClick();
          showAlert(getString(R.string.edit_cardtpl_in_brand_no_permission));
          return true;
        }
      });
    }else{
      isEnable(true);
    }
  }
}
