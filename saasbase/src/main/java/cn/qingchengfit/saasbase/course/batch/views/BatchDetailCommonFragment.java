package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasRouter;
import cn.qingchengfit.saasbase.routers.CourseUri;
import cn.qingchengfit.saasbase.routers.GymManageUri;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.widgets.ExpandedLayout;
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
 * Created by Paper on 2017/9/11.
 */
public class BatchDetailCommonFragment extends BaseFragment {

  @BindView(R2.id.img) ImageView img;
  @BindView(R2.id.img_layout) FrameLayout imgLayout;
  @BindView(R2.id.img_foot) ImageView imgFoot;
  @BindView(R2.id.text1) TextView text1;
  @BindView(R2.id.text3) TextView text3;
  @BindView(R2.id.righticon) ImageView righticon;
  @BindView(R2.id.course_layout) RelativeLayout courseLayout;
  @BindView(R2.id.coach) CommonInputView coach;
  @BindView(R2.id.space) CommonInputView space;
  @BindView(R2.id.order_sutdent_count) CommonInputView orderSutdentCount;
  @BindView(R2.id.pay_online) CommonInputView payOnline;
  @BindView(R2.id.pay_card) CommonInputView payCard;
  @BindView(R2.id.el_pay) ExpandedLayout elPay;
  @BindView(R2.id.el_multi_support) ExpandedLayout elMultiSupport;

  @Inject SaasRouter saasRouter;

  private Course course;
  private Staff trainer;

  public static BatchDetailCommonFragment newInstance(Course course, Staff trainer) {
    Bundle args = new Bundle();
    args.putParcelable("course",course);
    args.putParcelable("trainer",trainer);
    BatchDetailCommonFragment fragment = new BatchDetailCommonFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null){
      course = getArguments().getParcelable("course");
      trainer = getArguments().getParcelable("trainer");
      if (course == null) {
        course = new Course();
        course.is_private = true;
      }
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(course.is_private?R.layout.fragment_batch_detail_common_private:R.layout.fragment_batch_detail_common_group, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  /**
   * 设置课程信息
   */
  public void setCourse(Course course){
    if (text1 == null)
      return;
    if (course.is_private){
      coach.setContent(course.getName());
    }else {
      PhotoUtils.small(img, course.getPhoto());
      text1.setText(course.getName());
      text3.setText(getString(R.string.course_d_lenght, course.getLength() / 60));
    }
  }
  public String getCourseId(){
    if (course != null){
      return course.id;
    }else return "";
  }

  /**
   * 设置教练
   */
  public void setTrainer(Staff staff){
    if (coach == null)
      return;
    if (course.is_private){
      PhotoUtils.smallCircle(img,staff.getAvatar());
      text1.setText(staff.getUsername());
    }else {
      coach.setContent(staff.getUsername());
    }


  }
  public String getTrainerId(){
    if (trainer != null){
      return trainer.getId();
    }else return "";
  }

  /**
   * 设置可约人数
   */
  public void setOrderSutdentCount(@IntRange(from = 1) int x){
    if (orderSutdentCount != null)
      orderSutdentCount.setContent(Integer.toString(x));
  }

  public boolean mutilSupportble() {
    return elMultiSupport != null && elMultiSupport.isExpanded();
  }

  public boolean needPay() {
    return elPay == null || elPay.isExpanded();
  }
  public void openPay(boolean open){
    if (elPay != null)
      elPay.setExpanded(open);
  }

  public void openPayOnline(boolean open){
    if (open)
      payOnline.setContent("已开启");
    else payOnline.setContent("未设置");
  }


  /**
   * 获取课预约人数
   */
  @IntRange(from = 1)
  public int getOrderStudentCount(){
    if (orderSutdentCount != null){
      try{
        return Integer.parseInt(orderSutdentCount.getContent());
      }catch (Exception e){
        return 8;
      }
    }else return 8;
  }




  @Override public String getFragmentName() {
    return BatchDetailCommonFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 更改课程
   */
  @OnClick(R2.id.course_layout) public void onCourseLayoutClicked() {
    if (course.is_private)
      saasRouter.choose( CourseUri.COURSE_TYPE_GROUP_LIST);
    else saasRouter.choose(GymManageUri.TRAINER_LIST);
  }

  /**
   * 更改教练
   */
  @OnClick(R2.id.coach) public void onCoachClicked() {
    if (AppUtils.getCurApp(getContext()) == 0)
      return;
    if (course.is_private)
      saasRouter.choose( CourseUri.COURSE_TYPE_PRIVATE_LIST);
    else saasRouter.choose(GymManageUri.TRAINER_LIST);
  }

  /**
   * 更改场地
   */
  @OnClick(R2.id.space) public void onSpaceClicked() {
    saasRouter.choose(GymManageUri.SITE_LIST+"?muti="+(course.is_private?1:0));
  }

  /**
   * 更改上课人数
   */
  @OnClick(R2.id.order_sutdent_count) public void onOrderSutdentCountClicked() {
    new DialogList(getContext()).list(
        course.is_private? CmStringUtils.getNums(1, 10) : CmStringUtils.getNums(1, 300),
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int before = 8;
            try {
              before = Integer.parseInt(orderSutdentCount.getContent());
            }catch (Exception e){
              LogUtil.e("排课时上课人数不为int类型");
            }
            if (before != (position+1)) {
              orderSutdentCount.setContent(Integer.toString(position + 1));
              if (course.is_private) payCard.setHint("已修改可约人数，请重新设置");
            }

          }
        }).title("选择人数").show();
  }

  /**
   * 在线支付
   */
  @OnClick(R2.id.pay_online) public void onPayOnlineClicked() {
    saasRouter.choose(CourseUri.PAY_ONLINE);
  }

  /**
   * 卡支付设置
   */
  @OnClick(R2.id.pay_card) public void onPayCardClicked() {
    saasRouter.choose(CourseUri.PAY_CARDS);
  }

  @Override public boolean isBlockTouch() {
    return false;
  }
}
