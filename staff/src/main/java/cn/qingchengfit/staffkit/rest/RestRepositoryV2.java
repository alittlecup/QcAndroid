package cn.qingchengfit.staffkit.rest;

import cn.qingchengfit.network.RestRepository;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import javax.inject.Inject;

/**
 * Created by yangming on 16/11/16.
 */

public class RestRepositoryV2 extends RestRepository {

    @Inject public RestRepositoryV2() {
        super();
    }

    @Override public String serverUrl() {
        return Configs.Server;
    }

    @Override public String sessionId() {
        return PreferenceUtils.getPrefString(App.context, Configs.PREFER_SESSION, "");
    }

    @Override public String appVersionName() {
        return AppUtils.getAppVer(App.context);
    }

    @Override public String oem() {
        return App.context.getString(R.string.oem_tag);
    }
}
