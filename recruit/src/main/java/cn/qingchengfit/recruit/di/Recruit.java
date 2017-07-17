package cn.qingchengfit.recruit.di;

import android.support.v4.app.Fragment;
import cn.qingchengfit.recruit.views.DialogSendResumeFragment;
import cn.qingchengfit.recruit.views.JobFairListFragment;
import cn.qingchengfit.recruit.views.JobFairsAllFragment;
import cn.qingchengfit.recruit.views.JobsListFragment;
import cn.qingchengfit.recruit.views.MyPositionsInfoFragment;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.recruit.views.RecruitGymDescFragment;
import cn.qingchengfit.recruit.views.RecruitGymDetailEmployerFragment;
import cn.qingchengfit.recruit.views.RecruitGymDetailFragment;
import cn.qingchengfit.recruit.views.RecruitManageFragment;
import cn.qingchengfit.recruit.views.RecruitPositionDetailEmployerFragment;
import cn.qingchengfit.recruit.views.RecruitPositionDetailFragment;
import cn.qingchengfit.recruit.views.RecruitPositionsFragment;
import cn.qingchengfit.recruit.views.RecruitPositionsInGymFragment;
import cn.qingchengfit.recruit.views.RecruitPositionsInvitedFragment;
import cn.qingchengfit.recruit.views.RecruitPositionsSentFragment;
import cn.qingchengfit.recruit.views.RecruitPositionsStarredFragment;
import cn.qingchengfit.recruit.views.RecruitPublishJobFragment;
import cn.qingchengfit.recruit.views.RecruitStaffMyJobFairFragment;
import cn.qingchengfit.recruit.views.RecruitUserMyJobFairFragment;
import cn.qingchengfit.recruit.views.RecruitWriteGymIntroFragment;
import cn.qingchengfit.recruit.views.ResumeHandleFragment;
import cn.qingchengfit.recruit.views.ResumeListFragment;
import cn.qingchengfit.recruit.views.ResumeMarketHomeFragment;
import cn.qingchengfit.recruit.views.ResumeRecievedFragment;
import cn.qingchengfit.recruit.views.ResumeStarredFragment;
import cn.qingchengfit.recruit.views.SeekPositionHomeFragment;
import cn.qingchengfit.recruit.views.jobfair.JobFairSuccessFragment;
import cn.qingchengfit.recruit.views.jobfair.JobfairDetailFragment;
import cn.qingchengfit.recruit.views.jobfair.JobfairSignUpFragment;
import cn.qingchengfit.recruit.views.resume.AddEduExpFragment;
import cn.qingchengfit.recruit.views.resume.RecordEditFragment;
import cn.qingchengfit.recruit.views.resume.RecruitPermissionFragment;
import cn.qingchengfit.recruit.views.resume.ResumeBaseInfoFragment;
import cn.qingchengfit.recruit.views.resume.ResumeCertificateListFragment;
import cn.qingchengfit.recruit.views.resume.ResumeDetailFragment;
import cn.qingchengfit.recruit.views.resume.ResumeEditDescFragment;
import cn.qingchengfit.recruit.views.resume.ResumeEduExpListFragment;
import cn.qingchengfit.recruit.views.resume.ResumeHomeFragment;
import cn.qingchengfit.recruit.views.resume.ResumeIntentsFragment;
import cn.qingchengfit.recruit.views.resume.ResumeShowImgsFragment;
import cn.qingchengfit.recruit.views.resume.ResumeWorkExpListFragment;
import cn.qingchengfit.recruit.views.resume.ResumeWorkExpPreviewFragment;
import cn.qingchengfit.recruit.views.resume.WorkExpSyncDetailFragment;
import cn.qingchengfit.recruit.views.resume.WorkExpeEditFragment;
import cn.qingchengfit.saas.views.fragments.ChooseGymFragment;
import cn.qingchengfit.saas.views.fragments.EditGymInfoFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/5/31.
 */
