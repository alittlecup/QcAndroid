package cn.qingchengfit.saasbase.course.batch.bean;

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
public class BatchLoop implements Parcelable {
    public boolean autoAdd = false;
    public ArrayList<Integer> week = new ArrayList<>();
    public Date dateStart;
    public Date dateEnd;
    public int position;
    public int slice;  // 私教约课时间间隔，单位为秒
    public boolean isCross;

    public BatchLoop() {
    }

    public BatchLoop(ArrayList<Integer> week, Date dateStart, Date dateEnd) {
        this.week = week;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public static List<BatchLoop> getBeansFromTimeRep(HashMap<String, ArrayList<Integer>> map) {
        List<BatchLoop> ret = new ArrayList<>();
        try {
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {

                String key = (String) iterator.next();
                String[] times = key.split("-");
                BatchLoop bean = new BatchLoop(map.get(key), DateUtils.getDateFromHHmm(times[0]), DateUtils.getDateFromHHmm(times[1]));
                ret.add(bean);
            }
        } catch (Exception e) {

        }
        return ret;
    }

    public static ArrayList<Time_repeat> geTimeRepFromBean(List<BatchLoop> cmBeens) {
        ArrayList<Time_repeat> ret = new ArrayList<>();
        try {
            for (BatchLoop batchLoop : cmBeens) {
                for (Integer week : batchLoop.week) {
                    Time_repeat time_repeat = new Time_repeat();
                    time_repeat.setStart(DateUtils.getTimeHHMM(batchLoop.dateStart));
                    time_repeat.setEnd(DateUtils.getTimeHHMM(batchLoop.dateEnd));
                    time_repeat.setWeekday(week);
                    time_repeat.is_cross = batchLoop.isCross;
                    ret.add(time_repeat);
                }
            }
        } catch (Exception e) {

        }
        return ret;
    }

    // TODO: 2017/9/21 检查排期是否有本地冲突 增加了次日，逻辑有问题
    public static boolean CheckCmBean(List<BatchLoop> older, BatchLoop bean) {
        HashMap<Integer, List<Pair<Long, Long>>> dates = new HashMap<>();
        for (BatchLoop b : older) {
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
        dest.writeByte(this.autoAdd ? (byte) 1 : (byte) 0);
        dest.writeList(this.week);
        dest.writeLong(this.dateStart != null ? this.dateStart.getTime() : -1);
        dest.writeLong(this.dateEnd != null ? this.dateEnd.getTime() : -1);
        dest.writeInt(this.position);
        dest.writeByte(this.isCross ? (byte) 1 : (byte) 0);
    }

    protected BatchLoop(Parcel in) {
        this.autoAdd = in.readByte() != 0;
        this.week = new ArrayList<Integer>();
        in.readList(this.week, Integer.class.getClassLoader());
        long tmpDateStart = in.readLong();
        this.dateStart = tmpDateStart == -1 ? null : new Date(tmpDateStart);
        long tmpDateEnd = in.readLong();
        this.dateEnd = tmpDateEnd == -1 ? null : new Date(tmpDateEnd);
        this.position = in.readInt();
        this.isCross = in.readByte() != 0;
    }

    public static final Creator<BatchLoop> CREATOR = new Creator<BatchLoop>() {
        @Override public BatchLoop createFromParcel(Parcel source) {
            return new BatchLoop(source);
        }

        @Override public BatchLoop[] newArray(int size) {
            return new BatchLoop[size];
        }
    };
}
