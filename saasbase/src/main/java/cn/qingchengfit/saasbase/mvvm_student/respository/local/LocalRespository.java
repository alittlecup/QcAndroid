package cn.qingchengfit.saasbase.mvvm_student.respository.local;

import android.app.Application;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.saasbase.bill.filter.model.FilterWrapper;
import cn.qingchengfit.utils.FileUtils;

/**
 * Created by huangbaole on 2017/12/7.
 */

public final class LocalRespository {
    @Inject
    Application application;

    @Inject
    public LocalRespository() {
    }

    public List<FilterModel> getAssetsFilterModels() {
        return new Gson().fromJson(FileUtils.getJsonFromAssert("filter.json", application), FilterWrapper.class).filters;
    }
}
