package cn.qingchengfit.staffkit.allocate.coach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.CommonAllocateDetailItem;
import cn.qingchengfit.staffkit.allocate.coach.AllocateCoachActivity;
import cn.qingchengfit.staffkit.allocate.coach.comparator.ItemComparator;
import cn.qingchengfit.staffkit.allocate.coach.comparator.ItemComparatorJoinAt;
import cn.qingchengfit.staffkit.allocate.coach.event.ChangeUIEvent;
import cn.qingchengfit.staffkit.allocate.coach.item.CoachStudentDetailItem;
import cn.qingchengfit.staffkit.allocate.coach.model.StudentWithCoach;
import cn.qingchengfit.staffkit.allocate.coach.presenter.CoachDetailPresenter;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.MyDrawerLayout;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.widgets.AlphabetView;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcToggleButton;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/5/4.
 */

@FragmentWithArgs public class CoachStudentDetailFragment extends BaseFragment
    implements CoachDetailPresenter.CoachPreView, FlexibleAdapter.OnItemClickListener {

    public static final int SORT_TYPE_ALPHA = 684;//字母排序
    public static final int SORT_TYPE_REGISTER = 685;//最新注册时间排序
    public int sortType = SORT_TYPE_REGISTER;
    @Arg String name;
    @Arg String coachId;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.text_toolbar_right) TextView textToolbarRight;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.ll_add_stu) LinearLayout llAddStu;
    @BindView(R.id.alphabetview) AlphabetView alphabetview;
    @BindView(R.id.drawer) MyDrawerLayout drawer;
    @BindView(R.id.smrv_sale) RecyclerView smrvSale;
    @Inject CoachDetailPresenter presenter;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.tv_sort_register) QcToggleButton tvSortRegister;
    @BindView(R.id.tv_sort_alpha) QcToggleButton tvSortAlpha;
    @BindView(R.id.tv_sort_filter) QcToggleButton tvSortFilter;
    private Observable<StudentFilterEvent> obFilter;
    private List<CommonAllocateDetailItem> itemList = new ArrayList<>();
    private CommonFlexAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private StudentFilter mFilter;
    private List<QcStudentBean> datas = new ArrayList<>();
    private StudentFilter filter;
    private ArrayMap<String, Integer> alphabetSort = new ArrayMap<>();

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public String getFragmentName() {
        return CoachStudentDetailFragment.class.getName();
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_allocate_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        setToolbar();
        initRxBus();

        if (((AllocateCoachActivity) getActivity()).studentFilter != null) {
            mFilter = ((AllocateCoachActivity) getActivity()).studentFilter;
        } else {
            mFilter = new StudentFilter();
        }
        getData();
        adapter = new CommonFlexAdapter(itemList, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        smrvSale.setLayoutManager(linearLayoutManager);
        smrvSale.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        smrvSale.setAdapter(adapter);
        initView();
        return view;
    }

    private void setToolbar() {
        initToolbar(toolbar);
        if (name == null) {
            toolbarTitile.setText("未分配的会员");
            llAddStu.setVisibility(View.GONE);
            toolbar.inflateMenu(R.menu.menu_multi_allot);
        } else {
            toolbarTitile.setText(getString(R.string.coach_detail_student, name));
            toolbar.inflateMenu(R.menu.menu_multi_modify);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                getFragmentManager().beginTransaction()
                    .replace(((AllocateCoachActivity) getActivity()).getFragId(),
                        OperationStudentFragmentBuilder.newOperationStudentFragment(coachId, name))
                    .addToBackStack(null)
                    .commit();
                return false;
            }
        });
    }

    public void refreshView() {
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
        adapter.notifyDataSetChanged();
    }

    public void sortDataByAlpah() {
        sortType = SORT_TYPE_ALPHA;
        for (CommonAllocateDetailItem item : itemList) {
            QcStudentBean bean = item.getData();
            if (!"abcdefghijklmnopqrstuvwxyz".contains(bean.head()) || TextUtils.isEmpty(bean.head())) {
                bean.head = "~";
            }
        }
        Collections.sort(itemList, new ItemComparator());
        String tag = "";
        for (int i = 0; i < itemList.size(); i++) {
            QcStudentBean bean = itemList.get(i).getData();
            if (!bean.head.equalsIgnoreCase(tag)) {
                itemList.get(i).setAlphaBet(true);
                tag = bean.head;
                alphabetSort.put(tag, i);
            } else {
                itemList.get(i).setAlphaBet(false);
            }
        }
        refreshView();
    }

    public void sortDataByRegister() {
        sortType = SORT_TYPE_REGISTER;
        Collections.sort(itemList, new ItemComparatorJoinAt());
        refreshView();
    }

    private void initView() {
        alphabetview.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        // 侧边索引选中监听,list滚动到指定位置
        alphabetview.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
            @Override public void onChange(int position, String s) {
                if ("#".equals(s)) s = "~";
                linearLayoutManager.scrollToPositionWithOffset(getPositionForSection(s.charAt(0)), 0);
            }
        });

        tvSortAlpha.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (!tvSortAlpha.isChecked()) {
                    sortDataByAlpah();
                } else {
                    refreshView();
                }
            }
        });
        tvSortRegister.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (!tvSortRegister.isChecked()) {
                    sortDataByRegister();
                } else {
                    refreshView();
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

    private void getData() {
        presenter.queryStudent(App.staffId, mFilter, coachId);
        showLoadingTrans();
    }

    private void initRxBus() {

        // 侧边栏筛选事件监听
        obFilter = RxBus.getBus().register(StudentFilterEvent.class);
        obFilter.observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<StudentFilterEvent>() {
            @Override public void call(StudentFilterEvent event) {
                if (event.eventType == StudentFilterEvent.EVENT_CONFIRM_CLICK) {
                    resetData(event.filter);
                }
            }
        });
    }

    public int getPositionForSection(char section) {
        for (int i = 0; i < adapter.getItemCount() - 1; i++) {
            String sortStr = itemList.get(i).getData().head;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private void resetData(StudentFilter filter) {
        mFilter = filter;
        if (getActivity() instanceof AllocateCoachActivity) {
            ((AllocateCoachActivity) getActivity()).studentFilter = filter;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                ((AllocateCoachActivity) getActivity()).drawer.closeDrawers();
                getData();
            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        RxBus.getBus().unregister(StudentFilterEvent.class.getName(), obFilter);
    }

    @Override public void onStudentList(List<StudentWithCoach> list) {
        hideLoading();
        itemList.clear();
        datas.clear();
        datas.addAll(list);
        for (StudentWithCoach data : list) {
            itemList.add(new CoachStudentDetailItem(data));
        }
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                RxBus.getBus().post(new ChangeUIEvent(!mFilter.isEmpty()));
            }
        }, 300);
        hideLoadingTrans();
        sortDataByRegister();
    }

    @OnClick(R.id.ll_add_stu) public void onOpenStudent() {
        getFragmentManager().beginTransaction()
            .replace(R.id.frame_allocate_coach, AddStudentFragmentBuilder.newAddStudentFragment(coachId))
            .addToBackStack(null)
            .commit();
    }

    @Override public void onRemoveSucess(int position) {

    }

    @Override public void clearDatas() {
        itemList.clear();
    }

    @Override public boolean onItemClick(int position) {
        Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
        QcStudentBean qcBean = itemList.get(position).getData();
        StudentBean bean = qcBean.toStudentBean(gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model());
        it.putExtra("student", bean);
        startActivity(it);
        return false;
    }
}
