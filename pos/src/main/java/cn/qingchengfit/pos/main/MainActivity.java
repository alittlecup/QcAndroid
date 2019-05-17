package cn.qingchengfit.pos.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.activity.BaseActivity;
import com.anbillon.flabellum.annotations.Trunk;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import javax.inject.Inject;
import org.json.JSONObject;

@Trunk(fragments = {
  PosMainFragment.class
}) public class MainActivity extends BaseActivity implements HasSupportFragmentInjector {
  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  PosMainFragment posMainFragment;
  private Socket socket;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    posMainFragment = new PosMainFragment();
    getSupportFragmentManager().beginTransaction()
      .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
      .replace(R.id.frag_main, posMainFragment)
      .commit();
    initWs();
    initSensor();
  }




  @Override protected void onDestroy() {
    super.onDestroy();
    if (socket != null) socket.disconnect();
  }

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }

  public void initWs() {
    if (TextUtils.isEmpty(gymWrapper.getSocket_channel_id())) return;
    try {
      socket = IO.socket(
        "http://pusher02.qingchengfit.cn/?channelId=" + gymWrapper.getSocket_channel_id());
      socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
        @Override public void call(Object... args) {
          LogUtil.d("ws", "connect");
        }
      });
      socket.on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
        @Override public void call(Object... args) {
          JSONObject obj = (JSONObject) args[0];
          LogUtil.d("ws", "content:" + obj.toString());
          try {
            String msgtype = obj.getString("type");
            if (msgtype.equalsIgnoreCase("posTask")){
              if (posMainFragment != null && posMainFragment.isAdded()) {
                posMainFragment.showNoti(1);
              }else LogUtil.e("posMain Error!");
            }else LogUtil.e("ws","push data:"+obj.toString());
          }catch (Exception e){
            CrashUtils.sendCrash(e);
          }
        }
      });
      socket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
        @Override public void call(Object... args) {
          for (Object arg : args) {
            LogUtil.e("ws", "error:" + arg.toString());
          }
        }
      });
      socket.on(Socket.EVENT_PING, new Emitter.Listener() {
        @Override public void call(Object... args) {
          LogUtil.d("ws", "ping....");
        }
      });
      socket.connect();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }
}
