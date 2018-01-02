package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.staff.beans.Invitation;
import cn.qingchengfit.saasbase.staff.items.LinkShareItem;
import cn.qingchengfit.saasbase.staff.presenter.StaffAddPresenter;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.widgets.PhoneEditText;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import javax.inject.Inject;

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
 * Created by Paper on 2017/10/18.
 */
@Leaf(module = "staff", path = "/add/") public class StaffAddFragment extends SaasBaseFragment implements
  FlexibleAdapter.OnItemClickListener,StaffAddPresenter.MVPView{
  CommonFlexAdapter commonFlexAdapter;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.civ_name) CommonInputView civName;
  @BindView(R2.id.civ_gender) CommonInputView civGender;
  @BindView(R2.id.phone_num) PhoneEditText phoneNum;
  @BindView(R2.id.civ_position) CommonInputView civPosition;
  @BindView(R2.id.rv) RecyclerView rv;

  @Inject StaffAddPresenter presenter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_staff_add, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter,this);
    initToolbar(toolbar);
    initView();
    if (savedInstanceState != null && savedInstanceState.containsKey("select")){
      onItemClick(savedInstanceState.getInt("select",0));
    }
    presenter.queryPositions();
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("添加工作人员");
  }

  void initView(){

    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return 1;
      }
    });
    rv.setLayoutManager(gridLayoutManager);
    commonFlexAdapter.clear();
    commonFlexAdapter.addItem(new LinkShareItem(R.drawable.vd_invite_link_active,R.drawable.vd_invite_link_normal,R.string.invite_link));
    commonFlexAdapter.addItem(new LinkShareItem(R.drawable.vd_invite_qrcode_active,R.drawable.vd_invite_qrcode_normal,R.string.invite_qrcode));
    commonFlexAdapter.addItem(new LinkShareItem(R.drawable.vd_invite_msg_active,R.drawable.vd_invite_msg_normal,R.string.invite_msm));
    commonFlexAdapter.setMode(SelectableAdapter.Mode.SINGLE);
    rv.setAdapter(commonFlexAdapter);
    if (commonFlexAdapter.getSelectedPositions().size() == 0) {
      commonFlexAdapter.toggleSelection(0);
      commonFlexAdapter.notifyItemChanged(0);
    }
  }

  /**
   * 选择性别
   */
  @OnClick(R2.id.civ_gender) public void onCivGenderClicked() {
    new DialogList(getContext()).list(getResources().getStringArray(R.array.gender_list), new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.setGender(position);
        civGender.setContent(getResources().getStringArray(R.array.gender_list)[position]);
      }
    }).show();
  }

  /**
   * 选择职位
   */
  @OnClick(R2.id.civ_position) public void onCivPositionClicked() {
    presenter.choosePosition();
  }

  /**
   * 完成
   */
  @OnClick(R2.id.btn_done) public void onBtnDoneClicked() {
    presenter.addStaff();
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    int old = 0;
    if(commonFlexAdapter.getSelectedPositions().size() > 0)
      old = commonFlexAdapter.getSelectedPositions().get(0);
    commonFlexAdapter.toggleSelection(position);
    commonFlexAdapter.notifyItemChanged(old);
    commonFlexAdapter.notifyItemChanged(position);
    return true;
  }

  @Override public void onSaveInstanceState(@NonNull Bundle outState) {
    if (commonFlexAdapter != null && commonFlexAdapter.getSelectedPositions().size() >0) {
      outState.putInt("select", commonFlexAdapter.getSingleSelectedPos());
    }
    super.onSaveInstanceState(outState);
  }

  @Override public void onPosition(String position) {
    civPosition.setContent(position);
  }




  @Override public String getName() {
    return civName.getContent();
  }

  @Override public String getPhone() {
    return phoneNum.getPhoneNum();
  }

  @Override public String getPhoneDisrct() {
    return phoneNum.getDistrictInt();
  }

  @Override public void onGetLink(Invitation invitation) {
    int pos =commonFlexAdapter.getSingleSelectedPos();
    switch (pos){
      case 0:
        routeTo("/invite/link/?nostack=true",InviteLinkParams.builder().url(invitation.getUrl()).build());
        break;
      case 1:
        routeTo("/invite/qrcode/?nostack=true",InviteQrCodeParams.builder().url(invitation.getUrl()).build());
        break;
      case 2:
        routeTo("/invite/sendmsg/?nostack=true",InviteSendMsgParams.builder().invitation(invitation).build());
        break;
      default:
        break;
    }
  }

}
