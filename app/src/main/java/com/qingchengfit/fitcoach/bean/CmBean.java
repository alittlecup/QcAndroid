package com.qingchengfit.fitcoach.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.qingchengfit.widgets.utils.DateUtils;

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
 * Created by Paper on 16/5/5 2016.
 */
public class CmBean implements Parcelable {
    public ArrayList<Integer> week = new ArrayList<>();
    public Date dateStart;
    public Date dateEnd;

    public CmBean() {
    }

    public static List<CmBean> getBeansFromTimeRep(HashMap<String, ArrayList<Integer>> map) {
        List<CmBean> ret = new ArrayList<>();
        try {
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {

                String key = (String) iterator.next();
                String[] times = key.split("-");
                CmBean bean = new CmBean(map.get(key), DateUtils.getDateFromHHmm(times[0]), DateUtils.getDateFromHHmm(times[1]));
                ret.add(bean);
            }
        } catch (Exception e) {

        }
        return ret;

    }

//    public static ArrayList<Time_repeat> geTimeRepFromBean(List<CmBean> cmBeens) {
//        ArrayList<Time_repeat> ret = new ArrayList<>();
//        try {
//            for (CmBean cmBean : cmBeens) {
//                for (Integer week : cmBean.week) {
//                    Time_repeat time_repeat = new Time_repeat();
//                    time_repeat.setStart(DateUtils.getTimeHHMM(cmBean.dateStart));
//                    time_repeat.setEnd(DateUtils.getTimeHHMM(cmBean.dateEnd));
//                    time_repeat.setWeekday(week);
//                    ret.add(time_repeat);
//                }
//            }
//        } catch (Exception e) {
//
//        }
//        return ret;
//    }

//    public static boolean CheckCmBean(List<CmBean> older, CmBean bean) {
//        HashMap<Integer, List<Pair<Long, Long>>> dates = new HashMap<>();
//        for (CmBean b : older) {
//            for (Integer week : b.week) {
//                if (dates.get(week) != null) {
//                    dates.get(week).add(new Pair<Long, Long>(b.dateStart.getTime(), b.dateEnd.getTime()));
//                } else {
//                    List<Pair<Long, Long>> ttt = new ArrayList<>();
//                    ttt.add(new Pair<Long, Long>(b.dateStart.getTime(), b.dateEnd.getTime()));
//                    dates.put(week, ttt);
//                }
//            }
//        }
//
//        for (Integer week : bean.week) {
//            if (dates.get(week) != null) {
//                dates.get(week).add(new Pair<Long, Long>(bean.dateStart.getTime(), bean.dateEnd.getTime()));
//            } else {
//                List<Pair<Long, Long>> ttt = new ArrayList<>();
//                ttt.add(new Pair<Long, Long>(bean.dateStart.getTime(), bean.dateEnd.getTime()));
//                dates.put(week, ttt);
//            }
//        }
//
//        for (int i = 1; i < 8; i++) {
//            List<Pair<Long, Long>> ttt = dates.get(i);
//            if (ttt != null && ttt.size() > 0) {
//                Collections.sort(ttt, new PairFirstComparer());
//                for (int j = 0; j < ttt.size() - 1; j++) {
//                    if (ttt.get(j).second > ttt.get(j + 1).first) {
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//
//
//    }


    public CmBean(ArrayList<Integer> week, Date dateStart) {
        this.week = week;
        this.dateStart = dateStart;
        this.dateEnd = null;
    }

    public CmBean(ArrayList<Integer> week, Date dateStart, Date dateEnd) {
        this.week = week;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.week);
        dest.writeLong(dateStart != null ? dateStart.getTime() : -1);
        dest.writeLong(dateEnd != null ? dateEnd.getTime() : -1);
    }

    protected CmBean(Parcel in) {
        this.week = new ArrayList<Integer>();
        in.readList(this.week, Integer.class.getClassLoader());
        long tmpDateStart = in.readLong();
        this.dateStart = tmpDateStart == -1 ? null : new Date(tmpDateStart);
        long tmpDateEnd = in.readLong();
        this.dateEnd = tmpDateEnd == -1 ? null : new Date(tmpDateEnd);
    }

    public static final Parcelable.Creator<CmBean> CREATOR = new Parcelable.Creator<CmBean>() {
        @Override
        public CmBean createFromParcel(Parcel source) {
            return new CmBean(source);
        }

        @Override
        public CmBean[] newArray(int size) {
            return new CmBean[size];
        }
    };
}
