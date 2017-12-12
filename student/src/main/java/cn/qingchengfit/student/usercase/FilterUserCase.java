package cn.qingchengfit.student.usercase;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saasbase.bill.filter.model.Content;
import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.saasbase.bill.filter.model.UserExtra;
import cn.qingchengfit.saasbase.staff.network.response.SalerUserListWrap;
import cn.qingchengfit.saasbase.student.bean.SourceBean;
import cn.qingchengfit.saasbase.student.bean.SourceBeans;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.student.respository.StudentRespository;

/**
 * Created by huangbaole on 2017/12/7.
 */

@Singleton
public class FilterUserCase {

    @Inject
    StudentRespository studentRespository;

    public LiveData<List<FilterModel>> getFilterModel() {
        return filterModel;
    }

    MediatorLiveData<List<FilterModel>> filterModel = new MediatorLiveData<>();

    @Inject
    public FilterUserCase() {

    }

    public void excute(String id,String sellerId, HashMap<String, Object> params) {
        //取本地数据
        LiveData<List<FilterModel>> listLiveData = studentRespository.qcGetFilterModelFromLocal();
        //取服务端的推荐人列表
        LiveData<SalerUserListWrap> salerUserListWrapLiveData = studentRespository.qcGetTrackStudentsRecommends(id, params);
        //修改参数，取服务端来源
        if (!TextUtils.isEmpty(sellerId)) {
            params.put("seller_id", sellerId);
        } else {
            params.put("seller_id", 0);
        }
        LiveData<SourceBeans> sourceBeansLiveData = studentRespository.qcGetTrackStudentsOrigins(id, params);

        filterModel.addSource(salerUserListWrapLiveData, salerUserListWrap -> {
            filterModel.removeSource(salerUserListWrapLiveData);
            List<FilterModel> filterModels = new ArrayList<>();
            List<FilterModel> value = listLiveData.getValue();
            SourceBeans value1 = sourceBeansLiveData.getValue();
            filterModels.addAll(value);
            filterModels.add(salerusersToFilterModel(salerUserListWrap));
            filterModels.add(sourceBeanToFilterModel(value1));
            filterModel.setValue(filterModels);

        });
        filterModel.addSource(sourceBeansLiveData, sourceBeans -> {
            filterModel.removeSource(sourceBeansLiveData);
            List<FilterModel> filterModels = new ArrayList<>();
            List<FilterModel> value = listLiveData.getValue();
            SalerUserListWrap value1 = salerUserListWrapLiveData.getValue();
            filterModels.addAll(value);
            filterModels.add(salerusersToFilterModel(value1));
            filterModels.add(sourceBeanToFilterModel(sourceBeans));
            filterModel.setValue(filterModels);

        });
        filterModel.setValue(listLiveData.getValue());
    }

    private FilterModel sourceBeanToFilterModel(SourceBeans beans) {
        FilterModel filterModel = new FilterModel();
        filterModel.type = 5;
        filterModel.name = "来源";
        filterModel.key = "origin_id";
        List<Content> contents = new ArrayList<>();
        filterModel.content=contents;
        if (beans == null || beans.origins == null || beans.origins.isEmpty()) return filterModel;

        for (SourceBean bean : beans.origins) {
            Content content = new Content();
            content.name = bean.name;
            User user = new User();
            UserExtra userExtra = new UserExtra();
            user.username = bean.name;
            user.id = bean.id;
            userExtra.user = user;
            content.extra = userExtra;
            contents.add(content);
        }

        return filterModel;
    }

    private FilterModel salerusersToFilterModel(SalerUserListWrap bean) {
        FilterModel filterModel = new FilterModel();
        filterModel.type = 3;
        filterModel.name = "推荐人";
        filterModel.key = "recommend_user_id";
        List<Content> contents = new ArrayList<>();
        filterModel.content=contents;
        if (bean == null || bean.users == null || bean.users.isEmpty()) return filterModel;
        for (Staff staff : bean.users) {
            Content content = new Content();
            content.name = staff.username;
            content.value = staff.id;
            UserExtra userExtra = new UserExtra();
            User user = new User();
            user.avatar = staff.avatar;
            user.username = staff.username;
            user.phone=staff.phone;
            user.id = staff.id;
            user.gender = staff.gender;
            userExtra.user = user;
            content.extra = userExtra;
            contents.add(content);
        }

        return filterModel;
    }
}
