package cn.qingchengfit.recruit.di;

import cn.qingchengfit.recruit.views.RecruitActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

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
@Module public abstract class BindRecruitModule {
  @ContributesAndroidInjector(modules = {
      AndroidSupportInjectionModule.class, Recruit.SeekPositionHomeFragmentModule.class, Recruit.DialogSendResumeFragmentModule.class,
      Recruit.MyPositionsInfoFragmentModule.class, Recruit.RecruitGymDescFragmentModule.class, Recruit.RecruitGymDetailFragmentModule.class,
      Recruit.RecruitPositionDetailFragmentModule.class, Recruit.RecruitPositionsFragmentModule.class,
      Recruit.RecruitPositionsSentFragmentModule.class, Recruit.RecruitPositionsStarredFragmentModule.class,
      Recruit.RecruitPositionsInvitedFragmentModule.class, Recruit.RecruitPositionsInGymFragmentModule.class,
      Recruit.ResumeHomeFragmentModule.class, Recruit.ResumeCertificateListFragmentModule.class,
      Recruit.ResumeEduExplistFragmentModule.class, Recruit.ResumeIntentsFragmentModule.class, Recruit.AddEduExpFragmentModule.class,
      Recruit.ResumeWorkExpListFragmentModule.class, Recruit.WorkExpSyncDetailFragmentModule.class,
      Recruit.WorkexpeEditFragmentModule.class, Recruit.ResumeShowImgsFragmentModule.class, Recruit.RecordEditFragmentModule.class,
      Recruit.ResumeBaseInfoFragmentModule.class, Recruit.ResumeEditDescFragmentModule.class,
      Recruit.ResumeWorkExpPreviewFragmentModule.class,
      Recruit.ResumeMarketHomeFragmentModule.class, Recruit.RecruitManageFragmentModule.class,
      Recruit.RecruitGymDetailEditFragmentModule.class,
      Recruit.RecruitGymDetailEmployerFragmentModule.class,
      Recruit.RecruitPermsionFragmentModule.class, Recruit.RecruitPublishJobFragmentModule.class,

  }) abstract RecruitActivity contributeRecruitActivityInjector();
}
