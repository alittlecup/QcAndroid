package cn.qingchengfit.saasbase.events;

/**
 * 课程照片管理点击某个管理的事件
 */
public class CourseImageManageEvent {
    public int pos;

    public CourseImageManageEvent(int pos) {
        this.pos = pos;
    }
}