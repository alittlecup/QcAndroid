package com.qingchengfit.fitcoach.Utils;

import com.qingchengfit.fitcoach.http.bean.QcPrivateGymReponse;

import java.util.Comparator;

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
 * Created by Paper on 15/10/12 2015.
 */
public class GymCompare implements Comparator<QcPrivateGymReponse.OpenTime> {


    @Override
    public int compare(QcPrivateGymReponse.OpenTime lhs, QcPrivateGymReponse.OpenTime rhs) {
        if (lhs.day >= rhs.day) {
            return 1;
        } else return -1;
    }
}
