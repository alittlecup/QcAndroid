package cn.qingchengfit.student.bean;

import java.util.List;

public class CoachPtagAnswerBody {
  String user_id;
  String question_naire_id;
  List<CoachPtagAnswer> questions;

  public CoachPtagAnswerBody(Builder builder){
    this.user_id = builder.user_id;
    this.question_naire_id = builder.question_naire_id;
    this.questions = builder.questions;
  }

  public static class Builder{
    private String user_id;
    private String question_naire_id;
    private List<CoachPtagAnswer> questions;

    public Builder(){
    }

    public Builder user_id(String user_id){
      this.user_id = user_id;
      return this;
    }

    public Builder question_naire_id(String question_naire_id){
      this.question_naire_id = question_naire_id;
      return this;
    }

    public Builder questions(List<CoachPtagAnswer> questions){
      this.questions = questions;
      return this;
    }

    public CoachPtagAnswerBody build(){
      return new CoachPtagAnswerBody(this);
    }

  }
}
