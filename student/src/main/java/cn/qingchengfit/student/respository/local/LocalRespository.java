package cn.qingchengfit.student.respository.local;

import android.app.Application;
import cn.qingchengfit.saascommon.filter.model.FilterModel;
import cn.qingchengfit.saascommon.filter.model.FilterWrapper;
import cn.qingchengfit.utils.FileUtils;
import com.google.gson.Gson;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by huangbaole on 2017/12/7.
 */
public final class LocalRespository {

    @Inject Application application;

    @Inject
    public LocalRespository() {
    }

    public List<FilterModel> getAssetsFilterModels() {
        return new Gson().fromJson(FileUtils.getJsonFromAssert("filter.json", application), FilterWrapper.class).filters;
    }
}
