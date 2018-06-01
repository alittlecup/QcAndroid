package cn.qingchengfit.staffkit.views.gym.gym_web;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

	Toolbar toolbar;
	TextView toolbarTitile;

    @Inject public CompletedConnectFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_connect, container, false);
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

      toolbar.setNavigationIcon(R.drawable.ic_triangle_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbarTitile.setText("完成对接");
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return CompletedConnectFragment.class.getName();
    }

 public void onClick(View view) {
        switch (view.getId()) {
            case R.id.copy_link_to_wechat:
                ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(mCopyUrl);
                ToastUtils.showS("链接已复制");
                break;
            case R.id.btn_done:
                ArrayMap<String, Object> body = new ArrayMap<String, Object>();
                body.put("weixin_success", true);
                showLoading();
                RxRegiste(new RestRepository().getPost_api()
                    .qcEditShop(App.staffId, body)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<QcResponse>() {
                        @Override public void call(QcResponse qcResponse) {
                            hideLoading();
                            if (ResponseConstant.checkSuccess(qcResponse)) {
                                getFragmentManager().popBackStackImmediate("wechat", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            } else {
                                ToastUtils.show(qcResponse.getMsg());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            hideLoading();
                            ToastUtils.show("error!");
                        }
                    }));
                break;
            case R.id.go_to_how:
                WebActivity.startWeb(Configs.Server + Configs.WECHAT_GUIDE, getContext());
                break;
        }
    }
}
