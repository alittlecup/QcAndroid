package com.qingchengfit.fitcoach.Utils;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.bean.StudentBean;

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
public class StudentCompare implements Comparator<StudentBean> {


    @Override
    public int compare(StudentBean lhs, StudentBean rhs) {
        LogUtil.e("l:" + lhs.head.charAt(0) + "  " + rhs.head.charAt(0));
//        if (lhs.head.charAt(0) <= rhs.head.charAt(0)) {
//            return -1;
//        } else return 1;
        return lhs.head.compareTo(rhs.head);
    }
}
