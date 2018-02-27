package cn.qingchengfit.saasbase.chat.model;

import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.recruit.model.Job;

/**
 * Created by fb on 2017/7/10.
 */

public class Record {

  public Gym gym;
  public Job job;
  public Staff staff;
  public User user;

  public String created_at;
  public int id;
  public int type;

}
