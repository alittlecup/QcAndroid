package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PairFirstComparer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
 * Created by Paper on 16/5/5 2016.
 */
public class CmBean implements Parcelable {
    public static final Creator<CmBean> CREATOR = new Creator<CmBean>() {
        @Override public CmBean createFromParcel(Parcel source) {
            return new CmBean(source);
        }

        @Override public CmBean[] newArray(int size) {
            return new CmBean[size];
        }
    };
    public boolean autoAdd = false;
    public ArrayList<Integer> week = new ArrayList<>();
    public Date dateStart;
    public Date dateEnd;
    public int position;

    public CmBean() {
    }

    public CmBean(ArrayList<Integer> week, Date dateStart, Date dateEnd) {
        this.week = week;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    protected CmBean(Parcel in) {
        this.week = new ArrayList<Integer>();
        in.readList(this.week, Integer.class.getClassLoader());
        long tmpDateStart = in.readLong();
        this.dateStart = tmpDateStart == -1 ? null : new Date(tmpDateStart);
        long tmpDateEnd = in.readLong();
        this.dateEnd = tmpDateEnd == -1 ? null : new Date(tmpDateEnd);
        this.position = in.readInt();
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

    public static ArrayList<Time_repeat> geTimeRepFromBean(List<CmBean> cmBeens) {
        ArrayList<Time_repeat> ret = new ArrayList<>();
        try {
            for (CmBean cmBean : cmBeens) {
                for (Integer week : cmBean.week) {
                    Time_repeat time_repeat = new Time_repeat();
                    time_repeat.setStart(DateUtils.getTimeHHMM(cmBean.dateStart));
                    time_repeat.setEnd(DateUtils.getTimeHHMM(cmBean.dateEnd));
                    time_repeat.setWeekday(week);
                    ret.add(time_repeat);
                }
            }
        } catch (Exception e) {

        }
        return ret;
    }

    public static boolean CheckCmBean(List<CmBean> older, CmBean bean) {
        HashMap<Integer, List<Pair<Long, Long>>> dates = new HashMap<>();
        for (CmBean b : older) {
            for (Integer week : b.week) {
                if (dates.get(week) != null) {
                    dates.get(week).add(new Pair<Long, Long>(b.dateStart.getTime(), b.dateEnd.getTime()));
                } else {
                    List<Pair<Long, Long>> ttt = new ArrayList<>();
                    ttt.add(new Pair<Long, Long>(b.dateStart.getTime(), b.dateEnd.getTime()));
                    dates.put(week, ttt);
                }
            }
        }

        for (Integer week : bean.week) {
            if (dates.get(week) != null) {
                dates.get(week).add(new Pair<Long, Long>(bean.dateStart.getTime(), bean.dateEnd.getTime()));
            } else {
                List<Pair<Long, Long>> ttt = new ArrayList<>();
                ttt.add(new Pair<Long, Long>(bean.dateStart.getTime(), bean.dateEnd.getTime()));
                dates.put(week, ttt);
            }
        }

        for (int i = 1; i < 8; i++) {
            List<Pair<Long, Long>> ttt = dates.get(i);
            if (ttt != null && ttt.size() > 0) {
                Collections.sort(ttt, new PairFirstComparer());
                for (int j = 0; j < ttt.size() - 1; j++) {
                    if (ttt.get(j).second > ttt.get(j + 1).first) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.week);
        dest.writeLong(this.dateStart != null ? this.dateStart.getTime() : -1);
        dest.writeLong(this.dateEnd != null ? this.dateEnd.getTime() : -1);
        dest.writeInt(this.position);
    }
}
