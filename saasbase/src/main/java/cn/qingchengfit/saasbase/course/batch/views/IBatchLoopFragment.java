package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.Constants;
import cn.qingchengfit.events.EventBatchLooperConfict;
import cn.qingchengfit.items.CmBottomListChosenItem;
import cn.qingchengfit.network.errors.BusEventThrowable;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import rx.functions.Action1;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/9/21.
 */
public abstract class IBatchLoopFragment extends BaseFragment implements
    FlexibleAdapter.OnItemClickListener{

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.label_can_order) TextView labelCanOrder;
  @BindView(R2.id.starttime) CommonInputView starttime;
  @BindView(R2.id.endtime) CommonInputView endtime;
  @BindView(R2.id.civ_order_interval) CommonInputView civOrderInterval;
  @BindView(R2.id.desc) TextView desc;
  @BindView(R2.id.rv) RecyclerView rv;
  @BindArray(R2.array.order_time_interval) String[] orderTimeIntervals;
  protected boolean isPrivate;
  protected CommonFlexAdapter commonFlexAdapter;
  protected Date mStart,mEnd;
  protected boolean isCross;
  /**
   * 私教约课间隔，单位秒
   */
  protected int slice,courseLength;




  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_batch_loop, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    endtime.setVisibility(isPrivate?View.VISIBLE:View.GONE);
    civOrderInterval.setVisibility(isPrivate?View.VISIBLE:View.GONE);
    //init rv
    List<CmBottomListChosenItem> datas = new ArrayList<>();
    for (String week : Constants.WEEKS) {
      datas.add(new CmBottomListChosenItem(week,null));
    }
    commonFlexAdapter  = new CommonFlexAdapter(datas,this);
    commonFlexAdapter.setMode(SelectableAdapter.Mode.MULTI);
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rv.setAdapter(commonFlexAdapter);
    initSelect();
    initView();
    updateTxt();


    RxBusAdd(EventBatchLooperConfict.class)
    .subscribe(new Action1<EventBatchLooperConfict>() {
      @Override public void call(EventBatchLooperConfict rxbusBatchLooperConfictEvent) {
        if (rxbusBatchLooperConfictEvent.isConfict) {
          showAlert("与现有周期有冲突");
        } else {
          getActivity().onBackPressed();
        }
      }
    },new BusEventThrowable());
    return view;
  }

  abstract void initSelect();

  private void initView(){
    starttime.setContent(DateUtils.getTimeHHMM(mStart));
    endtime.setContent((isCross?"":"次日")+DateUtils.getTimeHHMM(mEnd));
    if (isPrivate){
      endtime.setVisibility(View.VISIBLE);
      civOrderInterval.setVisibility(View.VISIBLE);
      civOrderInterval.setContent((slice/60)+"分钟");
    }else {
      endtime.setVisibility(View.GONE);
      civOrderInterval.setVisibility(View.GONE);
    }
  }

  protected abstract void confirm();

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("新增周期");
    toolbar.inflateMenu(R.menu.menu_comfirm);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        confirm();
        return false;
      }
    });
  }



  @Override public String getFragmentName() {
    return IBatchLoopFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R2.id.starttime) public void onStarttimeClicked() {

  }

  @OnClick(R2.id.endtime) public void onEndtimeClicked() {

  }

  @OnClick(R2.id.civ_order_interval) public void onClickInterval() {
    new DialogList(getContext())
        .list(orderTimeIntervals, new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            civOrderInterval.setContent(orderTimeIntervals[position]);
            //根据位置计算实际时间的公式 5 15 30 45 60
            slice = (5+(position/3)*10)*(position%3+1+position/3)*60;
          }
        })
        .show();
  }

  @Override public boolean onItemClick(int position) {
    commonFlexAdapter.toggleSelection(position);
    commonFlexAdapter.notifyItemChanged(position);
    updateTxt();
    return false;
  }

  private void updateTxt() {
    StringBuilder sb = new StringBuilder("每个");
    Iterator<Integer> it = commonFlexAdapter.getSelectedPositions().iterator();
    while (it.hasNext()){
      int w = it.next();
      if (w < Constants.WEEKS.length) {
        if (it.hasNext())
          sb.append(Constants.WEEKS[w]).append(Constants.SEPARATE_CN);
        else sb.append(Constants.WEEKS[w]);
      }
    }
    sb.append(starttime.getContent()).append("-").append(endtime.getContent()).append("可预约此课程");
    if (isPrivate){
      sb.append(",约课间隔时间为").append(civOrderInterval.getContent());
    }
    desc.setText(sb.toString());
  }
}
