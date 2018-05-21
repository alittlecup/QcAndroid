package cn.qingchengfit.staffkit.train.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.train.model.GroupBean;
import cn.qingchengfit.staffkit.train.model.SignPersonalBean;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.AutoLineGroup;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/3/22.
 */

public class SignUpFormPersonalItem
    extends AbstractFlexibleItem<SignUpFormPersonalItem.PersonalVH> {

  private SignPersonalBean datas;
  private Context context;

  public SignUpFormPersonalItem(Context context, SignPersonalBean datas) {
    this.context = context;
    this.datas = datas;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public SignPersonalBean getData() {
    return datas;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_sign_up;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, PersonalVH holder, int position,
      List payloads) {

    Glide.with(holder.itemView.getContext())
        .load(PhotoUtils.getSmall(datas.avatar()))
        .asBitmap()
        .placeholder(datas.gender().equals("0") ? R.drawable.default_manage_male
            : R.drawable.default_manager_female)
        .error(R.drawable.default_manage_male)
        .into(new CircleImgWrapper(holder.circleImg, holder.itemView.getContext()));

    holder.textSignName.setText(datas.username());
    holder.imgSignGender.setImageDrawable(
        datas.gender().equals("0") ? ContextCompat.getDrawable(context,
            R.drawable.ic_gender_signal_male)
            : ContextCompat.getDrawable(context, R.drawable.ic_gender_signal_female));
    holder.textSignPhone.setText(datas.phone());
    holder.textSignTime.setText(context.getString(R.string.sign_up_personal_time,
        DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(datas.created_at))));
    holder.textSignCost.setText(context.getString(R.string.sign_up_personal_cost,
        datas.order == null ? "0" : datas.order.getPrice()));

    holder.llSignGroup.removeAllViews();

    for (GroupBean bean : datas.teams) {
      TextView textView = new TextView(holder.itemView.getContext());
      holder.icformGroup.setBounds(0, 0, holder.icformGroup.getMinimumWidth(),
          holder.icformGroup.getMinimumHeight());
      textView.setPadding(MeasureUtils.dpToPx(8f, holder.itemView.getContext().getResources()), 6,
          MeasureUtils.dpToPx(8f, holder.itemView.getContext().getResources()), 6);
      textView.setCompoundDrawables(holder.icformGroup, null, null, null);
      textView.setCompoundDrawablePadding(
          MeasureUtils.dpToPx(8f, holder.itemView.getContext().getResources()));
      textView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(),
          R.drawable.bg_border_cornor_grey));
      textView.setText(bean.name);
      textView.setTextSize(12);
      holder.llSignGroup.addView(textView);
    }
  }

  @Override public PersonalVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new PersonalVH(view, adapter);
  }

  class PersonalVH extends FlexibleViewHolder {

    ImageView circleImg;
    TextView textSignName;
    ImageView imgSignGender;
    TextView textSignPhone;
    TextView textSignTime;
    TextView textSignCost;
    AutoLineGroup llSignGroup;
    Drawable icformGroup;

    public PersonalVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      icformGroup = view.getResources().getDrawable(R.drawable.ic_form_group);
      circleImg = (ImageView) view.findViewById(R.id.image_sign_head);
      textSignName = (TextView) view.findViewById(R.id.text_sign_name);
      imgSignGender = (ImageView) view.findViewById(R.id.img_sign_gender);
      textSignPhone = (TextView) view.findViewById(R.id.text_sign_phone);
      textSignTime = (TextView) view.findViewById(R.id.text_sign_time);
      textSignCost = (TextView) view.findViewById(R.id.text_sign_cost);
      llSignGroup = (AutoLineGroup) view.findViewById(R.id.ll_sign_group);

      llSignGroup.setSpacing(16, 10);
    }
  }
}
