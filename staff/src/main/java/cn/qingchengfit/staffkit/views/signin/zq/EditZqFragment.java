package cn.qingchengfit.staffkit.views.signin.zq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.staffkit.views.signin.zq.model.AccessBody;

/**
 * Created by fb on 2017/9/27.
 */

public class EditZqFragment extends AddZqFragment {

  private String guardId;

  public static EditZqFragment newInstance(AccessBody body, String guardId) {
    Bundle args = new Bundle();
    args.putParcelable("access", body);
    args.putString("guard", guardId);
    EditZqFragment fragment = new EditZqFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null){
      body = getArguments().getParcelable("access");
      guardId = getArguments().getString("guard");
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    toolbarTitle.setText("编辑门禁");
    initView();
    return view;
  }

  private void initView(){
    if (body != null) {
      inputGymName.setContent(body.name);
      inputGymAddress.setContent(body.device_id);
      inputGymFun.setContent(body.behavior == 1 ? "签到" : "签出");
      inputGymStart.setContent(body.start);
      inputGymEnd.setContent(body.end);
    }
  }

  @Override public void save() {
    presenter.editZqAccess(guardId, body);
  }
}
