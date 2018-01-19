package cn.qingchengfit.staffkit.views.student.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshStudent;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.FilterCommonFragment;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.staffkit.views.student.StudentAdapter;
import cn.qingchengfit.staffkit.views.student.StudentOperationFragment;
import cn.qingchengfit.staffkit.views.student.StudentSearchFragment;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.staffkit.views.student.edit.EditStudentInfoFragment;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterFragment;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterFragmentBuilder;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.IntentUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/4 2016.
 */
public class StudentListFragment extends FilterCommonFragment {

    @BindView(R.id.searchview_et) public EditText searchviewEt;
    @BindView(R.id.searchview_clear) public ImageView searchviewClear;
    @BindView(R.id.searchview_cancle) public Button searchviewCancle;
    @BindView(R.id.searchview) public LinearLayout searchview;
    @BindView(R.id.studentlist_rv) RecycleViewWithNoImg studentlistRv;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @Inject StudentListPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentWrapper studentWrapper;
    @Inject SerPermisAction serPermisAction;
    private String keyWord;//搜索关键字
    private StudentAdapter studentAdapter;
    private List<StudentBean> datas = new ArrayList<>();
    private List<StudentBean> datasOfigin = new ArrayList<>();
    private String mChooseShopId;
    StudentFilterFragment filterFragment;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterFragment = new StudentFilterFragmentBuilder(1).build();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_studentlist, container, false);
        setView(view);
        super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolBar();
        initView();
        // 会员操作
        /**
         * {@link cn.qingchengfit.staffkit.views.student.StudentOperationFragment}
         */
        getChildFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
            .replace(R.id.frame_student_operation, new StudentOperationFragment())
            .commit();
        // 会员筛选页

        getChildFragmentManager().beginTransaction().replace(R.id.frame_student_filter, filterFragment).commit();

        // 注册 event 刷新列表
        RxBusAdd(EventFreshStudent.class).subscribe(new Action1<EventFreshStudent>() {
            @Override public void call(EventFreshStudent eventFreshStudent) {
                freshData();
            }
        });
        return view;
    }



    @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
        super.onChildViewCreated(fm, f, v, savedInstanceState);
        setBackPress();
    }

    @Override public boolean onFragmentBackPress() {
        if (drawer.isDrawerOpen(Gravity.END)){
            drawer.closeDrawer(Gravity.END);
            return true;
        }else return false;
    }

    public void filterStudentConfirm(final StudentFilterEvent event) {
        if (getActivity() != null)
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                filter = event.filter;
                drawer.closeDrawers();
                showLoading();
                onClickFilterUIChange();
                freshData();
            }
        });
    }


    @Override public void onDrawerClosed(View drawerView) {
        //filterFragment.resetView(filter);
        super.onDrawerClosed(drawerView);
    }



    public void initToolBar() {
        super.initToolbar(toolbar);

        if (!gymWrapper.inBrand()) {
            toolbarTitile.setText("会员");
            toolbar.inflateMenu(R.menu.menu_search);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_search) {
                        //
                        ArrayList<StudentBean> listForSearch = new ArrayList<StudentBean>();
                        listForSearch.addAll(datasOfigin);
                        getFragmentManager().beginTransaction()
                            .replace(mCallbackActivity.getFragId(), StudentSearchFragment.newInstanceStudnet(listForSearch))
                            .addToBackStack(null)
                            .commit();
                    } else if (item.getItemId() == R.id.action_add) {

                    }
                    return true;
                }
            });
        } else {
            toolbarTitile.setText(getString(R.string.all_gyms));
            down.setVisibility(View.VISIBLE);
            toolbar.inflateMenu(R.menu.menu_search);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_search) {
                        //
                        ArrayList<StudentBean> listForSearch = new ArrayList<StudentBean>();
                        listForSearch.addAll(datasOfigin);
                        getFragmentManager().beginTransaction()
                            .replace(mCallbackActivity.getFragId(), StudentSearchFragment.newInstanceStudnet(listForSearch))
                            .addToBackStack(null)
                            .commit();
                    } else if (item.getItemId() == R.id.action_add) {
                        if (serPermisAction.checkNoOne(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
                            showAlert(R.string.alert_permission_forbid);
                            return true;
                        }

                        //新增学员
                        MutiChooseGymFragment.start(StudentListFragment.this, true, null, PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE,
                            1);
                    }
                    return true;
                }
            });
        }
    }

    @OnClick(R.id.titile_layout) public void onTitleClick() {
        if (gymWrapper.inBrand()) {
            ChooseGymActivity.start(this, 9, PermissionServerUtils.MANAGE_MEMBERS, getString(R.string.choose_gym), mChooseShopId);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 9) {
                mChooseShopId = IntentUtils.getIntentString(data, 1);
                String title = IntentUtils.getIntentString(data, 0);
                if (!TextUtils.isEmpty(mChooseShopId)) {
                    if (!serPermisAction.check(mChooseShopId, PermissionServerUtils.MANAGE_MEMBERS)) {
                        showAlert("您没有该场馆查看会员权限");
                        return;
                    }
                    toolbarTitile.setText(title);
                    freshData();
                } else {
                    toolbarTitile.setText("全部学员");
                    freshData();
                }
            } else if (requestCode == 1) {
                /**
                 * 添加学员
                 */
                Shop shops = (Shop) IntentUtils.getParcelable(data);
                if (shops != null) {
                    if (!serPermisAction.check(shops.id, PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
                        showAlert("抱歉!您无该场馆权限");
                        return;
                    }
                }
            }
        }
    }

    private void initView() {

        studentAdapter = new StudentAdapter(datas);
        studentlistRv.setLayoutManager(mLinearLayoutManager);

        studentlistRv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        studentlistRv.setAdapter(studentAdapter);

        studentlistRv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                //freshdata
                if (searchview.getVisibility() != View.VISIBLE) {
                    freshData();
                } else {
                    studentlistRv.setFresh(false);
                }
            }
        });
        presenter.subsribeDb();
        freshData();
        studentAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
                StudentBean bean = datas.get(pos);
                it.putExtra("student", bean);
                studentWrapper.setStudentBean(bean);
                startActivity(it);
            }
        });

        alphabetView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideKeyboard(getActivity());
                if (searchviewEt != null && TextUtils.isEmpty(searchviewEt.getText())) searchviewCancle.performClick();
                return true;
            }
        });

        setUpSeachView();
        onClickFilterUIChange();
    }

    @Override public void onResume() {
        super.onResume();
        studentWrapper.setStudentBean(null);
    }

    /**
     * 初始化搜索组件
     */
    private void setUpSeachView() {
        searchviewEt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    searchviewClear.setVisibility(View.VISIBLE);
                } else {
                    searchviewClear.setVisibility(View.GONE);
                }
                keyWord = s.toString().trim();
                presenter.filter(keyWord);
            }
        });
    }

    //搜索栏清除按钮
    @OnClick(R.id.searchview_clear) public void onClear() {
        searchviewEt.setText("");
    }

    //取消搜索
    @OnClick(R.id.searchview_cancle) public void onClickCancel() {

        if (searchviewEt.getText().toString().length() > 0) {
            searchviewEt.setText("");
        }
        AppUtils.hideKeyboard(getActivity());
        searchview.setVisibility(View.GONE);
    }

    private void freshData() {
        presenter.queryStudent(App.staffId, mChooseShopId, filter);
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onStudentList(List<StudentBean> list) {
        hideLoading();
        studentlistRv.setFresh(false);
        if (!TextUtils.isEmpty(keyWord)) {
            return;
        }
        if (!isFilter) {
            datasOfigin.clear();
            datasOfigin.addAll(list);
        }
        datas.clear();
        datas.addAll(list);
        sortData(datas);
        studentAdapter.notifyDataSetChanged();
        setNoData(datas);
    }

    @Override public void onFilterStudentList(List<StudentBean> list) {
        studentlistRv.setFresh(false);
        datas.clear();
        datas.addAll(list);
        sortData(datas);
        studentAdapter.notifyDataSetChanged();
        setNoData(list);
    }

    /**
     * 判断 list 是否为空，recyclerView 是否显示无数据
     *
     * @param list list
     */
    public void setNoData(List<StudentBean> list) {
        if (list != null && list.size() > 0) {
            studentlistRv.setNoData(false);
        } else {
            studentlistRv.setNoData(true);
        }
    }

    @Override public void onFaied() {
        hideLoading();
    }

    @Override public void onStopFresh() {
        hideLoading();
        studentlistRv.stopLoading();
    }

    @Override public String getFragmentName() {
        return StudentListFragment.class.getName();
    }

    // 排序，筛选事件点击
    @OnClick({ R.id.tv_sort_alpha, R.id.tv_sort_register, R.id.tv_sort_filter }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sort_alpha:
                sortType = SORT_TYPE_ALPHA;
                //onClickSortUIChange();
                sortData(datas);
                studentAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_sort_register:
                sortType = SORT_TYPE_REGISTER;
                //onClickSortUIChange();
                sortData(datas);
                studentAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_sort_filter:
                drawer.openDrawer(GravityCompat.END);
                break;
        }
    }

    @OnClick(R.id.fab_add_student) void onClickAddStudent() {

        if (serPermisAction.checkNoOne(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
            showAlert(getString(R.string.alert_permission_forbid));
            return;
        }
        EditStudentInfoFragment editStudentInfoFragment = new EditStudentInfoFragment();
        editStudentInfoFragment.isAdd = true;
        //新增学员
        getFragmentManager().beginTransaction().add(mCallbackActivity.getFragId(), editStudentInfoFragment).addToBackStack(null).commit();
    }
}
