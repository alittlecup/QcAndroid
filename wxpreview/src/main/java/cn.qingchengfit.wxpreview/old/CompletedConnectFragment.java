package cn.qingchengfit.wxpreview.old;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.wxpreview.R;
import cn.qingchengfit.wxpreview.old.newa.network.WxPreviewApi;
import javax.inject.Inject;

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
 * Created by Paper on 2017/1/21.
 */
public class CompletedConnectFragment extends BaseFragment {

  public String mCopyUrl;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository restRepository;

  Toolbar toolbar;
  TextView toolbarTitile;

  @Inject public CompletedConnectFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.wx_fragment_completed_connect, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.copy_link_to_wechat).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        CompletedConnectFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        CompletedConnectFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.go_to_how).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        CompletedConnectFragment.this.onClick(v);
      }
    });
    initToolbar(toolbar);
    toolbarTitile.setText("完成对接");

    return view;
  }

  @Override public String getFragmentName() {
    return CompletedConnectFragment.class.getName();
  }

  public void onClick(View view) {
    int i = view.getId();
    if (i == R.id.copy_link_to_wechat) {
      ClipboardManager cmb =
          (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
      cmb.setText(mCopyUrl);
      ToastUtils.showS("链接已复制");
    } else if (i == R.id.btn_done) {
      ArrayMap<String, Object> body = new ArrayMap<String, Object>();
      body.put("weixin_success", true);
      showLoading();
      RxRegiste(restRepository.createRxJava1Api(WxPreviewApi.class)
          .qcEditShop(loginStatus.staff_id(), body,gymWrapper.getParams())
          .compose(RxHelper.schedulersTransformer())
          .subscribe(qcResponse -> {
            hideLoading();
            if (ResponseConstant.checkSuccess(qcResponse)) {
             getActivity().finish();
            } else {
              ToastUtils.show(qcResponse.getMsg());
            }
          }, throwable -> {

            hideLoading();
            ToastUtils.show("error!");
          }));
    } else if (i == R.id.go_to_how) {
      WebActivity.startWeb(Configs.Server + Configs.WECHAT_GUIDE, getContext());
    }
  }
}
