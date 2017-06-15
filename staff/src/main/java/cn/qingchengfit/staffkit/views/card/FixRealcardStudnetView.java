package cn.qingchengfit.staffkit.views.card;

import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.mvpbase.PView;
import java.util.List;

/**
 * Created by Paper on 16/6/16.
 * <p>
 * ((      /|_/|
 * \\.._.'  , ,\
 * /\ | '.__ v /
 * (_ .   /   "
 * ) _)._  _ /
 * '.\ \|( / ( mrf
 * '' ''\\ \\
 */

public interface FixRealcardStudnetView extends PView {

    void onStudentList(List<StudentBean> list);

    void onFilterStudentList(List<StudentBean> list);

    void onFaied();

    void onSuccess();

    void onStopFresh();
}
