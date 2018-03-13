package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.PermissionServerUtils;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by fb on 2018/1/9.
 */

@Leaf(module = "card", path = "/cardtpl/detail/brand/")
public class CardTplDetailInBrandFragment extends CardTplDetailFragment {

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    supportGyms.setVisibility(View.VISIBLE);
    supportGyms.setContent(cardTpl.getShopIds().size() + "家");
    return view;
  }

  @Override public void onCheckPermission() {
    if (!permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE,
        cardTpl.getShopIds())) {
      isEnable(false);
    }else{
      isEnable(true);
    }
  }


  @Override public boolean hasAddPermission() {
    boolean ret = permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE,cardTpl.getShopIds());
    if (!ret) {
      showAlert("您没有会员卡新增权限");
    }
    return ret;
  }

  @Override public boolean hasAddPermission(boolean toast) {
    boolean ret = permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE,cardTpl.getShopIds());
    if (!ret && toast) {
      showAlert("您没有会员卡新增权限");
    }
    return ret;
  }
}
