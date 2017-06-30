package cn.qingchengfit.staffkit.rest;

import cn.qingchengfit.model.responese.CreatBrand;
import cn.qingchengfit.model.responese.GymList;
import cn.qingchengfit.model.responese.Login;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponseSystenInit;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.Post_Api;
import cn.qingchengfit.staffkit.usecase.bean.CreatBrandBody;
import cn.qingchengfit.staffkit.usecase.bean.FeedBackBody;
import cn.qingchengfit.staffkit.usecase.bean.FixPhoneBody;
import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
import cn.qingchengfit.staffkit.usecase.bean.LoginBody;
import cn.qingchengfit.staffkit.usecase.bean.ModifyPwBody;
import cn.qingchengfit.staffkit.usecase.bean.RegisteBody;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;
import timber.log.Timber;

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
 * Created by Paper on 15/12/2 2015.
 */
public class RestRepository implements Repository {

    private final Get_Api get_api;
    private final Post_Api post_api;
    private Action1<Throwable> doOnError = new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
            if (throwable != null) Timber.e("retrofit:" + throwable.getMessage());
        }
    };

    public RestRepository() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

            @Override public void log(String message) {
              LogUtil.d(message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File cacheFile = new File(App.context.getExternalCacheDir(), "http_cache/");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 20); //20Mb
        OkHttpClient client =
            new OkHttpClient.Builder().retryOnConnectionFailure(false).cache(cache).addNetworkInterceptor(new Interceptor() {
                @Override public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!request.method().equalsIgnoreCase("GET")) {
                      String token = "";
                      try {
                        token = get_api.qcGetToken().execute().body().data.getToken();
                      } catch (Exception e) {

                      }
                        request = request.newBuilder()
                            .addHeader("Connection", "close")
                            .addHeader("X-CSRFToken", token)
                            .addHeader("Cookie",
                                "csrftoken=" + token + ";sessionid=" + PreferenceUtils.getPrefString(App.context, Configs.PREFER_SESSION,
                                    ""))
                            .addHeader("User-Agent",
                                " FitnessTrainerAssistant/" + AppUtils.getAppVer(App.context) + " Android  OEM:" + App.context.getString(
                                    R.string.oem_tag) + "  QingchengApp/Staff")
                            .build();
                    } else {
                        request = request.newBuilder()
                            .addHeader("Connection", "close")
                            .addHeader("Cookie", "sessionid=" + PreferenceUtils.getPrefString(App.context, Configs.PREFER_SESSION, ""))
                            .addHeader("User-Agent",
                                " FitnessTrainerAssistant/" + AppUtils.getAppVer(App.context) + " Android  OEM:" + App.context.getString(
                                    R.string.oem_tag) + "  QingchengApp/Staff")
                            .build();
                    }
                    return chain.proceed(request);
                }
            }).addNetworkInterceptor(interceptor).readTimeout(3, TimeUnit.MINUTES).build();

        Gson customGsonInstance = new GsonBuilder().enableComplexMapKeySerialization()

            //                .setExclusionStrategies(new ExclusionStrategy() {
            //                    @Override
            //                    public boolean shouldSkipField(FieldAttributes f) {
            //                        return f.getDeclaringClass().equals(RealmObject.class);
            //                    }
            //
            //                    @Override
            //                    public boolean shouldSkipClass(Class<?> clazz) {
            //                        return false;
            //                    }
            //                })
            .create();
        Retrofit getApiAdapter = new Retrofit.Builder().baseUrl(Configs.Server)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

        Retrofit postApiAdapter = new Retrofit.Builder().baseUrl(Configs.Server)
            .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(client)
            .build();

        get_api = getApiAdapter.create(Get_Api.class);
        post_api = postApiAdapter.create(Post_Api.class);
    }

    public RestRepository(final String session) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();
                if (!request.method().equalsIgnoreCase("GET")) {
                    String token = get_api.qcGetToken().execute().body().data.getToken();

                    request = request.newBuilder()
                        .addHeader("X-CSRFToken", token)
                        .addHeader("Cookie", "csrftoken=" + token + ";sessionid=" + session)
                        .addHeader("User-Agent", "Android Staff")
                        .build();
                } else {
                    request =
                        request.newBuilder().addHeader("Cookie", "sessionid=" + session).addHeader("User-Agent", "Android Staff").build();
                }
                return chain.proceed(request);
            }
        }).addNetworkInterceptor(interceptor).readTimeout(3, TimeUnit.MINUTES).build();
        Gson customGsonInstance = new GsonBuilder().enableComplexMapKeySerialization()
            //                .setExclusionStrategies(new ExclusionStrategy() {
            //                    @Override
            //                    public boolean shouldSkipField(FieldAttributes f) {
            //                        return f.getDeclaringClass().equals(RealmObject.class);
            //                    }
            //
            //                    @Override
            //                    public boolean shouldSkipClass(Class<?> clazz) {
            //                        return false;
            //                    }
            //                })
            .create();
        Retrofit getApiAdapter = new Retrofit.Builder().baseUrl(Configs.Server)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

        Retrofit postApiAdapter = new Retrofit.Builder().baseUrl(Configs.Server)
            .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(client)
            .build();

        get_api = getApiAdapter.create(Get_Api.class);
        post_api = postApiAdapter.create(Post_Api.class);
    }

    public Get_Api getGet_api() {
        return get_api;
    }

    public Post_Api getPost_api() {
        return post_api;
    }

    @Override public Observable<QcResponseData<Login>> qcLogin(LoginBody loginBody) {
        return post_api.qcLogin(loginBody).doOnError(doOnError);
    }

    public Observable<QcResponse> qcQueryCode(GetCodeBody body) {
        return post_api.qcGetCode(body).doOnError(doOnError);
    }

    public Observable<QcResponseData<Login>> qcRegiste(RegisteBody registeBody) {
        return post_api.qcRegister(registeBody).doOnError(doOnError);
    }

    /**
     * 获取健身房服务列表
     */
    public Observable<QcResponseData<GymList>> qcGetGymList(String coach_id, String brand_id) {
        return get_api.qcGetCoachService(coach_id, brand_id).retry(new Func2<Integer, Throwable, Boolean>() {
            @Override public Boolean call(Integer integer, Throwable throwable) {
                return integer < 3 && throwable instanceof SocketTimeoutException;
            }
        }).doOnError(doOnError);
    }

    /**
     * 修改密码
     */
    public Observable<QcResponse> qcFixPW(ModifyPwBody modifyPwBody) {
        return post_api.qcMoidfyPw(App.staffId, modifyPwBody).retry(new Func2<Integer, Throwable, Boolean>() {
            @Override public Boolean call(Integer integer, Throwable throwable) {
                return integer < 3 && throwable instanceof SocketTimeoutException;
            }
        }).doOnError(doOnError);
    }

    public Observable<QcResponse> qcFixPhone(FixPhoneBody fixPhoneBody) {
        return post_api.qcModifyPhoneNum(App.staffId, fixPhoneBody).retry(new Func2<Integer, Throwable, Boolean>() {
            @Override public Boolean call(Integer integer, Throwable throwable) {
                return integer < 3 && throwable instanceof SocketTimeoutException;
            }
        }).doOnError(doOnError);
    }

    public Observable<QcResponse> qcReport(FeedBackBody bean) {
        return post_api.qcFeedBack(bean).retry(new Func2<Integer, Throwable, Boolean>() {
            @Override public Boolean call(Integer integer, Throwable throwable) {
                return integer < 3 && throwable instanceof SocketTimeoutException;
            }
        }).doOnError(doOnError);
    }

    public Observable<QcResponseData<CreatBrand>> qcCreateBrand(CreatBrandBody bean) {
        return post_api.qcCreatBrand(bean).retry(new Func2<Integer, Throwable, Boolean>() {
            @Override public Boolean call(Integer integer, Throwable throwable) {
                return integer < 3 && throwable instanceof SocketTimeoutException;
            }
        })
            //                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            //                    @Override
            //                    public Observable<?> call(Observable<? extends Throwable> observable) {
            //                        return null;
            //                    }
            //                })
            //                .doOnError(doOnError)
            ;
    }

    public Observable<QcResponseSystenInit> qcSystemInit(SystemInitBody bean) {
        return post_api.qcSystemInit(bean).retry(new Func2<Integer, Throwable, Boolean>() {
            @Override public Boolean call(Integer integer, Throwable throwable) {
                return integer < 3 && throwable instanceof SocketTimeoutException;
            }
        }).doOnError(doOnError);
    }
}
