package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.staff.beans.SalerData;
import cn.qingchengfit.saasbase.staff.beans.response.SalerDataWrap;
import cn.qingchengfit.saasbase.staff.items.SalerDataItem;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.jakewharton.rxbinding.view.RxMenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 2017/11/15.
 */
@Leaf(module = "staff",path = "/saler/data/")
public class SalerDataFragment extends BaseFragment {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.cb) CheckBox cb;
  @BindView(R2.id.item_student_header) ImageView itemStudentHeader;
  @BindView(R2.id.item_student_header_loop) RelativeLayout itemStudentHeaderLoop;
  @BindView(R2.id.item_student_name) TextView itemStudentName;
  @BindView(R2.id.item_student_gender) ImageView itemStudentGender;
  @BindView(R2.id.item_tv_student_status) TextView itemTvStudentStatus;
  @BindView(R2.id.item_student_phonenum) TextView itemStudentPhonenum;
  @BindView(R2.id.item_student_gymname) TextView itemStudentGymname;
  @BindView(R2.id.icon_right) ImageView iconRight;
  @BindView(R2.id.rv) RecyclerView rv;
  @BindView(R2.id.tv_hint) TextView tvHint;
  CommonFlexAdapter commonFlexAdapter;

  @Inject IStaffModel staffModel;

  @Need Staff staff;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_saler_data, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    rv.setLayoutManager(new LinearLayoutManager(getContext()));
    rv.setAdapter(commonFlexAdapter);
    RxRegiste(staffModel.getSalerDatas(staff.id)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<SalerDataWrap>>() {
        @Override public void onNext(QcDataResponse<SalerDataWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            onUserInfo(qcResponse.data.user);
            List<SalerData> d = new ArrayList<>();
            d.add(qcResponse.data.stat.today);
            d.add(qcResponse.data.stat.week);
            d.add(qcResponse.data.stat.month);
            onData(d);
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("销售详情");
    toolbar.inflateMenu(R.menu.menu_flow);
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          List<String> bot = new ArrayList<>();
          bot.add("编辑销售资料");
          showSelectSheet(null, bot, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              routeTo("staff",cn.qingchengfit.saasbase.staff.views.StaffDetailParams.builder().staff(staff).build());
            }
          });
        }
      });
  }

  void onUserInfo(User staff) {
    PhotoUtils.smallCircle(itemStudentHeader, staff.getAvatar());
    itemStudentName.setText(staff.getUsername());
    itemStudentGender.setImageResource(
      staff.gender == 1 ? R.drawable.vd_gender_female : R.drawable.vd_gender_male);
    itemStudentPhonenum.setText(staff.getPhone());
  }

  void onData(List<SalerData> das) {
    List<SalerDataItem> d = new ArrayList<>();
    for (SalerData da : das) {
      d.add(new SalerDataItem(da));
    }
    commonFlexAdapter.updateDataSet(d, false);
  }

  @Override public String getFragmentName() {
    return SalerDataFragment.class.getName();
  }
}
