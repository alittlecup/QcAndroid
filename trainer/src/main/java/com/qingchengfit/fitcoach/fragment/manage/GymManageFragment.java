package com.qingchengfit.fitcoach.fragment.manage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.Utils;

public class GymManageFragment extends BaseFragment {
  TextView tvOpen;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View root = inflater.inflate(R.layout.fragment_gym_manage, container, false);
    Toolbar toolbar = root.findViewById(R.id.toolbar);
    initToolbar(toolbar);
    TextView title = root.findViewById(R.id.toolbar_title);
    title.setText("场馆管理");
    tvOpen = root.findViewById(R.id.tv_open_staff);
    tvOpen.setOnClickListener(v -> {
      try {
        Utils.openApp(getActivity());
      } catch (Exception e) {
        try {
          Intent i = new Intent(Intent.ACTION_VIEW);
          i.setData(Uri.parse("http://fir.im/qcfit"));
          i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(i);
        } catch (Exception e2) {

        }
      }
    });
    return root;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    toolbar.setNavigationIcon(cn.qingchengfit.widgets.R.drawable.vd_navigate_before_white_24dp);
    toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
  }
}