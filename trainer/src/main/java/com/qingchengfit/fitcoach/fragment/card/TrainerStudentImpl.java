package com.qingchengfit.fitcoach.fragment.card;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.qingchengfit.student.routers.studentImpl;
import cn.qingchengfit.student.view.choose.SearchStudentFragment;
import cn.qingchengfit.student.view.choose.TrainerChooseAndSearchFragment;

public class TrainerStudentImpl extends studentImpl {
  @Override public Fragment toChooseAndSearchStudentFragment(Bundle args) {
    TrainerChooseAndSearchFragment fragment = new TrainerChooseAndSearchFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public Fragment toSearchStudentFragment(Bundle args) {
    TrainerChooseAndSearchFragment fragment = new TrainerChooseAndSearchFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
