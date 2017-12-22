package cn.qingchengfit.staffkit.permission;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.staffkit.model.db.QCDbManagerImpl;

/**
 * Created by fb on 2017/12/19.
 */

public class PermissionModel implements IPermissionModel {

  QcRestRepository qcRestRepository;
  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  QCDbManagerImpl qcDbManager;

  public PermissionModel(QcRestRepository qcRestRepository, GymWrapper gymWrapper, LoginStatus loginStatus, QCDbManagerImpl qcDbManager) {
    this.qcRestRepository = qcRestRepository;
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    this.qcDbManager = qcDbManager;
  }

  @Override public boolean check(String permission) {
    //qcDbManager.check(gymWrapper.shop_id(), permission);
    return true;
  }
}
