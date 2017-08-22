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
public class StudentCompare implements Comparator<StudentBean> {

    @Override public int compare(StudentBean lhs, StudentBean rhs) {
        //
        //        ItemComparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        //        if (!TextUtils.isEmpty(lhs.getUsername()) && !TextUtils.isEmpty(rhs.getUsername())){
        //            String f = lhs.getUsername().trim().substring(0,1);
        //            String s = rhs.getUsername().trim().substring(0,1);
        //            if (ChineseCharToEn.containsChinese(f) && ChineseCharToEn.containsChinese(s)){
        //               return -com.compare(f,s);
        //            }else if(ChineseCharToEn.containsChinese(f) && !ChineseCharToEn.containsChinese(s)){
        //
        //                return lhs.head.compareTo(rhs.head);
        //            }else if(!ChineseCharToEn.containsChinese(f) && ChineseCharToEn.containsChinese(s)){
        //                return lhs.head.compareTo(rhs.head);
        //            }else if(!ChineseCharToEn.containsChinese(f) && !ChineseCharToEn.containsChinese(s)){
        //                return lhs.head.compareTo(rhs.head);
        //            }else {
        //                return lhs.head.compareTo(rhs.head);
        //            }
        //        }else
        return lhs.head.compareTo(rhs.head);
    }
}
