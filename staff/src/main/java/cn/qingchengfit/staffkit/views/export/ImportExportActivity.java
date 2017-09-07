package cn.qingchengfit.staffkit.views.export;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.activity.BaseActivity;

/**
 * Created by fb on 2017/9/6.
 */

public class ImportExportActivity extends BaseActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_import_export);

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.frag_import_export, new ImportExportFragment())
        .commit();
  }
}
