package cn.qingchengfit.staffkit.views.student.filter;

public class StudentFilterEventBuilder {
    public int eventType = -1; //事件类型
    public int position = -1; // referrer item click position
    public int status = -1; //
    public int eventFrom = -1;

    public StudentFilter filter;

    public StudentFilterEventBuilder eventType(int eventType) {
        this.eventType = eventType;
        return this;
    }

    public StudentFilterEventBuilder eventFrom(int eventFrom) {
        this.eventFrom = eventFrom;
        return this;
    }

    public StudentFilterEventBuilder position(int position) {
        this.position = position;
        return this;
    }

    public StudentFilterEventBuilder status(int status) {
        this.status = status;
        return this;
    }

    public StudentFilterEventBuilder filter(StudentFilter filter) {
        this.filter = filter;
        return this;
    }

    public StudentFilterEvent createStudentFilterEvent() {
        return new StudentFilterEvent(eventType, position, status, filter, eventFrom);
    }
}