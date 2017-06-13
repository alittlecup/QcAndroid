package cn.qingchengfit.staffkit.views.student.choose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.DirtySender;
import cn.qingchengfit.staffkit.views.bottom.BottomStudentsFragment;
import cn.qingchengfit.staffkit.views.custom.MyDrawerLayout;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.widgets.QcToggleButton;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
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

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rb_select_all) CheckBox rbSelectAll;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.drawer) MyDrawerLayout drawer;
    @BindView(R.id.tg_sort_alphabet) QcToggleButton tgSortAlphabet;
    @BindView(R.id.tg_sort_regist) QcToggleButton tgSortRegist;
    @BindView(R.id.tg_filter) QcToggleButton tgFilter;
    @BindView(R.id.tv_allotsale_select_count) TextView tvAllotsaleSelectCount;
    @BindView(R.id.search_clear) ImageView searchClear;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentListPresenter presenter;

    private StudentFilterWithBirthFragment filterFragment;
    private ChooseStudentListFragment chooseStudentListFragment;
    private StudentFilter filter = new StudentFilter();

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_multi_choose_student, container, false);
        unbinder = ButterKnife.bind(this, v);
        initToolbar(toolbar);
        delegatePresenter(presenter, this);
        etSearch.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_search_24dp_grey), null,
            null, null);
        etSearch.setHint(getString(R.string.search_hint));
        //etSearch.addTextChangedListener(new TextWatcher() {
        //    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
        //    }
        //
        //    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        //
        //    }
        //
        //    @Override public void afterTextChanged(Editable s) {
        //        chooseStudentListFragment.localFilter(s.toString());
        //        searchClear.setVisibility(s.length() > 0 ?View.VISIBLE:View.GONE);
        //    }
        //});
        RxTextView.afterTextChangeEvents(etSearch)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
                @Override public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                    chooseStudentListFragment.localFilter(etSearch.getText().toString().trim());
                    searchClear.setVisibility(etSearch.getText().toString().trim().length() > 0 ? View.VISIBLE : View.GONE);
                }
            });
        RxView.clicks(searchClear).subscribe(new Action1<Void>() {
            @Override public void call(Void aVoid) {
                etSearch.setText("");
            }
        });
        tgFilter.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });
        chooseStudentListFragment = new ChooseStudentListFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.frag_student_list, chooseStudentListFragment).commit();
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
        tvAllotsaleSelectCount.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(tvAllotsaleSelectCount.getViewTreeObserver(), this);
                if (expandedChosen) {
                    showSelect();
                }
            }
        });
        return v;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        toolbarTitile.setText("选择会员");
        toolbar.inflateMenu(R.menu.menu_cancel);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
                return true;
            }
        });
    }

    @OnClick(R.id.btn_modify_sale) public void onComplete() {
        Intent ret = new Intent();
        //DirtySender.studentList.clear();
        //DirtySender.studentList.addAll(chooseStudentListFragment.getSelectedStudents());
        getActivity().setResult(Activity.RESULT_OK, ret);
        getActivity().finish();
    }

    @OnClick({ R.id.tg_sort_alphabet, R.id.tg_sort_regist }) public void onSortChange(View v) {
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
        getChildFragmentManager().beginTransaction().replace(R.id.frame_student_filter, filterFragment).commit();
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

    @OnClick(R.id.ll_show_select) public void showSelect() {
        BottomStudentsFragment selectSutdentFragment = new BottomStudentsFragment();
        selectSutdentFragment.setListener(new BottomStudentsFragment.BottomStudentsListener() {
            @Override public void onBottomStudents(List<Personage> list) {
                if (chooseStudentListFragment != null) {
                    chooseStudentListFragment.selectStudent(ListUtils.transerList(new ArrayList<QcStudentBean>(), list));
                }
                setChosenCount();
            }
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
}
