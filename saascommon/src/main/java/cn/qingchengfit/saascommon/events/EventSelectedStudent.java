package cn.qingchengfit.saascommon.events;

import android.text.TextUtils;
import cn.qingchengfit.Constants;
import cn.qingchengfit.model.base.QcStudentBean;
import java.util.ArrayList;
import java.util.List;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/9/30.
 */

public class EventSelectedStudent {
  List<QcStudentBean> students = new ArrayList<>();
  String src;

  public String getSrc() {
    return src;
  }

  public void setSrc(String src) {
    this.src = src;
  }

  public EventSelectedStudent(List<QcStudentBean> students, String src) {
    this.students = students;
    this.src = src;
  }

  public List<QcStudentBean> getStudents() {
    return students;
  }

  public String getIdFirst(){
    if (students != null && students.size()>0) {
      return students.get(0).getId();
    }else return "";
  }

  public String getNameFirst(){
    if (students != null && students.size()>0) {
      return students.get(0).getUsername();
    }else return "";
  }

  public  String getIdStr(){
    if (students != null) {
      String ret = "";
      int i = 1;
      for (QcStudentBean staff : students) {
        ret = TextUtils.concat(ret,staff.getId()).toString();
        if (i < students.size())
          ret = TextUtils.concat(ret, Constants.SEPARATOR_EN).toString();
        i++;
      }
      return ret;
    }else return "";
  }

  public ArrayList<String> getIDlist() {
    ArrayList<String> ret = new ArrayList<>();
    if (students != null) {
      for (QcStudentBean staff : students) {
        ret.add(staff.id());
      }
    }
    return ret;
  }
  public String getNameStr(){
    if (students != null) {
      String ret = "";
      int i = 1;
      for (QcStudentBean staff : students) {
        ret = TextUtils.concat(ret,staff.getUsername()).toString();
        if (i < students.size())
          ret = TextUtils.concat(ret, Constants.SEPARATE_CN).toString();
        i++;
      }
      return ret;
    }else return "";
  }

}
