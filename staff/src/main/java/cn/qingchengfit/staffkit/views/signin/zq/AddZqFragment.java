package cn.qingchengfit.staffkit.views.signin.zq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.signin.zq.event.EventAddZq;
import cn.qingchengfit.staffkit.views.signin.zq.model.AccessBody;
import cn.qingchengfit.staffkit.views.signin.zq.model.Guard;
import cn.qingchengfit.staffkit.views.signin.zq.presenter.ZqAccessPresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/9/20.
 */

public class AddZqFragment extends BaseFragment implements
    BottomListFragment.ComfirmChooseListener, ZqAccessPresenter.MVPView {

  public static final String FIND_NO_URL = "https://mp.weixin.qq.com/s/eyKfbS8cRHNt_bqHk6DfJQ";

	public Toolbar toolbar;
	public TextView toolbarTitle;
	FrameLayout toolbarLayout;
	CommonInputView inputGymName;
	CommonInputView inputGymAddress;
	TextView btnFindEquip;
	CommonInputView inputGymFun;
	CommonInputView inputGymStart;
	CommonInputView inputGymEnd;
  @Inject ZqAccessPresenter presenter;
  public AccessBody body;
  BottomListFragment listFragment;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_zq_access, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    inputGymName = (CommonInputView) view.findViewById(R.id.input_gym_name);
    inputGymAddress = (CommonInputView) view.findViewById(R.id.input_gym_address);
    btnFindEquip = (TextView) view.findViewById(R.id.btn_find_equip);
    inputGymFun = (CommonInputView) view.findViewById(R.id.input_gym_fun);
    inputGymStart = (CommonInputView) view.findViewById(R.id.input_gym_start);
    inputGymEnd = (CommonInputView) view.findViewById(R.id.input_gym_end);
    view.findViewById(R.id.btn_find_equip).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onFind();
      }
    });
    view.findViewById(R.id.input_gym_fun).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        selectFun();
      }
    });
    view.findViewById(R.id.input_gym_start).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onTime(v);
      }
    });
    view.findViewById(R.id.input_gym_end).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onTime(v);
      }
    });

    delegatePresenter(presenter, this);
    if (body == null){
      body = new AccessBody();
    }
    setToolbar();
    initBottom();
    return view;
  }

  private void setToolbar(){
    initToolbar(toolbar);
    toolbar.inflateMenu(R.menu.menu_save);
    toolbarTitle.setText("添加门禁");
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (TextUtils.isEmpty(inputGymName.getContent())
            || TextUtils.isEmpty(inputGymAddress.getContent())
            || TextUtils.isEmpty(inputGymFun.getContent())
            || TextUtils.isEmpty(inputGymStart.getContent())
            || TextUtils.isEmpty(inputGymEnd.getContent())) {
          DialogUtils.showAlert(getContext(), getResources().getString(R.string.dialog_tips_add_access));
          return false;
        }
        body.name = inputGymName.getContent();
        body.device_id = inputGymAddress.getContent();
        body.behavior = getStatus(inputGymFun.getContent());
        body.start = inputGymStart.getContent();
        body.end = inputGymEnd.getContent();
        save();
        return false;
      }
    });
  }


  public void onFind(){
    WebActivity.startWeb(FIND_NO_URL, getContext());
  }

  public void save(){
    presenter.addZqAccess(body);
  }

  private int getStatus(String fun){
    switch (fun){
      case "签到":
        return 1;
      case "签出":
        return 2;
    }
    return 0;
  }

  private void initBottom(){
    List<AbstractFlexibleItem> bottomList = new ArrayList<>();
    bottomList.add(new SimpleTextItemItem("签到"));
    bottomList.add(new SimpleTextItemItem("签出"));
    listFragment = BottomListFragment.newInstance("");
    listFragment.loadData(bottomList);
    listFragment.setListener(this);
  }


  public void selectFun(){
    listFragment.show(getFragmentManager(), null);
  }


  public void onTime(final View v){
    TimeDialogWindow timeDialog = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS);
    timeDialog.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        switch (v.getId()){
          case R.id.input_gym_start:
            inputGymStart.setContent(DateUtils.getTimeHHMM(date));
            break;
          case R.id.input_gym_end:
            inputGymEnd.setContent(DateUtils.getTimeHHMM(date));
            break;
        }
      }
    });
    timeDialog.show();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onGetAccess(List<Guard> guardList) {

  }

  @Override public void changeStatusOk() {
  }

  @Override public void onDeleteOk() {

  }

  @Override public void onAddOk() {
    RxBus.getBus().post(new EventAddZq());
    getActivity().onBackPressed();
  }

  @Override public void onEditOk() {
    ToastUtils.show("修改成功");
    getActivity().onBackPressed();
  }

  @Override public void onComfirmClick(List<IFlexible> dats, List<Integer> selectedPos) {
    inputGymFun.setContent(selectedPos.get(0) == 0 ? "签到" : "签出");
  }
}
