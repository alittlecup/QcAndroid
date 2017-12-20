package cn.qingchengfit.staffkit.permission;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saasbase.repository.IPermissionModel;

/**
 * Created by fb on 2017/12/19.
 */

public class PermissionModel implements IPermissionModel {

  QcRestRepository qcRestRepository;
  GymWrapper gymWrapper;
  LoginStatus loginStatus;

  public PermissionModel(QcRestRepository qcRestRepository, GymWrapper gymWrapper, LoginStatus loginStatus) {
    this.qcRestRepository = qcRestRepository;
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
  }

  @Override public boolean check(String permission) {
    return true;
  }
}
