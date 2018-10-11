package cn.qingchengfit.saasbase.export.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;



import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.export.bean.ExportRecord;
import cn.qingchengfit.saasbase.export.presenter.ImportExportPresenter;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.List;
import javax.inject.Inject;
@Leaf(module = "card",path = "/member/")
public class ImportExportFragment extends BaseFragment implements ImportExportPresenter.MVPView {

  public static final String STUDENT_EXPORT_STR = "member";
  public static final String CARD_EXPORT_STR = "member_card";

	public TextView tvStudentImport;
	public TextView tvStudentExport;
	Toolbar toolbar;
	public TextView toolbarTitle;
	FrameLayout toolbarLayout;

  @Inject public ImportExportPresenter presenter;
  @Inject IPermissionModel serPermisAction;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_import_export, container, false);
    tvStudentImport = (TextView) view.findViewById(R.id.tv_import);
    tvStudentExport = (TextView) view.findViewById(R.id.tv_export);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
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
    if (!serPermisAction.check(PermissionServerUtils.STUDENT_IMPORT)) {
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    onClickImport();
  }

  public void onClickImport() {
    //Intent toScan = new Intent(getActivity(), QRActivity.class);
    //toScan.putExtra(QRActivity.LINK_URL, Configs.Server
    //  + "app2web/?id="
    //  + gymWrapper.id()
    //  + "&model="
    //  + gymWrapper.model()
    //  + "&module="
    //  + GymFunctionFactory.STUDENT_IMPORT);
    //startActivity(toScan);
    QRActivity.start(getContext(),"/manage/members/import");
  }

 public void onExport() {
    if (!serPermisAction.check(PermissionServerUtils.STUDENT_EXPORT)) {
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

  @Override public void onExportRecord(List<ExportRecord> record) {

  }

  @Override public void onExportSuccess() {
    DialogUtils.instanceDelDialog(getContext(),
      getResources().getString(R.string.tip_dialog_success_title),
      getResources().getString(R.string.tip_dialog_success_content), null).show();
  }

  @Override public void onSendEmailSuccess() {

  }
}