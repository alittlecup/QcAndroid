package cn.qingchengfit.saasbase.student.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.student.presenters.ChooseAndSearchPresenter;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
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
public class ChooseAndSearchStudentFragment extends BaseFragment {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.et_searchview) EditText etSearchview;
  @BindView(R2.id.btn_add_student) ImageView btnAddStudent;
  @BindView(R2.id.ll_head_search) LinearLayout llHeadSearch;
  @BindView(R2.id.frag_student_list) FrameLayout fragStudentList;
  @BindView(R2.id.tv_select_count) TextView tvSelectCount;
  @BindView(R2.id.img_down) ImageView imgDown;
  @BindView(R2.id.ll_show_select) LinearLayout llShowSelect;
  @BindView(R2.id.ll_bottom) LinearLayout llBottom;

  @Inject ChooseAndSearchPresenter presenter;
  private ChooseStudentListFragment chooseStudentListFragment;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    chooseStudentListFragment = new ChooseStudentListFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_choose_and_search_student, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    return view;
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    if (f instanceof ChooseStudentListFragment) {
      presenter.getAllStudents();
      //搜索变化
      RxTextView.afterTextChangeEvents(etSearchview)
          .throttleLast(1000, TimeUnit.MILLISECONDS)
          .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
              if (chooseStudentListFragment != null && chooseStudentListFragment.isAdded()) {
                chooseStudentListFragment.filter(etSearchview.getText().toString());
              }
            }
          });
    }
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(chooseStudentListFragment);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("选择会员");
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
    // TODO: 2017/8/29 跳去新增会员
  }
  //
  ///**
  // * 底部选择框
  // */
  //@OnClick(R2.id.ll_show_select) public void onLlShowSelectClicked() {
  //
  //}
}
