package com.qingchengfit.fitcoach.Utils;

import com.qingchengfit.fitcoach.App;
import com.umeng.analytics.MobclickAgent;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/8/13 2015.
 */
public class RevenUtils {

    public static void sendException(String reason, String tag, Throwable e) {
//        String rawDsn = "http://e808f33e682c4b10963a41a03a6733fb:6eb9bfd0f4ea4ed48d2488774779df6e@cloudsentry.qingchengfit.cn/3";
//        Raven raven = RavenFactory.ravenInstance(new Dsn(rawDsn));
//
//        EventBuilder eventBuilder = new EventBuilder()
//                .setMessage(reason)
//                .setLevel(Event.Level.ERROR)
//                .setPlatform(PhoneInfoUtils.getHandSetInfo())
//                .setLogger(tag)
//                .addSentryInterface(new ExceptionInterface(e));
//
//        raven.runBuilderHelpers(eventBuilder);
//        raven.sendEvent(eventBuilder.build());
        MobclickAgent.reportError(App.AppContex, reason);
    }

}
