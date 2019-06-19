package cn.qingchengfit.student.bean;

import cn.qingchengfit.model.base.Shop;
import java.util.List;

public class ClassRecords {
    public Stat stat;
    public List<AttendanceRecord> attendances;
    public List<Shop> shops;

    public static class Stat {
        public int group;
        public int checkin;
        public int private_count;
        public int days;
    }
}