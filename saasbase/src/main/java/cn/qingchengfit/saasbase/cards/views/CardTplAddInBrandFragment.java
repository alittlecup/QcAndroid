package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by fb on 2017/12/19.
 */

@Leaf(module = "card", path = "/cardtpl/add/brand")
public class CardTplAddInBrandFragment extends CardtplAddFragment {

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    supportGyms.setVisibility(View.VISIBLE);
  }
}
