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
 * Created by Paper on 15/11/25 2015.
 */
public class PinyinComparator implements Comparator<StudentBean> {

    public int compare(StudentBean course1, StudentBean course2) {
        String o1FirstLetter, o2FirstLetter = null;
        try {
            o1FirstLetter = StringUtils.getFirstPinYin(course1.username);
            o2FirstLetter = StringUtils.getFirstPinYin(course1.username);
        } catch (Exception e) {
            return -1;
        }
        if (!o1FirstLetter.equals("#") && o2FirstLetter.equals("#")) {
            return 1;
        } else if (o1FirstLetter.equals("#") && !o2FirstLetter.equals("#")) {
            return -1;
        } else if (o1FirstLetter.equals("#") && o2FirstLetter.equals("#")) {
            return 0;
        } else {
            return o1FirstLetter.compareTo(o2FirstLetter);
        }
    }
}

