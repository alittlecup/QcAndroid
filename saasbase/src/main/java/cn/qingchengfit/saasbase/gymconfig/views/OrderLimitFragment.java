//package cn.qingchengfit.saasbase.gymconfig.views;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.StringRes;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.qingchengfit.items.SimpleTextItemItem;
//import cn.qingchengfit.saasbase.R;
//import cn.qingchengfit.saasbase.R2;
//import cn.qingchengfit.saasbase.SaasBaseFragment;
//import cn.qingchengfit.saasbase.gymconfig.network.response.ShopConfigBody;
//import cn.qingchengfit.saasbase.gymconfig.presenter.OrderLimitPresenter;
//import cn.qingchengfit.utils.ToastUtils;
//import cn.qingchengfit.views.fragments.BottomListFragment;
//import cn.qingchengfit.widgets.CommonInputView;
//import cn.qingchengfit.widgets.ExpandedLayout;
//import com.anbillon.flabellum.annotations.Need;
//import com.hannesdorfmann.fragmentargs.FragmentArgs;
//import com.hannesdorfmann.fragmentargs.annotation.Arg;
//import eu.davidea.flexibleadapter.items.IFlexible;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import javax.inject.Inject;
//
//public class OrderLimitFragment extends SaasBaseFragment
//    implements OrderLimitPresenter.MVPView, BottomListFragment.ComfirmChooseListener {
//
//  @BindView(R2.id.civ_order_course_time) CommonInputView civOrderCourseTime;
//  @BindView(R2.id.sw_order_course) ExpandedLayout swOrderCourse;
//  @BindView(R2.id.civ_cancel_time) CommonInputView civCancelTime;
//  @BindView(R2.id.sw_cancle) ExpandedLayout swCancle;
//  @BindView(R2.id.sw_substitute) ExpandedLayout swSubstitute;
//
//  @Need public boolean mIsPrivate;
//  @BindView(R2.id.toolbar) Toolbar toolbar;
//  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
//  @Inject OrderLimitPresenter mOrderLimitPresenter;
//  @BindView(R2.id.input_sign_class_way) CommonInputView inputSignClassWay;
//  @BindView(R2.id.input_sign_class_start) CommonInputView inputSignClassStart;
//  @BindView(R2.id.input_sign_class_end) CommonInputView inputSignClassEnd;
//  @BindView(R2.id.sw_sign_group) ExpandedLayout swSignGroup;
//  private String mOrderId;
//  private String mCancleId;
//  private String mMsgId;
//  private String mSignOpenId;
//  private String mSignWayId;
//  private String mSignStartId;
//  private String mSignEndId;
//
//  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    FragmentArgs.inject(this);
//  }
//
//  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
//      Bundle savedInstanceState) {
//    View view = inflater.inflate(R.layout.fragment_order_limit, container, false);
//    ButterKnife.bind(this, view);
//    delegatePresenter(mOrderLimitPresenter, this);
//    mCallbackActivity.setToolbar(mIsPrivate ? "私教预约限制" : "团课预约限制", false, null, R.menu.menu_comfirm,
//        new Toolbar.OnMenuItemClickListener() {
//          @Override public boolean onMenuItemClick(MenuItem item) {
//            showLoading();
//            comfirmConfigs();
//            return true;
//          }
//        });
//    swSignGroup.setLabel(mIsPrivate ? getResources().getString(R.string.label_sign_private_class)
//        : getResources().getString(R.string.label_sign_group_class));
//
//    mOrderLimitPresenter.queryCancelLimit(mIsPrivate);
//    mOrderLimitPresenter.queryOrderLimit(mIsPrivate);
//    mOrderLimitPresenter.querySubstituteLimit(mIsPrivate);
//    mOrderLimitPresenter.querySignLimit(mIsPrivate);
//    initToolbar(toolbar);
//
//    return view;
//  }
//
//  @Override public void initToolbar(@NonNull Toolbar toolbar) {
//    super.initToolbar(toolbar);
//    toolbarTitile.setText(mIsPrivate ? "私教预约限制" : "团课预约限制");
//    toolbar.inflateMenu(R.menu.menu_save);
//    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//      @Override public boolean onMenuItemClick(MenuItem item) {
//        comfirmConfigs();
//        return true;
//      }
//    });
//  }
//
//  @OnClick(R.id.input_sign_class_way)
//  public void onSelectSignway(){
//    ArrayList<String> items = new ArrayList<>();
//    items.add("教练点名");
//    items.add("会员扫码");
//    BottomListFragment listFragment = BottomListFragment.newInstance("", items);
//    listFragment.setListener(this);
//    listFragment.show(getFragmentManager(), null);
//  }
//
//  @Override public String getFragmentName() {
//    return OrderLimitFragment.class.getName();
//  }
//
//  public void comfirmConfigs() {
//    if (swOrderCourse.isExpanded() && civOrderCourseTime.isEmpty()) {
//      ToastUtils.show("请填写课程预约限制时间");
//      return;
//    }
//    if (swCancle.isExpanded() && civCancelTime.isEmpty()) {
//      ToastUtils.show("请填写取消预约限制时间");
//      return;
//    }
//    if (swSignGroup.isExpanded() && (inputSignClassWay.isEmpty()
//        || inputSignClassStart.isEmpty()
//        || inputSignClassEnd.isEmpty())) {
//      ToastUtils.show("请将签课信息补充完整");
//      return;
//    }
//    if (isHaveUnData(inputSignClassStart.getContent()) || isHaveUnData(
//        inputSignClassEnd.getContent())) {
//      ToastUtils.show("请填写合法时间");
//      return;
//    }
//    List<ShopConfigBody.Config> data = new ArrayList<>();
//    data.add(new ShopConfigBody.Config(mOrderId, swOrderCourse.isExpanded() ? civOrderCourseTime.getContent() : "0"));
//    data.add(new ShopConfigBody.Config(mCancleId, swCancle.isExpanded() ? civCancelTime.getContent() : "0"));
//    data.add(new ShopConfigBody.Config(mSignOpenId, swSignGroup.isExpanded() ? "1" : "0"));
//    if (swSignGroup.isExpanded()){
//      data.add(new ShopConfigBody.Config(mSignWayId, getSignWayId(inputSignClassWay.getContent())));
//      data.add(new ShopConfigBody.Config(mSignStartId, inputSignClassStart.getContent()));
//      data.add(new ShopConfigBody.Config(mSignEndId, inputSignClassEnd.getContent()));
//    }
//    //        data.add(new ShopConfigBody.Config(ShopConfigs.TEAM_MINUTES_,swOrderCourse.isExpanded()?"1":"0"));
//    mOrderLimitPresenter.saveConfigs(new ShopConfigBody(data));
//    showLoading();
//  }
//
//  private boolean isHaveUnData(String content){
//    String p = "\\D{1,}";
//    Pattern pattern = Pattern.compile(p);
//    Matcher matcher = pattern.matcher(content);
//    return matcher.find();
//  }
//
//  //1表示教练点名， 2表示会员扫码
//  private String getSignWayId(String way){
//    switch (way){
//      case "教练点名":
//        return "1";
//      case "会员扫码":
//        return "2";
//    }
//    return "";
//  }
//
//  @Override public void onSuccess() {
//    hideLoading();
//    getActivity().onBackPressed();
//  }
//
//  @Override public void onCourseOrderLimit(SignInConfig.Config config) {
//    if (config != null) {
//      if (config.getValue() instanceof Double) {
//        swOrderCourse.setExpanded(config.getValueInt() > 0);
//        civOrderCourseTime.setContent(config.getValueInt() + "");
//        mOrderId = config.getId() + "";
//      }
//    }
//  }
//
//  @Override public void onCourseCancelLimit(SignInConfig.Config config) {
//    if (config != null) {
//      if (config.getValue() instanceof Double) {
//        swCancle.setExpanded(((Double) config.getValue()).intValue() > 0);
//        civCancelTime.setContent(((Double) config.getValue()).intValue() + "");
//        mCancleId = config.getId() + "";
//      }
//    }
//  }
//
//  @Override public void onCourseSubstituteLimit(SignInConfig.Config config) {
//
//  }
//
//  @Override public void onSaveOk() {
//
//  }
//
//  @Override public void onSignClassLimit(List<SignInConfig.Config> configs) {
//    if (configs.size() > 0) {
//      mSignOpenId = String.valueOf(configs.get(0).getId());
//      swSignGroup.setExpanded((Boolean) configs.get(0).getValue());
//
//      mSignWayId = String.valueOf(configs.get(1).getId());
//      inputSignClassWay.setContent(
//          ((Double) configs.get(1).getValue()).intValue() == 1 ? "教练点名" : "会员扫码");
//
//      mSignStartId = String.valueOf(configs.get(2).getId());
//      inputSignClassStart.setContent(((Double) configs.get(2).getValue()).intValue() + "");
//
//      mSignEndId = String.valueOf(configs.get(3).getId());
//      inputSignClassEnd.setContent(((Double) configs.get(3).getValue()).intValue() + "");
//    }
//  }
//
//  @Override public void onShowError(String e) {
//    hideLoading();
//    ToastUtils.show(e);
//  }
//
//  @Override public void onShowError(@StringRes int e) {
//    onShowError(getString(e));
//  }
//
//  @Override public void onDestroyView() {
//    super.onDestroyView();
//  }
//
//  @Override public void onComfirmClick(List<IFlexible> dats, List<Integer> selectedPos) {
//    if (dats.get(0) instanceof SimpleTextItemItem) {
//      inputSignClassWay.setContent(((SimpleTextItemItem)dats.get(0)).getData());
//    }
//  }
//}
