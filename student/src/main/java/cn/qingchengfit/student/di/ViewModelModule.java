package cn.qingchengfit.student.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import cn.qingchengfit.student.common.mvvm.ViewModelFactory;
import cn.qingchengfit.student.di.scope.ViewModelKey;
import cn.qingchengfit.student.respository.StudentRespository;
import cn.qingchengfit.student.respository.StudentRespositoryImpl;
import cn.qingchengfit.student.viewmodel.allot.AllotChooseViewModel;
import cn.qingchengfit.student.viewmodel.allot.AllotListViewModel;
import cn.qingchengfit.student.viewmodel.allot.AllotMultiStaffViewModel;
import cn.qingchengfit.student.viewmodel.allot.AllotStaffDetailViewModel;
import cn.qingchengfit.student.viewmodel.attendance.AttendanceStudentViewModel;
import cn.qingchengfit.student.viewmodel.attendance.absent.AttendanceAbsentViewModel;
import cn.qingchengfit.student.viewmodel.attendance.nosign.AttendanceNosignViewModel;
import cn.qingchengfit.student.viewmodel.attendance.rank.AttendanceRankViewModel;
import cn.qingchengfit.student.viewmodel.followup.FollowUpFilterViewModel;
import cn.qingchengfit.student.viewmodel.followup.FollowUpStatusViewModel;
import cn.qingchengfit.student.viewmodel.followup.FollowUpStudentViewModel;
import cn.qingchengfit.student.viewmodel.home.StudentFilterViewModel;
import cn.qingchengfit.student.viewmodel.home.StudentHomeViewModel;
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
    abstract StudentRespository bindStudentRespository(StudentRespositoryImpl studentRespository);

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

    @Binds
    @IntoMap
    @ViewModelKey(FollowUpStatusViewModel.class)
    abstract ViewModel bindFollowUpStatusViewModel(FollowUpStatusViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(AllotListViewModel.class)
    abstract ViewModel bindAllotListViewModel(AllotListViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(AllotStaffDetailViewModel.class)
    abstract ViewModel bindAllotStaffDetailViewModel(AllotStaffDetailViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(AllotMultiStaffViewModel.class)
    abstract ViewModel bindAllotMultiStaffViewModel(AllotMultiStaffViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(AllotChooseViewModel.class)
    abstract ViewModel bindAllotChooseViewModel(AllotChooseViewModel model);


    @Binds
    @IntoMap
    @ViewModelKey(StudentHomeViewModel.class)
    abstract ViewModel bindStudentHomeViewModel(StudentHomeViewModel model);


    @Binds
    @IntoMap
    @ViewModelKey(StudentFilterViewModel.class)
    abstract ViewModel bindStudentFilterViewModel(StudentFilterViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(FollowUpFilterViewModel.class)
    abstract ViewModel bindFollowUpFilterViewModel(FollowUpFilterViewModel model);

}
