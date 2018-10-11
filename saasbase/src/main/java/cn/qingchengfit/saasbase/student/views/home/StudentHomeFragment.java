package cn.qingchengfit.saasbase.student.views.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qingchengfit.utils.SensorsUtils;
import com.anbillon.flabellum.annotations.Leaf;

import com.sensorsdata.analytics.android.sdk.SensorsDataTrackFragmentAppViewScreen;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentStudentlistBinding;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.other.EventFreshStudent;
import cn.qingchengfit.saasbase.student.presenters.home.StudentHomePresenter;
import cn.qingchengfit.utils.ToastUtils;
import rx.functions.Action1;

/**
 * Created by huangbaole on 2017/10/26.
 */
@SensorsDataTrackFragmentAppViewScreen
@Leaf(module = "student", path = "/student/home")
public class StudentHomeFragment extends SaasBaseFragment implements StudentHomePresenter.MVPView,SwipeRefreshLayout.OnRefreshListener {
    FragmentStudentlistBinding binding;
    StudentRecyclerViewFragment studentRecyclerViewFragment;
    @Inject
    StudentHomePresenter presenter;
    @Inject
    GymWrapper gymWrapper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ToolbarModel toolbarModel=new ToolbarModel("会员");
//        ToolbarModel toolbarModel=new ToolbarModel("会员");
        toolbarModel.setMenu(R.menu.menu_search);
        toolbarModel.setListener(item->{
            ToastUtils.show("click");
            return true;
        });
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_studentlist, container, false);
        delegatePresenter(presenter,this);
        initToolbar(binding.includeToolbar.toolbar);
        replaceFragment();
        RxBusAdd(EventFreshStudent.class).subscribe(new Action1<EventFreshStudent>() {
            @Override
            public void call(EventFreshStudent eventFreshStudent) {
                // REFACTOR: 2017/10/26 刷新数据
                freshData();
            }
        });
        binding.setFilterModel(studentRecyclerViewFragment);
        binding.setFragment(this);

        binding.setToolbarModel(toolbarModel);
        freshData();

        return binding.getRoot();
    }

    @Override
    protected boolean isfitSystemPadding() {
        return false;
    }

    /**
     *
     */

    private void replaceFragment() {
        getChildFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
                .replace(R.id.frame_student_operation, new StudentOperationFragment())
                .commit();
        studentRecyclerViewFragment = new StudentRecyclerViewFragment();
        studentRecyclerViewFragment.setOnFilterClickListener(new StudentRecyclerViewFragment.onFilterClickListener() {
            @Override
            public void onFilterButtonClick(boolean isChecked) {
                binding.drawer.openDrawer(GravityCompat.END);
            }
        });
        studentRecyclerViewFragment.initListener(this);
        getChildFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
                .replace(R.id.frag_student_list, studentRecyclerViewFragment)
                .commit();
        binding.alphabetview.setOnAlphabetChange((p,s)->{
            studentRecyclerViewFragment.scrollToPosition(p,s);
        });


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && binding.drawer.isShown()) {
                    binding.drawer.closeDrawer(GravityCompat.END);
                    return true;
                }
                return false;
            }
        });
    }


    private void freshData() {
        // REFACTOR: 2017/10/26 设置搜索数据
        ToastUtils.show("freshData");
        presenter.queryStudent("7505", null, new StudentFilter());
    }

    @Override
    public void onStudentList(List<QcStudentBean> list) {
        studentRecyclerViewFragment.setData(list);
    }


    @Override
    public void onStopFresh() {
        studentRecyclerViewFragment.stopRefresh();
    }
    public void onFabClick(View view){
//        if (serPermisAction.checkNoOne(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
//            showAlert(getString(R.string.alert_permission_forbid));
//            return;
//        }
//        EditStudentInfoFragment editStudentInfoFragment = new EditStudentInfoFragment();
//        editStudentInfoFragment.isAdd = true;
//        //新增学员
//        getFragmentManager().beginTransaction().add(mCallbackActivity.getFragId(), editStudentInfoFragment).addToBackStack(null).commit();
    }

    @Override
    public void onRefresh() {
        freshData();
    }
}
