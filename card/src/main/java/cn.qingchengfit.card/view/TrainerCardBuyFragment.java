package cn.qingchengfit.card.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TrainerCardBuyFragment extends StaffCardBuyFragment {
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    civSaler.setShowRight(false);
    civSaler.setContent(loginStatus.getLoginUser().getUsername());
    civSaler.setCanClick(false);
    presenter.setSaler(loginStatus.getLoginUser().getId());
    return view;
  }
}
