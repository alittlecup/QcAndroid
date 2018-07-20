package cn.qingchengfit.staffkit.views.student.choose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saasbase.common.bottom.BottomStudentsFragment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.MyDrawerLayout;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.QcToggleButton;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
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
 * Created by Paper on 2017/3/15.
 */
@FragmentWithArgs public class MultiChooseStudentWithFilterFragment extends BaseFragment
    implements DrawerLayout.DrawerListener, StudentListPresenter.MVPView {
  @Arg(required = false) boolean expandedChosen;

  Toolbar toolbar;
  CheckBox rbSelectAll;
  TextView toolbarTitile;
  EditText etSearch;
  MyDrawerLayout drawer;
  QcToggleButton tgSortAlphabet;
  QcToggleButton tgSortRegist;
  QcToggleButton tgFilter;
  TextView tvAllotsaleSelectCount;
  ImageView searchClear;

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject StudentListPresenter presenter;
  LinearLayout layoutMultiChoose;

  private StudentFilterWithBirthFragment filterFragment;
  private ChooseStudentListFragment chooseStudentListFragment;
  private StudentFilter filter = new StudentFilter();

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_multi_choose_student, container, false);
    toolbar = (Toolbar) v.findViewById(R.id.toolbar);
    rbSelectAll = (CheckBox) v.findViewById(R.id.rb_select_all);
    toolbarTitile = (TextView) v.findViewById(R.id.toolbar_title);
    etSearch = (EditText) v.findViewById(R.id.et_search);
    drawer = (MyDrawerLayout) v.findViewById(R.id.drawer);
    tgSortAlphabet = (QcToggleButton) v.findViewById(R.id.tg_sort_alphabet);
    tgSortRegist = (QcToggleButton) v.findViewById(R.id.tg_sort_regist);
    tgFilter = (QcToggleButton) v.findViewById(R.id.tg_filter);
    tvAllotsaleSelectCount = (TextView) v.findViewById(R.id.tv_allotsale_select_count);
    searchClear = (ImageView) v.findViewById(R.id.search_clear);
    layoutMultiChoose = (LinearLayout) v.findViewById(R.id.layout_multi_choose);
    v.findViewById(R.id.btn_modify_sale).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onComplete();
      }
    });
    v.findViewById(R.id.tg_sort_alphabet).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSortChange(v);
      }
    });
    v.findViewById(R.id.tg_sort_regist).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSortChange(v);
      }
    });
    v.findViewById(R.id.ll_show_select).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showSelect();
      }
    });

    setToolbar(toolbar);
    delegatePresenter(presenter, this);
    etSearch.setCompoundDrawablesWithIntrinsicBounds(
        ContextCompat.getDrawable(getContext(), R.drawable.ic_search_24dp_grey), null, null, null);
    etSearch.setHint(getString(R.string.search_hint));
    RxTextView.afterTextChangeEvents(etSearch)
        .debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(textViewAfterTextChangeEvent -> {
          chooseStudentListFragment.localFilter(etSearch.getText().toString().trim());
          searchClear.setVisibility(
              etSearch.getText().toString().trim().length() > 0 ? View.VISIBLE : View.GONE);
        });
    RxView.clicks(searchClear).subscribe(aVoid -> etSearch.setText(""));
    tgFilter.setOnClickListener(v1 -> drawer.openDrawer(GravityCompat.END));
    chooseStudentListFragment = new ChooseStudentListFragment();
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_student_list, chooseStudentListFragment)
        .commit();
    chooseStudentListFragment.setListener(new ChooseStudentListFragment.SelectChangeListener() {
      @Override public void onSelectChange(int c) {
        tvAllotsaleSelectCount.setText(DirtySender.studentList.size() + "");
      }
    });
    setFilterFragment();
    rbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (chooseStudentListFragment != null) {
          DirtySender.studentList.clear();
          if (isChecked) {
            chooseStudentListFragment.selectAll();
            //DirtySender.studentList.addAll(chooseStudentListFragment.getSelectedStudents());
          } else {
            chooseStudentListFragment.clear();
          }
          setChosenCount();
        }
      }
    });
    setChosenCount();
    showLoadingTrans();
    presenter.queryStudentlist(filter);
    RxBusAdd(StudentFilterEvent.class).subscribe(new Action1<StudentFilterEvent>() {
      @Override public void call(StudentFilterEvent studentFilterEvent) {
        if (studentFilterEvent.eventType == StudentFilterEvent.EVENT_RESET_CLICK
            || studentFilterEvent.eventType == StudentFilterEvent.EVENT_CONFIRM_CLICK) {
          if (studentFilterEvent.filter == null) {
            filter = new StudentFilter();
          } else {
            filter = studentFilterEvent.filter;
          }
          presenter.queryStudentlist(filter);
          tgFilter.setChecked(!filter.isEmpty());
          drawer.closeDrawer(GravityCompat.END);
        }
      }
    });
    tvAllotsaleSelectCount.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            CompatUtils.removeGlobalLayout(tvAllotsaleSelectCount.getViewTreeObserver(), this);
            if (expandedChosen) {
              showSelect();
            }
          }
        });
    return v;
  }

  private void setToolbar(Toolbar toolbar) {
    toolbarTitile.setText("选择会员");
    toolbar.inflateMenu(R.menu.menu_cancel);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
        return true;
      }
    });
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup) {
      layoutMultiChoose.setPadding(0, MeasureUtils.getStatusBarHeight(getContext()), 0, 0);
    }
  }

  public void onComplete() {
    Intent ret = new Intent();
    //DirtySender.studentList.clear();
    //DirtySender.studentList.addAll(chooseStudentListFragment.getSelectedStudents());
    getActivity().setResult(Activity.RESULT_OK, ret);
    getActivity().finish();
    String callId = getActivity().getIntent().getStringExtra("callId");
    if (!TextUtils.isEmpty(callId)) {
      QC.sendQCResult(callId, QCResult.success());
    }
  }

  public void onSortChange(View v) {
    switch (v.getId()) {
      case R.id.tg_sort_alphabet:
        if (tgSortAlphabet.isChecked()) return;
        break;
      case R.id.tg_sort_regist:
        if (tgSortRegist.isChecked()) return;
        break;
    }
    tgSortAlphabet.toggle();
    tgSortRegist.toggle();
    clickSort(tgSortAlphabet.isChecked() ? 1 : 0);
  }

  /**
   * 选择排序方式
   *
   * @param t 0，是字母排序
   * 1，是最新注册
   */
  void clickSort(int t) {
    if (chooseStudentListFragment != null) {
      chooseStudentListFragment.setSortType(t);
      chooseStudentListFragment.localFilter(etSearch.getText().toString().trim());
    }
  }

  @Override protected void onVisible() {
    super.onVisible();
    if (getContext() != null && drawer != null) {

    }
  }

  /**
   * {@link StudentFilterWithBirthFragment}
   */
  public void setFilterFragment() {
    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    drawer.addDrawerListener(this);
    //会员筛选页
    filterFragment = new StudentFilterWithBirthFragment();
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frame_student_filter, filterFragment)
        .commit();
  }

  @Override public String getFragmentName() {
    return MultiChooseStudentWithFilterFragment.class.getName();
  }

  @Override public void onDrawerSlide(View drawerView, float slideOffset) {

  }

  @Override public void onDrawerOpened(View drawerView) {

  }

  @Override public void onDrawerClosed(View drawerView) {

  }

  @Override public void onDrawerStateChanged(int newState) {

  }

  @Override public void onShowError(String e) {

  }

  @Override public void onShowError(@StringRes int e) {

  }

  @Override public void onStudentsList(List<QcStudentBean> studentBeens) {
    hideLoadingTrans();
    chooseStudentListFragment.setDatas(studentBeens, etSearch.getText().toString().trim());
  }

  public void showSelect() {
    BottomStudentsFragment selectSutdentFragment = new BottomStudentsFragment();
    selectSutdentFragment.setListener(list -> {
      DirtySender.studentList.clear();
      DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(), list));
      if (chooseStudentListFragment != null && chooseStudentListFragment.isAdded()) {
        chooseStudentListFragment.selectStudent(
            ListUtils.transerList(new ArrayList<QcStudentBean>(), list));
      }
      setChosenCount();
    });
    selectSutdentFragment.setDatas(DirtySender.studentList);
    selectSutdentFragment.show(getFragmentManager(), "");
  }

  private void setChosenCount() {
    if (DirtySender.studentList.size() > 99) {
      tvAllotsaleSelectCount.setText("...");
    } else {
      tvAllotsaleSelectCount.setText("" + DirtySender.studentList.size());
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
