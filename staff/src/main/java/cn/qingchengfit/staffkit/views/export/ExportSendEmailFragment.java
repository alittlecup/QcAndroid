package cn.qingchengfit.staffkit.views.export;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.tv_download_content) TextView tvDownloadContent;
  @BindView(R.id.tv_download_info) TextView tvDownloadInfo;
  @BindView(R.id.edit_target_email) EditText editTargetEmail;
  @BindView(R.id.tv_send_email) TextView tvSendEmail;
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
    unbinder = ButterKnife.bind(this, view);
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
    tvDownloadContent.setText(record.file_name);
    tvDownloadInfo.setText(DateUtils.formatDateFromServer(record.created_at) + " " + record.created_by.username);
  }

  @OnClick(R.id.tv_send_email) public void send() {
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
    ToastUtils.show("发送成功");
    getActivity().onBackPressed();
  }
}
