package cn.qingchengfit.staffkit.usecase;

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
 * Created by Paper on 16/3/2 2016.
 */
//public class ScheduleUseCase {
//    @Inject
//    RestRepository restRepository;
//
//    @Inject
//    public ScheduleUseCase(RestRepository restRepository) {
//        this.restRepository = restRepository;
//    }
//
//    public Observable<QcSchedulesResponse> getOneDaySchedule(String id, String date, String brand_id,String gymid,String gymmodel) {
//        return restRepository.getGet_api().qcGetSchedules(id, date, brand_id,gymid,gymmodel)
//                .observeOn(AndroidSchedulers.mainThread()).onBackpressureBuffer().subscribeOn(Schedulers.io())
//                .retry(new Func2<Integer, Throwable, Boolean>() {
//                    @Override
//                    public Boolean call(Integer integer, Throwable throwable) {
//                        return integer < 3 && throwable instanceof SocketTimeoutException;
//                    }
//                });
//    }
//
//}
