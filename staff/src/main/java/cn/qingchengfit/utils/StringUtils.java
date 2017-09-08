package cn.qingchengfit.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.model.CoachBean;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.custom.CircleView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.pinyin4j.PinyinHelper;

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
 * Created by Paper on 16/5/5 2016.
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return (str == null || str.equalsIgnoreCase(""));
    }

    public static boolean isNumZero(String str) {
        try {
            if (str == null || str.equalsIgnoreCase("") || str.equalsIgnoreCase("null")) return true;

            float x = Float.parseFloat(str);
            return x == 0;
        } catch (Exception e) {
            return false;
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

    public static String List2StrWithChineseSplit(List<String> stringList) {
        String ret = "";
        if (stringList == null) return null;
        for (int i = 0; i < stringList.size(); i++) {
            if (i < stringList.size() - 1) {
                ret = TextUtils.concat(ret, stringList.get(i), "，").toString();
            } else {
                ret = TextUtils.concat(ret, stringList.get(i)).toString();
            }
        }
        return ret;
    }

    public static List<String> Str2List(String s) {
        if (s == null) return null;
        List<String> ret = new ArrayList<>();
        if (s.contains(",")) {
            String[] rets = s.split(",");
            for (int i = 0; i < rets.length; i++) {
                ret.add(rets[i]);
            }
        } else {
            ret.add(s);
        }
        return ret;
    }

    public static String getNumString(String string) {
        try {
            if (TextUtils.isEmpty(string)) return "";
            if (string.contains(".")) {
                float f = Float.parseFloat(string);
                if (f / 10000f >= 1) {
                    return String.format(Locale.CHINA, "%.1fW", f / 10000);
                } else {
                    return String.format(Locale.CHINA, "%.2f", f);
                }
            } else {
                Integer i = Integer.parseInt(string);
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

    public static String getFloatDot1(float fo) {
        return String.format(Locale.CHINA, "%.1f", fo);
    }

    public static String getNumString(float fo) {
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

    public static String getUnit(Context context, int type) {
        switch (type) {
            case Configs.CATEGORY_VALUE:
                return context.getString(R.string.unit_yuan);
            case Configs.CATEGORY_TIMES:
                return context.getString(R.string.unit_time);
            case Configs.CATEGORY_DATE:
                return context.getString(R.string.unit_day);
            default:
                return "";
        }
    }

    public static String getBalance(Context context, float balance, int type) {
        switch (type) {
            case Configs.CATEGORY_VALUE:
                return String.format(Locale.CHINA, "%.2f", balance) + context.getString(R.string.unit_yuan);
            case Configs.CATEGORY_TIMES:
                try {
                    return String.format(Locale.CHINA, "%.0f", balance) + context.getString(R.string.unit_time);
                } catch (Exception e) {
                    return balance + context.getString(R.string.unit_time);
                }
            case Configs.CATEGORY_DATE:
                try {
                    return String.format(Locale.CHINA, "%.0f", balance) + context.getString(R.string.unit_day);
                } catch (Exception e) {
                    return balance + context.getString(R.string.unit_day);
                }
            default:
                return "";
        }
    }

    public static String getFloatDot2(float f) {
        if (((int) (f * 100)) % 100 != 0) return String.format(Locale.CHINA, "%.2f", f);
        if (f == 0) return "0";

        return String.format(Locale.CHINA, "%.0f", f);
    }

    public static String formatPrice(Context context, float price, int type) {
        switch (type) {
            case Configs.CATEGORY_VALUE:
                return String.format(Locale.CHINA, "%.2f", price);
            case Configs.CATEGORY_TIMES:
                return String.format(Locale.CHINA, "%.0f", price);
            case Configs.CATEGORY_DATE:
                return String.format(Locale.CHINA, "%.0f", price);
            default:
                return "";
        }
    }

    /**
     * 数字数组,包含头尾
     */
    public static List<String> getNums(int from, int to) {
        List<String> ret = new ArrayList<>();
        for (int i = from; i < to + 1; i++) {
            ret.add(Integer.toString(i));
        }
        return ret;
    }

    /**
     * 变更销售-会员状态
     *
     * @param view view
     * @param status status
     * 0 # 新注册
     * 1 # 跟进中
     * 2 # 会员
     * 3 # 非会员
     */
    public static void studentStatus(TextView view, int status) {
        String statuStr = "";
        Drawable drawable = null;
        switch (Integer.valueOf(status)) {
            case 0:
                statuStr = "新注册";
                drawable = new CircleView(R.color.qc_student_status_0);
                break;
            case 1:
                statuStr = "已接洽";
                drawable = new CircleView(R.color.qc_student_status_1);
                break;
            case 2:
                statuStr = "会员";
                drawable = new CircleView(R.color.qc_student_status_2);
                break;
            default:
                statuStr = "未知";
                drawable = new CircleView(R.color.qc_student_status_0);
                break;
        }
        view.setText(statuStr);
        drawable.setBounds(0, 0, 26, 26);
        view.setCompoundDrawablePadding(10);
        view.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 变更销售-会员状态
     *
     * @param view view
     * @param status status
     * 0 # 新注册
     * 1 # 跟进中
     * 2 # 会员
     * 3 # 非会员
     */
    public static void studentStatusWithArrow(TextView view, int status) {
        Drawable drawableArrow = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_arrow_right);
        drawableArrow.setBounds(0, 0, drawableArrow.getMinimumWidth(), drawableArrow.getMinimumHeight());
        String statuStr = "";
        Drawable drawable = null;
        switch (Integer.valueOf(status)) {
            case 0:
                statuStr = "新注册";
                drawable = new CircleView(R.color.qc_student_status_0);
                break;
            case 1:
                statuStr = "已接洽";
                drawable = new CircleView(R.color.qc_student_status_1);
                break;
            case 2:
                statuStr = "会员";
                drawable = new CircleView(R.color.qc_student_status_2);
                break;
            default:
                statuStr = "未知";
                drawable = new CircleView(R.color.qc_student_status_0);
                break;
        }
        view.setText(statuStr);
        drawable.setBounds(0, 0, 26, 26);
        view.setCompoundDrawablePadding(10);
        view.setCompoundDrawables(drawable, null, drawableArrow, null);
    }

    public static void studentScorePosition(TextView view, int position) {
        Drawable drawable = null;
        switch (position) {
            case 0: // 第一名
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.vector_score_position_bg_1);
                view.setTextColor(Color.parseColor("#ffffff"));
                view.setBackgroundResource(R.drawable.vector_score_position_bg_1);
                break;
            case 1: // 第二名
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.vector_score_position_bg_2);
                view.setTextColor(Color.parseColor("#ffffff"));
                view.setBackgroundResource(R.drawable.vector_score_position_bg_2);
                break;
            case 2: // 第三名
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.vector_score_position_bg_3);
                view.setTextColor(Color.parseColor("#ffffff"));
                view.setBackgroundResource(R.drawable.vector_score_position_bg_3);
                break;
            default:
                view.setTextColor(Color.parseColor("#9d9d9d"));
                view.setBackgroundResource(R.drawable.vector_score_position_bg_4);
                break;
        }
        view.setText(String.valueOf(position + 1));
    }

    public static String studentScoreHistotyTitle(String type) {
        String title = "";
        if ("teamarrange".equals(type)) {
            title = "团课预约";
        } else if ("teamarrange_cancel".equals(type)) {
            title = "团课取消";
        } else if ("priarrange".equals(type)) {
            title = "私教预约";
        } else if ("priarrange_cancel".equals(type)) {
            title = "私教取消";
        } else if ("checkin".equals(type)) {
            title = "入场签到";
        } else if ("checkin_cancel".equals(type)) {
            title = "撤销签到";
        } else if ("buycard".equals(type)) {
            title = "新购会员卡";
        } else if ("chargecard".equals(type)) {
            title = "会员卡续费";
        } else if ("add".equals(type)) {
            title = "添加";
        } else if ("dec".equals(type)) {
            title = "扣除";
        }
        return title;
    }

    public static String sellersNames(List<Staff> list) {
        String ret = "";
        for (int i = 0; i < list.size(); i++) {
            if ((i < list.size() - 1) && !TextUtils.isEmpty(list.get(i).username)) {
                ret = TextUtils.concat(ret, list.get(i).username, "，").toString();
            } else if (!TextUtils.isEmpty(list.get(i).username)) {
                ret = TextUtils.concat(ret, list.get(i).username).toString();
            }
        }
        return ret;
    }

    public static String coachesNames(List<CoachBean> list) {
        if (list == null) {
            return "";
        }
        String ret = "";
        for (int i = 0; i < list.size(); i++) {
            if ((i < list.size() - 1) && !TextUtils.isEmpty(list.get(i).username)) {
                ret = TextUtils.concat(ret, list.get(i).username, "，").toString();
            } else if (!TextUtils.isEmpty(list.get(i).username)) {
                ret = TextUtils.concat(ret, list.get(i).username).toString();
            }
        }
        return ret;
    }

    public static List<String> coachIds(List<CoachBean> list) {
        List<String> idList = new ArrayList<>();
        if (list == null) {
            return idList;
        }
        for (int i = 0; i < list.size(); i++) {
            idList.add(list.get(i).id);
        }
        return idList;
    }

    public static boolean isEmail(String str){
        boolean isEmail = true;
        String pattern = "(.*)@(.*)(\\.)(.*)";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(str);
        if (!matcher.find()){
            isEmail = false;
        }
        return isEmail;
    }

    /**
     * 是否是汉字
     */
    public static boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    /**
     * 第一个字的拼音，除了汉字和字母，其他都返回#
     */
    public static String getFirstPinYin(String str) {
        String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char first = str.toCharArray()[0];
        if (LETTERS.contains(str.substring(0, 1).toUpperCase(Locale.getDefault()))) {
            return str.toUpperCase(Locale.getDefault());
        } else if (isChineseChar(first + "")) {
            return PinyinHelper.toHanyuPinyinStringArray(first)[0].toUpperCase(Locale.getDefault());
        } else {
            return "#";
        }
    }

    /**
     * 第一个字的拼音，如果是字母返回字母，如果是数字返回数字，如果是汉字返回拼音
     */
    public static String getFirstPinYinOrNumber(String str) {
        String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String NUMBERS = "0123456789";
        char first = str.toCharArray()[0];
        if (LETTERS.contains(str.substring(0, 1).toUpperCase(Locale.getDefault()))) {
            return str.toUpperCase(Locale.getDefault());
        } else if (NUMBERS.contains(str.substring(0, 1))) {
            return str.substring(0, 1);
        } else if (isChineseChar(first + "")) {
            return PinyinHelper.toHanyuPinyinStringArray(first)[0].toUpperCase(Locale.getDefault());
        } else {
            return "#";
        }
    }

    public static String getStringHtml(String text, String color) {
        return TextUtils.concat("<font color=\"" + color + "\">" + text + "</font>").toString();
    }

    public static String array2str(int[] from) {
        String ret = "";
        for (int i = 0; i < from.length; i++) {
            if (i < from.length - 1) {
                ret = ret.concat(Integer.toString(from[i])).concat(",");
            } else {
                ret = ret.concat(Integer.toString(from[i]));
            }
        }
        return ret;
    }
}
