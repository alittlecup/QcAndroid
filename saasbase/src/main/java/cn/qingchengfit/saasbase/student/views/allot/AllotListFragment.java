//package cn.qingchengfit.saasbase.student.views.allot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.qingchengfit.saasbase.R;
//import cn.qingchengfit.saasbase.student.network.body.AllotDataResponse;
//import cn.qingchengfit.views.fragments.BaseListFragment;
//import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
//
///**
// *
// * 这里关于数据的类型，应该还是有优化的空间的，传进来一个函数
// * Created by huangbaole on 2017/10/27.
// */
//
//public abstract class AllotListFragment extends BaseListFragment {
//
//
//    @Override
//    public int getNoDataIconRes() {
//        return R.drawable.schedules_no_data;
//    }
//
//    @Override
//    public String getNoDataStr() {
//        return "暂无数据";
//    }
//
//    public void setAllotDatas(List<AllotDataResponse> list){
//        stopRefresh();
//        if (commonFlexAdapter != null) {
//            List<AbstractFlexibleItem> datas = new ArrayList<>();
//            if (list != null) {
//                for (AllotDataResponse item : list) {
//                    datas.add(generateItem(item));
//                }
//            }
//            commonFlexAdapter.updateDataSet(datas,true);
////            setDatas(datas,1);
//        }
//    }
//    public int getItemCount(){
//        return commonFlexAdapter==null?0:commonFlexAdapter.getItemCount();
//    }
//    public List<AbstractFlexibleItem> getDatas(){
//        return commonFlexAdapter.getMainItems();
//    }
//
//    public abstract AbstractFlexibleItem generateItem(AllotDataResponse item) ;
//}
