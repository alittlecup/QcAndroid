//package cn.qingchengfit.saasbase.student.views;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.view.GravityCompat;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.qingchengfit.di.model.GymWrapper;
//import cn.qingchengfit.di.model.LoginStatus;
//import cn.qingchengfit.model.base.PermissionServerUtils;
//import cn.qingchengfit.model.base.StudentBean;
//import cn.qingchengfit.saasbase.R;
//import cn.qingchengfit.saasbase.permission.SerPermisAction;
//import cn.qingchengfit.student.presenters.StudentListPresenter;
//import cn.qingchengfit.views.fragments.BaseFragment;
//import cn.qingchengfit.widgets.R2;
//import java.util.ArrayList;
//import java.util.List;
//import javax.inject.Inject;
//import rx.functions.Action1;
//
//import static cn.qingchengfit.saasbase.R.id.drawer;
//
///**
// * power by
// * <p/>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p/>
// * <p/>
// * Created by Paper on 16/3/4 2016.
// */
//public class StudentListFragment extends BaseFragment {
//
//    @BindView(R2.id.toolbar) Toolbar toolbar;
//    @BindView(R2.id.toolbar_title) TextView toolbarTitile;
//    @BindView(R2.id.down) ImageView down;
//    @Inject StudentListPresenter presenter;
//    @Inject LoginStatus loginStatus;
//    @Inject GymWrapper gymWrapper;
//    private String keyWord;//搜索关键字
//    private List<StudentBean> datas = new ArrayList<>();
//    private List<StudentBean> datasOfigin = new ArrayList<>();
//    private String mChooseShopId;
//
//    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_studentlist, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        delegatePresenter(presenter, this);
//        initToolbar(toolbar);
//        initView();
//        // 会员操作
//        /**
//         * {@link cn.qingchengfit.staffkit.views.student.StudentOperationFragment}
//         */
//        getChildFragmentManager().beginTransaction().replace(R.id.frame_student_operation, new cn.qingchengfit.student.views.StudentOperationFragment()).commit();
//        // 会员筛选页
//        StudentFilterFragment filterFragment = new StudentFilterFragmentBuilder(1).build();
//        getChildFragmentManager().beginTransaction().replace(R.id.frame_student_filter, filterFragment).commit();
//
//        // 注册 event 刷新列表
//        RxBusAdd(EventFreshStudent.class).subscribe(new Action1<EventFreshStudent>() {
//            @Override public void call(EventFreshStudent eventFreshStudent) {
//                freshData();
//            }
//        });
//        return view;
//    }
//
//    @Override public void initToolbar(@NonNull Toolbar toolbar) {
//        super.initToolbar(toolbar);
//        toolbarTitile.setText("会员");
//        toolbar.inflateMenu(R.menu.menu_search);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override public boolean onMenuItemClick(MenuItem item) {
//                if (item.getItemId() == R.id.action_search) {
//                    //
//                    ArrayList<StudentBean> listForSearch = new ArrayList<StudentBean>();
//                    listForSearch.addAll(datasOfigin);
//                    getFragmentManager().beginTransaction()
//                        .replace(mCallbackActivity.getFragId(),
//                            StudentSearchFragment.newInstanceStudnet(listForSearch))
//                        .addToBackStack(null)
//                        .commit();
//                }
//                return true;
//            }
//        });
//    }
//
//    private void initView() {
//        //studentAdapter = new StudentAdapter(datas);
//        //studentlistRv.setLayoutManager(mLinearLayoutManager);
//        //
//        //studentlistRv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
//        //
//        //studentlistRv.setAdapter(studentAdapter);
//        //
//        //studentlistRv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//        //    @Override public void onRefresh() {
//        //        //freshdata
//        //        if (searchview.getVisibility() != View.VISIBLE) {
//        //            freshData();
//        //        } else {
//        //            studentlistRv.setFresh(false);
//        //        }
//        //    }
//        //});
//        //presenter.subsribeDb();
//        //freshData();
//        //studentAdapter.setListener(new OnRecycleItemClickListener() {
//        //    @Override public void onItemClick(View v, int pos) {
//        //        Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
//        //        StudentBean bean = datas.get(pos);
//        //        it.putExtra("student", bean);
//        //        studentWrapper.setStudentBean(bean);
//        //        startActivity(it);
//        //    }
//        //});
//        //
//        //alphabetView.setOnTouchListener(new View.OnTouchListener() {
//        //    @Override public boolean onTouch(View v, MotionEvent event) {
//        //        AppUtils.hideKeyboard(getActivity());
//        //        if (searchviewEt != null && TextUtils.isEmpty(searchviewEt.getText())) searchviewCancle.performClick();
//        //        return false;
//        //    }
//        //});
//        //
//        //setUpSeachView();
//        //onClickFilterUIChange();
//    }
//
//
//
//    @Override public void onStudentList(List<StudentBean> list) {
//        hideLoading();
//        studentlistRv.setFresh(false);
//        if (!TextUtils.isEmpty(keyWord)) {
//            return;
//        }
//        if (!isFilter) {
//            datasOfigin.clear();
//            datasOfigin.addAll(list);
//        }
//        datas.clear();
//        datas.addAll(list);
//        sortData(datas);
//        studentAdapter.notifyDataSetChanged();
//        setNoData(datas);
//    }
//
//    @Override public void onFilterStudentList(List<StudentBean> list) {
//        studentlistRv.setFresh(false);
//        datas.clear();
//        datas.addAll(list);
//        sortData(datas);
//        studentAdapter.notifyDataSetChanged();
//        setNoData(list);
//    }
//
//    /**
//     * 判断 list 是否为空，recyclerView 是否显示无数据
//     *
//     * @param list list
//     */
//    public void setNoData(List<StudentBean> list) {
//        if (list != null && list.size() > 0) {
//            studentlistRv.setNoData(false);
//        } else {
//            studentlistRv.setNoData(true);
//        }
//    }
//
//    @Override public void onFaied() {
//        hideLoading();
//    }
//
//    @Override public void onStopFresh() {
//        hideLoading();
//        studentlistRv.stopLoading();
//    }
//
//    @Override public String getFragmentName() {
//        return StudentListFragment.class.getName();
//    }
//
//    // 排序，筛选事件点击
//    @OnClick({ R.id.tv_sort_alpha, R.id.tv_sort_register, R.id.tv_sort_filter }) public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_sort_alpha:
//                sortType = FilterCommonFragment.SORT_TYPE_ALPHA;
//                //onClickSortUIChange();
//                sortData(datas);
//                studentAdapter.notifyDataSetChanged();
//                break;
//            case R.id.tv_sort_register:
//                sortType = FilterCommonFragment.SORT_TYPE_REGISTER;
//                //onClickSortUIChange();
//                sortData(datas);
//                studentAdapter.notifyDataSetChanged();
//                break;
//            case R.id.tv_sort_filter:
//                drawer.openDrawer(GravityCompat.END);
//                break;
//        }
//    }
//
//    @OnClick(R2.id.fab_add_student) void onClickAddStudent() {
//
//        if (SerPermisAction.checkNoOne(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
//            showAlert(getString(R.string.alert_permission_forbid));
//            return;
//        }
//        EditStudentInfoFragment editStudentInfoFragment = new EditStudentInfoFragment();
//        editStudentInfoFragment.isAdd = true;
//        //新增学员
//        getFragmentManager().beginTransaction().add(mCallbackActivity.getFragId(), editStudentInfoFragment).addToBackStack(null).commit();
//    }
//}
