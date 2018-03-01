package cn.qingchengfit.saasbase.user.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.databinding.FUserEditBinding;
import cn.qingchengfit.saasbase.staff.beans.StaffLoginMethod;
import cn.qingchengfit.saasbase.user.bean.EditUserBody;
import cn.qingchengfit.saasbase.user.presenter.UserEditPresenter;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.CitiesChooser;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.widgets.DialogList;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
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
 * Created by Paper on 2018/2/6.
 */
@Leaf(module = "user", path = "/edit/") public class UserEditFragment extends BaseFragment
  implements UserEditPresenter.MVPView {

  @Inject UserEditPresenter presenter;
  ChoosePictureFragmentNewDialog chooseAvatar;
  FUserEditBinding db;
  private CitiesChooser citiesChooser;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    citiesChooser = new CitiesChooser(getContext());


    RxBus.getBus().register(SendAuth.Resp.class)
      .compose(bindToLifecycle())
      .onBackpressureDrop()
      .subscribe(new BusSubscribe<SendAuth.Resp>() {
        @Override public void onNext(SendAuth.Resp resp) {
          presenter.bindWxtoPhone(resp.code);
        }
      });
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    db = FUserEditBinding.inflate(inflater);
    delegatePresenter(presenter, this);
    initToolbar(db.layoutToolbar.toolbar);
    db.headerLayout.setOnClickListener(view -> clickAvatar());
    db.civCity.setOnClickListener(view -> clickCity());
    db.civGender.setOnClickListener(view -> clickGender());
    db.username.setOnClickListener(view -> clickUsername());
    db.layoutLoginPhone.setOnClickListener(view -> routeTo("/new/phone/",null));
    db.btnBind.setOnClickListener(view -> bindWx());
    db.civPw.setOnClickListener(view -> routeTo("/check/code/",null));
    presenter.getCurUser();
    RxBusAdd(EventTxT.class)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventTxT>() {
        @Override public void onNext(EventTxT eventTxT) {
          db.username.setContent(eventTxT.txt);
          presenter.editUser(EditUserBody.newBuilder().username(eventTxT.txt).build());
        }
      });
    return db.getRoot();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    db.setToolbarModel(new ToolbarModel("我的资料"));
  }

  void bindWx(){
    if (presenter.getUser().isWeixin_active()){
      DialogUtils.instanceDelDialog(getContext(), "确定要解除微信绑定吗？", "解除绑定后你将无法通过微信登录", new MaterialDialog.SingleButtonCallback() {
        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
          presenter.unBind();
        }
      }).show();
    }else
      presenter.bindWx();
  }

  /**
   * 修改头像
   */
  void clickAvatar() {
    chooseAvatar = ChoosePictureFragmentNewDialog.newInstance();
    chooseAvatar.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
      @Override public void onChoosefile(String s) {
        PhotoUtils.smallCircle(db.headerImg, s);
      }

      @Override public void onUploadComplete(String s, String s1) {
        PhotoUtils.smallCircle(db.headerImg, s1);
        presenter.editUser(EditUserBody.newBuilder().build());
      }
    });
    chooseAvatar.show(getChildFragmentManager(), "");
  }

  /**
   * 修改用户名
   */
  void clickUsername() {
    routeTo("common", "/input/", CommonInputParams.builder()
      .title("修改昵称")
      .hint("输入昵称")
      .content(presenter.getUser().username)
      .build());
  }

  /**
   * 选择性别
   */
  void clickGender() {
    new DialogList(getContext()).list(getResources().getStringArray(R.array.gender_list),
      (parent, view, position, id) -> {
        db.civGender.setContent(getResources().getStringArray(R.array.gender_list)[position % 2]);
        presenter.editUser(EditUserBody.newBuilder().gender(position % 2).build());
      }).show();
  }

  /**
   * 选择城市
   */
  void clickCity() {
    citiesChooser.setOnCityChoosenListener((provice, city, district, id) -> {
      db.civCity.setContent(city);
      presenter.editUser(EditUserBody.newBuilder().gd_district_id(id + "").build());
    });
    citiesChooser.show(getView());
  }


  //用户信息填充
  public void onUserInfo(User user) {
    if (user == null) return;
    db.setMethods(new StaffLoginMethod.Builder().wx_active(user.isWeixin_active())
      .wx(user.getWeixin())
      .phone_active(user.isPhone_active())
      .phone(user.getPhone())
      .build());
    PhotoUtils.smallCircle(db.headerImg, user.getAvatar());//todo默认头像
    db.username.setContent(user.username);
    db.civCity.setContent(user.getCity());

    db.civGender.setContent(getResources().getStringArray(R.array.gender_list)[user.gender % 2]);

    db.imgWx.setImageResource(TextUtils.isEmpty(user.getWeixin())?R.drawable.vd_loginmethod_wechat_disable:R.drawable.vd_loginmethod_wechat);
  }

  @Override public String getFragmentName() {
    return UserEditFragment.class.getName();
  }
}
