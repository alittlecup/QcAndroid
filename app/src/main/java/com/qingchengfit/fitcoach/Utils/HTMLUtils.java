package com.qingchengfit.fitcoach.Utils;

import android.text.TextUtils;

import com.qingchengfit.fitcoach.bean.BriefInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
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
 * Created by Paper on 15/9/21 2015.
 */
public class HTMLUtils {

    public String toHTML(List<BriefInfo> list) {
        StringBuffer sb = new StringBuffer();
        for (BriefInfo info : list) {
            if (TextUtils.isEmpty(info.getImg())) {
                sb.append("<p>");
                sb.append(info.getText());
                sb.append("</p>");
            } else {
                sb.append("<img> src=\"");
                sb.append(info.getImg());
                sb.append("\" </img>");
            }
        }
        return "";
    }

    public List<BriefInfo> fromHTML(String s) {
        List<BriefInfo> dats = new ArrayList<>();
        try {
            XmlPullParser xpr = XmlPullParserFactory.newInstance().newPullParser();

        } catch (XmlPullParserException e) {
            //e.printStackTrace();
        }
        return dats;

    }


}
