package cn.qingchengfit.wxpreview.old;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import cn.qingchengfit.wxpreview.R;
import cn.qingchengfit.wxpreview.old.newa.MiniProgramUtil;

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
 * Created by Paper on 2017/1/18.
 */

public class GymPoplularize extends ShareDialogFragment {

  private TextView btnToWX;

  public static GymPoplularize newInstance(String title, String text, String img, String url,
      boolean success) {
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("text", text);
    args.putString("img", img);
    args.putString("url", url);
    args.putBoolean("s", success);
    GymPoplularize fragment = new GymPoplularize();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public int getLayoutRes() {
    return R.layout.wx_fragment_share_popularize;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    btnToWX = (TextView) view.findViewById(R.id.connect_public);
    btnToWX.setText(getArguments().getBoolean("s", false) ? "已对接" : "未对接");
    view.findViewById(R.id.btn_to_wechat_public).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onBtnToWechatPublicClicked();
      }
    });
    view.findViewById(R.id.btn_home_qr).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onBtnHomeQrClicked();
      }
    });
    view.findViewById(R.id.btn_more_popularize).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onBtnMorePopularizeClicked();
      }
    });
    view.findViewById(R.id.btn_home_mini).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (MiniProgramUtil.getMiniProgream(getContext()) != null) {
          RouteUtil.routeTo(getContext(), "wxmini", "/show/mini", null);
        } else {
          RouteUtil.routeTo(getContext(), "wxmini", "/mini/page", null);
        }
      }
    });
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void onBtnToWechatPublicClicked() {
    if (listener != null) {
      listener.onBtnToWechatPublicClicked(this);
    }
  }

  public void onBtnHomeQrClicked() {
    if (listener != null) {
      listener.onBtnHomeQrClicked(this);
    }
  }

  public void onBtnMorePopularizeClicked() {
    if (listener != null) {
      listener.onBtnMorePopularizeClicked(this);
    }
  }

  private GymPoplularizeListener listener;

  public void setOnListItemClickListener(GymPoplularizeListener listener) {
    this.listener = listener;
  }

  public interface GymPoplularizeListener {

    void onBtnToWechatPublicClicked(GymPoplularize dialog);

    void onBtnHomeQrClicked(GymPoplularize dialog);

    void onBtnMorePopularizeClicked(GymPoplularize dialog);
  }
}
