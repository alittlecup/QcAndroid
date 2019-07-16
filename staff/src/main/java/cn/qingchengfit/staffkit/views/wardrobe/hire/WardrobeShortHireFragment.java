package cn.qingchengfit.staffkit.views.wardrobe.hire;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.body.HireWardrobeBody;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.events.EventSelectedStudent;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventLongHire;
import cn.qingchengfit.staffkit.views.wardrobe.edit.WardrobeEditFragment;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

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
 * Created by Paper on 16/8/31.
 */
public class WardrobeShortHireFragment extends BaseFragment
    implements WardrobeShortHirePresenter.MVPView {

  CommonInputView chooseStudent;
  Button comfirm;

  @Inject WardrobeShortHirePresenter mPersenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  private Locker mLocker;
  private StudentBean mChoseStu;
  private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
    @Override public boolean onMenuItemClick(MenuItem item) {
      if (item.getItemId() == R.id.action_edit) {
        getParentFragment().getFragmentManager()
            .beginTransaction()
            .replace(mCallbackActivity.getFragId(), WardrobeEditFragment.newInstance(mLocker))
            .addToBackStack("")
            .commit();
      }
      return true;
    }
  };

  public static WardrobeShortHireFragment newInstance(Locker locker) {

    Bundle args = new Bundle();
    args.putParcelable("l", locker);
    WardrobeShortHireFragment fragment = new WardrobeShortHireFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mLocker = getArguments().getParcelable("l");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_wardrobe_short_hire, container, false);
    chooseStudent = (CommonInputView) view.findViewById(R.id.choose_student);
    comfirm = (Button) view.findViewById(R.id.comfirm);
    view.findViewById(R.id.comfirm).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        WardrobeShortHireFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.btn_long_hire).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        WardrobeShortHireFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.choose_student).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        WardrobeShortHireFragment.this.onClick();
      }
    });

    mCallbackActivity.setToolbar("租用更衣柜", false, null, R.menu.menu_edit, listener);
    //
    delegatePresenter(mPersenter, this);
    RxBusAdd(EventSelectedStudent.class).observeOn(AndroidSchedulers.mainThread())
        .filter(eventSelectedStudent -> CmStringUtils.isEmpty(eventSelectedStudent.getSrc())
            || getFragmentName().equalsIgnoreCase(eventSelectedStudent.getSrc()))
        .subscribe(eventSelectedStudent -> {
          chooseStudent.setContent(eventSelectedStudent.getNameFirst());
          StudentBean sb = new StudentBean();
          sb.setId(eventSelectedStudent.getIdFirst());
          mChoseStu = sb;
        });
    return view;
  }

  @Override public String getFragmentName() {
    return WardrobeShortHireFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.comfirm:
        showLoading();
        mPersenter.hireWardrobe(App.staffId,
            new HireWardrobeBody.Builder().is_long_term_borrow(false)
                .locker_id(mLocker.id)
                .user_id(mChoseStu == null ? null : mChoseStu.getId())
                .build());
        break;
      case R.id.btn_long_hire://跳去长期租用
        RxBus.getBus().post(new EventLongHire());
        break;
    }
  }

  @Override public void onHireOK() {
    hideLoading();
    getActivity().onBackPressed();
  }

  @Override public void onShowError(String e) {
    hideLoading();
    ToastUtils.show(e);
  }

  @Override public void onShowError(@StringRes int e) {
    onShowError(getString(e));
  }

  public void onClick() {
    Map<String, Object> map = new HashMap<>();
    map.put("source", getFragmentName());
    map.put("from", "locker");
    map.put("chooseType", 1);
    QcRouteUtil.setRouteOptions(
        new RouteOptions("student").setActionName("/choose/student/").addParams(map)).call();
  }
}
