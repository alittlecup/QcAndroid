package cn.qingchengfit.student.view.choose;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.QcStudentBean;

import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.events.EventSaasFresh;
import cn.qingchengfit.saascommon.events.EventSelectedStudent;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.widgets.CompatEditView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.SelectableAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

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
    extends SaasCommonFragment
    implements ChooseAndSearchPresenter.MVPView, SwipeRefreshLayout.OnRefreshListener {
  Toolbar toolbar;
  TextView toolbarTitle;
  FrameLayout toolbarLayout;
  CompatEditView etSearch;
  SwipeRefreshLayout srl;
  LinearLayout llSearchAll;
  TextView searchText;
  ImageView addStudent;

  ChooseStudentListFragment chooseStudentListFragment;
  @Inject ChooseAndSearchPresenter presenter;
  @Inject IPermissionModel permissionModel;
  @Need public ArrayList<String> studentIdList;
  @Need public String source;
  @Need public Integer chooseType;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ChooseAndSearchStudentParams.inject(this);
    if (chooseType == null || chooseType == 0) chooseType = SelectableAdapter.Mode.MULTI;
    chooseStudentListFragment = ChooseStudentListFragment.newInstance(chooseType);
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
    View view = inflater.inflate(R.layout.st_fragment_student_choose, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    etSearch = (CompatEditView) view.findViewById(R.id.et_search);
    srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    llSearchAll = view.findViewById(R.id.ll_search_all);
    addStudent = view.findViewById(R.id.btn_add_student);
    searchText = view.findViewById(R.id.tv_search_text);
    view.findViewById(R.id.btn_add_student).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnAddStudentClicked();
      }
    });

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    RxTextView.afterTextChangeEvents(etSearch)
        .throttleLast(1000, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(textViewAfterTextChangeEvent -> {
          filterText(etSearch.getText().toString());
        });
    srl.setRefreshing(true);
    srl.setOnRefreshListener(this);
    return view;
  }

  protected void filterText(String text) {
    if (chooseStudentListFragment != null
        && chooseStudentListFragment.isAdded()
        && etSearch != null) {
      chooseStudentListFragment.filter(text);
    }
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
        postBackSelectedStudent();
        return false;
      }
    });
  }

  public void postBackSelectedStudent() {
    RxBus.getBus()
        .post(new EventSelectedStudent(chooseStudentListFragment.getSelectedStudent(), source));
    popBack();
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
  public void onBtnAddStudentClicked() {
    if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
      QcRouteUtil.setRouteOptions(new RouteOptions("staff").setActionName("/add/student")).call();
    } else {
      DialogUtils.showAlert(getContext(),
          getResources().getString(R.string.alert_permission_forbid));
    }
  }

  @Override public void onStudentList(List<QcStudentBean> stus) {
    srl.setRefreshing(false);
    if (chooseStudentListFragment != null && chooseStudentListFragment.isAdded()) {
      chooseStudentListFragment.setData(stus);
      chooseStudentListFragment.selectStudent(studentIdList);
    }
  }

  @Override public void onRefresh() {
    getData();
  }

  public void getData() {
    presenter.getAllStudents();
  }
}
