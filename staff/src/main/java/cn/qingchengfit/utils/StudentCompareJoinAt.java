package cn.qingchengfit.utils;

import cn.qingchengfit.model.base.StudentBean;
import java.util.Comparator;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/10/12 2015.
 */
public class StudentCompareJoinAt implements Comparator<StudentBean> {

    @Override public int compare(StudentBean lhs, StudentBean rhs) {
        return rhs.joined_at.compareTo(lhs.joined_at);
    }
}
