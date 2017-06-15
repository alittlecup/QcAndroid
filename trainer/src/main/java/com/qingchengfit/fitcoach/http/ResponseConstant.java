package com.qingchengfit.fitcoach.http;

import cn.qingchengfit.network.response.QcResponse;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/2/18 2016.
 */
public class ResponseConstant {
    public static final int SUCCESS = 200;
    public static final String E_Login = "400001";

    public static boolean checkSuccess(QcResponse qcResponse) {
        if (qcResponse.status == SUCCESS) {
            return true;
        } else {
            return false;
        }
    }
}
