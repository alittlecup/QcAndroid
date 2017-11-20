package cn.qingchengfit.student.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import cn.qingchengfit.student.common.mvvm.ViewModelFactory;
import cn.qingchengfit.student.di.scope.ViewModelKey;
import cn.qingchengfit.student.viewmodel.attendance.AttendanceStudentViewModel;
import cn.qingchengfit.student.viewmodel.attendance.absent.AttendanceAbsentViewModel;
import cn.qingchengfit.student.viewmodel.attendance.nosign.AttendanceNosignViewModel;
import cn.qingchengfit.student.viewmodel.attendance.rank.AttendanceRankViewModel;
import cn.qingchengfit.student.viewmodel.followup.FollowUpStudentViewModel;
import cn.qingchengfit.student.viewmodel.transfer.TransferStudentViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by huangbaole on 2017/11/15.
 */

@Module
public abstract class ViewModelModule {


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(AttendanceStudentViewModel.class)
    abstract ViewModel bindAttendanceStudentViewModel(AttendanceStudentViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(AttendanceAbsentViewModel.class)
    abstract ViewModel bindAttendanceAbsentViewModel(AttendanceAbsentViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(AttendanceRankViewModel.class)
    abstract ViewModel bindAttendanceRankViewModel(AttendanceRankViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(AttendanceNosignViewModel.class)
    abstract ViewModel bindAttendanceNosignViewModel(AttendanceNosignViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(TransferStudentViewModel.class)
    abstract ViewModel bindTransferStudentViewModel(TransferStudentViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(FollowUpStudentViewModel.class)
    abstract ViewModel bindFollowUpStudentViewModel(FollowUpStudentViewModel model);

}
