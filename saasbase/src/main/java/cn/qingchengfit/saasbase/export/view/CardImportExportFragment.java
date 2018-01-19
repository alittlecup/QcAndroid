package cn.qingchengfit.saasbase.export.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.qrcode.views.QRActivity;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "export",path = "/card/")
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
    QRActivity.start(getContext(),"/cards/import");
  }

  @Override public void onClickExport() {
    presenter.qcPostExport(CARD_EXPORT_STR);
  }
}