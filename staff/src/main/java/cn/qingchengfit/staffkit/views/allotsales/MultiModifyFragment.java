package cn.qingchengfit.staffkit.views.allotsales;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rxbus.event.AllotSaleEvent;
import cn.qingchengfit.staffkit.rxbus.event.AllotSaleSelectAllEvent;
import cn.qingchengfit.staffkit.views.FilterCommonFragment;
import cn.qingchengfit.staffkit.views.adapter.AllotSaleChooseAdapter;
import cn.qingchengfit.staffkit.views.allotsales.choose.MutiChooseSalersActivity;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.staffkit.views.student.edit.EditStudentInfoFragment;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.AlphabetView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

/**
 * 批量修改
 * 批量分配
 * 添加名下会员
 * 三个页面使用同一个布局和fragment
 * 通过type区分,并做不同的页面显示和操作控制。
 * Created by yangming on 16/10/14.
 */
@FragmentWithArgs public class MultiModifyFragment extends FilterCommonFragment
  implements MultiModifyPresenter.PresenterView, View.OnClickListener {

  public static final int TYPE_MULTI_MODIFY = 1;//批量修改
  public static final int TYPE_MULTI_ALLOT = 2;//批量分配
  public static final int TYPE_ADD_STUDENT = 3;//添加名下会员
  // 传入参数
  // 传入参数-学员list
  public List<StudentBean> students;
  public List<StudentBean> originList;
  // 传入参数-所属销售
  @Arg Staff saler;
  // 传入参数-页面标示
  @Arg int type;
  @Arg StudentFilter studentFilter;
  @Inject MultiModifyPresenter presenter;
  @Inject SaleDetailPresenter saleDetailPresenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  //@Inject EditStudentInfoFragment editStudentInfoFragment;

  // 学员list
  List<StudentBean> datas = new ArrayList<>();
  // 选中list
  List<StudentBean> datasChoose = new ArrayList<>();
  // 筛选list
  List<StudentBean> datasFilter = new ArrayList<>();

  // 全选
  Observable<AllotSaleSelectAllEvent> observableSelectAll;
  @BindView(R.id.swt_show_config) SwitcherLayout swtShowConfig;
  @BindView(R.id.et_search) EditText etSearch;
  @BindView(R.id.img_search_clear) ImageView imgSearchClear;
  @BindView(R.id.add_student) ImageView addStudent;
  @BindView(R.id.rl_search) RelativeLayout rlSearch;
  @BindView(R.id.ll_sort) LinearLayout llSort;
  @BindView(R.id.myhome_appBar) AppBarLayout myhomeAppBar;
  @BindView(R.id.rv_student) RecycleViewWithNoImg rvStudent;
  @BindView(R.id.ll_divider_bottom) View llDividerBottom;
  @BindView(R.id.tv_allotsale_select_count) TextView tvAllotsaleSelectCount;
  @BindView(R.id.img_down) ImageView imgDown;
  @BindView(R.id.ll_show_select) LinearLayout llShowSelect;
  @BindView(R.id.view_space) View viewSpace;
  @BindView(R.id.btn_modify_sale) Button btnModifySale;
  @BindView(R.id.ll_modify_sale) LinearLayout llModifySale;
  @BindView(R.id.btn_remove_stud) Button btnRemoveStud;
  @BindView(R.id.ll_remove_stud) LinearLayout llRemoveStud;
  @BindView(R.id.ll_bottom) LinearLayout llBottom;
  @BindView(R.id.scroll_root) CoordinatorLayout scrollRoot;
  // 学员 list adapter
  private AllotSaleChooseAdapter studentAdapter;
  // 学员 recyclerView LayoutManager
  private LinearLayoutManager mLinearLayoutManager;
  // 搜索关键字
  private String keyWord;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_allotsale_multi_modify, container, false);
    unbinder = ButterKnife.bind(this, view);
    setView(view);
    super.onCreateView(inflater, container, savedInstanceState);
    initTitle();
    initDI();
    initRxBus();
    initView();

    return view;
  }

  @Override public void onResume() {
    //        if (getActivity() instanceof AllotSalesActivity){
    //            ((AllotSalesActivity)getActivity()).setFilterFragment();
    //        }
    super.onResume();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void initTitle() {
    switch (type) {
      case TYPE_ADD_STUDENT://添加名下会员
        swtShowConfig.setVisibility(View.VISIBLE);
        addStudent.setVisibility(View.VISIBLE);
        if (getActivity() != null && getActivity() instanceof AllotSalesActivity) {
          ((AllotSalesActivity) getActivity()).initTextToolbar();
        }
        mCallbackActivity.setToolbar(getString(R.string.qc_allotsale_add_stud_title), false, null,
          R.menu.menu_compelete, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
              // 完成,批量添加会员
              if (datasChoose.size() == 0) {
                getActivity().onBackPressed();
              } else {
                showLoading();
                presenter.AddStudents(saler.id, datasChoose);
              }
              return false;
            }
          });

        break;
      case TYPE_MULTI_MODIFY://批量修改
        // 重置toolbar,显示全选按钮
        RxBus.getBus().post(new AllotSaleEvent());
        viewSpace.setVisibility(View.VISIBLE);
        llModifySale.setVisibility(View.VISIBLE);
        llRemoveStud.setVisibility(View.VISIBLE);
        mCallbackActivity.setToolbar(getString(R.string.qc_allotsale_multi_modify_title), false,
          null, R.menu.menu_cancel, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
              getActivity().onBackPressed();
              return false;
            }
          });
        btnModifySale.setText(getString(R.string.qc_allotsale_modify_sale));
        //                onClickSortUIChange();
        rvStudent.stopLoading();
        break;
      case TYPE_MULTI_ALLOT://批量分配
        // 重置toolbar,显示全选按钮
        RxBus.getBus().post(new AllotSaleEvent());
        viewSpace.setVisibility(View.VISIBLE);
        llModifySale.setVisibility(View.VISIBLE);
        mCallbackActivity.setToolbar(getString(R.string.qc_allotsale_multi_allot_title), false,
          null, R.menu.menu_cancel, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
              getActivity().onBackPressed();
              return false;
            }
          });
        btnModifySale.setText(getString(R.string.allot_saler));
        //                onClickSortUIChange();
        rvStudent.stopLoading();
        break;
    }
  }

  private void initDI() {
    delegatePresenter(presenter, this);
  }

  private void initRxBus() {
    // 全选事件
    observableSelectAll = RxBus.getBus().register(AllotSaleSelectAllEvent.class);
    observableSelectAll.observeOn(AndroidSchedulers.mainThread())
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .subscribe(new Action1<AllotSaleSelectAllEvent>() {
        @Override public void call(AllotSaleSelectAllEvent selectAllEvent) {
          for (StudentBean student : datas) {
            student.isChosen = selectAllEvent.isSelectAll;
          }
          studentAdapter.notifyDataSetChanged();
          if (selectAllEvent.isSelectAll) {
            datasChoose.clear();
            datasChoose.addAll(datas);
          } else {
            datasChoose.clear();
          }
          tvAllotsaleSelectCount.setText(
            datasChoose.size() > 99 ? "..." : "" + datasChoose.size());
          llBottom.setVisibility(datasChoose.size() > 0 ? View.VISIBLE : View.GONE);
        }
      });
  }

  private void initView() {
    switch (type) {
      case TYPE_ADD_STUDENT://添加名下会员,列表数据请求接口
        //                onClickSortUIChange();
        presenter.getSellerUsers(saler.id);
        break;
      case TYPE_MULTI_MODIFY://批量修改,列表数据为名下会员页面传过来的
        datas.addAll(students);
        break;
      case TYPE_MULTI_ALLOT://批量分配,列表数据为名下会员页面传过来的
        datas.addAll(students);
        break;
    }
    studentAdapter = new AllotSaleChooseAdapter(datas);
    mLinearLayoutManager = new LinearLayoutManager(getActivity());
    rvStudent.setLayoutManager(mLinearLayoutManager);
    rvStudent.addItemDecoration(
      new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    rvStudent.setAdapter(studentAdapter);
    rvStudent.setRefreshble(false);
    rvStudent.setFresh(false);

    tvSortRegister.setOnClickListener(this);
    tvSortAlpha.setOnClickListener(this);
    tvSortFilter.setOnClickListener(this);

    //        if (getActivity() instanceof AllotSalesActivity){
    //            ((AllotSalesActivity)getActivity()).drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
    //                @Override
    //                public void onDrawerSlide(View drawerView, float slideOffset) {
    //
    //                }
    //
    //                @Override
    //                public void onDrawerOpened(View drawerView) {
    //                    filterFragment.setStartFilter(studentFilter);
    //                }
    //
    //                @Override
    //                public void onDrawerClosed(View drawerView) {
    //
    //                }
    //
    //                @Override
    //                public void onDrawerStateChanged(int newState) {
    //
    //                }
    //            });
    //        }

    llBottom.setVisibility(View.GONE);
    // 学员 list item 选择事件监听
    studentAdapter.setListener(new OnRecycleItemClickListener() {
      @Override public void onItemClick(View v, int pos) {
        StudentBean student;
        if (!TextUtils.isEmpty(keyWord) || swtShowConfig.isOpen()) {
          student = datasFilter.get(pos);
        } else {
          student = datas.get(pos);
        }

        if (student.isChosen) {
          for (StudentBean studentBean : students) {
            //只有在批量添加情况下不可反选
            if (studentBean.id.equals(student.id) && type == TYPE_ADD_STUDENT) {
              //                            ToastUtils.show("不可取消已有的名下会员");
              return;
            }
          }
          // 取消一个item
          student.isChosen = false;
          for (int i = 0; i < datasChoose.size(); i++) {
            StudentBean b = datasChoose.get(i);
            if (TextUtils.equals(b.getId(), student.getId())) {
              datasChoose.remove(i);
              break;
            }
          }
        } else {
          // 选中一个item
          for (int i = 0; i < datas.size(); i++) {
            StudentBean b = datas.get(i);
            if (TextUtils.equals(b.getId(), student.getId())) {
              student.isChosen = true;
              b.isChosen = true;
              datasChoose.add(b);
              break;
            }
          }
        }
        studentAdapter.notifyItemChanged(pos);
        tvAllotsaleSelectCount.setText(
          datasChoose.size() > 99 ? "..." : "" + datasChoose.size());
        llBottom.setVisibility(datasChoose.size() > 0 ? View.VISIBLE : View.GONE);
      }
    });

    rvStudent.setAdapter(studentAdapter);
    // 侧边索引选中监听,list滚动到指定位置
    alphabetView.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
      @Override public void onChange(int position, String s) {
        if ("#".equals(s)) s = "~";
        mLinearLayoutManager.scrollToPositionWithOffset(
          studentAdapter.getPositionForSection(s.charAt(0)), 0);
      }
    });

    // 搜索框
    Drawable drawable = getResources().getDrawable(R.drawable.ic_allotsale_search);
    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    etSearch.setCompoundDrawables(drawable, null, null, null);
    etSearch.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void afterTextChanged(Editable editable) {
        keyWord = editable.toString().trim();
        if (keyWord.length() > 0) {
          RxBus.getBus().post(new AllotSaleEvent(true));
          imgSearchClear.setVisibility(View.VISIBLE);
          filterData(keyWord);
        } else {
          if (type == TYPE_MULTI_MODIFY) RxBus.getBus().post(new AllotSaleEvent(false));
          imgSearchClear.setVisibility(View.GONE);
          if (!swtShowConfig.isOpen()) {
            studentAdapter.setDatas(datas);
          } else {
            filterData(keyWord);
          }
        }
      }
    });
    // 顶部显示开关
    swtShowConfig.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        swtShowConfig.setOpen(b);
        if (datas.size() == 0) return;
        if (b) {
          filterNoSeller();
        } else {
          if (TextUtils.isEmpty(keyWord)) {
            studentAdapter.setDatas(datas);
          } else {
            filterData(keyWord);
          }
        }
      }
    });
  }

  private void freshData() {
    presenter.queryStudent(filter);
  }

  public void filterSaleConfirm(StudentFilterEvent event) {
    filter = event.filter;
    getActivity().runOnUiThread(new Runnable() {
      @Override public void run() {
        if (getActivity() instanceof AllotSalesActivity) {
          ((AllotSalesActivity) getActivity()).drawer.closeDrawers();
        }
        showLoading();
        onClickFilterUIChange();
      }
    });
    freshData();
  }

  // 44万条数据耗时300+毫秒
  public void filterData(String keyWord) {
    datasFilter.clear();
    for (StudentBean student : originList) {
      if ((student.getUsername().contains(keyWord) || student.getPhone().contains(keyWord))) {
        if (swtShowConfig.isOpen() && student.sellers.isEmpty()) {
          datasFilter.add(student);
        } else {
          datasFilter.add(student);
        }
      }
    }
    sortData(datasFilter);
    studentAdapter.setDatas(datasFilter);
  }

  public void filterNoSeller() {
    rvStudent.stopLoading();
    datasFilter.clear();
    for (StudentBean student : datas) {
      if (student.sellers.size() == 0) {
        if (!TextUtils.isEmpty(keyWord) && (student.getUsername().contains(keyWord)
          || student.getPhone().contains(keyWord))) {
          datasFilter.add(student);
        } else if (TextUtils.isEmpty(keyWord)) {
          datasFilter.add(student);
        }
      }
    }
    sortData(datasFilter);
    studentAdapter.setDatas(datasFilter);
  }

  @Override public void onStudentList(List<StudentBean> list) {
    hideLoading();
    rvStudent.stopLoading();
    List<StudentBean> temp = new ArrayList<>();
    temp.addAll(datasChoose);
    datasChoose.clear();
    datas.clear();
    datas.addAll(list);

    // 初始化已有的名下会员的选中状态为已选中
    for (StudentBean studentBean : datas) {
      // 初始化已有的名下会员的选中状态为已选中
      for (StudentBean choose : students) {
        if (studentBean.id.equals(choose.id)) {
          //                    studentBean.isChosen = true;
          studentBean.isOrigin = true;
        }
      }
      // 刚刚已选中的状态
      for (StudentBean choose : temp) {
        if (studentBean.id.equals(choose.id)) {
          studentBean.isChosen = true;
          datasChoose.add(studentBean);
        }
      }
    }
    if (temp.size() != 0 && datasChoose.size() == 0) {
      datasChoose.addAll(temp);
    }
    tvAllotsaleSelectCount.setText(
      datasChoose.size() > 99 ? "..." : "" + datasChoose.size());
    llBottom.setVisibility(datasChoose.size() > 0 ? View.VISIBLE : View.GONE);
    sortData(datas);
    studentAdapter.setDatas(datas);
    studentAdapter.notifyDataSetChanged();
    etSearch.setText("");
  }

  @Override public void onShowError(String e) {
    ToastUtils.show(e);
  }

  @Override public void onAddSuccess() {
    hideLoading();
    ToastUtils.show("添加完成");
    getFragmentManager().popBackStack("saleDetail", POP_BACK_STACK_INCLUSIVE);
  }

  @Override public void onRemoveSuccess() {
    rvStudent.stopLoading();
    getActivity().onBackPressed();
  }

  @Override public void onStopLoading() {
    rvStudent.stopLoading();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    RxBus.getBus().unregister(AllotSaleSelectAllEvent.class.getName(), observableSelectAll);
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public String getFragmentName() {
    return this.getClass().getName();
  }

  //处理绑定的点击事件
  @OnClick({
    R.id.btn_remove_stud, R.id.btn_modify_sale, R.id.ll_show_select, R.id.add_student,
    R.id.img_search_clear
  }) public void handleClick(View view) {
    switch (view.getId()) {
      case R.id.btn_modify_sale:
        if (datasChoose.size() <= 0) {
          ToastUtils.show("请选择会员");
          return;
        }
        if (serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
          Intent toChooseSaler = new Intent(getActivity(), MutiChooseSalersActivity.class);
          ArrayList<String> students = new ArrayList<>();
          for (StudentBean student : datasChoose) {
            students.add(student.getId());
          }
          // 传入已选会员的id 的 stringList
          toChooseSaler.putStringArrayListExtra(MutiChooseSalersActivity.INPUT_STUDENT, students);
          toChooseSaler.putExtra(MutiChooseSalersActivity.INPUT_CURRENT, saler.id);
          toChooseSaler.putExtra(MutiChooseSalersActivity.INPUT_SALERS,
            new ArrayList<String>(Arrays.asList(saler.id)));
          toChooseSaler.putExtra(MutiChooseSalersActivity.INPUT_TYPE, type);
          startActivityForResult(toChooseSaler, 22);
        } else {
          showAlert(R.string.alert_permission_forbid);
        }
        break;
      case R.id.btn_remove_stud:
        if (datasChoose.size() <= 0) {
          ToastUtils.show("请选择会员");
          return;
        }
        new MaterialDialog.Builder(getContext()).autoDismiss(true)
          .content("确认将选中会员从" + saler.username + "名下移除?")
          .positiveText(R.string.common_comfirm)
          .negativeText(R.string.common_cancel)
          .autoDismiss(true)
          .onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              // 批量移除
              showLoading();
              presenter.removeStudents(saler.id, datasChoose);
            }
          })
          .show();
        break;
      case R.id.ll_show_select:
        // 显示已选列表
        AllotSaleShowSelectDialogFragment.start(this, 11, (ArrayList<StudentBean>) datasChoose);
        break;
      case R.id.img_search_clear:
        etSearch.setText("");
        break;
      case R.id.add_student:
        // 添加会员
        EditStudentInfoFragment editStudentInfoFragment = new EditStudentInfoFragment();
        editStudentInfoFragment.isAdd = true;
        getFragmentManager().beginTransaction()
          .replace(mCallbackActivity.getFragId(), editStudentInfoFragment)
          .addToBackStack(null)
          .commit();
        break;
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 11) {//已选列表返回
        tvAllotsaleSelectCount.setText(
          datasChoose.size() > 99 ? "..." : "" + datasChoose.size());
        llBottom.setVisibility(datasChoose.size() > 0 ? View.VISIBLE : View.GONE);
        if (datasChoose.size() == 0) RxBus.getBus().post(new AllotSaleEvent());
        studentAdapter.notifyDataSetChanged();
      } else if (requestCode == 22) {//分配销售成功返回
        mCallbackActivity.cleanToolbar();
        //getFragmentManager().popBackStack("saleList", POP_BACK_STACK_INCLUSIVE);
        getFragmentManager().beginTransaction()
          .replace(mCallbackActivity.getFragId(), new SalesListFragment())
          .commit();
        //getFragmentManager().popBackStackImmediate("saleDetail",1);
        getFragmentManager().popBackStackImmediate("saleList", 1);
      }
    }
  }

  @Override public void onClick(View view) {
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
        if (getActivity() instanceof AllotSalesActivity) {
          ((AllotSalesActivity) getActivity()).drawer.openDrawer(GravityCompat.END);
        }
        break;
    }
  }
}
