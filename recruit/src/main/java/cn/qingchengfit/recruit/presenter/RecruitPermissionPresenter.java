package cn.qingchengfit.recruit.presenter;

import android.support.v4.app.Fragment;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.model.ChatGym;
import cn.qingchengfit.recruit.model.RecruitPermission;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.network.body.EditPermissionBody;
import cn.qingchengfit.recruit.network.response.ChatGymWrap;
import cn.qingchengfit.recruit.network.response.PermisionnListWrap;
import cn.qingchengfit.recruit.network.response.PermissionUserWrap;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.utils.ListUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RecruitPermissionPresenter extends BasePresenter {
  @Inject QcRestRepository qcRestRepository;
  @Inject RecruitRouter router;
  private MVPView view;
  private PermissionUserWrap permissionUsers;
  private ChatGym chatGym;
  private int curEditPermission = 0;

  @Inject public RecruitPermissionPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void queryPermissions(String key, String gymid) {
    HashMap<String, Object> params = new HashMap<>();
    if (key != null) params.put("key", key);
    if (gymid != null) params.put("gym_id", gymid);

    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryRecruitPermission(params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<PermisionnListWrap>>() {
          @Override public void call(QcDataResponse<PermisionnListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onPermssionList(qcResponse.data.permissions);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void editPermssionUsers(EditPermissionBody body) {
    RxRegiste(qcRestRepository.createGetApi(PostApi.class)
        .editpermsiion(body)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onEditOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void queryPermissionUsers(String gymid) {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryPermissionUser(gymid)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<PermissionUserWrap>>() {
          @Override public void call(QcDataResponse<PermissionUserWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              permissionUsers = qcResponse.data;
              view.onPermssionCoutnt(permissionUsers);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  /**
   * 获取工作人员
   */
  public void queryCommonstaff(String gymid) {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryCommonStaffs(gymid)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<ChatGymWrap>>() {
          @Override public void call(QcDataResponse<ChatGymWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              chatGym = qcResponse.data.users;
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public List<User> getPermissonUsers(int key) {
    if (permissionUsers != null) {
      switch (key) {
        case 1:
          return permissionUsers.permissions.resume.users;
        case 2:
          return permissionUsers.permissions.fair.users;
        case 3:
          return permissionUsers.permissions.setting.users;
        default:
          return permissionUsers.permissions.job.users;
      }
    } else {
      return null;
    }
  }

  public void chooseStaff(int i) {
    curEditPermission = i;
    DirtySender.studentList.clear();
    switch (i) {
      case 1:
        DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(),
            permissionUsers.permissions.job.users));
        break;
      case 2:
        DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(),
            permissionUsers.permissions.fair.users));
        break;
      case 3:
        DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(),
            permissionUsers.permissions.setting.users));
        break;
      default:
        DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(),
            permissionUsers.permissions.resume.users));
        break;
    }
    if (chatGym != null && view instanceof Fragment) {
      BaseRouter.toChooseStaff(((Fragment) view), chatGym);
    }
  }

  public String getCurKey() {
    switch (curEditPermission) {
      case 1:
        return "job";
      case 2:
        return "fair";
      case 3:
        return "setting";
      default:
        return "resume";
    }
  }

  public interface MVPView extends CView {
    void onPermssionList(List<RecruitPermission> permissions);

    void onPermssionCoutnt(PermissionUserWrap permissionUserWrap);

    void onEditOk();
  }
}
