package cn.qingchengfit.testmodule;

/**
 * Created by huangbaole on 2017/11/16.
 */

//public class MainAvtivityViewModel extends BaseViewModel {
//
//    MutableLiveData<Integer> liveData1=new MutableLiveData<>();
//    MutableLiveData<Integer> liveData2=new MutableLiveData<>();
//    MediatorLiveData<Integer> liveMerge=new MediatorLiveData<>();
//    public MainAvtivityViewModel(){
//        liveMerge.addSource(liveData1, new Observer<Integer>() {
//            @Override
//            public void onChanged(@Nullable Integer integer) {
//                liveMerge.setValue(integer);
//            }
//        });
//        liveMerge.addSource(liveData2, new Observer<Integer>() {
//            @Override
//            public void onChanged(@Nullable Integer integer) {
//                liveMerge.setValue(integer);
//            }
//        });
//    }
//}
