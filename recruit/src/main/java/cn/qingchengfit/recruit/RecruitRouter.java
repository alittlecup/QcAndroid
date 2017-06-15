package cn.qingchengfit.recruit;

import android.support.v4.app.FragmentManager;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.views.MyPositionsInfoFragment;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.recruit.views.RecruitGymDetailFragment;
import cn.qingchengfit.recruit.views.RecruitPositionDetailFragment;
import cn.qingchengfit.recruit.views.ResumeMarketHomeFragment;
import cn.qingchengfit.recruit.views.SeekPositionHomeFragment;
import cn.qingchengfit.recruit.views.resume.ResumeHomeFragment;
import cn.qingchengfit.router.InnerRouter;
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

}
