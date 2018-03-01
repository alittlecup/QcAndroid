package cn.qingchengfit.saasbase.mvvm_student.view.attendance;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.PageAttendanceStudentBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.attendance.AttendanceStudentViewModel;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by huangbaole on 2017/11/15.
 */
@Leaf(module = "student", path = "/attendance/page")
public class AttendanceStudentPage extends
    StudentBaseFragment<PageAttendanceStudentBinding, AttendanceStudentViewModel> {

    private AttendanceFilterView fitlerView;


    @Override
    protected void subscribeUI() {


        mViewModel.getToUri().observe(this, uri -> {
            routeTo(Uri.parse(uri), null);
        });
        mViewModel.getOffSetDay().observe(this, offsetDay -> {
            mViewModel.offDay.set(offsetDay);
            mViewModel.qcFBChecked.set(false);
        });
        mViewModel.getFilterIndex().observe(this, index -> {
            fitlerView.showPage(index);
        });

        mViewModel.getResponse().observe(this, attendanceCharDataBean -> {
            mViewModel.datas.set(attendanceCharDataBean.datas);
        });

    }

    @Override
    public PageAttendanceStudentBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageAttendanceStudentBinding.inflate(inflater, container, false);
        mBinding.setViewModel(mViewModel);
        mBinding.setToolbarModel(new ToolbarModel("出勤统计"));
        initToolbar(mBinding.includeToolbar.toolbar);
        initView();
        return mBinding;
    }


    private void initView() {
        fitlerView = new AttendanceFilterView();
        stuff(R.id.frag_filter, fitlerView);
    }


}
