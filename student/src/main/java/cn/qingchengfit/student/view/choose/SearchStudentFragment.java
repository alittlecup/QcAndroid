package cn.qingchengfit.student.view.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.events.EventSelectedStudent;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.view.detail.StudentDetailWithCardPage;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;

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
      etSearch.setHint("手机号");
    }
    chooseStudentListFragment.setSelctedStudents(selectedStudent);
    return root;
  }

  @Override public void onStudentList(List<QcStudentBean> stus) {
    if (llSearchAll != null) {
      if (!TextUtils.isEmpty(etSearch.getText().toString())
          && etSearch.getText().toString().length() >= 4) {
        llSearchAll.setVisibility(View.GONE);
      } else {
        llSearchAll.setVisibility(View.VISIBLE);
      }
      if (chooseStudentListFragment != null && chooseStudentListFragment.isAdded()) {
        List<QcStudentBean> selectedStudent = chooseStudentListFragment.getSelectedStudent();
        if(selectedStudent!=null&&!selectedStudent.isEmpty()){
          List<QcStudentBean> qcStudentBeans = chooseStudentListFragment.getQcStudentBeans();
          for(QcStudentBean qcStudentBean:selectedStudent){
            if(!qcStudentBeans.contains(qcStudentBean)){
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
    if (llSearchAll != null) {
      if (!TextUtils.isEmpty(etSearch.getText().toString())
          && etSearch.getText().toString().length() >= 4) {
        presenter.loadStudentByPhoneStart(etSearch.getText().toString());
      } else {
        llSearchAll.setVisibility(View.VISIBLE);
        srl.setRefreshing(false);
      }
    }
  }

  @Override protected void filterText(String text) {
    if (!TextUtils.isEmpty(text)) {
      if (text.length() > 4) {
        super.filterText(text);
        llSearchAll.setVisibility(View.GONE);
      } else if (text.length() == 4) {
        srl.setRefreshing(true);
        presenter.loadStudentByPhoneStart(text);
      } else {
        llSearchAll.setVisibility(View.VISIBLE);
      }
    }
  }
}
