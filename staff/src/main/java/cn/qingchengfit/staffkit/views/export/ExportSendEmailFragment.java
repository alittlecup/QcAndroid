package cn.qingchengfit.staffkit.views.export;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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
import cn.qingchengfit.views.fragments.BaseFragment;

/**
 * Created by fb on 2017/9/6.
 */

public class ExportSendEmailFragment extends BaseFragment {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.tv_download_content) TextView tvDownloadContent;
  @BindView(R.id.tv_download_info) TextView tvDownloadInfo;
  @BindView(R.id.edit_target_email) EditText editTargetEmail;
  @BindView(R.id.tv_send_email) TextView tvSendEmail;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_download_export, container, false);
    unbinder = ButterKnife.bind(this, view);
    setToolbar();
    return view;
  }

  private void setToolbar(){
    initToolbar(toolbar);
    toolbarTitle.setText(getString(R.string.toolbar_send_email));
  }

  @OnClick() public void send(){

  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