@Subcomponent(modules = Recruit.SeekPositionHomeFragmentModule.class) public interface Recruit
    extends AndroidInjector<RecruitActivity> {

  //@Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitActivity> {
  //}
  //@Module(subcomponents = RecruitSubComponent.class) abstract class RecruitModule {
  //    @Binds @IntoMap @ActivityKey(RecruitActivity.class)
  //    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(RecruitSubComponent.Builder builder);
  //}

  @Subcomponent() public interface SeekPositionHomeFragmentSubcomponent
      extends AndroidInjector<SeekPositionHomeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SeekPositionHomeFragment> {
    }
  }

  @Subcomponent() public interface MyPositionsInfoFragmentSubcomponent
      extends AndroidInjector<MyPositionsInfoFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MyPositionsInfoFragment> {
    }
  }

  @Subcomponent() public interface DialogSendResumeFragmentSubcomponent
      extends AndroidInjector<DialogSendResumeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<DialogSendResumeFragment> {
    }
  }

  @Subcomponent() public interface RecruitGymDescFragmentSubcomponent
      extends AndroidInjector<RecruitGymDescFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitGymDescFragment> {
    }
  }

  @Subcomponent() public interface RecruitGymDetailFragmentSubcomponent
      extends AndroidInjector<RecruitGymDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitGymDetailFragment> {
    }
  }

  @Subcomponent() public interface RecruitPositionDetailFragmentSubcomponent
      extends AndroidInjector<RecruitPositionDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitPositionDetailFragment> {
    }
  }

  @Subcomponent() public interface RecruitPositionsFragmentSubcomponent
      extends AndroidInjector<RecruitPositionsFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitPositionsFragment> {
    }
  }

  @Subcomponent() public interface RecruitPositionsInGymFragmentSubcomponent
      extends AndroidInjector<RecruitPositionsInGymFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitPositionsInGymFragment> {
    }
  }

  @Subcomponent() public interface RecruitPositionsInvitedFragmentSubcomponent
      extends AndroidInjector<RecruitPositionsInvitedFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitPositionsInvitedFragment> {
    }
  }

  @Subcomponent() public interface RecruitPositionsSentFragmentSubcomponent
      extends AndroidInjector<RecruitPositionsSentFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitPositionsSentFragment> {
    }
  }

  @Subcomponent() public interface RecruitPositionsStarredFragmentSubcomponent
      extends AndroidInjector<RecruitPositionsStarredFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitPositionsStarredFragment> {
    }
  }

  @Subcomponent() public interface ResumeHomeFragmentSubcomponent
      extends AndroidInjector<ResumeHomeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeHomeFragment> {
    }
  }

  @Subcomponent() public interface ResumeCertificateListFragmentSubcomponent
      extends AndroidInjector<ResumeCertificateListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeCertificateListFragment> {
    }
  }

  @Subcomponent() public interface ResumeEduExplistFragmentSubcomponent
      extends AndroidInjector<ResumeEduExpListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeEduExpListFragment> {
    }
  }

  @Subcomponent() public interface AddEduExpFragmentSubcomponent
      extends AndroidInjector<AddEduExpFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AddEduExpFragment> {
    }
  }

  @Subcomponent() public interface ResumeIntentsFragmentSubcomponent
      extends AndroidInjector<ResumeIntentsFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeIntentsFragment> {
    }
  }

  @Subcomponent() public interface ResumeWorkExpListFragmentSubcomponent
      extends AndroidInjector<ResumeWorkExpListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeWorkExpListFragment> {
    }
  }

  @Subcomponent() public interface WorkExpSyncDetailFragmentSubcomponent
      extends AndroidInjector<WorkExpSyncDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WorkExpSyncDetailFragment> {
    }
  }

  @Subcomponent() public interface WorkexpeEditFragmentSubcomponent
      extends AndroidInjector<WorkExpeEditFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WorkExpeEditFragment> {
    }
  }

  @Subcomponent() public interface ResumeShowImgsFragmentSubcomponent
      extends AndroidInjector<ResumeShowImgsFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeShowImgsFragment> {
    }
  }

  @Subcomponent() public interface RecordEditFragmentSubcomponent
      extends AndroidInjector<RecordEditFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecordEditFragment> {
    }
  }

  @Subcomponent() public interface ResumeBaseInfoFragmentSubcomponent
      extends AndroidInjector<ResumeBaseInfoFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeBaseInfoFragment> {
    }
  }

  @Subcomponent() public interface ResumeEditDescFragmentSubcomponent
      extends AndroidInjector<ResumeEditDescFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeEditDescFragment> {
    }
  }

  @Subcomponent() public interface ResumeWorkExpPreviewFragmentSubcomponent
      extends AndroidInjector<ResumeWorkExpPreviewFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeWorkExpPreviewFragment> {
    }
  }

  @Subcomponent() public interface ResumeMarketHomeFragmentSubcomponent
      extends AndroidInjector<ResumeMarketHomeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeMarketHomeFragment> {
    }
  }

  @Subcomponent() public interface RecruitManageFragmentSubcomponent
      extends AndroidInjector<RecruitManageFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitManageFragment> {
    }
  }

  @Subcomponent() public interface RecruitGymDetailEmployerFragmentSubcomponent
      extends AndroidInjector<RecruitGymDetailEmployerFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitGymDetailEmployerFragment> {
    }
  }

  @Subcomponent() public interface RecruitPermsionFragmentSubcomponent
      extends AndroidInjector<RecruitPermissionFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitPermissionFragment> {
    }
  }

  @Subcomponent() public interface ResumeListFragmentSubcomponent
      extends AndroidInjector<ResumeListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeListFragment> {
    }
  }

  @Subcomponent() public interface RecruitPositionDetailEmployerFragmentSubcomponent
      extends AndroidInjector<RecruitPositionDetailEmployerFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitPositionDetailEmployerFragment> {
    }
  }

  @Subcomponent() public interface ResumeHandleFragmentSubcomponent
      extends AndroidInjector<ResumeHandleFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeHandleFragment> {
    }
  }

  @Subcomponent() public interface RecruitWriteGymIntroFragmentSubcomponent
      extends AndroidInjector<RecruitWriteGymIntroFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitWriteGymIntroFragment> {
    }
  }

  @Subcomponent() public interface JobFairListFragmentSubcomponent
      extends AndroidInjector<JobFairListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<JobFairListFragment> {
    }
  }

  @Subcomponent() public interface RecruitStaffMyJobFairFragmentSubcomponent
      extends AndroidInjector<RecruitStaffMyJobFairFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitStaffMyJobFairFragment> {
    }
  }

  @Subcomponent() public interface RecruitUserMyJobFairFragmentSubcomponent
      extends AndroidInjector<RecruitUserMyJobFairFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitUserMyJobFairFragment> {
    }
  }

  /**
   * {@link ResumeStarredFragment}
   */
  @Subcomponent() public interface ResumeStarredFragmentSubcomponent
      extends AndroidInjector<ResumeStarredFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeStarredFragment> {
    }
  }

  @Subcomponent() public interface JobfairDetailFragmentSubcomponent
      extends AndroidInjector<JobfairDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<JobfairDetailFragment> {
    }
  }

  @Subcomponent() public interface JobfairSignUpFragmentSubcomponent
      extends AndroidInjector<JobfairSignUpFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<JobfairSignUpFragment> {
    }
  }

  @Subcomponent() public interface JobsListFragmentSubcomponent
      extends AndroidInjector<JobsListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<JobsListFragment> {
    }
  }

  @Subcomponent() public interface RecruitPublishJobFragmentSubcomponent
      extends AndroidInjector<RecruitPublishJobFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitPublishJobFragment> {
    }
  }

  @Subcomponent() public interface ResumeDetailFragmentSubcomponent
      extends AndroidInjector<ResumeDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeDetailFragment> {
    }
  }

  @Subcomponent() public interface EditGymInfoFragmentSubcomponent
      extends AndroidInjector<EditGymInfoFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<EditGymInfoFragment> {
    }
  }

  @Subcomponent() public interface ResumeRecievedFragmentSubcomponent
      extends AndroidInjector<ResumeRecievedFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ResumeRecievedFragment> {
    }
  }

  @Subcomponent() public interface ChooseGymFragmentSubcomponent
      extends AndroidInjector<ChooseGymFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseGymFragment> {
    }
  }

  @Module(subcomponents = SeekPositionHomeFragmentSubcomponent.class)
  abstract class SeekPositionHomeFragmentModule {
    @Binds @IntoMap @FragmentKey(SeekPositionHomeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SeekPositionHomeFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = MyPositionsInfoFragmentSubcomponent.class)
  abstract class MyPositionsInfoFragmentModule {
    @Binds @IntoMap @FragmentKey(MyPositionsInfoFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        MyPositionsInfoFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = DialogSendResumeFragmentSubcomponent.class)
  abstract class DialogSendResumeFragmentModule {
    @Binds @IntoMap @FragmentKey(DialogSendResumeFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        DialogSendResumeFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitGymDescFragmentSubcomponent.class)
  abstract class RecruitGymDescFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitGymDescFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitGymDescFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitGymDetailFragmentSubcomponent.class)
  abstract class RecruitGymDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitGymDetailFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitGymDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitPositionDetailFragmentSubcomponent.class)
  abstract class RecruitPositionDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitPositionDetailFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitPositionDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitPositionsFragmentSubcomponent.class)
  abstract class RecruitPositionsFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitPositionsFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitPositionsFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitPositionsInGymFragmentSubcomponent.class)
  abstract class RecruitPositionsInGymFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitPositionsInGymFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitPositionsInGymFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitPositionsInvitedFragmentSubcomponent.class)
  abstract class RecruitPositionsInvitedFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitPositionsInvitedFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitPositionsInvitedFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitPositionsSentFragmentSubcomponent.class)
  abstract class RecruitPositionsSentFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitPositionsSentFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitPositionsSentFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitPositionsStarredFragmentSubcomponent.class)
  abstract class RecruitPositionsStarredFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitPositionsStarredFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitPositionsStarredFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeHomeFragmentSubcomponent.class)
  abstract class ResumeHomeFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeHomeFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeHomeFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeCertificateListFragmentSubcomponent.class)
  abstract class ResumeCertificateListFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeCertificateListFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeCertificateListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeEduExplistFragmentSubcomponent.class)
  abstract class ResumeEduExplistFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeEduExpListFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeEduExplistFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = AddEduExpFragmentSubcomponent.class)
  abstract class AddEduExpFragmentModule {
    @Binds @IntoMap @FragmentKey(AddEduExpFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AddEduExpFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeIntentsFragmentSubcomponent.class)
  abstract class ResumeIntentsFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeIntentsFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeIntentsFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeWorkExpListFragmentSubcomponent.class)
  abstract class ResumeWorkExpListFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeWorkExpListFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeWorkExpListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = WorkExpSyncDetailFragmentSubcomponent.class)
  abstract class WorkExpSyncDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(WorkExpSyncDetailFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WorkExpSyncDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = WorkexpeEditFragmentSubcomponent.class)
  abstract class WorkexpeEditFragmentModule {
    @Binds @IntoMap @FragmentKey(WorkExpeEditFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WorkexpeEditFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeShowImgsFragmentSubcomponent.class)
  abstract class ResumeShowImgsFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeShowImgsFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeShowImgsFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecordEditFragmentSubcomponent.class)
  abstract class RecordEditFragmentModule {
    @Binds @IntoMap @FragmentKey(RecordEditFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecordEditFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeBaseInfoFragmentSubcomponent.class)
  abstract class ResumeBaseInfoFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeBaseInfoFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeBaseInfoFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeEditDescFragmentSubcomponent.class)
  abstract class ResumeEditDescFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeEditDescFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeEditDescFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeWorkExpPreviewFragmentSubcomponent.class)
  abstract class ResumeWorkExpPreviewFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeWorkExpPreviewFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeWorkExpPreviewFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeMarketHomeFragmentSubcomponent.class)
  abstract class ResumeMarketHomeFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeMarketHomeFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeMarketHomeFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitManageFragmentSubcomponent.class)
  abstract class RecruitManageFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitManageFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitManageFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitGymDetailEmployerFragmentSubcomponent.class)
  abstract class RecruitGymDetailEmployerFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitGymDetailEmployerFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitGymDetailEmployerFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitPermsionFragmentSubcomponent.class)
  abstract class RecruitPermsionFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitPermissionFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitPermsionFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeListFragmentSubcomponent.class)
  abstract class ResumeListFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeListFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitPositionDetailEmployerFragmentSubcomponent.class)
  abstract class RecruitPositionDetailEmployerFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitPositionDetailEmployerFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitPositionDetailEmployerFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeHandleFragmentSubcomponent.class)
  abstract class ResumeHandleFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeHandleFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeHandleFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitWriteGymIntroFragmentSubcomponent.class)
  abstract class RecruitWriteGymIntroFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitWriteGymIntroFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitWriteGymIntroFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = JobFairListFragmentSubcomponent.class)
  abstract class JobFairListFragmentModule {
    @Binds @IntoMap @FragmentKey(JobFairListFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        JobFairListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitStaffMyJobFairFragmentSubcomponent.class)
  abstract class RecruitStaffMyJobFairFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitStaffMyJobFairFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitStaffMyJobFairFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitUserMyJobFairFragmentSubcomponent.class)
  abstract class RecruitUserMyJobFairFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitUserMyJobFairFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitUserMyJobFairFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeStarredFragmentSubcomponent.class)
  abstract class ResumeStarredFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeStarredFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeStarredFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = JobfairDetailFragmentSubcomponent.class)
  abstract class JobfairDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(JobfairDetailFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        JobfairDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = JobfairSignUpFragmentSubcomponent.class)
  abstract class JobfairSignUpFragmentModule {
    @Binds @IntoMap @FragmentKey(JobfairSignUpFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        JobfairSignUpFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = JobsListFragmentSubcomponent.class)
  abstract class JobsListFragmentModule {
    @Binds @IntoMap @FragmentKey(JobsListFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        JobsListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitPublishJobFragmentSubcomponent.class)
  abstract class RecruitPublishJobFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitPublishJobFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitPublishJobFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeDetailFragmentSubcomponent.class)
  abstract class ResumeDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeDetailFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = EditGymInfoFragmentSubcomponent.class)
  abstract class EditGymInfoFragmentModule {
    @Binds @IntoMap @FragmentKey(EditGymInfoFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        EditGymInfoFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ResumeRecievedFragmentSubcomponent.class)
  abstract class ResumeRecievedFragmentModule {
    @Binds @IntoMap @FragmentKey(ResumeRecievedFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ResumeRecievedFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseGymFragmentSubcomponent.class)
  abstract class ChooseGymFragmentModule {
    @Binds @IntoMap @FragmentKey(ChooseGymFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseGymFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface JobFairSuccessFragmentSubcomponent
      extends AndroidInjector<JobFairSuccessFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<JobFairSuccessFragment> {
    }
  }

  @Module(subcomponents = JobFairSuccessFragmentSubcomponent.class)
  abstract class JobFairSuccessFragmentModule {
    @Binds @IntoMap @FragmentKey(JobFairSuccessFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        JobFairSuccessFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface JobFairsAllFragmentSubcomponent
      extends AndroidInjector<JobFairsAllFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<JobFairsAllFragment> {
    }
  }

  @Module(subcomponents = JobFairsAllFragmentSubcomponent.class)
  abstract class JobFairsAllFragmentModule {
    @Binds @IntoMap @FragmentKey(JobFairsAllFragment.class)
    abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        JobFairsAllFragmentSubcomponent.Builder builder);
  }
}
