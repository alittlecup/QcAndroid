package cn.qingchengfit.saasbase.course.batch.network.body;

/**
 * Created by fb on 2018/4/16.
 */

public class BatchCopyBody {

  private int is_private;
  private String from_start;
  private String from_end;
  private boolean teacher_all;
  private boolean course_all;
  private String teacher_id;
  private String course_id;
  private String to_start;

  public BatchCopyBody(Builder builder){
    this.is_private = builder.is_private;
    this.from_start = builder.from_start;
    this.from_end = builder.from_end;
    this.teacher_all = builder.teacher_all;
    this.course_all = builder.course_all;
    this.teacher_id = builder.teacher_id;
    this.course_id = builder.course_id;
    this.to_start = builder.to_start;
  }

  public static final class Builder{

    private int is_private;
    private String from_start;
    private String from_end;
    private boolean teacher_all;
    private boolean course_all;
    private String teacher_id;
    private String course_id;
    private String to_start;

    public Builder is_private(int is_private){
      this.is_private = is_private;
      return this;
    }

    public Builder from_start(String from_start){
      this.from_start = from_start;
      return this;
    }

    public Builder from_end(String from_end){
      this.from_end = from_end;
      return this;
    }

    public Builder to_start(String to_start){
      this.to_start = to_start;
      return this;
    }

    public Builder teacher_all(boolean teacher_all){
      this.teacher_all = teacher_all;
      return this;
    }

    public Builder course_all(boolean course_all){
      this.course_all = course_all;
      return this;
    }

    public Builder teacher_id(String teacher_id){
      this.teacher_id = teacher_id;
      return this;
    }

    public Builder course_id(String course_id){
      this.course_id = course_id;
      return this;
    }

    public BatchCopyBody build(){
      return new BatchCopyBody(this);
    }

  }

}
