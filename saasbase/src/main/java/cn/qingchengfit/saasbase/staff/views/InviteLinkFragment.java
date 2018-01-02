package cn.qingchengfit.saasbase.staff.views;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentLinkInviteBinding;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.jakewharton.rxbinding.view.RxView;
import java.util.concurrent.TimeUnit;
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
 * Created by Paper on 2018/1/1.
 */
@Leaf(module = "staff", path = "/invite/link/")
public class InviteLinkFragment extends SaasBaseFragment {

  @Need String url;

  FragmentLinkInviteBinding db;
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    db = FragmentLinkInviteBinding.inflate(inflater);
    db.setToolbarModel(new ToolbarModel.Builder()
      .title("邀请链接")
      .build());
    initToolbar(db.layoutToolbar.toolbar);
    RxView.clicks(db.btnCopy)
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          if (getActivity() != null) {
            ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            if (cmb != null) {
              cmb.setPrimaryClip(ClipData.newPlainText("qingcheng_invite_link", url));
              ToastUtils.showDefaultStyle("已复制到剪切板");
            }
          }
        }
      });
    db.setUrl(url);
    return db.getRoot();
  }


  @Override public String getFragmentName() {
    return InviteLinkFragment.class.getName();
  }
}
