package cn.qingchengfit.student.view.export;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.student.R;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PermissionUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/9/6.
 */
@Leaf(module = "student", path = "/student/export") public class ImportExportFragment
    extends BaseFragment implements ImportExportPresenter.MVPView {

  public static final String STUDENT_EXPORT_STR = "member";
  public static final String CARD_EXPORT_STR = "member_card";

  @Inject GymWrapper gymWrapper;
  @Inject IPermissionModel permissionModel;
  public TextView tvStudentImport;
  public TextView tvStudentExport;
  @Inject public ImportExportPresenter presenter;
  Toolbar toolbar;

  public TextView toolbarTitle;
  FrameLayout toolbarLayout;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_import_export, container, false);
    tvStudentImport = view.findViewById(R.id.tv_import);
    tvStudentExport = view.findViewById(R.id.tv_export);
    toolbar = view.findViewById(R.id.toolbar);
    toolbarTitle = view.findViewById(R.id.toolbar_title);
    toolbarLayout = view.findViewById(R.id.toolbar_layout);
    view.findViewById(R.id.tv_import).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onImport();
      }
    });
    view.findViewById(R.id.tv_export).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onExport();
      }
    });

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    toolbarTitle.setText(getResources().getString(R.string.toolbar_import_export_student));
    return view;
  }

  public void onImport() {
    if (!permissionModel.check(PermissionServerUtils.STUDENT_IMPORT)) {
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    onClickImport();
  }

  public void onClickImport() {
    Intent toScan = new Intent(getActivity(), QRActivity.class);
    toScan.putExtra(QRActivity.LINK_URL, Configs.Server
        + "app2web/?id="
        + gymWrapper.id()
        + "&model="
        + gymWrapper.model()
        + "&module="
        + QRActivity.STUDENT_IMPORT);
    startActivity(toScan);
  }

  public void onExport() {
    if (!permissionModel.check(PermissionServerUtils.STUDENT_EXPORT)) {
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    onClickExport();
  }

  public void onClickExport() {
    presenter.qcPostExport(STUDENT_EXPORT_STR);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onExportSuccess() {
    DialogUtils.instanceDelDialog(getContext(),
        getResources().getString(R.string.tip_dialog_success_title),
        getResources().getString(R.string.tip_dialog_success_content), null).show();
  }
}
