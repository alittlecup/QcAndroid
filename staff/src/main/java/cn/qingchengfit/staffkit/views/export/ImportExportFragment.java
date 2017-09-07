package cn.qingchengfit.staffkit.views.export;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.QRActivity;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;

/**
 * Created by fb on 2017/9/6.
 */

public class ImportExportFragment extends BaseFragment {

  @Inject GymWrapper gymWrapper;
  @BindView(R.id.tv_import) public TextView tvStudentImport;
  @BindView(R.id.tv_export) public TextView tvStudentExport;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_import_export, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.tv_import) public void onImport(){
    if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.STUDENT_IMPORT)) {
      showAlert(R.string.sorry_for_no_permission);
      return;
    }

    Intent toScan = new Intent(getActivity(), QRActivity.class);
    toScan.putExtra(QRActivity.LINK_URL, Configs.Server
        + "app2web/?id="
        + gymWrapper.id()
        + "&model="
        + gymWrapper.model()
        + "&module="
        + GymFunctionFactory.PERMISSION_STAFF);
    startActivity(toScan);
  }

  @OnClick(R.id.tv_export) public void onExport(){

  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
