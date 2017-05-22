package cn.qingchengfit.repository;

import cn.qingchengfit.db.QcDbHelper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.LogUtil;
import com.qingcheng.model.base.CoachServiceModel;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

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

    @Inject QcDbHelper helper;

    @Inject public RepoCoachServiceImpl() {
    }

    /**
     * 本地存储
     */
    @Override public void createService(CoachService coachService) {
        helper.getBriteDatabase().insert(CoachService.TABLE_NAME, CoachService.FACTORY.marshal(coachService).asContentValues());
    }

    @Override public void createServices(List<CoachService> coachServices) {
        BriteDatabase.Transaction trans = helper.getBriteDatabase().newTransaction();
        try {
            deleteAllServices();
            coachServices.forEach(this::createService);
            trans.markSuccessful();
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        } finally {
            trans.end();
        }
    }

    @Override public Observable<CoachService> readServiceByIdModel(String id, String model) {
        return helper.getBriteDatabase()
            .createQuery(CoachServiceModel.TABLE_NAME, CoachService.FACTORY.getByIdModel(id, model).statement)
            .mapToOne(cursor -> CoachService.FACTORY.getByIdModelMapper().map(cursor))
            .asObservable();
    }

    @Override public Observable<List<CoachService>> readAllServices() {
        return helper.getBriteDatabase()
            .createQuery(CoachServiceModel.TABLE_NAME, CoachService.FACTORY.getAllCoachService().statement)
            .mapToList(cursor -> CoachService.FACTORY.getAllCoachServiceMapper().map(cursor))
            .asObservable();
    }

    @Override public void updateService(CoachService coachService) {
        LogUtil.e("updateService undo");
    }

    @Override public void updateServices(List<CoachService> coachServices) {
        LogUtil.e("updateServices undo");
    }

    @Override public void deleteServiceByIdModel(String id, String model) {
    }

    @Override public void deleteAllServices() {
        helper.getBriteDatabase().execute(CoachService.DELETEALL);
    }
}
