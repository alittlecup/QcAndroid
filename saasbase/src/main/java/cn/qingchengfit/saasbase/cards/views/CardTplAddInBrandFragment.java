package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.saasbase.network.model.Shop;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.List;

/**
 * Created by fb on 2017/12/19.
 */

@Leaf(module = "card", path = "/cardtpl/add/brand")
public class CardTplAddInBrandFragment extends CardtplAddFragment {

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    supportGyms.setVisibility(View.VISIBLE);
  }

  @Override public void onSelectSupportGyms(List<Shop> shops) {
    String ids = "";
    if (shops != null) {
      for (int i = 0; i < shops.size(); i++) {
        if (i < shops.size() - 1) {
          ids = TextUtils.concat(ids, shops.get(i).id, ",").toString();
        } else {
          ids = TextUtils.concat(ids, shops.get(i).id).toString();
        }
      }
      supportShopStr = ids;
      supportGyms.setContent(shops.size() + "家");
    }
  }
}
