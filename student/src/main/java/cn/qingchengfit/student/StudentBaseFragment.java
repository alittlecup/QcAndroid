package cn.qingchengfit.student;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.student.routers.StudentParamsInjector;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;

public abstract class StudentBaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel>
    extends SaasBindingFragment<DB, VM> {
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    StudentParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
    if (mViewModel != null) {
      mViewModel.getErrorMsg().observe(this, str -> ToastUtils.show(str));
    }
    mViewModel.defaultResult.observe(this, objectResource -> {
      dealResource(objectResource);
    });
  }

  public <T> void dealResource(Resource<T> resource) {
    switch (resource.status) {
      case SUCCESS:
        if (resource.data != null && resource.data instanceof String && !TextUtils.isEmpty(
            (String) resource.data)) {
          handleHttpSuccess((String) resource.data);
        }
        break;
      case ERROR:
        ToastUtils.show(resource.message);
        break;
      case LOADING:
        break;
    }
  }

  protected void handleHttpSuccess(String s) {

  }

  @Override protected void routeTo(String uri, Bundle bd, boolean b) {
    if (BuildConfig.RUN_AS_APP) {
      if (!uri.startsWith("/")) {
        uri = "/" + uri;
      }
      if (this.getActivity() instanceof BaseActivity) {
        this.routeTo(Uri.parse(BuildConfig.PROJECT_NAME
            + "://"
            + ((BaseActivity) this.getActivity()).getModuleName()
            + uri), bd, b);
      }
    } else {
      super.routeTo(uri, bd, b);
    }
  }

  protected void toTrainStudentInfo(String studentId, String shipID) {
    String uri =
        "qccoach://studentdetail/student/detail?studentId=" + studentId + "&shipId=" + shipID;
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    getContext().startActivity(intent);
  }
}
