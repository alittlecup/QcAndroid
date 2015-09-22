package com.qingchengfit.fitcoach.Utils;

import android.text.TextUtils;

import com.qingchengfit.fitcoach.bean.BriefInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

    /**
     * 用pull将规定格式xml解析成list
     *
     * @param s
     * @return
     * @throws Exception
     */

    public static ArrayList<BriefInfo> fromHTML(String s) throws Exception {

        InputStream xml = new ByteArrayInputStream(s.getBytes());
        //XmlPullParserFactory pullPaser = XmlPullParserFactory.newInstance();
        ArrayList<BriefInfo> persons = new ArrayList<>();
        BriefInfo breif = null;
        boolean isImg = false;
        // 创建一个xml解析的工厂
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // 获得xml解析类的引用
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(xml, "UTF-8");
        // 获得事件的类型
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    breif = new BriefInfo();
                    if ("p".equals(parser.getName())) {
                        isImg = false;
                    } else if ("img".equals(parser.getName())) {
                        isImg = true;
                    }

                    break;
                case XmlPullParser.TEXT:
                    if (isImg) {
                        String img = parser.getText();
                        String img_out = img.substring(6, img.length() - 2);
                        breif.setImg(img_out);
                    } else {
                        String content = parser.getText();
                        breif.setText(content);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    persons.add(breif);

                    break;
            }
            eventType = parser.next();
        }
        return persons;
    }

    /**
     * 将数据转化成html字符串
     *
     * @param list
     * @return
     */

    public static String toHTML(List<BriefInfo> list) {
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
        return sb.toString();
    }


}
