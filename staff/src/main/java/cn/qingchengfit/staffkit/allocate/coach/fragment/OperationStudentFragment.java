package cn.qingchengfit.staffkit.allocate.coach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.CommonAllocateDetailItem;
import cn.qingchengfit.staffkit.allocate.coach.AllocateCoachActivity;
import cn.qingchengfit.staffkit.allocate.coach.MutiChooseCoachActivity;
import cn.qingchengfit.staffkit.allocate.coach.comparator.ItemComparator;
import cn.qingchengfit.staffkit.allocate.coach.comparator.ItemComparatorJoinAt;
import cn.qingchengfit.staffkit.allocate.coach.item.ChooseStudentItem;
import cn.qingchengfit.staffkit.allocate.coach.presenter.OperationPresenter;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.allotsales.choose.MutiChooseSalersActivity;
import cn.qingchengfit.staffkit.views.bottom.BottomStudentsFragment;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.MyDrawerLayout;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.AlphabetView;
import cn.qingchengfit.widgets.QcToggleButton;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * Created by fb on 2017/5/3.
 */

@FragmentWithArgs public class OperationStudentFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, OperationPresenter.OperationView {

    public static final int SORT_TYPE_ALPHA = 684;//字母排序
    public static final int SORT_TYPE_REGISTER = 685;//最新注册时间排序
    public int sortType = SORT_TYPE_REGISTER;
    @Arg String name;
    @Arg String coachId;
    @BindView(R.id.swt_show_config) SwitcherLayout swtShowConfig;
    @BindView(R.id.img_search_clear) ImageView imgSearchClear;
    @BindView(R.id.add_student) ImageView addStudent;
    @BindView(R.id.rl_search) RelativeLayout rlSearch;
    @BindView(R.id.myhome_appBar) AppBarLayout myhomeAppBar;
    @BindView(R.id.rv_student) RecyclerView rvStudent;
    @BindView(R.id.alphaTextDialog) TextView alphaTextDialog;
    @BindView(R.id.alphabetview) AlphabetView alphabetview;
    @BindView(R.id.tv_allotsale_select_count) TextView tvAllotsaleSelectCount;
    @BindView(R.id.img_down) ImageView imgDown;
    @BindView(R.id.ll_show_select) LinearLayout llShowSelect;
    @BindView(R.id.view_space) View viewSpace;
    @BindView(R.id.ll_bottom) LinearLayout llBottom;
    @BindView(R.id.scroll_root) CoordinatorLayout scrollRoot;
    @BindView(R.id.drawer) MyDrawerLayout drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.text_toolbar_right) TextView textToolbarRight;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.btn_change_coach) TextView btnChangeCoach;
    @BindView(R.id.btn_remove) TextView btnRemove;
    @Inject OperationPresenter presenter;
    @BindView(R.id.rb_select_all) CheckBox rbSelectAll;
    @BindView(R.id.btn_allocate_coach) TextView btnAllocateCoach;
    @BindView(R.id.tv_sort_register) QcToggleButton tvSortRegister;
    @BindView(R.id.tv_sort_alpha) QcToggleButton tvSortAlpha;
    @BindView(R.id.tv_sort_filter) QcToggleButton tvSortFilter;
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.clear_text) ImageView clearText;
    private List<CommonAllocateDetailItem> itemList = new ArrayList<>();
    private CommonFlexAdapter adapter;
    private List<QcStudentBean> selectList = new ArrayList<>();
    private String keyword;
    private Observable<StudentFilterEvent> obFilter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<QcStudentBean> datas = new ArrayList<>();
    private StudentFilter filter;
    private ArrayMap<String, Integer> alphabetSort = new ArrayMap<>();
    private List<CommonAllocateDetailItem> stashList = new ArrayList<>();
    private List<String> datasSelected = new ArrayList<>();

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public String getFragmentName() {
        return OperationStudentFragment.class.getName();
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_student_operation, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        if (getActivity() instanceof AllocateCoachActivity) {
            filter = ((AllocateCoachActivity) getActivity()).studentFilter;
        }
        setToolbar();
        initView();
        initRxBus();
        refreshView();
        getData(keyword, filter);

        adapter = new CommonFlexAdapter(itemList, this);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        rvStudent.setLayoutManager(mLinearLayoutManager);
        rvStudent.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rvStudent.setAdapter(adapter);
        return view;
    }

    private void setToolbar() {
        if (name != null) {
            toolbarTitile.setText("批量修改");
            btnChangeCoach.setVisibility(View.VISIBLE);
            btnRemove.setVisibility(View.VISIBLE);
            btnAllocateCoach.setVisibility(View.GONE);
        } else {
            toolbarTitile.setText("批量分配");
            btnAllocateCoach.setVisibility(View.VISIBLE);
            btnChangeCoach.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
        }
        rbSelectAll.setVisibility(View.VISIBLE);
        rbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        adapter.addSelection(i);
                        selectList.add(itemList.get(i).getData());
                    }
                } else {
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        adapter.removeSelection(i);
                    }
                    selectList.clear();
                }
                adapter.notifyDataSetChanged();
                refreshView();
            }
        });
        toolbar.inflateMenu(R.menu.menu_cancel);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                getActivity().onBackPressed();
                return false;
            }
        });
    }

    private void initView() {
        addStudent.setVisibility(View.GONE);
        viewSpace.setVisibility(View.VISIBLE);

        alphabetview.setAlphaDialog(alphaTextDialog);
        alphabetview.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        // 侧边索引选中监听,list滚动到指定位置
        alphabetview.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
            @Override public void onChange(int position, String s) {
                if ("#".equals(s)) s = "~";
                mLinearLayoutManager.scrollToPositionWithOffset(getPositionForSection(s.charAt(0)), 0);
            }
        });

        tvSortAlpha.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (!tvSortAlpha.isChecked()) {
                    sortDataByAlpah();
                } else {
                    refreshSortView();
                }
            }
        });
        tvSortRegister.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (!tvSortRegister.isChecked()) {
                    sortDataByRegister();
                } else {
                    refreshSortView();
                }
            }
        });
        tvSortFilter.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (getActivity() instanceof AllocateCoachActivity) {
                    ((AllocateCoachActivity) getActivity()).drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });
    }

    public void localFilter(String s) {
        if (TextUtils.isEmpty(s)) {
            adapter.setSearchText(null);
            getActivity().runOnUiThread(new Runnable() {
                @Override public void run() {
                    adapter.updateDataSet(itemList);
                    adapter.notifyDataSetChanged();
                    if (sortType == SORT_TYPE_ALPHA) {
                        sortDataByAlpah();
                    } else {
                        sortDataByRegister();
                    }
                    clearText.setVisibility(GONE);
                }
            });
        } else {
            adapter.setSearchText(s);
            adapter.filterItems(itemList, 100);
            adapter.hideAllHeaders();
            getActivity().runOnUiThread(new Runnable() {
                @Override public void run() {
                    clearText.setVisibility(View.VISIBLE);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override public void run() {
                            selectItem();
                        }
                    }, 200);
                }
            });
        }
    }

    @OnClick({ R.id.btn_change_coach, R.id.btn_remove, R.id.btn_allocate_coach }) public void onOperation(View v) {
        Intent intent = new Intent(getActivity(), MutiChooseCoachActivity.class);
        if (v.getId() == R.id.btn_allocate_coach || v.getId() == R.id.btn_change_coach) {
            ArrayList<String> students = new ArrayList<>();
            ArrayList<String> coachIdList = new ArrayList<>();
            for (QcStudentBean studentBean : selectList) {
                students.add(studentBean.id());
                for (Staff coachBean : studentBean.sellers) {
                    coachIdList.add(coachBean.id);
                }
            }
            intent.putStringArrayListExtra(MutiChooseCoachActivity.INPUT_STUDENT, students);
            intent.putExtra(MutiChooseCoachActivity.INPUT_CURRENT, coachId);
        }
        switch (v.getId()) {
            case R.id.btn_change_coach:
                intent.putExtra(MutiChooseCoachActivity.INPUT_TYPE, MutiChooseCoachActivity.CHANGE);
                startActivityForResult(intent, 101);
                break;
            case R.id.btn_allocate_coach:
                intent.putExtra(MutiChooseSalersActivity.INPUT_TYPE, 100);
                startActivityForResult(intent, 102);
                break;
            case R.id.btn_remove:
                new MaterialDialog.Builder(getContext()).autoDismiss(true)
                    .content("确认将选中会员从" + name + "名下移除?")
                    .positiveText(R.string.common_comfirm)
                    .negativeText(R.string.common_cancel)
                    .autoDismiss(true)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // 批量移除
                            presenter.removeStudents(coachId, selectList);
                            showLoading();
                        }
                    })
                    .show();
                break;
        }
    }

    public int getPositionForSection(char section) {
        for (int i = 0; i < adapter.getItemCount() - 1; i++) {
            String sortStr = ((ChooseStudentItem) adapter.getItem(i)).getData().head;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private void initRxBus() {
        //搜索事件监听
        etSearch.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_search_24dp_grey), null,
            null, null);
        RxTextView.afterTextChangeEvents(etSearch)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
                @Override public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                    keyword = textViewAfterTextChangeEvent.editable().toString().trim();
                    localFilter(keyword);
                }
            });

        // 侧边栏筛选事件监听
        obFilter = RxBus.getBus().register(StudentFilterEvent.class);
      obFilter.observeOn(Schedulers.io())
          .onBackpressureBuffer()
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<StudentFilterEvent>() {
            @Override public void call(StudentFilterEvent event) {
                if (event.eventType == StudentFilterEvent.EVENT_CONFIRM_CLICK) {
                    resetData(event.filter);
                }
            }
        });
    }

    private void resetData(final StudentFilter filter) {
        if (getActivity() instanceof AllocateCoachActivity) {
            ((AllocateCoachActivity) getActivity()).studentFilter = filter;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                ((AllocateCoachActivity) getActivity()).drawer.closeDrawers();
                getData(keyword, filter);
            }
        });
    }

    private void getData(String keyword, StudentFilter filter) {
        presenter.queryStudent(filter, coachId, OperationPresenter.OPERATION_TYPE);
        showLoadingTrans();
    }

    private void refreshView() {
        if (selectList.size() == 0) {
            llBottom.setVisibility(View.GONE);
            return;
        } else {
            llBottom.setVisibility(View.VISIBLE);
        }
        tvAllotsaleSelectCount.setText(selectList.size() > 99 ? "..." : "" + selectList.size());
    }

    private void refreshSortView() {
        tvSortRegister.setChecked(sortType == SORT_TYPE_REGISTER);
        tvSortAlpha.setChecked(sortType == SORT_TYPE_ALPHA);

        if (sortType == SORT_TYPE_ALPHA) {
            if (alphabetview != null) alphabetview.setVisibility(View.VISIBLE);
        } else {
            for (CommonAllocateDetailItem item : itemList) {
                item.setAlphaBet(false);
            }
            if (alphabetview != null) alphabetview.setVisibility(View.GONE);
        }
        selectItem();
    }

    private void dealItemList() {
        List<CommonAllocateDetailItem> tempList = new ArrayList<>();
        if (adapter.getItemCount() > 0) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                tempList.add((CommonAllocateDetailItem) adapter.getItem(i));
            }
        } else {
            tempList.addAll(itemList);
        }
        stashList.clear();
        stashList.addAll(tempList);
    }

    private void selectItem() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i) instanceof CommonAllocateDetailItem) {
                if (selectList.contains(((CommonAllocateDetailItem) adapter.getItem(i)).getData())) {
                    adapter.addSelection(i);
                } else {
                    adapter.removeSelection(i);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void sortDataByAlpah() {
        sortType = SORT_TYPE_ALPHA;
        dealItemList();

        for (CommonAllocateDetailItem item : stashList) {
            QcStudentBean bean = item.getData();
            if (!"abcdefghijklmnopqrstuvwxyz".contains(bean.head()) || TextUtils.isEmpty(bean.head())) {
                bean.head = "~";
            }
        }
        Collections.sort(stashList, new ItemComparator());
        String tag = "";
        for (int i = 0; i < stashList.size(); i++) {
            QcStudentBean bean = stashList.get(i).getData();
            if (!bean.head.equalsIgnoreCase(tag)) {
                stashList.get(i).setAlphaBet(true);
                tag = bean.head;
                alphabetSort.put(tag, i);
            } else {
                stashList.get(i).setAlphaBet(false);
            }
        }
        adapter.updateDataSet(stashList);
        adapter.notifyDataSetChanged();
        refreshSortView();
    }

    public void sortDataByRegister() {
        sortType = SORT_TYPE_REGISTER;
        dealItemList();
        Collections.sort(stashList, new ItemComparatorJoinAt());
        adapter.updateDataSet(stashList);
        adapter.notifyDataSetChanged();
        refreshSortView();
    }

    @OnClick(R.id.ll_bottom) public void openSelectedBottom() {
        BottomStudentsFragment selectSutdentFragment = new BottomStudentsFragment();
        selectSutdentFragment.setListener(new BottomStudentsFragment.BottomStudentsListener() {
            @Override public void onBottomStudents(List<Personage> list) {
                selectList.clear();
                selectList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(), list));
                adapter.clearSelection();
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    QcStudentBean b = ((ChooseStudentItem) adapter.getItem(i)).getData();
                    if (selectList.contains(b)) {
                        adapter.addSelection(i);
                    }
                }
                adapter.notifyDataSetChanged();
                refreshView();
            }
        });
        selectSutdentFragment.setDatas(selectList);
        selectSutdentFragment.show(getFragmentManager(), "");
    }

    @OnClick(R.id.clear_text) public void onClear() {
        etSearch.setText("");
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        RxBus.getBus().unregister(StudentFilterEvent.class.getName(), obFilter);
    }

    @Override public boolean onItemClick(int position) {
        if (adapter.getItem(position) instanceof ChooseStudentItem) {
            if (adapter.isSelected(position)) {
                selectList.remove(((ChooseStudentItem) adapter.getItem(position)).getData());
            } else {
                selectList.add(((ChooseStudentItem) adapter.getItem(position)).getData());
            }
            adapter.toggleSelection(position);
            adapter.notifyItemChanged(position);
            refreshView();
        }
        return false;
    }

    @Override public void onStudentList(List<QcStudentBean> list) {
        itemList.clear();
        datas.clear();
        datas.addAll(list);
        for (QcStudentBean data : list) {
            itemList.add(new ChooseStudentItem(data));
        }
        hideLoadingTrans();
        sortDataByRegister();
    }

    @Override public void onAddSuccess() {
        ToastUtils.show("添加成功");
        getData(keyword, filter);
    }

    @Override public void onRemoveSuccess() {
        ToastUtils.show("移除成功");
        //        onRefreshBack();
        getActivity().onBackPressed();
    }

    @Override public void onStopLoading() {

    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 101 || requestCode == 102) {
                //              if (requestCode == 102 || data.getBooleanExtra("isRemove", false)){
                //                  adapter.removeAllSelectedItems();
                //                  selectList.clear();
                //                  refreshView();
                //              }
                //
                //              new Handler().postDelayed(new Runnable() {
                //                  @Override
                //                  public void run() {
                //                      presenter.initPage();
                //                      getData(keyword, filter);
                //                  }
                //              }, 500);//          }
                //                getActivity().onBackPressed();
                hideLoadingTrans();
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_allocate_coach, new AllocateCoachListFragment())
                    .commit();
                getActivity().getSupportFragmentManager().popBackStackImmediate(null, 1);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface OnBackListener {
        void onBack();
    }
}
