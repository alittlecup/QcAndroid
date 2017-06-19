package cn.qingchengfit.utils;

import android.text.TextUtils;
import cn.qingchengfit.model.common.BriefInfo;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/6/12.
 */

public class CmStringUtils {

    public static String getMoneyStr(float fo) {
        try {
            boolean hasX = ((int) (fo * 100) % 100) != 0;
            if (hasX) {
                float f = fo;
                if (f / 10000f >= 1) {
                    return String.format(Locale.CHINA, "%.1fW", f / 10000);
                } else {
                    return String.format(Locale.CHINA, "%.2f", f);
                }
            } else {
                Integer i = (int) fo;
                if (i / 10000 > 0) {
                    return String.format(Locale.CHINA, "%.1fW", ((float) i / 10000f));
                } else {
                    return String.format(Locale.CHINA, "%d", i);
                }
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String List2Str(List<String> stringList) {
        String ret = "";
        if (stringList == null) return null;
        for (int i = 0; i < stringList.size(); i++) {
            if (i < stringList.size() - 1) {
                ret = TextUtils.concat(ret, stringList.get(i), ",").toString();
            } else {
                ret = TextUtils.concat(ret, stringList.get(i)).toString();
            }
        }
        return ret;
    }

    public static String getStringHtml(String text, String color) {
        return TextUtils.concat("<font color=\"" + color + "\">" + text + "</font>").toString();
    }

    public static String getFloatDot1(float fo) {
        return String.format(Locale.CHINA, "%.1f", fo);
    }

    /**
     * 用pull将规定格式xml解析成list
     *
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
                        if (parser.getAttributeCount() > 0) breif.setImg(parser.getAttributeValue(0));
                    }

                    break;
                case XmlPullParser.TEXT:
                    if (isImg) {
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
     */

    public static String toHTML(List<BriefInfo> list) {
        StringBuffer sb = new StringBuffer();
        for (BriefInfo info : list) {
            if (TextUtils.isEmpty(info.getImg())) {
                sb.append("<p>");
                sb.append(info.getText());
                sb.append("</p>");
            } else {
                sb.append("<img src=\"");
                sb.append(info.getImg());
                sb.append("\" />");
            }
        }
        return sb.toString();
    }

    public static String getMobileHtml(String content) {
        return "<html>\n"
            + "<head>\n"
            + "\t<title>容器</title>\n"
            + "\t<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,user-scalable=no\">\n"
            + "\t<style type=\"text/css\">\n"
            + "\t\tbody{overflow-x:hidden;overflow-y:auto;}\n"
            + "\t\t.richTxtCtn{margin:0;padding:0;}\n"
            + "\t\t.richTxtCtn *{max-width:100% !important;}\n"
            + "\t</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "\t<div class=\"richTxtCtn\">"
            + content
            + "</div></body></html>";
    }
}
