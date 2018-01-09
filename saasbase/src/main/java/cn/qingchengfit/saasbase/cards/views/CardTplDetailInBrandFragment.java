package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    isEnable(true);
    return view;
  }



}
