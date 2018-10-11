package cn.qingchengfit.checkout.bean;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static cn.qingchengfit.checkout.bean.PayChannel.*;

/**
 * 支付方式字段：channel
 *
 * ALIPAY_QRCODE 支付宝二维码
 * WEIXIN_QRCODE 微信二维码
 * ALIPAY_BARCODE 支付宝扫码支付
 * WEIXIN_BARCODE 微信扫码支付
 * WEIXIN 微信公众号支付
 * app充值购卡的支付方式字段： charge_type
 *
 * 1 # 现金支付
 * 2 # 刷卡
 * 3 # 转账
 * 4 # 其他
 * 7 # 微信二维码支付
 * 12 # 支付宝二维码
 * 13 # 微信扫码支付
 * 14 # 支付宝扫码支付
 */
@IntDef(value = {
    ALIPAY_QRCODE,WEIXIN_QRCODE,ALIPAY_BARCODE,WEIXIN_BARCODE
})
@Retention(RetentionPolicy.RUNTIME) public @interface PayChannel {
  int ALIPAY_QRCODE = 12;
  int WEIXIN_QRCODE = 7;
  int ALIPAY_BARCODE = 14;
  int WEIXIN_BARCODE = 13;
}
