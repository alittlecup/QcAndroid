package cn.qingchengfit.staffkit.views.export;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;

/**
 * Created by fb on 2017/9/7.
 */

public class CardImportExportFragment extends ImportExportFragment {

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    tvStudentExport.setText(getResources().getString(R.string.text_card_export));
    tvStudentImport.setText(getResources().getString(R.string.text_card_import));
    toolbarTitle.setText(getResources().getString(R.string.toolbar_import_export_card));
    return view;
  }

  @Override public void onClickImport() {
    Intent toScan = new Intent(getActivity(), QRActivity.class);
    toScan.putExtra(QRActivity.LINK_URL, Configs.Server
        + "app2web/?id="
        + gymWrapper.id()
        + "&model="
        + gymWrapper.model()
        + "&module="
        + QRActivity.CARD_IMPORT);
    startActivity(toScan);
  }

  @Override public void onClickExport() {
    presenter.qcPostExport(CARD_EXPORT_STR);
  }
}
