package cn.qingchengfit.staffkit.views.student.followup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.common.FilterTimesFragment;
import cn.qingchengfit.common.FilterTimesFragmentBuilder;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.model.responese.TrackStudents;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.FilterFragment;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rxbus.event.EventRouter;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.TotalCountFooterItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.course.SimpleTextItemItem;
import cn.qingchengfit.staffkit.views.student.StudentSearchFragment;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.QcFilterToggle;
import cn.qingchengfit.widgets.QcToggleButton;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/5.
 */
public class FollowUpStatusFragment extends BaseFragment
    implements FollowUpStatusPresenter.PresenterView, FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener {

    public int studentStatus;

    //@BindView(R.id.tv_today_nodata) TextView tvTodayNodata;
    @BindView(R.id.recycler_view_today) RecyclerView recyclerViewToday;
    @BindView(R.id.layout_collapsed) AppBarLayout layoutCollapsed;

    StudentSearchFragment studentSearchFragment;
    @BindView(R.id.qft_status) QcFilterToggle qftStatus;
    @BindView(R.id.qft_times) QcFilterToggle qftTime;
    @BindView(R.id.qft_saler) QcFilterToggle qftSaler;
    @BindView(R.id.qtb_filter) QcToggleButton qtbFilter;
    @BindView(R.id.qft_gender) QcFilterToggle qftGender;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.frag_chart) FrameLayout fragChart;
    @BindView(R.id.frag_follow_up_status_filter_container) FrameLayout fragContaiter;
    FollowUpFilterFragment filterFragment;
    TopFilterSaleFragment saleFragment;
    FilterTimesFragment registerFragment;
    FilterFragment filterStatus;
    FilterFragment filterGender;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject FollowUpStatusPresenter presenter;
    @Inject SerPermisAction serPermisAction;
    ProgressItem progress;
    private CommonFlexAdapter flexibleAdapterToday;
    private List<AbstractFlexibleItem> itemsToday = new ArrayList<>();
    private List<Student> dataToday = new ArrayList<>();
    private StudentFilter filter = new StudentFilter();
    private Fragment emptyFragment = new Fragment();
    private boolean isGraphExpand = true;//图表是否展示
    private TotalCountFooterItem footItem;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_up_status, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        if (!serPermisAction.checkHasAllMember()) {
            qftSaler.setText(loginStatus.staff_name());
        }
        initTitleAndFilterBar();
        initFilterEntity();
        initView();
        initBus();
        presenter.getStudentsWithStatus(filter, studentStatus);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.inflateMenu(R.menu.menu_search);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                studentSearchFragment = StudentSearchFragment.newInstanceFollow(studentStatus, (ArrayList<Student>) dataToday);
                getFragmentManager().beginTransaction()
                    .add(mCallbackActivity.getFragId(), studentSearchFragment)
                    .addToBackStack(null)
                    .commit();
                return true;
            }
        });
    }

    @Override protected void onFinishAnimation() {
        saleFragment = new TopFilterSaleFragment();
        registerFragment = new FilterTimesFragmentBuilder(-1, 1).build();
        saleFragment.page = 1;
        registerFragment.page = 1;

        filterStatus = new FilterFragment();
        List<AbstractFlexibleItem> list = new ArrayList<>();
        list.add(new FilterCommonLinearItem("全部"));
        list.add(new FilterCommonLinearItem("新注册"));
        list.add(new FilterCommonLinearItem("已接洽"));
        list.add(new FilterCommonLinearItem("会员"));
        filterStatus.setItemList(list);
        filterStatus.setOnSelectListener(new FilterFragment.OnSelectListener() {
            @Override public void onSelectItem(int position) {
                switch (position) {
                    case 0:
                        filter.status = null;
                        qftStatus.setText("会员状态");
                        break;
                    case 1:
                        filter.status = "0";
                        qftStatus.setText("新注册");
                        break;
                    case 2:
                        filter.status = "1";
                        qftStatus.setText("已接洽");
                        break;
                    case 3:
                        filter.status = "2";
                        qftStatus.setText("会员");
                        break;
                    default:
                        break;
                }
                qftStatus.setChecked(false);
                if (filterFragment != null) filterFragment.selectStatusPos(position - 1);
                expandGraph(true);
                presenter.getStudentsWithStatus(filter, studentStatus);
            }
        });
        filterGender = new FilterFragment();
        List<AbstractFlexibleItem> listGender = new ArrayList<>();
        listGender.add(new FilterCommonLinearItem("全部"));
        listGender.add(new FilterCommonLinearItem("男"));
        listGender.add(new FilterCommonLinearItem("女"));
        filterGender.setItemList(listGender);
        filterGender.setOnSelectListener(new FilterFragment.OnSelectListener() {
            @Override public void onSelectItem(int position) {
                switch (position) {
                    case 1:
                        filter.gender = "0";
                        qftGender.setText("男");
                        break;
                    case 2:
                        filter.gender = "1";
                        qftGender.setText("女");
                        break;
                    default:
                        filter.gender = null;
                        qftGender.setText("性别");
                        break;
                }
                qftGender.setChecked(false);
                expandGraph(true);
                presenter.getStudentsWithStatus(filter, studentStatus);
            }
        });

        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
            .replace(R.id.frag_chart, new FollowUpDataStatisticsFragmentBuilder(studentStatus).build())
            .commit();

        getChildFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out)
            .add(R.id.frag_follow_up_status_filter_container, saleFragment, TopFilterSaleFragment.class.getName())
            .add(R.id.frag_follow_up_status_filter_container, filterStatus, FilterFragment.class.getName())
            .add(R.id.frag_follow_up_status_filter_container, registerFragment, FilterTimesFragment.class.getName())
            .add(R.id.frag_follow_up_status_filter_container, filterGender, "gender")
            .hide(saleFragment)
            .hide(filterStatus)
            .hide(registerFragment)
            .hide(filterGender)
            .commitAllowingStateLoss();

        if (studentStatus == 0) {
            //初始化边侧栏
            filterFragment = new FollowUpFilterFragment();
            ((FollowUpActivity) getActivity()).setUpDrawer(filterFragment);
        }
    }

    private void initBus() {
        /*
         *  监听筛选
         */
        RxBusAdd(FollowUpFilterEvent.class).observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<FollowUpFilterEvent>() {
                @Override public void call(FollowUpFilterEvent followUpFilterEvent) {
                    if (followUpFilterEvent.page == 0) return;
                    switch (followUpFilterEvent.eventType) {
                        case FollowUpFilterEvent.EVENT_SALE_ITEM_CLICK:
                            filter.sale = followUpFilterEvent.filter.sale;
                            if (filter.sale != null) {
                                qftSaler.setText(filter.sale.getUsername());
                            } else {
                                qftSaler.setText("销售");
                            }
                            qftSaler.setChecked(false);
                            //设置边侧栏同步
                            if (filterFragment != null && filter.sale != null) //未分配销售
                            {
                                filterFragment.saleFragment.selectSaler(filter.sale.getId());
                            }
                            expandGraph(true);
                            hideAll();
                            break;
                        case FollowUpFilterEvent.EVENT_LATEST_TIME_CLICK:
                            switch (followUpFilterEvent.position) {
                                case 0:
                                    filter.registerTimeStart = DateUtils.getStringToday();
                                    filter.registerTimeEnd = DateUtils.getStringToday();
                                    qftTime.setText("今日");
                                    break;
                                case 1:
                                    filter.registerTimeEnd = DateUtils.getStringToday();
                                    filter.registerTimeStart = DateUtils.minusDay(new Date(), 6);
                                    qftTime.setText("最近7天");
                                    break;
                                default:
                                    filter.registerTimeEnd = DateUtils.getStringToday();
                                    filter.registerTimeStart = DateUtils.minusDay(new Date(), 29);
                                    qftTime.setText("最近30天");
                                    break;
                            }
                            //qftTime.setText(filter.registerTimeStart + "至" + filter.registerTimeEnd);
                            qftTime.setChecked(false);
                            if (filterFragment != null) filterFragment.selectTimePos(followUpFilterEvent.position);
                            expandGraph(true);
                            hideAll();
                            break;
                        case FollowUpFilterEvent.EVENT_LATEST_TIME_CUSTOM_DATA:
                            filter.registerTimeStart = followUpFilterEvent.start;
                            filter.registerTimeEnd = followUpFilterEvent.end;
                            qftTime.setText("自定义");
                            qftTime.setChecked(false);
                            if (filterFragment != null) {
                                filterFragment.tvStudentFilterTimeStart.setText(followUpFilterEvent.start);
                                filterFragment.tvStudentFilterTimeEnd.setText(followUpFilterEvent.end);
                            }
                            expandGraph(true);
                            hideAll();
                            break;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            showLoading();
                        }
                    });
                    presenter.getStudentsWithStatus(filter, studentStatus);
                }
            });
        RxBusAdd(StudentFilterEvent.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<StudentFilterEvent>() {
            @Override public void call(StudentFilterEvent event) {
                //边侧栏筛选
                if (event.eventFrom == StudentFilterEvent.EVENT_FROM_FOLLOW_UP) {
                    RxBus.getBus().post(new EventRouter(RouterFollowUp.DRAWER_CLOSE));
                    expandGraph(true);
                    filter = event.filter;
                    if (!TextUtils.isEmpty(event.filter.status)) {
                        qftStatus.setText(BusinessUtils.getStudentStatus(Integer.parseInt(event.filter.status)));
                    }
                    if (event.filter.sale != null && Integer.parseInt(event.filter.sale.id) > 0) {
                        qftSaler.setText(event.filter.sale.getUsername());
                    } else {
                        qftSaler.setText("销售");
                    }
                    if (filter.timePos > 0) {
                        switch (filter.timePos) {
                            case 1:
                                qftTime.setText("最近7天");
                                break;
                            case 2:
                                qftTime.setText("最近30天");
                                break;
                            default:
                                qftTime.setText("今日");
                                break;
                        }
                    } else {
                        qftTime.setText("自定义");
                    }
                    if (!TextUtils.isEmpty(filter.gender)) {
                        if (filter.gender.equals("0")) {
                            qftGender.setText("男");
                        } else {
                            qftGender.setText("女");
                        }
                    } else {
                        qftGender.setText("性别");
                    }
                    showLoading();
                    presenter.getStudentsWithStatus(filter, studentStatus);
                }
            }
        }, new NetWorkThrowable());
    }

    /**
     * 展开图表
     *
     * @param b 是否展示
     */
    private void expandGraph(final boolean b) {
        if (b == isGraphExpand) {
            return;
        } else {
            isGraphExpand = b;
        }
        fragContaiter.setVisibility(b ? View.GONE : View.VISIBLE);
        layoutCollapsed.setExpanded(b);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initTitleAndFilterBar() {
        String title = "";
        switch (studentStatus) {
            case 0:
                title = getString(R.string.qc_foolow_up_lable_today_new);
                break;
            case 1:
                title = "新增跟进";
                break;
            case 2:
                title = getString(R.string.qc_follow_up_lable_toady_member);
                break;
        }
        toolbarTitile.setText(title);
        //根据状态展示筛选 0是展示 8是不展示
        switch (studentStatus) {
            case 0:
                showFilter(new int[] { 0, 0, 0, 0, 8 });
                break;
            case 1:
                showFilter(new int[] { 0, 0, 0, 8, 0 });
                break;
            case 2:
                showFilter(new int[] { 8, 0, 0, 8, 0 });
                break;
        }
    }

    public void showFilter(int[] show) {
        qftStatus.setVisibility(show[0]);
        qftTime.setVisibility(show[1]);
        qftSaler.setVisibility(show[2]);
        qtbFilter.setVisibility(show[3]);
        qftGender.setVisibility(show[4]);
    }

    /**
     * 初始化筛选数据
     */
    private void initFilterEntity() {
        filter.status = null;
        filter.registerTimeStart = DateUtils.getStringToday();
        filter.registerTimeEnd = DateUtils.getStringToday();
        if (!serPermisAction.checkHasAllMember()) {
            filter.sale = loginStatus.getLoginUser();
        }
    }

    private void initView() {

        recyclerViewToday.setLayoutManager(new SmoothScrollLinearLayoutManager(getActivity()));
        progress = new ProgressItem(getContext());
        footItem = new TotalCountFooterItem(1);
        if (flexibleAdapterToday == null) {
            flexibleAdapterToday = new CommonFlexAdapter(itemsToday, this);
            flexibleAdapterToday.setEndlessScrollListener(this, progress);
        }
        recyclerViewToday.setAdapter(flexibleAdapterToday);
    }

    @Override public void onDestroyView() {
        if (studentStatus == 0) ((FollowUpActivity) getActivity()).disableDrawer();
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

    /**
     * 背景点击
     */
    @OnClick(R.id.frag_follow_up_status_filter_container) public void onBackClick() {
        closeExcepte(0);
        expandGraph(true);
    }

    /**
     * 点击filter条
     *
     * @param view 点击的view
     */
    @OnClick({
        R.id.qft_saler, R.id.qft_times, R.id.qft_status, R.id.qtb_filter, R.id.qft_gender
    }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qft_saler:
                if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
                    ToastUtils.show("您只能查看自己名下的会员");
                    return;
                }
                qftSaler.toggle();
                if (qftSaler.isChecked()) {
                    closeExcepte(view.getId());
                    expandGraph(false);
                    showFragment(saleFragment);
                } else {
                    expandGraph(true);
                    hideFragment(saleFragment);
                }
                break;
            case R.id.qft_times:
                qftTime.toggle();
                if (qftTime.isChecked()) {
                    closeExcepte(view.getId());
                    expandGraph(false);
                    showFragment(registerFragment);
                } else {
                    hideFragment(registerFragment);
                    expandGraph(true);
                }
                break;
            case R.id.qft_status:
                qftStatus.toggle();
                if (qftStatus.isChecked()) {
                    closeExcepte(view.getId());
                    expandGraph(false);
                    showFragment(filterStatus);
                } else {
                    hideFragment(filterStatus);
                    expandGraph(true);
                }
                break;
            case R.id.qft_gender:
                qftGender.toggle();
                if (qftGender.isChecked()) {
                    closeExcepte(view.getId());
                    expandGraph(false);
                    showFragment(filterGender);
                } else {
                    hideFragment(filterGender);
                    expandGraph(true);
                }
                break;
            case R.id.qtb_filter:
                RxBus.getBus().post(new EventRouter(RouterFollowUp.DRAWER_OPEN));
                break;
        }
    }

    @Override public void onTrackStudent(TrackStudents students, int curPage) {
        hideLoading();
        if (curPage == 1) {
            dataToday.clear();
            flexibleAdapterToday.clear();
        }
        List<Student> studentList = students.users == null ? (new ArrayList<Student>()) : students.users;
        dataToday.addAll(studentList);
        if (curPage == 1 && (studentList == null || studentList.isEmpty())) {
            itemsToday.add(new SimpleTextItemItem("暂无数据", Gravity.CENTER));
            flexibleAdapterToday.notifyDataSetChanged();
            return;
        }
        List<AbstractFlexibleItem> newItems = new ArrayList<>();
        for (Student student : studentList) {
            newItems.add(new FollowUpItem(this, student, studentStatus));
        }
        flexibleAdapterToday.onLoadMoreComplete(newItems);

        //if (curPage >= flexibleAdapterToday.getEndlessTargetCount())
        //    flexibleAdapterToday.addItem(new TotalCountFooterItem(flexibleAdapterToday.getItemCount()));
    }

    @Override public void onToatalPages(int toatalPags) {
        flexibleAdapterToday.setEndlessTargetCount(toatalPags);
    }

    @Override public void onNoMoreLoad() {

        flexibleAdapterToday.onLoadMoreComplete(null);
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void initEndlessLoad() {
        if (flexibleAdapterToday != null) flexibleAdapterToday.setEndlessScrollListener(this, progress);
    }

    @Override public boolean onItemClick(int position) {
        if (position < flexibleAdapterToday.getItemCount()) {
            if (flexibleAdapterToday.getItem(position) instanceof FollowUpItem) {
                // 跳转会员详情
                Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
                StudentBean bean = ((FollowUpItem) flexibleAdapterToday.getItem(position)).data.toStudentBean();
                bean.setSupport_shop_ids(gymWrapper.shop_id());
                it.putExtra("student", bean);
                it.putExtra("status_to_tab", studentStatus);
                getActivity().startActivity(it);
            }
        }
        return false;
    }

    private void closeExcepte(int res) {
        if (res != R.id.qft_saler) qftSaler.setChecked(false);
        if (res != R.id.qft_times) qftTime.setChecked(false);
        if (res != R.id.qft_status) qftStatus.setChecked(false);
        if (res != R.id.qft_gender) qftGender.setChecked(false);
    }

    private void hideFragment(Fragment f) {
        getChildFragmentManager().beginTransaction().hide(f).commitAllowingStateLoss();
    }

    private void showFragment(Fragment f) {
        hideAll();
        getChildFragmentManager().beginTransaction().show(f).commitAllowingStateLoss();
    }

    private void hideAll() {
        getChildFragmentManager().beginTransaction()
            .hide(filterGender)
            .hide(saleFragment)
            .hide(filterStatus)
            .hide(registerFragment)
            .commitAllowingStateLoss();
    }

    @Override public void noMoreLoad(int i) {
        if (flexibleAdapterToday.getItem(flexibleAdapterToday.getItemCount() - 1) instanceof ProgressItem) {
            flexibleAdapterToday.removeItem(flexibleAdapterToday.getItemCount() - 1);
        }

        if (flexibleAdapterToday.getMainItemCount() > 0
            && !(flexibleAdapterToday.getItem(0) instanceof SimpleTextItemItem)
            && !flexibleAdapterToday.contains(footItem)) {
            flexibleAdapterToday.addScrollableFooter(footItem);
        }
    }

    @Override public void onLoadMore(int i, int i1) {
        presenter.loadMore(studentStatus);
    }
}