package cn.qingchengfit.staffkit.views.card;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.student.StudentAdapter;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/2/27.
 */

public class FixRealCardBindStudentFragment extends BaseFragment implements FixRealCardBindPresenter.OnBindStudentListener {

    @BindView(R.id.rl_bind_student) RecyclerView rlBindStudent;

    @Inject FixRealCardBindPresenter fixRealCardBindPresenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StudentAdapter studentAdapter;
    private ArrayList<StudentBean> datas = new ArrayList<>();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_bind, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        initToolbar();
        return view;
    }

    private void initToolbar() {
        mCallbackActivity.setToolbar("绑定会员", false, null, R.menu.menu_change_bind_student, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), FixRealcardStudentFragment.newInstance(gymWrapper.id(), gymWrapper.model(),
                        FixRealcardStudentFragment.BIND_STUDENT_LIST_DETAIL))
                    .addToBackStack(getFragmentName())
                    .commit();
                return false;
            }
        });
    }

    private void initData() {
        fixRealCardBindPresenter.setOnBindStudentListener(this);
        fixRealCardBindPresenter.queryGetCardDetail();
        rlBindStudent.setLayoutManager(new LinearLayoutManager(getContext()));
        studentAdapter = new StudentAdapter(datas);
        studentAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
                StudentBean bean = datas.get(pos);
                it.putExtra("student", bean);
                startActivity(it);
            }
        });
        studentAdapter.setCoachService(gymWrapper.getCoachService());
        studentAdapter.setIsBindStudent(true);
        rlBindStudent.setAdapter(studentAdapter);
    }

    @Override public String getFragmentName() {
        return FixRealCardBindStudentFragment.class.getName();
    }

    @Override public void onGetDataSuccess(List<StudentBean> studentList) {
        datas.clear();
        datas.addAll(studentList);
        studentAdapter.notifyDataSetChanged();
    }

    @Override public void onGetFailed(String msg) {
        ToastUtils.show(msg);
    }
}
