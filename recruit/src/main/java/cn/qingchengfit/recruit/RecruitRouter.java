package cn.qingchengfit.recruit;

import android.support.v4.app.FragmentManager;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.views.MyPositionsInfoFragment;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.recruit.views.RecruitGymDetailFragment;
import cn.qingchengfit.recruit.views.RecruitPositionDetailFragment;
import cn.qingchengfit.recruit.views.SeekPositionHomeFragment;
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

    @Inject RecruitActivity activity;

    @Inject public RecruitRouter() {
    }

    @Override public FragmentManager getFragmentManager() {
        return activity.getSupportFragmentManager();
    }

    @Override public int getFragId() {
        return R.id.frag_recruit;
    }

    public void home() {
        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
            .replace(getFragId(), new SeekPositionHomeFragment())
            .commit();
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
}
