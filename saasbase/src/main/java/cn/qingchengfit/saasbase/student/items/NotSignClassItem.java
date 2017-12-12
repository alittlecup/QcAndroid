package cn.qingchengfit.saasbase.student.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.student.bean.StudentWIthCount;
import cn.qingchengfit.utils.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by fb on 2017/9/18.
 */

public class NotSignClassItem extends AbstractFlexibleItem<NotSignClassItem.NotSignVH> {

    private StudentWIthCount student;

    public NotSignClassItem(StudentWIthCount student) {
        this.student = student;
    }

    public StudentWIthCount getData() {
        return student;
    }

    @Override
    public NotSignVH createViewHolder(View view, FlexibleAdapter adapter) {
        NotSignVH holder = new NotSignVH(view, adapter);
        holder.btnContactHim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return holder;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, NotSignVH holder, int position,
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

    @Override
    public boolean equals(Object o) {
        return true;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.item_not_sign_class;
    }

    class NotSignVH extends FlexibleViewHolder {
        @BindView(R2.id.title_not_sign)
        TextView titleNotSign;
        @BindView(R2.id.text_not_sign_class_count)
        TextView textNotSignClassCount;
        @BindView(R2.id.text_not_sign_class_name)
        TextView textNotSignClassName;
        @BindView(R2.id.image_not_sign_gender)
        ImageView imageNotSignGender;
        @BindView(R2.id.text_not_sign_phone)
        TextView textNotSignPhone;
        @BindView(R2.id.btn_contact_him)
        TextView btnContactHim;
        @BindView(R2.id.img_not_sign_head)
        ImageView imgNotSignHead;

        public NotSignVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }


}
