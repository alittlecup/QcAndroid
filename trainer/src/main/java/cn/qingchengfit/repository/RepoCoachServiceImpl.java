package cn.qingchengfit.repository;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.saasbase.permission.QcDbManager;
import cn.qingchengfit.utils.LogUtil;
import io.reactivex.Flowable;
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
 * Created by Paper on 2017/5/15.
 */

public class RepoCoachServiceImpl implements RepoCoachService {

    @Inject QcDbManager db;

    @Inject public RepoCoachServiceImpl() {
    }

    /**
     * 本地存储
     */
    @Override public void createService(CoachService coachService) {
        List<CoachService> r = new ArrayList<>();
        r.add(coachService);
        db.writeGyms(r);
    }

    @Override public void createServices(List<CoachService> coachServices) {
        db.writeGyms(coachServices);
    }

    @Override public Flowable<CoachService> readServiceByIdModel(String id, String model) {
        return db.getGymByModel(id,model);
    }

    @Override public Flowable<List<CoachService>> readAllServices() {
        return db.getAllCoachService();
    }

    @Override public void updateService(CoachService coachService) {
        LogUtil.e("updateService undo");
    }

    @Override public void updateServices(List<CoachService> coachServices) {
        LogUtil.e("updateServices undo");
    }

    @Override public void deleteServiceByIdModel(String id, String model) {
        //db.
    }

    @Override public void deleteAllServices() {
    }
}
