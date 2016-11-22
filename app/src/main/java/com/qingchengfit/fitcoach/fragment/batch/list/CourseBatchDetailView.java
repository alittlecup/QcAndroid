package com.qingchengfit.fitcoach.fragment.batch.list;

import com.anbillon.qcmvplib.CView;
import com.anbillon.qcmvplib.PView;
import com.qingchengfit.fitcoach.bean.base.Course;
import com.qingchengfit.fitcoach.http.bean.QcResponseGroupDetail;
import com.qingchengfit.fitcoach.http.bean.QcResponsePrivateDetail;

import java.util.List;


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
 * Created by Paper on 16/3/29 2016.
 */
public interface CourseBatchDetailView extends CView {
    void onGoup(Course course, List<QcResponseGroupDetail.GroupBatch> batch);

    void onPrivate(QcResponsePrivateDetail.PrivateCoach coach, List<QcResponsePrivateDetail.PrivateBatch> batch);
}
