package cn.qingchengfit.staffkit.views.export;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.activity.BaseActivity;

/**
 * Created by fb on 2017/9/6.
 */

public class ImportExportActivity extends BaseActivity {

  public static final int TYPE_RECORD = 101;
  public static final int TYPE_EXPORT = 102;
  public static final int TYPE_CARD = 103;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_import_export);

    if (getIntent() != null) {
      if (getIntent().getIntExtra("type", 0) == TYPE_EXPORT) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.frag_import_export, new ImportExportFragment())
            .commit();
      } else if (getIntent().getIntExtra("type", 0) == TYPE_RECORD) {
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
            .replace(R.id.frag_import_export, new ExportRecordFragment())
            .commit();
      }else if (getIntent().getIntExtra("type", 0) == TYPE_CARD) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.frag_import_export, new CardImportExportFragment())
            .commit();
      }
    }
  }
}
