package com.qingchengfit.fitcoach.fragment.statement.presenter;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.QcResponseData;
import com.anbillon.qcmvplib.PView;
import com.qingchengfit.fitcoach.bean.StudentBean;
import com.qingchengfit.fitcoach.di.BasePresenter;
import com.qingchengfit.fitcoach.fragment.statement.SalerChooseDialogView;
import com.qingchengfit.fitcoach.fragment.statement.StatementUsecase;
import com.qingchengfit.fitcoach.fragment.statement.model.Sellers;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 16/7/5 2016.
 */
public class SalerChoosePresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StatementUsecase usecase;
    private SalerChooseDialogView view;

    @Inject public SalerChoosePresenter(StatementUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        view = (SalerChooseDialogView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void querySaler() {
        usecase.querySalers(gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model(), new Action1<QcResponseData<Sellers>>() {
            @Override public void call(QcResponseData<Sellers> qcResponseSalers) {
                if (ResponseConstant.checkSuccess(qcResponseSalers)) {
                    if (qcResponseSalers.data.users != null) {
                        List<StudentBean> ret = new ArrayList<StudentBean>();
                        for (int i = 0; i < qcResponseSalers.data.users.size(); i++) {
                            StudentBean studentBean = new StudentBean();
                            studentBean.headerPic = qcResponseSalers.data.users.get(i).avatar;
                            studentBean.id = qcResponseSalers.data.users.get(i).id;
                            studentBean.username = qcResponseSalers.data.users.get(i).username;
                            ret.add(studentBean);
                        }
                        view.onList(ret);
                    }
                }
            }
        });
    }
}
