//package cn.qingchengfit.saasbase.student.views.allot;
//
//import android.databinding.DataBindingUtil;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.anbillon.flabellum.annotations.Leaf;
//import com.anbillon.flabellum.annotations.Need;
//import com.jakewharton.rxbinding.widget.RxTextView;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import javax.inject.Inject;
//
//import cn.qingchengfit.di.model.LoginStatus;
//import cn.qingchengfit.model.base.QcStudentBean;
//import cn.qingchengfit.model.base.Staff;
//import cn.qingchengfit.saasbase.R;
//import cn.qingchengfit.saasbase.SaasBaseFragment;
//import cn.qingchengfit.saasbase.databinding.FragmentMultiAllotSalesBinding;
//import cn.qingchengfit.model.others.ToolbarModel;
//import cn.qingchengfit.saasbase.student.items.ChosenStudentItem;
//import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
//import cn.qingchengfit.saasbase.student.other.ChooseStaffParams;
//import cn.qingchengfit.saasbase.student.presenters.allot.SalesDetailPresenter;
//import cn.qingchengfit.saasbase.student.views.home.StudentRecyclerViewFragment;
//import cn.qingchengfit.utils.ToastUtils;
//import eu.davidea.flexibleadapter.FlexibleAdapter;
//import rx.android.schedulers.AndroidSchedulers;
//
///**
// * Created by huangbaole on 2017/10/31.
// */
//@Leaf(module = "student", path = "/multi/sales")
//public class MultiAllotSalesFragment extends SaasBaseFragment implements
//        SwipeRefreshLayout.OnRefreshListener,
//        FlexibleAdapter.OnItemClickListener,
//        SalesDetailPresenter.MVPView {
//    @Need
//    Staff staff;
//    @Need
//    String title;
//    @Inject
//    SalesDetailPresenter presenter;
//    @Inject
//    LoginStatus loginStatus;
//    FragmentMultiAllotSalesBinding binding;
//    StudentRecyclerViewFragment studentRecyclerViewFragment;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multi_allot_sales, container, false);
//        delegatePresenter(presenter, this);
//        initToolbar();
//        initBottom();
//        initFragment();
//        initView();
//        binding.setFilterModel(studentRecyclerViewFragment);
//        loadData();
//        binding.setFragment(this);
//        return binding.getRoot();
//    }
//
//    private void initView() {
//        RxTextView.afterTextChangeEvents(binding.etSearch)
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(textViewAfterTextChangeEvent -> {
//                    String filterKey = textViewAfterTextChangeEvent.editable().toString().trim();
//                    if (!TextUtils.isEmpty(filterKey)) {
//                        binding.imgSearchClear.setVisibility(View.VISIBLE);
//                    }
//                    studentRecyclerViewFragment.filter(filterKey);
//                });
//    }
//
//    private void loadData() {
//        presenter.queryStudent("7505", new StudentFilter(), staff.id);
//    }
//
//    private void initFragment() {
//        studentRecyclerViewFragment = new StudentRecyclerViewFragment();
//        stuff(R.id.fragment_list_container, studentRecyclerViewFragment);
//        studentRecyclerViewFragment.initListener(this);
//        studentRecyclerViewFragment.setCallback(ChosenStudentItem::new);
//        studentRecyclerViewFragment.setSwipeRefreshLayoutEnable(false);
//
//    }
//
//    private void initBottom() {
//        binding.setHasName(!TextUtils.isEmpty(staff.username));
//        binding.llBottom.setOnClickListener(view -> {
//            // REFACTOR: 2017/11/1 跳转展示界面
//
//        });
//    }
//
//    private void initToolbar() {
//        ToolbarModel toolbarModel = new ToolbarModel(title);
//        toolbarModel.setMenu(R.menu.menu_cancel);
//        toolbarModel.setListener(item -> {
//            getActivity().onBackPressed();
//            return false;
//        });
//        binding.setToolbarModel(toolbarModel);
//        binding.rbSelectAll.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b) {
//                studentRecyclerViewFragment.selectAll();
//            } else {
//                studentRecyclerViewFragment.clearSelection();
//            }
//            updateBottom();
//        });
//    }
//
//
//    protected boolean isfitSystemPadding() {
//        return false;
//    }
//
//    @Override
//    public boolean onItemClick(int position) {
//        studentRecyclerViewFragment.togglePositionSelect(position);
//        updateBottom();
//        return true;
//    }
//
//    private void updateBottom() {
//        int count = studentRecyclerViewFragment.getSelectedItemCount();
//        if (count > 0) {
//            binding.llBottom.setVisibility(View.VISIBLE);
//            binding.tvAllotsaleSelectCount.setText(count > 99 ? "..." : count + "");
//        } else {
//            binding.llBottom.setVisibility(View.GONE);
//            binding.tvAllotsaleSelectCount.setText("0");
//        }
//    }
//
//    @Override
//    public void onStudentList(List<QcStudentBean> list) {
//        studentRecyclerViewFragment.setData(list);
//    }
//
//    @Override
//    public void onRemoveSuccess() {
//        ToastUtils.show("移除成功");
//        getActivity().onBackPressed();
//    }
//
//    @Override
//    public void stopRefresh() {
//        studentRecyclerViewFragment.stopRefresh();
//    }
//
//    @Override
//    public void onRefresh() {
//        loadData();
//    }
//
//    public void onRemoveBtnClick(View view) {
//        // REFACTOR: 2017/11/2 移除
//        new MaterialDialog.Builder(getContext()).autoDismiss(true)
//                .content("确认将选中会员从" + staff.username + "名下移除?")
//                .positiveText(R.string.common_comfirm)
//                .negativeText(R.string.common_cancel)
//                .autoDismiss(true)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        // 批量移除
//                        presenter.removeStudents(loginStatus.staff_id(), staff.id, studentRecyclerViewFragment.getSelectedItemIds());
//                    }
//                })
//                .show();
//
//
//    }
//
//    public void onAllotBtnClick(View view) {
//        // REFACTOR: 2017/11/2 分配或者更改
//        Uri uri = Uri.parse("qcstaff://student/choose/staff");
//        routeTo(uri, new ChooseStaffParams()
//                .title(((TextView) view).getText().toString())
//                .curId(staff.id)
//                .textContent(getString(R.string.choose_saler) + "\n" + getString(R.string.choose_saler_tips))
//                .studentIds((new ArrayList<String>(studentRecyclerViewFragment.getSelectedItemIds())))
//                .build());
//    }
//
//    public void onClearEditClick(View view) {
//        binding.etSearch.setText("");
//        binding.imgSearchClear.setVisibility(View.GONE);
//
//    }
//}
