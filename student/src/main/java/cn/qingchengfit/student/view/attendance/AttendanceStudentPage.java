package cn.qingchengfit.student.view.attendance;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageAttendanceStudentBinding;
import cn.qingchengfit.student.viewmodel.attendance.AttendanceStudentViewModel;

/**
 * Created by huangbaole on 2017/11/15.
 */
@Leaf(module = "student",path = "/attendance/page")
public class AttendanceStudentPage extends StudentBaseFragment<PageAttendanceStudentBinding, AttendanceStudentViewModel> {

    private AttendanceFilterView fitlerView;




    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this,factory).get(AttendanceStudentViewModel.class);
        mViewModel.getToUri().observe(this, uri -> {
            routeTo(Uri.parse(uri), null);
        });
        mViewModel.getOffSetDay().observe(this, offsetDay -> {
            mViewModel.offDay.set(offsetDay);
            mViewModel.qcFBChecked.set(false);
        });
        mViewModel.getFilterIndex().observe(this,index->{
            fitlerView.showPage(index);
        });
        mViewModel.getResponse().observe(this,attendanceCharDataBean -> {
                mViewModel.datas.set(attendanceCharDataBean.datas);
        });
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageAttendanceStudentBinding.inflate(inflater, container, false);
        mBinding.setViewModel(mViewModel);
        mBinding.setToolbarModel(new ToolbarModel("出勤统计"));
        initToolbar(mBinding.includeToolbar.toolbar);
        initView();
        return mBinding.getRoot();
    }



    private void initView() {
        fitlerView = new AttendanceFilterView();
        stuff(R.id.frag_filter, fitlerView);
    }


}
