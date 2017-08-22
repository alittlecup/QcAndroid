package cn.qingchengfit.staffkit.allocate.coach.model;

import cn.qingchengfit.model.responese.QcResponsePage;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by fb on 2017/5/3.
 */

public class AllocateStudentBean extends QcResponsePage {

    @SerializedName("users") public List<StudentWithCoach> users;
}
