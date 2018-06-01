package cn.qingchengfit.staffkit.views.export;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.export.model.ExportRecord;
import cn.qingchengfit.staffkit.views.export.presenter.ImportExportPresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/9/6.
 */

public class ExportSendEmailFragment extends BaseFragment implements ImportExportPresenter.MVPView {

	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout toolbarLayout;
	TextView tvDownloadContent;
	TextView tvDownloadInfo;
	EditText editTargetEmail;
	TextView tvSendEmail;
  @Inject ImportExportPresenter presenter;
  private ExportRecord record;

  public static ExportSendEmailFragment newInstance(ExportRecord record) {
    Bundle args = new Bundle();
    args.putParcelable("record", record);
    ExportSendEmailFragment fragment = new ExportSendEmailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      record = getArguments().getParcelable("record");
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_download_export, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    tvDownloadContent = (TextView) view.findViewById(R.id.tv_download_content);
    tvDownloadInfo = (TextView) view.findViewById(R.id.tv_download_info);
    editTargetEmail = (EditText) view.findViewById(R.id.edit_target_email);
    tvSendEmail = (TextView) view.findViewById(R.id.tv_send_email);
    view.findViewById(R.id.tv_send_email).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        send();
      }
    });

    delegatePresenter(presenter, this);
    setToolbar();
    initView();
    return view;
  }

  private void setToolbar() {
    initToolbar(toolbar);
    toolbarTitle.setText(getString(R.string.toolbar_send_email));
  }

  private void initView() {
    tvDownloadContent.setText(DateUtils.getFileNameFormServer(record.file_name));
    tvDownloadInfo.setText(DateUtils.replaceTFromServer(record.created_at) + " " + record.created_by.username);
  }

 public void send() {
    if (TextUtils.isEmpty(editTargetEmail.getText()) || !StringUtils.isEmail(editTargetEmail.getText().toString())){
      DialogUtils.showAlert(getContext(), "请填写正确的邮箱格式");
      return;
    }
    presenter.qcSendEmail(record.excel_url, editTargetEmail.getText().toString());
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onExportRecord(List<ExportRecord> record) {

  }

  @Override public void onExportSuccess() {

  }

  @Override public void onSendEmailSuccess() {
    ToastUtils.show("邮件发送成功");
    getActivity().onBackPressed();
  }
}
