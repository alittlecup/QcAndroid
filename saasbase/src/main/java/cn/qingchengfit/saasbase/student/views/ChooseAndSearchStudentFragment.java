package cn.qingchengfit.saasbase.student.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.saasbase.events.EventSelectedStudent;
import cn.qingchengfit.saasbase.student.presenters.ChooseAndSearchPresenter;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.widgets.CompatEditView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.trello.rxlifecycle.android.FragmentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/8/29.
 */

@Leaf(module = "student", path = "/choose/student/") public class ChooseAndSearchStudentFragment
  extends SaasBaseFragment
  implements ChooseAndSearchPresenter.MVPView, SwipeRefreshLayout.OnRefreshListener {
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.et_search) CompatEditView etSearch;

  private ChooseStudentListFragment chooseStudentListFragment;
  @Inject ChooseAndSearchPresenter presenter;
  @Need public ArrayList<String> studentIdList;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    chooseStudentListFragment = new ChooseStudentListFragment();
    RxBus.getBus()
      .register(EventSaasFresh.StudentList.class)
      .compose(this.<EventSaasFresh.StudentList>bindToLifecycle())
      .compose(this.<EventSaasFresh.StudentList>doWhen(FragmentEvent.CREATE_VIEW))
      .subscribe(new BusSubscribe<EventSaasFresh.StudentList>() {
        @Override public void onNext(EventSaasFresh.StudentList studentList) {
          onRefresh();
        }
      });
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_student_choose, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    RxTextView.afterTextChangeEvents(etSearch)
      .throttleLast(1000, TimeUnit.MILLISECONDS)
      .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
        @Override public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
          if (chooseStudentListFragment != null && chooseStudentListFragment.isAdded()) {
            chooseStudentListFragment.filter(etSearch.getText().toString());
          }
        }
      });
    return view;
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
    Bundle savedInstanceState) {
    if (f instanceof ChooseStudentListFragment) {
      onRefresh();
    }
  }

  @Override public int getLayoutRes() {
    return R.id.frag_student_list;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(chooseStudentListFragment);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("选择会员");
    toolbar.getMenu().clear();
    toolbar.getMenu().add("确定").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        RxBus.getBus()
          .post(new EventSelectedStudent(chooseStudentListFragment.getSelectedStudent()));
        getActivity().onBackPressed();
        return false;
      }
    });
  }

  @Override public String getFragmentName() {
    return ChooseAndSearchStudentFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 新增会员
   */
  @OnClick(R2.id.btn_add_student) public void onBtnAddStudentClicked() {
    routeTo("/add/", null);
  }

  @Override public void onStudentList(List<QcStudentBean> stus) {
    if (chooseStudentListFragment != null && chooseStudentListFragment.isAdded()) {
      chooseStudentListFragment.setData(stus);
      if (studentIdList != null) {
        chooseStudentListFragment.selectStudent(studentIdList);
      }
    }
  }

  @Override public void onRefresh() {
    presenter.getAllStudents();
  }
  ///**
  // * 底部选择框
  // */
  //@OnClick(R2.id.ll_show_select) public void onLlShowSelectClicked() {
  //
  //}
}
