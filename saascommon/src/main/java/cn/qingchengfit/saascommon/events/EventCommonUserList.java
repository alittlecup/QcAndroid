package cn.qingchengfit.saascommon.events;

import cn.qingchengfit.model.common.ICommonUser;
import java.util.List;

public class EventCommonUserList {
  List<ICommonUser> commonUsers;

  public EventCommonUserList(List<ICommonUser> commonUsers) {
    this.commonUsers = commonUsers;
  }

  public List<ICommonUser> getCommonUsers() {
    return commonUsers;
  }

  public void setCommonUsers(List<ICommonUser> commonUsers) {
    this.commonUsers = commonUsers;
  }
}
