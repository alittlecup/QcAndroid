/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.qingchengfit.fitcoach.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.utils.ToastUtils;
import com.qingchengfit.fitcoach.R;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

  private static final int RETURN_MSG_TYPE_LOGIN = 1;
  private static final int RETURN_MSG_TYPE_SHARE = 2;
    private IWXAPI api;

  @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, getString(R.string.wechat_code));
        api.handleIntent(getIntent(), this);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override public void onReq(BaseReq baseReq) {
        //ToastUtils.showS(baseReq.getType()+ ":  type" );
    }

    @Override public void onResp(BaseResp baseResp) {
        ToastUtils.showS(baseResp.errStr);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK: {
              if (baseResp.getType() == RETURN_MSG_TYPE_SHARE) {
                ToastUtils.showS("分享成功！");
              }else if (baseResp.getType() == RETURN_MSG_TYPE_LOGIN){
                SendAuth.Resp auth = (SendAuth.Resp) baseResp;
                RxBus.getBus().post(auth);
              }
            }
            break;
        }
        this.finish();
    }


}
