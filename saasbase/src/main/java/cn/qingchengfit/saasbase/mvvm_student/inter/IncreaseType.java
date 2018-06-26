package cn.qingchengfit.saasbase.mvvm_student.inter;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef(value = { IncreaseType.INCREASE_MEMBER, IncreaseType.INCREASE_STUDENT })
@Retention(RetentionPolicy.RUNTIME) public @interface IncreaseType {
  String INCREASE_MEMBER = "新注册";
  String INCREASE_STUDENT = "会员";
  String INCREASE_FOLLOWUP = "新用户";
}