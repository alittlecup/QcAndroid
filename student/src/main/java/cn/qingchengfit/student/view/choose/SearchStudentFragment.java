package cn.qingchengfit.student.view.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.student.R;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

@Leaf(module = "student", path = "/search/student/") public class SearchStudentFragment
    extends ChooseAndSearchStudentFragment {
  @Need Boolean addAble = true;

  @Override public void onStudentList(List<QcStudentBean> stus) {
    if (llSearchAll != null) {
      if (!TextUtils.isEmpty(etSearch.getText().toString())
          && etSearch.getText().toString().length() >= 4) {
        llSearchAll.setVisibility(View.GONE);
      } else {
        llSearchAll.setVisibility(View.VISIBLE);
      }
    }
    super.onStudentList(stus);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (addAble) {
      addStudent.setVisibility(View.VISIBLE);
    } else {
      addStudent.setVisibility(View.GONE);
      toolbar.getMenu().clear();
    }
    if(chooseStudentListFragment!=null){
      chooseStudentListFragment.setOnItemClickListener(new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int position) {
          StudentItem item = chooseStudentListFragment.getItem(position);
          if(item!=null){
            QcStudentBean qcStudentBean = item.getQcStudentBean();
            routeTo();
          }
          return false;
        }
      });
    }
  }

  @Override protected void filterText(String text) {
    if (!TextUtils.isEmpty(text) && text.length() >= 4) {
      super.filterText(text);
      llSearchAll.setVisibility(View.GONE);
    } else {
      llSearchAll.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(text)) {
      super.filterText("");
    }
  }
}
