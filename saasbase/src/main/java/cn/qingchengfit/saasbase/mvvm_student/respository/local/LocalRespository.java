package cn.qingchengfit.saasbase.mvvm_student.respository.local;

import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.saasbase.bill.filter.model.FilterWrapper;
import cn.qingchengfit.utils.FileUtils;
import com.google.gson.Gson;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/7.
 */

public final class LocalRespository {

    //@Inject
    //Application application;

    @Inject
    public LocalRespository() {
    }

    public List<FilterModel> getAssetsFilterModels() {
        return new Gson().fromJson(FileUtils.getJsonFromAssert("filter.json", null), FilterWrapper.class).filters;
    }
}
