package cn.qingchengfit.utils;

import cn.qingchengfit.model.base.QcStudentBean;
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
public class QcStudentComparator implements Comparator<QcStudentBean> {
    @Override public int compare(QcStudentBean lhs, QcStudentBean rhs) {
        //        if (TextUtils.isEmpty(rhs.getHead()))
        //            rhs.setHead("~");
        return lhs.getHead().compareTo(rhs.getHead());
    }

    //    }
    //
    //    public int compare(QcStudentBean course1, QcStudentBean course2) {
    //        String o1 = course1.getUsername();
    //        String o2 = course2.getUsername();
    //        if (TextUtils.isEmpty(o1)) {
    //            o1 = "~";
    //        }
    //        if (TextUtils.isEmpty(o2)) {
    //            o2 = "~";
    //        }
    //        char c1 = o1.charAt(0);
    //        char c2 = o2.charAt(0);
    //        if (isChinese(c1) && isChinese(c2))
    //            return concatPinyinStringArray(
    //                    PinyinHelper.toHanyuPinyinStringArray(c1)).compareTo(
    //                    concatPinyinStringArray(PinyinHelper
    //                            .toHanyuPinyinStringArray(c2)));
    //        else
    //            return o1.compareTo(o2);
    //
    //    }
    //
    //    private String concatPinyinStringArray(String[] pinyinArray) {
    //        StringBuffer pinyinSbf = new StringBuffer();
    //        if ((pinyinArray != null) && (pinyinArray.length > 0)) {
    //            for (int i = 0; i < pinyinArray.length; i++) {
    //                pinyinSbf.append(pinyinArray[i]);
    //            }
    //        }
    //        return pinyinSbf.toString();
    //    }
}

