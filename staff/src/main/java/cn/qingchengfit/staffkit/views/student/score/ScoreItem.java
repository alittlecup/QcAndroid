package cn.qingchengfit.staffkit.views.student.score;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.StringUtils;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/22.
 */

public class ScoreItem extends AbstractFlexibleItem<ScoreItem.ItemVH> {

    private Student data;

    public ScoreItem(Student data) {
        this.data = data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_score;
    }

    @Override public ItemVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ItemVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ItemVH holder, int position, List payloads) {

        StringUtils.studentScorePosition(holder.tvItemStudentPosition, position);
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(data.avatar))
            .asBitmap()
            .placeholder(data.gender == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female)
            .error(data.gender == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female)
            .into(new CircleImgWrapper(holder.itemPersonHeader, holder.itemView.getContext()));
        //holder.itemPersonHeaderLoop.setBackgroundDrawable(new LoopView("#00000000"));
        holder.itemPersonName.setText(data.username);
        holder.itemPersonPhonenum.setText(data.phone);
        holder.itemPersonDesc.setVisibility(View.GONE);

        if (0 == data.gender) {//男
            holder.itemPersonGender.setImageResource(R.drawable.ic_gender_signal_male);
        } else {
            holder.itemPersonGender.setImageResource(R.drawable.ic_gender_signal_female);
        }

        holder.tvItemStudentScore.setText(data.score);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public static class ItemVH extends FlexibleViewHolder {
        @BindView(R.id.item_person_header) ImageView itemPersonHeader;
        //@BindView(R.id.item_person_header_loop) RelativeLayout itemPersonHeaderLoop;
        @BindView(R.id.item_person_name) TextView itemPersonName;
        @BindView(R.id.item_person_gender) ImageView itemPersonGender;
        @BindView(R.id.item_person_phonenum) TextView itemPersonPhonenum;
        @BindView(R.id.item_person_desc) TextView itemPersonDesc;
        @BindView(R.id.tv_item_student_score) TextView tvItemStudentScore;
        @BindView(R.id.tv_item_student_position) TextView tvItemStudentPosition;

        public ItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
