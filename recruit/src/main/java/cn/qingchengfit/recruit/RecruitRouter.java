package cn.qingchengfit.recruit;

import android.support.v4.app.FragmentManager;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.model.Organization;
import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.network.body.ResumeBody;
import cn.qingchengfit.recruit.views.MyPositionsInfoFragment;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.recruit.views.RecruitGymDetailFragment;
import cn.qingchengfit.recruit.views.RecruitPositionDetailFragment;
import cn.qingchengfit.recruit.views.ResumeMarketHomeFragment;
import cn.qingchengfit.recruit.views.SeekPositionHomeFragment;
import cn.qingchengfit.recruit.views.resume.AddEduExpFragment;
import cn.qingchengfit.recruit.views.resume.AddEduExpFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.RecordEditFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeBaseInfoFragment;
import cn.qingchengfit.recruit.views.resume.ResumeBaseInfoFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeCertificateListFragment;
import cn.qingchengfit.recruit.views.resume.ResumeEditDescFragment;
import cn.qingchengfit.recruit.views.resume.ResumeEditDescFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeEduExpListFragment;
import cn.qingchengfit.recruit.views.resume.ResumeHomeFragment;
import cn.qingchengfit.recruit.views.resume.ResumeIntentJobsFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeIntentsCitiesFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeIntentsFragment;
import cn.qingchengfit.recruit.views.resume.ResumeIntentsFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeShowImgsFragment;
import cn.qingchengfit.recruit.views.resume.ResumeShowImgsFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeWorkExpListFragment;
import cn.qingchengfit.recruit.views.resume.WorkExpSyncDetailFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.WorkExpeEditFragmentBuilder;
import cn.qingchengfit.router.InnerRouter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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

public class RecruitRouter extends InnerRouter {

    public static final int RESULT_POSISTION_NAME = 2001;
    public static final int RESULT_POSISTION_DESC = 2003;
    public static final int RESULT_POSISTION_DEMANDS = 2004;
    public static final int RESULT_POSISTION_REQUIRE = 2005;



    @Inject RecruitActivity activity;

    @Inject public RecruitRouter() {
    }

    @Override public FragmentManager getFragmentManager() {
        return activity.getSupportFragmentManager();
    }

    @Override public int getFragId() {
        return R.id.frag_recruit;
    }



    /**
     * 求职版主页
     */
    public void home() {
        init(new SeekPositionHomeFragment());
    }

    public void goJobDetail(Job job) {
        doAction(RecruitPositionDetailFragment.newInstance(job));
    }

    public void mySent() {
        doAction(MyPositionsInfoFragment.newMySent());
    }

    public void myStarred() {
        doAction(MyPositionsInfoFragment.newMyStared());
    }

    public void myInvited() {
        doAction(MyPositionsInfoFragment.newMyInvited());
    }

    public void toGymDetial(Gym service) {
        doAction(RecruitGymDetailFragment.newInstance(service));
    }

    public void myResume() {

    }

    public void myJobFair() {

    }

    public void toJobFairDetail() {
    }

    /**
     * 人才市场 人才浏览 或者叫 招聘版主页
     */
    public void resumeMarketHome(){
        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out, R.anim.card_flip_left_in, R.anim.card_flip_left_out)
            .replace(getFragId(),new ResumeMarketHomeFragment())
            .addToBackStack(null)
            .commit();
    }

    public void resumeHome() {
        init(new ResumeHomeFragment());
    }

    public void routeToEdit(int pos) {
        switch (pos) {
            case 0:
                add(new ResumeBaseInfoFragment());
                break;
            case 1:
                add(new ResumeIntentsFragment());
                break;
            case 2:
                add(new ResumeShowImgsFragment());
                break;
            case 3:
                add(new ResumeWorkExpListFragment());
                break;
            case 4:
                add(new ResumeCertificateListFragment());
                break;
            case 5:
                add(new ResumeEduExpListFragment());
                break;
            case 6:
                add(new ResumeEditDescFragment());
                break;
        }
    }

    public void editResumeInfo(ResumeBody body) {
        add(new ResumeBaseInfoFragmentBuilder(body).build());
    }

    public void editImages(List<String> imgs) {
        add(new ResumeShowImgsFragmentBuilder(new ArrayList<String>(imgs)).build());
    }

    public void editEduExp(Education education) {
        add(new AddEduExpFragmentBuilder().education(education).build());
    }

    public void addEduExp() {
        add(new AddEduExpFragment());
    }

    public void addWorkExp(Gym gym) {
        add(new WorkExpeEditFragmentBuilder(gym).build());
    }

    public void editWorkExp(WorkExp workExp) {
        if (workExp.is_authenticated) {
            add(new WorkExpSyncDetailFragmentBuilder(workExp).build());
        } else {
            add(new WorkExpeEditFragmentBuilder(workExp.gym).workExp(workExp).build());
        }
    }

    public void listCertifaciton() {
        add(new ResumeCertificateListFragment());
    }

    public void listEdulist() {
        add(new ResumeEduExpListFragment());
    }

    public void listWorkList() {
        add(new ResumeWorkExpListFragment());
    }

    public void addCertification(int type, Organization organization) {
        add(new RecordEditFragmentBuilder(organization, type).build());
    }

    public void editCertification(int type, Organization organization, Certificate certificate) {
        add(new RecordEditFragmentBuilder(organization, type).certificatesEntity(certificate).build());
    }

    public void toIntentCities(List<CityBean> listcity) {
        add(new ResumeIntentsCitiesFragmentBuilder(new ArrayList<CityBean>(listcity)).build());
    }

    public void toIntentPosition(List<String> jobs) {
        add(new ResumeIntentJobsFragmentBuilder(new ArrayList<String>(jobs)).build());
    }

    public void toExpect(ResumeHome resumeHome) {
        add(new ResumeIntentsFragmentBuilder().build());
    }

    public void brief(String content) {
        add(new ResumeEditDescFragmentBuilder(content).build());
    }
}
