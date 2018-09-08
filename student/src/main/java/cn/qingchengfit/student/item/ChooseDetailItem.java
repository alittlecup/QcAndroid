package cn.qingchengfit.student.item;

import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.saascommon.item.IItemData;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.databinding.ItemChooseDetailBinding;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class ChooseDetailItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemChooseDetailBinding>>
    implements ISectionable<DataBindingViewHolder<ItemChooseDetailBinding>, IHeader>, IItemData,
    IFilterable {

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
    return o instanceof ChooseDetailItem && ((ChooseDetailItem) o).getData()
        .getId()
        .equalsIgnoreCase(getData().getId());
  }

  @Override public int getLayoutRes() {
    return R.layout.item_choose_detail;
  }

  @Override public DataBindingViewHolder<ItemChooseDetailBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    DataBindingViewHolder<ItemChooseDetailBinding> bindingViewHolder =
        new DataBindingViewHolder<>(view, adapter);
    View.OnClickListener listener = v -> {
      if (v.getId() == R.id.btn_contact_him) {
        if (adapter instanceof CommonFlexAdapter) {
          ((CommonFlexAdapter) adapter).setTag("contact", true);
        }
      } else {
        if (adapter instanceof CommonFlexAdapter) {
          ((CommonFlexAdapter) adapter).setTag("contact", false);
        }
      }
      bindingViewHolder.onClick(v);
    };
    bindingViewHolder.getContentView().setOnClickListener(listener);
    bindingViewHolder.getDataBinding().btnContactHim.setOnClickListener(listener);

    return bindingViewHolder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemChooseDetailBinding> holder, int position, List payloads) {
    holder.itemView.setTag(data);
    boolean showSelected = false;
    boolean showBase = false;
    if (adapter instanceof CommonFlexAdapter) {
      Integer choose = (Integer) ((CommonFlexAdapter) adapter).getTag("choose");
      if (choose != null) {
        type = choose;
      }
      Boolean selected = (Boolean) ((CommonFlexAdapter) adapter).getTag("selected");
      if (selected != null) {
        showSelected = selected;
      }
      Boolean based = (Boolean) ((CommonFlexAdapter) adapter).getTag("showBase");
      if (based != null) {
        showBase = based;
      }
    }
    ItemChooseDetailBinding binding = holder.getDataBinding();
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

    binding.tvStudentStatus.setText(binding.tvStudentStatus.getContext()
        .getResources()
        .getStringArray(cn.qingchengfit.saascommon.R.array.student_status)[data.getStatus() % 3]);
    binding.tvStudentStatus.setCompoundDrawablePadding(10);
    binding.tvStudentStatus.setCompoundDrawables(
        StudentBusinessUtils.getStudentStatusDrawable(holder.itemView.getContext(),
            data.getStatus() % 3), null, null, null);
    if (showBase) {//这里与UNDEFINE 相同处理是因为UNDEFINE 在外部代表了群发短信的样式， 但是有时要求分配销售和教练也不显示多余信息，用处在StudentAllPage
      binding.tvStudentDesc.setVisibility(View.GONE);
      binding.itemPersonDesc.setVisibility(View.GONE);
      binding.btnContactHim.setVisibility(View.GONE);
    } else if (type == Mode.UNDEFINE) {
      binding.tvStudentDesc.setVisibility(View.GONE);
      binding.itemPersonDesc.setVisibility(View.GONE);
      binding.btnContactHim.setVisibility(View.GONE);
    } else if (type == Mode.SALLER) {
      List<String> sellerNames = new ArrayList<>();
      if (data.sellers != null && !data.sellers.isEmpty()) {
        for (Staff seller : data.sellers) {
          sellerNames.add(seller.username);
        }
      }
      binding.itemPersonDesc.setText(new StringBuilder().append("销售：")
          .append(StringUtils.List2StrWithChineseSplit(sellerNames))
          .toString());
      binding.itemPersonDesc.setVisibility(View.VISIBLE);
      binding.tvStudentDesc.setVisibility(View.GONE);
      binding.btnContactHim.setVisibility(View.GONE);
    } else if (type == Mode.TRAINER) {
      List<String> sellerNames2 = new ArrayList<>();
      if (data.coaches != null && !data.coaches.isEmpty()) {
        for (Staff saler : data.coaches) {
          sellerNames2.add(saler.username);
        }
      }
      binding.itemPersonDesc.setText(new StringBuilder().append("教练：")
          .append(StringUtils.List2StrWithChineseSplit(sellerNames2))
          .toString());
      binding.itemPersonDesc.setVisibility(View.VISIBLE);
      binding.tvStudentDesc.setVisibility(View.GONE);
      binding.btnContactHim.setVisibility(View.GONE);
    } else if (type == Mode.BOTH) {
      List<String> sellerNames = new ArrayList<>();
      if (data.sellers != null && !data.sellers.isEmpty()) {
        for (Staff seller : data.sellers) {
          sellerNames.add(seller.username);
        }
      }
      List<String> sellerNames2 = new ArrayList<>();
      if (data.coaches != null && !data.coaches.isEmpty()) {
        for (Staff saler : data.coaches) {
          sellerNames2.add(saler.username);
        }
      }
      binding.itemPersonDesc.setText(new StringBuilder().append("销售：")
          .append(StringUtils.List2StrWithChineseSplit(sellerNames))
          .toString());
      binding.itemPersonCoach.setText(new StringBuilder().append("教练：")
          .append(StringUtils.List2StrWithChineseSplit(sellerNames2))
          .toString());
      binding.itemPersonDesc.setVisibility(View.VISIBLE);
      binding.itemPersonCoach.setVisibility(View.VISIBLE);
      binding.tvStudentDesc.setVisibility(View.GONE);
      binding.btnContactHim.setVisibility(View.GONE);
    } else if (type == Mode.MORE_INFO) {
      List<String> sellerNames = new ArrayList<>();
      if (data.sellers != null && !data.sellers.isEmpty()) {
        for (Staff seller : data.sellers) {
          sellerNames.add(seller.username);
        }
      }
      binding.itemPersonDesc.setText(new StringBuilder().append("销售：")
          .append(StringUtils.List2StrWithChineseSplit(sellerNames))
          .toString());

      binding.itemPersonDesc.setVisibility(View.VISIBLE);
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
              .append(TextUtils.isEmpty(data.joined_at) ? ""
                  : DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(data.joined_at)))
              .toString();
          break;
        case 1:
          desc = new StringBuilder().append("最新跟进：")
              .append(TextUtils.isEmpty(data.track_record) ? "" : data.track_record)
              .append("\n跟进状态：")
              .append(TextUtils.isEmpty(data.track_status) ? "" : data.track_status)
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
      if ((status == 2 && TextUtils.isEmpty(data.first_card_info)) || TextUtils.isEmpty(desc)) {
        binding.tvStudentDesc.setVisibility(View.GONE);
        binding.btnContactHim.setVisibility(View.GONE);
      } else {
        binding.tvStudentDesc.setVisibility(View.VISIBLE);
        binding.tvStudentDesc.setText(desc);
        binding.btnContactHim.setVisibility(View.VISIBLE);
      }
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

  @Override public boolean filter(String constraint) {
    return data.filter(constraint);
  }

  @IntDef({
      Mode.UNDEFINE, Mode.SALLER, Mode.TRAINER, Mode.MORE_INFO, Mode.BOTH
  }) @Retention(RetentionPolicy.RUNTIME) public @interface Mode {
    int UNDEFINE = -1;
    int SALLER = 0;
    int TRAINER = 1;
    int MORE_INFO = 2;
    int BOTH = 3;
  }

  private @Mode int type = Mode.UNDEFINE;
}
