package cn.qingchengfit.staffkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.routers.studentImpl;
import cn.qingchengfit.staffkit.views.student.edit.EditStudentInfoFragment;

/**
 * Created by fb on 2018/1/9.
 */

public class CardStudentRouters extends studentImpl {

  @Override public Fragment toStudentAddFragment(Bundle args) {
    EditStudentInfoFragment fragment = new EditStudentInfoFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
