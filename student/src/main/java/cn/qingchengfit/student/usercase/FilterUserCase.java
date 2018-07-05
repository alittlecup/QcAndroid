package cn.qingchengfit.student.usercase;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.text.TextUtils;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saascommon.filter.model.Content;
import cn.qingchengfit.saascommon.filter.model.FilterModel;
import cn.qingchengfit.saascommon.filter.model.FilterWrapper;
import cn.qingchengfit.saascommon.filter.model.UserExtra;
import cn.qingchengfit.saascommon.network.HttpCheckFunc;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.bean.SourceBean;
import cn.qingchengfit.student.bean.SourceBeans;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.utils.FileUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * Created by huangbaole on 2017/12/7.
 */

@Singleton
public class FilterUserCase {
    @Inject Application application;
    @Inject StudentRepository studentRespository;

    //@Inject IStudentModel remoteService;

    public LiveData<List<FilterModel>> getFilterModel() {
        return filterModel;
    }

    MediatorLiveData<List<FilterModel>> filterModel = new MediatorLiveData<>();

    @Inject
    public FilterUserCase() {

    }

    public void excute(String id,String sellerId, HashMap<String, Object> params) {
        Observable<List<FilterModel>> filter =
            Observable.just("filter.json").map(s -> new Gson().fromJson(
                FileUtils.getJsonFromAssert("filter.json", application), FilterWrapper.class).filters);

        //Observable<SalerUserListWrap> salers =
        //    remoteService.qcGetTrackStudentsRecommends(id, params)
        //    .map(new HttpCheckFunc());
        //
        //if (!TextUtils.isEmpty(sellerId)) {
        //    params.put("seller_id", sellerId);
        //} else {
        //    //params.put("seller_id", 0);
        //}
        //
        //Observable<SourceBeans> sources =
        //    remoteService.qcGetTrackStudentsOrigins(id, params).map(new HttpCheckFunc());
        //
        //Observable.zip(filter, salers, sources, (filterModels, salerUserListWrap, sourceBeans) -> {
        //    filterModels.add(salerusersToFilterModel(salerUserListWrap));
        //    filterModels.add(sourceBeanToFilterModel(sourceBeans));
        //    return filterModels;
        //}).compose(RxHelper.schedulersTransformer()).subscribe(
        //    filterModels -> filterModel.setValue(filterModels));
        filter.compose(RxHelper.schedulersTransformer()).subscribe(filterModels -> filterModel.setValue(filterModels));

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

    //private FilterModel salerusersToFilterModel(SalerUserListWrap bean) {
    //    FilterModel filterModel = new FilterModel();
    //    filterModel.type = 3;
    //    filterModel.name = "推荐人";
    //    filterModel.key = "recommend_user_id";
    //    List<Content> contents = new ArrayList<>();
    //    filterModel.content=contents;
    //    if (bean == null || bean.users == null || bean.users.isEmpty()) return filterModel;
    //    for (Staff staff : bean.users) {
    //        Content content = new Content();
    //        content.name = staff.username;
    //        content.value = staff.id;
    //        UserExtra userExtra = new UserExtra();
    //        User user = new User();
    //        user.avatar = staff.avatar;
    //        user.username = staff.username;
    //        user.phone=staff.phone;
    //        user.id = staff.id;
    //        user.gender = staff.gender;
    //        userExtra.user = user;
    //        content.extra = userExtra;
    //        contents.add(content);
    //    }
    //
    //    return filterModel;
    //}
}
