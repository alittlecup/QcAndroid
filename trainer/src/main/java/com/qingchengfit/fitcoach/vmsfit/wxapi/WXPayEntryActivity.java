package com.qingchengfit.fitcoach.vmsfit.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.common.PayEvent;
import cn.qingchengfit.utils.LogUtil;
import com.qingchengfit.fitcoach.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

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

    @Override public void onReq(BaseReq req) {
    }

    @Override public void onResp(BaseResp resp) {
        LogUtil.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            RxBus.getBus().post(new PayEvent(resp.errCode));
            //            if (resp.errCode == 0){
            //                //成功
            //            }else if (resp.errCode == -1){
            //
            //            }else if (resp.errCode == -2){
            //
            //            }
        }
        this.finish();
    }
}