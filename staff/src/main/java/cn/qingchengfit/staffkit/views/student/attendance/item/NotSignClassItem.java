package cn.qingchengfit.staffkit.views.student.attendance.item;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.attendance.model.NotSignStudent;
import cn.qingchengfit.utils.CircleImgWrapper;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/9/18.
 */

public class NotSignClassItem extends AbstractFlexibleItem<NotSignClassItem.NotSignVH>{

  NotSignStudent student;

  public NotSignClassItem(NotSignStudent student) {
    this.student = student;
  }

  public NotSignStudent getData() {
    return student;
  }

  @Override public NotSignVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    NotSignVH holder = new NotSignVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    holder.btnContactHim.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });
    return holder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, NotSignVH holder, int position,
      List payloads) {
    Glide.with(holder.itemView.getContext())
        .load(student.avatar)
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(holder.imgNotSignHead, holder.itemView.getContext()));
    holder.textNotSignClassCount.setText(String.valueOf(student.count));
    holder.textNotSignClassName.setText(student.username);
    holder.textNotSignPhone.setText(student.phone);
    holder.imageNotSignGender.setImageResource(
        student.gender == 1 ? R.drawable.ic_gender_signal_female
            : R.drawable.ic_gender_signal_male);
  }

  @Override public boolean equals(Object o) {
    return true;
  }


  @Override public int getLayoutRes() {
    return R.layout.item_not_sign_class;
  }

  class NotSignVH extends FlexibleViewHolder {
    @BindView(R.id.title_not_sign) TextView titleNotSign;
    @BindView(R.id.text_not_sign_class_count) TextView textNotSignClassCount;
    @BindView(R.id.text_not_sign_class_name) TextView textNotSignClassName;
    @BindView(R.id.image_not_sign_gender) ImageView imageNotSignGender;
    @BindView(R.id.text_not_sign_phone) TextView textNotSignPhone;
    @BindView(R.id.btn_contact_him) TextView btnContactHim;
    @BindView(R.id.img_not_sign_head) ImageView imgNotSignHead;

    public NotSignVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}
