package cn.qingchengfit.staffkit.student;

import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.staff.views.StaffAddFragment;
import cn.qingchengfit.saasbase.student.views.ChooseAndSearchStudentFragment;
import com.anbillon.flabellum.annotations.Trunk;

/**
 * Created by fb on 2017/12/18.
 */

@Trunk(fragments = {ChooseAndSearchStudentFragment.class, StaffAddFragment.class})
public class StaffStudentActivity extends SaasContainerActivity {
  @Override public String getModuleName() {
    return "student";
  }
}
