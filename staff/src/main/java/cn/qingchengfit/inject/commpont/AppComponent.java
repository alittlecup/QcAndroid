package cn.qingchengfit.inject.commpont;

import android.app.Activity;
import android.support.v4.app.Fragment;
import cn.qingchengfit.card.CardModule;
import cn.qingchengfit.card.view.SimpleCardListFragment;
import cn.qingchengfit.checkout.CheckViewModule;
import cn.qingchengfit.checkout.di.BindCheckoutCounterActivity;
import cn.qingchengfit.gym.GymModule;
import cn.qingchengfit.gym.gymconfig.views.UpgradeDoneFragment;
import cn.qingchengfit.inject.moudle.AppAbstractModule;
import cn.qingchengfit.inject.moudle.AppModel;
import cn.qingchengfit.inject.moudle.CardTypeWrapperModule;
import cn.qingchengfit.inject.moudle.RealcardModule;
import cn.qingchengfit.inject.moudle.StaffWrapperMoudle;
import cn.qingchengfit.login.di.BindLoginActivity;
import cn.qingchengfit.notisetting.view.NotiSettingChargeHistoryFragment;
import cn.qingchengfit.notisetting.view.NotiSettingChargeResultFragment;
import cn.qingchengfit.notisetting.view.NotiSettingHomeFragment;
import cn.qingchengfit.notisetting.view.NotiSettingMsgChargeFragment;
import cn.qingchengfit.notisetting.view.NotiSettingMsgDetailFragment;
import cn.qingchengfit.notisetting.view.NotiSettingMsgRuleFragment;
import cn.qingchengfit.notisetting.view.NotiSettingSendListDetailFragment;
import cn.qingchengfit.notisetting.view.NotiSettingWxTemplateFragment;
import cn.qingchengfit.notisetting.view.SendChannelTabFragment;
import cn.qingchengfit.recruit.ChooseStaffFragment;
import cn.qingchengfit.recruit.di.BindRecruitModule;
import cn.qingchengfit.recruit.di.BindSeacherOrgModule;
import cn.qingchengfit.recruit.views.JobSearchChatActivity;
import cn.qingchengfit.saas.di.BindSaas;
import cn.qingchengfit.saas.views.fragments.ChooseGymFragment;
import cn.qingchengfit.saas.views.fragments.EditGymInfoFragment;
import cn.qingchengfit.saasbase.article.ArticleCommentsListFragment;
import cn.qingchengfit.saasbase.article.ArticleReplyFragment;
import cn.qingchengfit.saasbase.cards.views.CardProtocolActivity;
import cn.qingchengfit.saasbase.cards.views.offday.AddOffDayFragment;
import cn.qingchengfit.saasbase.chat.ChatChooseInGymFragment;
import cn.qingchengfit.saasbase.chat.ChatFriendAllChooseFragment;
import cn.qingchengfit.saasbase.chat.ConversationFriendsFragment;
import cn.qingchengfit.saasbase.chat.RecruitMessageListFragment;
import cn.qingchengfit.saasbase.common.bottom.BottomBuyLimitFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchPayOnlineFragment;
import cn.qingchengfit.saasbase.course.batch.views.UpgradeInfoDialogFragment;
import cn.qingchengfit.saasbase.di.BindImportExportActivity;
import cn.qingchengfit.saasbase.di.BindSaasCommActivity;
import cn.qingchengfit.saasbase.di.BindUserActivity;
import cn.qingchengfit.saasbase.mvvm_student.di.ViewModelModule;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.widget.BaseStatementChartFragment;
import cn.qingchengfit.saascommon.widget.FlexableListFragment;
import cn.qingchengfit.shop.di.BindShopActivity;
import cn.qingchengfit.staff.di.BindSettingActivity;
import cn.qingchengfit.staff.di.BindSignInActivity;
import cn.qingchengfit.staff.di.BindStaffCourseActivity;
import cn.qingchengfit.staff.di.BindStaffStaffActivity;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.allocate.FilterFragment;
import cn.qingchengfit.staffkit.allocate.coach.AllocateCoachActivity;
import cn.qingchengfit.staffkit.allocate.coach.MutiChooseCoachActivity;
import cn.qingchengfit.staffkit.allocate.coach.fragment.AddStudentFragment;
import cn.qingchengfit.staffkit.allocate.coach.fragment.AllocateCoachListFragment;
import cn.qingchengfit.staffkit.allocate.coach.fragment.CoachStudentDetailFragment;
import cn.qingchengfit.staffkit.allocate.coach.fragment.OperationStudentFragment;
import cn.qingchengfit.staffkit.dianping.DianPingModule;
import cn.qingchengfit.staffkit.train.SignUpChooseActivity;
import cn.qingchengfit.staffkit.train.SignUpDetailActivity;
import cn.qingchengfit.staffkit.train.fragment.CreateGroupFragment;
import cn.qingchengfit.staffkit.train.fragment.DeleteMemberFragment;
import cn.qingchengfit.staffkit.train.fragment.MemberOperationFragment;
import cn.qingchengfit.staffkit.train.fragment.RankCountFragment;
import cn.qingchengfit.staffkit.train.fragment.SignUpChangeNameFragment;
import cn.qingchengfit.staffkit.train.fragment.SignUpFormGroupFragment;
import cn.qingchengfit.staffkit.train.fragment.SignUpFormHomeFragment;
import cn.qingchengfit.staffkit.train.fragment.SignUpFormPersonalFragment;
import cn.qingchengfit.staffkit.train.fragment.SignUpGroupDetailFragment;
import cn.qingchengfit.staffkit.train.fragment.SignUpPersonalDetailFragment;
import cn.qingchengfit.staffkit.train.fragment.TrainChooseGymFragment;
import cn.qingchengfit.staffkit.train.moudle.TrainMoudle;
import cn.qingchengfit.staffkit.views.AddBrandActivity;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.ChooseBrandActivity;
import cn.qingchengfit.staffkit.views.ChooseBrandFragment;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.EditTextActivity;
import cn.qingchengfit.staffkit.views.GuideActivity;
import cn.qingchengfit.staffkit.views.GuideChooseBrandAcitivity;
import cn.qingchengfit.staffkit.views.GymDetailShowGuideDialogFragment;
import cn.qingchengfit.staffkit.views.MainFirstFragment;
import cn.qingchengfit.staffkit.views.PopFromBottomActivity;
import cn.qingchengfit.staffkit.views.allotsales.AllotSalesActivity;
import cn.qingchengfit.staffkit.views.allotsales.MultiModifyFragment;
import cn.qingchengfit.staffkit.views.allotsales.SalesListFragment;
import cn.qingchengfit.staffkit.views.allotsales.choose.MutiChooseSalersActivity;
import cn.qingchengfit.staffkit.views.course.CourseReverseFragment;
import cn.qingchengfit.staffkit.views.custom.SimpleChooseFragment;
import cn.qingchengfit.staffkit.views.custom.SimpleImgDialog;
import cn.qingchengfit.staffkit.views.export.ExportRecordFragment;
import cn.qingchengfit.staffkit.views.export.ExportSendEmailFragment;
import cn.qingchengfit.staffkit.views.gym.AddBrandInMainFragment;
import cn.qingchengfit.staffkit.views.gym.AddNewCoachFragment;
import cn.qingchengfit.staffkit.views.gym.ChooseCoachFragment;
import cn.qingchengfit.staffkit.views.gym.GymActivity;
import cn.qingchengfit.staffkit.views.gym.GymDetailFragment;
import cn.qingchengfit.staffkit.views.gym.GymInfoFragment;
import cn.qingchengfit.staffkit.views.gym.GymInfoNoEditFragment;
import cn.qingchengfit.staffkit.views.gym.GymMoreFragment;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.staffkit.views.gym.QuitGymFragment;
import cn.qingchengfit.staffkit.views.gym.RenewalHistoryFragment;
import cn.qingchengfit.staffkit.views.gym.SetGymFragment;
import cn.qingchengfit.staffkit.views.gym.WriteAddressFragment;
import cn.qingchengfit.staffkit.views.gym.WriteDescFragment;
import cn.qingchengfit.staffkit.views.gym.coach.CoachDetailFragment;
import cn.qingchengfit.staffkit.views.gym.site.AddNewSiteFragment;
import cn.qingchengfit.staffkit.views.gym.site.ChooseSiteFragment;
import cn.qingchengfit.staffkit.views.gym.site.MutiChooseSiteFragment;
import cn.qingchengfit.staffkit.views.gym.site.SiteDetailFragment;
import cn.qingchengfit.staffkit.views.gym.site.SiteListFragment;
import cn.qingchengfit.staffkit.views.gym.staff.StaffDetailFragment;
import cn.qingchengfit.staffkit.views.gym.upgrate.GymExpireFragment;
import cn.qingchengfit.staffkit.views.gym.upgrate.TrialProDialogFragment;
import cn.qingchengfit.staffkit.views.gym.upgrate.UpgrateGymFragment;
import cn.qingchengfit.staffkit.views.login.SplashActivity;
import cn.qingchengfit.staffkit.views.main.ChooseBrandInMainFragment;
import cn.qingchengfit.staffkit.views.main.GymsFragment;
import cn.qingchengfit.staffkit.views.main.HomeFragment;
import cn.qingchengfit.staffkit.views.main.HomeUnLoginFragment;
import cn.qingchengfit.staffkit.views.main.MainMsgFragment;
import cn.qingchengfit.staffkit.views.main.QcVipFragment;
import cn.qingchengfit.staffkit.views.main.SetGymInMainFragment;
import cn.qingchengfit.staffkit.views.main.SettingFragment;
import cn.qingchengfit.staffkit.views.main.UnloginAdFragment;
import cn.qingchengfit.staffkit.views.notification.NotificationActivity;
import cn.qingchengfit.staffkit.views.notification.page.NotificationFragment;
import cn.qingchengfit.staffkit.views.schedule.ScheduleActivity;
import cn.qingchengfit.staffkit.views.schedule.ScheduleListFragment;
import cn.qingchengfit.staffkit.views.setting.BrandManageActivity;
import cn.qingchengfit.staffkit.views.setting.FixCheckinFragment;
import cn.qingchengfit.staffkit.views.setting.FixNotifySettingFragment;
import cn.qingchengfit.staffkit.views.setting.FixSelfInfoFragment;
import cn.qingchengfit.staffkit.views.setting.ReportFragment;
import cn.qingchengfit.staffkit.views.setting.brand.BrandCreatorEditFragment;
import cn.qingchengfit.staffkit.views.setting.brand.BrandDetailFragment;
import cn.qingchengfit.staffkit.views.setting.brand.BrandEditFragment;
import cn.qingchengfit.staffkit.views.setting.brand.BrandManageFragment;
import cn.qingchengfit.staffkit.views.signin.SignInCloseFragment;
import cn.qingchengfit.staffkit.views.signin.SignInConfigFragment;
import cn.qingchengfit.staffkit.views.signin.SignInDetailFragment;
import cn.qingchengfit.staffkit.views.signin.SignInHomeFragment;
import cn.qingchengfit.staffkit.views.signin.SignInLogFragment;
import cn.qingchengfit.staffkit.views.signin.SignInManualActivity;
import cn.qingchengfit.staffkit.views.signin.SignInStudentListFragment;
import cn.qingchengfit.staffkit.views.signin.config.SignInCardFragment;
import cn.qingchengfit.staffkit.views.signin.config.SignInChildCodeFragment;
import cn.qingchengfit.staffkit.views.signin.config.SignInCodeFragment;
import cn.qingchengfit.staffkit.views.signin.config.SignInConfigScreenFragment;
import cn.qingchengfit.staffkit.views.signin.config.SignInTypeFragment;
import cn.qingchengfit.staffkit.views.signin.config.SigninConfigCardtypeListFragment;
import cn.qingchengfit.staffkit.views.signin.config.SigninConfigListFragment;
import cn.qingchengfit.staffkit.views.signin.config.SinginConfigWardrobeFragment;
import cn.qingchengfit.staffkit.views.signin.in.SignInFragment;
import cn.qingchengfit.staffkit.views.signin.in.SignInListFragment;
import cn.qingchengfit.staffkit.views.signin.in.SignInManualFragment;
import cn.qingchengfit.staffkit.views.signin.out.SignOutFragment;
import cn.qingchengfit.staffkit.views.signin.out.SignOutListFragment;
import cn.qingchengfit.staffkit.views.signin.out.SignOutManualFragment;
import cn.qingchengfit.staffkit.views.signin.zq.AddZqFragment;
import cn.qingchengfit.staffkit.views.signin.zq.EditZqFragment;
import cn.qingchengfit.staffkit.views.signin.zq.ZqAccessFragment;
import cn.qingchengfit.staffkit.views.statement.ContainerActivity;
import cn.qingchengfit.staffkit.views.statement.DataStatementFragment;
import cn.qingchengfit.staffkit.views.statement.SaleFilterActivity;
import cn.qingchengfit.staffkit.views.statement.StatmentFilterActivity;
import cn.qingchengfit.staffkit.views.statement.custom.CustomSaleFragment;
import cn.qingchengfit.staffkit.views.statement.custom.CustomSigninFragment;
import cn.qingchengfit.staffkit.views.statement.custom.CustomStatmentFragment;
import cn.qingchengfit.staffkit.views.statement.detail.CourseCardFormFragment;
import cn.qingchengfit.staffkit.views.statement.detail.CourseTypeFormFragment;
import cn.qingchengfit.staffkit.views.statement.detail.SaleCardTypeFragment;
import cn.qingchengfit.staffkit.views.statement.detail.SaleDetailFragment;
import cn.qingchengfit.staffkit.views.statement.detail.SaleTradeTypeFormFragment;
import cn.qingchengfit.staffkit.views.statement.detail.SigninReportFormFragment;
import cn.qingchengfit.staffkit.views.statement.detail.SigninReportFragment;
import cn.qingchengfit.staffkit.views.statement.detail.StatementDetailFragment;
import cn.qingchengfit.staffkit.views.statement.excel.OutExcelFragment;
import cn.qingchengfit.staffkit.views.statement.filter.CardTypeChooseDialogFragment;
import cn.qingchengfit.staffkit.views.statement.filter.CoachChooseDialogFragment;
import cn.qingchengfit.staffkit.views.statement.filter.CourseChooseDialogFragment;
import cn.qingchengfit.staffkit.views.statement.filter.SalerChooseDialogFragment;
import cn.qingchengfit.staffkit.views.statement.glance.SaleGlanceFragment;
import cn.qingchengfit.staffkit.views.statement.glance.SigninGlanceFragment;
import cn.qingchengfit.staffkit.views.statement.glance.StatementGlanceFragment;
import cn.qingchengfit.staffkit.views.student.ChooseOriginActivity;
import cn.qingchengfit.staffkit.views.student.ChooseReferrerActivity;
import cn.qingchengfit.staffkit.views.student.MutiChooseStudentActivity;
import cn.qingchengfit.staffkit.views.student.MutiChooseStudentFragment;
import cn.qingchengfit.staffkit.views.student.StudentActivity;
import cn.qingchengfit.staffkit.views.student.StudentOperationFragment;
import cn.qingchengfit.staffkit.views.student.StudentSearchFragment;
import cn.qingchengfit.staffkit.views.student.attendance.AbsenceStuentListFragment;
import cn.qingchengfit.staffkit.views.student.attendance.AttendanceActivity;
import cn.qingchengfit.staffkit.views.student.attendance.AttendanceHomeFragment;
import cn.qingchengfit.staffkit.views.student.attendance.AttendanceNotSignFragment;
import cn.qingchengfit.staffkit.views.student.attendance.AttendanceRankFragment;
import cn.qingchengfit.staffkit.views.student.attendance.AttendanceStaticFragment;
import cn.qingchengfit.staffkit.views.student.attendance.FilterCustomFragment;
import cn.qingchengfit.staffkit.views.student.bodytest.BodyTestFragment;
import cn.qingchengfit.staffkit.views.student.bodytest.BodyTestListFragment;
import cn.qingchengfit.staffkit.views.student.bodytest.ModifyBodyTestFragment;
import cn.qingchengfit.staffkit.views.student.choose.ChooseStudentListFragment;
import cn.qingchengfit.staffkit.views.student.choose.MultiChooseStudentWithFilterFragment;
import cn.qingchengfit.staffkit.views.student.choose.StudentFilterWithBirthFragment;
import cn.qingchengfit.staffkit.views.student.detail.StudentHomeFragment;
import cn.qingchengfit.staffkit.views.student.detail.StudentMoreInfoFragment;
import cn.qingchengfit.staffkit.views.student.detail.StudentSignInImageFragment;
import cn.qingchengfit.staffkit.views.student.detail.StudentsCardsFragment;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.staffkit.views.student.edit.EditStudentInfoFragment;
import cn.qingchengfit.staffkit.views.student.filter.ReferrerFragment;
import cn.qingchengfit.staffkit.views.student.filter.SourceFragment;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterFragment;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpActivity;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpDataStatistics0Fragment;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpDataStatistics1Fragment;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpDataStatistics2Fragment;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpDataStatisticsBaseFragment;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpDataStatisticsFragment;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpDataTransfer0Fragment;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpDataTransferFragment;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpFragment;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpStatusFragment;
import cn.qingchengfit.staffkit.views.student.followup.LatestTimeFragment;
import cn.qingchengfit.staffkit.views.student.followup.TopFilterLatestFollowFragment;
import cn.qingchengfit.staffkit.views.student.followup.TopFilterRegisterFragment;
import cn.qingchengfit.staffkit.views.student.followup.TopFilterSaleFragment;
import cn.qingchengfit.staffkit.views.student.followup.TopFilterSourceFragment;
import cn.qingchengfit.staffkit.views.student.list.StudentListFragment;
import cn.qingchengfit.staffkit.views.student.score.BaseConfigFragment;
import cn.qingchengfit.staffkit.views.student.score.ConfigFragment;
import cn.qingchengfit.staffkit.views.student.score.ScoreAwardAddFragment;
import cn.qingchengfit.staffkit.views.student.score.ScoreDetailActivity;
import cn.qingchengfit.staffkit.views.student.score.ScoreDetailFragment;
import cn.qingchengfit.staffkit.views.student.score.ScoreHomeFragment;
import cn.qingchengfit.staffkit.views.student.score.ScoreModifyFragment;
import cn.qingchengfit.staffkit.views.student.score.ScoreRuleAddFragemnt;
import cn.qingchengfit.staffkit.views.wardrobe.WardrobeActivity;
import cn.qingchengfit.staffkit.views.wardrobe.WardrobePayBottomFragment;
import cn.qingchengfit.staffkit.views.wardrobe.add.DistrictAddFragment;
import cn.qingchengfit.staffkit.views.wardrobe.add.WardrobeAddFragment;
import cn.qingchengfit.staffkit.views.wardrobe.back.WardrobeReturnDialog;
import cn.qingchengfit.staffkit.views.wardrobe.back.WardrobeReturnFragment;
import cn.qingchengfit.staffkit.views.wardrobe.choose.ChooseMainFragment;
import cn.qingchengfit.staffkit.views.wardrobe.choose.ChooseRegionFragment;
import cn.qingchengfit.staffkit.views.wardrobe.choose.ChooseWardrobeActivity;
import cn.qingchengfit.staffkit.views.wardrobe.choose.SearchResultFragment;
import cn.qingchengfit.staffkit.views.wardrobe.district.DistrictListFragment;
import cn.qingchengfit.staffkit.views.wardrobe.edit.WardrobeEditFragment;
import cn.qingchengfit.staffkit.views.wardrobe.hire.WardrobeContinueHireFragment;
import cn.qingchengfit.staffkit.views.wardrobe.hire.WardrobeDetailFragment;
import cn.qingchengfit.staffkit.views.wardrobe.hire.WardrobeLongHireFragment;
import cn.qingchengfit.staffkit.views.wardrobe.hire.WardrobeShortHireFragment;
import cn.qingchengfit.staffkit.views.wardrobe.main.WardrobeListFragment;
import cn.qingchengfit.staffkit.views.wardrobe.main.WardrobeMainFragment;
import cn.qingchengfit.student.StudentViewModel;
import cn.qingchengfit.student.di.BindStudentActivity;
import cn.qingchengfit.student.view.detail.NotSignFilterFragment;
import cn.qingchengfit.student.view.followrecord.FollowRecordPage;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.ChooseAddressFragment;
import cn.qingchengfit.views.fragments.WebFragment;
import cn.qingchengfit.views.fragments.WebFragmentNoFresh;
import cn.qingchengfit.weex.di.WeexModule;
import cn.qingchengfit.wxpreview.old.newa.WxPreviewModule;
import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import javax.inject.Singleton;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/1/20 2016.
 */
