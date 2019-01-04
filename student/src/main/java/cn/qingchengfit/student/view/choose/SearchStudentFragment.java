package cn.qingchengfit.student.view.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.events.EventSelectedStudent;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "student", path = "/search/student/") public class SearchStudentFragment
    extends ChooseAndSearchStudentFragment {
  @Need public Boolean addAble = true;
  @Need public ArrayList<QcStudentBean> selectedStudent;
  View root;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (root == null) {
      SearchStudentParams.inject(this);
      root = super.onCreateView(inflater, container, savedInstanceState);
      srl.setRefreshing(false);
      llSearchAll.setVisibility(View.VISIBLE);
      etSearch.setHint("会员姓名，手机号");
      etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
      etSearch.setSingleLine(true);
      etSearch.setOnKeyListener(new View.OnKeyListener() {
        @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_SEARCH||keyCode==KeyEvent.KEYCODE_ENTER) {
            srl.setRefreshing(true);
            onSearch(etSearch.getText().toString());
          }
          return false;
        }
      });
    }

    if (!addAble) {
      toolbarTitle.setText("会员卡续卡");
      searchText.setText("搜索需要续卡的会员");
    }
    chooseStudentListFragment.setSelctedStudents(selectedStudent);
    return root;
  }

  private void onSearch(String text) {
    if (TextUtils.isEmpty(text)) {
      ToastUtils.show("会员姓名或手机号不能为空");
    } else {
      presenter.loadStudentByPhoneStart(text);
    }
  }

  @Override public void onStudentList(List<QcStudentBean> stus) {
    if (llSearchAll != null) {
      llSearchAll.setVisibility(View.GONE);
      if (chooseStudentListFragment != null && chooseStudentListFragment.isAdded()) {
        List<QcStudentBean> selectedStudent = chooseStudentListFragment.getSelectedStudent();
        if (selectedStudent != null && !selectedStudent.isEmpty()) {
          List<QcStudentBean> qcStudentBeans = chooseStudentListFragment.getQcStudentBeans();
          for (QcStudentBean qcStudentBean : selectedStudent) {
            if (!qcStudentBeans.contains(qcStudentBean)) {
              qcStudentBeans.add(qcStudentBean);
            }
          }
          chooseStudentListFragment.setSelctedStudents(qcStudentBeans);
        }
      }
      super.onStudentList(stus);
    }
  }

  @Override public void postBackSelectedStudent() {
    List<QcStudentBean> selectedStudent = chooseStudentListFragment.getSelectedStudent();
    List<QcStudentBean> qcStudentBeans = chooseStudentListFragment.getQcStudentBeans();
    if (qcStudentBeans != null && !qcStudentBeans.isEmpty()) {
      for (QcStudentBean studentBean : qcStudentBeans) {
        if (!selectedStudent.contains(studentBean)) {
          selectedStudent.add(studentBean);
        }
      }
    }
    RxBus.getBus().post(new EventSelectedStudent(selectedStudent, source));
    popBack();
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    chooseStudentListFragment.changeOrderType(1);
    chooseStudentListFragment.setAlphabetViewVisible(false);
  }

  @Override public void onResume() {
    super.onResume();
    if (chooseStudentListFragment != null) {
      chooseStudentListFragment.setAlphabetViewVisible(false);
    }
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (addAble) {
      addStudent.setVisibility(View.VISIBLE);
    } else {
      addStudent.setVisibility(View.GONE);
      toolbar.getMenu().clear();
    }
    if (chooseStudentListFragment != null) {
      chooseStudentListFragment.setOnItemClickListener(new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
          StudentItem item = chooseStudentListFragment.getItem(position);
          if (item != null) {
            QcStudentBean qcStudentBean = item.getQcStudentBean();
            Bundle bundle = new Bundle();
            bundle.putParcelable("qcStudentBean", qcStudentBean);
            String qcCallId = getActivity().getIntent().getStringExtra("qcCallId");
            if (!TextUtils.isEmpty(qcCallId)) {
              bundle.putString("qcCallId", qcCallId);
            }
            routeTo("/student/card/list", bundle);
          }
          return false;
        }
      });
    }
  }

  @Override public void onRefresh() {
    onSearch(etSearch.getText().toString());
  }
}
