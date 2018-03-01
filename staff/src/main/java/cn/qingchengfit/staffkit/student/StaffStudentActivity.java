package cn.qingchengfit.staffkit.student;

import cn.qingchengfit.saasbase.SaasContainerActivity;
import com.anbillon.flabellum.annotations.Trunk;

/**
 * Created by fb on 2017/12/18.
 */

@Trunk(fragments = { })
public class StaffStudentActivity extends SaasContainerActivity {
  @Override public String getModuleName() {
    return "student";
  }
}
