package cn.qingchengfit.student.item;

import android.databinding.ViewDataBinding;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.saascommon.item.IItemData;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.databinding.ItemStudentFollowUpStateBinding;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChooseDetailItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemStudentFollowUpStateBinding>>
    implements ISectionable<DataBindingViewHolder<ItemStudentFollowUpStateBinding>, IHeader>,
    IItemData {

  public QcStudentBeanWithFollow data;
  public int status;

  public ChooseDetailItem(QcStudentBeanWithFollow data, Integer status) {
    this.data = data;
    this.status = status;
  }

  public QcStudentBeanWithFollow getData() {
    return data;
  }

  public void setData(QcStudentBeanWithFollow data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_student_follow_up_state;
  }

  @Override
  public DataBindingViewHolder<ItemStudentFollowUpStateBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {

    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemStudentFollowUpStateBinding> holder, int position, List payloads) {
    holder.itemView.setTag(data);
    boolean showSelected = false;
    boolean noInfo = false;
    if (adapter instanceof CommonFlexAdapter) {
      Integer choose = (Integer) ((CommonFlexAdapter) adapter).getTag("choose");
      if (choose != null) {
        type = choose;
      }
      Boolean selected = (Boolean) ((CommonFlexAdapter) adapter).getTag("selected");
      Boolean noInfo1 = (Boolean) ((CommonFlexAdapter) adapter).getTag("noInfo");
      if (selected != null) {
        showSelected = selected;
      }
      if (noInfo1 != null) {
        noInfo = noInfo1;
      }
    }
    ItemStudentFollowUpStateBinding binding = holder.getDataBinding();
    binding.cb.setClickable(false);
    binding.cb.setChecked(adapter.isSelected(position));

    PhotoUtils.smallCircle(binding.itemPersonHeader, data.avatar);

    //姓名
    binding.itemPersonName.setText(data.username);
    //性别
    if (0 == data.gender) { // 男
      binding.itemPersonGender.setImageResource(R.drawable.ic_gender_signal_male);
    } else {
      binding.itemPersonGender.setImageResource(R.drawable.ic_gender_signal_female);
    }

    //电话
    binding.itemPersonPhonenum.setText(
        new StringBuilder().append("手机：").append(data.phone).toString());
    if (!noInfo) {
      //if (type == Mode.UNDEFINE || type == Mode.SALLER) {
      //销售
      List<String> sellerNames = new ArrayList<>();
      if (data.sellers != null && !data.sellers.isEmpty()) {
        for (Staff seller : data.sellers) {
          sellerNames.add(seller.username);
        }
      }
      binding.itemPersonDesc.setText(new StringBuilder().append("销售：")
          .append(StringUtils.List2StrWithChineseSplit(sellerNames)).toString());
      //} else if (type == Mode.TRAINER) {
      //教练
      // TODO: 2018/7/13 这里字段有问题
      List<String> sellerNames2 = new ArrayList<>();
      if (data.coaches != null && !data.coaches.isEmpty()) {
        for (Staff saler : data.coaches) {
          sellerNames2.add(saler.username);
        }
      }
      binding.itemPersonCoach.setText(
          new StringBuilder()
          .append("教练：")
          .append(StringUtils.List2StrWithChineseSplit(sellerNames2))
          .toString());
      //}
    }else{
      binding.itemPersonCoach.setVisibility(View.GONE);
      binding.itemPersonDesc.setVisibility(View.GONE);
    }

    binding.tvStudentStatus.setText(binding.tvStudentStatus.getContext()
        .getResources()
        .getStringArray(cn.qingchengfit.saascommon.R.array.student_status)[data.getStatus() % 3]);
    binding.tvStudentStatus.setCompoundDrawablePadding(10);
    binding.tvStudentStatus.setCompoundDrawables(
        StudentBusinessUtils.getStudentStatusDrawable(holder.itemView.getContext(),
            data.getStatus() % 3), null, null, null);

    if (type == Mode.UNDEFINE) {
      String desc = "";
      switch (status) {
        case 0:
          desc = new StringBuilder().append("来源：")
              .append(TextUtils.isEmpty(data.origin) ? "" : data.origin)
              .append("\n推荐人：")
              .append(
                  data.recommend_by == null || TextUtils.isEmpty(data.recommend_by.username) ? ""
                      : data.recommend_by.username)
              .append("\n注册时间：")
              .append(TextUtils.isEmpty(data.joined_at) ? "" : data.joined_at.split("T")[0])
              .toString();
          break;
        case 1:
          desc = new StringBuilder().append("最新跟进：")
              .append(TextUtils.isEmpty(data.track_record) ? "" : data.track_record)
              .append("\n跟进状态：")
              .append(TextUtils.isEmpty(data.track_status) ? "" : data.track_status)
              .append("\n跟进时间：")
              .append(TextUtils.isEmpty(data.track_at) ? ""
                  : DateUtils.getYYYYMMDDfromServer(data.track_at))
              .toString();
          break;
        case 2:
          if (!TextUtils.isEmpty(data.first_card_info)) {
            desc = new StringBuilder().append("首次购卡：").append(data.first_card_info).toString();
          }
          break;
        case 4://会员转化
          // REFACTOR: 2017/11/13 会员转换的item中销售的字段重复
          StringBuilder sbStrans = new StringBuilder().append("销售：");
          if (data.sellers != null && data.sellers.size() > 0) {
            for (Staff staff : data.sellers) {
              sbStrans.append(staff.getUsername()).append(" ");
            }
          }
          sbStrans.append("\n注册时间：").append(DateUtils.getYYYYMMDDfromServer(data.joined_at));
          desc = sbStrans.toString();
          break;
      }
      if (status == 2 && TextUtils.isEmpty(data.first_card_info)) {
        binding.tvStudentDesc.setVisibility(View.GONE);
      } else {
        binding.tvStudentDesc.setVisibility(View.VISIBLE);
      }
      if (TextUtils.isEmpty(desc)) {
        binding.tvStudentDesc.setVisibility(View.GONE);
      } else {
        binding.tvStudentDesc.setText(desc);
      }
    } else {
      binding.tvStudentDesc.setVisibility(View.GONE);
    }
    if (showSelected) {
      binding.cb.setVisibility(View.VISIBLE);
      binding.cbSpace.setVisibility(View.VISIBLE);
    } else {
      binding.cb.setVisibility(View.GONE);
      binding.cbSpace.setVisibility(View.GONE);
    }
  }

  IHeader head;

  public void setSelectMode(@Mode int type) {
    this.type = type;
  }

  @Override public IHeader getHeader() {
    return head;
  }

  @Override public void setHeader(IHeader header) {
    this.head = header;
  }

  @IntDef({
      Mode.UNDEFINE, Mode.SALLER, Mode.TRAINER
  }) @Retention(RetentionPolicy.RUNTIME) public @interface Mode {
    int UNDEFINE = -1;
    int SALLER = 0;
    int TRAINER = 1;
  }

  private @Mode int type = Mode.UNDEFINE;
}