//压制了 未使用 拼写检查 和public

@SuppressWarnings({ "unused", "SpellCheckingInspection", "UnnecessaryInterfaceModifier" })
@Component(modules = {
    AppModel.class, AppAbstractModule.class, AndroidInjectionModule.class,
    AndroidSupportInjectionModule.class, AppComponent.ContainterModule.class,
    BindRecruitModule.class, BindSeacherOrgModule.class, BindImportExportActivity.class,

    BindStaffCourseActivity.class,BindSignInActivity.class,
    BindStaffStaffActivity.class, CardModule.class,
     BindSaasCommActivity.class,
    BindUserActivity.class, BindLoginActivity.class,
    BindStudentActivity.class, WeexModule.class, ViewModelModule.class,
    BindShopActivity.class,StudentViewModel.class,BindCheckoutCounterActivity.class,CheckViewModule.class,
    WxPreviewModule.class, DianPingModule.class, BindSettingActivity.class, GymModule.class,

    AppComponent.SplashModule.class, AppComponent.MainFirstModule.class,
    AppComponent.MainMsgModule.class, AppComponent.UnloginAdModule.class,
    AppComponent.HomeModule.class, AppComponent.QcVipModule.class,
    AppComponent.BaseStatementChartModule.class, AppComponent.YourFragmentModule.class,
    AppComponent.ChooseModule.class, AppComponent.MainModule.class, AppComponent.GymsModule.class,
    AppComponent.SettingModule.class, AppComponent.FixSelfInfoModule.class,
    AppComponent.ChooseGymModule.class,
    AppComponent.PopFromBottomModule.class, AppComponent.StatmentFilterModule.class,
    AppComponent.FixCheckinModule.class, AppComponent.ReportModule.class,
    AppComponent.FixNotifySettingModule.class, AppComponent.GymDetailModule.class,
    AppComponent.GymMoreModule.class, AppComponent.GymExpireModule.class,
    AppComponent.UpgrateGymModule.class, AppComponent.TrialProDialogModule.class,
    AppComponent.SaleGlanceModule.class, AppComponent.SaleDetailModule.class,
    AppComponent.CustomSaleModule.class, AppComponent.StatementGlanceModule.class,
    AppComponent.StatementDetailModule.class, AppComponent.CustomStatmentModule.class,
    AppComponent.SigninReportFormFragmentModule.class, AppComponent.EditTextModule.class,
    AppComponent.QuitGymFragmentModule.class, AppComponent.SaleTradeTypeFormFragmentModule.class,
    AppComponent.CourseCardFormFragmentModule.class, AppComponent.SaleCardTypeFragmentModule.class,
    AppComponent.SaleFilterModule.class, AppComponent.GuideChooseBrandModule.class,
    AppComponent.AddBrandModule.class,
    AppComponent.BottomBuyLimitFragmentModule.class, AppComponent.ChooseStaffFragmentModule.class,

    AppComponent.SigninGlanceModule.class, AppComponent.SignInDetailModule.class,
    AppComponent.SigninReportModule.class, AppComponent.CustomSigninModule.class,
    AppComponent.OutExcelModule.class, AppComponent.CoachChooseDialogModule.class,
    AppComponent.ChooseWardrobeModule.class, AppComponent.MutiChooseStudentModule.class,
    AppComponent.WardrobeModule.class, AppComponent.WardrobeAddModule.class,
    AppComponent.WardrobeLongHireModule.class, AppComponent.WardrobeEditModule.class,
    AppComponent.WardrobeContinueHireModule.class, AppComponent.WardrobeReturnDialogModule.class,
    AppComponent.WardrobeReturnModule.class, AppComponent.DistrictListModule.class,
    AppComponent.WardrobeShortHireModule.class, AppComponent.MutiChooseStudentFragModule.class,
    AppComponent.DistrictAddModule.class, AppComponent.ChooseRegionModule.class,
    AppComponent.WardrobeMainModule.class, AppComponent.WardrobePayBottomModule.class,
    AppComponent.SiteListModule.class, AppComponent.SiteDetailModule.class,
    AppComponent.ChooseSiteModule.class, AppComponent.MutiChooseSiteModule.class,
    AppComponent.ScheduleModule.class, AppComponent.ScheduleListModule.class,
    AppComponent.SignInHomeModule.class,
    AppComponent.SignInFragModule.class, AppComponent.SignOutModule.class,
    AppComponent.SignInConfigModule.class, AppComponent.SignInLogModule.class,
    AppComponent.SignInListModule.class, AppComponent.SignInStudentListModule.class,
    AppComponent.SignOutListModule.class, AppComponent.SinginConfigWardrobeModule.class,
    AppComponent.SigninConfigCardtypeListModule.class, AppComponent.SignInConfigScreenModule.class,
    AppComponent.SigninConfigListModule.class, AppComponent.SignInTypeModule.class,
    AppComponent.ScoreHomeModule.class, AppComponent.ScoreConfigModule.class,
    AppComponent.BaseConfigModule.class, AppComponent.ScoreRuleAddModule.class,
    AppComponent.ScoreAwardAddModule.class, AppComponent.DataStatementModule.class,
    AppComponent.SalerChooseDialogModule.class, AppComponent.StudentSearchModule.class,
    AppComponent.GymModule.class, AppComponent.MutiChooseSalersModule.class,
    AppComponent.NotificationModule.class, AppComponent.NotificationAModule.class,
    AppComponent.SimpleImgFragmentModule.class, AppComponent.ConversationFriendsModule.class,
    AppComponent.ChatFriendAllChooseModule.class, AppComponent.ChatChooseInGymModule.class,
    AppComponent.GymDetailShowGuideDialogModule.class, AppComponent.MutiChooseSiteModule.class,
    AppComponent.BatchPayOnlineModule.class, AppComponent.ChooseBrandModule.class,
    AppComponent.WebModule.class,
    AppComponent.SimpleChooseModule.class, AppComponent.WriteDescModule.class,
    AppComponent.CoachDetailFragmentModule.class, AppComponent.HomeUnLoginFragmentModule.class,
    AppComponent.WardrobeListFragmentModule.class, AppComponent.WardrobeDetailFragmentModule.class,
    AppComponent.SignInCloseFragmentModule.class, AppComponent.SignInManualFragmentModule.class,
    AppComponent.SignOutManualFragmentModule.class, AppComponent.ChooseMainFragmentModule.class,
    AppComponent.SearchResultFragmentModule.class, AppComponent.TopFilterSourceFragmentModule.class,
    AppComponent.ChooseReferrerModule.class, AppComponent.WebFragmentNoFreshFragmentModule.class,
    AppComponent.UpgradeInfoDialogFragmentModule.class,
    AppComponent.RenewalHistoryFragmentModule.class, AppComponent.GuideModule.class,
    AppComponent.MutiChooseGymFragmentModule.class, AppComponent.SetGymFragmentModule.class,
    AppComponent.CourseTypeFormFragmentModule.class, AppComponent.SignUpChooseModule.class,
    AppComponent.SignUpDetailModule.class, AppComponent.ChooseCoachFragmentModule.class,
    AppComponent.EditGymInfoFragmentModule.class,
    //文章评论
    AppComponent.ArticleCommentsListFragmentModule.class,
    AppComponent.ArticleReplyFragmentModule.class, AppComponent.ChooseOriginModule.class,

    AppComponent.GymInfoNoEditFragmentModule.class, AppComponent.GymInfoFragmentModule.class,
    AppComponent.ChooseAddressFragmentModule.class, AppComponent.SimpleCardListFragmentModule.class,
    //赛事训练营
    TrainMoudle.class, AppComponent.TrainChooseGymFragmentModule.class,
    AppComponent.SignUpPersonalDetailFragmentModule.class,
    AppComponent.SignUpGroupDetailFragmentModule.class,
    AppComponent.SignUpFormPersonalFragmentModule.class,
    AppComponent.SignUpFormHomeFragmentModule.class,
    AppComponent.SignUpFormGroupFragmentModule.class,
    AppComponent.SignUpChangeNameFragmentModule.class, AppComponent.RankCountFragmentModule.class,
    AppComponent.MemberOperationFragmentModule.class, AppComponent.DeleteMemberFragmentModule.class,
    AppComponent.CreateGroupFragmentModule.class,

    AppComponent.SignUpDetailModule.class,

    RealcardModule.class,
    /*
      带有学员信息的页面
     */

    AppComponent.StudentHomeModule.class, AppComponent.FollowRecordPageModule.class,
    AppComponent.StudentMoreInfoModule.class, AppComponent.StudentsCardsModule.class,
     AppComponent.StudentSignInImageFragmentModule.class,

    AppComponent.SignInManualModule.class, AppComponent.StudentsDetailModule.class,
    AppComponent.ScoreDetailAModule.class, AppComponent.ScoreDetailModule.class,
    AppComponent.ScoreModifyModule.class, AppComponent.AddNewSiteFragmentModule.class,

    //体测
    AppComponent.ModifyBodyTestFragmentModule.class, AppComponent.BodyTestListFragmentModule.class,
    AppComponent.BodyTestFragmentModule.class,
    /*
     * 以下为附加component
     */
    AppComponent.ChooseBrandActivityModule.class,

    AppComponent.WebFModule.class, CardTypeWrapperModule.class, RealcardModule.class,
    /**
     * 引导
     */
    AppComponent.AddBrandInMainFragmentModule.class, AppComponent.SetGymInMainFragmentModule.class,
    AppComponent.ChooseBrandInMainFragmentModule.class,
    AppComponent.WriteAddressFragmentModule.class,

    /**
     * 群发短信
     */
    //AppComponent.MsgSendFragmentModule.class, AppComponent.ShortMsgDetailModule.class,
    AppComponent.FlexableListModule.class,
    //AppComponent.SendMsgHomeModule.class,
    //AppComponent.SendMsgsModule.class,
    AppComponent.MultiChooseStudentWithFilterModule.class,
    AppComponent.StudentFilterWithBirthModule.class, AppComponent.ChooseStudentListModule.class,

    /**
     * 会员出勤
     */

    AppComponent.FilterCustomModule.class, AppComponent.AttendanceRankModule.class,
    AppComponent.AbsenceStuentListModule.class, AppComponent.AttendanceStaticModule.class,
    AppComponent.AttendanceFilterModule.class, AppComponent.AttendanceHomeModule.class,
    AppComponent.AttendanceModule.class,

    /**
     * 会员跟进
     */
    AppComponent.FollowUpDataTransfer0Module.class, AppComponent.FollowUpDataStatisticsModule.class,
    AppComponent.FollowUpDataTransferModule.class, AppComponent.FollowFUpModule.class,
    AppComponent.FollowUpModule.class, AppComponent.FollowUpDataStatisticsBaseModule.class,
    AppComponent.TopFilterSaleFragmentModule.class, AppComponent.FollowUpStatusFragmentModule.class,
    AppComponent.FollowUpDataStatistics0FragmentModule.class,
    AppComponent.FollowUpDataStatistics1FragmentModule.class,
    AppComponent.FollowUpDataStatistics2FragmentModule.class,
    AppComponent.TopFilterRegisterFragmentModule.class,
    AppComponent.TopFilterLatestFollowFragmentModule.class,
    AppComponent.LatestTimeFragmentModule.class,

    StaffWrapperMoudle.class,
    /**
     *
     * 销售分配
     */
    AppComponent.MultiModifyModule.class, AppComponent.StudentFilterModule.class,
    AppComponent.SalesListModule.class, AppComponent.AllotSaleDetailModule.class,
    AppComponent.AllotSalesModule.class,

    AppComponent.StudentListModule.class,

    AppComponent.StudentOperationModule.class, AppComponent.ReferrerModule.class,
    AppComponent.SourceModule.class, AppComponent.EditStudentInfoModule.class,

    //品牌管理
    AppComponent.BrandEditFragmentModule.class, AppComponent.BrandDetailFragmentModule.class,
    AppComponent.BrandManageFragmentModule.class, AppComponent.BrandManageModule.class,
    AppComponent.BrandCreatorEditFragmentModule.class,
    AppComponent.CourseReverseFragmentModule.class,

    //教练分配
    AppComponent.AddStudentFragmentModule.class, AppComponent.AllocateCoachModule.class,
    AppComponent.AllocateCoachListFragmentModule.class,
    AppComponent.CoachStudentDetailFragmentModule.class,
    AppComponent.OperationStudentFragmentModule.class, AppComponent.MutiChooseCoachModule.class,
    AppComponent.CardTypeChooseDialogFragmentModule.class, BindSaas.ChooseGymFragmentModule.class,

    /**
     * 聊天
     */
    AppComponent.JobSearchChatModule.class, AppComponent.RecruitMessageListFragmentModule.class,
    /**
     * 消息
     */
    AppComponent.SendChannelTabFragmentModule.class,
    AppComponent.NotiSettingWxTemplateFragmentModule.class,
    AppComponent.NotiSettingHomeFragmentModule.class,
    AppComponent.NotiSettingSendListDetailFragmentModule.class,
    AppComponent.NotiSettingMsgRuleFragmentModule.class,
    AppComponent.NotiSettingMsgDetailFragmentModule.class,
    AppComponent.NotiSettingMsgChargeFragmentModule.class,
    AppComponent.NotiSettingChargeResultFragmentModule.class,
    AppComponent.NotiSettingChargeHistoryFragmentModule.class,
    //导入导出
    AppComponent.ExportRecordFragmentModule.class, AppComponent.ExportSendEmailFragmentModule.class,

    //筛选，签课
    AppComponent.NotSignFilterFragmentModule.class,
    AppComponent.AttendanceNotSignFragmentModule.class,

    AppComponent.StaffDetailFragmentModule.class, AppComponent.CardProtocolModule.class,
    AppComponent.COurseChooseDialogFragmentModule.class,
    AppComponent.SignInCodeFragmentModule.class, AppComponent.QRModule.class,
    AppComponent.ZqAccessFragmentModule.class, AppComponent.EditZqFragmentModule.class,
    AppComponent.AddZqFragmentModule.class, AppComponent.UpgradeDoneFragmentModule.class,
})

