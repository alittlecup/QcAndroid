package cn.qingchengfit.wxpreview.old;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.wxpreview.R;
import cn.qingchengfit.wxpreview.old.newa.WxPreviewEmptyActivity;
import cn.qingchengfit.wxpreview.old.newa.network.WxPreviewApi;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

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
 * Created by Paper on 2017/1/20.
 */
public class ConnectWechatFragment extends BaseFragment {

  CommonInputView civWechatName;
  ImageView imgWechatPublic;
  Toolbar toolbar;
  TextView toolbarTitile;
  @Inject LoginStatus loginStatus;

  String mWxName;
  @Inject QcRestRepository qcRestRepository;
  String mWxQr;
  private String mUpImg;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mWxQr = getArguments().getString("wxQr");
      mWxName = getArguments().getString("wxName");
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_connect_wechat, container, false);
    civWechatName = (CommonInputView) view.findViewById(R.id.civ_wechat_name);
    imgWechatPublic = (ImageView) view.findViewById(R.id.img_wechat_public);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.layout_up_img).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ConnectWechatFragment.this.onClick();
      }
    });
    initToolbar(toolbar);
    toolbarTitile.setText("对接公众账号");
    toolbar.inflateMenu(R.menu.wx_menu_next);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (civWechatName.isEmpty()) {
          ToastUtils.show("请填写微信公众号");
          return true;
        }
        if (TextUtils.isEmpty(mUpImg)) {
          ToastUtils.show("请上传公众号二维码");
          return true;
        }

        ArrayMap<String, Object> body = new ArrayMap<String, Object>();
        body.put("weixin", civWechatName.getContent());
        body.put("weixin_image", mUpImg);
        showLoading();
        Observable<QcDataResponse> qcDataResponseObservable=null;
        if(AppUtils.getCurApp(getContext())==0){
          qcDataResponseObservable =
              qcRestRepository.createGetApi(WxPreviewApi.class)
                  .qcTrainEditShop(loginStatus.staff_id(), body);
        }else{
          qcDataResponseObservable =
              qcRestRepository.createGetApi(WxPreviewApi.class)
                  .qcEditShop(loginStatus.staff_id(), body);
        }
        RxRegiste(qcDataResponseObservable
            .compose(RxHelper.schedulersTransformer())
            .subscribe(qcResponse -> {
              hideLoading();
              if (ResponseConstant.checkSuccess(qcResponse)) {
                if(getActivity() instanceof WxPreviewEmptyActivity){
                  Bundle bundle=new Bundle();
                  bundle.putString("mCopyUrl",getArguments().getString("mCopyUrl"));
                  ((WxPreviewEmptyActivity) getActivity()).toCompleteWxChat(bundle);
                }
              } else {
                ToastUtils.show(qcResponse.getMsg());
              }
            }, throwable -> {
              hideLoading();
              ToastUtils.show("error!");
            }));
        return true;
      }
    });
    civWechatName.setContent(mWxName);
    Glide.with(getContext())
        .load(mWxQr)
        .placeholder(R.drawable.ic_photo_camera_white_24dp)
        .into(imgWechatPublic);

    RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
      @Override public void call(EventChooseImage eventChooseImage) {
        RxRegiste(
            UpYunClient.rxUpLoad("/", eventChooseImage.filePath).subscribe(new Action1<String>() {
              @Override public void call(String s) {
                mUpImg = s;
                Glide.with(getContext())
                    .load(s)
                    .placeholder(R.drawable.ic_photo_camera_white_24dp)
                    .into(imgWechatPublic);
              }
            }));
      }
    });
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {

        return true;
      }
    });
    return view;
  }

  @Override public String getFragmentName() {
    return ConnectWechatFragment.class.getName();
  }

  public void onClick() {
    ChoosePictureFragmentDialog.newInstance(true).show(getFragmentManager(), "");
  }
}
