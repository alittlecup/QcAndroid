package com.qingchengfit.fitcoach.Utils;

import android.text.TextUtils;
import java.lang.reflect.Field;
import java.util.Comparator;
import net.sourceforge.pinyin4j.PinyinHelper;

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
 * Created by Paper on 15/11/25 2015.
 */
public class PinyinComparator implements Comparator<Object> {
    public String field;

    public PinyinComparator(String field) {
        this.field = field;
    }

    public static boolean isChinese(char a) {
        int v = (int) a;
        return (v >= 19968 && v <= 171941);
    }

    public int compare(Object oo1, Object oo2) {
        String o1 = "";
        String o2 = "";
        try {
            Field f = oo1.getClass().getDeclaredField(field);
            f.setAccessible(true);
            o1 = String.valueOf(f.get(oo1));
            Field f2 = oo2.getClass().getDeclaredField(field);
            f2.setAccessible(true);
            o2 = String.valueOf(f2.get(oo1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(o1)) {
            o1 = "~";
        }
        if (TextUtils.isEmpty(o2)) {
            o2 = "~";
        }
        char c1 = o1.charAt(0);
        char c2 = o2.charAt(0);
        if (isChinese(c1) && isChinese(c2)) {
            return concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(c1)).compareTo(
                concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(c2)));
        } else {
            return o1.compareTo(o2);
        }
    }

    private String concatPinyinStringArray(String[] pinyinArray) {
        StringBuffer pinyinSbf = new StringBuffer();
        if ((pinyinArray != null) && (pinyinArray.length > 0)) {
            for (int i = 0; i < pinyinArray.length; i++) {
                pinyinSbf.append(pinyinArray[i]);
            }
        }
        return pinyinSbf.toString();
    }
}