public interface AppComponent {

  void inject(ChooseActivity activity);

  void inject(App activity);

  App app();

  @Subcomponent() public interface ChooseGymSubcomponent
      extends AndroidInjector<ChooseGymActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseGymActivity> {
    }
  }

  @Module public abstract class StaffStudentActivityModule {
    @Singleton @ContributesAndroidInjector()
    abstract StudentActivity contributeStudentActivityInjector();
  }

  @Subcomponent() public interface GuideSubcomponent extends AndroidInjector<GuideActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<GuideActivity> {
    }
  }

  @Subcomponent() public interface ChooseReferrerSubcomponent
      extends AndroidInjector<ChooseReferrerActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseReferrerActivity> {
    }
  }

  @Subcomponent() public interface SimpleImgFragmentSubcomponent
      extends AndroidInjector<SimpleImgDialog> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SimpleImgDialog> {
    }
  }

  @Subcomponent() public interface WebFragmentNoFreshFragmentSubcomponent
      extends AndroidInjector<WebFragmentNoFresh> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WebFragmentNoFresh> {
    }
  }

  @Subcomponent() public interface YourFragmentSubcomponent
      extends AndroidInjector<AddNewCoachFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AddNewCoachFragment> {
    }
  }

  @Subcomponent() public interface ContainterSubcomponent
      extends AndroidInjector<ContainerActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ContainerActivity> {
    }
  }

  @Subcomponent() public interface ChooseSubcomponent extends AndroidInjector<ChooseActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseActivity> {
    }
  }



  @Subcomponent() public interface PopFromBottomSubcomponent
      extends AndroidInjector<PopFromBottomActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<PopFromBottomActivity> {
    }
  }

  @Subcomponent() public interface StatmentFilterSubcomponent
      extends AndroidInjector<StatmentFilterActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StatmentFilterActivity> {
    }
  }

  @Subcomponent() public interface SplashSubcomponent extends AndroidInjector<SplashActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SplashActivity> {
    }
  }

  @Subcomponent() public interface WebSubcomponent extends AndroidInjector<WebActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WebActivity> {
    }
  }

  @Subcomponent() public interface GymSubcomponent extends AndroidInjector<GymActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<GymActivity> {
    }
  }

  @Subcomponent() public interface SimpleChooseSubcomponent
      extends AndroidInjector<SimpleChooseFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SimpleChooseFragment> {
    }
  }

  @Subcomponent() public interface WriteDescSubcomponent
      extends AndroidInjector<WriteDescFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WriteDescFragment> {
    }
  }

  @Subcomponent() public interface AddOffDaySubcomponent
      extends AndroidInjector<AddOffDayFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AddOffDayFragment> {
    }
  }

  @Subcomponent() public interface HomeUnLoginFragmentSubcomponent
      extends AndroidInjector<HomeUnLoginFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<HomeUnLoginFragment> {
    }
  }

  @Subcomponent() public interface MainSubcomponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MainActivity> {
    }
  }

  @Subcomponent() public interface SettingSubcomponent extends AndroidInjector<SettingFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SettingFragment> {
    }
  }

  @Subcomponent() public interface HomeSubcomponent extends AndroidInjector<HomeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<HomeFragment> {
    }
  }

  @Subcomponent() public interface MainFirstSubcomponent
      extends AndroidInjector<MainFirstFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MainFirstFragment> {
    }
  }

  @Subcomponent() public interface GymsSubcomponent extends AndroidInjector<GymsFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<GymsFragment> {
    }
  }

  @Subcomponent() public interface QcVipSubcomponent extends AndroidInjector<QcVipFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<QcVipFragment> {
    }
  }

  @Subcomponent() public interface MainMsgSubcomponent extends AndroidInjector<MainMsgFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MainMsgFragment> {
    }
  }

  @Subcomponent() public interface BaseStatementChartSubcomponent
      extends AndroidInjector<BaseStatementChartFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BaseStatementChartFragment> {
    }
  }

  @Subcomponent() public interface UnloginAdSubcomponent
      extends AndroidInjector<UnloginAdFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<UnloginAdFragment> {
    }
  }

  /**
   * 设置
   */

  @Subcomponent() public interface FixSelfInfoSubcomponent
      extends AndroidInjector<FixSelfInfoFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FixSelfInfoFragment> {
    }
  }

  @Subcomponent() public interface FixCheckinSubcomponent
      extends AndroidInjector<FixCheckinFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FixCheckinFragment> {
    }
  }

  @Subcomponent() public interface ReportSubcomponent extends AndroidInjector<ReportFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ReportFragment> {
    }
  }

  @Subcomponent() public interface FixNotifySettingSubcomponent
      extends AndroidInjector<FixNotifySettingFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FixNotifySettingFragment> {
    }
  }

  @Subcomponent() public interface ChooseBrandSubcomponent
      extends AndroidInjector<ChooseBrandFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseBrandFragment> {
    }
  }

  /**
   * 健身房详情
   */

  @Subcomponent() public interface GymDetailSubcomponent
      extends AndroidInjector<GymDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<GymDetailFragment> {
    }
  }

  @Subcomponent() public interface GymMoreSubcomponent extends AndroidInjector<GymMoreFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<GymMoreFragment> {
    }
  }

  @Subcomponent() public interface GymExpireSubcomponent
      extends AndroidInjector<GymExpireFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<GymExpireFragment> {
    }
  }

  @Subcomponent() public interface TrialProDialogSubcomponent
      extends AndroidInjector<TrialProDialogFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<TrialProDialogFragment> {
    }
  }

  @Subcomponent() public interface TopFilterSourceFragmentSubcomponent
      extends AndroidInjector<TopFilterSourceFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<TopFilterSourceFragment> {
    }
  }

  /**
   * 销售报表
   */

  @Subcomponent() public interface SaleGlanceSubcomponent
      extends AndroidInjector<SaleGlanceFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SaleGlanceFragment> {
    }
  }

  @Subcomponent() public interface SaleDetailSubcomponent
      extends AndroidInjector<SaleDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SaleDetailFragment> {
    }
  }

  @Subcomponent() public interface CustomSaleSubcomponent
      extends AndroidInjector<CustomSaleFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CustomSaleFragment> {
    }
  }

  /**
   * 主页
   */
  //void inject(MainActivity activity);
  //
  //void inject(SettingFragment activity);
  //
  //void inject(HomeFragment activity);
  //
  //void inject(MainFirstFragment activity);
  //
  //void inject(GymsFragment activity);

  @Subcomponent() public interface StatementGlanceSubcomponent
      extends AndroidInjector<StatementGlanceFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StatementGlanceFragment> {
    }
  }

  @Subcomponent() public interface StatementDetailSubcomponent
      extends AndroidInjector<StatementDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StatementDetailFragment> {
    }
  }

  @Subcomponent() public interface CustomStatmentSubcomponent
      extends AndroidInjector<CustomStatmentFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CustomStatmentFragment> {
    }
  }

  @Subcomponent() public interface SigninGlanceSubcomponent
      extends AndroidInjector<SigninGlanceFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SigninGlanceFragment> {
    }
  }

  @Subcomponent() public interface SignInDetailSubcomponent
      extends AndroidInjector<SignInDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInDetailFragment> {
    }
  }

  @Subcomponent() public interface SigninReportFormFragmentSubcomponent
      extends AndroidInjector<SigninReportFormFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SigninReportFormFragment> {
    }
  }

  @Subcomponent() public interface SigninReportSubcomponent
      extends AndroidInjector<SigninReportFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SigninReportFragment> {
    }
  }

  @Subcomponent() public interface CustomSigninSubcomponent
      extends AndroidInjector<CustomSigninFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CustomSigninFragment> {
    }
  }

  @Subcomponent() public interface OutExcelSubcomponent extends AndroidInjector<OutExcelFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<OutExcelFragment> {
    }
  }

  @Subcomponent() public interface CoachChooseDialogSubcomponent
      extends AndroidInjector<CoachChooseDialogFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CoachChooseDialogFragment> {
    }
  }

  /**
   * 更衣柜
   */
  @Subcomponent() public interface ChooseWardrobeSubcomponent
      extends AndroidInjector<ChooseWardrobeActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseWardrobeActivity> {
    }
  }

  @Subcomponent() public interface MutiChooseStudentSubcomponent
      extends AndroidInjector<MutiChooseStudentActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MutiChooseStudentActivity> {
    }
  }

  @Subcomponent() public interface WardrobeSubcomponent extends AndroidInjector<WardrobeActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeActivity> {
    }
  }

  @Subcomponent() public interface WardrobeAddSubcomponent
      extends AndroidInjector<WardrobeAddFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeAddFragment> {
    }
  }

  //void inject(FixSelfInfoFragment activity);
  //
  //void inject(FixPwFragment activity);
  //
  //void inject(FixPhoneFragment activity);
  //
  //void inject(FixCheckinFragment activity);
  //
  //void inject(ReportFragment activity);
  //
  //void inject(FixNotifySettingFragment i);

  @Subcomponent() public interface WardrobeLongHireSubcomponent
      extends AndroidInjector<WardrobeLongHireFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeLongHireFragment> {
    }
  }

  @Subcomponent() public interface WardrobeEditSubcomponent
      extends AndroidInjector<WardrobeEditFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeEditFragment> {
    }
  }

  @Subcomponent() public interface WardrobeContinueHireSubcomponent
      extends AndroidInjector<WardrobeContinueHireFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeContinueHireFragment> {
    }
  }

  @Subcomponent() public interface WardrobeReturnDialogSubcomponent
      extends AndroidInjector<WardrobeReturnDialog> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeReturnDialog> {
    }
  }

  @Subcomponent() public interface WardrobeReturnSubcomponent
      extends AndroidInjector<WardrobeReturnFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeReturnFragment> {
    }
  }

  @Subcomponent() public interface DistrictListSubcomponent
      extends AndroidInjector<DistrictListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<DistrictListFragment> {
    }
  }

  @Subcomponent() public interface WardrobeShortHireSubcomponent
      extends AndroidInjector<WardrobeShortHireFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeShortHireFragment> {
    }
  }

  @Subcomponent() public interface MutiChooseStudentFragSubcomponent
      extends AndroidInjector<MutiChooseStudentFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MutiChooseStudentFragment> {
    }
  }

  @Subcomponent() public interface ChooseMainFragmentSubcomponent
      extends AndroidInjector<ChooseMainFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseMainFragment> {
    }
  }

  @Subcomponent() public interface SearchResultFragmentSubcomponent
      extends AndroidInjector<SearchResultFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SearchResultFragment> {
    }
  }

  @Subcomponent() public interface DistrictAddSubcomponent
      extends AndroidInjector<DistrictAddFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<DistrictAddFragment> {
    }
  }

  @Subcomponent() public interface ChooseRegionSubcomponent
      extends AndroidInjector<ChooseRegionFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseRegionFragment> {
    }
  }

  @Subcomponent() public interface WardrobeMainSubcomponent
      extends AndroidInjector<WardrobeMainFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeMainFragment> {
    }
  }

  @Subcomponent() public interface WardrobePayBottomSubcomponent
      extends AndroidInjector<WardrobePayBottomFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobePayBottomFragment> {
    }
  }

  @Subcomponent() public interface WardrobeDetailFragmentSubcomponent
      extends AndroidInjector<WardrobeDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeDetailFragment> {
    }
  }

  /**
   * 内部管理
   */

  @Subcomponent() public interface SiteListSubcomponent extends AndroidInjector<SiteListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SiteListFragment> {
    }
  }

  @Subcomponent() public interface SiteDetailSubcomponent
      extends AndroidInjector<SiteDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SiteDetailFragment> {
    }
  }

  @Subcomponent() public interface ChooseSiteSubcomponent
      extends AndroidInjector<ChooseSiteFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseSiteFragment> {
    }
  }

  @Subcomponent() public interface MutiChooseSiteSubcomponent
      extends AndroidInjector<MutiChooseSiteFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MutiChooseSiteFragment> {
    }
  }

  @Subcomponent() public interface StaffDetailFragmentSubcomponent
      extends AndroidInjector<StaffDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StaffDetailFragment> {
    }
  }

  /**
   * 课程预约
   */
  @Subcomponent() public interface ScheduleSubcomponent extends AndroidInjector<ScheduleActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ScheduleActivity> {
    }
  }

  @Subcomponent() public interface ScheduleListSubcomponent
      extends AndroidInjector<ScheduleListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ScheduleListFragment> {
    }
  }

  /**
   * 签到
   */


  @Subcomponent() public interface SignInHomeSubcomponent
      extends AndroidInjector<SignInHomeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInHomeFragment> {
    }
  }

  @Subcomponent() public interface SignInSubFragcomponent extends AndroidInjector<SignInFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInFragment> {
    }
  }

  @Subcomponent() public interface SignOutSubcomponent extends AndroidInjector<SignOutFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignOutFragment> {
    }
  }

  @Subcomponent() public interface SignInConfigSubcomponent
      extends AndroidInjector<SignInConfigFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInConfigFragment> {
    }
  }

  @Subcomponent() public interface SignInLogSubcomponent
      extends AndroidInjector<SignInLogFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInLogFragment> {
    }
  }

  @Subcomponent() public interface SignInListSubcomponent
      extends AndroidInjector<SignInListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInListFragment> {
    }
  }

  @Subcomponent() public interface SignInStudentListSubcomponent
      extends AndroidInjector<SignInStudentListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInStudentListFragment> {
    }
  }

  @Subcomponent() public interface SignOutListSubcomponent
      extends AndroidInjector<SignOutListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignOutListFragment> {
    }
  }

  @Subcomponent() public interface SinginConfigWardrobeSubcomponent
      extends AndroidInjector<SinginConfigWardrobeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SinginConfigWardrobeFragment> {
    }
  }

  @Subcomponent() public interface SigninConfigCardtypeListSubcomponent
      extends AndroidInjector<SigninConfigCardtypeListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SigninConfigCardtypeListFragment> {
    }
  }

  //void inject(CourseTypeBatchFragment gymDetailFragment);
  //
  //void inject(CourseDetailFragment gymDetailFragment);
  //
  //
  //void inject(CourseListFragment gymDetailFragment);
  //
  //
  //void inject(CourseChooseDialogFragment gymDetailFragment);
  //
  //void inject(JacketManagerFragment gymDetailFragment);
  //
  //void inject(EditCourseFragment gymDetailFragment);
  //
  //void inject(ShopCommentsFragment gymDetailFragment);
  //
  //void inject(CourseImagesFragment gymDetailFragment);
  //
  //void inject(cn.qingchengfit.staffkit.views.batch.CourseListFragment gymDetailFragment);
  //
  //void inject(CourseBatchDetailFragment gymDetailFragment);
  //
  //void inject(AddBatchFragment gymDetailFragment);

  @Subcomponent() public interface SignInConfigScreenSubcomponent
      extends AndroidInjector<SignInConfigScreenFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInConfigScreenFragment> {
    }
  }

  @Subcomponent() public interface SigninConfigListSubcomponent
      extends AndroidInjector<SigninConfigListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SigninConfigListFragment> {
    }
  }

  @Subcomponent() public interface SignInTypeSubcomponent
      extends AndroidInjector<SignInTypeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInTypeFragment> {
    }
  }

  /**
   * 积分
   */
  @Subcomponent() public interface ScoreHomeSubcomponent
      extends AndroidInjector<ScoreHomeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ScoreHomeFragment> {
    }
  }

  @Subcomponent() public interface ScoreConfigSubcomponent extends AndroidInjector<ConfigFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ConfigFragment> {
    }
  }

  @Subcomponent() public interface BaseConfigSubcomponent
      extends AndroidInjector<BaseConfigFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BaseConfigFragment> {
    }
  }

  @Subcomponent() public interface ScoreRuleAddSubcomponent
      extends AndroidInjector<ScoreRuleAddFragemnt> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ScoreRuleAddFragemnt> {
    }
  }

  @Subcomponent() public interface ScoreAwardAddSubcomponent
      extends AndroidInjector<ScoreAwardAddFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ScoreAwardAddFragment> {
    }
  }

  @Subcomponent() public interface DataStatementSubcomponent
      extends AndroidInjector<DataStatementFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<DataStatementFragment> {
    }
  }

  @Subcomponent() public interface SalerChooseDialogSubcomponent
      extends AndroidInjector<SalerChooseDialogFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SalerChooseDialogFragment> {
    }
  }

  @Subcomponent() public interface StudentSearchSubcomponent
      extends AndroidInjector<StudentSearchFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StudentSearchFragment> {
    }
  }

  /**
   * 选择销售
   */

  @Subcomponent() public interface MutiChooseSalersSubcomponent
      extends AndroidInjector<MutiChooseSalersActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MutiChooseSalersActivity> {
    }
  }

  /**
   * 通知
   */
  @Subcomponent() public interface NotificationSubcomponent
      extends AndroidInjector<NotificationFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotificationFragment> {
    }
  }

  @Subcomponent() public interface NotificationASubcomponent
      extends AndroidInjector<NotificationActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotificationActivity> {
    }
  }

  @Subcomponent() public interface WebFSubcomponent extends AndroidInjector<WebFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WebFragment> {
    }
  }

  @Subcomponent() public interface ConversationFriendsSubcomponent
      extends AndroidInjector<ConversationFriendsFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ConversationFriendsFragment> {
    }
  }

  @Subcomponent() public interface ChatFriendAllChooseSubcomponent
      extends AndroidInjector<ChatFriendAllChooseFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChatFriendAllChooseFragment> {
    }
  }

  @Subcomponent() public interface ChatChooseInGymSubcomponent
      extends AndroidInjector<ChatChooseInGymFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChatChooseInGymFragment> {
    }
  }

  @Subcomponent() public interface GymDetailShowGuideDialogSubcomponent
      extends AndroidInjector<GymDetailShowGuideDialogFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<GymDetailShowGuideDialogFragment> {
    }
  }

  //
  //void inject(SaleGlanceFragment f);
  //
  //void inject(SaleDetailFragment f);
  //
  //void inject(CustomSaleFragment f);
  //
  //void inject(StatementGlanceFragment f);
  //
  //void inject(StatementDetailFragment f);
  //
  //void inject(CustomStatmentFragment f);
  //
  //void inject(SigninGlanceFragment f);
  //
  //void inject(SignInDetailFragment f);
  //
  //void inject(SigninReportFragment f);
  //
  //void inject(CustomSigninFragment f);
  //
  //void inject(OutExcelFragment f);
  //
  //void inject(CoachChooseDialogFragment f);

  @Subcomponent() public interface BatchPayOnlineSubcomponent
      extends AndroidInjector<BatchPayOnlineFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BatchPayOnlineFragment> {
    }
  }

  /**
   * 会员
   */
  @Subcomponent() public interface StudentSignInImageFragmentSubcomponent
      extends AndroidInjector<StudentSignInImageFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StudentSignInImageFragment> {
    }
  }

  ///**
  // * 群发短信
  // */
  //@Subcomponent() public interface SendMsgsSubcomponent extends AndroidInjector<SendMsgsActivity> {
  //  @Subcomponent.Builder public abstract class Builder
  //      extends AndroidInjector.Builder<SendMsgsActivity> {
  //  }
  //}
  //
  //@Subcomponent() public interface SendMsgHomeSubcomponent
  //    extends AndroidInjector<SendMsgHomeFragment> {
  //  @Subcomponent.Builder public abstract class Builder
  //      extends AndroidInjector.Builder<SendMsgHomeFragment> {
  //  }
  //}

  @Subcomponent() public interface FlexableListSubcomponent
      extends AndroidInjector<FlexableListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FlexableListFragment> {
    }
  }
  //
  //@Subcomponent() public interface ShortMsgDetailSubcomponent
  //    extends AndroidInjector<ShortMsgDetailFragment> {
  //  @Subcomponent.Builder public abstract class Builder
  //      extends AndroidInjector.Builder<ShortMsgDetailFragment> {
  //  }
  //}
  //
  //@Subcomponent() public interface MsgSendFragmentSubcomponent
  //    extends AndroidInjector<MsgSendFragmentFragment> {
  //  @Subcomponent.Builder public abstract class Builder
  //      extends AndroidInjector.Builder<MsgSendFragmentFragment> {
  //  }
  //}

  @Subcomponent() public interface MultiChooseStudentWithFilterSubcomponent
      extends AndroidInjector<MultiChooseStudentWithFilterFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MultiChooseStudentWithFilterFragment> {
    }
  }

  @Subcomponent() public interface ChooseStudentListSubcomponent
      extends AndroidInjector<ChooseStudentListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseStudentListFragment> {
    }
  }

  @Subcomponent() public interface StudentFilterWithBirthSubcomponent
      extends AndroidInjector<StudentFilterWithBirthFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StudentFilterWithBirthFragment> {
    }
  }

  /**
   * 会员出勤
   */
  @Subcomponent() public interface AttendanceSubcomponent
      extends AndroidInjector<AttendanceActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AttendanceActivity> {
    }
  }

  @Subcomponent() public interface AttendanceHomeSubcomponent
      extends AndroidInjector<AttendanceHomeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AttendanceHomeFragment> {
    }
  }

  @Subcomponent() public interface AttendanceFilterSubcomponent
      extends AndroidInjector<FilterFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FilterFragment> {
    }
  }

  @Subcomponent() public interface AttendanceStaticSubcomponent
      extends AndroidInjector<AttendanceStaticFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AttendanceStaticFragment> {
    }
  }

  @Subcomponent() public interface AbsenceStuentListSubcomponent
      extends AndroidInjector<AbsenceStuentListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AbsenceStuentListFragment> {
    }
  }

  @Subcomponent() public interface AttendanceRankSubcomponent
      extends AndroidInjector<AttendanceRankFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AttendanceRankFragment> {
    }
  }

  @Subcomponent() public interface AttendanceFilterCustomSubcomponent
      extends AndroidInjector<FilterCustomFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FilterCustomFragment> {
    }
  }

  /**
   * 会员跟进
   */
  @Subcomponent() public interface FollowUpSubcomponent extends AndroidInjector<FollowUpActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowUpActivity> {
    }
  }

  @Subcomponent() public interface FollowUpFSubcomponent extends AndroidInjector<FollowUpFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowUpFragment> {
    }
  }

  @Subcomponent() public interface FollowUpDataTransferSubcomponent
      extends AndroidInjector<FollowUpDataTransferFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowUpDataTransferFragment> {
    }
  }

  @Subcomponent() public interface FollowUpDataStatisticsSubcomponent
      extends AndroidInjector<FollowUpDataStatisticsFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowUpDataStatisticsFragment> {
    }
  }

  @Subcomponent() public interface FollowUpDataTransfer0Subcomponent
      extends AndroidInjector<FollowUpDataTransfer0Fragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowUpDataTransfer0Fragment> {
    }
  }

  @Subcomponent() public interface FollowUpDataStatisticsBaseSubcomponent
      extends AndroidInjector<FollowUpDataStatisticsBaseFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowUpDataStatisticsBaseFragment> {
    }
  }

  @Subcomponent() public interface FollowUpDataStatistics0FragmentSubcomponent
      extends AndroidInjector<FollowUpDataStatistics0Fragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowUpDataStatistics0Fragment> {
    }
  }

  @Subcomponent() public interface FollowUpDataStatistics1FragmentSubcomponent
      extends AndroidInjector<FollowUpDataStatistics1Fragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowUpDataStatistics1Fragment> {
    }
  }

  @Subcomponent() public interface FollowUpDataStatistics2FragmentSubcomponent
      extends AndroidInjector<FollowUpDataStatistics2Fragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowUpDataStatistics2Fragment> {
    }
  }

  @Subcomponent() public interface FollowUpStatusFragmentSubcomponent
      extends AndroidInjector<FollowUpStatusFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowUpStatusFragment> {
    }
  }

  @Subcomponent() public interface TopFilterSaleFragmentSubcomponent
      extends AndroidInjector<TopFilterSaleFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<TopFilterSaleFragment> {
    }
  }

  @Subcomponent() public interface TopFilterRegisterFragmentSubcomponent
      extends AndroidInjector<TopFilterRegisterFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<TopFilterRegisterFragment> {
    }
  }

  @Subcomponent() public interface TopFilterLatestFollowFragmentSubcomponent
      extends AndroidInjector<TopFilterLatestFollowFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<TopFilterLatestFollowFragment> {
    }
  }

  @Subcomponent() public interface LatestTimeFragmentSubcomponent
      extends AndroidInjector<LatestTimeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<LatestTimeFragment> {
    }
  }

  /**
   * 体测
   */
  @Subcomponent() public interface BodyTestListFragmentSubcomponent
      extends AndroidInjector<BodyTestListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BodyTestListFragment> {
    }
  }

  @Subcomponent() public interface ModifyBodyTestFragmentSubcomponent
      extends AndroidInjector<ModifyBodyTestFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ModifyBodyTestFragment> {
    }
  }

  @Subcomponent() public interface BodyTestFragmentSubcomponent
      extends AndroidInjector<BodyTestFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BodyTestFragment> {
    }
  }

  /**
   * 分配销售
   */
  @Subcomponent() public interface AllotSalesSubcomponent
      extends AndroidInjector<AllotSalesActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AllotSalesActivity> {
    }
  }

  @Subcomponent() public interface AllotSaleDetailSubcomponent
      extends AndroidInjector<cn.qingchengfit.staffkit.views.allotsales.SaleDetailFragment> {
    @Subcomponent.Builder public abstract class Builder extends
        AndroidInjector.Builder<cn.qingchengfit.staffkit.views.allotsales.SaleDetailFragment> {
    }
  }

  @Subcomponent() public interface SalesListSubcomponent
      extends AndroidInjector<SalesListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SalesListFragment> {
    }
  }

  @Subcomponent() public interface StudentFilterSubcomponent
      extends AndroidInjector<StudentFilterFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StudentFilterFragment> {
    }
  }
  //
  //void inject(ChooseWardrobeActivity i);
  //
  //void inject(MutiChooseStudentActivity i);
  //
  //void inject(WardrobeActivity i);
  //
  //void inject(WardrobeAddFragment i);
  //
  //void inject(WardrobeLongHireFragment i);
  //
  //void inject(WardrobeEditFragment i);
  //
  //void inject(WardrobeContinueHireFragment i);
  //
  //void inject(WardrobeReturnDialog i);
  //
  //void inject(WardrobeReturnFragment i);
  //
  //void inject(DistrictListFragment i);
  //
  //void inject(WardrobeShortHireFragment i);
  //
  //void inject(MutiChooseStudentFragment i);
  //
  //void inject(DistrictAddFragment i);
  //
  //void inject(ChooseRegionFragment i);
  //
  //void inject(WardrobeMainFragment i);
  //
  //void inject(WardrobePayBottomFragment i);

  @Subcomponent() public interface MultiModifySubcomponent
      extends AndroidInjector<MultiModifyFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MultiModifyFragment> {
    }
  }

  @Subcomponent() public interface StudentsDetailSubcomponent
      extends AndroidInjector<StudentsDetailActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StudentsDetailActivity> {
    }
  }

  @Subcomponent() public interface ScoreDetailASubcomponent
      extends AndroidInjector<ScoreDetailActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ScoreDetailActivity> {
    }
  }

  @Subcomponent() public interface ScoreDetailSubcomponent
      extends AndroidInjector<ScoreDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ScoreDetailFragment> {
    }
  }

  @Subcomponent() public interface ScoreModifySubcomponent
      extends AndroidInjector<ScoreModifyFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ScoreModifyFragment> {
    }
  }

  @Subcomponent() public interface StudentListSubcomponent
      extends AndroidInjector<StudentListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StudentListFragment> {
    }
  }

  /**
   * {@link StudentOperationFragment}
   */
  @Subcomponent() public interface StudentOperationSubcomponent
      extends AndroidInjector<StudentOperationFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StudentOperationFragment> {
    }
  }

  @Subcomponent() public interface EditStudentInfoSubcomponent
      extends AndroidInjector<EditStudentInfoFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<EditStudentInfoFragment> {
    }
  }

  @Subcomponent() public interface ReferrerSubcomponent extends AndroidInjector<ReferrerFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ReferrerFragment> {
    }
  }

  @Subcomponent() public interface SourceSubcomponent extends AndroidInjector<SourceFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SourceFragment> {
    }
  }

  @Subcomponent() public interface StudentHomeSubcomponent
      extends AndroidInjector<StudentHomeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StudentHomeFragment> {
    }
  }

  @Subcomponent() public interface FollowRecordPageSubcomponent
      extends AndroidInjector<FollowRecordPage> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<FollowRecordPage> {
    }
  }

  @Subcomponent() public interface SimpleCardListFragmentSubcomponent
      extends AndroidInjector<SimpleCardListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SimpleCardListFragment> {
    }
  }


  @Subcomponent() public interface StudentsCardsSubcomponent
      extends AndroidInjector<StudentsCardsFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StudentsCardsFragment> {
    }
  }

  @Subcomponent() public interface StudentMoreInfoSubcomponent
      extends AndroidInjector<StudentMoreInfoFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<StudentMoreInfoFragment> {
    }
  }

  @Subcomponent() public interface CoachDetailFragmentSubcomponent
      extends AndroidInjector<CoachDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CoachDetailFragment> {
    }
  }

  /*
   * 更衣柜
   */
  @Subcomponent() public interface WardrobeListFragmentSubcomponent
      extends AndroidInjector<WardrobeListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WardrobeListFragment> {
    }
  }

  /**
   * 签到
   */
  @Subcomponent() public interface SignInCloseFragmentSubcomponent
      extends AndroidInjector<SignInCloseFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInCloseFragment> {
    }
  }

  @Subcomponent() public interface SignInManualSubcomponent
      extends AndroidInjector<SignInManualActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInManualActivity> {
    }
  }

  @Subcomponent() public interface SignInManualFragmentSubcomponent
      extends AndroidInjector<SignInManualFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInManualFragment> {
    }
  }
  //
  //void inject(AllocateCoachListFragment i);
  //
  //void inject(CoachDetailFragment i);
  //
  //void inject(StaffListFragment i);
  //
  //void inject(StaffDetailFragment i);
  //
  //void inject(SuFragment i);
  //
  //void inject(SuIdendifyFragment i);
  //
  //void inject(SuNewFragment i);
  //
  //void inject(SiteListFragment i);
  //
  //void inject(SiteDetailFragment i);
  //
  //void inject(ChooseSiteFragment i);
  //
  //void inject(MutiChooseSiteFragment i);

  @Subcomponent() public interface SignOutManualFragmentSubcomponent
      extends AndroidInjector<SignOutManualFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignOutManualFragment> {
    }
  }

  @Subcomponent() public interface AddNewSiteFragmentSubcomponent
      extends AndroidInjector<AddNewSiteFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AddNewSiteFragment> {
    }
  }

  //品牌管理
  @Subcomponent() public interface BrandManageSubcomponent
      extends AndroidInjector<BrandManageActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BrandManageActivity> {
    }
  }

  @Subcomponent() public interface BrandManageFragmentSubcomponent
      extends AndroidInjector<BrandManageFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BrandManageFragment> {
    }
  }

  @Subcomponent() public interface BrandDetailFragmentSubcomponent
      extends AndroidInjector<BrandDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BrandDetailFragment> {
    }
  }

  @Subcomponent() public interface BrandEditFragmentSubcomponent
      extends AndroidInjector<BrandEditFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BrandEditFragment> {
    }
  }

  @Subcomponent() public interface BrandCreatorEditFragmentSubcomponent
      extends AndroidInjector<BrandCreatorEditFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BrandCreatorEditFragment> {
    }
  }

  //充值
  @Subcomponent() public interface UpgradeInfoDialogFragmentSubcomponent
      extends AndroidInjector<UpgradeInfoDialogFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<UpgradeInfoDialogFragment> {
    }
  }

  @Subcomponent() public interface TrialProDialogFragmentSubcomponent
      extends AndroidInjector<TrialProDialogFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<TrialProDialogFragment> {
    }
  }

  @Subcomponent() public interface UpgrateGymSubcomponent
      extends AndroidInjector<UpgrateGymFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<UpgrateGymFragment> {
    }
  }

  @Subcomponent() public interface RenewalHistoryFragmentSubcomponent
      extends AndroidInjector<RenewalHistoryFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RenewalHistoryFragment> {
    }
  }

  @Subcomponent() public interface EditTextSubcomponent extends AndroidInjector<EditTextActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<EditTextActivity> {
    }
  }

  //初始化流程
  @Subcomponent() public interface ChooseBrandInMainFragmentSubcomponent
      extends AndroidInjector<ChooseBrandInMainFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseBrandInMainFragment> {
    }
  }

  @Subcomponent() public interface SetGymInMainFragmentSubcomponent
      extends AndroidInjector<SetGymInMainFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SetGymInMainFragment> {
    }
  }

  @Subcomponent() public interface AddBrandInMainFragmentSubcomponent
      extends AndroidInjector<AddBrandInMainFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AddBrandInMainFragment> {
    }
  }

  @Subcomponent() public interface WriteAddressFragmentSubcomponent
      extends AndroidInjector<WriteAddressFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<WriteAddressFragment> {
    }
  }

  @Subcomponent() public interface MutiChooseGymFragmentSubcomponent
      extends AndroidInjector<MutiChooseGymFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MutiChooseGymFragment> {
    }
  }

  @Subcomponent() public interface SetGymFragmentSubcomponent
      extends AndroidInjector<SetGymFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SetGymFragment> {
    }
  }

  /**
   * 文章评论
   */
  @Subcomponent() public interface ArticleReplyFragmentSubcomponent
      extends AndroidInjector<ArticleReplyFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ArticleReplyFragment> {
    }
  }

  @Subcomponent() public interface ArticleCommentsListFragmentSubcomponent
      extends AndroidInjector<ArticleCommentsListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ArticleCommentsListFragment> {
    }
  }

  @Subcomponent() public interface QuitGymFragmentSubcomponent
      extends AndroidInjector<QuitGymFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<QuitGymFragment> {
    }
  }

  @Subcomponent() public interface SaleTradeTypeFormFragmentSubcomponent
      extends AndroidInjector<SaleTradeTypeFormFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SaleTradeTypeFormFragment> {
    }
  }

  @Subcomponent() public interface CourseCardFormFragmentSubcomponent
      extends AndroidInjector<CourseCardFormFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CourseCardFormFragment> {
    }
  }

  @Subcomponent() public interface SaleCardTypeFragmentSubcomponent
      extends AndroidInjector<SaleCardTypeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SaleCardTypeFragment> {
    }
  }

  @Subcomponent() public interface CourseTypeFormFragmentSubcomponent
      extends AndroidInjector<CourseTypeFormFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CourseTypeFormFragment> {
    }
  }

  @Subcomponent() public interface SaleFilterSubcomponent
      extends AndroidInjector<SaleFilterActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SaleFilterActivity> {
    }
  }

  @Subcomponent() public interface GuideChooseBrandSubcomponent
      extends AndroidInjector<GuideChooseBrandAcitivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<GuideChooseBrandAcitivity> {
    }
  }

  @Subcomponent() public interface ChooseBrandAcitiviySubcomponent
      extends AndroidInjector<ChooseBrandActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseBrandActivity> {
    }
  }

  @Subcomponent() public interface AddBrandSubcomponent extends AndroidInjector<AddBrandActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AddBrandActivity> {
    }
  }

  @Subcomponent() public interface ChooseOriginSubcomponent
      extends AndroidInjector<ChooseOriginActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseOriginActivity> {
    }
  }

  @Subcomponent() public interface CreateGroupFragmentSubcomponent
      extends AndroidInjector<CreateGroupFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CreateGroupFragment> {
    }
  }

  @Subcomponent() public interface DeleteMemberFragmentSubcomponent
      extends AndroidInjector<DeleteMemberFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<DeleteMemberFragment> {
    }
  }

  @Subcomponent() public interface MemberOperationFragmentSubcomponent
      extends AndroidInjector<MemberOperationFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MemberOperationFragment> {
    }
  }

  @Subcomponent() public interface RankCountFragmentSubcomponent
      extends AndroidInjector<RankCountFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RankCountFragment> {
    }
  }


  @Subcomponent() public interface SignUpChangeNameFragmentSubcomponent
      extends AndroidInjector<SignUpChangeNameFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignUpChangeNameFragment> {
    }
  }

  @Subcomponent() public interface SignUpFormGroupFragmentSubcomponent
      extends AndroidInjector<SignUpFormGroupFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignUpFormGroupFragment> {
    }
  }

  @Subcomponent() public interface SignUpFormHomeFragmentSubcomponent
      extends AndroidInjector<SignUpFormHomeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignUpFormHomeFragment> {
    }
  }

  @Subcomponent() public interface SignUpFormPersonalFragmentSubcomponent
      extends AndroidInjector<SignUpFormPersonalFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignUpFormPersonalFragment> {
    }
  }

  @Subcomponent() public interface SignUpGroupDetailFragmentSubcomponent
      extends AndroidInjector<SignUpGroupDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignUpGroupDetailFragment> {
    }
  }

  @Subcomponent() public interface SignUpPersonalDetailFragmentSubcomponent
      extends AndroidInjector<SignUpPersonalDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignUpPersonalDetailFragment> {
    }
  }

  @Subcomponent() public interface TrainChooseGymFragmentSubcomponent
      extends AndroidInjector<TrainChooseGymFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<TrainChooseGymFragment> {
    }
  }

  @Subcomponent() public interface SignUpDetailSubcomponent
      extends AndroidInjector<SignUpDetailActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignUpDetailActivity> {
    }
  }

  @Subcomponent() public interface SignUpChooseSubcomponent
      extends AndroidInjector<SignUpChooseActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignUpChooseActivity> {
    }
  }

  @Subcomponent() public interface ChooseAddressFragmentSubcomponent
      extends AndroidInjector<ChooseAddressFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseAddressFragment> {
    }
  }

  @Subcomponent() public interface GymInfoNoEditFragmentSubcomponent
      extends AndroidInjector<GymInfoNoEditFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<GymInfoNoEditFragment> {
    }
  }

  @Subcomponent() public interface GymInfoFragmentSubcomponent
      extends AndroidInjector<GymInfoFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<GymInfoFragment> {
    }
  }

  @Subcomponent() public interface CourseReverseFragmentSubcomponent
      extends AndroidInjector<CourseReverseFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CourseReverseFragment> {
    }
  }

  /**
   * {@link }
   * 教练分配
   */
  @Subcomponent() public interface AllocateCoachSubcomponent
      extends AndroidInjector<AllocateCoachActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AllocateCoachActivity> {
    }
  }

  @Subcomponent() public interface AddStudentFragmentSubcomponent
      extends AndroidInjector<AddStudentFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AddStudentFragment> {
    }
  }

  @Subcomponent() public interface AllocateCoachListFragmentSubcomponent
      extends AndroidInjector<AllocateCoachListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AllocateCoachListFragment> {
    }
  }

  @Subcomponent() public interface CoachStudentDetailFragmentSubcomponent
      extends AndroidInjector<CoachStudentDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CoachStudentDetailFragment> {
    }
  }

  @Subcomponent() public interface OperationStudentFragmentSubcomponent
      extends AndroidInjector<OperationStudentFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<OperationStudentFragment> {
    }
  }

  @Subcomponent() public interface MutiChooseCoachSubcomponent
      extends AndroidInjector<MutiChooseCoachActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<MutiChooseCoachActivity> {
    }
  }

  @Subcomponent() public interface CardTypeChooseDialogFragmentSubcomponent
      extends AndroidInjector<CardTypeChooseDialogFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CardTypeChooseDialogFragment> {
    }
  }

  @Subcomponent() public interface BottomBuyLimitFragmentSubcomponent
      extends AndroidInjector<BottomBuyLimitFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<BottomBuyLimitFragment> {
    }
  }

  @Subcomponent() public interface JobSearchChatSubcomponent
      extends AndroidInjector<JobSearchChatActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<JobSearchChatActivity> {
    }
  }

  @Subcomponent() public interface RecruitMessageListFragmentSubcomponent
      extends AndroidInjector<RecruitMessageListFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<RecruitMessageListFragment> {
    }
  }
  //
  //void inject(CardTypeActivity i);
  //
  //void inject(BrandCardListFragment i);
  //
  //void inject(EditCardTypeFragment i);
  //
  //void inject(CardtypeDetailFragment i);
  //
  //void inject(ChooseCardTypeActivity activity);
  //
  //void inject(AddCardStandardFragment activity);
  //
  //void inject(EditCardStandardFragment activity);
  //
  //void inject(CardTypeListFragment activity);

  /**
   * {@link GymInfoNoEditFragment}
   */

  @Subcomponent() public interface ChooseGymFragmentSubcomponent
      extends AndroidInjector<ChooseGymFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseGymFragment> {
    }
  }

  @Subcomponent() public interface ChooseCoachFragmentSubcomponent
      extends AndroidInjector<ChooseCoachFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseCoachFragment> {
    }
  }

  @Subcomponent() public interface ChooseStaffFragmentSubcomponent
      extends AndroidInjector<ChooseStaffFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseStaffFragment> {
    }
  }

  /**
   * 通知模块相关
   */
  @Subcomponent() public interface NotiSettingChargeHistoryFragmentSubcomponent
      extends AndroidInjector<NotiSettingChargeHistoryFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotiSettingChargeHistoryFragment> {
    }
  }

  @Subcomponent() public interface NotiSettingChargeResultFragmentSubcomponent
      extends AndroidInjector<NotiSettingChargeResultFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotiSettingChargeResultFragment> {
    }
  }

  @Subcomponent() public interface NotiSettingMsgChargeFragmentSubcomponent
      extends AndroidInjector<NotiSettingMsgChargeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotiSettingMsgChargeFragment> {
    }
  }

  @Subcomponent() public interface NotiSettingMsgDetailFragmentSubcomponent
      extends AndroidInjector<NotiSettingMsgDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotiSettingMsgDetailFragment> {
    }
  }

  @Subcomponent() public interface NotiSettingMsgRuleFragmentSubcomponent
      extends AndroidInjector<NotiSettingMsgRuleFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotiSettingMsgRuleFragment> {
    }
  }

  @Subcomponent() public interface NotiSettingSendListDetailFragmentSubcomponent
      extends AndroidInjector<NotiSettingSendListDetailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotiSettingSendListDetailFragment> {
    }
  }

  @Subcomponent() public interface NotiSettingWxTemplateFragmentSubcomponent
      extends AndroidInjector<NotiSettingWxTemplateFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotiSettingWxTemplateFragment> {
    }
  }

  @Subcomponent() public interface SendChannelTabFragmentSubcomponent
      extends AndroidInjector<SendChannelTabFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SendChannelTabFragment> {
    }
  }

  @Subcomponent() public interface NotiSettingHomeFragmentSubcomponent
      extends AndroidInjector<NotiSettingHomeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotiSettingHomeFragment> {
    }
  }

  /**
   * {@link GymInfoNoEditFragment}
   */

  @Module(subcomponents = ChooseGymSubcomponent.class) abstract class ChooseGymModule {
    @Binds @IntoMap @ActivityKey(ChooseGymActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        ChooseGymSubcomponent.Builder builder);
  }

  @Module(subcomponents = GuideSubcomponent.class) abstract class GuideModule {
    @Binds @IntoMap @ActivityKey(GuideActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        GuideSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseReferrerSubcomponent.class) abstract class ChooseReferrerModule {
    @Binds @IntoMap @ActivityKey(ChooseReferrerActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        ChooseReferrerSubcomponent.Builder builder);
  }

  @Module(subcomponents = SimpleImgFragmentSubcomponent.class)
  abstract class SimpleImgFragmentModule {
    @Binds @IntoMap @FragmentKey(SimpleImgDialog.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SimpleImgFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = WebFragmentNoFreshFragmentSubcomponent.class)
  abstract class WebFragmentNoFreshFragmentModule {
    @Binds @IntoMap @FragmentKey(WebFragmentNoFresh.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WebFragmentNoFreshFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = YourFragmentSubcomponent.class) abstract class YourFragmentModule {
    @Binds @IntoMap @FragmentKey(AddNewCoachFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        YourFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ContainterSubcomponent.class) abstract class ContainterModule {
    @Binds @IntoMap @ActivityKey(ContainerActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        ContainterSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseSubcomponent.class) abstract class ChooseModule {
    @Binds @IntoMap @ActivityKey(ChooseActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        ChooseSubcomponent.Builder builder);
  }


  @Module(subcomponents = PopFromBottomSubcomponent.class) abstract class PopFromBottomModule {
    @Binds @IntoMap @ActivityKey(PopFromBottomActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        PopFromBottomSubcomponent.Builder builder);
  }

  @Module(subcomponents = StatmentFilterSubcomponent.class) abstract class StatmentFilterModule {
    @Binds @IntoMap @ActivityKey(StatmentFilterActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        StatmentFilterSubcomponent.Builder builder);
  }

  @Module(subcomponents = SplashSubcomponent.class) abstract class SplashModule {
    @Binds @IntoMap @ActivityKey(SplashActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        SplashSubcomponent.Builder builder);
  }

  @Module(subcomponents = WebSubcomponent.class) abstract class WebModule {
    @Binds @IntoMap @ActivityKey(WebActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        WebSubcomponent.Builder builder);
  }

  @Module(subcomponents = GymSubcomponent.class) abstract class GymModule {
    @Binds @IntoMap @ActivityKey(GymActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        GymSubcomponent.Builder builder);
  }

  ;

  @Module(subcomponents = SimpleChooseSubcomponent.class) abstract class SimpleChooseModule {
    @Binds @IntoMap @FragmentKey(SimpleChooseFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SimpleChooseSubcomponent.Builder builder);
  }

  @Module(subcomponents = WriteDescSubcomponent.class) abstract class WriteDescModule {
    @Binds @IntoMap @FragmentKey(WriteDescFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WriteDescSubcomponent.Builder builder);
  }

  @Module(subcomponents = HomeUnLoginFragmentSubcomponent.class)
  abstract class HomeUnLoginFragmentModule {
    @Binds @IntoMap @FragmentKey(HomeUnLoginFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        HomeUnLoginFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = MainSubcomponent.class) abstract class MainModule {
    @Binds @IntoMap @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        MainSubcomponent.Builder builder);
  }

  @Module(subcomponents = SettingSubcomponent.class) abstract class SettingModule {
    @Binds @IntoMap @FragmentKey(SettingFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SettingSubcomponent.Builder builder);
  }

  @Module(subcomponents = HomeSubcomponent.class) abstract class HomeModule {
    @Binds @IntoMap @FragmentKey(HomeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        HomeSubcomponent.Builder builder);
  }

  @Module(subcomponents = MainFirstSubcomponent.class) abstract class MainFirstModule {
    @Binds @IntoMap @FragmentKey(MainFirstFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        MainFirstSubcomponent.Builder builder);
  }

  @Module(subcomponents = GymsSubcomponent.class) abstract class GymsModule {
    @Binds @IntoMap @FragmentKey(GymsFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        GymsSubcomponent.Builder builder);
  }

  @Module(subcomponents = QcVipSubcomponent.class) abstract class QcVipModule {
    @Binds @IntoMap @FragmentKey(QcVipFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        QcVipSubcomponent.Builder builder);
  }

  @Module(subcomponents = MainMsgSubcomponent.class) abstract class MainMsgModule {
    @Binds @IntoMap @FragmentKey(MainMsgFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        MainMsgSubcomponent.Builder builder);
  }

  @Module(subcomponents = BaseStatementChartSubcomponent.class)
  abstract class BaseStatementChartModule {
    @Binds @IntoMap @FragmentKey(BaseStatementChartFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        BaseStatementChartSubcomponent.Builder builder);
  }

  @Module(subcomponents = UnloginAdSubcomponent.class) abstract class UnloginAdModule {
    @Binds @IntoMap @FragmentKey(UnloginAdFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        UnloginAdSubcomponent.Builder builder);
  }

  @Module(subcomponents = FixSelfInfoSubcomponent.class) abstract class FixSelfInfoModule {
    @Binds @IntoMap @FragmentKey(FixSelfInfoFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FixSelfInfoSubcomponent.Builder builder);
  }

  @Module(subcomponents = FixCheckinSubcomponent.class) abstract class FixCheckinModule {
    @Binds @IntoMap @FragmentKey(FixCheckinFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FixCheckinSubcomponent.Builder builder);
  }

  @Module(subcomponents = ReportSubcomponent.class) abstract class ReportModule {
    @Binds @IntoMap @FragmentKey(ReportFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ReportSubcomponent.Builder builder);
  }

  @Module(subcomponents = FixNotifySettingSubcomponent.class)
  abstract class FixNotifySettingModule {
    @Binds @IntoMap @FragmentKey(FixNotifySettingFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FixNotifySettingSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseBrandSubcomponent.class) abstract class ChooseBrandModule {
    @Binds @IntoMap @FragmentKey(ChooseBrandFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseBrandSubcomponent.Builder builder);
  }

  @Module(subcomponents = GymDetailSubcomponent.class) abstract class GymDetailModule {
    @Binds @IntoMap @FragmentKey(GymDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        GymDetailSubcomponent.Builder builder);
  }

  @Module(subcomponents = GymMoreSubcomponent.class) abstract class GymMoreModule {
    @Binds @IntoMap @FragmentKey(GymMoreFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        GymMoreSubcomponent.Builder builder);
  }

  @Module(subcomponents = GymExpireSubcomponent.class) abstract class GymExpireModule {
    @Binds @IntoMap @FragmentKey(GymExpireFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        GymExpireSubcomponent.Builder builder);
  }

  @Module(subcomponents = TrialProDialogSubcomponent.class) abstract class TrialProDialogModule {
    @Binds @IntoMap @FragmentKey(TrialProDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        TrialProDialogSubcomponent.Builder builder);
  }

  @Module(subcomponents = TopFilterSourceFragmentSubcomponent.class)
  abstract class TopFilterSourceFragmentModule {
    @Binds @IntoMap @FragmentKey(TopFilterSourceFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        TopFilterSourceFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SaleGlanceSubcomponent.class) abstract class SaleGlanceModule {
    @Binds @IntoMap @FragmentKey(SaleGlanceFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SaleGlanceSubcomponent.Builder builder);
  }

  @Module(subcomponents = SaleDetailSubcomponent.class) abstract class SaleDetailModule {
    @Binds @IntoMap @FragmentKey(SaleDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SaleDetailSubcomponent.Builder builder);
  }

  @Module(subcomponents = CustomSaleSubcomponent.class) abstract class CustomSaleModule {
    @Binds @IntoMap @FragmentKey(CustomSaleFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CustomSaleSubcomponent.Builder builder);
  }

  @Module(subcomponents = StatementGlanceSubcomponent.class) abstract class StatementGlanceModule {
    @Binds @IntoMap @FragmentKey(StatementGlanceFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StatementGlanceSubcomponent.Builder builder);
  }

  @Module(subcomponents = StatementDetailSubcomponent.class) abstract class StatementDetailModule {
    @Binds @IntoMap @FragmentKey(StatementDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StatementDetailSubcomponent.Builder builder);
  }

  @Module(subcomponents = CustomStatmentSubcomponent.class) abstract class CustomStatmentModule {
    @Binds @IntoMap @FragmentKey(CustomStatmentFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CustomStatmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SigninGlanceSubcomponent.class) abstract class SigninGlanceModule {
    @Binds @IntoMap @FragmentKey(SigninGlanceFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SigninGlanceSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInDetailSubcomponent.class) abstract class SignInDetailModule {
    @Binds @IntoMap @FragmentKey(SignInDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInDetailSubcomponent.Builder builder);
  }

  @Module(subcomponents = SigninReportFormFragmentSubcomponent.class)
  abstract class SigninReportFormFragmentModule {
    @Binds @IntoMap @FragmentKey(SigninReportFormFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SigninReportFormFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SigninReportSubcomponent.class) abstract class SigninReportModule {
    @Binds @IntoMap @FragmentKey(SigninReportFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SigninReportSubcomponent.Builder builder);
  }

  @Module(subcomponents = CustomSigninSubcomponent.class) abstract class CustomSigninModule {
    @Binds @IntoMap @FragmentKey(CustomSigninFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CustomSigninSubcomponent.Builder builder);
  }

  @Module(subcomponents = OutExcelSubcomponent.class) abstract class OutExcelModule {
    @Binds @IntoMap @FragmentKey(OutExcelFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        OutExcelSubcomponent.Builder builder);
  }

  @Module(subcomponents = CoachChooseDialogSubcomponent.class)
  abstract class CoachChooseDialogModule {
    @Binds @IntoMap @FragmentKey(CoachChooseDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CoachChooseDialogSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseWardrobeSubcomponent.class) abstract class ChooseWardrobeModule {
    @Binds @IntoMap @ActivityKey(ChooseWardrobeActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        ChooseWardrobeSubcomponent.Builder builder);
  }

  @Module(subcomponents = MutiChooseStudentSubcomponent.class)
  abstract class MutiChooseStudentModule {
    @Binds @IntoMap @ActivityKey(MutiChooseStudentActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        MutiChooseStudentSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeSubcomponent.class) abstract class WardrobeModule {
    @Binds @IntoMap @ActivityKey(WardrobeActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        WardrobeSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeAddSubcomponent.class) abstract class WardrobeAddModule {
    @Binds @IntoMap @FragmentKey(WardrobeAddFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobeAddSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeLongHireSubcomponent.class)
  abstract class WardrobeLongHireModule {
    @Binds @IntoMap @FragmentKey(WardrobeLongHireFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobeLongHireSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeEditSubcomponent.class) abstract class WardrobeEditModule {
    @Binds @IntoMap @FragmentKey(WardrobeEditFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobeEditSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeContinueHireSubcomponent.class)
  abstract class WardrobeContinueHireModule {
    @Binds @IntoMap @FragmentKey(WardrobeContinueHireFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobeContinueHireSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeReturnDialogSubcomponent.class)
  abstract class WardrobeReturnDialogModule {
    @Binds @IntoMap @FragmentKey(WardrobeReturnDialog.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobeReturnDialogSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeReturnSubcomponent.class) abstract class WardrobeReturnModule {
    @Binds @IntoMap @FragmentKey(WardrobeReturnFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobeReturnSubcomponent.Builder builder);
  }

  @Module(subcomponents = DistrictListSubcomponent.class) abstract class DistrictListModule {
    @Binds @IntoMap @FragmentKey(DistrictListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        DistrictListSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeShortHireSubcomponent.class)
  abstract class WardrobeShortHireModule {
    @Binds @IntoMap @FragmentKey(WardrobeShortHireFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobeShortHireSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseMainFragmentSubcomponent.class)
  abstract class ChooseMainFragmentModule {
    @Binds @IntoMap @FragmentKey(ChooseMainFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseMainFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SearchResultFragmentSubcomponent.class)
  abstract class SearchResultFragmentModule {
    @Binds @IntoMap @FragmentKey(SearchResultFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SearchResultFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = MutiChooseStudentFragSubcomponent.class)
  abstract class MutiChooseStudentFragModule {
    @Binds @IntoMap @FragmentKey(MutiChooseStudentFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        MutiChooseStudentFragSubcomponent.Builder builder);
  }

  @Module(subcomponents = DistrictAddSubcomponent.class) abstract class DistrictAddModule {
    @Binds @IntoMap @FragmentKey(DistrictAddFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        DistrictAddSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseRegionSubcomponent.class) abstract class ChooseRegionModule {
    @Binds @IntoMap @FragmentKey(ChooseRegionFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseRegionSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeMainSubcomponent.class) abstract class WardrobeMainModule {
    @Binds @IntoMap @FragmentKey(WardrobeMainFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobeMainSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobePayBottomSubcomponent.class)
  abstract class WardrobePayBottomModule {
    @Binds @IntoMap @FragmentKey(WardrobePayBottomFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobePayBottomSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeDetailFragmentSubcomponent.class)
  abstract class WardrobeDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(WardrobeDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobeDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SiteListSubcomponent.class) abstract class SiteListModule {
    @Binds @IntoMap @FragmentKey(SiteListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SiteListSubcomponent.Builder builder);
  }

  @Module(subcomponents = SiteDetailSubcomponent.class) abstract class SiteDetailModule {
    @Binds @IntoMap @FragmentKey(SiteDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SiteDetailSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseSiteSubcomponent.class) abstract class ChooseSiteModule {
    @Binds @IntoMap @FragmentKey(ChooseSiteFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseSiteSubcomponent.Builder builder);
  }

  @Module(subcomponents = MutiChooseSiteSubcomponent.class) abstract class MutiChooseSiteModule {
    @Binds @IntoMap @FragmentKey(MutiChooseSiteFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        MutiChooseSiteSubcomponent.Builder builder);
  }

  @Module(subcomponents = StaffDetailFragmentSubcomponent.class)
  abstract class StaffDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(StaffDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StaffDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ScheduleSubcomponent.class) abstract class ScheduleModule {
    @Binds @IntoMap @ActivityKey(ScheduleActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        ScheduleSubcomponent.Builder builder);
  }

  @Module(subcomponents = ScheduleListSubcomponent.class) abstract class ScheduleListModule {
    @Binds @IntoMap @FragmentKey(ScheduleListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ScheduleListSubcomponent.Builder builder);
  }



  @Module(subcomponents = SignInHomeSubcomponent.class) abstract class SignInHomeModule {
    @Binds @IntoMap @FragmentKey(SignInHomeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInHomeSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInSubFragcomponent.class) abstract class SignInFragModule {
    @Binds @IntoMap @FragmentKey(SignInFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInSubFragcomponent.Builder builder);
  }

  @Module(subcomponents = SignOutSubcomponent.class) abstract class SignOutModule {
    @Binds @IntoMap @FragmentKey(SignOutFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignOutSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInConfigSubcomponent.class) abstract class SignInConfigModule {
    @Binds @IntoMap @FragmentKey(SignInConfigFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInConfigSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInLogSubcomponent.class) abstract class SignInLogModule {
    @Binds @IntoMap @FragmentKey(SignInLogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInLogSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInListSubcomponent.class) abstract class SignInListModule {
    @Binds @IntoMap @FragmentKey(SignInListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInListSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInStudentListSubcomponent.class)
  abstract class SignInStudentListModule {
    @Binds @IntoMap @FragmentKey(SignInStudentListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInStudentListSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignOutListSubcomponent.class) abstract class SignOutListModule {
    @Binds @IntoMap @FragmentKey(SignOutListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignOutListSubcomponent.Builder builder);
  }

  @Module(subcomponents = SinginConfigWardrobeSubcomponent.class)
  abstract class SinginConfigWardrobeModule {
    @Binds @IntoMap @FragmentKey(SinginConfigWardrobeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SinginConfigWardrobeSubcomponent.Builder builder);
  }

  @Module(subcomponents = SigninConfigCardtypeListSubcomponent.class)
  abstract class SigninConfigCardtypeListModule {
    @Binds @IntoMap @FragmentKey(SigninConfigCardtypeListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SigninConfigCardtypeListSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInConfigScreenSubcomponent.class)
  abstract class SignInConfigScreenModule {
    @Binds @IntoMap @FragmentKey(SignInConfigScreenFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInConfigScreenSubcomponent.Builder builder);
  }

  @Module(subcomponents = SigninConfigListSubcomponent.class)
  abstract class SigninConfigListModule {
    @Binds @IntoMap @FragmentKey(SigninConfigListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SigninConfigListSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInTypeSubcomponent.class) abstract class SignInTypeModule {
    @Binds @IntoMap @FragmentKey(SignInTypeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInTypeSubcomponent.Builder builder);
  }

  @Module(subcomponents = ScoreHomeSubcomponent.class) abstract class ScoreHomeModule {
    @Binds @IntoMap @FragmentKey(ScoreHomeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ScoreHomeSubcomponent.Builder builder);
  }

  @Module(subcomponents = ScoreConfigSubcomponent.class) abstract class ScoreConfigModule {
    @Binds @IntoMap @FragmentKey(ConfigFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ScoreConfigSubcomponent.Builder builder);
  }

  @Module(subcomponents = BaseConfigSubcomponent.class) abstract class BaseConfigModule {
    @Binds @IntoMap @FragmentKey(BaseConfigFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        BaseConfigSubcomponent.Builder builder);
  }

  @Module(subcomponents = ScoreRuleAddSubcomponent.class) abstract class ScoreRuleAddModule {
    @Binds @IntoMap @FragmentKey(ScoreRuleAddFragemnt.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ScoreRuleAddSubcomponent.Builder builder);
  }

  @Module(subcomponents = ScoreAwardAddSubcomponent.class) abstract class ScoreAwardAddModule {
    @Binds @IntoMap @FragmentKey(ScoreAwardAddFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ScoreAwardAddSubcomponent.Builder builder);
  }

  @Module(subcomponents = DataStatementSubcomponent.class) abstract class DataStatementModule {
    @Binds @IntoMap @FragmentKey(DataStatementFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        DataStatementSubcomponent.Builder builder);
  }

  @Module(subcomponents = SalerChooseDialogSubcomponent.class)
  abstract class SalerChooseDialogModule {
    @Binds @IntoMap @FragmentKey(SalerChooseDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SalerChooseDialogSubcomponent.Builder builder);
  }

  @Module(subcomponents = StudentSearchSubcomponent.class) abstract class StudentSearchModule {
    @Binds @IntoMap @FragmentKey(StudentSearchFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StudentSearchSubcomponent.Builder builder);
  }

  @Module(subcomponents = MutiChooseSalersSubcomponent.class)
  abstract class MutiChooseSalersModule {
    @Binds @IntoMap @ActivityKey(MutiChooseSalersActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        MutiChooseSalersSubcomponent.Builder builder);
  }

  @Module(subcomponents = NotificationSubcomponent.class) abstract class NotificationModule {
    @Binds @IntoMap @FragmentKey(NotificationFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        NotificationSubcomponent.Builder builder);
  }

  @Module(subcomponents = NotificationASubcomponent.class) abstract class NotificationAModule {
    @Binds @IntoMap @ActivityKey(NotificationActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        NotificationASubcomponent.Builder builder);
  }

  @Module(subcomponents = WebFSubcomponent.class) abstract class WebFModule {
    @Binds @IntoMap @FragmentKey(WebFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WebFSubcomponent.Builder builder);
  }

  @Module(subcomponents = ConversationFriendsSubcomponent.class)
  abstract class ConversationFriendsModule {
    @Binds @IntoMap @FragmentKey(ConversationFriendsFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ConversationFriendsSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChatFriendAllChooseSubcomponent.class)
  abstract class ChatFriendAllChooseModule {
    @Binds @IntoMap @FragmentKey(ChatFriendAllChooseFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChatFriendAllChooseSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChatChooseInGymSubcomponent.class) abstract class ChatChooseInGymModule {
    @Binds @IntoMap @FragmentKey(ChatChooseInGymFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChatChooseInGymSubcomponent.Builder builder);
  }

  @Module(subcomponents = GymDetailShowGuideDialogSubcomponent.class)
  abstract class GymDetailShowGuideDialogModule {
    @Binds @IntoMap @FragmentKey(GymDetailShowGuideDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        GymDetailShowGuideDialogSubcomponent.Builder builder);
  }

  @Module(subcomponents = BatchPayOnlineSubcomponent.class) abstract class BatchPayOnlineModule {
    @Binds @IntoMap @FragmentKey(BatchPayOnlineFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        BatchPayOnlineSubcomponent.Builder builder);
  }

  @Module(subcomponents = StudentSignInImageFragmentSubcomponent.class)
  abstract class StudentSignInImageFragmentModule {
    @Binds @IntoMap @FragmentKey(StudentSignInImageFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StudentSignInImageFragmentSubcomponent.Builder builder);
  }


  @Module(subcomponents = FlexableListSubcomponent.class) abstract class FlexableListModule {
    @Binds @IntoMap @FragmentKey(FlexableListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FlexableListSubcomponent.Builder builder);
  }

  @Module(subcomponents = MultiChooseStudentWithFilterSubcomponent.class)
  abstract class MultiChooseStudentWithFilterModule {
    @Binds @IntoMap @FragmentKey(MultiChooseStudentWithFilterFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        MultiChooseStudentWithFilterSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseStudentListSubcomponent.class)
  abstract class ChooseStudentListModule {
    @Binds @IntoMap @FragmentKey(ChooseStudentListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseStudentListSubcomponent.Builder builder);
  }

  @Module(subcomponents = StudentFilterWithBirthSubcomponent.class)
  abstract class StudentFilterWithBirthModule {
    @Binds @IntoMap @FragmentKey(StudentFilterWithBirthFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StudentFilterWithBirthSubcomponent.Builder builder);
  }

  @Module(subcomponents = AttendanceSubcomponent.class) abstract class AttendanceModule {
    @Binds @IntoMap @ActivityKey(AttendanceActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        AttendanceSubcomponent.Builder builder);
  }

  @Module(subcomponents = AttendanceHomeSubcomponent.class) abstract class AttendanceHomeModule {
    @Binds @IntoMap @FragmentKey(AttendanceHomeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AttendanceHomeSubcomponent.Builder builder);
  }

  @Module(subcomponents = AttendanceFilterSubcomponent.class)
  abstract class AttendanceFilterModule {
    @Binds @IntoMap @FragmentKey(FilterFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AttendanceFilterSubcomponent.Builder builder);
  }

  @Module(subcomponents = AttendanceStaticSubcomponent.class)
  abstract class AttendanceStaticModule {
    @Binds @IntoMap @FragmentKey(AttendanceStaticFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AttendanceStaticSubcomponent.Builder builder);
  }

  @Module(subcomponents = AbsenceStuentListSubcomponent.class)
  abstract class AbsenceStuentListModule {
    @Binds @IntoMap @FragmentKey(AbsenceStuentListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AbsenceStuentListSubcomponent.Builder builder);
  }

  @Module(subcomponents = AttendanceRankSubcomponent.class) abstract class AttendanceRankModule {
    @Binds @IntoMap @FragmentKey(AttendanceRankFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AttendanceRankSubcomponent.Builder builder);
  }

  @Module(subcomponents = AttendanceFilterCustomSubcomponent.class)
  abstract class FilterCustomModule {
    @Binds @IntoMap @FragmentKey(FilterCustomFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AttendanceFilterCustomSubcomponent.Builder builder);
  }

  @Module(subcomponents = FollowUpSubcomponent.class) abstract class FollowUpModule {
    @Binds @IntoMap @ActivityKey(FollowUpActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        FollowUpSubcomponent.Builder builder);
  }

  @Module(subcomponents = FollowUpFSubcomponent.class) abstract class FollowFUpModule {
    @Binds @IntoMap @FragmentKey(FollowUpFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FollowUpFSubcomponent.Builder builder);
  }

  @Module(subcomponents = FollowUpDataTransferSubcomponent.class)
  abstract class FollowUpDataTransferModule {
    @Binds @IntoMap @FragmentKey(FollowUpDataTransferFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FollowUpDataTransferSubcomponent.Builder builder);
  }

  @Module(subcomponents = FollowUpDataStatisticsSubcomponent.class)
  abstract class FollowUpDataStatisticsModule {
    @Binds @IntoMap @FragmentKey(FollowUpDataStatisticsFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FollowUpDataStatisticsSubcomponent.Builder builder);
  }

  @Module(subcomponents = FollowUpDataTransfer0Subcomponent.class)
  abstract class FollowUpDataTransfer0Module {
    @Binds @IntoMap @FragmentKey(FollowUpDataTransfer0Fragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FollowUpDataTransfer0Subcomponent.Builder builder);
  }

  @Module(subcomponents = FollowUpDataStatisticsBaseSubcomponent.class)
  abstract class FollowUpDataStatisticsBaseModule {
    @Binds @IntoMap @FragmentKey(FollowUpDataStatisticsBaseFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FollowUpDataStatisticsBaseSubcomponent.Builder builder);
  }

  @Module(subcomponents = FollowUpDataStatistics0FragmentSubcomponent.class)
  abstract class FollowUpDataStatistics0FragmentModule {
    @Binds @IntoMap @FragmentKey(FollowUpDataStatistics0Fragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FollowUpDataStatistics0FragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = FollowUpDataStatistics1FragmentSubcomponent.class)
  abstract class FollowUpDataStatistics1FragmentModule {
    @Binds @IntoMap @FragmentKey(FollowUpDataStatistics1Fragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FollowUpDataStatistics1FragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = FollowUpDataStatistics2FragmentSubcomponent.class)
  abstract class FollowUpDataStatistics2FragmentModule {
    @Binds @IntoMap @FragmentKey(FollowUpDataStatistics2Fragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FollowUpDataStatistics2FragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = FollowUpStatusFragmentSubcomponent.class)
  abstract class FollowUpStatusFragmentModule {
    @Binds @IntoMap @FragmentKey(FollowUpStatusFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FollowUpStatusFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = TopFilterSaleFragmentSubcomponent.class)
  abstract class TopFilterSaleFragmentModule {
    @Binds @IntoMap @FragmentKey(TopFilterSaleFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        TopFilterSaleFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = TopFilterRegisterFragmentSubcomponent.class)
  abstract class TopFilterRegisterFragmentModule {
    @Binds @IntoMap @FragmentKey(TopFilterRegisterFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        TopFilterRegisterFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = TopFilterLatestFollowFragmentSubcomponent.class)
  abstract class TopFilterLatestFollowFragmentModule {
    @Binds @IntoMap @FragmentKey(TopFilterLatestFollowFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        TopFilterLatestFollowFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = LatestTimeFragmentSubcomponent.class)
  abstract class LatestTimeFragmentModule {
    @Binds @IntoMap @FragmentKey(LatestTimeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        LatestTimeFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = BodyTestListFragmentSubcomponent.class)
  abstract class BodyTestListFragmentModule {
    @Binds @IntoMap @FragmentKey(BodyTestListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        BodyTestListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ModifyBodyTestFragmentSubcomponent.class)
  abstract class ModifyBodyTestFragmentModule {
    @Binds @IntoMap @FragmentKey(ModifyBodyTestFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ModifyBodyTestFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = BodyTestFragmentSubcomponent.class)
  abstract class BodyTestFragmentModule {
    @Binds @IntoMap @FragmentKey(BodyTestFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        BodyTestFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = AllotSalesSubcomponent.class) abstract class AllotSalesModule {
    @Binds @IntoMap @ActivityKey(AllotSalesActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        AllotSalesSubcomponent.Builder builder);
  }

  @Module(subcomponents = AllotSaleDetailSubcomponent.class) abstract class AllotSaleDetailModule {
    @Binds @IntoMap @FragmentKey(cn.qingchengfit.staffkit.views.allotsales.SaleDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AllotSaleDetailSubcomponent.Builder builder);
  }

  @Module(subcomponents = SalesListSubcomponent.class) abstract class SalesListModule {
    @Binds @IntoMap @FragmentKey(SalesListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SalesListSubcomponent.Builder builder);
  }

  @Module(subcomponents = StudentFilterSubcomponent.class) abstract class StudentFilterModule {
    @Binds @IntoMap @FragmentKey(StudentFilterFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StudentFilterSubcomponent.Builder builder);
  }

  @Module(subcomponents = MultiModifySubcomponent.class) abstract class MultiModifyModule {
    @Binds @IntoMap @FragmentKey(MultiModifyFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        MultiModifySubcomponent.Builder builder);
  }

  @Module(subcomponents = StudentsDetailSubcomponent.class) abstract class StudentsDetailModule {
    @Binds @IntoMap @ActivityKey(StudentsDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        StudentsDetailSubcomponent.Builder builder);
  }

  @Module(subcomponents = ScoreDetailASubcomponent.class) abstract class ScoreDetailAModule {
    @Binds @IntoMap @ActivityKey(ScoreDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        ScoreDetailASubcomponent.Builder builder);
  }

  @Module(subcomponents = ScoreDetailSubcomponent.class) abstract class ScoreDetailModule {
    @Binds @IntoMap @FragmentKey(ScoreDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ScoreDetailSubcomponent.Builder builder);
  }

  @Module(subcomponents = ScoreModifySubcomponent.class) abstract class ScoreModifyModule {
    @Binds @IntoMap @FragmentKey(ScoreModifyFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ScoreModifySubcomponent.Builder builder);
  }

  @Module(subcomponents = StudentListSubcomponent.class) abstract class StudentListModule {
    @Binds @IntoMap @FragmentKey(StudentListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StudentListSubcomponent.Builder builder);
  }

  @Module(subcomponents = StudentOperationSubcomponent.class)
  abstract class StudentOperationModule {
    @Binds @IntoMap @FragmentKey(StudentOperationFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StudentOperationSubcomponent.Builder builder);
  }

  @Module(subcomponents = EditStudentInfoSubcomponent.class) abstract class EditStudentInfoModule {
    @Binds @IntoMap @FragmentKey(EditStudentInfoFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        EditStudentInfoSubcomponent.Builder builder);
  }

  @Module(subcomponents = ReferrerSubcomponent.class) abstract class ReferrerModule {
    @Binds @IntoMap @FragmentKey(ReferrerFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ReferrerSubcomponent.Builder builder);
  }

  @Module(subcomponents = SourceSubcomponent.class) abstract class SourceModule {
    @Binds @IntoMap @FragmentKey(SourceFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SourceSubcomponent.Builder builder);
  }

  @Module(subcomponents = StudentHomeSubcomponent.class) abstract class StudentHomeModule {
    @Binds @IntoMap @FragmentKey(StudentHomeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StudentHomeSubcomponent.Builder builder);
  }

  @Module(subcomponents = FollowRecordPageSubcomponent.class)
  abstract class FollowRecordPageModule {
    @Binds @IntoMap @FragmentKey(FollowRecordPage.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        FollowRecordPageSubcomponent.Builder builder);
  }

  @Module(subcomponents = SimpleCardListFragmentSubcomponent.class)
  abstract class SimpleCardListFragmentModule {
    @Binds @IntoMap @FragmentKey(SimpleCardListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SimpleCardListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = StudentsCardsSubcomponent.class) abstract class StudentsCardsModule {
    @Binds @IntoMap @FragmentKey(StudentsCardsFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StudentsCardsSubcomponent.Builder builder);
  }

  @Module(subcomponents = StudentMoreInfoSubcomponent.class) abstract class StudentMoreInfoModule {
    @Binds @IntoMap @FragmentKey(StudentMoreInfoFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        StudentMoreInfoSubcomponent.Builder builder);
  }

  @Module(subcomponents = CoachDetailFragmentSubcomponent.class)
  abstract class CoachDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(CoachDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CoachDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = WardrobeListFragmentSubcomponent.class)
  abstract class WardrobeListFragmentModule {
    @Binds @IntoMap @FragmentKey(WardrobeListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WardrobeListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInCloseFragmentSubcomponent.class)
  abstract class SignInCloseFragmentModule {
    @Binds @IntoMap @FragmentKey(SignInCloseFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInCloseFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInManualSubcomponent.class) abstract class SignInManualModule {
    @Binds @IntoMap @ActivityKey(SignInManualActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        SignInManualSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignInManualFragmentSubcomponent.class)
  abstract class SignInManualFragmentModule {
    @Binds @IntoMap @FragmentKey(SignInManualFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInManualFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignOutManualFragmentSubcomponent.class)
  abstract class SignOutManualFragmentModule {
    @Binds @IntoMap @FragmentKey(SignOutManualFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignOutManualFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = AddNewSiteFragmentSubcomponent.class)
  abstract class AddNewSiteFragmentModule {
    @Binds @IntoMap @FragmentKey(AddNewSiteFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AddNewSiteFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = BrandManageSubcomponent.class) abstract class BrandManageModule {
    @Binds @IntoMap @ActivityKey(BrandManageActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        BrandManageSubcomponent.Builder builder);
  }

  @Module(subcomponents = BrandManageFragmentSubcomponent.class)
  abstract class BrandManageFragmentModule {
    @Binds @IntoMap @FragmentKey(BrandManageFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        BrandManageFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = BrandDetailFragmentSubcomponent.class)
  abstract class BrandDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(BrandDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        BrandDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = BrandEditFragmentSubcomponent.class)
  abstract class BrandEditFragmentModule {
    @Binds @IntoMap @FragmentKey(BrandEditFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        BrandEditFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = BrandCreatorEditFragmentSubcomponent.class)
  abstract class BrandCreatorEditFragmentModule {
    @Binds @IntoMap @FragmentKey(BrandCreatorEditFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        BrandCreatorEditFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = UpgradeInfoDialogFragmentSubcomponent.class)
  abstract class UpgradeInfoDialogFragmentModule {
    @Binds @IntoMap @FragmentKey(UpgradeInfoDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        UpgradeInfoDialogFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = TrialProDialogFragmentSubcomponent.class)
  abstract class TrialProDialogFragmentModule {
    @Binds @IntoMap @FragmentKey(TrialProDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        TrialProDialogFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = UpgrateGymSubcomponent.class) abstract class UpgrateGymModule {
    @Binds @IntoMap @FragmentKey(UpgrateGymFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        UpgrateGymSubcomponent.Builder builder);
  }

  @Module(subcomponents = RenewalHistoryFragmentSubcomponent.class)
  abstract class RenewalHistoryFragmentModule {
    @Binds @IntoMap @FragmentKey(RenewalHistoryFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RenewalHistoryFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = EditTextSubcomponent.class) abstract class EditTextModule {
    @Binds @IntoMap @ActivityKey(EditTextActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        EditTextSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseBrandInMainFragmentSubcomponent.class)
  abstract class ChooseBrandInMainFragmentModule {
    @Binds @IntoMap @FragmentKey(ChooseBrandInMainFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseBrandInMainFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SetGymInMainFragmentSubcomponent.class)
  abstract class SetGymInMainFragmentModule {
    @Binds @IntoMap @FragmentKey(SetGymInMainFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SetGymInMainFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = AddBrandInMainFragmentSubcomponent.class)
  abstract class AddBrandInMainFragmentModule {
    @Binds @IntoMap @FragmentKey(AddBrandInMainFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AddBrandInMainFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = WriteAddressFragmentSubcomponent.class)
  abstract class WriteAddressFragmentModule {
    @Binds @IntoMap @FragmentKey(WriteAddressFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        WriteAddressFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = MutiChooseGymFragmentSubcomponent.class)
  abstract class MutiChooseGymFragmentModule {
    @Binds @IntoMap @FragmentKey(MutiChooseGymFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        MutiChooseGymFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SetGymFragmentSubcomponent.class) abstract class SetGymFragmentModule {
    @Binds @IntoMap @FragmentKey(SetGymFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SetGymFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ArticleReplyFragmentSubcomponent.class)
  abstract class ArticleReplyFragmentModule {
    @Binds @IntoMap @FragmentKey(ArticleReplyFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ArticleReplyFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ArticleCommentsListFragmentSubcomponent.class)
  abstract class ArticleCommentsListFragmentModule {
    @Binds @IntoMap @FragmentKey(ArticleCommentsListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ArticleCommentsListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = QuitGymFragmentSubcomponent.class) abstract class QuitGymFragmentModule {
    @Binds @IntoMap @FragmentKey(QuitGymFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        QuitGymFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SaleTradeTypeFormFragmentSubcomponent.class)
  abstract class SaleTradeTypeFormFragmentModule {
    @Binds @IntoMap @FragmentKey(SaleTradeTypeFormFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SaleTradeTypeFormFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = CourseCardFormFragmentSubcomponent.class)
  abstract class CourseCardFormFragmentModule {
    @Binds @IntoMap @FragmentKey(CourseCardFormFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CourseCardFormFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SaleCardTypeFragmentSubcomponent.class)
  abstract class SaleCardTypeFragmentModule {
    @Binds @IntoMap @FragmentKey(SaleCardTypeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SaleCardTypeFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = CourseTypeFormFragmentSubcomponent.class)
  abstract class CourseTypeFormFragmentModule {
    @Binds @IntoMap @FragmentKey(CourseTypeFormFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CourseTypeFormFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SaleFilterSubcomponent.class) abstract class SaleFilterModule {
    @Binds @IntoMap @ActivityKey(SaleFilterActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        SaleFilterSubcomponent.Builder builder);
  }

  @Module(subcomponents = GuideChooseBrandSubcomponent.class)
  abstract class GuideChooseBrandModule {
    @Binds @IntoMap @ActivityKey(GuideChooseBrandAcitivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        GuideChooseBrandSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseBrandAcitiviySubcomponent.class)
  abstract class ChooseBrandActivityModule {
    @Binds @IntoMap @ActivityKey(ChooseBrandActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        ChooseBrandAcitiviySubcomponent.Builder builder);
  }

  @Module(subcomponents = AddBrandSubcomponent.class) abstract class AddBrandModule {
    @Binds @IntoMap @ActivityKey(AddBrandActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        AddBrandSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseOriginSubcomponent.class) abstract class ChooseOriginModule {
    @Binds @IntoMap @ActivityKey(ChooseOriginActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        ChooseOriginSubcomponent.Builder builder);
  }

  @Module(subcomponents = CreateGroupFragmentSubcomponent.class)
  abstract class CreateGroupFragmentModule {
    @Binds @IntoMap @FragmentKey(CreateGroupFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CreateGroupFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = DeleteMemberFragmentSubcomponent.class)
  abstract class DeleteMemberFragmentModule {
    @Binds @IntoMap @FragmentKey(DeleteMemberFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        DeleteMemberFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = MemberOperationFragmentSubcomponent.class)
  abstract class MemberOperationFragmentModule {
    @Binds @IntoMap @FragmentKey(MemberOperationFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        MemberOperationFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = RankCountFragmentSubcomponent.class)
  abstract class RankCountFragmentModule {
    @Binds @IntoMap @FragmentKey(RankCountFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RankCountFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignUpChangeNameFragmentSubcomponent.class)
  abstract class SignUpChangeNameFragmentModule {
    @Binds @IntoMap @FragmentKey(SignUpChangeNameFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignUpChangeNameFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignUpFormGroupFragmentSubcomponent.class)
  abstract class SignUpFormGroupFragmentModule {
    @Binds @IntoMap @FragmentKey(SignUpFormGroupFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignUpFormGroupFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignUpFormHomeFragmentSubcomponent.class)
  abstract class SignUpFormHomeFragmentModule {
    @Binds @IntoMap @FragmentKey(SignUpFormHomeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignUpFormHomeFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignUpFormPersonalFragmentSubcomponent.class)
  abstract class SignUpFormPersonalFragmentModule {
    @Binds @IntoMap @FragmentKey(SignUpFormPersonalFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignUpFormPersonalFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignUpGroupDetailFragmentSubcomponent.class)
  abstract class SignUpGroupDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(SignUpGroupDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignUpGroupDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignUpPersonalDetailFragmentSubcomponent.class)
  abstract class SignUpPersonalDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(SignUpPersonalDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignUpPersonalDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = TrainChooseGymFragmentSubcomponent.class)
  abstract class TrainChooseGymFragmentModule {
    @Binds @IntoMap @FragmentKey(TrainChooseGymFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        TrainChooseGymFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignUpDetailSubcomponent.class) abstract class SignUpDetailModule {
    @Binds @IntoMap @ActivityKey(SignUpDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        SignUpDetailSubcomponent.Builder builder);
  }

  @Module(subcomponents = SignUpChooseSubcomponent.class) abstract class SignUpChooseModule {
    @Binds @IntoMap @ActivityKey(SignUpChooseActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        SignUpChooseSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseAddressFragmentSubcomponent.class)
  abstract class ChooseAddressFragmentModule {
    @Binds @IntoMap @FragmentKey(ChooseAddressFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseAddressFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = GymInfoNoEditFragmentSubcomponent.class)
  abstract class GymInfoNoEditFragmentModule {
    @Binds @IntoMap @FragmentKey(GymInfoNoEditFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        GymInfoNoEditFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = GymInfoFragmentSubcomponent.class) abstract class GymInfoFragmentModule {
    @Binds @IntoMap @FragmentKey(GymInfoFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        GymInfoFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = CourseReverseFragmentSubcomponent.class)
  abstract class CourseReverseFragmentModule {
    @Binds @IntoMap @FragmentKey(CourseReverseFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CourseReverseFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = AllocateCoachSubcomponent.class) abstract class AllocateCoachModule {
    @Binds @IntoMap @ActivityKey(AllocateCoachActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        AllocateCoachSubcomponent.Builder builder);
  }

  @Module(subcomponents = AddStudentFragmentSubcomponent.class)
  abstract class AddStudentFragmentModule {
    @Binds @IntoMap @FragmentKey(AddStudentFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AddStudentFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = AllocateCoachListFragmentSubcomponent.class)
  abstract class AllocateCoachListFragmentModule {
    @Binds @IntoMap @FragmentKey(AllocateCoachListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AllocateCoachListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = CoachStudentDetailFragmentSubcomponent.class)
  abstract class CoachStudentDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(CoachStudentDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CoachStudentDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = OperationStudentFragmentSubcomponent.class)
  abstract class OperationStudentFragmentModule {
    @Binds @IntoMap @FragmentKey(OperationStudentFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        OperationStudentFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = MutiChooseCoachSubcomponent.class) abstract class MutiChooseCoachModule {
    @Binds @IntoMap @ActivityKey(MutiChooseCoachActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        MutiChooseCoachSubcomponent.Builder builder);
  }

  @Module(subcomponents = CardTypeChooseDialogFragmentSubcomponent.class)
  abstract class CardTypeChooseDialogFragmentModule {
    @Binds @IntoMap @FragmentKey(CardTypeChooseDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CardTypeChooseDialogFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = BottomBuyLimitFragmentSubcomponent.class)
  abstract class BottomBuyLimitFragmentModule {
    @Binds @IntoMap @FragmentKey(BottomBuyLimitFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        BottomBuyLimitFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = JobSearchChatSubcomponent.class) abstract class JobSearchChatModule {
    @Binds @IntoMap @ActivityKey(JobSearchChatActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        JobSearchChatSubcomponent.Builder builder);
  }

  @Module(subcomponents = RecruitMessageListFragmentSubcomponent.class)
  abstract class RecruitMessageListFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitMessageListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitMessageListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseCoachFragmentSubcomponent.class)
  abstract class ChooseCoachFragmentModule {
    @Binds @IntoMap @FragmentKey(ChooseCoachFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseCoachFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseStaffFragmentSubcomponent.class)
  abstract class ChooseStaffFragmentModule {
    @Binds @IntoMap @FragmentKey(ChooseStaffFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseStaffFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = NotiSettingChargeHistoryFragmentSubcomponent.class)
  abstract class NotiSettingChargeHistoryFragmentModule {
    @Binds @IntoMap @FragmentKey(NotiSettingChargeHistoryFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        NotiSettingChargeHistoryFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = NotiSettingChargeResultFragmentSubcomponent.class)
  abstract class NotiSettingChargeResultFragmentModule {
    @Binds @IntoMap @FragmentKey(NotiSettingChargeResultFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        NotiSettingChargeResultFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = NotiSettingMsgChargeFragmentSubcomponent.class)
  abstract class NotiSettingMsgChargeFragmentModule {
    @Binds @IntoMap @FragmentKey(NotiSettingMsgChargeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        NotiSettingMsgChargeFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = NotiSettingMsgDetailFragmentSubcomponent.class)
  abstract class NotiSettingMsgDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(NotiSettingMsgDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        NotiSettingMsgDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = NotiSettingMsgRuleFragmentSubcomponent.class)
  abstract class NotiSettingMsgRuleFragmentModule {
    @Binds @IntoMap @FragmentKey(NotiSettingMsgRuleFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        NotiSettingMsgRuleFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = NotiSettingSendListDetailFragmentSubcomponent.class)
  abstract class NotiSettingSendListDetailFragmentModule {
    @Binds @IntoMap @FragmentKey(NotiSettingSendListDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        NotiSettingSendListDetailFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = NotiSettingWxTemplateFragmentSubcomponent.class)
  abstract class NotiSettingWxTemplateFragmentModule {
    @Binds @IntoMap @FragmentKey(NotiSettingWxTemplateFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        NotiSettingWxTemplateFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = SendChannelTabFragmentSubcomponent.class)
  abstract class SendChannelTabFragmentModule {
    @Binds @IntoMap @FragmentKey(SendChannelTabFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SendChannelTabFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = NotiSettingHomeFragmentSubcomponent.class)
  abstract class NotiSettingHomeFragmentModule {
    @Binds @IntoMap @FragmentKey(NotiSettingHomeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        NotiSettingHomeFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface EditGymInfoFragmentSubcomponent
      extends AndroidInjector<EditGymInfoFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<EditGymInfoFragment> {
    }
  }

  @Module(subcomponents = EditGymInfoFragmentSubcomponent.class)
  abstract class EditGymInfoFragmentModule {
    @Binds @IntoMap @FragmentKey(EditGymInfoFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        EditGymInfoFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface ExportRecordFragmentSubcomponent
      extends AndroidInjector<ExportRecordFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ExportRecordFragment> {
    }
  }

  @Module(subcomponents = ExportRecordFragmentSubcomponent.class)
  abstract class ExportRecordFragmentModule {
    @Binds @IntoMap @FragmentKey(ExportRecordFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ExportRecordFragmentSubcomponent.Builder builder);
  }
  //
  //@Subcomponent() public interface ImportExportFragmentSubcomponent
  //    extends AndroidInjector<ImportExportFragment> {
  //  @Subcomponent.Builder public abstract class Builder
  //      extends AndroidInjector.Builder<ImportExportFragment> {
  //  }
  //}
  //
  //@Module(subcomponents = ImportExportFragmentSubcomponent.class)
  //abstract class ImportExportFragmentModule {
  //  @Binds @IntoMap @FragmentKey(ImportExportFragment.class)
  //  abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
  //      ImportExportFragmentSubcomponent.Builder builder);
  //}

  @Subcomponent() public interface ExportSendEmailFragmentSubcomponent
      extends AndroidInjector<ExportSendEmailFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ExportSendEmailFragment> {
    }
  }

  @Module(subcomponents = ExportSendEmailFragmentSubcomponent.class)
  abstract class ExportSendEmailFragmentModule {
    @Binds @IntoMap @FragmentKey(ExportSendEmailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ExportSendEmailFragmentSubcomponent.Builder builder);
  }

  //@Subcomponent() public interface CardImportExportFragmentSubcomponent
  //    extends AndroidInjector<CardImportExportFragment> {
  //  @Subcomponent.Builder public abstract class Builder
  //      extends AndroidInjector.Builder<CardImportExportFragment> {
  //  }
  //}
  //
  //@Module(subcomponents = CardImportExportFragmentSubcomponent.class)
  //abstract class CardImportExportFragmentModule {
  //  @Binds @IntoMap @FragmentKey(CardImportExportFragment.class)
  //  abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
  //      CardImportExportFragmentSubcomponent.Builder builder);
  //}

  @Subcomponent() public interface NotSignFilterFragmentSubcomponent
      extends AndroidInjector<NotSignFilterFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<NotSignFilterFragment> {
    }
  }

  @Module(subcomponents = NotSignFilterFragmentSubcomponent.class)
  abstract class NotSignFilterFragmentModule {
    @Binds @IntoMap @FragmentKey(NotSignFilterFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        NotSignFilterFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface AttendanceNotSignFragmentSubcomponent
      extends AndroidInjector<AttendanceNotSignFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AttendanceNotSignFragment> {
    }
  }

  @Module(subcomponents = AttendanceNotSignFragmentSubcomponent.class)
  abstract class AttendanceNotSignFragmentModule {
    @Binds @IntoMap @FragmentKey(AttendanceNotSignFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AttendanceNotSignFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface AddZqFragmentSubcomponent
      extends AndroidInjector<AddZqFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<AddZqFragment> {
    }
  }

  @Module(subcomponents = AddZqFragmentSubcomponent.class) abstract class AddZqFragmentModule {
    @Binds @IntoMap @FragmentKey(AddZqFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        AddZqFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface EditZqFragmentSubcomponent
      extends AndroidInjector<EditZqFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<EditZqFragment> {
    }
  }

  @Module(subcomponents = EditZqFragmentSubcomponent.class) abstract class EditZqFragmentModule {
    @Binds @IntoMap @FragmentKey(EditZqFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        EditZqFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface SignInCardFragmentSubcomponent
      extends AndroidInjector<SignInCardFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInCardFragment> {
    }
  }

  @Module(subcomponents = SignInCardFragmentSubcomponent.class)
  abstract class SignInCardFragmentModule {
    @Binds @IntoMap @FragmentKey(SignInCardFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInCardFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface SignInChildCodeFragmentSubcomponent
      extends AndroidInjector<SignInChildCodeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInChildCodeFragment> {
    }
  }

  @Module(subcomponents = SignInChildCodeFragmentSubcomponent.class)
  abstract class SignInChildCodeFragmentModule {
    @Binds @IntoMap @FragmentKey(SignInChildCodeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInChildCodeFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface SignInCodeFragmentSubcomponent
      extends AndroidInjector<SignInCodeFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SignInCodeFragment> {
    }
  }

  @Module(subcomponents = SignInCodeFragmentSubcomponent.class)
  abstract class SignInCodeFragmentModule {
    @Binds @IntoMap @FragmentKey(SignInCodeFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        SignInCodeFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface CardProtocolSubcomponent
      extends AndroidInjector<CardProtocolActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CardProtocolActivity> {
    }
  }

  @Module(subcomponents = CardProtocolSubcomponent.class) abstract class CardProtocolModule {
    @Binds @IntoMap @ActivityKey(CardProtocolActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        CardProtocolSubcomponent.Builder builder);
  }

  @Subcomponent() public interface CourseChooseDialogFragmentSubcomponent
      extends AndroidInjector<CourseChooseDialogFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<CourseChooseDialogFragment> {
    }
  }

  @Module(subcomponents = CourseChooseDialogFragmentSubcomponent.class)
  abstract class COurseChooseDialogFragmentModule {
    @Binds @IntoMap @FragmentKey(CourseChooseDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        CourseChooseDialogFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface QRSubcomponent extends AndroidInjector<QRActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<QRActivity> {
    }
  }

  @Module(subcomponents = QRSubcomponent.class) abstract class QRModule {
    @Binds @IntoMap @ActivityKey(QRActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        QRSubcomponent.Builder builder);
  }

  @Subcomponent() public interface ZqAccessFragmentSubcomponent
      extends AndroidInjector<ZqAccessFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ZqAccessFragment> {
    }
  }

  @Module(subcomponents = ZqAccessFragmentSubcomponent.class)
  abstract class ZqAccessFragmentModule {
    @Binds @IntoMap @FragmentKey(ZqAccessFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ZqAccessFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface UpgradeDoneFragmentSubcomponent
      extends AndroidInjector<UpgradeDoneFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<UpgradeDoneFragment> {
    }
  }

  @Module(subcomponents = UpgradeDoneFragmentSubcomponent.class)
  abstract class UpgradeDoneFragmentModule {
    @Binds @IntoMap @FragmentKey(UpgradeDoneFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        UpgradeDoneFragmentSubcomponent.Builder builder);
  }
}
