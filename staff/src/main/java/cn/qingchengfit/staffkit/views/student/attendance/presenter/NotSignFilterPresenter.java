package cn.qingchengfit.staffkit.views.student.attendance.presenter;

import cn.qingchengfit.di.BasePresenter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/9/19.
 */

public class NotSignFilterPresenter extends BasePresenter {

  @Inject public NotSignFilterPresenter() {
  }

  public List<String> getClassFilterList(){
    List<String> classList = new ArrayList<>();
    classList.add("全部");
    classList.add("团课");
    classList.add("私教");
    classList.add("自由训练");
    return classList;
  }

  public List<String> getStatusFilter(String type){
    List<String> statusList = new ArrayList<>();
    statusList.add("全部");
    if (type.equals("自由训练") || type.equals("全部")){
      statusList.add("已签到");
      statusList.add("已签出");
    }
    if (!type.equals("自由训练")){
      statusList.add("已预约");
      statusList.add("已取消");
      statusList.add("已完成");
      statusList.add("已签课");
    }
    return statusList;
  }

  public List<String> getTimeFiler(){
    List<String> timeList = new ArrayList<>();
    timeList.add("不限");
    timeList.add("今日");
    timeList.add("最近7天");
    timeList.add("最近30天");
    timeList.add("自定义");
    return timeList;
  }

}
